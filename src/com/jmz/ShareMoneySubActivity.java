package com.jmz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jmz.adapter.SubShareMoneyAdapter;
import com.jmz.bean.Less;
import com.jmz.bean.LessList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.view.PullToRefreshLayout;
import com.jmz.view.PullToRefreshLayout.OnRefreshListener;
import com.jmz.view.PullableListView;

/**
 * 播券减少奖金
 * 
 * @author Administrator
 * 
 */
public class ShareMoneySubActivity extends ParentActivity implements
		OnRefreshListener {
	private LinearLayout emptyView;
	private PullToRefreshLayout refreLayout, pullToRefreshLayout;
	private PullableListView pullableListView;
	private SubShareMoneyAdapter adapter;
	private LessList lessList;
	private int tag = Config.DRAG_NORMAL;
	private int page = 1;
	private int pageTotal;
	private List<Less> lesses;
	private SubmitTask moneySubTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadiagle();
		base_topView.setVisibility(View.GONE);
		base_logoView.setVisibility(View.GONE);
		setTittleText(R.string.sharemoney_tittle);
		lesses = new ArrayList<Less>();
		mainSubmit();
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(ShareMoneySubActivity.this).inflate(
				R.layout.activity_sharemoney_addandsub, null));
		refreLayout = (PullToRefreshLayout) findViewById(R.id.sharemoney_center_addrefrelayout);
		refreLayout.setOnRefreshListener(this);
		emptyView = (LinearLayout) findViewById(R.id.activity_emptyview);
		pullableListView = (PullableListView) findViewById(R.id.sharemoney_addandsub_listview);
		pullableListView.setEmptyView(emptyView);
	}

	/**
	 * 奖金减少提交
	 */
	@Override
	protected void mainSubmit() {
		HashMap<String, String> subMap = new HashMap<String, String>();
		subMap.put(Config.TAG_PAGE, "" + page);
		moneySubTask = new SubmitTask(this, Config.MONEY_SUB, LessList.class,
				new OnSubSubmitListener(), false);
		moneySubTask.execute(subMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (moneySubTask != null) {
			moneySubTask.destorySelf();
		}
	}

	/**
	 * 响应增加列表
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnSubSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			lessList = (LessList) result.getObject();
			pageTotal = Integer.parseInt(lessList.getData().get(0)
					.getPageTotal());
			if (tag == Config.DRAG_INDEX) {
				lesses.clear();
				lesses.addAll(lessList.getCommissionLessList());
				adapter.notifyDataSetChanged();
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			} else if (tag == Config.LOADMORE_INDEX) {
				lesses.addAll(lessList.getCommissionLessList());
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				adapter.notifyDataSetChanged();
			} else {
				initMyView();
				lesses.addAll(lessList.getCommissionLessList());
				adapter = new SubShareMoneyAdapter(ShareMoneySubActivity.this,
						lesses);
				pullableListView.setAdapter(adapter);
			}
		}

	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		this.pullToRefreshLayout = pullToRefreshLayout;
		page = 1;
		tag = 1;
		mainSubmit();
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
