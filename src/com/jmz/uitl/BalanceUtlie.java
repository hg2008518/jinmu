package com.jmz.uitl;

import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.jmz.MyApplication;
import com.jmz.ParentActivity;
import com.jmz.bean.DBUser;
import com.jmz.bean.OnlyMoney;
import com.jmz.db.UserService;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;

/**
 * 查询余额
 * 
 * @author Administrator
 * 
 */
public class BalanceUtlie implements OnSubmitListener {

	public static final String SUCCESS = "success";
	private Context context;
	private SubmitTask submitTask;
	private DBUser dbUser;
	private ParentActivity activity;
	private Handler handler;

	public BalanceUtlie(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		dbUser = MyApplication.getInstance().getDbUser();
		activity = (ParentActivity) context;
		submit();
	}

	/**
	 * 查询余额提交
	 */
	private void submit() {
		HashMap<String, String> map = new HashMap<String, String>();
		submitTask = new SubmitTask(context, Config.MONEY_ONLY,
				OnlyMoney.class, this, true);
		submitTask.execute(map);
	}

	/**
	 * 销毁task
	 */
	public void destroy() {
		if (submitTask != null) {
			submitTask.destorySelf();
		}
	}

	@Override
	public void onSubmitSuccess(ApiResponse<Object> result) {
		OnlyMoney money = (OnlyMoney) result.getObject();
		dbUser.setUseMoney(Float.parseFloat(money.getAvailable()));
		dbUser.setUnUseMoney(Float.parseFloat(money.getLockSum()));
		MyApplication.getInstance().setDbUser(dbUser);
		Message msg = new Message();
		msg.obj = SUCCESS;
		msg.what = 1;
		handler.sendMessage(msg);
	}
}
