package com.jmz;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.jmz.adapter.StartPagerAdapter;
import com.jmz.bean.DBUser;
import com.jmz.db.UserService;
import com.jmz.uitl.Config;
import com.jmz.uitl.FileUtlie;
import com.jmz.uitl.Logger;
import com.jmz.view.JazzyViewPager;
import com.jmz.view.JazzyViewPager.TransitionEffect;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 启动动画
 * 
 * @author Administrator
 * 
 */
public class StartActivity extends Activity implements OnPageChangeListener {
	private String url;
	private JazzyViewPager viewpager;
	private ImageView closeBtn;
	private String id = "id=";
	private String rid = "&rid=";
	private String pf = "&pf=";
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			if (url != null && url.contains("/product/info.aspx")
					&& url.contains(id) && url.contains(rid)) {
				// http://m.jmzgo.com/product/info.aspx?id=202&rid=378&pf=android
				Intent intent = new Intent(StartActivity.this,
						ProductActivity.class);
				intent.putExtra(Config.TAG_PUODUCTID,
						url.substring(url.indexOf(id) + 3, url.indexOf(rid)));
				if (url.contains(pf)) {
					intent.putExtra(Config.TAG_REFERRERUSERID, url.substring(
							url.indexOf(rid) + 5, url.indexOf(pf)));
				} else if (url.contains("#")) {
					intent.putExtra(
							Config.TAG_REFERRERUSERID,
							url.substring(url.indexOf(rid) + 5,
									url.indexOf("#")));
				}
				startActivity(intent);
				StartActivity.this.finish();
			} else {
				startActivity(new Intent(StartActivity.this,
						TabBaseAcitivity.class));
				StartActivity.this.finish();
			}
		};
	};
	private boolean isViewPagerEnd;
	private ImageView img;
	private StartPagerAdapter adapter;
	private DBUser dbUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设置竖屏
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		setContentView(R.layout.activity_start);
		img = (ImageView) findViewById(R.id.start_img);

		// 设置用户未登录
		dbUser = MyApplication.getInstance().getDbUser();
		if (dbUser.getIsFirstEnterApp() == 0) {
			dbUser.setIsFirstEnterApp(1);
			MyApplication.getInstance().setDbUser(dbUser);
			img.setVisibility(View.GONE);
			viewpager = (JazzyViewPager) findViewById(R.id.start_viewpager);
			closeBtn = (ImageView) findViewById(R.id.start_btn);
			closeBtn.setVisibility(View.VISIBLE);
			adapter = new StartPagerAdapter(this, viewpager);
			viewpager.setAdapter(adapter);
			viewpager.setTransitionEffect(TransitionEffect.Accordion);
			viewpager.setCurrentItem(0);
			viewpager.setOnPageChangeListener(this);
		} else {
			iniTimer();
			img.setVisibility(View.VISIBLE);
		}

		url = getIntent().getDataString();

	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && isViewPagerEnd) {
			startActivity(new Intent(StartActivity.this, TabBaseAcitivity.class));
			StartActivity.this.finish();
		}
		return false;
	}

	/**
	 * 启动图片轮播定时器
	 */
	private void iniTimer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					handler.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(final int position) {
		if (position == 3) {
			isViewPagerEnd = true;
		} else {
			isViewPagerEnd = false;
		}
		closeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(StartActivity.this,
						TabBaseAcitivity.class));
				StartActivity.this.finish();
				handler.sendEmptyMessage(0);
			}
		});
	}
}
