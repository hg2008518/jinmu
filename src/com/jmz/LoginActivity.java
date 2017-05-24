package com.jmz;

import java.util.HashMap;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jmz.bean.DBUser;
import com.jmz.bean.LoginBean;
import com.jmz.bean.OnlyMoney;
import com.jmz.bean.ParentBean;
import com.jmz.bean.WXUnionIDBean;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.ServerReturnStatus;
import com.jmz.uitl.Utile;
import com.jmz.uitl.YMShare;
import com.umeng.socialize.utils.*;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * 用户登录
 * 
 * @author Administrator
 * 
 */
public class LoginActivity extends ParentActivity {
	public static final String LOGTAG = "LoginActivity";
	private TextView loginBtn;
	private TextView registerBtn;
	private TextView forgetWordBtn;
	private EditText usenameedit;
	private EditText passwordedit;
	private CheckBox checkBox;
	private Button qqLoginBtn;
	private Button wxLoginBtn;
	private SubmitTask moneyTask;
	private String acitivty;
	private TextView companyinfo;
	private DBUser dbUser;
	private SubmitTask loginTask,isBindTask,oauthLoginTask;
	private String openID,accessToken,unionID= "";
	private String accessType;
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(R.string.login_tittle);
		initcenterView(LayoutInflater.from(LoginActivity.this).inflate(
				R.layout.activity_login_center, null));
		loginBtn = (TextView) findViewById(R.id.uselogin_loginbtn);
		forgetWordBtn = (TextView) findViewById(R.id.uselogin_forgetbtn);
		registerBtn = (TextView) findViewById(R.id.uselogin_registerbtn);
		companyinfo = (TextView) findViewById(R.id.uselogin_companyinfo);
		usenameedit = (EditText) findViewById(R.id.uselogin_name);
		passwordedit = (EditText) findViewById(R.id.uselogin_password);
		checkBox = (CheckBox) findViewById(R.id.uselogin_save);
		qqLoginBtn = (Button) findViewById(R.id.qq_longinbtn);
		qqLoginBtn.setOnClickListener(new LoginOclickImpl());
		wxLoginBtn = (Button) findViewById(R.id.weixin_longinbtn);
		wxLoginBtn.setOnClickListener(new LoginOclickImpl());
		loginBtn.setOnClickListener(new LoginOclickImpl());
		registerBtn.setOnClickListener(new LoginOclickImpl());
		forgetWordBtn.setOnClickListener(new LoginOclickImpl());

