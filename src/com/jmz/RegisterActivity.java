package com.jmz;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.jmz.bean.ParentBean;
import com.jmz.bean.Register;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.ServerReturnStatus;
import com.jmz.uitl.Utile;

/**
 * 注册
 * 
 * @author Administrator
 * 
 */
public class RegisterActivity extends ParentActivity {
	private TextView sendSms, registerBtn, jmzgoRule;
	private EditText phoneEdit, codeEdit, passwordEdit, passwordAgainEdit,referrenNameEdit;
	private CheckBox agree;
	private SubmitTask phoneCodeTask;
	private String phone;
	private String code;
	private String passowrd;
	private String passwordAgain;
	private String phoneNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(getString(R.string.register_tittle));
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);

		if (tm.getLine1Number() != null) {
			if (tm.getLine1Number().startsWith("+86")) {
				phoneNumber = tm.getLine1Number().substring(3);
			} else {
				phoneNumber = tm.getLine1Number();
			}
		}
		initMyView();

		isStaticPage = false;

	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(RegisterActivity.this).inflate(
				R.layout.activity_register_center, null));
		sendSms = (TextView) findViewById(R.id.register_sendsms);
		registerBtn = (TextView) findViewById(R.id.register_sure);
		jmzgoRule = (TextView) findViewById(R.id.register_rule);
		jmzgoRule.setOnClickListener(new onRegiserClickImpl());
		jmzgoRule.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		phoneEdit = (EditText) findViewById(R.id.register_phonenumber);
		codeEdit = (EditText) findViewById(R.id.register_code);
		referrenNameEdit = (EditText) findViewById(R.id.register_referrername);
		passwordEdit = (EditText) findViewById(R.id.register_password);
		passwordAgainEdit = (EditText) findViewById(R.id.register_passwordagain);
		agree = (CheckBox) findViewById(R.id.register_agree);
		sendSms.setOnClickListener(new onRegiserClickImpl());
		registerBtn.setOnClickListener(new onRegiserClickImpl());
		phoneEdit.setText(phoneNumber);
		agree.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton button, boolean isCheck) {
				// if(isCheck){
				// registerBtn.setEnabled(true);
				// }else{
				// registerBtn.setEnabled(false);
				// }
				registerBtn.setEnabled(isCheck ? true : false);
			}
		});
	}

	/**
	 * 提交获取注册验证码
	 */
	private void phoneCodeSubmit() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Config.TAG_PHONENUMBER, phoneEdit.getText().toString());
		phoneCodeTask = new SubmitTask(this, Config.SMS_POST, ParentBean.class,
				new OnRequestSubmitListener(), false);
		phoneCodeTask.execute(map);
	}

	/**
	 * 提交注册
	 */
	private void registerSubmit() {
		HashMap<String, String> registerMap = new HashMap<String, String>();
		registerMap.put(Config.TAG_USERNAME, phoneEdit.getText().toString());
		registerMap.put(Config.TAG_PASSWORD, passwordEdit.getText().toString());
		registerMap.put(Config.TAG_REFRERERNAME, referrenNameEdit.getText().toString());
		registerMap.put(Config.TAG_VALIDCODE, codeEdit.getText().toString());
		phoneCodeTask = new SubmitTask(this, Config.USER_REGISTER,
				Register.class, new OnRegisterSubmitListener(), true);
		phoneCodeTask.execute(registerMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (phoneCodeTask != null) {
			phoneCodeTask.destorySelf();
		}
	}

	/**
	 * 单击事件响应
	 * 
	 * @author Administrator
	 * 
	 */
	private class onRegiserClickImpl implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.register_sendsms:
				phone = phoneEdit.getText().toString();
				if (!isNetworkConnected()) {
					showToast(R.string.msg_neterror);
				} else if (phone.length() != 11 || !phone.startsWith("1")) {
					showToast(R.string.register_error_phone);
				} else if (phone.length() == 11 && phone.startsWith("1")) {
					phoneCodeSubmit();
				}
				break;
			case R.id.register_rule:
				jmzgoRule.setTextColor(getResources().getColor(
						R.color.deeporange));
				Intent intent =new Intent(RegisterActivity.this,
						JMZRuleAcitivity.class);
				intent.putExtra(JMZRuleAcitivity.JMZ_RULE_FILE_NAME, "rule.xml");
				startActivity(intent);
				break;
			case R.id.register_sure:
				phone = phoneEdit.getText().toString();
				code = codeEdit.getText().toString();
				passowrd = passwordEdit.getText().toString();
				passwordAgain = passwordAgainEdit.getText().toString();
				if (phone.length() == 11 && phone.startsWith("1")) {
					if (code.length() == 4) {
						if (passowrd.length() >= 6) {
							if (passowrd == passwordAgain
									|| passowrd.equals(passwordAgain)) {
								if (isNetworkConnected()) {
									registerSubmit();
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
				break;

			default:
				break;
			}
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
					codeEdit.setHint(R.string.register_say_code);
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
	private class OnRegisterSubmitListener implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			Register registerBean = (Register) result.getObject();
			if (registerBean != null) {
				if (Utile.isEqual(registerBean.getServerReturn(),
						Config.STATUSSUCCESS)) {
					showToast(R.string.register_say_susse);
					startActivity(new Intent(RegisterActivity.this,
							LoginActivity.class));
					RegisterActivity.this.finish();
				} else {
					showToast(ServerReturnStatus.checkReturn(registerBean
							.getServerReturn()));
				}
			}
		}
	}
}
