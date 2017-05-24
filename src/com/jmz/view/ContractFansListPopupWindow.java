package com.jmz.view;

import java.util.ArrayList;
import java.util.HashMap;

import cn.jpush.android.util.ac;

import com.jmz.ParentActivity;
import com.jmz.R;
import com.jmz.ShareMoneyAddActivity;
import com.jmz.adapter.AddShareMoneyAdapter;
import com.jmz.adapter.FansListAdapter;
import com.jmz.adapter.PopShopContactsAdapter;
import com.jmz.bean.DBUser;
import com.jmz.bean.Fans;
import com.jmz.bean.FansList;
import com.jmz.bean.Increase;
import com.jmz.bean.OnlyMoney;
import com.jmz.bean.ShopContantList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.ServerReturnStatus;
import com.jmz.view.PullToRefreshLayout.OnRefreshListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class ContractFansListPopupWindow extends PopupWindow implements OnRefreshListener {
	private LinearLayout emptyView;
	private PullableListView refreView;
	private PullToRefreshLayout refreLayout, pullToRefreshLayout;
	private LinearLayout layout;
	private ParentActivity activity;
	private FansListAdapter adapter;
	private SubmitTask task;
	private Context context;
	private DBUser dbUser;
	private FansList fanslist;
	private ArrayList<Fans> list;
	private int page = 1,pageTotal;
	private int tag = Config.DRAG_NORMAL;
	
	
	public ContractFansListPopupWindow(Context context, DBUser dbUser) {
		super(context);
		this.context = context;
		this.activity = (ParentActivity) context;
		this.dbUser = dbUser;
		list =  new ArrayList<Fans>();
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.pop_contract_fans_list,
				null);
		refreLayout = (PullToRefreshLayout) layout.findViewById(R.id.contractfansrefrelayout);
		refreLayout.setOnRefreshListener(this);
		emptyView = (LinearLayout) layout.findViewById(R.id.activity_emptyview);
		refreView = (PullableListView) layout.findViewById(R.id.contract_fans_listview);
		refreView.setEmptyView(emptyView);
		
		// 设置SelectPicPopupWindow的View
		this.setContentView(layout);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
//		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupAnimation);
		ContractFansSubmit();
		update();
	}
	
	/**
	 * 粉丝列表提交
	 */
	private void ContractFansSubmit() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Config.TAG_USERNAME, dbUser.getUserName());
		map.put(Config.TAG_PASSWORD, dbUser.getPassWordString());
		map.put(Config.TAG_PAGE, page + "");
		task = new SubmitTask(activity, Config.FANS_LIST,FansList.class,
				new onFansListSubmit(), false);
		task.execute(map);
		
	}

	/**
	 * 响应粉丝列表查询
	 * 
	 * @author Administrator
	 * 
	 */
	private class onFansListSubmit implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			fanslist = (FansList) result.getObject();
			Logger.i("zwt", "--->fanslist:" + fanslist + "  tag:" + tag);
			if (fanslist != null && !fanslist.getData().isEmpty()
					&& fanslist.getData().get(0).getPageTotal() != null) {
				pageTotal = Integer.parseInt(fanslist.getData().get(0)
						.getPageTotal());
				
				if (fanslist.getServerReturn() != null) {
					Toast.makeText(context, ServerReturnStatus
							.checkReturn(fanslist.getServerReturn()), Toast.LENGTH_SHORT).show();
					
				}else {
					if (tag == Config.DRAG_INDEX) {
						list.clear();
						list.addAll(fanslist.getFansList());
						pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
						adapter.notifyDataSetChanged();
					} else if (tag == Config.LOADMORE_INDEX) {
						list.addAll(fanslist.getFansList());
						pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
						adapter.notifyDataSetChanged();
					} else {
						list.addAll(fanslist.getFansList());
						adapter = new FansListAdapter(activity, list);
						refreView.setAdapter(adapter);
					}
				}
				
			} 
			
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		page = 1;
		tag = 1;
		ContractFansSubmit();
		this.pullToRefreshLayout = pullToRefreshLayout;
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// TODO Auto-generated method stub
		this.pullToRefreshLayout = pullToRefreshLayout;
		if (page < pageTotal) {
			page++;
			tag = 2;
			ContractFansSubmit();
		} else {
			Toast.makeText(context, R.string.refre_last, Toast.LENGTH_SHORT).show();
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
		}
	}
	
}
