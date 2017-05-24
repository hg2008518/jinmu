package com.jmz.uitl;

import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jmz.CityActivity;
import com.jmz.MyApplication;
import com.jmz.ParentActivity;
import com.jmz.R;
import com.jmz.bean.AllCity;
import com.jmz.bean.City;
import com.jmz.bean.RefreBean;
import com.jmz.bean.SinaIpBean;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;

/**
 * 更新助手
 * 
 * @author Administrator
 * 
 */
public class RefreUtile {
	
	private final static String TAG_ISDISS = "isDiss";
	private Context context;
	private ParentActivity activity;
	private SubmitTask refreTask;
	public HashMap<String, String> map;
	private Dialog miniDialog;
	private LinearLayout dialogLayout;
	private TextView msgTv;
	private TextView sureTv;
	private EditText msgEdt;
	private TextView noTv;
	private RefreBean refreBean;
	private SinaIpBean sinaBean;
	private String version = "1.1.0";
	private boolean isMsg;
	private SharedPreferences refreSharedPreferences;
	private Editor refreEditor;
	private SubmitTask sinaTask;
	private AllCity allCity;
	private ReferCallBack callBack;

	public RefreUtile(Context context, boolean isMsg) {
		this.context = context;
		this.isMsg = isMsg;
		activity = (ParentActivity) context;
		refreSharedPreferences = context.getSharedPreferences("refre",
				Activity.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象（第二步）
		refreEditor = refreSharedPreferences.edit();
		try {
			version = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			version = "1.1.0";
		}
	}

	/**
	 * 检查更新
	 */
	public void refreSubmit() {
		map = new HashMap<String, String>();
		map.put(Config.TAG_TYPE, "andriod");
		refreTask = new SubmitTask(context, Config.REFRE, RefreBean.class,
				new OnRefreListSubmitListener(), false);
		refreTask.execute(map);
	}

	/**
	 * 获取是否更新
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnRefreListSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			refreBean = (RefreBean) result.getObject();
			Logger.i("-->RefreUtile", "refreBean:" + refreBean);
			if (refreBean != null && refreBean.getVersion() != null
					&& Utile.isNewVersion(refreBean.getVersion(), version)) {
				if (refreSharedPreferences.getBoolean(TAG_ISDISS, true) || callBack != null) {
					showMiniDialog();
					if (callBack != null)
						callBack.callBack(true);
				}
			} else if (isMsg) {
				activity.showToast(R.string.msg_version_error);
				if (callBack != null)
					callBack.callBack(false);
			}
		}
	}

	/**
	 * 显示删除对话框
	 */
	private void showMiniDialog() {
		miniDialog = new Dialog(context);
		miniDialog.setCancelable(false);
		dialogLayout = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.dialog_nomal, null);
		msgTv = (TextView) dialogLayout.findViewById(R.id.myalerdialog_msg);
		msgTv.setText(context.getString(R.string.msg_found_new_version));
		sureTv = (TextView) dialogLayout.findViewById(R.id.myalerdialog_sure);
		msgEdt = (EditText) dialogLayout.findViewById(R.id.myalerdialog_edit);
		noTv = (TextView) dialogLayout.findViewById(R.id.myalerdialog_cancel);
		msgEdt.setMinHeight(activity.screenHeight / 10);
		sureTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (refreBean != null && refreBean.getURL() != null
						&& refreBean.getURL() != null) {
					Uri uri = Uri.parse(refreBean.getURL());
					Intent it = new Intent(Intent.ACTION_VIEW, uri);
					context.startActivity(it);
				}
			}
		});
		noTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// activity.finish();
				miniDialog.dismiss();
				// 用putString的方法保存数据
				refreEditor.putBoolean(TAG_ISDISS, false);
				// 提交当前数据
				refreEditor.commit();
			}
		});
		miniDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		miniDialog.setContentView(dialogLayout);
		miniDialog.setCancelable(false);
		miniDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		miniDialog.getWindow().setLayout(activity.screenWidth * 8 / 10,
				LayoutParams.WRAP_CONTENT);
		if (!miniDialog.isShowing()) {
			miniDialog.show();
		}

	}

	public void setCallBack(ReferCallBack callBack) {
		this.callBack = callBack;
	}
	
}
