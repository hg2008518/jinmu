package com.jmz;

import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmz.bean.FindPassWord;
import com.jmz.bean.ParentBean;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.ServerReturnStatus;
import com.jmz.uitl.Utile;

/**
 * 找回密码
 * 
 * @author Administrator
 * 
 */
public class FindPassWordActivity extends ParentActivity implements
		OnClickListener {
	private EditText usernameEdit, codeEdit, passwordEdit, passwordAgainEdit;
	private TextView smsCode, sure;
	private SubmitTask phoneCodeTask;
	private String phone;
	private String code;
	private String password;
	private String passwordAgain;
	private String phoneNumber = "";
	private HashMap<String, String> map;
	private TelephonyManager tm;
	private HashMap<String, String> phoneCodeMap;
	private LinearLayout ruleLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(getString(R.string.findpassword_tittle));
		tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getLine1Number() != null) {
			phoneNumber = tm.getLine1Number().startsWith("+86") ? tm
					.getLine1Number().substring(3) : tm.getLine1Number();
		}
		isStaticPage = false;
		initMyView();
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		// 如果Task还在运行，则先取消它
		if (phoneCodeTask != null) {
			phoneCodeTask.destorySelf();
		}
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(FindPassWordActivity.this).inflate(
				R.layout.activity_register_center, null));
		usernameEdit = (EditText) findViewById(R.id.register_phonenumber);
		codeEdit = (EditText) findViewById(R.id.register_code);
		passwordEdit = (EditText) findViewById(R.id.register_password);
		passwordAgainEdit = (EditText) findViewById(R.id.register_passwordagain);
		sure = (TextView) findViewById(R.id.register_sure);
		smsCode = (TextView) findViewById(R.id.register_sendsms);
		ruleLayout = (LinearLayout) findViewById(R.id.register_layout_rule);
		setDataToView();
	}

	/**
	 * 给组件赋值
	 */
	private void setDataToView() {
		usernameEdit.setHint(R.string.findpassword_phone);
		phoneNumber = getIntent().getStringExtra(Config.TAG_USERNAME);
		usernameEdit.setText(phoneNumber);
		sure.setOnClickListener(this);
		smsCode.setOnClickListener(this);
		ruleLayout.setVisibility(View.GONE);
	}

	/**
	 * 提交获取注册验证码
	 */
	private void phoneCodeSubmit() {
		map = new HashMap<String, String>();
		map.put(Config.TAG_PHONENUMBER, usernameEdit.getText().toString());
		phoneCodeTask = new SubmitTask(this, Config.SMS_POST, ParentBean.class,
				new OnRequestSubmitListener(), false);
		phoneCodeTask.execute(map);
	}

	/**
	 * 提交注册
	 */
	private void findPassWordSubmit() {
		phoneCodeMap = new HashMap<String, String>();
		phoneCodeMap
				.put(Config.TAG_USERNAME, usernameEdit.getText().toString());
		phoneCodeMap.put(Config.TAG_NEWPASSWORD, passwordAgainEdit.getText()
				.toString());
		phoneCodeMap.put(Config.TAG_VALIDCODE, codeEdit.getText().toString());
		phoneCodeTask = new SubmitTask(this, Config.USER_RESETPASSWORD,
				FindPassWord.class, new OnFindPassWordSubmitListener(), true);
		phoneCodeTask.execute(phoneCodeMap);
	}

	@Override
	public void onClick(View v) {

		phone = usernameEdit.getText().toString();
		code = codeEdit.getText().toString();
		password = passwordEdit.getText().toString();
		passwordAgain = passwordAgainEdit.getText().toString();

		switch (v.getId()) {
		case R.id.register_sendsms:
			if (phone.length() != 11 || !phone.startsWith("1")) {
				showToast(R.string.register_error_phone);
			} else {
				phoneCodeSubmit();
			}
			break;
		case R.id.register_sure:
			// if (phone.length() == 11 && phone.startsWith("1")) {
			// if (code.length() == 4) {
			// if (password.length() >= 6) {
			// if (password == passwordAgain
			// || password.equals(passwordAgain)) {
			// findPassWordSubmit();
			// } else {
			// showToast(R.string.register_error_passwordagian);
			// }
			// } else {
			// showToast(R.string.register_error_password);
			// }
			// } else {
			// showToast(R.string.register_error_code);
			// }
			// } else {
			// showToast(R.string.register_error_phone);
			// }
			if (phone.length() != 11 || !phone.startsWith("1")) {
				showToast(R.string.register_error_phone);
			} else if (code.length() != 4) {
				showToast(R.string.register_error_code);
			} else if (password.length() < 6 || passwordAgain.length() < 6) {
				showToast(R.string.register_hint_password);
			} else if (!Utile.isEqual(password, passwordAgain)) {
				showToast(R.string.register_error_passwordagian);
			} else {
				findPassWordSubmit();
			}
			break;

		default:
			break;
		}

	}

	/**
	 * 响应获得手机验证码
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnRequestSubmitListener implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ParentBean parentBean = (ParentBean) result.getObject();
			if (parentBean != null) {
				if (Utile.isEqual(parentBean.getServerReturn(),
						Config.STATUSSUCCESS)) {
					showToast(R.string.register_say_code);
				} else {
					showToast(ServerReturnStatus.checkReturn(parentBean
							.getServerReturn()));
				}
			}
		}
	}

	/**
	 * 响应注册
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnFindPassWordSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			FindPassWord findPassWordBean = (FindPassWord) result.getObject();
			if (findPassWordBean != null) {
				if (Utile.isEqual(findPassWordBean.getServerReturn(),
						Config.STATUSSUCCESS)) {
					showToast(R.string.findpassword_suss);
					setResult(1);
					finish();
				} else {
					showToast(ServerReturnStatus.checkReturn(findPassWordBean
							.getServerReturn()));
				}
			}
		}
	}
}
