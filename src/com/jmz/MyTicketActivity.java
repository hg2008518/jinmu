package com.jmz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.jmz.adapter.MyTicketAdapter;
import com.jmz.bean.MyOrderShop;
import com.jmz.bean.MyOrderShopList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnDeleteUtileListener;
import com.jmz.impl.OnSubmitListener;
import com.jmz.impl.OnUpdataUtileListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.DeleteOrderUtile;
import com.jmz.uitl.Logger;
import com.jmz.uitl.OrderState;
import com.jmz.view.PullToRefreshLayout;
import com.jmz.view.PullToRefreshLayout.OnRefreshListener;
import com.jmz.view.PullableListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * 我的订单
 * 
 * @author Administrator
 * 
 */
public class MyTicketActivity extends ParentActivity implements
		OnRefreshListener {
	public static final String LOGTAG = "MyTicketActivity";
	private int page = 1;
	private int pageTotal;
	private int tag = Config.DRAG_NORMAL;
	private PullableListView refreView;
	private PullToRefreshLayout refreLayout, pullToRefreshLayout;
	private MyTicketAdapter adapter;
	private List<MyOrderShop> myTicketShops;
	private MyOrderShopList ticketList;
	private LinearLayout emptyView;
	private RadioGroup group;
	private RadioButton nouseView;
	private RadioButton usedView;
	private RadioButton unenableView;
	private String orderStatus = OrderState.WaitSellerSendGoods.name();
	private boolean isShowDialog = false;
	private RadioButton allView;
	private SubmitTask myTicketListTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(R.string.myticket_tittle);
		initLoadiagle();
		myTicketShops = new ArrayList<MyOrderShop>();
		mainSubmit();
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(MyTicketActivity.this).inflate(
				R.layout.activity_myticket_center, null));
		refreLayout = (PullToRefreshLayout) findViewById(R.id.myticket_center_refrelayout);
		refreLayout.setOnRefreshListener(this);
		refreView = (PullableListView) findViewById(R.id.myticket_listview);
		emptyView = (LinearLayout) findViewById(R.id.activity_emptyview);
		refreView.setEmptyView(emptyView);
		refreView.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), false, false));
		group = (RadioGroup) findViewById(R.id.myticket_center_group);
		allView = (RadioButton) findViewById(R.id.myticket_center_all);
		nouseView = (RadioButton) findViewById(R.id.myticket_center_nouse);
		usedView = (RadioButton) findViewById(R.id.myticket_center_useed);
		unenableView = (RadioButton) findViewById(R.id.myticket_center_unenable);

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkId) {
				if (pullToRefreshLayout != null) {
					pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
				}
				if (nouseView.getId() == checkId) {
					orderStatus = OrderState.WaitSellerSendGoods.name();
				} else if (usedView.getId() == checkId) {
					orderStatus = OrderState.TradeFinished.name();
				} else if (unenableView.getId() == checkId) {
					orderStatus = OrderState.TradeClosed.name();
				} else {
					orderStatus = "";
				}
				page = 1;
				tag = Config.REFRE_OTHER;
				isShowDialog = true;
				mainSubmit();
			}
		});
	}

	/**
	 * 查询订单提交
	 */
	@Override
	protected void mainSubmit() {
		HashMap<String, String> myTicketListMap = new HashMap<String, String>();
		myTicketListMap.put(Config.TAG_PAGE, page + "");
		myTicketListMap.put(Config.TAG_ORDERSTATUS, orderStatus);
		myTicketListMap.put(Config.TAG_TYPE, "User");
		myTicketListMap.put(Config.TAG_PRODUCTTYPE, "Ticket");
		if (myTicketListTask != null) {
			myTicketListTask.destorySelf();
		}
		myTicketListTask = new SubmitTask(this, Config.My_ORDER,
				MyOrderShopList.class, new OnMyOrderListSubmitListener(),
				isShowDialog);
		myTicketListTask.execute(myTicketListMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (myTicketListTask != null) {
			myTicketListTask.destorySelf();
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		tag = Config.DRAG_INDEX;
		isShowDialog = false;
		mainSubmit();
		this.pullToRefreshLayout = pullToRefreshLayout;
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		isShowDialog = false;
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
	 * 查询所有订单
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnMyOrderListSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ticketList = (MyOrderShopList) result.getObject();
			
			if (ticketList.getData() != null
					&& ticketList.getOrderList() != null) {
				pageTotal = Integer.parseInt(ticketList.getData().get(0)
						.getPageTotal() == null ? "0" : ticketList.getData()
						.get(0).getPageTotal());
				if (tag == Config.DRAG_INDEX) {
					myTicketShops.clear();
					myTicketShops.addAll(ticketList.getOrderList());
					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
					adapter.setMyOrderShops(myTicketShops);
				} else if (tag == Config.LOADMORE_INDEX) {
					myTicketShops.addAll(ticketList.getOrderList());
					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
					adapter.setMyOrderShops(myTicketShops);
				} else if (tag == Config.REFRE_OTHER) {
					myTicketShops.clear();
					myTicketShops.addAll(ticketList.getOrderList());
					adapter.setMyOrderShops(myTicketShops);
					Logger.e("hhy", ticketList.toString());
				} else if (tag == Config.DRAG_NORMAL) {
					myTicketShops.addAll(ticketList.getOrderList());
					initMyView();
					adapter = new MyTicketAdapter(MyTicketActivity.this,
							myTicketShops);
					refreView.setAdapter(adapter);
				}
				refreView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(MyTicketActivity.this,
								MyTicketInfoActivity.class);
						intent.putExtra(Config.TAG_ORDERID,
								myTicketShops.get(position).getOrderID());
						startActivity(intent);
					}
				});
			}
		}
	}
}
