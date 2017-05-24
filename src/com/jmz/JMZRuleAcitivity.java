package com.jmz;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jmz.uitl.Logger;
import com.jmz.uitl.Utile;

/**
 * 金拇指购协议
 * 
 * @author Administrator
 * 
 */
public class JMZRuleAcitivity extends ParentActivity {
	public final static String JMZ_RULE_FILE_NAME="filename";
	private TextView info;
	private String rule;
	private String fileName;
	private Handler handler =new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				initcenterView(LayoutInflater.from(JMZRuleAcitivity.this).inflate(
						R.layout.activity_rule_center, null));
				info = (TextView) findViewById(R.id.jmzgoinfo);
				info.setText(Html.fromHtml(rule));
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(R.string.rule);
		initLoadiagle();
		fileName = getIntent().getStringExtra(JMZ_RULE_FILE_NAME);
		Logger.e("hhy", fileName);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					rule = Utile.getFromAssets(
							JMZRuleAcitivity.this, fileName);
					if(rule != null){
						handler.sendEmptyMessage(0);	
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}).start();

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
}
