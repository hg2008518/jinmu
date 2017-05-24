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
import android.os.Handler;
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
import com.jmz.bean.DBUser;
import com.jmz.bean.RefreBean;
import com.jmz.bean.SinaIpBean;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;

/**
 * 定位助手
 * 
 * @author Administrator
 * 
 */
public class CityUtile {
	// private final static String TAG_ISDISS = "isDiss";
	public final static int TAG_CITY_MSG = 123;
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
	private SinaIpBean sinaBean;
	private SharedPreferences cityListSharedPreferences;
	private Editor cityListEditor;
	private SubmitTask sinaTask;
	private AllCity allCity;
	private DBUser dbUser;
	private Handler handler;

	public CityUtile(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
		activity = (ParentActivity) context;
		cityListSharedPreferences = context.getSharedPreferences(
				CityActivity.CITY_SHAREPREFER_NAME, Activity.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象（第二步）
		cityListEditor = cityListSharedPreferences.edit();
		allCity = (AllCity) new Gson().fromJson(cityListSharedPreferences
				.getString(CityActivity.TAG_CITYlIST_NAME, ""), AllCity.class);

		dbUser = MyApplication.getInstance().getDbUser();
	}

	/**
	 * 检查更新
	 */
	public void sinaSubmit() {
		map = new HashMap<String, String>();
		sinaTask = new SubmitTask(context, Config.SINA_LOCALCITY,
				SinaIpBean.class, new OnSinaIptSubmitListener(), false);
		sinaTask.execute(map);
	}

	/**
	 * 获取新浪的定位城市
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnSinaIptSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			sinaBean = (SinaIpBean) result.getObject();
			if (sinaBean != null && sinaBean.getCity() != null
					&& allCity != null && allCity.getCityList() != null
					&& !allCity.getCityList().isEmpty()) {
				for (City city : allCity.getCityList()) {
					if (sinaBean.getProvince().contains("广西")
							&& sinaBean.getCity().contains(city.getCityName())
							&& !sinaBean.getCity().contains(
									dbUser.getCityName())
							&& city.getOpenFlgBool()) {
						showMiniDialog();
						break ; 
					} 
//					else {
//						dbUser.setCityId("15");
//						dbUser.setCityName("广西");
//						MyApplication.getInstance().setDbUser(dbUser);
//					}
				}
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
		msgTv.setText("    系统定位您在" + sinaBean.getCity() + ",需要切换到"
				+ sinaBean.getCity() + "吗？");
		sureTv = (TextView) dialogLayout.findViewById(R.id.myalerdialog_sure);
		msgEdt = (EditText) dialogLayout.findViewById(R.id.myalerdialog_edit);
		noTv = (TextView) dialogLayout.findViewById(R.id.myalerdialog_cancel);
		msgEdt.setMinHeight(activity.screenHeight / 10);
		sureTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				for (City city : allCity.getCityList()) {
					if (sinaBean.getCity().contains(city.getCityName())) {
						dbUser.setCityId(city.getCityID());
						dbUser.setCityName(city.getCityName());
						MyApplication.getInstance().setDbUser(dbUser);
						handler.sendEmptyMessage(TAG_CITY_MSG);
						miniDialog.dismiss();
					}
				}
			}
		});
		noTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// activity.finish();
				miniDialog.dismiss();
				// 用putString的方法保存数据
				// cityListEditor.putBoolean(TAG_ISDISS, true);
				// // 提交当前数据
				// cityListEditor.commit();
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
}
