package com.jmz;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jmz.adapter.CarShopAdapter;
import com.jmz.bean.CarProduct;
import com.jmz.bean.CarShopList;
import com.jmz.bean.ParentBean;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnBalancePayListener;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.DeleteCarProductUtile;
import com.jmz.uitl.Logger;
import com.jmz.uitl.ServerReturnStatus;
import com.jmz.uitl.Utile;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * 购物车
 * 
 * @author hhy 2014-7-11
 */
public class CarActivity extends ParentActivity implements OnClickListener,OnBalancePayListener{
	public static final String LOGTAG = "CarActivity";
	public static final int TAG_CARPRODUCT_DELETE = 1;
	public static final int TAG_CARPRODUCT_SELECT = 2;
	public static final int TAG_CARSHOP_SELECT = 3;
	public static final int TAG_CARPRODUCT_UPDATE = 4;
	public static final String TAG_CARSHOPLIST = "CarShopList";
	protected static final int TAG_CARPRODUCT_PAYDELETE = 5;
	private ListView listview;
	private TextView payBtn;
	private CheckBox selectAll;
	private TextView allMoney;
	private CarShopAdapter adapter;
	private LinearLayout emptyView;
	private int page = 0;
	private int pagesize = 0;
	private CarShopList carShopList;
	private Handler handler;
	private SubmitTask task;
	private SubmitTask deleteTask;
	private SubmitTask updateTask;
	private CarProduct carProduct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadiagle();
		setTittleText(R.string.car_title);
		mainSubmit();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					switch (msg.what) {
					case TAG_CARPRODUCT_DELETE:
						carProduct = (CarProduct) msg.obj;
						showMyAlertDialog(R.string.car_delete_msg,
								CarActivity.this, View.GONE);
						break;
					case TAG_CARPRODUCT_SELECT:
						carShopList.getShoppingCartList().get(msg.arg1)
								.getProductList().get(msg.arg2)
								.setCheck((Boolean) msg.obj);
						carShopList.getShoppingCartList().get(msg.arg1)
								.setAllCheck();
						setDataToHandle();
						break;
					case TAG_CARSHOP_SELECT:
						carShopList.getShoppingCartList().get(msg.arg1)
								.setCheck((Boolean) msg.obj);
						carShopList.getShoppingCartList().get(msg.arg1)
								.selectAllProduct((Boolean) msg.obj);
						setDataToHandle();
						break;
					case TAG_CARPRODUCT_UPDATE:
						carProduct = (CarProduct) msg.obj;
						UpdateSubmit(msg.arg1 + "");
						allMoney.setText(getString(R.string.car_initmoney)
								+ carShopList.allSelectMoney().toString());
						break;
					case TAG_CARPRODUCT_PAYDELETE:
						carProduct = (CarProduct) msg.obj;
						UpdateSubmit(msg.arg1 + "");
						break;

					default:
						break;
					}
				}
			};
		};
	}
	@Override
	protected void mainSubmit() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Config.TAG_PAGE, page + "");
		map.put(Config.TAG_PAGESIZE, pagesize + "");
		task = new SubmitTask(this, Config.CAR_LIST, CarShopList.class,
				new onCarShopListSubmitListener(), false);
		task.execute(map);
	}

	/**
	 * 删除购物车商品
	 */
	// protected void deleteSubmit(String cartId) {
	// HashMap<String, String> deleteMap = new HashMap<String, String>();
	// deleteMap.put(Config.TAG_SHOPPINGCARTIDS,cartId);
	// deleteMap.put(Config.TAG_TYPE, "");
	// Logger.e("hhy", deleteMap.toString());
	// deleteTask = new SubmitTask(this, Config.CAR_DELETE, ParentBean.class,
	// new onCarDeleteSubmitListener(), false);
	// deleteTask.execute(deleteMap);
	// }

	/**
	 * 更新购物车商品
	 * 
	 * @param number
	 *            数量
	 */
	protected void UpdateSubmit(String number) {
		HashMap<String, String> updateMap = new HashMap<String, String>();
		updateMap
				.put(Config.TAG_SHOPPINGCARTID, carProduct.getShoppingCartID());
		updateMap.put(Config.TAG_QUANTITY, number);
		updateMap.put(Config.TAG_ATTREXTENDID, "");
		updateTask = new SubmitTask(this, Config.CAR_UPDATE, ParentBean.class,
				new onCarUpdateSubmitListener(), true);
		updateTask.execute(updateMap);
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(CarActivity.this).inflate(
				R.layout.activity_car, null));
		listview = (ListView) findViewById(R.id.car_listview);
		emptyView = (LinearLayout) findViewById(R.id.activity_emptyview);
		selectAll = (CheckBox) findViewById(R.id.car_select_all);
		allMoney = (TextView) findViewById(R.id.car_payallmoney);
		payBtn = (TextView) findViewById(R.id.car_paybtn);
		listview.setEmptyView(emptyView);
		payBtn.setOnClickListener(this);
		payBtn.setEnabled(false);
		selectAll.setOnClickListener(this);
		allMoney.setText(getString(R.string.car_initmoney)
				+ carShopList.allSelectMoney().toString());
	}

	/**
	 * 为handle赋值
	 */
	private void setDataToHandle() {
		selectAll.setChecked(carShopList.isAllSelect());
		if (carShopList.getSelectNumber() <= 0) {
			payBtn.setEnabled(false);
		} else {
			payBtn.setEnabled(true);
		}
		allMoney.setText(getString(R.string.car_initmoney)
				+ carShopList.allSelectMoney().toString());
		payBtn.setText("去付款(" + carShopList.getSelectNumber() + ")");
		adapter.setCarShops(carShopList.getShoppingCartList());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.car_select_all:
			if (carShopList.isCheck()) {
				carShopList.setCheck(false);
			} else {
				carShopList.setCheck(true);
			}
			setDataToHandle();
			break;
		case R.id.car_paybtn:
			Intent intent = new Intent(CarActivity.this, CarOrderActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(TAG_CARSHOPLIST, carShopList);
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
			break;
		case R.id.adress_edit_delete:
			showMyAlertDialog(R.string.address_delete_msg, this, View.GONE);
			break;
		case R.id.myalerdialog_sure:
			DeleteCarProductUtile deleteCarProductUtile = new DeleteCarProductUtile(
					CarActivity.this,CarActivity.this);
			deleteCarProductUtile.deleteSubmit(carProduct.getShoppingCartID());
			break;
		case R.id.myalerdialog_cancel:
			dissMyAlertDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * 响应获取所有购物车里面商品
	 * 
	 * @author Administrator
	 * 
	 */
	private class onCarShopListSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			carShopList = (CarShopList) result.getObject();
			initMyView();
			if (carShopList != null
					&& carShopList.getShoppingCartList() != null) {
				adapter = new CarShopAdapter(CarActivity.this,
						carShopList.getShoppingCartList(), handler);
				listview.setAdapter(adapter);
			}
		}
	}

	// /**
	// * 响应删除结果
	// *
	// * @author Administrator
	// *
	// */
	// private class onCarDeleteSubmitListener implements OnSubmitListener {
	// private ParentBean bean;
	//
	// @Override
	// public void onSubmitSuccess(ApiResponse<Object> result) {
	// bean = (ParentBean) result.getObject();
	// if (Utile.isEqual(bean.getServerReturn(), Config.STATUSSUCCESS)) {
	// showToast(R.string.address_edit_delete_suss);
	// dissMyAlertDialog();
	// mainSubmit();
	// } else {
	// showToast(ServerReturnStatus
	// .checkReturn(bean.getServerReturn()));
	// }
	//
	// }
	// }

	/**
	 * 响应删除结果
	 * 
	 * @author Administrator
	 * 
	 */
	private class onCarUpdateSubmitListener implements OnSubmitListener {
		private ParentBean bean;

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			bean = (ParentBean) result.getObject();
			if (Utile.isEqual(bean.getServerReturn(), Config.STATUSSUCCESS)) {
				// showToast(R.string.address_edit_delete_suss);
			} else {
				showToast(ServerReturnStatus
						.checkReturn(bean.getServerReturn()));
			}
		}
	}

	@Override
	public void onBalancePaySuccess() {
		mainSubmit();
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
