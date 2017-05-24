package com.jmz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jmz.adapter.MyPagerAdapter;
import com.jmz.bean.ActBean;
import com.jmz.bean.ActList;
import com.jmz.bean.Child;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.listener.MyOnPageChangedListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.view.JazzyViewPager;
import com.jmz.view.JazzyViewPager.TransitionEffect;
import com.jmz.view.TileTextView;

/**
 * 专为展示应用
 * 
 * @author Administrator
 * 
 */
public class BanerActivity extends ParentActivity implements OnClickListener {
	public static final String LOGTAG = "BanerActivity";
	public static final String BANER_LIST = "banerlist";

	// ============== 广告切换 ===================
	private JazzyViewPager mViewPager = null;
	/**
	 * 装指引的ImageView数组
	 */
	private ImageView[] mball;

	/**
	 * 装ViewPager中ImageView的数组
	 */
	private LinearLayout ballLayout = null;

	private static final int MSG_CHANGE_PHOTO = 1;
	/** 图片自动切换时间 */
	private static final int PHOTO_CHANGE_TIME = 5000;
	private Handler mHandler = null;

	// ============== 广告切换 ===================

	private List<ActBean> viewPagerList;
	private TileTextView leftImg;
	private TileTextView midImg;
	private TileTextView rightImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadiagle();
		setTittleText("逛市场");
		viewPagerList = (ArrayList<ActBean>) getIntent().getExtras()
				.getSerializable(BANER_LIST);
		mHandler = new Handler(getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case MSG_CHANGE_PHOTO:
					if (mViewPager != null) {
						int index = mViewPager.getCurrentItem();
						if (index == viewPagerList.size() - 1) {
							index = -1;
						}
						mViewPager.setCurrentItem(index + 1);
					}
					mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
							PHOTO_CHANGE_TIME);
				}
			}

		};
		mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);

		initMyView();
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(BanerActivity.this).inflate(
				R.layout.activity_baner_center, null));
		leftImg = (TileTextView) findViewById(R.id.baner_center_left);
		midImg = (TileTextView) findViewById(R.id.baner_center_mid);
		rightImg = (TileTextView) findViewById(R.id.baner_center_right);
		leftImg.setOnClickListener(this);
		midImg.setOnClickListener(this);
		rightImg.setOnClickListener(this);
		initPagerView();
	}

	/**
	 * 初始化viewpager
	 */
	private void initPagerView() {
		mViewPager = (JazzyViewPager) findViewById(R.id.baner_center_viewpager);
		ballLayout = (LinearLayout) findViewById(R.id.baner_center_ball);

		// ======= 初始化ViewPager ========
		mball = new ImageView[viewPagerList.size()];
		if (viewPagerList.size() <= 1) {
			ballLayout.setVisibility(View.GONE);
		}
		for (int i = 0; i < mball.length; i++) {
			ImageView imageView = new ImageView(this);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, 1);
			if (i != 0) {
				params.leftMargin = 5;
			}
			imageView.setLayoutParams(params);
			mball[i] = imageView;
			if (i == 0) {
				mball[i].setBackgroundResource(R.drawable.android_activities_cur);
			} else {
				mball[i].setBackgroundResource(R.drawable.android_activities_bg);
			}
			ballLayout.addView(imageView);
		}

		mViewPager.setTransitionEffect(TransitionEffect.Standard);
		mViewPager.setCurrentItem(0);
		ViewGroup.LayoutParams mViewPagerParams = mViewPager.getLayoutParams();
		mViewPagerParams.height = screenHeight * 1 / 4;
		mViewPager.setLayoutParams(mViewPagerParams);
		mViewPager.setOnPageChangeListener(new MyOnPageChangedListener(this,
				mball));
		mViewPager.setAdapter(new MyPagerAdapter(BanerActivity.this,
				mViewPager,viewPagerList));
		// ======= 初始化ViewPager ========
	}

	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.baner_center_left:
			intent.setClass(BanerActivity.this, ProductClassActivity.class);
			bundle.putSerializable(SearchActivity.CHILD, new Child("106", "",
					"中药港", "2"));
			break;
		case R.id.baner_center_mid:
			intent.setClass(BanerActivity.this, ProductClassActivity.class);
			bundle.putSerializable(SearchActivity.CHILD, new Child("109", "",
					"宏进市场", "2"));
			break;
		case R.id.baner_center_right:
			intent.setClass(BanerActivity.this, ProductClassActivity.class);
			bundle.putSerializable(SearchActivity.CHILD, new Child("110", "",
					"毅德商贸", "2"));
			break;
		}
		intent.putExtras(bundle);
		BanerActivity.this.startActivity(intent);
	}
}
