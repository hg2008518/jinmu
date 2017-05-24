package com.jmz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jmz.adapter.CarOrderShopAdapter;
import com.jmz.bean.AddressBean;
import com.jmz.bean.AddressList;
import com.jmz.bean.CarShop;
import com.jmz.bean.CarShopList;
import com.jmz.bean.OneOrderPreviewList;
import com.jmz.bean.Order;
import com.jmz.bean.OrderList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnBalancePayListener;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.DeleteCarProductUtile;
import com.jmz.uitl.Logger;
import com.jmz.uitl.OrderState;
import com.jmz.uitl.PayUtile;
import com.jmz.uitl.ServerReturnStatus;
import com.jmz.uitl.Utile;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * 购物车订单预览
 * 
 * @author Administrator
 * 
 */
public class CarOrderActivity extends ParentActivity implements
		OnClickListener, OnBalancePayListener {
	public static final String TAG_ONEORDERPREVIEWLIST = "OneOrderPreViewList";
	public static final String TAG_SHOPINGCATEID = "ShopingCartId";
	private CarOrderShopAdapter adapter;
	private SubmitTask addressTask;
	private HashMap<String, String> orderMap;
	private SubmitTask orderTask;
	private AddressList addressList;
	private AddressBean addressBean;
	public int userAddressId;
	private TextView userAdress;
	private ListView listView;
	private TextView userMoney;
	private EditText userPayEdit;
	private TextView payBtn;
	private TextView money;
	private CarShopList carShopList;
	private boolean isShowDialog = false;
	private double userPayMoney;
	private String type = "Preview";// "Order"
	public OneOrderPreviewList oneOrderPreviewList;
	private Handler handler;
	private PayUtile payUtile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadiagle();
		setTittleText(R.string.car_order_check);
		carShopList = (CarShopList) getIntent().getExtras().get(
				CarActivity.TAG_CARSHOPLIST);
		mainSubmit();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					carShopList
							.setShoppingCartList((ArrayList<CarShop>) msg.obj);
					previewSubmit();
				}
			};
		};
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode == 0) {
				gotoPayResult(OrderState.WaitBuyerPay.name());
			} else {
				gotoPayResult(OrderState.WaitSellerSendGoods.name());
			}
		} else if (requestCode == 1) {
			userAddressId = resultCode;
			mainSubmit();
		}

	}

	/**
	 * 跑去支付结果
	 */
	private void gotoPayResult(String orderState) {
		Intent intent = new Intent(CarOrderActivity.this,
				CarTradeResultActivity.class);
		Bundle bundle = new Bundle();
		oneOrderPreviewList.setOrderStatus(orderState);
		bundle.putSerializable(TAG_ONEORDERPREVIEWLIST, oneOrderPreviewList);
		bundle.putSerializable(CarActivity.TAG_CARSHOPLIST, carShopList);
		intent.putExtras(bundle);
		startActivity(intent);
		if (carShopList.getAllSelectArg(7) != null) {
			DeleteCarProductUtile deleteCarProductUtile = new DeleteCarProductUtile(
					CarOrderActivity.this, null);
			deleteCarProductUtile.isShowMsg = false;
			deleteCarProductUtile.deleteSubmit(carShopList.getAllSelectArg(7));
		}
		this.finish();
	}

	/**
	 * 提交获取地址
	 */
	@Override
	protected void mainSubmit() {
		HashMap<String, String> addressMap = new HashMap<String, String>();
		addressMap.put(Config.TAG_TYPE, "User");
		addressTask = new SubmitTask(this, Config.ADDRESS_LIST,
				AddressList.class, new onAddressListSubmitListener(),
				isShowDialog);
		addressTask.execute(addressMap);
	}

	/**
	 * 提交预览
	 */
	private void previewSubmit() {
		orderMap = new HashMap<String, String>();
		orderMap.put(Config.TAG_PRODUCTIDS, carShopList.getAllSelectArg(0));
		orderMap.put(Config.TAG_QUANTITYS, carShopList.getAllSelectArg(1));
		orderMap.put(Config.TAG_TYPE, "Preview");
		orderMap.put(Config.TAG_USERADDRESSID, addressBean == null ? "" : addressBean.getUserAddressID());
		orderMap.put(Config.TAG_REFERRERUSERID, carShopList.getAllSelectArg(2));
		orderMap.put(Config.TAG_COMMISSIONLESS, userPayEdit.getText()
				.toString());
		orderMap.put(Config.TAG_POSTSCRIPT, carShopList.getAllSelectArg(3));
		orderMap.put(Config.TAG_ATTRS, carShopList.getAllSelectArg(4));
		orderMap.put(Config.TAG_EXPRESSTYPES, carShopList.getAllSelectArg(5));
		orderTask = new SubmitTask(this, Config.ORDER_POST,
				OneOrderPreviewList.class, new onPreviewListSubmitListener(),
				true);
		orderTask.execute(orderMap);
	}

	/**
	 * 提交获取orderId
	 */
	private void orderSubmit() {
		orderMap = new HashMap<String, String>();
		orderMap.put(Config.TAG_PRODUCTIDS, carShopList.getAllSelectArg(0));
		orderMap.put(Config.TAG_QUANTITYS, carShopList.getAllSelectArg(1));
		orderMap.put(Config.TAG_TYPE, "Order");
		orderMap.put(Config.TAG_USERADDRESSID, addressBean.getUserAddressID());
		orderMap.put(Config.TAG_REFERRERUSERID, carShopList.getAllSelectArg(2));
		orderMap.put(Config.TAG_COMMISSIONLESS, userPayEdit.getText()
				.toString());
		orderMap.put(Config.TAG_POSTSCRIPT, carShopList.getAllSelectArg(3));
		orderMap.put(Config.TAG_ATTRS, carShopList.getAllSelectArg(4));
		orderMap.put(Config.TAG_EXPRESSTYPES, carShopList.getAllSelectArg(5));
		// orderMap.put(Config.TAG_QRCODEID,
		// ticketQrCodeID.getText().toString());
		// orderMap.put(Config.TAG_VALIDCODE,
		// ticketValidCode.getText().toString());
		orderTask = new SubmitTask(this, Config.ORDER_POST, OrderList.class,
				new onOrderListSubmitListener(), true);
		orderTask.execute(orderMap);
	}

	/*
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(CarOrderActivity.this).inflate(
				R.layout.activity_carorder_center, null));
		initbottomView(LayoutInflater.from(CarOrderActivity.this).inflate(
				R.layout.activity_carorder_bottom, null));
		userAdress = (TextView) findViewById(R.id.carorder_address);
		listView = (ListView) findViewById(R.id.carorder_list);
		userMoney = (TextView) findViewById(R.id.carorder_usermoney);
		userPayEdit = (EditText) findViewById(R.id.carorder_useusermoney_edit);
		payBtn = (TextView) findViewById(R.id.carorder_bottom_pay);
		money = (TextView) findViewById(R.id.carorder_bottom_money);
	}

	/**
	 * 为组件赋值
	 */
	private void setDateToView() {
		userAdress.setOnClickListener(this);
		payBtn.setOnClickListener(this);
		adapter = new CarOrderShopAdapter(this,
				carShopList.getCheckShoppingCartList(), handler,
				oneOrderPreviewList);
		listView.setAdapter(adapter);
		Spannable WordtoSpan = new SpannableString("账户支付 你可用金额"
				+ MyApplication.getInstance().getDbUser().getUseMoney() + "元");
		WordtoSpan.setSpan(new RelativeSizeSpan(1.3f), 0, 4,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		WordtoSpan.setSpan(new RelativeSizeSpan(1f), 4, WordtoSpan.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		WordtoSpan.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.deeporange)), 0, 4,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		WordtoSpan.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.deeporange)), 10, WordtoSpan.length() - 1,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		userMoney.setText(WordtoSpan);
		money.setText(getString(R.string.car_initmoney)
				+ oneOrderPreviewList.getData().getOrderAmountTotal());
	}

	/**
	 * 支付
	 */
	private void pay(String orderId, String orderStatus) {
		HashMap<String, String> tradeMap = new HashMap<String, String>();
		tradeMap.put(Config.TAG_TITLE, carShopList.getAllSelectArg(6));
		tradeMap.put(Config.TAG_TRADETYPE, Config.TRADE_TYPE_GOUWU);
		tradeMap.put(Config.TAG_ORDERIDS, orderId);
		if (Utile.isEqual(orderStatus, OrderState.WaitBuyerPay.name())) {
			payUtile = new PayUtile(CarOrderActivity.this, tradeMap,
					CarOrderActivity.this);
			payUtile.tradeSubmit();
		}
	}

	@Override
	public void onBalancePaySuccess() {
		gotoPayResult(OrderState.WaitSellerSendGoods.name());
	}

	@Override
	public void onBalancePayFail() {
		gotoPayResult(OrderState.WaitBuyerPay.name());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.carorder_address:
			isShowDialog = true;
			Intent intent = new Intent(CarOrderActivity.this,
					AddressActivity.class);
			intent.putExtra(OrderActivity.ISPAY, true);
			intent.putExtra(Config.TAG_USERADDRESSID, userAddressId);
			startActivityForResult(intent, 1);
			break;
		case R.id.carorder_bottom_pay:
			if (userPayEdit.getText().toString().length() <= 0) {
				userPayEdit.setText("0");
			}
			userPayMoney = new BigDecimal(userPayEdit.getText().toString())
					.doubleValue();
			if (new BigDecimal(oneOrderPreviewList.getData()
					.getOrderAmountTotal()).doubleValue() < userPayMoney) {
				showToast("账户支付输入的金额不正确");
			} else if (addressBean == null) {
				showToast("收货地址不正确！");
			} else {
				orderSubmit();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 响应获取当前默认地址
	 * 
	 * @author Administrator
	 * 
	 */
	private class onAddressListSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			initMyView();
			isShowDialog = false;
			addressList = (AddressList) result.getObject();
			if (addressList != null && addressList.getUserAddressList() != null
					&& addressList.getUserAddressList().size() > 0) {
				for (int i = 0; i < addressList.getUserAddressList().size(); i++) {
					if (Utile.isEqual(addressList.getUserAddressList().get(i)
							.getIsDefault(), "True")) {
						addressBean = addressList.getUserAddressList().get(i);
					} else if (Utile.isEqual(addressList.getUserAddressList()
							.get(i).getUserAddressID(), userAddressId + "")) {
						addressBean = addressList.getUserAddressList().get(i);
					} else {
						addressBean = addressList.getUserAddressList().get(0);
					}
				}
				if (addressBean != null) {
					userAddressId = Integer.parseInt(addressBean
							.getUserAddressID());
					userAdress.setText(Html
							.fromHtml("<font color='#000000'>收货人："
									+ addressBean.getConsignee()
									+ "&nbsp; &nbsp; &nbsp;"
									+ addressBean.getTelephone()
									+ "</font><br><font color='#999999'>收货地址："
									+ addressBean.getRegion()
									+ addressBean.getStreet() + "</font>"));
				} else {
					userAdress.setText(R.string.address_exit);
				}
			} else {
				userAdress.setText(R.string.address_exit);
			}
			previewSubmit();
		}
	}

	/**
	 * 响应获取订单号
	 * 
	 * @author Administrator
	 * 
	 */
	private class onOrderListSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			OrderList list = (OrderList) result.getObject();
			if (Utile.isEqual(list.getServerReturn(), Config.STATUSSUCCESS)) {
				oneOrderPreviewList.setOrderID(list.getOrderIds());
				oneOrderPreviewList.setAddressBean(addressBean);
				oneOrderPreviewList.setUserPayMoney(userPayMoney + "");
				oneOrderPreviewList.setProductType(list.getOrderList().get(0)
						.getProductType());
				if (new BigDecimal(oneOrderPreviewList.getData()
						.getOrderAmountTotal()).doubleValue() == userPayMoney) {
					showToast("支付成功！");
					gotoPayResult(OrderState.WaitSellerSendGoods.name());
				} else {
					if (Utile.isEqual(list.getOrderStatus(),
							OrderState.WaitBuyerPay.name())) {
						pay(list.getOrderIds(), OrderState.WaitBuyerPay.name());
					} else {
						gotoPayResult(OrderState.WaitBuyerPay.name());
					}
				}
			} else {
				showToast(ServerReturnStatus
						.checkReturn(list.getServerReturn()));
			}
		}
	}

	/**
	 * 响应预览订单
	 * 
	 * @author Administrator
	 * 
	 */
	private class onPreviewListSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			oneOrderPreviewList = (OneOrderPreviewList) result.getObject();
			if (oneOrderPreviewList.getData() != null
					&& oneOrderPreviewList.getData().getOrderAmountTotal() != null
					&& new BigDecimal(oneOrderPreviewList.getData()
							.getOrderAmountTotal()).doubleValue() >= 0) {
				setDateToView();
			} else {
				CarOrderActivity.this.showToast(R.string.msg_order_fail);
				CarOrderActivity.this.finish();
			}
		}
	}

	@Override
	public void WXPaySingeSuccess(PayReq req) {
		// TODO Auto-generated method stub
		sendPayReq(req);
	}

}