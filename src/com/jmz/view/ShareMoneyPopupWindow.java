package com.jmz.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.jmz.ParentActivity;
import com.jmz.R;

public class ShareMoneyPopupWindow extends PopupWindow {

	private LinearLayout layout;
	private ParentActivity activity;
	private Context context;

	public ShareMoneyPopupWindow(Context context) {
		super(context);
		this.context = context;
		this.activity = (ParentActivity) context;
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.pop_sharemoney, null);
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
		ColorDrawable dw = new ColorDrawable(android.R.color.transparent);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// 软键盘不会挡着popupwindow
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		update();
	}

}
