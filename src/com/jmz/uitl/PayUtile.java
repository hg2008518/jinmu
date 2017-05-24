package com.jmz.uitl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.hxcr.umspay.activity.Initialize;
import com.jmz.HomeActivity;
import com.jmz.OrderActivity;
import com.jmz.ParentActivity;
import com.jmz.PostOutActivity;
import com.jmz.R;
import com.jmz.WebViewAcitivity;
import com.jmz.bean.QmfOrder;
import com.jmz.bean.Trade;
import com.jmz.bean.WXOrder;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnBalancePayListener;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.zhifubao.Result;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.umeng.socialize.bean.HandlerRequestCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * 支付操作助手
 * 
 * @author Administrator
 * 
 */
public class PayUtile implements OnClickListener {
	public static final String LOGTAG = "PayUtile";
	public static final String TRADEID = "TradeID";
	public static final String ISQMFWEBPAY = "isQMFWebPay";
	public static final String CHRCODE = "ChrCode";
	public static final String TRANID = "TranID";
	public static final String MERSIGN = "MerSign";
	private Context context;
	private ParentActivity activity;
	public String tradeId, orderId;
	private SubmitTask tradeTask, qmfOrderTask, wxOrderTask;
	private String productTitle;
	// private String useName, passwordString, productTitle;
	public HashMap<String, String> map;
	private Dialog dialog;
	private LinearLayout dialogLayout;
	private CheckBox qmf;
	private CheckBox zhifubao;
	private CheckBox weixin;
	private TextView cancelPayBtn, surePayBtn;
	private String payOut;
	private PayReq req;
	private String payWay;
	private UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 1:
				activity.showToast((String) msg.obj);
				break;
			default:
				break;
			}
		};
	};
	private AliPay alipay;
	private ImageView zhifubaoImg;
	private ImageView qmfImg;
	private ImageView weixinImg;
	private RelativeLayout zhifubaoLayout;
	private RelativeLayout qmfLayout;
	private RelativeLayout wxLayout;
	private OnBalancePayListener listener;

	public PayUtile(Context context, HashMap<String, String> map,
			OnBalancePayListener listener) {
		this.context = context;
		this.map = map;
		this.listener = listener;
		activity = (ParentActivity) context;
		// this.useName = map.get(Config.TAG_USERNAME);
		// this.passwordString = map.get(Config.TAG_PASSWORD);
		this.productTitle = map.get(Config.TAG_TITLE);
		this.orderId = map.get(Config.TAG_ORDERIDS);
		this.tradeId = map.get(Config.TAG_TRADEID);
	}

	/**
	 * 销毁task
	 */
	public void destroy() {
		if (tradeTask != null
				&& tradeTask.getStatus() == AsyncTask.Status.RUNNING) {
			tradeTask.cancel(true);
		}
		if (qmfOrderTask != null
				&& qmfOrderTask.getStatus() == AsyncTask.Status.RUNNING) {
			qmfOrderTask.cancel(true);
		}
		if (wxOrderTask != null
				&& wxOrderTask.getStatus() == AsyncTask.Status.RUNNING) {
			wxOrderTask.cancel(true);
		}
	}

	/**
	 * 提交tradenumber
	 */
	public void tradeSubmit() {
		HashMap<String, String> tradeMap = new HashMap<String, String>();
		tradeMap.put(Config.TAG_TITLE, productTitle);
		tradeMap.put(Config.TAG_TRADETYPE, Config.TRADE_TYPE_GOUWU);
		tradeMap.put(Config.TAG_ORDERIDS, orderId);
		tradeTask = new SubmitTask(context, Config.TRADE_POST, Trade.class,
				new OnTradeSubmitListenerImpl(), true);
		tradeTask.execute(tradeMap);
	}

	/**
	 * 提交全民付
	 */
	private void qmfSubmit() {
		HashMap<String, String> qmfOrderMap = new HashMap<String, String>();
		qmfOrderMap.put(Config.TAG_TRADEID, tradeId);
		qmfOrderTask = new SubmitTask(context, Config.QMF_ORDER,
				QmfOrder.class, new OnQmfOrderSubmitListenerImpl(), true);
		qmfOrderTask.execute(qmfOrderMap);
	}

	/**
	 * 微信支付
	 * */
	private void weixin() {
		HashMap<String, String> wxOrderMap = new HashMap<String, String>();
		wxOrderMap.put(Config.TAG_TRADEID, tradeId);
		wxOrderMap.put(Config.TAG_TYPE, "APP");
		wxOrderMap.put(Config.TAG_OPENID, "");
		wxOrderTask = new SubmitTask(context, Config.WXPAY_ORDER,
				WXOrder.class, new OnWXOrderSubmitListenerImpl(), true);
		wxOrderTask.execute(wxOrderMap);
		
		// if (listener != null) {
		// new WXPayUtil(activity, listener);
		// }

	}

	private String a = "resultStatus={";
	private String b = "};memo={";
	private String c = "};result={";
	protected String str;

	/**
	 * 提交支付宝
	 */
	private void zhifubao() {
		new Thread() {
			public void run() {
				alipay = new AliPay(activity, mHandler);
				String result = alipay.pay(Utile.info(tradeId, productTitle,
						productTitle, payOut));
				if (result != null
						&& Utile.isEqual(result.substring(
								result.indexOf(a) + a.length(),
								result.indexOf(b)), "9000")) {
					if (listener != null) {
						listener.onBalancePaySuccess();
					}
				} else {
					str = result.substring(result.indexOf(b) + b.length(),
							result.indexOf(c));
					Message message = new Message();
					message.arg1 = 1;
					message.obj = str;
					mHandler.sendMessage(message);
					if (listener != null) {
						listener.onBalancePayFail();
					}
				}
			}
		}.start();
	}

	/**
	 * 显示选择支付方式
	 */
	public void showMiniDialog() {
		dialog = new Dialog(context);
		dialogLayout = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.dialog_chosepay, null);
		zhifubao = (CheckBox) dialogLayout.findViewById(R.id.select_zhifubao);
		qmf = (CheckBox) dialogLayout.findViewById(R.id.select_qmf);
		weixin = (CheckBox) dialogLayout.findViewById(R.id.select_wx);
		zhifubaoImg = (ImageView) dialogLayout
				.findViewById(R.id.select_img_zhifubao);
		qmfImg = (ImageView) dialogLayout.findViewById(R.id.select_img_qmf);
		weixinImg = (ImageView) dialogLayout.findViewById(R.id.select_img_wx);
		cancelPayBtn = (TextView) dialogLayout.findViewById(R.id.cancel_paybtn);
		cancelPayBtn.setOnClickListener(this);
		surePayBtn = (TextView) dialogLayout.findViewById(R.id.sure_paybtn);
		surePayBtn.setOnClickListener(this);
		zhifubaoLayout = (RelativeLayout) dialogLayout
				.findViewById(R.id.select_layout_zhifubao);
		qmfLayout = (RelativeLayout) dialogLayout
				.findViewById(R.id.select_layout_qmf);
		wxLayout = (RelativeLayout) dialogLayout
				.findViewById(R.id.select_layout_wx);
		zhifubao.setOnClickListener(this);
		qmf.setOnClickListener(this);
		weixin.setOnClickListener(this);
		zhifubaoImg.setOnClickListener(this);
		qmfImg.setOnClickListener(this);
		weixinImg.setOnClickListener(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(dialogLayout);
		dialog.setTitle(R.string.dialog_chosepaystyle);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		dialog.getWindow().setLayout(activity.screenWidth * 8 / 10,
				LayoutParams.WRAP_CONTENT);
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_zhifubao:
		case R.id.select_img_zhifubao:
		case R.id.select_layout_zhifubao:
			zhifubao.setChecked(true);
			qmf.setChecked(false);
			weixin.setChecked(false);
			//zhifubao();
			payWay = "zhifubao";
			//dialog.dismiss();
			break;
		case R.id.select_qmf:
		case R.id.select_img_qmf:
		case R.id.select_layout_qmf:
			zhifubao.setChecked(false);
			qmf.setChecked(true);
			weixin.setChecked(false);
			//qmfSubmit();
			payWay = "qmf";
			//dialog.dismiss();
			break;
		case R.id.select_wx:
		case R.id.select_img_wx:
		case R.id.select_layout_wx:
			zhifubao.setChecked(false);
			qmf.setChecked(false);
			weixin.setChecked(true);
			//weixin();
			payWay = "weixin";
			//dialog.dismiss();
			break;
		case R.id.cancel_paybtn:
			
			dialog.dismiss();
			break;
			
		case R.id.sure_paybtn:
			if (Utile.isEqual(payWay, "zhifubao")) {
				zhifubao();
			}else if (Utile.isEqual(payWay, "qmf")) {
				qmfSubmit();
			}else if (Utile.isEqual(payWay, "weixin")) {
				weixin();
			}
			
			dialog.dismiss();
			break;
		default:
			break;
		}
	}

	/**
	 * 响应用户新增交易
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnTradeSubmitListenerImpl implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			Trade trade = (Trade) result.getObject();
			if (Utile.isEqual(trade.getServerReturn(), Config.STATUSSUCCESS)) {
				tradeId = trade.getTradeID();
				payOut = trade.getPayout();
				showMiniDialog();
			} else {
				activity.showToast(ServerReturnStatus.checkReturn(trade
						.getServerReturn()));
			}
		}
	}

	/**
	 * 响应使用全民付下单
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnQmfOrderSubmitListenerImpl implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			QmfOrder qmfOrder = (QmfOrder) result.getObject();
			if (qmfOrder != null && qmfOrder.getTransId() != null) {
				Intent intent = new Intent();
				intent.setClass(activity, WebViewAcitivity.class);
				intent.putExtra(TRADEID, tradeId);
				intent.putExtra(ISQMFWEBPAY, true);
				intent.putExtra(PostOutActivity.CACHE, true);
				intent.putExtra(CHRCODE, qmfOrder.getChrCode());
				intent.putExtra(TRANID, qmfOrder.getTransId());
				intent.putExtra(MERSIGN, qmfOrder.getSignature());
				intent.putExtra("url", Config.QMFWEB_PAY);
				activity.startActivityForResult(intent, 0);

				/*
				 * StringBuffer buffer = new StringBuffer(); buffer = new
				 * StringBuffer(); String xml =
				 * buffer.append(qmfOrder.getSignature()).append("|")
				 * .append(qmfOrder.getChrCode()).append("|")
				 * .append(qmfOrder.getTransId()).append("|")
				 * .append(qmfOrder.getMerId()).toString(); Intent intent = new
				 * Intent(context, Initialize.class); intent.putExtra("xml",
				 * xml); intent.putExtra("istest", "0");// “0”为生产环境，其他为测试环境
				 * activity.startActivityForResult(intent, 0);
				 */
				if (Utile.isEqual(activity.getClass().getName(),
						OrderActivity.LOGTAG)) {
					activity.finish();
				}
			} else {
				activity.showToast("系统错误,可以换另一种支付方式哦");
			}
		}
	}

	/**
	 * 响应使用微信下单
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnWXOrderSubmitListenerImpl implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			// TODO Auto-generated method stub
			WXOrder wxOrder = (WXOrder) result.getObject();
			if (wxOrder != null && wxOrder.getSign() != null
					&& wxOrder.getServerReturn() == null) {
				req = new PayReq();
				req.appId = wxOrder.getAppid();
				req.partnerId = wxOrder.getPartnerid();
				req.prepayId = wxOrder.getPrepay_id();
				req.packageValue = wxOrder.getPackage();
				req.nonceStr = wxOrder.getNoncestr();
				req.timeStamp = wxOrder.getTimestamp();
				req.sign = wxOrder.getSign();
				
//				List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//				signParams.add(new BasicNameValuePair("appid", req.appId));
//				signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
//				signParams.add(new BasicNameValuePair("package", req.packageValue));
//				signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
//				signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
//				signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
//
//				req.sign = genAppSign(signParams);
				
				activity.sendPayReq(req);
//				listener.WXPaySingeSuccess(req);

			} else {
				activity.showToast("系统错误,可以换另一种支付方式哦");
			}

		}

	}
	
}
