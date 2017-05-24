package com.jmz;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmz.adapter.MyOrderProductInfoAdapter;
import com.jmz.bean.MyOrderInfo;
import com.jmz.bean.MyOrderInfoList;
import com.jmz.bean.MyOrderShop;
import com.jmz.bean.OrderProduct;
import com.jmz.bean.QrCode;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnDeleteUtileListener;
import com.jmz.impl.OnSubmitListener;
import com.jmz.impl.OnUpdataUtileListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.DeleteOrderUtile;
import com.jmz.uitl.Logger;
import com.jmz.uitl.OrderState;
import com.jmz.uitl.PayUtile;
import com.jmz.uitl.UpdateOrderStateUtile;
import com.jmz.uitl.Utile;
import com.jmz.uitl.YMShare;
import com.jmz.uitl.YMShareTicket;
import com.jmz.view.MyListView;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 单个订单详情
 * 
 * @author Administrator
 * 
 */
public class MyTicketInfoActivity extends ParentActivity implements
		OnClickListener, OnUpdataUtileListener {

	private ImageView qrc;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1 && msg.obj != null) {
				bitmap = (Bitmap) msg.obj;
				qrc.setImageBitmap(bitmap);
				msg.what = 0;
			}
		};
	};
	private Bitmap bitmap;
	private TextView ticketTitle;
	private MyOrderInfoList infoList;
	private MyOrderInfo myTicketInfo;
	private TextView ticketMoney;
	private TextView ticketQuantity;
	private TextView ticketNumber;
	private TextView ticketOrderTime;
	private TextView ticketFinshTime;
	private TextView ticketPassword;
	private TextView ticketInfo;
	private ImageView stateImg;
	private TextView ticketState;
	private TextView useTicketQuantity;
	private YMShareTicket ymShareTicket;
	private SubmitTask oneTicketTask;
	// private Dialog MyticketInfoAlertDialog;
	private EditText msgEdt;
	private SubmitTask ticketTransferTask;
	private OrderProduct orderProduct;
	private TextView ticketQrcodeId;
	private LinearLayout codeLayout;
	private TextView refundBtn;
	private UpdateOrderStateUtile updateOrderStateUtile;
	private String ticketId;
	private TextView refundRule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(R.string.myorderinfo_tittle);
		initLoadiagle();
		ticketId = getIntent().getStringExtra(Config.TAG_ORDERID);
		mainSubmit();
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	}

	/**
	 * 初始化组件
	 * 
	 * @param codeLayout
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(MyTicketInfoActivity.this).inflate(
				R.layout.activity_myticketinfo_center, null));
		// shareImg.setVisibility(View.VISIBLE);
		ticketTitle = (TextView) findViewById(R.id.myticketinfo_center_tickettitle);
		ticketMoney = (TextView) findViewById(R.id.myticketinfo_center_money);
		ticketQuantity = (TextView) findViewById(R.id.myticketinfo_center_quantity);
		useTicketQuantity = (TextView) findViewById(R.id.myticketinfo_center_useticketnumber);
		ticketNumber = (TextView) findViewById(R.id.myticketinfo_center_ticketnumber);
		ticketOrderTime = (TextView) findViewById(R.id.myticketinfo_center_ordertime);
		ticketFinshTime = (TextView) findViewById(R.id.myticketinfo_center_finshtime);
		codeLayout = (LinearLayout) findViewById(R.id.myticketinfo_center_codelayout);
		ticketPassword = (TextView) findViewById(R.id.myticketinfo_center_password);
		ticketQrcodeId = (TextView) findViewById(R.id.myticketinfo_center_qrcodeid);
		ticketInfo = (TextView) findViewById(R.id.myticketinfo_center_info);
		ticketState = (TextView) findViewById(R.id.myticketinfo_center_orderstate);
		refundBtn = (TextView) findViewById(R.id.myticketinfo_center_refund);
		refundRule = (TextView) findViewById(R.id.myticketinfo_center_refundrule);
		qrc = (ImageView) findViewById(R.id.myticketinfo_center_passwordimg);
		stateImg = (ImageView) findViewById(R.id.myticketinfo_center_stateimg);
		setDataToView();
	}

	/**
	 * 为组件赋值
	 */
	private void setDataToView() {
		ticketTitle.setOnClickListener(this);
		top_shareImg.setOnClickListener(this);
		refundBtn.setOnClickListener(this);
		ticketTitle.setText(myTicketInfo.getTitle());
		ticketMoney.setText("订单金额：" + myTicketInfo.getOrderAmount() + "元");
		ticketQuantity.setText("购买数量："
				+ myTicketInfo.getProductNmber().toString() + "张");
		useTicketQuantity.setText("已使用票数："
				+ myTicketInfo.getOrderProductList().get(0)
						.getSendGoodsQuantity() + "张");
		ticketNumber.setText(" 订 单 号：" + myTicketInfo.getOrderID());
		ticketOrderTime.setText("下单时间：" + myTicketInfo.getOrderDate());
		ticketFinshTime.setText("有效时间：" + myTicketInfo.getExpireDate());

		ticketPassword.setText(" 验 证 码 ："
				+ myTicketInfo.getOrderProductList().get(0).getValidCode());
		ticketQrcodeId.setText(" 劵      号 ：" + orderProduct.getQrCodeID());
		refundRule.setText(Html
				.fromHtml(getString(R.string.myticket_refundmsg_head)
						+ "<u><font color ='#FF0000'>"
						+ getString(R.string.myticket_refundmsg_last)
						+ "</font></u>"));
		refundRule.setOnClickListener(this);
		if (Utile.isEqual(myTicketInfo.getRefundStatus(),
				OrderState.None.name())) {
			ticketState.setText("订单状态："
					+ OrderState.valueOf(myTicketInfo.getOrderStatus())
							.getName());
			if (Utile.isEqual(myTicketInfo.getOrderStatus(),
					OrderState.TradeFinished.name())
					&& Utile.isEqual(myTicketInfo.getOrderProductList().get(0)
							.getQuantity(), myTicketInfo.getOrderProductList()
							.get(0).getSendGoodsQuantity())) {
				stateImg.setVisibility(View.VISIBLE);
			} else if (Utile.isEqual(myTicketInfo.getOrderStatus(),
					OrderState.WaitBuyerConfirmGoods.name())
					|| Utile.isEqual(myTicketInfo.getOrderStatus(),
							OrderState.WaitSellerSendGoods.name())) {
				codeLayout.setVisibility(View.VISIBLE);

			}else if (Utile.isEqual(myTicketInfo.getOrderStatus(),
					OrderState.TradeFinished.name())
					&& !Utile.isEqual(myTicketInfo.getOrderProductList().get(0)
							.getQuantity(), myTicketInfo.getOrderProductList()
							.get(0).getSendGoodsQuantity())) {
				ticketState.setText("订单状态：已过期");
			}
		} else {
			ticketState.setText("订单状态："
					+ OrderState.valueOf(myTicketInfo.getRefundStatus())
							.getName());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mController != null) {
			/** 使用SSO授权必须添加如下代码 */
			UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
					requestCode);
			if (ssoHandler != null) {
				ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			}
			if (requestCode == 2 && resultCode == 2 && myTicketInfo != null
					&& !myTicketInfo.getOrderProductList().isEmpty()
					&& bitmap != null) {
				ymShareTicket.showImg(bitmap);
			}
		}
	}

	/**
	 * 提交单个订单的信息
	 */
	@Override
	protected void mainSubmit() {
		HashMap<String, String> oneOrderInfoMap = new HashMap<String, String>();
		oneOrderInfoMap.put(Config.TAG_ORDERID, ticketId);
		oneOrderInfoMap.put(Config.TAG_TYPE, "User");
		oneTicketTask = new SubmitTask(this, Config.ONE_ORDER_INFO,
				MyOrderInfoList.class, new onOneOrderInfoSubmitListener(),
				false);
		oneTicketTask.execute(oneOrderInfoMap);
	}

	/**
	 * 电子票务转发
	 */
	protected void ticketTransferSubmit() {
		HashMap<String, String> ticketTransferMap = new HashMap<String, String>();
		ticketTransferMap.put(Config.TAG_ORDERID, ticketId);
		ticketTransferMap
				.put(Config.TAG_PRODUCTID, orderProduct.getProductID());
		ticketTransferMap.put(Config.TAG_TRANSFERNUM, msgEdt.getText()
				.toString());
		ticketTransferTask = new SubmitTask(this, Config.TICKET_TRANSFER,
				QrCode.class, new TicketTransFerSubmitListener(), true);
		ticketTransferTask.execute(ticketTransferMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (oneTicketTask != null) {
			oneTicketTask.destorySelf();
		}
	}

	/**
	 * 生成二维码
	 */
	private void getQrc(final int position) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Config.TDC_QRC);
				List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
				nvps.add(new BasicNameValuePair(Config.TAG_USERNAME,
						MyApplication.getInstance().getDbUser().getUserName()));
				nvps.add(new BasicNameValuePair(Config.TAG_PASSWORD,
						MyApplication.getInstance().getDbUser()
								.getPassWordString())); // 参数
				nvps.add(new BasicNameValuePair(Config.TAG_TOKEN, Config.TOKEN)); // 参数
				nvps.add(new BasicNameValuePair(Config.TAG_QRCODEID,
						orderProduct.getQrCodeID()));
				nvps.add(new BasicNameValuePair(Config.TAG_TYPE, "User"));
				nvps.add(new BasicNameValuePair("Width", "" + screenWidth / 2));
				nvps.add(new BasicNameValuePair("Height", "" + screenWidth / 2));
				HttpResponse response;
				try {
					httppost.setEntity(new UrlEncodedFormEntity(nvps,
							HTTP.UTF_8));
					response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					Bitmap pngBM = BitmapFactory.decodeStream(entity
							.getContent());
					Message msg = new Message();
					msg.what = position;
					msg.obj = pngBM;
					handler.sendMessage(msg);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// /**
	// * 显示对话框
	// */
	// public void showOrderAlertDialog(OnClickListener listener) {
	// MyticketInfoAlertDialog = new Dialog(this);
	// LinearLayout myDialogLayout = (LinearLayout) LayoutInflater.from(this)
	// .inflate(R.layout.dialog_nomal, null);
	// TextView msgTv = (TextView) myDialogLayout
	// .findViewById(R.id.myalerdialog_msg);
	// msgTv.setText(R.string.myorder_msg_enternumber);
	// TextView sureTv = (TextView) myDialogLayout
	// .findViewById(R.id.myalerdialog_sure);
	// msgEdt = (EditText) myDialogLayout
	// .findViewById(R.id.myalerdialog_numedit);
	// TextView noTv = (TextView) myDialogLayout
	// .findViewById(R.id.myalerdialog_cancel);
	// sureTv.setOnClickListener(listener);
	// noTv.setOnClickListener(listener);
	// msgEdt.setVisibility(View.VISIBLE);
	// MyticketInfoAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// MyticketInfoAlertDialog.setContentView(myDialogLayout);
	// // dialog.setCancelable(true);
	// MyticketInfoAlertDialog.getWindow().setBackgroundDrawableResource(
	// android.R.color.transparent);
	// MyticketInfoAlertDialog.getWindow().setLayout(screenWidth * 8 / 10,
	// LayoutParams.WRAP_CONTENT);
	// if (!MyticketInfoAlertDialog.isShowing()) {
	// MyticketInfoAlertDialog.show();
	// }
	// }
	private Intent intent = new Intent();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myticketinfo_center_tickettitle:
			intent.setClass(MyTicketInfoActivity.this, ProductActivity.class);
			intent.putExtra(Config.TAG_PUODUCTID, myTicketInfo
					.getOrderProductList().get(0).getProductID());
			startActivity(intent);
			break;
		case R.id.top_share:

			break;
		case R.id.myticketinfo_center_refundrule:
			intent.setClass(MyTicketInfoActivity.this, WebViewAcitivity.class);
			intent.putExtra(PostOutActivity.URLSTRING,
					"file:///android_asset/refund_rule.htm");
			MyTicketInfoActivity.this.startActivity(intent);
			break;
		case R.id.myticketinfo_center_refund:
			showMyAlertDialog(R.string.myorder_msg_refund, this, View.VISIBLE);
			break;
		case R.id.myalerdialog_cancel:
			// MyticketInfoAlertDialog.dismiss();
			dissMyAlertDialog();
			break;
		case R.id.myalerdialog_sure:
			// MyticketInfoAlertDialog.dismiss();
			// ticketTransferSubmit();
			updateOrderStateUtile = new UpdateOrderStateUtile(this, this,
					myTicketInfo.getOrderID(), getMsgEditText());
			dissMyAlertDialog();
			updateOrderStateUtile.submit(true);
			break;
		default:
			break;
		}
	}

	/**
	 * 响应获取订单信息
	 * 
	 * @author Administrator
	 * 
	 */
	@SuppressLint("NewApi")
	private class onOneOrderInfoSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			infoList = (MyOrderInfoList) result.getObject();
			if (infoList != null && infoList.getOrderInfo() != null
					&& infoList.getOrderInfo().size() > 0) {
				myTicketInfo = infoList.getOrderInfo().get(0);
				orderProduct = myTicketInfo.getOrderProductList().get(0);
				initMyView();
				getQrc(1);
			} else {
				MyTicketInfoActivity.this.finish();
				showToast(R.string.myticket_exist);
			}
		}

	}

	/**
	 * 响应获取新的二维码id
	 * 
	 * @author Administrator
	 * 
	 */
	@SuppressLint("NewApi")
	private class TicketTransFerSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			// if(orderProduct != null && bitmap != null){
			// ymShareTicket = new YMShareTicket(MyTicketInfoActivity.this,
			// orderProduct, mController);
			// ymShareTicket.showImg(bitmap);
			// }
		}

	}

	@Override
	public void onUpdataUtileSuccess(boolean b, String OrderId) {
		mainSubmit();
	}

}
