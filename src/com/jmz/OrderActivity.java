package com.jmz;

import java.math.BigDecimal;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jmz.bean.AddressBean;
import com.jmz.bean.AddressList;
import com.jmz.bean.FareBean;
import com.jmz.bean.OneOrderPreviewList;
import com.jmz.bean.OrderList;
import com.jmz.bean.Product;
import com.jmz.bean.ShopAddressBean;
import com.jmz.bean.ShopAddressList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.ServerReturnStatus;
import com.jmz.uitl.Utile;
import com.jmz.view.ClearEditTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 下单
 * 
 * @author Administrator
 * 
 */
public class OrderActivity extends ParentActivity implements OnClickListener {
	public static final String LOGTAG = "OrderActivity";
	private Intent intent = new Intent();
	private AddressBean addressBean;
	private ShopAddressBean shopAddressBean;
	private SubmitTask addressTask;
	private SubmitTask shopAddressTask ;
	private Product product;
	private AddressList addressList;
	private ShopAddressList shopAddressList;
	private double userPayMoney;
	private int userAddressId;
	private int selectNumered;
	private HashMap<String, String> orderMap;
	// private HashMap<String, String> fareMap;
	// private SubmitTask fareTask;
	private SubmitTask orderTask;
	private TextView userAdress;
	private TextView shop_address;
	private ImageView img;
	private TextView productTitle;
	private TextView attr;
	private TextView subBtn;
	private TextView addBtn;
	private EditText numberEdit;
	private RadioGroup typeRadioGrop;
	private RadioButton expressTypeBtn,selfGetTypeBtn;
	private TextView farePrice;
	private TextView price;
	private TextView userAllMoney;
	private EditText userPay;
	private EditText userMsg;
	private TextView money;
	private TextView toTrade;
	private double noFareMoney = 5000;
	private double includeFareMoney = 5000;
	private String expressType = "Express";
	private RelativeLayout fareLayout;
	private int position;
	private String[] statesCn = new String[2];
	private String[] statesEn = new String[2];
	private ClearEditTextView ticketQrCodeID;
	private ClearEditTextView ticketValidCode;
	public OneOrderPreviewList oneOrderPreviewList;
	private LinearLayout linear_shopaddress;
	private CheckBox check_box;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadiagle();
		setTittleText(R.string.order_title);
		top_Home.setVisibility(View.VISIBLE);
		if (getIntent().getExtras() != null) {
			product = (Product) getIntent().getExtras().getSerializable(
					ProductActivity.TAG_PRODUCT);
			selectNumered = getIntent().getExtras().getInt(
					ProductActivity.TAG_SELCETNUMBERED);
			if (product == null) {
				showToast(R.string.product_exist);
				this.finish();
			} else {
				mainSubmit();
				ShopSubmit();
			}
		} else {
			showToast(R.string.product_exist);
			this.finish();
		}
		statesCn[0] = getString(R.string.order_express);
		statesCn[1] = getString(R.string.order_selfget);

