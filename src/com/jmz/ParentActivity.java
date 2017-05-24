package com.jmz;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.OnTabActivityResultListener;
import com.jmz.uitl.RefreUtile;
import com.jmz.uitl.Utile;
import com.jmz.uitl.YMShare;
import com.jmz.view.MenuPopupWindow;
import com.jmz.view.TileTextView;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.testin.agent.TestinAgent;
import com.umeng.socialize.controller.UMSocialService;

/**
 * 父类
 * 
 * @author Administrator
 * 
 */
public class ParentActivity extends BaseActivity {
	public final static String MESSAGE_RECEIVED_ACTION = "message_received";
	public final static String KEY_MESSAGE = "key_message";
	public final static String KEY_EXTRAS = "key_extras";
	private TextView top_title;
	protected ImageView top_serchImg, top_promptImg, top_shareImg;
	protected ImageView top_logoImg;
	private String parent_appName;
	protected TextView top_logoCityText, top_classText,top_utilBtn;
	public int screenWidth;
	public int screenHeight;
	public UMSocialService mController;
	private MenuPopupWindow parent_menuPopupWindow;
	private Dialog parent_AlertDialog;
	protected boolean isFinishAvtivity = true;
	public boolean isStaticPage = true;
	protected TileTextView top_Home,top_Car;
	private EditText parent_msgEdt;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initlogoView(LayoutInflater.from(ParentActivity.this).inflate(
				R.layout.activity_logo, null));
		initTopView(LayoutInflater.from(ParentActivity.this).inflate(
				R.layout.activity_top, null));
		initcenterView(LayoutInflater.from(ParentActivity.this).inflate(
				R.layout.activity_emptyview, null));
		top_title = (TextView) findViewById(R.id.top_back);
		top_title.setOnClickListener(new OnParentClickImpl());
		top_serchImg = (ImageView) findViewById(R.id.top_serch);
		top_serchImg.setOnClickListener(new OnParentClickImpl());
		top_serchImg.setVisibility(View.GONE);
		top_classText = (TextView) findViewById(R.id.top_class);
		top_classText.setVisibility(View.GONE);
		top_utilBtn = (TextView) findViewById(R.id.top_utile);
		top_utilBtn.setVisibility(View.GONE);
		top_promptImg = (ImageView) findViewById(R.id.top_prompt);
		top_promptImg.setVisibility(View.GONE);
		top_shareImg = (ImageView) findViewById(R.id.top_share);
		top_shareImg.setVisibility(View.GONE);
		top_logoImg = (ImageView) findViewById(R.id.logo_img);
		top_logoCityText = (TextView) findViewById(R.id.logo_citytext);
		top_Home = (TileTextView) findViewById(R.id.top_home);
		top_Car = (TileTextView) findViewById(R.id.top_car);
		top_Home.setOnClickListener(new OnParentClickImpl());

		screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 获取屏幕宽
		screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 获取屏幕

		MyApplication.getInstance().getActivities().add(this);
		msgApi.registerApp(YMShare.WXAPPID);

		new RefreUtile(this, false).refreSubmit();
		isFinishAvtivity = getIntent().getBooleanExtra(
				TabBaseAcitivity.TAG_FINISH, true);
		if (!isFinishAvtivity) {
			base_topView.setVisibility(View.GONE);
		}
		
		TestinAgent.init(this, Config.TAG_TESTIN_APPKEY, Config.TAG_TESTIN_CHANNEL);
		TestinAgent.setLocalDebug(false);//设置为true，则在log中打印崩溃堆栈
		
		Intent i_getvalue = getIntent();  
		String action = i_getvalue.getAction();  
		  
