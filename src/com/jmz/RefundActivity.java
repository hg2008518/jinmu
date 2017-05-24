package com.jmz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.jmz.bean.MyOrderShop;
import com.jmz.bean.ParentBean;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.OrderState;
import com.jmz.uitl.ServerReturnStatus;
import com.jmz.uitl.Utile;

/**
 * 退款管理
 * 
 * @author hhy 2014-7-11
 */
public class RefundActivity extends ParentActivity implements OnClickListener {
	private TextView sure;
	private TextView expType;
	private EditText expNumber;
	private List<String> list;
	private int postion = 0;
	private SubmitTask refundExpTask;
	private HashMap<String, String> refundExpMap;
	private MyOrderShop myOrderShop;
	private TextView producTitle;
	private SubmitTask expressPanyTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadiagle();
		setTittleText(R.string.refund_title);
		mainSubmit();
		list = new ArrayList<String>();
		myOrderShop = (MyOrderShop) getIntent().getExtras().getSerializable(
				MyOrderActivity.TAG_MYORDERSHOP);
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(RefundActivity.this).inflate(
				R.layout.activity_refund, null));
		expType = (TextView) findViewById(R.id.refund_exptype);
		producTitle = (TextView) findViewById(R.id.refund_exptype_title);
		expNumber = (EditText) findViewById(R.id.refund_expnumber);
		sure = (TextView) findViewById(R.id.refund_submit);
		sure.setOnClickListener(this);
		expType.setOnClickListener(this);
		producTitle.setText(myOrderShop.getTitle());
	}

	@Override
	protected void mainSubmit() {
		HashMap<String, String> expressCompanyMap = new HashMap<String, String>();
		expressCompanyMap.put(Config.TAG_TYPE, "List");
		expressCompanyMap.put(Config.TAG_EXPRESSNAME, "");
		expressCompanyMap.put(Config.TAG_ORDERTRADEORREFUND, "Refund");
		expressPanyTask = new SubmitTask(this, Config.EXPRESS_COMPANY,
				Map.class, new onExpressComPanySubmitListener(), false);
		expressPanyTask.execute(expressCompanyMap);
	}

	/**
	 * 提交退款物流
	 */
	protected void refundExpSubmit() {
		refundExpMap = new HashMap<String, String>();
		refundExpMap.put(Config.TAG_EXPRESSNUMBER, expNumber.getText()
				.toString());
		refundExpMap.put(Config.TAG_ORDERID, myOrderShop.getOrderID());
		refundExpMap.put(Config.TAG_EXPRESSCOMPANY, expType.getText()
				.toString());
		refundExpTask = new SubmitTask(this,
				Config.UPDATE_REFUNDSTATUS_EXPRESS, ParentBean.class,
				new onRefundExpressSubmitListener(), false);
		refundExpTask.execute(refundExpMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (refundExpTask != null) {
			refundExpTask.destorySelf();
		}
		if (expressPanyTask != null) {
			expressPanyTask.destorySelf();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.refund_exptype:
			new AlertDialog.Builder(RefundActivity.this)
					.setTitle(getString(R.string.refund_expmsg))
					.setSingleChoiceItems(
							(String[]) list.toArray(new String[list.size()]),
							postion, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									expType.setText(list.get(which));
									postion = which;
									dialog.dismiss();
								}
							}).show();
			break;
		case R.id.refund_submit:
			refundExpSubmit();
			break;

		default:
			break;
		}
	}

	/**
	 * 响应获取所有地址
	 * 
	 * @author Administrator
	 * 
	 */
	private class onExpressComPanySubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			initMyView();
			Map map = (Map) result.getObject();
			for (Object key : map.keySet()) {
				list.add((String) key);
			}
			expType.setText(list.get(0));
		}
	}

	/**
	 * 响应获取所有地址
	 * 
	 * @author Administrator
	 * 
	 */
	private class onRefundExpressSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ParentBean bean = (ParentBean) result.getObject();
			if (Utile.isEqual(bean.getServerReturn(), Config.STATUSSUCCESS)) {
				showToast(R.string.address_edit_susse);
				myOrderShop.setRefundStatus(OrderState.WaitSellerConfirmGoods.name());
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable(MyOrderActivity.TAG_MYORDERSHOP,
						myOrderShop);
				intent.putExtras(bundle);
				RefundActivity.this.setResult(MyOrderInfoActivity.TAG_REFUND, intent);
				RefundActivity.this.finish();
			} else {
				showToast(ServerReturnStatus
						.checkReturn(bean.getServerReturn()));
			}
		}
	}

}
