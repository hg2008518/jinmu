package com.jmz;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jmz.adapter.AddressAdapter;
import com.jmz.bean.AddressBean;
import com.jmz.bean.AddressList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;

/**
 * 地址管理
 * 
 * @author hhy 2014-7-11
 */
public class AddressActivity extends ParentActivity {
	protected static final String TAG_ADDRESS = "address";
	private ListView listview;
	private TextView addNewAdress;
	private AddressAdapter adapter;
	private LinearLayout emptyView;
	private AddressList addressList;
	private AddressBean addressBean;
	private boolean isPay = false;
	private String userAddressId;
	private HashMap<String, String> map;
	private SubmitTask addressListTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadiagle();
		setTittleText(R.string.address_title);
		isPay = getIntent().getBooleanExtra(OrderActivity.ISPAY, false);
		userAddressId = getIntent().getIntExtra(Config.TAG_USERADDRESSID, 0)
				+ "";
		mainSubmit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 1) {
			mainSubmit();
		}
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(AddressActivity.this).inflate(
				R.layout.activity_adress_center, null));
		listview = (ListView) findViewById(R.id.adress_listview);
		emptyView = (LinearLayout) findViewById(R.id.activity_emptyview);
		listview.setEmptyView(emptyView);
		addNewAdress = (TextView) findViewById(R.id.adress_addnew);
		addNewAdress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddressActivity.this,
						AddressEditActivity.class);
				startActivityForResult(intent, 1);
			}
		});
	}

	@Override
	protected void mainSubmit() {
		map = new HashMap<String, String>();
		map.put(Config.TAG_TYPE, "User");
		addressListTask = new SubmitTask(this, Config.ADDRESS_LIST,
				AddressList.class, new onAddressListSubmitListener(), false);
		addressListTask.execute(map);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (addressListTask != null) {
			addressListTask.destorySelf();
		}
	}

	/**
	 * 响应获取所有地址
	 * 
	 * @author Administrator
	 * 
	 */
	private class onAddressListSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			initMyView();
			addressList = (AddressList) result.getObject();
			if (addressList != null && addressList.getUserAddressList() != null
					&& !addressList.getUserAddressList().isEmpty()) {
				adapter = new AddressAdapter(AddressActivity.this, addressList,
						isPay, userAddressId);
				listview.setAdapter(adapter);
				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						addressBean = addressList.getUserAddressList().get(
								position);
						if (addressBean != null) {
							if (isPay) {
								AddressActivity.this.setResult(Integer
										.parseInt(addressBean
												.getUserAddressID()));
								adapter.userAddressId = addressBean
										.getUserAddressID();
								setResult(Integer.parseInt(addressBean
										.getUserAddressID()));
								adapter.notifyDataSetChanged();
								AddressActivity.this.finish();
							} else {
								Intent intent = new Intent(
										AddressActivity.this,
										AddressEditActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable(TAG_ADDRESS, addressList
										.getUserAddressList().get(position));
								intent.putExtras(bundle);
								startActivityForResult(intent, 1);
							}
						}
					}
				});
			}
		}
	}
}