		if(Intent.ACTION_VIEW.equals(action)){  
		    Uri uri = i_getvalue.getData();  
		    if(uri != null){  
		        String name = uri.getQueryParameter("name");  
		        String age= uri.getQueryParameter("age");  
		    }  
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getInstance().getActivities().remove(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		parent_menuPopupWindow = new MenuPopupWindow(this);
		if (base_bottomView != null) {
			parent_menuPopupWindow.showAtLocation(base_bottomView, Gravity.BOTTOM, 0, 0);
		}
		return false;
	}

	/**
	 * 退出设置，如果还在加载先停止加载在执行退出
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finishAcivity();
		}
		return false;
	}

	/**
	 * 关闭正在运行的task
	 */
	public void destroyTask() {
	}

	/**
	 * 关闭窗口
	 */
	public void finishAcivity() {
		if (isFinishAvtivity) {
			this.finish();
		}
	}

	/**
	 * 设置页面名
	 */
	public void setTittleText(int id) {
		parent_appName = getString(id);
		top_title.setText(parent_appName);
	}

	/**
	 * 设置页面名
	 */
	public void setTittleText(String str) {
		parent_appName = str;
		top_title.setText(parent_appName);
	}

	/**
	 * 弹出提示
	 * 
	 * @param str
	 */
	public void showToast(String str) {
		Toast.makeText(getBaseContext(), str, 1000).show();
	}

	/**
	 * 弹出提示
	 * 
	 * @param str
	 */
	public void showToast(int id) {
		Toast.makeText(getBaseContext(), getString(id), 1000).show();
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) ParentActivity.this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 提示提交连接服务器失败
	 */
	public void initNetError() {
		if (isStaticPage) {
			initcenterView(LayoutInflater.from(ParentActivity.this).inflate(
					R.layout.activity_neterror, null));
			ImageView button = (ImageView) findViewById(R.id.neterror_imgbtn);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					initLoadiagle();
					mainSubmit();
				}
			});
		}
	}

	/***
	 * 主要提交入口
	 */
	protected void mainSubmit() {

	}
	
	
	/**
	 * 微支付调用入口
	 * @param req
	 */
	public void sendPayReq(PayReq req) {
		if(msgApi.isWXAppInstalled()) {
			if(msgApi.isWXAppSupportAPI()) {
				msgApi.registerApp(YMShare.WXAPPID);
				msgApi.sendReq(req);
			}else 
				showToast(R.string.wx_no_supportAPI);
		}else {
			showToast(R.string.no_have_wx_client);
		}
		
	} 

	/**
	 * 获取一个组件的宽高
	 * 
	 * @param view
	 * @param which
	 *            true 宽 false高
	 * @return
	 */
	public int getHeightOrWidth(View view, Boolean which) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		if (which) {
			return view.getMeasuredWidth();
		} else {
			return view.getMeasuredHeight();
		}
	}

	/***
	 * 初始化加载页面
	 */
	public void initLoadiagle() {
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.dialog_loading, null);
		ImageView imageView = (ImageView) layout.findViewById(R.id.loadpageimg);
		RotateAnimation anim = new RotateAnimation(0f, 359f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(1000);
		imageView.setAnimation(anim);
		initcenterView(layout);
	}
	
	public void wxPayResult(BaseResp resp) {
		// TODO Auto-generated method stub
		Integer errCode = Integer.valueOf(String.valueOf(resp.errCode));
		switch (errCode) {
		case 0:
			showToast("支付成功");
			break;
		case -1:
			showToast("支付失败");
			break;
			
		case -2:
			showToast("取消支付");
			break;

		default:
			break;
		}
	}

	/**
	 * 取得页面的名字
	 * 
	 * @return
	 */
	public String getTittleText() {
		return parent_appName;
	}

	/**
	 * 显示对话框
	 * @param msg 显示内容
	 * @param listener 监听器
	 * @param visibility 是否打开输入框
	 */
	public void showMyAlertDialog(Integer msg, OnClickListener listener,int visibility) {
		parent_AlertDialog = new Dialog(this);
		LinearLayout myDialogLayout = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.dialog_nomal, null);
		TextView msgTv = (TextView) myDialogLayout
				.findViewById(R.id.myalerdialog_msg);
		msgTv.setText(getString(msg));
		TextView sureTv = (TextView) myDialogLayout
				.findViewById(R.id.myalerdialog_sure);
		parent_msgEdt = (EditText) myDialogLayout
				.findViewById(R.id.myalerdialog_edit);
		parent_msgEdt.setVisibility(visibility);
		if(visibility == View.GONE){
			msgTv.setText(getString(msg));
		}else if(visibility == View.VISIBLE){
			msgTv.setText(getString(R.string.msg_refund_reson));
		}
		TextView noTv = (TextView) myDialogLayout
				.findViewById(R.id.myalerdialog_cancel);
		parent_msgEdt.setMinHeight(screenHeight / 10);
		sureTv.setOnClickListener(listener);
		noTv.setOnClickListener(listener);
		parent_AlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		parent_AlertDialog.setContentView(myDialogLayout);
		// dialog.setCancelable(true);
		parent_AlertDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		parent_AlertDialog.getWindow().setLayout(screenWidth * 8 / 10,
				LayoutParams.WRAP_CONTENT);
		if (!parent_AlertDialog.isShowing()) {
			parent_AlertDialog.show();
		}
	}
	/**
	 * 当申请退款时取得退款理由
	 * @return
	 */
	public String getMsgEditText(){
		return parent_msgEdt.getText().toString();
	}
	/**
	 * 关闭对话框
	 */
	public void dissMyAlertDialog() {
		if (parent_AlertDialog != null) {
			parent_AlertDialog.dismiss();
		}
	}

	/**
	 * 父类单击事件监听
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnParentClickImpl implements OnClickListener {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.top_back:
				finishAcivity();
				break;
			case R.id.top_serch:
				startActivity(new Intent(ParentActivity.this,
						SearchActivity.class));
				break;
			case R.id.top_home:
				for (ParentActivity activity : MyApplication.getInstance()
						.getActivities()) {
//					if (!activity.getClass().getName()
//							.contains(HomeActivity.LOGTAG)
//							&& !activity.getClass().getName()
//									.contains(UserActivity.LOGTAG)
//							&& !activity.getClass().getName()
//									.contains(ProductClassActivity.LOGTAG)
//							&& !activity.getClass().getName()
//									.contains(FindServiceActivity.LOGTAG)) {
//						activity.finish();
//					}
					
					if(activity.getParent() == null ){
						activity.finish();
					}
				}
				break;
			default:
				break;
			}
		}
	}

}
