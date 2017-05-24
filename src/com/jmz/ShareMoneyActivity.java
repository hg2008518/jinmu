package com.jmz;

import java.util.HashMap;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import com.jmz.bean.DBUser;
import com.jmz.bean.FansList;
import com.jmz.bean.OnlyMoney;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.ServerReturnStatus;
import com.jmz.uitl.Utile;
import com.jmz.view.ContractFansListPopupWindow;
import com.jmz.view.ContractQrPopupWindow;
import com.jmz.view.PullToRefreshLayout;
import com.jmz.view.ShareMoneyPopupWindow;

/**
 * 播券奖金
 * 
 * @author Administrator
 * 
 */
public class ShareMoneyActivity extends ParentActivity implements
		OnClickListener {
	private static final String ADD = "add";
	private static final String CON = "con";
	private static final String SUB = "sub";
	private TextView useMoneyTv, bogomoney, contractmoney, fansbutton, qrbutton,sharemoney_usemoney_canuse,
	  				sharemoney_bogomoney_daiding,sharemoney_contractmoney_substitution,sharemoney_addtab,sharemoney_contracttab;
	private TabHost tabHost;
	private RadioGroup radioGroup;
	private RadioButton contractbutton,subbutton;
	private LocalActivityManager mLocalActivityManager;
	private LinearLayout contractFansQrLayout, contractMoneyLayout;
	private ShareMoneyPopupWindow promptPopupWindow;
	private ContractFansListPopupWindow fansListWindow;
	private ContractQrPopupWindow qrWindow;
	private DBUser dbUser;
	private SubmitTask moneyTask, fansTask;
	private int fansTotal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(R.string.sharemoney_tittle);
		mLocalActivityManager = new LocalActivityManager(this, false);
		mLocalActivityManager.dispatchCreate(savedInstanceState);
		top_promptImg.setVisibility(View.VISIBLE);
		top_promptImg.setOnClickListener(this);
		dbUser = MyApplication.getInstance().getDbUser();
		initMyView();
		sharemoney_addtab.setOnClickListener(this);
		sharemoney_contracttab.setOnClickListener(this);
		
		if(dbUser.getIsContract() == 1) {
			fansListWindow = new ContractFansListPopupWindow(ShareMoneyActivity.this, dbUser);
			qrWindow = new ContractQrPopupWindow(this, dbUser);
			ContractFansSubmit();
		}

	}
	
	@Override
	public void destroyTask() {
		super.destroyTask();
		if (moneyTask != null) {
			moneyTask.destorySelf();
		}
		if (fansTask != null) {
			fansTask.destorySelf();
		}
	}
	
	/**
	 * 粉丝列表提交
	 */
	private void ContractFansSubmit() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Config.TAG_USERNAME, dbUser.getUserName());
		map.put(Config.TAG_PASSWORD, dbUser.getPassWordString());
		map.put(Config.TAG_PAGE, "1");
		fansTask = new SubmitTask(ShareMoneyActivity.this, Config.FANS_LIST,FansList.class,
				new onFansListSubmit(), false);
		fansTask.execute(map);
		
	}

	/**
	 * 响应粉丝列表查询
	 * 
	 * @author Administrator
	 * 
	 */
	private class onFansListSubmit implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			FansList fanslist = (FansList) result.getObject();
			if (fanslist != null && !fanslist.getData().isEmpty()
					&& fanslist.getData().get(0).getPageTotal() != null) {
				fansTotal = Integer.parseInt(fanslist.getData().get(0)
						.getFansTotal());
				fansbutton.setText("我的粉丝(" + fansTotal + ")");
				
			} 
			
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
				new onOnlyMoneySubmit(), false);
		moneyTask.execute(map);
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
			useMoneyTv.setText(Html.fromHtml("<font color = '#ff9933'>"
					+ money.getAvailable().toString() + "</font>"
					+ getString(R.string.yuan)));
			sharemoney_usemoney_canuse.setText(Html.fromHtml("<font color = '#ff9933'>"+"</font>" + "("+"可用"
					+ money.getAvailable().toString() + "</font>"
					+ getString(R.string.yuan)+")"));
			bogomoney.setText(Html.fromHtml("<font color = '#fb73de'>"
					+ money.getBoGoAvailableSum().toString() +"</font>"+ getString(R.string.yuan)));
			sharemoney_bogomoney_daiding.setText(Html.fromHtml("<font color = '#fb73de'>"
					+"</font>" + "("+"待定"
					+ money.getBoGoNotAvailableSum().toString() + "元)"));
			contractmoney.setText(Html.fromHtml("<font color = '#82a8fc'>"
					+ money.getContractAvailableSum().toString() + "</font>"
					+ "元" ));
			sharemoney_contractmoney_substitution.setText(Html.fromHtml("<font color = '#82a8fc'>"+"(待定"
					+ money.getContractAvailableSum().toString() + "</font>"
					+ "元" )+")");
			int maxHeight = Utile.getMax(Utile.getMax(useMoneyTv.getHeight(),bogomoney.getHeight()),contractmoney.getHeight());
			useMoneyTv.setHeight(maxHeight);
			bogomoney.setHeight(maxHeight);
			contractmoney.setHeight(maxHeight);
		}
	}
	
	/**
	 * 初始化组件
	 */
	private void initMyView() {
		tabHost = (TabHost) LayoutInflater.from(ShareMoneyActivity.this)
				.inflate(R.layout.activity_sharemoney_center, null);
		initcenterView(tabHost);
		contractFansQrLayout = (LinearLayout) findViewById(R.id.contract_fans_qr_layout);
		contractMoneyLayout = (LinearLayout) findViewById(R.id.contract_money_layout);
		fansbutton = (TextView) findViewById(R.id.contract_fans);
		fansbutton.setOnClickListener(this);
		qrbutton = (TextView) findViewById(R.id.contract_qr);
		qrbutton.setOnClickListener(this);
		useMoneyTv = (TextView) findViewById(R.id.sharemoney_usemoney);
		sharemoney_usemoney_canuse=(TextView) findViewById(R.id.sharemoney_usemoney_canuse);
		bogomoney = (TextView) findViewById(R.id.sharemoney_bogomoney);
		sharemoney_bogomoney_daiding=(TextView) findViewById(R.id.sharemoney_bogomoney_daiding);
		contractmoney = (TextView) findViewById(R.id.sharemoney_contractmoney);
		sharemoney_contractmoney_substitution=(TextView) findViewById(R.id.sharemoney_contractmoney_substitution);
		contractbutton = (RadioButton) findViewById(R.id.sharemoney_contract);
		subbutton = (RadioButton) findViewById(R.id.sharemoney_add);
		sharemoney_addtab=(TextView) findViewById(R.id.sharemoney_addtab);
		sharemoney_contracttab=(TextView) findViewById(R.id.sharemoney_contracttab);
		if (dbUser.getIsContract() == 0) {
			contractFansQrLayout.setVisibility(View.GONE);
			contractMoneyLayout.setVisibility(View.GONE);
			contractbutton.setVisibility(View.GONE);
		}
		radioGroup = (RadioGroup) findViewById(R.id.sharemoney_radiogroup);
		tabHost.setup(mLocalActivityManager);
		Intent intent = new Intent();
		intent.putExtra("Type", "BoGo");
		intent.setClass(this, ShareMoneyAddActivity.class);
		tabHost.addTab(tabHost.newTabSpec(ADD).setIndicator(ADD)
				.setContent(intent));
		intent.putExtra("Type", "Contract");
		tabHost.addTab(tabHost.newTabSpec(CON).setIndicator(CON)
				.setContent(intent));
		tabHost.addTab(tabHost.newTabSpec(SUB).setIndicator(SUB)
				.setContent(new Intent(this, ShareMoneySubActivity.class)));
		tabHost.setCurrentTab(0);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.sharemoney_add:
					tabHost.setCurrentTab(0);
					break;
				case R.id.sharemoney_contract:
					tabHost.setCurrentTab(1);
					break;
				case R.id.sharemoney_sub:
					tabHost.setCurrentTab(2);
					break;
				default:
					break;
				}
			}
		});

		UseMoneySubmit();
	}

	
	@Override
	public void onClick(View v) {
		promptPopupWindow = new ShareMoneyPopupWindow(ShareMoneyActivity.this);
		switch (v.getId()) {
		case R.id.top_prompt:
			if (!promptPopupWindow.isShowing()) {
				promptPopupWindow.showAtLocation(tabHost, Gravity.BOTTOM, 0, 0);
			} else {
				promptPopupWindow.dismiss();
			}
			break;
		case R.id.contract_fans:
			
			if (!fansListWindow.isShowing()) {
				//fansListWindow.showAtLocation(contractMoneyLayout, Gravity.BOTTOM, 0, 0);
				fansListWindow.setHeight(screenHeight/2);
				fansListWindow.showAsDropDown(contractFansQrLayout, 0, 0);
			} else {
				fansListWindow.dismiss();
			}
			break;
		case R.id.contract_qr:
			if (qrWindow.isShowing()) {
				qrWindow.dismiss();
			}else {
				int offx = screenWidth/2;
				qrWindow.showAsDropDown(contractFansQrLayout, offx, 0);
			}
			break;
		case R.id.sharemoney_addtab://播购奖金
			tabHost.setCurrentTab(0);
			subbutton.setChecked(true);
			break;
		case R.id.sharemoney_contracttab://推广奖金
			tabHost.setCurrentTab(1);
			contractbutton.setChecked(true);
			break;
		default:
			break;
		}

	}
}
