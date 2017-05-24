package com.jmz;

import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.jmz.adapter.UserAdapter;
import com.jmz.bean.AwardList;
import com.jmz.bean.DBUser;
import com.jmz.uitl.BalanceUtlie;
import com.jmz.uitl.Logger;
import com.jmz.uitl.Utile;
import com.jmz.view.MyGridView;

/**
 * 用户详情
 * 
 * @author Administrator
 * 
 */
public class UserActivity extends ParentActivity implements OnClickListener {
	public static final String LOGTAG = "UserActivity";
	public static final String TAG_AWARDLIST = "AwardList";
	private TextView useNameText, loginOutText, onlyMoneyText, chargeText;
	private TextView postOut;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				showToast(R.string.user_isnew);
			} else if (msg.what == 1
					&& Utile.isEqual((String) msg.obj, BalanceUtlie.SUCCESS)) {
				setDataToView();
			}
		};
	};
	private MyGridView grid;
	private UserAdapter adapter;
	private ScrollView scrollView;
	private int scrollY;
	private DBUser dbUser;
	private TextView canUseMoneyText;
	private TextView noCanUseMoneyText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(R.string.user_tittle);
		dbUser = MyApplication.getInstance().getDbUser();
		new BalanceUtlie(UserActivity.this, handler);
		initMyView();
		setDataToView();
		isFinishAvtivity = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				if (scrollView != null)
					scrollView.smoothScrollTo(0, scrollY);
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (scrollView != null)
			scrollY = scrollView.getScrollY();
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		handler.removeCallbacksAndMessages(null);
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(UserActivity.this).inflate(
				R.layout.activity_user_center, null));
		scrollView = (ScrollView) findViewById(R.id.user_scrollview);
		useNameText = (TextView) findViewById(R.id.user_name);
		onlyMoneyText = (TextView) findViewById(R.id.user_onlymoney);
		canUseMoneyText = (TextView) findViewById(R.id.user_canusemoney);
		noCanUseMoneyText = (TextView) findViewById(R.id.user_nocanusemoney);
		chargeText = (TextView) findViewById(R.id.user_charge);
		postOut = (TextView) findViewById(R.id.user_postout);
		grid = (MyGridView) findViewById(R.id.user_grid);
		loginOutText = (TextView) findViewById(R.id.user_login_out);
	}

	/**
	 * 为组件赋值
	 */
	private void setDataToView() {
		useNameText.setText(dbUser.getUserName());
		onlyMoneyText.setText(Html.fromHtml(getString(R.string.user_onlymoney)
				+ "<font color = '#e4700d'>"
				+ String.format("%.2f", dbUser.getTotolMoney()) + "</font>"
				+ getString(R.string.yuan)));
		canUseMoneyText.setText("可提现：    "
				+ MyApplication.getInstance().getDbUser().getUseMoney()
				+ getString(R.string.yuan));
		noCanUseMoneyText.setText("不可提现：   "
				+ MyApplication.getInstance().getDbUser().getUnUseMoney()
				+ getString(R.string.yuan));
		chargeText.setOnClickListener(this);
		postOut.setOnClickListener(this);
		adapter = new UserAdapter(UserActivity.this);
		grid.setAdapter(adapter);
		loginOutText.setOnClickListener(this);
		JPushInterface.setAliasAndTags(getApplicationContext(), MyApplication
				.getInstance().getDbUser().getUserId(), null, mAliasCallback);
	}
	
	/**
	 * 添加推送别名回调
	 */
	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				Logger.e("hhy", "设置Alias成功"+alias);
				break;

			case 6002:
				Logger.e("hhy", "设置Alias失败");
				break;

			default:
				Logger.e("hhy", "设置Alias失败");
				break;
			}
		}

	};

	/**
	 * 退出
	 */
	private void eixtLogin() {
		showToast(R.string.user_loginout_suss);
		if (!MyApplication.getInstance().getActivities().isEmpty()) {
			for (ParentActivity activity : MyApplication.getInstance()
					.getActivities()) {
				if (!activity.getClass().getName()
						.contains(HomeActivity.LOGTAG)
						&& !activity.getClass().getName()
								.contains(SearchActivity.LOGTAG)
						&& !activity.getClass().getName()
								.contains(ProductClassActivity.LOGTAG)
						&& !activity.getClass().getName()
								.contains(ProductActivity.LOGTAG)
						&& !activity.getClass().getName()
								.contains(WebViewAcitivity.LOGTAG)) {
					activity.finish();
				}
			}
		}
		startActivity(new Intent(UserActivity.this, TabBaseAcitivity.class));
		dbUser.setIsLogin(0);
		MyApplication.getInstance().setDbUser(dbUser);
	}

	private Intent intent = new Intent();
	public AwardList awardList;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_charge:
			showToast(R.string.msg_more_to_refre);
			break;
		case R.id.user_postout:
			intent.setClass(UserActivity.this, PostOutActivity.class);
			startActivity(intent);
			break;
		case R.id.user_login_out:
			showMyAlertDialog(R.string.user_eixtlogin, this,View.GONE);
			break;
		case R.id.myalerdialog_sure:
			eixtLogin();
			break;
		case R.id.myalerdialog_cancel:
			dissMyAlertDialog();
			break;

		default:
			break;
		}
	}

}
