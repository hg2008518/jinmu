package com.jmz;

import java.util.HashMap;

import com.jmz.bean.DBUser;
import com.jmz.bean.LoginBean;
import com.jmz.bean.ParentBean;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.ServerReturnStatus;
import com.jmz.uitl.Utile;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class BandingAccountActivity extends ParentActivity {

	private LinearLayout registerLayout, loginLayout;
	private RadioGroup group;
	private RadioButton registerView, loginView;
	private TextView sendSms, ensureBtn, jmzgoRule;
	private EditText phoneEdit, passwordEdit, passwordAgainEdit, validcodeEdit;
	private EditText usenameedit;
	private EditText passwordedit;
	private CheckBox agree;
	private SubmitTask phoneCodeTask, bandingTask;
	private String phone;
	private String code;
	private String passowrd;
	private String passwordAgain;
	private String openID, unionID, accessToken, accessType;
	private int flag = 1;// 1表示新注册账户绑定 2表示已有账户绑定
	private DBUser dbUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTittleText(R.string.banding_title);
		dbUser = MyApplication.getInstance().getDbUser();
		openID = getIntent().getExtras().getString(Config.TAG_OPENID);
		accessToken = getIntent().getExtras().getString(Config.TAG_ACCESSTOKEN);
		accessType = getIntent().getExtras().getString(Config.TAG_ACCESSTYPE);
		unionID = getIntent().getExtras().getString(Config.TAG_UNIONID);

		initMyView();
	}

	private void initMyView() {
		initcenterView(LayoutInflater.from(BandingAccountActivity.this)
				.inflate(R.layout.activity_banding_center, null));
		registerLayout = (LinearLayout) findViewById(R.id.banding_register_linearlayout);
		loginLayout = (LinearLayout) findViewById(R.id.banding_login_linearlayout);
		loginLayout.setVisibility(View.GONE);
		sendSms = (TextView) findViewById(R.id.banding_register_sendsms);
		ensureBtn = (TextView) findViewById(R.id.banding_ensurebtn);
		jmzgoRule = (TextView) findViewById(R.id.banding_register_rule);
		jmzgoRule.setOnClickListener(new onBandingClickImpl());
		jmzgoRule.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		phoneEdit = (EditText) findViewById(R.id.banding_register_phonenumber);
		validcodeEdit = (EditText) findViewById(R.id.banding_register_code);
		passwordEdit = (EditText) findViewById(R.id.banding_register_password);
		passwordAgainEdit = (EditText) findViewById(R.id.banding_register_passwordagain);

		usenameedit = (EditText) findViewById(R.id.banding_uselogin_name);
		passwordedit = (EditText) findViewById(R.id.banding_uselogin_password);

		agree = (CheckBox) findViewById(R.id.banding_register_agree);
		sendSms.setOnClickListener(new onBandingClickImpl());
		ensureBtn.setOnClickListener(new onBandingClickImpl());
		agree.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton button, boolean isCheck) {

				ensureBtn.setEnabled(isCheck ? true : false);
			}
		});

		group = (RadioGroup) findViewById(R.id.banding_center_group);
		registerView = (RadioButton) findViewById(R.id.banding_registerview);
		registerView.setChecked(true);
		loginView = (RadioButton) findViewById(R.id.banding_loginview);
		group.setOnCheckedChangeListener(new RadioGroupCheckBoxListener());

	}

	class RadioGroupCheckBoxListener implements
			RadioGroup.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if (checkedId == registerView.getId()) {
				registerLayout.setVisibility(View.VISIBLE);
				loginLayout.setVisibility(View.GONE);
				flag = 1;
				;
			} else if (checkedId == loginView.getId()) {
				registerLayout.setVisibility(View.GONE);
				loginLayout.setVisibility(View.VISIBLE);
				flag = 2;
			}
		}

	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (phoneCodeTask != null) {
			phoneCodeTask.destorySelf();
		}
		if (bandingTask != null) {
			bandingTask.destorySelf();
		}
	}

	/**
	 * 单击事件响应
	 * 
	 * @author Administrator
	 * 
	 */
	private class onBandingClickImpl implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.banding_register_sendsms:
				phone = phoneEdit.getText().toString();
				if (!isNetworkConnected()) {
					showToast(R.string.msg_neterror);
				} else if (phone.length() != 11 || !phone.startsWith("1")) {
					showToast(R.string.register_error_phone);
				} else if (phone.length() == 11 && phone.startsWith("1")) {
					phoneCodeSubmit();
				}
				break;
			case R.id.banding_register_rule:
				jmzgoRule.setTextColor(getResources().getColor(
						R.color.deeporange));
				Intent intent = new Intent(BandingAccountActivity.this,
						JMZRuleAcitivity.class);
				intent.putExtra(JMZRuleAcitivity.JMZ_RULE_FILE_NAME, "rule.xml");
				startActivity(intent);
				break;

			case R.id.banding_ensurebtn:
				if (flag == 1) {
					phone = phoneEdit.getText().toString();
					code = validcodeEdit.getText().toString();
					passowrd = passwordEdit.getText().toString();
					passwordAgain = passwordAgainEdit.getText().toString();
					if (phone.length() == 11 && phone.startsWith("1")) {
						if (code.length() == 4) {
							if (passowrd.length() >= 6) {
								if (passowrd == passwordAgain
										|| passowrd.equals(passwordAgain)) {
									if (isNetworkConnected()) {
										bandingSubmit();
									} else {
										showToast(R.string.msg_neterror);
									}
								} else {
									showToast(R.string.register_error_passwordagian);
								}
							} else {
								showToast(R.string.register_error_password);
							}
						} else {
							showToast(R.string.register_error_code);
						}
					} else {
						showToast(R.string.register_error_phone);
					}
				} else {
					phone = usenameedit.getText().toString();
					code = "";
					passowrd = passwordedit.getText().toString();
					if (usenameedit.getText().toString().length() < 11) {
						showToast(R.string.login_error_phone);
					} else if (passwordedit.getText().toString().length() < 6) {
						showToast(R.string.login_error_password);
					} else if (!isNetworkConnected()) {
						showToast(R.string.msg_neterror);
					} else {
						bandingSubmit();
					}
				}

				break;
			default:
				break;
			}
		}
	}

	/**
	 * 提交获取注册验证码
	 */
	private void phoneCodeSubmit() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Config.TAG_PHONENUMBER, phoneEdit.getText().toString());
		phoneCodeTask = new SubmitTask(this, Config.SMS_POST, ParentBean.class,
				new OnGetCodeSubmitListener(), false);
		phoneCodeTask.execute(map);
	}

	/**
	 * 提交绑定
	 */
	private void bandingSubmit() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Config.TAG_USERNAME, phone);
		map.put(Config.TAG_PASSWORD, passowrd);
		map.put(Config.TAG_REFRERERID, "0");
		map.put(Config.TAG_VALIDCODE, code);
		map.put(Config.TAG_OPENID, openID);
		map.put(Config.TAG_ACCESSTOKEN, accessToken);
		map.put(Config.TAG_ACCESSTYPE, accessType);
		map.put(Config.TAG_UNIONID, unionID);
		map.put(Config.TAG_EXPIRESIN, "");
		bandingTask = new SubmitTask(this, Config.OAUTHBIND, LoginBean.class,
				new OnBandingSubmitListener(), true);
		bandingTask.execute(map);
	}

	/**
	 * 响应获得手机验证码
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnGetCodeSubmitListener implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ParentBean parentBean = (ParentBean) result.getObject();
			if (parentBean != null) {
				if (Utile.isEqual(parentBean.getServerReturn(),
						Config.STATUSSUCCESS)) {
					showToast(R.string.register_say_code);
					validcodeEdit.setHint(R.string.register_say_code);
				} else {
					showToast(ServerReturnStatus.checkReturn(parentBean
							.getServerReturn()));
				}
			}
		}
	}

	/**
	 * 响应绑定账户
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnBandingSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			// TODO Auto-generated method stub
			LoginBean loginBean = (LoginBean) result.getObject();
			if (Utile
					.isEqual(loginBean.getServerReturn(), Config.STATUSSUCCESS)) {
				dbUser.setIsLogin(1);
				dbUser.setUserName(loginBean.getUserName().toString());
				dbUser.setPassWordString(loginBean.getPasswordString());
				dbUser.setUserId(loginBean.getUserID());

				dbUser.setPassWord("");
				dbUser.setIsRemeberPassword(0);
				
				setResult(1);
				finish();
			}else if (Utile.isEqual(loginBean.getServerReturn(), Config.OPENIDISNotBIND)) {//还没有绑定金拇指购账号
				showToast("还没有绑定金拇指购账号,请重新进行绑定");
			} else {
				showToast(ServerReturnStatus.checkReturn(loginBean
						.getServerReturn()));
			}
			
		}

	}

}
