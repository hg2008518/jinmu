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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.jmz.adapter.FavAdapter;
import com.jmz.bean.FavBean;
import com.jmz.bean.FavList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnFavListener;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.FavUtile;
import com.jmz.uitl.Logger;
import com.jmz.view.PullToRefreshLayout;
import com.jmz.view.PullToRefreshLayout.OnRefreshListener;
import com.jmz.view.PullableListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * 收藏
 * 
 * @author Administrator
 * 
 */
public class MyFavActivity extends ParentActivity implements
		OnRefreshListener, OnFavListener {
	private int page = 1;
	private int pageTotal;
	private int tag = Config.DRAG_NORMAL;
	private PullableListView refreView;
	private PullToRefreshLayout refreLayout,pullToRefreshLayout;
	private FavAdapter adapter;
	private List<FavBean> allMyFavs;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			position = msg.what;
			FavUtile favUtile = new FavUtile(MyFavActivity.this, 
					MyFavActivity.this);
			favUtile.submit(2,allMyFavs.get(position).getFavProductID());
		};
	};
	private SubmitTask unFavTask;
	private FavList favList;
	private int position;
	private LinearLayout emptyView;
	private HashMap<String, String> favMap;
	private SubmitTask favTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadiagle();
		setTittleText(R.string.fav_title);
		allMyFavs = new ArrayList<FavBean>();
		mainSubmit();
	}
	/**
	 * 初始化组件
	 */
	private void initMyView(){
		initcenterView(LayoutInflater.from(MyFavActivity.this).inflate(
				R.layout.activity_fav_center, null));
		refreLayout = (PullToRefreshLayout)findViewById(R.id.fav_center_refrelayout);
		refreLayout.setOnRefreshListener(this);
		refreView = (PullableListView) findViewById(R.id.fav_listview);
		emptyView = (LinearLayout) findViewById(R.id.activity_emptyview);
		refreView.setEmptyView(emptyView);
		refreView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, false));
	}
	
	/**
	 * 提交
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void mainSubmit() {
		favMap = new HashMap<String, String>();
		favMap.put(Config.TAG_PAGE, "" + page);
		favTask = new SubmitTask(this, Config.FAV_PRODUST_LIST,
				FavList.class, new onFavSubmitSuccess(), false);
		favTask.execute(favMap);
	}
	
	@Override
	public void destroyTask() {
		super.destroyTask();
		if (favTask != null) {
			favTask.destorySelf();
		}
	}
	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		tag = 1;
		mainSubmit();
		this.pullToRefreshLayout=pullToRefreshLayout;
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		this.pullToRefreshLayout=pullToRefreshLayout;
		if (page < pageTotal) {
			page++;
			tag = 2;
			mainSubmit();
		} else {
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
			showToast(R.string.refre_last);
		}
	}
	@Override
	public void onSuccess(int result) {
		if (result == 1) {
			allMyFavs.remove(position);
			adapter.isClear = true;
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 响应余额查询
	 * 
	 * @author Administrator
	 * 
	 */
	private class onFavSubmitSuccess implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			favList = (FavList) result.getObject();
			pageTotal = Integer.parseInt(favList.getData().get(0)
					.getPageTotal());
			if (tag == Config.DRAG_INDEX) {
				allMyFavs.clear();
				allMyFavs.addAll(favList.getFavProductList());
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				adapter.notifyDataSetChanged();
			} else if (tag == Config.LOADMORE_INDEX) {
				allMyFavs.addAll(favList.getFavProductList());
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				adapter.notifyDataSetChanged();
			} else {
				allMyFavs.clear();
				allMyFavs.addAll(favList.getFavProductList());
				initMyView();
				adapter = new FavAdapter(MyFavActivity.this, allMyFavs, handler);
				refreView.setAdapter(adapter);
			}
			refreView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(MyFavActivity.this,
							ProductActivity.class);
					intent.putExtra(Config.TAG_PUODUCTID,
							allMyFavs.get(position).getProductID());
					startActivity(intent);
				}
			});
		}

	}

}
