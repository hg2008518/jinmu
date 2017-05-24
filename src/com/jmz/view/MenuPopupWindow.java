package com.jmz.view;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jmz.MyApplication;
import com.jmz.ParentActivity;
import com.jmz.PostOutActivity;
import com.jmz.R;
import com.jmz.WebViewAcitivity;
import com.jmz.bean.DBUser;
import com.jmz.db.UserService;
import com.jmz.uitl.Config;
import com.jmz.uitl.FileUtlie;
import com.jmz.uitl.RefreUtile;

public class MenuPopupWindow extends PopupWindow {

	private LinearLayout layout;
	private ParentActivity activity;
	private OnClickListener listener;
	private TextView info;
	private TextView refre;
	private TextView exit;

	public MenuPopupWindow(Context context) {
		super(context);
		this.activity = (ParentActivity)context;
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.activity_menu, null);
		info = (TextView) layout.findViewById(R.id.menu_info);
		refre = (TextView) layout.findViewById(R.id.menu_refre);
		exit = (TextView) layout.findViewById(R.id.menu_exit);

		// 设置按钮监听
		info.setOnClickListener(new OnMenuClick());
		refre.setOnClickListener(new OnMenuClick());
		exit.setOnClickListener(new OnMenuClick());
		// 设置SelectPicPopupWindow的View
		this.setContentView(layout);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(R.color.basecolor);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// 软键盘不会挡着popupwindow
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		update();
	}

	private class OnMenuClick implements OnClickListener {
		private File file;
		private DBUser dbUser;
		private UserService service;
		private Intent intent = new Intent();
		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.menu_exit:
				file = FileUtlie.getJmzFile();
				if (file != null && file.exists()) {
					for (File f : file.listFiles()) {
						if (f.isFile())
							f.delete(); // 删除所有文件
					}
					file.delete();
				}
				for (ParentActivity activity : MyApplication.getInstance()
						.getActivities()) {
					activity.finish();
				}
				MyApplication.getInstance().getActivities().clear();
				service = new UserService(activity);
				dbUser = MyApplication.getInstance().getDbUser();
				dbUser.setIsLogin(0);
				service.update(dbUser);
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
				break;
			case R.id.menu_info:
				intent .setClass(activity, WebViewAcitivity.class);
				intent.putExtra(PostOutActivity.URLSTRING, Config.JMZGO_URL);
				activity.startActivity(intent);
				break;
			case R.id.menu_refre:
				new RefreUtile(activity,true).refreSubmit();
				break;
				
			default:
				break;
			}
			dismiss();
		}

	}
}
