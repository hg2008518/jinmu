package com.jmz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jmz.adapter.FindServiceAdapter;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.Utile;
import com.jmz.view.TileTextView;

/**
 * 联系客服
 * 
 * @author hhy 2014-7-11
 */
public class FindServiceActivity extends ParentActivity {
	public static final String LOGTAG = "FindServiceActivity";
	private ListView listView;
	private EditText editText;
	private TextView send;
	private List<Map<String, Object>> list;
	private HashMap<String, Object> hashMap;
	private FindServiceAdapter adapter;
	private List<String> answerList;
	private TileTextView one;
	private TileTextView two;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(R.string.user_findservice);
		initMyView();
		list = new ArrayList<Map<String, Object>>();
		adapter = new FindServiceAdapter(this, list);
		listView.setAdapter(adapter);
		hashMap = new HashMap<String, Object>();
		hashMap.put("info", getString(R.string.findservice_initword));
		hashMap.put("who", true);
		list.add(hashMap);
		answerList = Arrays.asList(Utile.getFromAssets(this,
				"quetion.txt").split("%"));
		
		isStaticPage = false;
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(FindServiceActivity.this).inflate(
				R.layout.activity_findservice_center, null));
		listView = (ListView) findViewById(R.id.findservice_list);
		listView.setSelector(android.R.color.transparent);
		editText = (EditText) findViewById(R.id.findservice_edit);
		send = (TextView) findViewById(R.id.findservice_send);
		one= (TileTextView) findViewById(R.id.findservice_online_one);
		two= (TileTextView) findViewById(R.id.findservice_online_two);
		send.setOnClickListener(new FindServiceOnClickListener());
		one.setOnClickListener(new FindServiceOnClickListener());
		two.setOnClickListener(new FindServiceOnClickListener());
		
	}
	
	/**
	 * 单击事件听听
	 * 
	 * @author Administrator
	 * 
	 */
	private class FindServiceOnClickListener implements OnClickListener {
		private String answer;
		private boolean haveAnswer;
		private Intent intent = new Intent();
		private Uri content_url;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.findservice_send:
				if (editText.getText().toString().length() > 0) {
					hashMap = new HashMap<String, Object>();
					hashMap.put("info", editText.getText().toString());
					hashMap.put("who", false);
					list.add(hashMap);
					for (String s : answerList) {
						if (editText.getText().toString().replace(" ", "")
								.length() > 0) {
							if (s.substring(0, s.indexOf("？")).contains(editText.getText().toString())) {
								answer = s;
								haveAnswer = true;
								break;
							}else{
								haveAnswer = false;
							}
						}
					}
					if (!haveAnswer) {
						answer = getString(R.string.findservice_initword);
					}
					hashMap = new HashMap<String, Object>();
					hashMap.put("info", answer);
					hashMap.put("who", true);
					list.add(hashMap);
				} else {
					showToast(R.string.findservice_hint);
				}
				adapter.notifyDataSetChanged();
				editText.setText("");
				break;
			case R.id.findservice_online_one:
				intent.setAction("android.intent.action.VIEW");
				content_url = Uri.parse(Config.SHASHA);
				intent.setData(content_url);
				startActivity(intent);
				break;
			case R.id.findservice_online_two:
				intent.setAction("android.intent.action.VIEW");
				content_url = Uri.parse(Config.BAOBAO);
				intent.setData(content_url);
				startActivity(intent);
				break;
			default:
				break;
			}
		}

	}
}
