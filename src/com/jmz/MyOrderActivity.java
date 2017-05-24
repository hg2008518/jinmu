package com.jmz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jmz.adapter.MyOrderShopAdapter;
import com.jmz.bean.MyOrderShop;
import com.jmz.bean.MyOrderShopList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.OrderState;
import com.jmz.view.PullToRefreshLayout;
import com.jmz.view.PullToRefreshLayout.OnRefreshListener;
import com.jmz.view.PullableListView;

/**
 * 我的订单
 * 
 * @author Administrator
 * 
 */
public class MyOrderActivity extends ParentActivity implements
		OnRefreshListener {
	public static final String TAG_MYORDERSHOP = "myordershop";
	public static final String LOGTAG = "MyOrderActivity";
	public static final int TAG_MYORDERINFO = 1;
	private int page = 1;
	private int pageTotal;
	private int tag = Config.DRAG_NORMAL;
	private PullableListView refreView;
	private PullToRefreshLayout refreLayout, pullToRefreshLayout;
	private MyOrderShopAdapter adapter;
	private List<MyOrderShop> myOrderShops;
	private MyOrderShopList orderList;
	private PopupWindow popupWindow;
	private LayoutInflater inflater;
	private LinearLayout popLayout;
	private TextView waitsendTv, waitsureTv, sussTv, waitpayTv, allTv;
	private String orderStatus = "";
	private LinearLayout emptyView;
	private HashMap<String, String> myorderListMap;
	private boolean isShowDialog = false;
	private SubmitTask myOrderListTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(R.string.myorder_tittle);
		initLoadiagle();
		mainSubmit();
		myOrderShops = new ArrayList<MyOrderShop>();
		top_classText.setVisibility(View.VISIBLE);
		showClassSelectPop();
		top_classText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
				} else {
					popupWindow.showAsDropDown(base_topView);
				}
			}
		});
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent = new Intent(MyOrderActivity.this,
					MyOrderInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(MyOrderActivity.TAG_MYORDERSHOP,
					myOrderShops.get(msg.what));
			intent.putExtras(bundle);
			startActivityForResult(intent, TAG_MYORDERINFO);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TAG_MYORDERINFO
				&& resultCode == MyOrderInfoActivity.TAG_DELETE) {
			mainSubmit();
		}else if (resultCode == 0) {//全民付web支付返回
			mainSubmit();
		}
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(MyOrderActivity.this).inflate(
				R.layout.activity_myorder_center, null));
		refreLayout = (PullToRefreshLayout) findViewById(R.id.myorder_center_refrelayout);
		refreLayout.setOnRefreshListener(this);
		refreView = (PullableListView) findViewById(R.id.myorder_listview);
		emptyView = (LinearLayout) findViewById(R.id.activity_emptyview);
		refreView.setEmptyView(emptyView);
	}

	/**
	 * 显示订单选择
	 */
	private void showClassSelectPop() {
		inflater = LayoutInflater.from(this);
		popLayout = (LinearLayout) inflater.inflate(
				R.layout.activity_myorder_poplayout, null);
		popupWindow = new PopupWindow(popLayout, screenWidth, getHeightOrWidth(
				popLayout, false));
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		waitsendTv = (TextView) popLayout
				.findViewById(R.id.myorder_pop_waitsend);
		waitsureTv = (TextView) popLayout
				.findViewById(R.id.myorder_pop_waitsure);
		sussTv = (TextView) popLayout.findViewById(R.id.myorder_pop_suss);
		waitpayTv = (TextView) popLayout.findViewById(R.id.myorder_pop_waitpay);
		allTv = (TextView) popLayout.findViewById(R.id.myorder_pop_all);
		waitsendTv.setOnClickListener(new OnMyOrderClickListener());
		waitsureTv.setOnClickListener(new OnMyOrderClickListener());
		sussTv.setOnClickListener(new OnMyOrderClickListener());
		waitpayTv.setOnClickListener(new OnMyOrderClickListener());
		allTv.setOnClickListener(new OnMyOrderClickListener());
	}

	/**
	 * 查询订单提交
	 */
	@Override
	protected void mainSubmit() {
		myorderListMap = new HashMap<String, String>();
		myorderListMap.put(Config.TAG_PAGE, page + "");
		myorderListMap.put(Config.TAG_ORDERSTATUS, orderStatus);
		myorderListMap.put(Config.TAG_TYPE, "User");
		myorderListMap.put(Config.TAG_PRODUCTTYPE, "Normal");
		if (myOrderListTask != null) {
			myOrderListTask.destorySelf();
		}
		myOrderListTask = new SubmitTask(this, Config.My_ORDER,
				MyOrderShopList.class, new OnMyOrderListSubmitListener(),
				isShowDialog);
		myOrderListTask.execute(myorderListMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (myOrderListTask != null) {
			myOrderListTask.destorySelf();
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		tag = Config.DRAG_INDEX;
		mainSubmit();
		this.pullToRefreshLayout = pullToRefreshLayout;
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		this.pullToRefreshLayout = pullToRefreshLayout;
		if (page < pageTotal) {
			page++;
			tag = Config.LOADMORE_INDEX;
			mainSubmit();
		} else {
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
			showToast(R.string.refre_last);
		}
	}

	/**
	 * 弹出菜单的单击点击
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnMyOrderClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.myorder_pop_waitsend:
				orderStatus = OrderState.WaitSellerSendGoods.name();
				top_classText.setText(getString(R.string.myorder_state_waitsend));
				break;
			case R.id.myorder_pop_waitsure:
				orderStatus = OrderState.WaitBuyerConfirmGoods.name();
				top_classText.setText(getString(R.string.myorder_state_waitsure));
				break;
			case R.id.myorder_pop_suss:
				orderStatus = OrderState.TradeFinished.name();
				top_classText.setText(getString(R.string.myorder_state_suss));
				break;
			case R.id.myorder_pop_waitpay:
				orderStatus = OrderState.WaitBuyerPay.name();
				top_classText.setText(getString(R.string.myorder_state_topay));
				break;
			case R.id.myorder_pop_all:
				orderStatus = "";
				top_classText.setText(getString(R.string.myorder_state_allmyorder));
				break;
			default:
				break;
			}
			page = 1;
			tag = Config.REFRE_OTHER;
			isShowDialog = true;
			mainSubmit();
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
			}
		}
	}

	/**
	 * 查询所有订单
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnMyOrderListSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			orderList = (MyOrderShopList) result.getObject();
			isShowDialog = false;
			if (orderList.getData() != null && orderList.getOrderList() != null) {

				pageTotal = Integer.parseInt(orderList.getData().get(0)
						.getPageTotal());

				if (tag == Config.DRAG_INDEX) {
					myOrderShops.clear();
					myOrderShops.addAll(orderList.getOrderList());
					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
					adapter.setMyOrderShops(myOrderShops);
				} else if (tag == Config.LOADMORE_INDEX) {
					myOrderShops.addAll(orderList.getOrderList());
					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
					adapter.setMyOrderShops(myOrderShops);
				} else if (tag == Config.REFRE_OTHER) {
					myOrderShops.clear();
					myOrderShops.addAll(orderList.getOrderList());
					adapter.setMyOrderShops(myOrderShops);
				} else {
					myOrderShops.clear();
					myOrderShops.addAll(orderList.getOrderList());
					initMyView();
					adapter = new MyOrderShopAdapter(MyOrderActivity.this,
							myOrderShops, handler);
					refreView.setAdapter(adapter);
				}
			}
		}
	}

}
