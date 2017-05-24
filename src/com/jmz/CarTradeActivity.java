//package com.jmz;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//
//import com.jmz.bean.AddressBean;
//import com.jmz.bean.CarShopList;
//import com.jmz.bean.MyOrderInfo;
//import com.jmz.bean.MyOrderInfoList;
//import com.jmz.bean.OneOrderPreviewList;
//import com.jmz.bean.OrderList;
//import com.jmz.bean.Product;
//import com.jmz.http.ApiResponse;
//import com.jmz.impl.OnSubmitListener;
//import com.jmz.submit.SubmitTask;
//import com.jmz.uitl.Config;
//import com.jmz.uitl.Logger;
//import com.jmz.uitl.OrderState;
//import com.jmz.uitl.PayUtile;
//import com.jmz.uitl.Utile;
//
///**
// * 订单预览
// * 
// * @author Administrator
// * 
// */
//public class CarTradeActivity extends ParentActivity {
//	protected static final String LOGTAG = "CarTradeActivity";
////	public static final String MYORDERINFO = "myorderinfo";
//	private TextView orderQmfBtn;
//	private TextView shopName;
//	private TextView productTitle;
//	private TextView totalPayMoneyTv;
//	private TextView productNum;
//	private TextView fareMoney;
//	private TextView orderNumber;
//	private TextView orderTimeTv;
//	private TextView bottomPrice;
//	private PayUtile payUtile;
////	private HashMap<String, String> oneOrderMap;
////	private SubmitTask oneOrderTask;
//	private TextView userPayMoneyTv;
////	private MyOrderInfo myOrderInfo;
//	private TextView addressString;
//	private String fare;
//	private TextView productAttr;
//	private OneOrderPreviewList oneOrderPreviewList;
//	private CarShopList carShopList;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		initLoadiagle();
//		setTittleText(R.string.order_title);
//		oneOrderPreviewList = (OneOrderPreviewList) getIntent().getExtras()
//				.get(CarOrderActivity.TAG_ONEORDERPREVIEWLIST);
//		carShopList = (CarShopList) getIntent().getExtras()
//				.get(CarActivity.TAG_CARSHOPLIST);
//		fare = oneOrderPreviewList.getOrderPreviewInfo().get(0)
//				.getItemSummaryPromotionInfo().getFareActualPrice();
//		//mainSubmit();
//		initMyView();
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		if (payUtile != null && payUtile.isFinish()) {
//			gotoPayResult();
//			payUtile.setFinish(false);
//		}
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == 0) {
//			gotoPayResult();
//		}
//	}
//
//	/*
//	 * 初始化组件
//	 */
//	private void initMyView() {
//		initcenterView(LayoutInflater.from(CarTradeActivity.this).inflate(
//				R.layout.activity_trade_center, null));
//		initbottomView(LayoutInflater.from(CarTradeActivity.this).inflate(
//				R.layout.activity_trade_bottom, null));
//		shopName = (TextView) findViewById(R.id.trade_center_orderbusshop);
//		productTitle = (TextView) findViewById(R.id.trade_center_producttittle);
//		productAttr = (TextView) findViewById(R.id.trade_center_productattr);
//		totalPayMoneyTv = (TextView) findViewById(R.id.trade_center_productallmoney);
//		userPayMoneyTv = (TextView) findViewById(R.id.trade_center_productuserpay);
//		productNum = (TextView) findViewById(R.id.trade_center_productnumber);
//		fareMoney = (TextView) findViewById(R.id.trade_center_productexpmoney);
//		addressString = (TextView) findViewById(R.id.trade_center_adress);
//		orderNumber = (TextView) findViewById(R.id.trade_center_ordernumber);
//		orderTimeTv = (TextView) findViewById(R.id.trade_center_ordertime);
//		orderQmfBtn = (TextView) findViewById(R.id.trade_bottom_surepay);
//		bottomPrice = (TextView) findViewById(R.id.trade_bottom_price);
//		setDataToView();
//	}
//
//	/**
//	 * 为组件赋值
//	 */
//	private void setDataToView() {
//		productTitle.setText(carShopList.getAllSelectArg(6));
////		productAttr.setText(myOrderInfo.getOrderProductList().get(0)
////				.getProductAttribute());
//		productNum.setText(carShopList.getAllSelectArg(1));
//		fareMoney.setText(getString(R.string.yuanicon) + fare);
//		totalPayMoneyTv.setText(getString(R.string.yuanicon)
//				+ oneOrderPreviewList.getData().getOrderAmountTotal());
//		userPayMoneyTv.setText(getString(R.string.yuanicon) + oneOrderPreviewList.getUserPayMoney());
////		addressString.setText(myOrderInfo.getExpressString());
//		addressString.setText(Html
//				.fromHtml("<font color='#000000'>收货人："
//						+ oneOrderPreviewList.getAddressBean().getConsignee()
//						+ "&nbsp; &nbsp; &nbsp;"
//						+ oneOrderPreviewList.getAddressBean().getTelephone()
//						+ "</font><br>"
//						+ oneOrderPreviewList.getAddressBean().getRegion()
//						+ oneOrderPreviewList.getAddressBean().getStreet() ));
//		orderNumber.setText(oneOrderPreviewList.getOrderID());
//		orderTimeTv.setText(myOrderInfo.getOrderDate());
//		BigDecimal userPayDecimal = new BigDecimal(oneOrderPreviewList.getUserPayMoney());
//		BigDecimal allDecimal = new BigDecimal(oneOrderPreviewList.getData().getOrderAmountTotal());
//		bottomPrice.setText(getString(R.string.yuanicon)
//				+ allDecimal.subtract(userPayDecimal).toString());
//		orderQmfBtn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				HashMap<String, String> tradeMap = new HashMap<String, String>();
//				tradeMap.put(Config.TAG_TITLE, carShopList.getAllSelectArg(6));
//				tradeMap.put(Config.TAG_TRADETYPE, Config.TRADE_TYPE_GOUWU);
//				tradeMap.put(Config.TAG_ORDERIDS, oneOrderPreviewList.getOrderID());
//				if (Utile.isEqual(myOrderInfo.getOrderStatus(),
//						OrderState.WaitBuyerPay.name())) {
//					payUtile = new PayUtile(CarTradeActivity.this, tradeMap);
//					payUtile.tradeSubmit();
//				}
//			}
//		});
//
//	}
//
//	/**
//	 * 跑去支付结果
//	 */
//	private void gotoPayResult() {
//		Intent intent = new Intent(this, TradeResultActivity.class);
//		intent.putExtras(getIntent().getExtras());
//		startActivity(intent);
//		this.finish();
//	}
//
////	/**
////	 * 查询该笔订单状态
////	 */
////	@Override
////	protected void mainSubmit() {
////		oneOrderMap = new HashMap<String, String>();
////		oneOrderMap.put(Config.TAG_ORDERID, oneOrderPreviewList.getOrderID());
////		oneOrderMap.put(Config.TAG_TYPE, "User");
////		oneOrderTask = new SubmitTask(this, Config.ONE_ORDER_INFO,
////				MyOrderInfoList.class, new onOneOrderInfoSubmitListener(), true);
////		oneOrderTask.execute(oneOrderMap);
////	}
//
//	@Override
//	public void destroyTask() {
//		super.destroyTask();
//		if (payUtile != null) {
//			payUtile.destroy();
//		}
////		if (oneOrderTask != null) {
////			oneOrderTask.destorySelf();
////		}
//	}
//
////	/**
////	 * 响应获取订单信息
////	 * 
////	 * @author Administrator
////	 * 
////	 */
////	private class onOneOrderInfoSubmitListener implements OnSubmitListener {
////
////		@Override
////		public void onSubmitSuccess(ApiResponse<Object> result) {
////			MyOrderInfoList infoList = (MyOrderInfoList) result.getObject();
////			if (infoList != null&& infoList.getOrderInfo() != null && !infoList.getOrderInfo().isEmpty()) {
////				myOrderInfo = infoList.getOrderInfo().get(0);
////				initMyView();
////				if (Utile.isEqual(myOrderInfo.getOrderStatus(),
////						OrderState.WaitSellerSendGoods.name())) {
////					CarTradeActivity.this.finish();
////					gotoPayResult();
////				} else {
////					setDataToView();
////				}
////			} else {
////				CarTradeActivity.this.finish();
////				showToast("提交失败！");
////			}
////		}
////
////	}
//
//}
