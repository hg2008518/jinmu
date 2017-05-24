package com.jmz.uitl;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.jmz.bean.DBUser;
import com.jmz.bean.MyOrderInfoList;
import com.jmz.db.UserService;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnBalancePayListener;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;

/**
 * 使用余额额支付
 * 
 * @author Administrator
 * 
 */
public class BalancePayUtlie implements OnSubmitListener {
	private Context context;
	private OnBalancePayListener listener;
	private DBUser dbUser;
	private MyOrderInfoList infoList;
	private SubmitTask submitTask;
	private HashMap<String, String> map;
	private String orderId;

	public BalancePayUtlie(Context context,OnBalancePayListener listener, DBUser dbUser,String orderId) {
		this.context = context;
		this.listener = listener;
		this.dbUser = dbUser;
		this.orderId = orderId;
	}

	/**
	 * 获取订单状态
	 */
	public void submit() {
		map = new HashMap<String, String>();
		map.put(Config.TAG_ORDERID, orderId);
		map.put(Config.TAG_TYPE, "User");
		submitTask = new SubmitTask(context, Config.ONE_ORDER_INFO,
				MyOrderInfoList.class, this, true);
		submitTask.execute(map);
	}

	/**
	 * 销毁task
	 */
	public void destroy() {
		if (submitTask != null
				&& submitTask.getStatus() == AsyncTask.Status.RUNNING) {
			submitTask.cancel(true);
		}
	}

	@Override
	public void onSubmitSuccess(ApiResponse<Object> result) {
		infoList = (MyOrderInfoList) result.getObject();
		if (infoList != null
				&& Utile.isEqual(infoList.getOrderInfo().get(0)
						.getOrderStatus(),OrderState.WaitBuyerConfirmGoods.name())) {
			listener.onBalancePaySuccess();
		}
	}

}
