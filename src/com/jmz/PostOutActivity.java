package com.jmz;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.jmz.bean.ParentBean;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.BalanceUtlie;
import com.jmz.uitl.Config;
import com.jmz.uitl.ServerReturnStatus;
import com.jmz.uitl.Utile;

/**
 * 提现
 * 
 * @author Administrator
 * 
 */
public class PostOutActivity extends ParentActivity {
	public static final String URLSTRING = "url";
	public static final String CACHE = "cache";
	private TextView sure, useMoneyTv, oneRule,twoRule,threeRule;
	private EditText moneyEdit;
	private TextView unUseMoneyTv;
	private SubmitTask postOutTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(R.string.postout_tittle);
		initMyView();
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(PostOutActivity.this).inflate(
				R.layout.activity_postout_center, null));
		sure = (TextView) findViewById(R.id.postout_sure);
		useMoneyTv = (TextView) findViewById(R.id.postout_usemoney);
		unUseMoneyTv = (TextView) findViewById(R.id.postout_unusemoney);
		oneRule = (TextView) findViewById(R.id.postout_one);
		twoRule = (TextView) findViewById(R.id.postout_two);
		moneyEdit = (EditText) findViewById(R.id.postout_moneyedit);
		useMoneyTv.setText(Html.fromHtml(getString(R.string.postout_havemoney)
				+ "<font color=\"#FF9933\">" +  MyApplication.getInstance().getDbUser().getUseMoney()
				+ getString(R.string.yuan) + "</font>"));
		unUseMoneyTv.setText(getString(R.string.sharemoney_tittleunuseonly)
				+  MyApplication.getInstance().getDbUser().getUnUseMoney() + getString(R.string.yuan));
		oneRule.setText(Html
				.fromHtml(getString(R.string.postout_one) + "<a href=\"\">"
						+ getString(R.string.postout_goreqmf) + "</a>。"));
		twoRule.setText(Html
				.fromHtml(getString(R.string.postout_two) + "<a href=\"\">"
						+ getString(R.string.postout_goqmf) + "</a>。"));
		oneRule.setOnClickListener(new OnPostOutClickListener());
		twoRule.setOnClickListener(new OnPostOutClickListener());
		sure.setOnClickListener(new OnPostOutClickListener());
	}

	/**
	 * 提现提交
	 */
	@Override
	protected void mainSubmit() {
		HashMap<String, String> postOutMap = new HashMap<String, String>();
		postOutMap.put(Config.TAG_INCOME, moneyEdit.getText().toString());
		postOutMap.put(Config.TAG_TRANSPLATFORM, "QmfPay");
		postOutTask = new SubmitTask(this, Config.POST_OUT, ParentBean.class,
				new OnPostOutSubmitListener(), true);
		postOutTask.execute(postOutMap);
	}
	@Override
	public void destroyTask() {
		super.destroyTask();
		if(postOutTask != null){
			postOutTask.destorySelf();
		}
	}
	/**
	 * 响应单击事件
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnPostOutClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.postout_one:
			case R.id.postout_two:
				Intent intent = new Intent(PostOutActivity.this,
						WebViewAcitivity.class);
				intent.putExtra(URLSTRING,Config.JINMUZHIGOUBANDQNF_URL);
				intent.putExtra(CACHE, true);
				startActivity(intent);
				break;
			case R.id.postout_sure:
				if (!isNetworkConnected()) {
					showToast(R.string.msg_neterror);
				} else if (moneyEdit.getText().toString().length() < 2
						|| Float.parseFloat(moneyEdit.getText().toString()) < 50) {
					showToast(R.string.postout_error_moneyinfo);
				} else {
					showMyAlertDialog(R.string.postout_suss_moneyinfo,this,View.GONE);
				}
				break;
			case R.id.myalerdialog_sure:
				mainSubmit();
				dissMyAlertDialog();
				break;
			case R.id.myalerdialog_cancel:
				dissMyAlertDialog();
				break;
			default:
				break;
			}
		}

	}


	/**
	 * 响应提现
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnPostOutSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ParentBean parentBean = (ParentBean) result.getObject();
			if (Utile.isEqual(parentBean.getServerReturn(), "StatusSuccess")) {
				showToast(R.string.postout_success);
				new BalanceUtlie(PostOutActivity.this,new Handler());
			} else {
				showToast(ServerReturnStatus.checkReturn(parentBean
						.getServerReturn()));
			}
		}

	}

}
