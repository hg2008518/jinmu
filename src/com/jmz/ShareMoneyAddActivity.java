package com.jmz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jmz.adapter.AddShareMoneyAdapter;
import com.jmz.bean.Increase;
import com.jmz.bean.IncreaseList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.view.PullToRefreshLayout;
import com.jmz.view.PullToRefreshLayout.OnRefreshListener;
import com.jmz.view.PullableListView;

/**
 * 播券奖金
 * 
 * @author Administrator
 * 
 */
public class ShareMoneyAddActivity extends ParentActivity implements
		OnRefreshListener {
	private LinearLayout emptyView;
	private PullableListView addRefreView;
	private AddShareMoneyAdapter adapter;
	private IncreaseList increaseList;
	private int tag = Config.DRAG_NORMAL;
	private int page = 1;
	private int pageTotal;
	private String type;
	private List<Increase> increases;
	private PullToRefreshLayout refreLayout, pullToRefreshLayout;
	private SubmitTask moneyAddTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadiagle();
		base_topView.setVisibility(View.GONE);
		base_logoView.setVisibility(View.GONE);
		setTittleText(R.string.sharemoney_tittle);
		increases = new ArrayList<Increase>();
		type = getIntent().getExtras().getString("Type");
		Logger.i("zwt", "----->Type:" + type);
		mainSubmit();
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(ShareMoneyAddActivity.this).inflate(
				R.layout.activity_sharemoney_addandsub, null));
		refreLayout = (PullToRefreshLayout) findViewById(R.id.sharemoney_center_addrefrelayout);
		refreLayout.setOnRefreshListener(this);
		emptyView = (LinearLayout) findViewById(R.id.activity_emptyview);
		addRefreView = (PullableListView) findViewById(R.id.sharemoney_addandsub_listview);
		addRefreView.setEmptyView(emptyView);
	}

	/**
	 * 奖金增加提交
	 */
	@Override
	protected void mainSubmit() {
		HashMap<String, String> addMap = new HashMap<String, String>();
		addMap.put(Config.TAG_PAGE, "" + page);
		addMap.put(Config.TAG_TYPE, type);
		moneyAddTask = new SubmitTask(this, Config.MONEY_ADD,
				IncreaseList.class, new OnAddSubmitListener(), false);
		moneyAddTask.execute(addMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (moneyAddTask != null) {
			moneyAddTask.destorySelf();
		}
	}

	/**
	 * 响应增加列表
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnAddSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			increaseList = (IncreaseList) result.getObject();
			if (increaseList != null && !increaseList.getData().isEmpty()
					&& increaseList.getData().get(0).getPageTotal() != null) {
				pageTotal = Integer.parseInt(increaseList.getData().get(0)
						.getPageTotal());
			} else {
				ShareMoneyAddActivity.this.finish();
			}
			if (tag == Config.DRAG_INDEX) {
				increases.clear();
				increases.addAll(increaseList.getCommissionIncreaseList());
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				adapter.notifyDataSetChanged();
			} else if (tag == Config.LOADMORE_INDEX) {
				increases.addAll(increaseList.getCommissionIncreaseList());
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				adapter.notifyDataSetChanged();
			} else {
				initMyView();
				increases.addAll(increaseList.getCommissionIncreaseList());
				adapter = new AddShareMoneyAdapter(ShareMoneyAddActivity.this,
						increases);
				addRefreView.setAdapter(adapter);
			}
		}

	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		tag = 1;
		mainSubmit();
		this.pullToRefreshLayout = pullToRefreshLayout;
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		this.pullToRefreshLayout = pullToRefreshLayout;
		if (page < pageTotal) {
			page++;
			tag = 2;
			mainSubmit();
		} else {
			showToast(R.string.refre_last);
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
		}

	}

}
