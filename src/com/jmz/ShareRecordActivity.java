package com.jmz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jmz.adapter.ShareRecordAdapter;
import com.jmz.bean.ShareProduct;
import com.jmz.bean.ShareProductList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.YMShare;
import com.jmz.uitl.YMShare.RefreAdapter;
import com.jmz.view.PullToRefreshLayout;
import com.jmz.view.PullToRefreshLayout.OnRefreshListener;
import com.jmz.view.PullableListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 播券记录
 * 
 * @author Administrator
 * 
 */
public class ShareRecordActivity extends ParentActivity implements
		OnRefreshListener, RefreAdapter {
	private PullToRefreshLayout refreLayout;
	private PullableListView refreView;
	private ShareRecordAdapter adapter;
	private int pageTotal;
	private int page = 1;
	private int tag = Config.DRAG_NORMAL;
	private ShareProduct shareProduct;
	private List<ShareProduct> shareProducts = new ArrayList<ShareProduct>();
	private SubmitTask shareTask;
	private LinearLayout emptyView;
	private YMShare ymShare;
	private int position;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// shareProduct = (ShareProduct) msg.getData().getSerializable(
			// ProductActivity.TAG_PRODUCT);
			position = msg.what;
			shareProduct = shareProducts.get(position);
			ymShare = new YMShare(ShareRecordActivity.this, shareProduct,
					mController, ShareRecordActivity.this);
			ymShare.show();
		}
	};
	private PullToRefreshLayout pullToRefreshLayout;
	private SubmitTask shareProductTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadiagle();
		setTittleText(R.string.sharerecord_tittle);
		mainSubmit();
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mController != null) {
			/** 使用SSO授权必须添加如下代码 */
			UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
					requestCode);
			if (ssoHandler != null) {
				ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			}
			if (pullToRefreshLayout != null) {
				onRefresh(pullToRefreshLayout);
			}
		}
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(ShareRecordActivity.this).inflate(
				R.layout.activity_sharerecord_center, null));
		refreLayout = (PullToRefreshLayout) findViewById(R.id.sharerecord_refrelayout);
		refreView = (PullableListView) findViewById(R.id.sharerecord_listview);
		emptyView = (LinearLayout) findViewById(R.id.activity_emptyview);
		refreView.setEmptyView(emptyView);
		refreLayout.setOnRefreshListener(this);
		refreView.setSelector(android.R.color.transparent);
		refreView.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), false, false));
	}

	/**
	 * 提交
	 */
	@Override
	protected void mainSubmit() {
		HashMap<String, String> shareProductMap = new HashMap<String, String>();
		shareProductTask = new SubmitTask(this, Config.SHARE_RECOND,
				ShareProductList.class, new OnShareProductListSubmitListener(),
				false);
		shareProductTask.execute(shareProductMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (shareTask != null) {
			shareTask.cancel(true);
		}
		if (shareProductTask != null) {
			shareProductTask.destorySelf();
		}
		handler.removeCallbacksAndMessages(null);
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		tag = Config.DRAG_INDEX;
		this.pullToRefreshLayout = pullToRefreshLayout;
		mainSubmit();
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		this.pullToRefreshLayout = pullToRefreshLayout;
		if (page < pageTotal) {
			page++;
			tag = Config.LOADMORE_INDEX;
			mainSubmit();
		} else {
			showToast(R.string.refre_last);
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
		}
	}

	/**
	 * 响应提交
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnShareProductListSubmitListener implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ShareProductList shareProductList = (ShareProductList) result
					.getObject();
			if (!shareProductList.getData().isEmpty()) {
				pageTotal = Integer.parseInt(shareProductList.getData().get(0)
						.getPageTotal());
			} else {
				ShareRecordActivity.this.finish();
			}

			if (tag == Config.DRAG_INDEX) {
				shareProducts.clear();
				shareProducts.addAll(shareProductList.getShareProductReport());
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				adapter.setShareProducts(shareProducts);
			} else if (tag == Config.LOADMORE_INDEX) {
				shareProducts.addAll(shareProductList.getShareProductReport());
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				adapter.setShareProducts(shareProducts);
			} else {
				shareProducts.clear();
				shareProducts.addAll(shareProductList.getShareProductReport());
				initMyView();
				adapter = new ShareRecordAdapter(ShareRecordActivity.this,
						shareProducts, handler);
				refreView.setAdapter(adapter);
			}
		}
	}

	@Override
	public void onSuccess(ShareProduct product) {
		int a = 0;
		if (product.getTotal() != null) {
			a = Integer.parseInt(product.getTotal());
		}
		a++;
		shareProducts.get(position).setTotal(a + "");
		adapter.setShareProducts(shareProducts);
	}
}
