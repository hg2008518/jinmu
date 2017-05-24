package com.jmz.push;

import java.math.BigDecimal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.jmz.ParentActivity;
import com.jmz.PostOutActivity;
import com.jmz.ProductActivity;
import com.jmz.ProductClassActivity;
import com.jmz.SearchActivity;
import com.jmz.WebViewAcitivity;
import com.jmz.bean.Child;
import com.jmz.bean.MyReceiverBean;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.Utile;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyPushReceiver extends BroadcastReceiver {
	private Context context;
	private ParentActivity activity;

	public MyPushReceiver() {
	}

	public MyPushReceiver(Context context) {
		this.context = context;
	}

	// Bundle[{app=com.jmz, cn.jpush.android.NOTIFICATION_CONTENT_TITLE=金拇指购,
	// cn.jpush.android.NOTIFICATION_ID=1194302916,
	// cn.jpush.android.PUSH_ID=1194302916, cn.jpush.android.MSG_ID=1194302916,
	// cn.jpush.android.ALERT=法国拉菲庄园2012干红，支付成功后，标签推送-IOS-011,
	// cn.jpush.android.EXTRA={"nodeID":"105","propertyTypeID":"2","productID":"-1","propertyID":"-1","url":"-1"},
	// cn.jpush.android.NOTIFICATION_TYPE=0}]
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			// 接收RegistrationId
			// String regId = bundle
			// .getString(JPushInterface.EXTRA_REGISTRATION_ID);
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			// 接收到推送下来的自定义消息
			Logger.e("hhy", bundle.getString(JPushInterface.EXTRA_MESSAGE));

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			// 接收到推送下来的通知
			// int notifactionId =
			// bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			JPushInterface.reportNotificationOpened(context,
					bundle.getString(JPushInterface.EXTRA_MSG_ID));
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			// 用户点击打开了通知
			// 打开自定义的Activity
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			MyReceiverBean bean = new Gson().fromJson(extras,
					MyReceiverBean.class);
			Intent i = new Intent();
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if (new BigDecimal(bean.getA()).doubleValue() > 0
					&& new BigDecimal(bean.getB()).doubleValue() > 0) {
				i.setClass(context, ProductClassActivity.class);
				bundle.putSerializable(
						SearchActivity.CHILD,
						new Child(bean.getA(), "", bean.getC(), bean
								.getB()));
				i.putExtras(bundle);
				context.startActivity(i);
			}else if (new BigDecimal(bean.getD()).doubleValue() > 0
					&& new BigDecimal(bean.getB()).doubleValue() > 0) {
				i.setClass(context, ProductClassActivity.class);
				bundle.putSerializable(
						SearchActivity.CHILD,
						new Child("", bean.getD(), bean.getC(), "1"));
				i.putExtras(bundle);
				context.startActivity(i);
			}else if (new BigDecimal(bean.getE()).doubleValue() > 0) {
				i.setClass(context, ProductActivity.class);
				i.putExtra(Config.TAG_PUODUCTID, bean.getE());
				context.startActivity(i);
			} else if (bean.getF() != null
					&& !Utile.isEqual(bean.getF(), "-1")) {
				i.setClass(context, WebViewAcitivity.class);
				i.putExtra(PostOutActivity.URLSTRING, bean.getF());
				context.startActivity(i);
			}
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			// bundle.getString(JPushInterface.EXTRA_EXTRA)
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			// 已經连接
			// boolean connected = intent.getBooleanExtra(
			// JPushInterface.EXTRA_CONNECTION_CHANGE, false);
		} else {
			// 连接失败
			// Logger.e("hhy",
			// "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}
}
