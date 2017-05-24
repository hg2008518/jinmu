package com.jmz;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

/**
 * 三层结构
 * 
 * @author Administrator
 * 
 *         2014-7-8
 */
public class BaseActivity extends FragmentActivity {
	private RelativeLayout baseLayout;
	private RelativeLayout.LayoutParams layoutParams;
	protected View base_topView, base_centerView, base_bottomView, base_logoView;

	// logo图片
	protected void initlogoView(View view) {
		if (base_logoView != null) {
			baseLayout.removeView(base_logoView);
			base_logoView = null;
		}
		if (view != null) {
			base_logoView = view;
			RelativeLayout.LayoutParams logoParams = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			logoParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			view.setId(1);
			baseLayout.addView(view, logoParams);
		}

	}

	// 顶部结构
	protected void initTopView(View view) {
		if (base_topView != null) {
			baseLayout.removeView(base_topView);
			base_topView = null;
		}
		if (view != null) {
			base_topView = view;
			RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			topParams.addRule(RelativeLayout.BELOW, 1);
			view.setId(2);
			baseLayout.addView(view, topParams);
		}
	}
	// 中间结构
	protected void initcenterView(View view) {
		if (base_centerView != null) {
			baseLayout.removeView(base_centerView);
			base_centerView = null;
		}
		if (view != null) {
			base_centerView = view;
			RelativeLayout.LayoutParams centerParams = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			centerParams.addRule(RelativeLayout.BELOW, 2);
			centerParams.addRule(RelativeLayout.ABOVE, 4);
			base_centerView.setId(3);
			baseLayout.addView(view, centerParams);
		}
	}

	// 底部
	protected void initbottomView(View view) {
		if (base_bottomView != null) {
			baseLayout.removeView(base_bottomView);
			base_bottomView = null;
		}
		if (view != null) {
			base_bottomView = view;
			view.setId(4);
			RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			baseLayout.addView(view, bottomParams);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置没有标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置竖屏
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		baseLayout = new RelativeLayout(BaseActivity.this);
		layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		setContentView(baseLayout);
	}

}
