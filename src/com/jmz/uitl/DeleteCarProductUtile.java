package com.jmz.uitl;

import java.util.HashMap;

import android.content.Context;

import com.jmz.ParentActivity;
import com.jmz.R;
import com.jmz.bean.ParentBean;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnBalancePayListener;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;

/**
 * 删除购物车商品操作类
 * 
 * @author Administrator
 * 
 */
public class DeleteCarProductUtile {

	private SubmitTask deleteTask;
	private Context context;
	public boolean isShowMsg = true;
	private ParentActivity activity;
	private OnBalancePayListener listener;

	public DeleteCarProductUtile(Context context,OnBalancePayListener listener) {
		this.context = context;
		this.listener = listener;
		activity = (ParentActivity)context;
	}

	/**
	 * 删除购物车商品
	 */
	public void deleteSubmit(String cartId) {
		HashMap<String, String> deleteMap = new HashMap<String, String>();
		deleteMap.put(Config.TAG_SHOPPINGCARTIDS, cartId);
		deleteMap.put(Config.TAG_TYPE, "");
		deleteTask = new SubmitTask(context, Config.CAR_DELETE,
				ParentBean.class, new onCarDeleteSubmitListener(), false);
		deleteTask.execute(deleteMap);
	}

	/**
	 * 响应删除结果
	 * 
	 * @author Administrator
	 * 
	 */
	private class onCarDeleteSubmitListener implements OnSubmitListener {
		private ParentBean bean;

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			bean = (ParentBean) result.getObject();
			if (Utile.isEqual(bean.getServerReturn(), Config.STATUSSUCCESS)) {
				if(isShowMsg){
					activity.showToast(R.string.address_edit_delete_suss);
					activity.dissMyAlertDialog();
				}
				if(listener != null){
					listener.onBalancePaySuccess();
				}
			} else {
				activity.showToast(ServerReturnStatus
						.checkReturn(bean.getServerReturn()));
			}

		}
	}
}
