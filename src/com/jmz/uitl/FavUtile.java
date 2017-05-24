package com.jmz.uitl;

import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;

import com.jmz.ParentActivity;
import com.jmz.bean.DBUser;
import com.jmz.bean.ParentBean;
import com.jmz.bean.Product;
import com.jmz.db.UserService;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnFavListener;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;

/**
 * 收藏操作
 *  
 * @author Administrator
 * 
 */
public class FavUtile {

	private Context context;
	private SubmitTask favTask;
	private ParentActivity activity;
	private String url;
	private OnFavListener listener;
	private int type;

	public FavUtile(Context context, OnFavListener listener) {
		this.context = context;
		this.listener = listener;
		activity = (ParentActivity) context;
	}

	private void destroy() {
		if (favTask != null && favTask.getStatus() == AsyncTask.Status.RUNNING) {
			favTask.cancel(true);
		}

	}

	/**
	 * 提交收藏
	 * @param type 1、收藏商品，2、取消收藏商品，3、收藏店铺、4取消收藏店铺
	 */
	public void submit(int type,String id) { 
		this.type = type;
		HashMap<String, String> favMap = new HashMap<String, String>();
		switch (type) {
		case 1:
			url = Config.FAV_PRODUST;
			favMap.put(Config.TAG_PUODUCTID, id);
			break;
		case 2:
			url = Config.UNFAV_PRODUST;
			favMap.put(Config.TAG_RAVPRODUCTID,id);
			break;
		case 3:
			url = Config.FAV_SHOP;
			favMap.put(Config.TAG_PUODUCTID, id);
			break;
		case 4:
			url = Config.UNFAV_SHOP;
			favMap.put(Config.TAG_RAVPRODUCTID,id);
			break;

		default:
			break;
		}
		favTask = new SubmitTask(context, url, ParentBean.class,
				new OnFvaSubmitListener(), false);
		favTask.execute(favMap);
	}

	/**
	 * 获取结果
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnFvaSubmitListener implements OnSubmitListener {
		

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ParentBean bean = (ParentBean) result.getObject();
//			Logger.e("hhy", bean.toString());
			if (bean != null) {
				if (Utile.isEqual(bean.getServerReturn(), Config.STATUSSUCCESS)) {
					if (listener != null) {
						listener.onSuccess(1);
					}
					switch (type) {
					case 1:
					case 3:
						activity.showToast("收藏成功");
						break;
					case 2:
					case 4:
						activity.showToast("已取消收藏");
						break;

					default:
						break;
					}
				} else if (Utile.isEqual(bean.getServerReturn(),
						"FavProductExist")
						|| Utile.isEqual(bean.getServerReturn(), "FavShopExist")) {
					activity.showToast("已经收藏了");
				} else {
					activity.showToast(ServerReturnStatus.checkReturn(bean
							.getServerReturn()));
				}
			}
		}
	}
}
