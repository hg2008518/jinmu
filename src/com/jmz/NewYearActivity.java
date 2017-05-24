//package com.jmz;
//
//import java.util.HashMap;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//
//import com.jmz.bean.AwardList;
//import com.jmz.http.ApiResponse;
//import com.jmz.impl.OnSubmitListener;
//import com.jmz.submit.SubmitTask;
//import com.jmz.uitl.Config;
//import com.jmz.uitl.Logger;
//
///**
// * 新年活动
// * 
// * @author Administrator
// * 
// */
//public class NewYearActivity extends ParentActivity implements OnClickListener {
//	public static final String LOGTAG = "NewYearActivity";
//	private TextView cancel, msg, liuliang, hongbao;
//	private int tag = 0;
//	private AwardList awardList;
//	private SubmitTask task;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setTittleText(getString(R.string.back));
//		initMyView();
//	}
//
//	@Override
//	protected void mainSubmit() {
//		HashMap<String, String> newYearActmap = new HashMap<String, String>();
//		task = new SubmitTask(this, Config.NEWYEARE_AWARDST,
//				AwardList.class, new onAddressListSubmitListener(), true);
//		task.execute(newYearActmap);
//	}
//
//	/**
//	 * 初始化组件
//	 */
//	private void initMyView() {
//		initcenterView(LayoutInflater.from(NewYearActivity.this).inflate(
//				R.layout.activity_registerresutl_center, null));
//		msg = (TextView) findViewById(R.id.register_result_msg);
//		liuliang = (TextView) findViewById(R.id.register_result_liuliang);
//		hongbao = (TextView) findViewById(R.id.register_result_cancel);
//		liuliang.setOnClickListener(this);
//		hongbao.setOnClickListener(this);
//	}
//
//	@Override
//	public void destroyTask() {
//		super.destroyTask();
//		if (task != null) {
//			task.destorySelf();
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.register_result_cancel:
//			// Uri uri = Uri.parse(awardList.getAwardActionURL());
//			// Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//			// startActivity(intent);
//			NewYearActivity.this.finish();
//			break;
//		case R.id.register_result_liuliang:
////			showMyAlertDialog(R.string.register_result_liuliang_msg, this);
//			tag = 2;
//			break;
//		case R.id.myalerdialog_sure:
//			Uri smdUri = Uri.parse("smsto:10086");
//			Intent sendIntent = new Intent(Intent.ACTION_VIEW, smdUri);
//			sendIntent.putExtra("sms_body", "77536");
//			startActivity(sendIntent);
//			dissMyAlertDialog();
//			break;
//		case R.id.myalerdialog_cancel:
//			dissMyAlertDialog();
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	/**
//	 * 响应新年活动
//	 * 
//	 * @author Administrator
//	 * 
//	 */
//	private class onAddressListSubmitListener implements OnSubmitListener {
//
//		@Override
//		public void onSubmitSuccess(ApiResponse<Object> result) {
//			awardList = (AwardList) result.getObject();
//		}
//	}
//}
