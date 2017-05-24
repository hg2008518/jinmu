package com.jmz.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.jmz.MyApplication;
import com.jmz.ParentActivity;
import com.jmz.R;
import com.jmz.bean.DBUser;
import com.jmz.uitl.Config;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ContractQrPopupWindow extends PopupWindow {
	private LinearLayout layout;
	private ImageView qrImage;
	private TextView vailidDate;
	private ParentActivity activity;
	private Context context;
	private DBUser dbUser;

	public ContractQrPopupWindow(Context context, DBUser dbUser) {
		super(context);
		this.context = context;
		this.activity = (ParentActivity) context;
		this.dbUser = dbUser;
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.pop_contract_qr, null);
		qrImage = (ImageView) layout.findViewById(R.id.contract_qr_img);
		vailidDate = (TextView) layout.findViewById(R.id.contract_vaild_date);
		vailidDate.setText(dbUser.getContractDate());
		// 设置SelectPicPopupWindow的View
		this.setContentView(layout);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupAnimation);
		update();
		getQrc();
	}
	
	/**
	 * 生成二维码
	 */
	private void getQrc() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DefaultHttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(Config.REFERRER);
				List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
				nvps.add(new BasicNameValuePair(Config.TAG_UID,
						dbUser.getUserId())); // 参数
				nvps.add(new BasicNameValuePair("w", "150" ));
				nvps.add(new BasicNameValuePair("h", "150" ));
				HttpResponse response;
				try {
					httppost.setEntity(new UrlEncodedFormEntity(nvps,
							HTTP.UTF_8));
					response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					Bitmap pngBM = BitmapFactory.decodeStream(entity
							.getContent());
					qrImage.setImageBitmap(pngBM);
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
}