		statesEn[0] = "Express";
		statesEn[1] = "SelfGet";

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		userAddressId = resultCode;
		mainSubmit();
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(OrderActivity.this).inflate(
				R.layout.activity_order_center, null));
		initbottomView(LayoutInflater.from(OrderActivity.this).inflate(
				R.layout.activity_order_bottom, null));
		linear_shopaddress=(LinearLayout) findViewById(R.id.linear_shopaddress);
		userAdress = (TextView) findViewById(R.id.order_center_address);
		shop_address = (TextView) findViewById(R.id.shop_address);
		img = (ImageView) findViewById(R.id.order_center_img);
		productTitle = (TextView) findViewById(R.id.order_center_title);
		attr = (TextView) findViewById(R.id.order_center_attr);
		subBtn = (TextView) findViewById(R.id.order_center_subbtn);
		numberEdit = (EditText) findViewById(R.id.order_center_numberedit);
		addBtn = (TextView) findViewById(R.id.order_center_addbtn);
		price = (TextView) findViewById(R.id.order_center_price);
		fareLayout = (RelativeLayout) findViewById(R.id.order_center_farelayout);
		farePrice = (TextView) findViewById(R.id.order_center_fareprice);
		typeRadioGrop = (RadioGroup) findViewById(R.id.order_center_radiogroup);
		expressTypeBtn = (RadioButton) findViewById(R.id.express_type_button);
		selfGetTypeBtn = (RadioButton) findViewById(R.id.selfget_type_button);
		check_box=(CheckBox) findViewById(R.id.check_box);
		typeRadioGrop.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int childID) {
				// TODO Auto-generated method stub
				if (expressTypeBtn.getId() == childID) {
					expressType = statesEn[0];
					linear_shopaddress.setVisibility(View.INVISIBLE);
				}else if (selfGetTypeBtn.getId() == childID) {
					expressType = statesEn[1];
					linear_shopaddress.setVisibility(View.VISIBLE);
				}
				previewSubmit();
			}
		});
		
		ticketQrCodeID = (ClearEditTextView) findViewById(R.id.order_center_ticketnumber);
		ticketValidCode = (ClearEditTextView) findViewById(R.id.order_center_ticketcode);
		userAllMoney = (TextView) findViewById(R.id.order_center_userallmoney);
		userPay = (EditText) findViewById(R.id.order_center_userpay);
		userMsg = (EditText) findViewById(R.id.order_center_msg);
		money = (TextView) findViewById(R.id.order_bottom_price);
		toTrade = (TextView) findViewById(R.id.order_bottom_to_trade);
	}

	/**
	 * 数据绑定
	 */
	private void setDataToView() {
		LayoutParams params = img.getLayoutParams();
		params.width = screenWidth * 3 / 10;
		params.height = screenWidth * 3 / 10;
		img.setLayoutParams(params);
		ImageLoader.getInstance().displayImage(
				Config.JMZG + product.getImageUrl(), img,
				MyApplication.getInstance().getOptions());
		productTitle.setText(product.getProductTitle());
		attr.setText(product.getAttrText());
		numberEdit.setText("" + selectNumered);
		BigDecimal discount = new BigDecimal(oneOrderPreviewList
				.getOrderPreviewInfo().get(0).getOrderProductList().get(0)
				.getProductPromotionInfo().getDiscount());
		if (discount.doubleValue() < 1) {
			price.setText(getString(R.string.yuanicon) + noFareMoney + "("
					+ (discount.floatValue() * 10) + "折)");
		} else
			price.setText(getString(R.string.yuanicon) + noFareMoney);

		farePrice.setText(oneOrderPreviewList.getOrderPreviewInfo().get(0)
				.getItemSummaryPromotionInfo()
				.getMyFareActualPrice(expressType));
		Spannable WordtoSpan = new SpannableString("账户支付 你可用金额"
				+ MyApplication.getInstance().getDbUser().getUseMoney() + "元");
		WordtoSpan.setSpan(new AbsoluteSizeSpan(40), 0, 4,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		WordtoSpan.setSpan(new AbsoluteSizeSpan(35), 4, WordtoSpan.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		WordtoSpan.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.deeporange)), 0, 4,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		WordtoSpan.setSpan(
				new ForegroundColorSpan(getResources().getColor(
						R.color.deeporange)), 10, WordtoSpan.length() - 1,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		userAllMoney.setText(WordtoSpan);
		money.setText(getString(R.string.yuanicon) + includeFareMoney);
		userAdress.setOnClickListener(this);
		subBtn.setOnClickListener(this);
		addBtn.setOnClickListener(this);
		toTrade.setOnClickListener(this);
		fareLayout.setOnClickListener(this);
		check_box.setOnCheckedChangeListener((android.widget.CompoundButton.OnCheckedChangeListener) this);

	}

	/**
	 * 提交获取地址
	 */
	@Override
	protected void mainSubmit() {
		HashMap<String, String> addressMap = new HashMap<String, String>();
		addressMap.put(Config.TAG_TYPE, "User");
		addressTask = new SubmitTask(this, Config.ADDRESS_LIST,
				AddressList.class, new onAddressListSubmitListener(), false);
		addressTask.execute(addressMap);
	}
	
	/**
	 * 提交店铺地址
	 * */
	protected void ShopSubmit() {
		HashMap<String, String> shopAddressMap = new HashMap<String, String>();
		shopAddressMap.put(Config.TAG_SHOPID, product.getShopID());
		Logger.i("", "--->shopAddressMap:" + shopAddressMap);
		shopAddressTask = new SubmitTask(this, Config.SHOP_ADDRESS,
				ShopAddressList.class, new onShopAddressListSubmitListener(), false);
		shopAddressTask.execute(shopAddressMap);
	}

	/**
	 * 提交获取订单号orderIDs
	 */
	private void orderSubmit() {
		orderMap = new HashMap<String, String>();
		orderMap.put(Config.TAG_PRODUCTIDS, product.getProductID());
		orderMap.put(Config.TAG_QUANTITYS, selectNumered + "");
		orderMap.put(Config.TAG_TYPE, "Order");
		orderMap.put(Config.TAG_USERADDRESSID, addressBean.getUserAddressID());
		orderMap.put(Config.TAG_REFERRERUSERID, product.getReferrerUserID());
		orderMap.put(Config.TAG_COMMISSIONLESS, userPayMoney + "");
		orderMap.put(Config.TAG_POSTSCRIPT, "%ShopID_" + product.getShopID()
				+ "%" + userMsg.getText().toString());
		orderMap.put(Config.TAG_ATTRS, product.getAttrId());
		orderMap.put(Config.TAG_EXPRESSTYPES, "ShopID_" + product.getShopID()
				+ "%" + expressType);
		orderMap.put(Config.TAG_QRCODEID, ticketQrCodeID.getText().toString());
		orderMap.put(Config.TAG_VALIDCODE, ticketValidCode.getText().toString());
		orderMap.put(Config.TAG_SELFGETADDRS,addressBean.getUserAddressID()
				+ "|" + shopAddressBean.getShopID());
		orderTask = new SubmitTask(this, Config.ORDER_POST, OrderList.class,
				new onOrderListSubmitListener(), true);
		orderTask.execute(orderMap);
	}

	/**
	 * 提交预览
	 */
	private void previewSubmit() {
		orderMap = new HashMap<String, String>();
		orderMap.put(Config.TAG_PRODUCTIDS, product.getProductID());
		orderMap.put(Config.TAG_QUANTITYS, selectNumered + "");
		orderMap.put(Config.TAG_TYPE, "Preview");
		orderMap.put(Config.TAG_USERADDRESSID, addressBean == null ? ""
				: addressBean.getUserAddressID());
		orderMap.put(Config.TAG_REFERRERUSERID, product.getReferrerUserID());
		orderMap.put(Config.TAG_COMMISSIONLESS, userPay.getText().toString());
		orderMap.put(Config.TAG_POSTSCRIPT, userMsg.getText().toString());
		orderMap.put(Config.TAG_ATTRS, product.getAttrId());
		orderMap.put(Config.TAG_EXPRESSTYPES, "ShopID_" + product.getShopID()
				+ "%" + expressType);
		orderTask = new SubmitTask(this, Config.ORDER_POST,
				OneOrderPreviewList.class, new onPreviewListSubmitListener(),
				true);
		orderTask.execute(orderMap);
	}

	// /**
	// * 提交获取运费
	// */
	// private void fareSubmit() {
	// fareMap = new HashMap<String, String>();
	// fareMap.put(Config.TAG_PRODUCTID, product.getProductID());
	// fareMap.put(Config.TAG_QUANTITY, selectNumered + "");
	// fareTask = new SubmitTask(this, Config.FARE_PRICE, FareBean.class,
	// new onFareSubmitListener(), true);
	// fareTask.execute(fareMap);
	// }

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (addressTask != null) {
			addressTask.destorySelf();
		}
		// if (fareTask != null) {
		// fareTask.destorySelf();
		// }
		if (orderTask != null) {
			orderTask.destorySelf();
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
			addressList = (AddressList) result.getObject();
			if (addressList != null && addressList.getUserAddressList() != null
					&& addressList.getUserAddressList().size() > 0) {
				for (int i = 0; i < addressList.getUserAddressList().size(); i++) {
					if (Utile.isEqual(addressList.getUserAddressList()
							.get(i).getUserAddressID(), userAddressId + "")) {
						addressBean = addressList.getUserAddressList().get(i);
						break;
					}else if (Utile.isEqual(addressList.getUserAddressList().get(i)
							.getIsDefault(), "True")) {
						addressBean = addressList.getUserAddressList().get(i);
					}  else {
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
 * 响应店铺地址
 * 
 * */
	private class onShopAddressListSubmitListener implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			//initMyView();
			shopAddressList = (ShopAddressList) result.getObject();
			Logger.d("", "---->:shopAddressList:" + shopAddressList);
//			if (shopAddressList != null && shopAddressList.getShopAddressList() != null
//					&& shopAddressList.getShopAddressList().size() > 0) {
				for (int i = 0; i < shopAddressList.getShopAddressList().size(); i++) {
					shopAddressBean = shopAddressList.getShopAddressList().get(i);
					if (shopAddressBean != null) {
						shop_address.setText(
										shopAddressBean.getRegion()+
										shopAddressBean.getStreet() );
					} 
				}
		}
	}
			


	public static final String ISPAY = "isPay";
	public static final String FARE = "fare";
	public static final String ORDERID = "orderid";
	public static final String USERPAYMONEY = "UserPayMoney";

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
				Intent intent = new Intent(OrderActivity.this,
						TradeActivity.class);
				intent.putExtra(FARE, oneOrderPreviewList.getOrderPreviewInfo()
						.get(0).getItemSummaryPromotionInfo()
						.getFareActualPrice());
				intent.putExtra(ORDERID, list.getOrderList().get(0)
						.getOrderID());
				intent.putExtra(USERPAYMONEY, userPayMoney);
				startActivity(intent);
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
			if (oneOrderPreviewList.getServerReturn() != null) {
				showToast(ServerReturnStatus.checkReturn(oneOrderPreviewList
						.getServerReturn()));

			}else if (oneOrderPreviewList.getData() != null
					&& oneOrderPreviewList.getData().getOrderAmountTotal() != null
					&& new BigDecimal(oneOrderPreviewList.getData()
							.getOrderAmountTotal()).doubleValue() >= 0) {
				noFareMoney = new BigDecimal(oneOrderPreviewList
						.getOrderPreviewInfo().get(0)
						.getItemSummaryPromotionInfo().getShopActualPrice())
						.doubleValue();
				includeFareMoney = new BigDecimal(oneOrderPreviewList.getData()
						.getOrderAmountTotal()).doubleValue();
				setDataToView();
			}else {
				OrderActivity.this.showToast(R.string.msg_order_fail);
				OrderActivity.this.finish();
			}
		}
	}

	// /**
	// * 响应获取运费
	// *
	// * @author Administrator
	// *
	// */
	// private class onFareSubmitListener implements OnSubmitListener {
	// @Override
	// public void onSubmitSuccess(ApiResponse<Object> result) {
	// FareBean fareBean = (FareBean) result.getObject();
	// // if (fareBean != null) {
	// // productFare = fareBean.getFarePrice();
	// // } else {
	// // productFare = "500.0";
	// // }
	// productFare = productFare != null ? fareBean.getFarePrice() : "0.0";
	// setDataToView();
	// }
	// }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.order_center_address:
			intent = new Intent(OrderActivity.this, AddressActivity.class);
			intent.putExtra(ISPAY, true);
			intent.putExtra(Config.TAG_USERADDRESSID, userAddressId);
			startActivityForResult(intent, 1);
			break;
		case R.id.order_center_subbtn:
			if (1 < selectNumered) {
				selectNumered--;
			}
			// noFareMoney = new BigDecimal(product.getShopPrice()).multiply(
			// new BigDecimal("" + selectNumered)).doubleValue();
			// price.setText(getString(R.string.yuanicon) + noFareMoney);
			// numberEdit.setText(selectNumered + "");
			// fareSubmit();
			previewSubmit();
			break;
		case R.id.order_center_addbtn:
			if (selectNumered < product.getAllAttrTotalStock()) {
				selectNumered++;
			}
			// noFareMoney = new BigDecimal(product.getShopPrice()).multiply(
			// new BigDecimal("" + selectNumered)).doubleValue();
			// price.setText(getString(R.string.yuanicon) + noFareMoney);
			// numberEdit.setText(selectNumered + "");
			// fareSubmit();
			previewSubmit();
			break;
		case R.id.order_bottom_to_trade:
			userPayMoney = new BigDecimal(userPay.getText().toString())
					.doubleValue();
			if (includeFareMoney < userPayMoney) {
				showToast("账户支付输入的金额不正确");
			} else if (addressBean == null) {
				showToast("收货地址不正确！");
			} else {
				orderSubmit();
			}
			break;
		case R.id.order_center_farelayout:
			new AlertDialog.Builder(OrderActivity.this)
					.setTitle(getString(R.string.msg_selectfare))
					.setSingleChoiceItems(statesCn, position,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									position = which;
									expressType = statesEn[position];
									previewSubmit();
									dialog.dismiss();
								}
							});//.show()
			break;
		case R.id.check_box:
			if(((CheckBox) v).isChecked()){  
			}
		default:
			break;
		}
	}
}
