package com.jmz;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmz.adapter.MyOrderProductInfoAdapter;
import com.jmz.bean.MyOrderInfo;
import com.jmz.bean.MyOrderInfoList;
import com.jmz.bean.MyOrderShop;
import com.jmz.bean.OrderProduct;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnBalancePayListener;
import com.jmz.impl.OnDeleteUtileListener;
import com.jmz.impl.OnSubmitListener;
import com.jmz.impl.OnUpdataUtileListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.DeleteOrderUtile;
import com.jmz.uitl.Logger;
import com.jmz.uitl.OrderState;
import com.jmz.uitl.PayUtile;
import com.jmz.uitl.UpdateOrderStateUtile;
import com.jmz.uitl.Utile;
import com.jmz.view.MyListView;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * 单个订单详情
 * 
 * @author Administrator
 * 
 */
public class MyOrderInfoActivity extends ParentActivity implements
		OnDeleteUtileListener, OnUpdataUtileListener ,OnBalancePayListener{
	public static final String LOGTAG = "MyOrderInfoActivity";
	public static final int TAG_REFUND = 3;
	public static final int TAG_SURE = 5;
	public static final int TAG_DELETE = 4;
	private TextView refundBtn, orderPrice, address, orderNumber, orderTime,
			expprice, orderMoney, deleteBtn;
	private MyOrderShop myOrderShop;
	private String orderId;
	private MyOrderInfoList infoList;
	private List<OrderProduct> orderProducts;
	private MyOrderInfo myOrderInfo;
	private ImageView qrc;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1 && msg.obj != null) {
				qrc.setImageBitmap((Bitmap) msg.obj);
				msg.what = 0;
			}
		};
	};
	private TextView orderState;
	private MyListView listView;
	private PayUtile payUtile;
	private DeleteOrderUtile deleteOrderUtile;
	private UpdateOrderStateUtile updateOrderStateUtile;
	private MyOrderProductInfoAdapter adapter;
	private SubmitTask myOrderInfoTask;
	private boolean isShowDialog = false;
	private TextView postMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(R.string.myorderinfo_tittle);
		initLoadiagle();
		myOrderShop = (MyOrderShop) getIntent().getExtras().getSerializable(
				MyOrderActivity.TAG_MYORDERSHOP);
		orderId = myOrderShop.getOrderID();
		mainSubmit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null && data.getExtras() != null) {
			if (requestCode == TAG_REFUND && resultCode == TAG_REFUND) {
				isShowDialog = true;
				mainSubmit();
			}
		}
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(MyOrderInfoActivity.this).inflate(
				R.layout.activity_myorderinfo_center, null));
		orderPrice = (TextView) findViewById(R.id.myorderinfo_center_price);
		orderMoney = (TextView) findViewById(R.id.myorderinfo_center_ordermoney);
		address = (TextView) findViewById(R.id.myorderinfo_center_address);
		orderNumber = (TextView) findViewById(R.id.myorderinfo_center_ordernumber);
		orderTime = (TextView) findViewById(R.id.myorderinfo_center_ordertime);
		expprice = (TextView) findViewById(R.id.myorderinfo_center_expprice);
		postMsg = (TextView) findViewById(R.id.myorderinfo_center_postmsg);
		deleteBtn = (TextView) findViewById(R.id.myorderinfo_center_delete);
		deleteBtn.setOnClickListener(new OnMyOrderInfoClickLister());
		refundBtn = (TextView) findViewById(R.id.myorderinfo_center_refundstate);
		refundBtn.setOnClickListener(new OnMyOrderInfoClickLister());
		listView = (MyListView) findViewById(R.id.myorderinfo_center_list);
		orderState = (TextView) findViewById(R.id.myorderinfo_center_orderstate);
		setDataToView();
	}

	/**
	 * 为组件赋值
	 */
	private void setDataToView() {
		orderState.setOnClickListener(new OnMyOrderInfoClickLister());
		orderProducts = myOrderInfo.getOrderProductList();
		adapter = new MyOrderProductInfoAdapter(MyOrderInfoActivity.this,
				orderProducts);
		listView.setAdapter(adapter);
		orderMoney.setText(getString(R.string.yuanicon)
				+ myOrderInfo.getOrderAmount());
		orderPrice.setText(getString(R.string.yuanicon)
				+ myOrderInfo.getShopMoney());
		expprice.setText(getString(R.string.yuanicon)
				+ new BigDecimal(myOrderInfo.getOrderAmount())
						.subtract(myOrderInfo.getShopMoney()));
		orderNumber.setText(myOrderInfo.getOrderID());
		orderTime.setText(myOrderInfo.getOrderDate());
		address.setText(myOrderInfo.getExpressString());
		postMsg.setText(myOrderInfo.getPostscript());
		
		showBtn();

		// 返回购买商品的详情
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MyOrderInfoActivity.this,
						ProductActivity.class);
				intent.putExtra(Config.TAG_PUODUCTID,
						orderProducts.get(position).getProductID());
				startActivity(intent);
			}
		});
	}

	/**
	 * 根据情况显示按钮的不同情况
	 */
	private void showBtn() {

		// myOrderShop.setOrderStatus(Config.TRADECLOSEDBYSYSTEM);
		// myOrderShop.setRefundStatus(Config.WAITSELLERCONFIRMGOODS);
		// 先赋值和设置可不可用
		if (Utile
				.isEqual(myOrderInfo.getRefundStatus(), OrderState.None.name())) {
			orderState.setText(OrderState.valueOf(myOrderInfo.getOrderStatus())
					.getName());
			if (Utile.isEqual(myOrderInfo.getOrderStatus(),
					OrderState.WaitBuyerPay.name())
					|| Utile.isEqual(myOrderInfo.getOrderStatus(),
							OrderState.WaitBuyerConfirmGoods.name())) {
				orderState.setEnabled(true);
			} else {
				orderState.setEnabled(false);
			}
		} else {
			orderState.setText(OrderState
					.valueOf(myOrderInfo.getRefundStatus()).getName());
			if (Utile.isEqual(myOrderInfo.getRefundStatus(),
					OrderState.WaitBuyerReturnGoods.name())) {
				orderState.setEnabled(true);
			} else {
				orderState.setEnabled(false);
			}
		}
		// 删除按钮的显示与隐藏
		if (Utile.isEqual(myOrderInfo.getOrderStatus(),
				OrderState.WaitBuyerConfirmGoods.name())
				|| Utile.isEqual(myOrderInfo.getOrderStatus(),
						OrderState.TradeFinished.name())
				|| Utile.isEqual(myOrderInfo.getOrderStatus(),
						OrderState.WaitSellerSendGoods.name())) {
			deleteBtn.setVisibility(View.GONE);
		} else {
			deleteBtn.setVisibility(View.VISIBLE);
		}
		// 申请退款按钮的显示与隐藏
		if (Utile
				.isEqual(myOrderInfo.getRefundStatus(), OrderState.None.name())) {
			if (Utile.isEqual(myOrderInfo.getOrderStatus(),
					OrderState.WaitSellerSendGoods.name())
					&& Utile.isEqual(myOrderInfo.getOrderStatus(),
							OrderState.WaitSellerConfirmGoods.name())) {
				refundBtn.setVisibility(View.VISIBLE);
			} else {
				refundBtn.setVisibility(View.GONE);
			}
		} else {
			refundBtn.setVisibility(View.GONE);
		}
	}

	/**
	 * 提交单个订单的信息
	 */
	@Override
	protected void mainSubmit() {
		HashMap<String, String> oneOrderInfoMap = new HashMap<String, String>();
		oneOrderInfoMap.put(Config.TAG_ORDERID, orderId);
		oneOrderInfoMap.put(Config.TAG_TYPE, "User");
		myOrderInfoTask = new SubmitTask(this, Config.ONE_ORDER_INFO,
				MyOrderInfoList.class, new onOneOrderInfoSubmitListener(),
				isShowDialog);
		myOrderInfoTask.execute(oneOrderInfoMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (myOrderInfoTask != null) {
			myOrderInfoTask.destorySelf();
		}
		if (updateOrderStateUtile != null) {
			updateOrderStateUtile.destroy();
		}
		if (deleteOrderUtile != null) {
			deleteOrderUtile.destroy();
		}
		if (payUtile != null) {
			payUtile.destroy();
		}
	}

	@Override
	public void onUpdataUtileSuccess(boolean b,String OrderId) {
		mainSubmit();
	}

	@Override
	public void onDeleteUtileSuccess(MyOrderShop myOrderShop) {
		setResult(TAG_DELETE);
		this.finish();

	}

	/**
	 * 单击响应
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnMyOrderInfoClickLister implements OnClickListener {
		int currTag = 0;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.myorderinfo_center_delete:
				showMyAlertDialog(R.string.myorder_msg_delete, this, View.GONE);
				currTag = TAG_DELETE;
				deleteOrderUtile = new DeleteOrderUtile(
						MyOrderInfoActivity.this, myOrderShop,
						MyOrderInfoActivity.this);
				break;
			case R.id.myorderinfo_center_refundstate:
				showMyAlertDialog(R.string.myorder_msg_refund, this,
						View.VISIBLE);
				currTag = TAG_REFUND;
				break;
			case R.id.myorderinfo_center_orderstate:
				if (Utile.isEqual(myOrderShop.getRefundStatus(),
						OrderState.WaitBuyerReturnGoods.name())) {
					Intent intent = new Intent(MyOrderInfoActivity.this,
							RefundActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(MyOrderActivity.TAG_MYORDERSHOP,
							myOrderShop);
					intent.putExtras(bundle);
					MyOrderInfoActivity.this.startActivityForResult(intent,
							TAG_REFUND);
					// Logger.e("hhy", "到退回货物了");
				} else if (Utile.isEqual(myOrderShop.getOrderStatus(),
						OrderState.WaitBuyerPay.name())) {
					HashMap<String, String> tradeMap = new HashMap<String, String>();
					tradeMap.put(Config.TAG_TITLE, myOrderShop.getTitle());
					tradeMap.put(Config.TAG_TRADETYPE, Config.TRADE_TYPE_GOUWU);
					tradeMap.put(Config.TAG_ORDERIDS, myOrderShop.getOrderID());
					tradeMap.put(Config.TAG_TRADEID,
							myOrderShop.getTrade_TradeID());
					payUtile = new PayUtile(MyOrderInfoActivity.this, tradeMap,MyOrderInfoActivity.this);
					payUtile.tradeSubmit();
				} else if (Utile.isEqual(myOrderShop.getOrderStatus(),
						OrderState.WaitSellerSendGoods.name())) {//|| Utile.isEqual(myOrderShop.getOrderStatus(),OrderState.WaitBuyerConfirmGoods.name()
//					updateOrderStateUtile = new UpdateOrderStateUtile(
//							MyOrderInfoActivity.this, myOrderShop,
//							MyOrderInfoActivity.this, getMsgEditText());
//					updateOrderStateUtile.submit(false);
					// Logger.e("hhy", "到更新状态了");
				}else if (Utile.isEqual(myOrderShop.getOrderStatus(), OrderState.WaitBuyerConfirmGoods.name())) {
					showMyAlertDialog(
							R.string.myorder_msg_confirmgoods, this,View.GONE);
					currTag = TAG_SURE;
					dissMyAlertDialog();
				}
				break;
			case R.id.myalerdialog_sure:
				if (currTag == TAG_DELETE) {
					deleteOrderUtile.delete();
				} else if (currTag == TAG_REFUND) {
					updateOrderStateUtile = new UpdateOrderStateUtile(MyOrderInfoActivity.this,
							MyOrderInfoActivity.this, myOrderShop.getOrderID(), getMsgEditText());
					updateOrderStateUtile.submit(true);
				} else if (currTag == TAG_SURE) {
					updateOrderStateUtile = new UpdateOrderStateUtile(MyOrderInfoActivity.this,
							MyOrderInfoActivity.this, myOrderShop.getOrderID(),
							 getMsgEditText());
					updateOrderStateUtile.submit(false);
				}
				break;
			case R.id.myalerdialog_cancel:
				dissMyAlertDialog();
				break;
			default:
				break;
			}
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
			isShowDialog = false;
			infoList = (MyOrderInfoList) result.getObject();
			if (infoList != null && infoList.getOrderInfo() != null
					&& infoList.getOrderInfo().size() > 0) {
				myOrderInfo = infoList.getOrderInfo().get(0);
				initMyView();
			} else {
				MyOrderInfoActivity.this.finish();
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
		
	}

}
