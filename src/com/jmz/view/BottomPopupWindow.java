//package com.jmz.view;
//
//import java.io.File;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.ColorDrawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.view.WindowManager;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//
//import com.jmz.HomeActivity;
//import com.jmz.LoginActivity;
//import com.jmz.MyApplication;
//import com.jmz.ParentActivity;
//import com.jmz.PostOutActivity;
//import com.jmz.ProductClassActivity;
//import com.jmz.R;
//import com.jmz.UserActivity;
//import com.jmz.WebViewAcitivity;
//import com.jmz.bean.DBUser;
//import com.jmz.db.UserService;
//import com.jmz.uitl.Config;
//import com.jmz.uitl.FileUtlie;
//import com.jmz.uitl.RefreUtile;
///**
// * pop控件
// * @author Administrator
// *
// */
//public class BottomPopupWindow extends PopupWindow {
//
//	private LinearLayout layout;
//	private ParentActivity activity;
//	private RadioGroup popGroup;
//	private RadioButton popLeft;
//	private RadioButton popShare;
//	private RadioButton popRight;
//	private RadioButton popkefu;
//	private RadioButton popHome;
//	private String activityName;
//
//	public BottomPopupWindow(Context context,final String activityName) {
//		super(context);
//		this.activity = (ParentActivity)context;
//		this.activityName = activityName;
//		LayoutInflater inflater = (LayoutInflater) activity
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		layout = (LinearLayout) inflater.inflate(R.layout.activity_bottom, null);
//		popGroup = (RadioGroup) layout.findViewById(R.id.bottom_group);
//		popHome = (RadioButton) layout.findViewById(R.id.bottom_home);
//		popShare = (RadioButton) layout
//				.findViewById(R.id.bottom_share);
//		popRight = (RadioButton) layout.findViewById(R.id.bottom_user);
//		popkefu = (RadioButton) layout.findViewById(R.id.bottom_kefu);
//		popkefu.setVisibility(View.GONE);
//		popHome.setVisibility(View.VISIBLE);
//		popGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup group, int position) {
//				switch (position) {
//				case R.id.bottom_home:
//					for (ParentActivity activity : MyApplication.getInstance()
//							.getActivities()) {
//						if (!activity.getClass().getName()
//								.contains(HomeActivity.LOGTAG)) {
//							activity.finish();
//						}
//					}
//					break;
//				case R.id.bottom_share:
//					if (!activityName.contains(ProductClassActivity.LOGTAG)) {
//						activity.startActivity(new Intent(activity,
//								ProductClassActivity.class));
//					}
//					break;
//				case R.id.bottom_user:
//					if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
//						if (!activityName.contains(UserActivity.LOGTAG)) {
//							activity.startActivity(new Intent(activity,
//									UserActivity.class));
//						}
//					} else if (MyApplication.getInstance().getDbUser()
//							.getIsLogin() == 0) {
//						if (!activityName.contains(LoginActivity.LOGTAG)) {
//							Intent loginIntent = new Intent(
//									activity, LoginActivity.class);
//							loginIntent.putExtra("activity",
//									UserActivity.LOGTAG);
//							activity.startActivity(loginIntent);
//						}
//					}
//					break;
//
//				default:
//					break;
//				}
//				dismiss();
//			}
//		});
//		if (activityName.contains(HomeActivity.LOGTAG)) {
//			popLeft.setChecked(true);
//		} else if (activityName.contains(ProductClassActivity.LOGTAG)) {
//			popShare.setChecked(true);
//		} else if (activityName.contains(LoginActivity.LOGTAG)
//				|| activityName.contains(UserActivity.LOGTAG)) {
//			popRight.setChecked(true);
//		}
//
//		// 设置按钮监听
//		// 设置SelectPicPopupWindow的View
//		this.setContentView(layout);
//		// 设置SelectPicPopupWindow弹出窗体的宽
//		this.setWidth(LayoutParams.FILL_PARENT);
//		// 设置SelectPicPopupWindow弹出窗体的高
//		this.setHeight(LayoutParams.WRAP_CONTENT);
//		// 设置SelectPicPopupWindow弹出窗体可点击
//		this.setFocusable(true);
//		// 设置SelectPicPopupWindow弹出窗体动画效果
////		this.setAnimationStyle(R.style.PopupAnimation);
//		// 实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(R.color.basecolor);
//		// 设置SelectPicPopupWindow弹出窗体的背景
//		this.setBackgroundDrawable(dw);
//		// 软键盘不会挡着popupwindow
//		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//		update();
//	}
//}