		dbUser = MyApplication.getInstance().getDbUser();

		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		mController.getConfig().closeToast();

		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
				YMShare.QQAPPID, YMShare.QQKEY);
		qqSsoHandler.addToSocialSDK();
		
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, YMShare.WXAPPID, YMShare.WXSECRET);
		wxHandler.addToSocialSDK();

		usenameedit.setText(dbUser.getUserName());
		if (dbUser.getIsRemeberPassword() == 1) {
			checkBox.setChecked(true);
			try {
				passwordedit.setText(Utile.jiemi(dbUser.getPassWord()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (dbUser.getIsRemeberPassword() == 0) {
			checkBox.setChecked(false);
			passwordedit.setText("");
		}
		
		if (getIntent().getExtras() != null) {
			acitivty = getIntent().getExtras().getString(
					ProductActivity.TAG_ACIVITY);
		}
		
		Animation animation = AnimationUtils
				.loadAnimation(this, R.anim.push_up);
		loginBtn.startAnimation(animation);
		registerBtn.startAnimation(animation);
		companyinfo.startAnimation(animation);

		isStaticPage = false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 1) {
			passwordedit.setText("");
		}else if (requestCode == 2 && resultCode == 1) {//绑定界面绑定注册成功后返回的
			UseMoneySubmit();
		}
	}

	/**
	 * 查询余额提交
	 */
	private void UseMoneySubmit() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Config.TAG_USERNAME, dbUser.getUserName());
		map.put(Config.TAG_PASSWORD, dbUser.getPassWordString());
		moneyTask = new SubmitTask(this, Config.MONEY_ONLY, OnlyMoney.class,
				new onOnlyMoneySubmit(), true);
		moneyTask.execute(map);
	}

	/**
	 * 登陆提交
	 */
	private void loginSubmit() {
		HashMap<String, String> loginMap = new HashMap<String, String>();
		loginMap.put(Config.TAG_USERNAME, usenameedit.getText().toString());
		loginMap.put(Config.TAG_PASSWORD, passwordedit.getText().toString());
		loginTask = new SubmitTask(LoginActivity.this, Config.ACCOUNT_LOGIN,
				LoginBean.class, new LoginSubmit(), true);
		loginTask.execute(loginMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (moneyTask != null) {
			moneyTask.destorySelf();
		}
		if (loginTask != null) {
			loginTask.destorySelf();
		}
		if (isBindTask != null) {
			isBindTask.destorySelf();
		}
		if (oauthLoginTask != null) {
			oauthLoginTask.destorySelf();
		}
	}

	/**
	 * 单击时间监听
	 * 
	 * @author Administrator
	 * 
	 */
	private class LoginOclickImpl implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 登陆
			case R.id.uselogin_loginbtn:
				if (usenameedit.getText().toString().length() < 11) {
					showToast(R.string.login_error_phone);
				} else if (passwordedit.getText().toString().length() < 6) {
					showToast(R.string.login_error_password);
				} else if (!isNetworkConnected()) {
					showToast(R.string.msg_neterror);
				} else {
					loginSubmit();
				}
				break;
			// 注册
			case R.id.uselogin_registerbtn:
				startActivity(new Intent(LoginActivity.this,
						RegisterActivity.class));
				break;
			// 忘记密码
			case R.id.uselogin_forgetbtn:
				Intent intent = new Intent(LoginActivity.this,
						FindPassWordActivity.class);
				intent.putExtra(Config.TAG_USERNAME, usenameedit.getText()
						.toString());
				startActivityForResult(intent, 1);
				break;
			// QQ登录
			case R.id.qq_longinbtn:
				accessType = "qq";
				QQLogin();
				break;
			// 微信登录
			case R.id.weixin_longinbtn:
				accessType = "weixin";
				WeiXinLogin();
				break;

			default:
				break;
			}
		}

	}

	/**
	 * 登陆监听
	 * 
	 * @author Administrator
	 * 
	 */
	private class LoginSubmit implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			LoginBean loginBean = (LoginBean) result.getObject();
			if (Utile
					.isEqual(loginBean.getServerReturn(), Config.STATUSSUCCESS)) {
				dbUser.setIsLogin(1);
				dbUser.setUserName(usenameedit.getText().toString());
				dbUser.setPassWordString(loginBean.getPasswordString());
				dbUser.setUserId(loginBean.getUserID());
				if (Utile.isEqual(loginBean.getIsContract(), "True"))
					dbUser.setIsContract(1);
				else 
					dbUser.setIsContract(0);
				
				dbUser.setContractDate(loginBean.getContractDate());
				if (checkBox.isChecked()) {
					try {
						dbUser.setPassWord(Utile.jiami(passwordedit.getText()
								.toString()));
						dbUser.setIsRemeberPassword(1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					dbUser.setPassWord("");
					dbUser.setIsRemeberPassword(0);
				}
				UseMoneySubmit();
			}else {
				showToast(ServerReturnStatus.checkReturn(loginBean
						.getServerReturn()));
			}
		}
	}

	/**
	 * 响应余额查询
	 * 
	 * @author Administrator
	 * 
	 */
	private class onOnlyMoneySubmit implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			OnlyMoney money = (OnlyMoney) result.getObject();
			dbUser.setUseMoney(Float.parseFloat(money.getAvailable()));
			dbUser.setUnUseMoney(Float.parseFloat(money.getLockSum()));
			MyApplication.getInstance().setDbUser(dbUser);
			Intent intent = new Intent();
			if (acitivty != null) {
				if (Utile.isEqual(acitivty, OrderActivity.LOGTAG)) {
					intent.setClass(LoginActivity.this, OrderActivity.class);
					intent.putExtras(getIntent().getExtras());
					startActivity(intent);
				} else if (Utile.isEqual(acitivty, CarActivity.LOGTAG)) {
					setResult(2);
				} else if (Utile.isEqual(acitivty, ProductActivity.LOGTAG)
						|| Utile.isEqual(acitivty, WebViewAcitivity.LOGTAG)) {
					setResult(2);
				} else if (Utile.isEqual(acitivty, TabBaseAcitivity.LOGTAG)) {
					setResult(1);
				}
			} else {
				intent.setClass(LoginActivity.this, UserActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}
	
	/**
	 * QQ登录
	 */
	private void QQLogin() {
		mController.doOauthVerify(LoginActivity.this, SHARE_MEDIA.QQ,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						//Toast.makeText(LoginActivity.this, "授权开始",
								//Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权错误",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						//Toast.makeText(LoginActivity.this, "授权完成",
								//Toast.LENGTH_SHORT).show();
						Logger.i("TestData", "QQ value---->" + value);
						openID = value.getString("openid");
						accessToken = value.getString("access_token");
						unionID = "";	
						checkBandingStatusSubmit();
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权取消",
								Toast.LENGTH_SHORT).show();
					}
					
				});
	}
	
	/**
	 * 微信登录
	 */
	private void WeiXinLogin() {
		mController.doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN,
				new UMAuthListener() {
					@Override
					public void onStart(SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权开始",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权错误",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						//Toast.makeText(LoginActivity.this, "授权完成",
								//Toast.LENGTH_SHORT).show();
						Logger.i("TestData", "WeiXin value---->" + value);
						openID = value.getString("openid");
						accessToken = value.getString("access_token");
						getWXUnionID(openID,accessToken);
						//unionID = value.getString("unionid");;	
						//heckBandingStatusSubmit();
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						Toast.makeText(LoginActivity.this, "授权取消",
								Toast.LENGTH_SHORT).show();
					}
				});
	}
	
	private void getWXUnionID(String openid, String token) {
        HashMap<String, String> map = new HashMap<String, String>();
		map.put("openid", openid);
		map.put("access_token", token);
		isBindTask = new SubmitTask(this,"https://api.weixin.qq.com/sns/userinfo", WXUnionIDBean.class,
				new getUnionIDSubmit(), true);
		isBindTask.execute(map);

	}

	/**
	 * 检查是否已经绑定有金拇指购账户
	 * 
	 * */
	private void checkBandingStatusSubmit() {
		//目前没有检测绑定状态，直接跳转测试
//		Intent intent1 = new Intent();
//		intent1.setClass(LoginActivity.this, BandingAccountActivity.class);
//		startActivityForResult(intent1, 2);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Config.TAG_OPENID, openID);
		map.put(Config.TAG_UNIONID, unionID);
		map.put(Config.TAG_ACCESSTYPE, accessType);
		isBindTask = new SubmitTask(this, Config.OAUTHIS_BIND, ParentBean.class,
				new oIsBindSubmit(), true);
		isBindTask.execute(map);
		
	}
	
	/**
	 * 获取微信的UnionID的监听返回
	 * @author Administrator
	 *
	 */
	private class getUnionIDSubmit implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			WXUnionIDBean bean = (WXUnionIDBean) result.getObject();
			 unionID = bean.getUnionid();
			 Logger.i("test6", "bean:" + bean + "\n" + "unionid:" + unionID);
             checkBandingStatusSubmit();
		}
	}
	
	/**
	 * 判断OAuth用户是否已经绑定了金拇指账户返回数据
	 * @author Administrator
	 *
	 */
	private class oIsBindSubmit implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ParentBean parent = (ParentBean) result.getObject();
			String serverReturn = parent.getServerReturn().trim();
			Intent intent = new Intent();
			if(Utile.isEqual(serverReturn, Config.OAUTHISBIND)) {//已经绑定直接登录
				oauthLoginSubmit();
			}else if(Utile.isEqual(serverReturn, Config.OAUTHNOTBIND)) {//没有绑定
				intent.setClass(LoginActivity.this, BandingAccountActivity.class);
				intent.putExtra(Config.TAG_OPENID, openID);
				intent.putExtra(Config.TAG_UNIONID, unionID);
				intent.putExtra(Config.TAG_ACCESSTOKEN, accessToken);
				intent.putExtra(Config.TAG_ACCESSTYPE, accessType);
				startActivityForResult(intent, 2);
			} else {
				showToast(ServerReturnStatus.checkReturn(serverReturn));
			}
			
		}
	}
	
	/**
	 * 已经绑定了直接登录
	 */
	private void oauthLoginSubmit() {
		HashMap<String, String> loginMap = new HashMap<String, String>();
		loginMap.put(Config.TAG_OPENID, openID);
		loginMap.put(Config.TAG_UNIONID, unionID);
		loginMap.put(Config.TAG_ACCESSTYPE, accessType);
		loginMap.put(Config.TAG_ACCESSTOKEN, accessToken);
		oauthLoginTask = new SubmitTask(LoginActivity.this, Config.OAUTHLOGIN,
				LoginBean.class, new OAuthLoginSubmit(), true);
		oauthLoginTask.execute(loginMap);
	}
	
	private class OAuthLoginSubmit implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			LoginBean loginBean = (LoginBean) result.getObject();
			if (Utile
					.isEqual(loginBean.getServerReturn(), Config.STATUSSUCCESS)) {
				dbUser.setIsLogin(1);
				dbUser.setUserName(loginBean.getUserName().toString());
				dbUser.setPassWordString(loginBean.getPasswordString());
				dbUser.setUserId(loginBean.getUserID());
				if (Utile.isEqual(loginBean.getIsContract(), "True"))
					dbUser.setIsContract(1);
				else 
					dbUser.setIsContract(0);
				dbUser.setContractDate(loginBean.getContractDate());
				
				dbUser.setPassWord("");
				dbUser.setIsRemeberPassword(0);
				
				UseMoneySubmit();
			}else if (Utile.isEqual(loginBean.getServerReturn(), Config.OPENIDISNotBIND)) {//还没有绑定金拇指购账号
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, BandingAccountActivity.class);
				intent.putExtra(Config.TAG_OPENID, openID);
				intent.putExtra(Config.TAG_UNIONID, unionID);
				intent.putExtra(Config.TAG_ACCESSTOKEN, accessToken);
				intent.putExtra(Config.TAG_ACCESSTYPE, accessType);
				startActivityForResult(intent, 2);
			} else {
				showToast(ServerReturnStatus.checkReturn(loginBean
						.getServerReturn()));
			}
		}
	}
}