package com.jmz;

import java.math.BigDecimal;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jmz.bean.AddressBean;
import com.jmz.bean.MyOrderInfo;
import com.jmz.bean.MyOrderInfoList;
import com.jmz.bean.OrderList;
import com.jmz.bean.Product;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnBalancePayListener;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.OrderState;
import com.jmz.uitl.PayUtile;
import com.jmz.uitl.Utile;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * 订单预览
 * 
 * @author Administrator
 * 
 */
public class TradeActivity extends ParentActivity implements OnBalancePayListener{
	protected static final String LOGTAG = "TradeActivity";
	public static final String MYORDERINFO = "myorderinfo";
	private TextView orderQmfBtn;
	private TextView shopName;
	private TextView productTitle;
	private TextView totalPayMoneyTv;
	private TextView productNum;
	private TextView fareMoney;
	private TextView orderNumber;
	private TextView orderTimeTv;
	private TextView bottomPrice;
	private PayUtile payUtile;
	private HashMap<String, String> oneOrderMap;
	private SubmitTask oneOrderTask;
	private TextView userPayMoneyTv;
	private MyOrderInfo myOrderInfo;
	private TextView addressString;
	private String fare;
	private String orderId;
	private double userPayMoney;
	private TextView productAttr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadiagle();
		setTittleText(R.string.order_title);
		fare = getIntent().getStringExtra(OrderActivity.FARE);
		orderId = getIntent().getStringExtra(OrderActivity.ORDERID);
		userPayMoney = getIntent().getDoubleExtra(OrderActivity.USERPAYMONEY,
				0f);
		mainSubmit();
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
//		if (payUtile != null && payUtile.isFinish()) {
//			gotoPayResult();
//			payUtile.setFinish(false);
//		}
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			gotoPayResult();
		}
	}

	/*
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(TradeActivity.this).inflate(
				R.layout.activity_trade_center, null));
		initbottomView(LayoutInflater.from(TradeActivity.this).inflate(
				R.layout.activity_trade_bottom, null));
		shopName = (TextView) findViewById(R.id.trade_center_orderbusshop);
		productTitle = (TextView) findViewById(R.id.trade_center_producttittle);
		productAttr = (TextView) findViewById(R.id.trade_center_productattr);
		totalPayMoneyTv = (TextView) findViewById(R.id.trade_center_productallmoney);
		userPayMoneyTv = (TextView) findViewById(R.id.trade_center_productuserpay);
		productNum = (TextView) findViewById(R.id.trade_center_productnumber);
		fareMoney = (TextView) findViewById(R.id.trade_center_productexpmoney);
		addressString = (TextView) findViewById(R.id.trade_center_adress);
		orderNumber = (TextView) findViewById(R.id.trade_center_ordernumber);
		orderTimeTv = (TextView) findViewById(R.id.trade_center_ordertime);
		orderQmfBtn = (TextView) findViewById(R.id.trade_bottom_surepay);
		bottomPrice = (TextView) findViewById(R.id.trade_bottom_price);
	}

	/**
	 * 为组件赋值
	 */
	private void setDataToView() {
		productTitle.setText(myOrderInfo.getTitle());
		productAttr.setText(myOrderInfo.getOrderProductList().get(0)
				.getProductAttribute());
		productNum.setText(myOrderInfo.getProductNmber().toString());
		fareMoney.setText(getString(R.string.yuanicon) + fare);
		totalPayMoneyTv.setText(getString(R.string.yuanicon)
				+ myOrderInfo.getOrderAmount());
		userPayMoneyTv.setText(getString(R.string.yuanicon) + userPayMoney);
		addressString.setText(myOrderInfo.getExpressString());
		orderNumber.setText(orderId);
		orderTimeTv.setText(myOrderInfo.getOrderDate());
		BigDecimal userPayDecimal = new BigDecimal(userPayMoney);
		BigDecimal allDecimal = new BigDecimal(myOrderInfo.getOrderAmount());
		bottomPrice.setText(getString(R.string.yuanicon)
				+ allDecimal.subtract(userPayDecimal).toString());
		orderQmfBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap<String, String> tradeMap = new HashMap<String, String>();
				tradeMap.put(Config.TAG_TITLE, myOrderInfo.getTitle());
				tradeMap.put(Config.TAG_TRADETYPE, Config.TRADE_TYPE_GOUWU);
				tradeMap.put(Config.TAG_ORDERIDS, orderId);
				if (Utile.isEqual(myOrderInfo.getOrderStatus(),
						OrderState.WaitBuyerPay.name())) {
					payUtile = new PayUtile(TradeActivity.this, tradeMap,TradeActivity.this);
					payUtile.tradeSubmit();
				}
			}
		});

	}
	
	/**
	 * 跑去支付结果
	 */
	private void gotoPayResult() {
		Intent intent = new Intent(this, TradeResultActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(MYORDERINFO, myOrderInfo);
		intent.putExtras(bundle);
		startActivity(intent);
		this.finish();
	}

	/**
	 * 查询该笔订单状态
	 */
	@Override
	protected void mainSubmit() {
		oneOrderMap = new HashMap<String, String>();
		oneOrderMap.put(Config.TAG_ORDERID, orderId);
		oneOrderMap.put(Config.TAG_TYPE, "User");
		oneOrderTask = new SubmitTask(this, Config.ONE_ORDER_INFO,
				MyOrderInfoList.class, new onOneOrderInfoSubmitListener(), true);
		oneOrderTask.execute(oneOrderMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (payUtile != null) {
			payUtile.destroy();
		}
		if (oneOrderTask != null) {
			oneOrderTask.destorySelf();
		}
	}

	/**
	 * 响应获取订单信息
	 * 
	 * @author Administrator
	 * 
	 */
	private class onOneOrderInfoSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			MyOrderInfoList infoList = (MyOrderInfoList) result.getObject();
			if (infoList != null && !infoList.getOrderInfo().isEmpty()) {
				myOrderInfo = infoList.getOrderInfo().get(0);
				initMyView();
				if (Utile.isEqual(myOrderInfo.getOrderStatus(),
						OrderState.WaitSellerSendGoods.name())) {
					TradeActivity.this.finish();
					gotoPayResult();
				} else {
					setDataToView();
				}
			} else {
				TradeActivity.this.finish();
				showToast("提交失败！");
			}
		}

	}

	@Override
	public void onBalancePaySuccess() {
		
	}

	@Override
	public void onBalancePayFail() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void WXPaySingeSuccess(PayReq req) {
		// TODO Auto-generated method stub
		sendPayReq(req);
	}

}
