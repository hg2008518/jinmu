package com.jmz;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.style.BulletSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.Toast;

import com.jmz.bean.Child;
import com.jmz.bean.DBUser;
import com.jmz.db.UserService;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.OnTabActivityResultListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TabBaseAcitivity extends Activity {
	public static final String LOGTAG = "TabBaseAcitivity";
	protected static final String TAG_FINISH = "tag_finish";
	private final static String TAG_ISDISS = "isDiss";
	private LocalActivityManager mLocalActivityManager;
	private TabHost tabHost;
	private RadioGroup radioGroup;
	private long exitTime;
	private RadioButton homeRadioButton, carRadioButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLocalActivityManager = new LocalActivityManager(this, false);
		mLocalActivityManager.dispatchCreate(savedInstanceState);
		initMyView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
        	ProductClassActivity activity = (ProductClassActivity) mLocalActivityManager
					.getActivity(ProductClassActivity.LOGTAG);
        	 //判断是否实现返回值接口
            if (activity instanceof OnTabActivityResultListener) {
                //获取返回值接口实例
                OnTabActivityResultListener listener = (OnTabActivityResultListener) activity;
                //转发请求到子Activity
                listener.onTabActivityResult(requestCode, resultCode, data);
                
            }
        }else if (requestCode == 3) {
			if (tabHost != null) {
				switch (resultCode) {
				case 0:
					tabHost.setCurrentTab(0);
					homeRadioButton.setChecked(true);
					break;
				case 1:
					tabHost.setCurrentTab(4);
					break;
				case 2:
					tabHost.setCurrentTab(2);
					break;
				default:
					break;
				}
			}
		} else if (requestCode == 2) {
			switch (resultCode) {
			case 1:
				HomeActivity homeActivity = (HomeActivity) mLocalActivityManager
						.getActivity(HomeActivity.LOGTAG);
				homeActivity.onParentResult();
				break;

			default:
				break;
			}

		}

	}

	/**
	 * 退出设置，如果还在加载先停止加载在执行退出
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - exitTime > 2000) {
				Toast.makeText(this, R.string.msg_eixtapp, 1000).show();
				exitTime = System.currentTimeMillis();
			} else {
				allEixtApp();
			}
		}
		return false;
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		tabHost = (TabHost) LayoutInflater.from(this).inflate(
				R.layout.activity_tabbase, null);
		setContentView(tabHost);
		radioGroup = (RadioGroup) findViewById(R.id.tabbase_radiogroup);
		homeRadioButton = (RadioButton) findViewById(R.id.tabbase_home);
		carRadioButton = (RadioButton) findViewById(R.id.tabbase_car);
		tabHost.setup(mLocalActivityManager);
		// 0、首页
		tabHost.addTab(tabHost.newTabSpec(HomeActivity.LOGTAG)
				.setIndicator(HomeActivity.LOGTAG)
				.setContent(new Intent(this, HomeActivity.class)));
		// 1、千店
		Intent qianIntent = new Intent(this, ProductClassActivity.class);
		qianIntent.putExtra(TAG_FINISH, false);
		Bundle qianBundle = new Bundle();
		qianBundle.putSerializable(SearchActivity.CHILD, new Child("901", "",
				"千店特卖", "3"));
		qianIntent.putExtras(qianBundle);
		tabHost.addTab(tabHost.newTabSpec(ProductClassActivity.LOGTAG)
				.setIndicator(ProductClassActivity.LOGTAG)
				.setContent(qianIntent));

		// 2、购物车页
		Intent carIntent = new Intent(this, CarActivity.class);
		carIntent.putExtra(TAG_FINISH, false);
		tabHost.addTab(tabHost.newTabSpec(CarActivity.LOGTAG)
				.setIndicator(CarActivity.LOGTAG).setContent(carIntent));

		// 3、评星会
		Intent webIntent = new Intent(this, WebViewAcitivity.class);
		webIntent.putExtra(PostOutActivity.URLSTRING, Config.PINGXINGHUI_URL);
		webIntent.putExtra(TAG_FINISH, false);
		tabHost.addTab(tabHost.newTabSpec(WebViewAcitivity.LOGTAG)
				.setIndicator(WebViewAcitivity.LOGTAG).setContent(webIntent));

		// 4、账户页
		Intent userIntent = new Intent(this, UserActivity.class);
		userIntent.putExtra(TAG_FINISH, false);
		tabHost.addTab(tabHost.newTabSpec(UserActivity.LOGTAG)
				.setIndicator(UserActivity.LOGTAG).setContent(userIntent));

		tabHost.setCurrentTab(0);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int position) {

				switch (position) {
				case R.id.tabbase_home:
					tabHost.setCurrentTab(0);
					break;
				case R.id.tabbase_qiandian:
					tabHost.setCurrentTab(1);
					break;
				case R.id.tabbase_car:
					if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
						tabHost.setCurrentTab(2);
						CarActivity activity = (CarActivity) mLocalActivityManager
								.getActivity(CarActivity.LOGTAG);
						activity.mainSubmit();
					} else if (MyApplication.getInstance().getDbUser()
							.getIsLogin() == 0) {
						Intent intent = new Intent(TabBaseAcitivity.this,
								LoginActivity.class);
						Bundle loginBundle = new Bundle();
						loginBundle.putString(ProductActivity.TAG_ACIVITY,
								CarActivity.LOGTAG);
						intent.putExtras(loginBundle);
						startActivityForResult(intent, 3);
					}
					break;
				case R.id.tabbase_ping:
					tabHost.setCurrentTab(3);
					break;
				case R.id.tabbase_user:
					if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
						tabHost.setCurrentTab(4);
					} else if (MyApplication.getInstance().getDbUser()
							.getIsLogin() == 0) {
						Intent intent = new Intent(TabBaseAcitivity.this,
								LoginActivity.class);
						Bundle loginBundle = new Bundle();
						loginBundle.putString(ProductActivity.TAG_ACIVITY,
								LOGTAG);
						intent.putExtras(loginBundle);
						startActivityForResult(intent, 3);
					}
					break;

				default:
					break;
				}
			}
		});
	}

	/**
	 * 全部退出app
	 */
	public void allEixtApp() {
		//zwt 去掉取消登录
//		DBUser dbUser = MyApplication.getInstance().getDbUser();
//		dbUser.setIsLogin(0);
//		MyApplication.getInstance().setDbUser(dbUser);

		SharedPreferences settings = this.getSharedPreferences("refre",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(TAG_ISDISS, true);// 退出后要把获取更新的标示重置为true
		editor.commit();

		for (ParentActivity activity : MyApplication.getInstance()
				.getActivities()) {
			activity.finish();
		}
		MyApplication.getInstance().getActivities().clear();
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
	}
}
