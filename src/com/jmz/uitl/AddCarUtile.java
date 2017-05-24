package com.jmz.uitl;

import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;

import com.jmz.ParentActivity;
import com.jmz.R;
import com.jmz.bean.ParentBean;
import com.jmz.bean.Product;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;

/**
 * 加入购物车操作
 * 
 * @author Administrator
 * 
 */
public class AddCarUtile {

	private Context context;
	private SubmitTask task;
	private ParentActivity activity;
	private Product product;

	public AddCarUtile(Context context, Product product) {
		this.context = context;
		this.product = product;
		activity = (ParentActivity) context;
	}

	/**
	 * 自杀
	 */
	public void destroy() {
		if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	/**
	 * 添加商品到购物车
	 */
	public void addCarSubmit() {
		HashMap<String, String> addCarMap = new HashMap<String, String>();
		addCarMap.put(Config.TAG_PUODUCTID, product.getProductID());
		addCarMap.put(Config.TAG_QUANTITY, product.getCurrQuantity() + "");
		addCarMap.put(Config.TAG_REFERRERUSERID, product.getReferrerUserID());
		addCarMap.put(Config.TAG_ATTREXTENDID, product.getAttrId());
		task = new SubmitTask(activity, Config.CAR_POST, ParentBean.class,
				new AddCarTaskSubmitListener(), true);
		task.execute(addCarMap);
	}

	/**
	 * 获取结果
	 * 
	 * @author Administrator
	 * 
	 */
	private class AddCarTaskSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ParentBean bean = (ParentBean) result.getObject();
			if (Utile.isEqual(Config.STATUSSUCCESS, bean.getServerReturn())) {
				activity.showToast(R.string.msg_addcarsuss);
			} else {
				activity.showToast(ServerReturnStatus.checkReturn(bean
						.getServerReturn()));
			}
		}
	}
}
