package com.jmz.uitl;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jmz.ParentActivity;
import com.jmz.R;
import com.jmz.bean.DBUser;
import com.jmz.bean.MyOrderShop;
import com.jmz.bean.ParentBean;
import com.jmz.db.UserService;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.impl.OnUpdataUtileListener;
import com.jmz.submit.SubmitTask;

/**
 * 更新订单状态
 * 
 * @author Administrator
 * 
 */
public class UpdateOrderStateUtile {
	private Context context;
	private ParentActivity activity;
	private SubmitTask task;
	private HashMap<String, String> map;
	private String msg;
	private String orderId;
	private boolean isRefund;
	private OnUpdataUtileListener listener;

	public UpdateOrderStateUtile(Context context, OnUpdataUtileListener listener, String orderId, String msg) {
		this.context = context;
		this.orderId = orderId;
		this.msg = msg;
		this.listener = listener;
		activity = (ParentActivity) context;
	}

	/**
	 * 销毁task
	 */
	public void destroy() {
		if (task != null) {
			task.destorySelf();
		}
	}

	/**
	 * 提交
	 * 
	 * @param isRefund
	 *            True申请退款 ，Flase确认收货
	 */
	public void submit(boolean isRefund) {
		this.isRefund = isRefund;
		String url = Config.UPDATE_ORDERSTATUS;
		map =new HashMap<String, String>();
		map.put(Config.TAG_ORDERID, orderId);
		if (isRefund) {
			map.put(Config.TAG_REFUNDSTATUS, OrderState.WaitSellerAgree.name());
			map.put(Config.TAG_REFUNDREASON, msg);
			url = Config.UPDATE_REFUNDSTATUS;
		} else {
			map.put(Config.TAG_ORDERSTATUS, OrderState.TradeFinished.name());
			url = Config.UPDATE_ORDERSTATUS;
		}
		map.put(Config.TAG_TYPE, "User");
		destroy();
		task = new SubmitTask(context, url, ParentBean.class,
				new submitListenerImpl(), true);
		task.execute(map);
	}

	/**
	 * 响应
	 * 
	 * @author Administrator
	 * 
	 */
	private class submitListenerImpl implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ParentBean parentBean = (ParentBean) result.getObject();
			if (parentBean != null) {
				if (Utile.isEqual(parentBean.getServerReturn(),
						Config.STATUSSUCCESS)) {
					activity.showToast(R.string.myorderinfo_dossuccee);
					listener.onUpdataUtileSuccess(isRefund,orderId);
				} else {
					activity.showToast(ServerReturnStatus
							.checkReturn(parentBean.getServerReturn()));
				}
			}
		}
	}
}
