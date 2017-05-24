package com.jmz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jmz.bean.AddressBean;
import com.jmz.bean.CityBean;
import com.jmz.bean.ParentBean;
import com.jmz.bean.ProvinceBean;
import com.jmz.bean.ProvinceList;
import com.jmz.bean.XianBean;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.ServerReturnStatus;
import com.jmz.uitl.Utile;

/**
 * 新增收货地址
 * 
 * @author hhy 2014-7-11
 */
public class AddressEditActivity extends ParentActivity {
	private TextView delete, save;
	private EditText street, name, phone, code;
	private CheckBox setAuto;
	private AddressBean addressBean;
	private ProvinceList provinceList;
	private TextView provinceTv, cityTv, xianTv;
	private List<String> ps;
	private List<String> cs;
	private List<String> xs;
	private int pn, cn, xn;
	private String url = Config.ADD_ADDRESS;
	private String userAddressID;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0 && provinceList != null
					&& provinceList.getProvinces() != null) {
				initcenterView(LayoutInflater.from(AddressEditActivity.this)
						.inflate(R.layout.activity_adressedit_centert, null));
				initMyView();
				if (AddressEditActivity.this.getIntent().getExtras() != null) {
					addressBean = (AddressBean) getIntent().getExtras()
							.getSerializable(AddressActivity.TAG_ADDRESS);
					url = Config.EDIT_ADDRESS;
					userAddressID = addressBean.getUserAddressID();
					for (int i = 0; i < provinceList.getProvinces().size(); i++) {
						if (addressBean.getRegion().contains(
								provinceList.getProvinces().get(i)
										.getProvince())) {
							provinceTv.setText(provinceList.getProvinces()
									.get(i).getProvince());
							pn = i;
							for (int j = 0; j < provinceList.getProvinces()
									.get(pn).getCitys().size(); j++) {
								if (addressBean.getRegion().contains(
										provinceList.getProvinces().get(pn)
												.getCitys().get(j).getCity())) {
									cityTv.setText(provinceList.getProvinces()
											.get(pn).getCitys().get(j)
											.getCity());
									cityTv.setVisibility(View.VISIBLE);
									cn = j;
									if (provinceList.getProvinces().get(pn)
											.getCitys().get(cn).getXians() != null) {
										for (int k = 0; k < provinceList
												.getProvinces().get(pn)
												.getCitys().get(cn).getXians()
												.size(); k++) {
											if (addressBean
													.getRegion()
													.contains(
															provinceList
																	.getProvinces()
																	.get(pn)
																	.getCitys()
																	.get(cn)
																	.getXians()
																	.get(k)
																	.getXian())) {
												xianTv.setText(provinceList
														.getProvinces().get(pn)
														.getCitys().get(cn)
														.getXians().get(k)
														.getXian());
												xianTv.setVisibility(View.VISIBLE);
												xn = k;
											}
										}
									}
								}
							}
						}
					}
					street.setText(addressBean.getStreet());
					name.setText(addressBean.getConsignee());
					phone.setText(addressBean.getTelephone());
					code.setText(addressBean.getZipCode());
					if (Utile.isEqual(addressBean.getIsDefault(), "True")) {
						setAuto.setChecked(true);
					} else {
						setAuto.setChecked(false);
					}
				} else {
					provinceTv.setText(provinceList.getProvinces().get(0)
							.getProvince());
				}
			}
		};
	};
	private HashMap<String, String> deleteMap;
	private SubmitTask deleteTask;
	private HashMap<String, String> saveMap;
	private SubmitTask saveAddressTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(R.string.adressedit_tittle);
		initLoadiagle();
		intProvince();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}

	/**
	 * 读取城市信息
	 */
	private void intProvince() {
		new Thread(new Runnable() {
			private Gson gson;

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					gson = new Gson();
					provinceList = gson.fromJson(Utile.getFromAssets(
							AddressEditActivity.this, "provinces.txt"),
							ProvinceList.class);
					handler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 初始化控件
	 */
	private void initMyView() {
		provinceTv = (TextView) findViewById(R.id.adress_edit_province);
		cityTv = (TextView) findViewById(R.id.adress_edit_citytv);
		xianTv = (TextView) findViewById(R.id.adress_edit_xian);
		provinceTv.setOnClickListener(new AdressEditOclickImpl());
		cityTv.setOnClickListener(new AdressEditOclickImpl());
		xianTv.setOnClickListener(new AdressEditOclickImpl());
		street = (EditText) findViewById(R.id.adress_edit_street);
		name = (EditText) findViewById(R.id.adress_edit_name);
		phone = (EditText) findViewById(R.id.adress_edit_phone);
		code = (EditText) findViewById(R.id.adress_edit_code);
		delete = (TextView) findViewById(R.id.adress_edit_delete);
		delete.setOnClickListener(new AdressEditOclickImpl());
		save = (TextView) findViewById(R.id.adress_edit_save);
		save.setOnClickListener(new AdressEditOclickImpl());
		setAuto = (CheckBox) findViewById(R.id.adress_edit_checkbox);
	}

	/**
	 * 修改与保存提交地址
	 */
	private void saveeditSumbit() {
		saveMap.put(Config.TAG_REGION, provinceTv.getText().toString()
				+ cityTv.getText().toString() + xianTv.getText().toString());
		saveMap.put(Config.TAG_STREET, street.getText().toString());
		saveMap.put(Config.TAG_ZIPCODE, code.getText().toString());
		saveMap.put(Config.TAG_TELEPHONE, phone.getText().toString());
		saveMap.put(Config.TAG_CONSIGNEE, name.getText().toString());
		saveMap.put(Config.TAG_USERADDRESSID, userAddressID);
		saveMap.put(Config.TAG_TYPE, "User");
		saveAddressTask = new SubmitTask(AddressEditActivity.this, url,
				ParentBean.class, new OnSaveEditSubmitListener(), false);
		saveAddressTask.execute(saveMap);
	}

	/**
	 * 删除地址
	 */
	private void deleteSumbit() {
		if (userAddressID != null) {
			deleteMap = new HashMap<String, String>();
			deleteMap.put(Config.TAG_USERADDRESSID, userAddressID);
			deleteMap.put(Config.TAG_TYPE, "User");
			deleteTask = new SubmitTask(AddressEditActivity.this,
					Config.DELETE_ADDRESS, ParentBean.class,
					new OnDeleteSubmitListener(), false);
			deleteTask.execute(deleteMap);
		} else {
			showToast(R.string.address_delete_fail);
		}
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (deleteTask != null) {
			deleteTask.destorySelf();
		}
		if (saveAddressTask != null) {
			saveAddressTask.destorySelf();
		}

	}

	/**
	 * 单击事件提交新地址
	 * 
	 * @author Administrator
	 * 
	 */
	private class AdressEditOclickImpl implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.adress_edit_save:
				saveMap = new HashMap<String, String>();
				if (setAuto.isChecked()) {
					saveMap.put(Config.TAG_ISDEFAULT, "1");
				} else {
					saveMap.put(Config.TAG_ISDEFAULT, "0");
				}
				if (street.getText().toString().length() <= 0) {
					showToast(R.string.address_edit_error_street);
				} else if (name.getText().toString().length() <= 0) {
					showToast(R.string.address_edit_error_name);
				} else if (phone.getText().toString().length() < 10) {
					showToast(R.string.address_edit_error_phone);
				} else if (code.getText().toString().length() < 6) {
					showToast(R.string.address_edit_error_code);
				} else {
					saveeditSumbit();
				}
				break;
			case R.id.adress_edit_delete:
				showMyAlertDialog(R.string.address_delete_msg, this,View.GONE);
				break;
			case R.id.myalerdialog_sure:
				deleteSumbit();
				break;
			case R.id.myalerdialog_cancel:
				dissMyAlertDialog();
				break;
			// 省
			case R.id.adress_edit_province:
				if (provinceList != null) {
					ps = new ArrayList<String>();
					for (ProvinceBean bean : provinceList.getProvinces()) {
						ps.add(bean.getProvince());
					}
					if (ps != null) {
						new AlertDialog.Builder(AddressEditActivity.this)
								.setTitle(
										getString(R.string.address_edit_provincetitle))
								.setSingleChoiceItems(
										(String[]) ps.toArray(new String[ps
												.size()]), pn,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												provinceTv.setText(ps
														.get(which));
												pn = which;
												cn = 0;
												xn = 0;
												if (provinceList.getProvinces()
														.get(pn).getCitys() != null) {
													cityTv.setText(provinceList
															.getProvinces()
															.get(pn).getCitys()
															.get(0).getCity());
													if (provinceList
															.getProvinces()
															.get(pn).getCitys()
															.get(cn).getXians() != null) {
														xianTv.setText(provinceList
																.getProvinces()
																.get(pn)
																.getCitys()
																.get(cn)
																.getXians()
																.get(0)
																.getXian());
														xianTv.setVisibility(View.VISIBLE);
													}
													cityTv.setVisibility(View.VISIBLE);
												} else {
													cityTv.setVisibility(View.GONE);
												}
												if (provinceList.getProvinces()
														.get(pn).getCitys()
														.get(cn).getXians() != null) {
													xianTv.setText(provinceList
															.getProvinces()
															.get(pn).getCitys()
															.get(cn).getXians()
															.get(xn).getXian());
													xianTv.setVisibility(View.VISIBLE);
												} else {
													xianTv.setVisibility(View.GONE);
												}
												dialog.dismiss();
											}
										}).show();
					}
				}
				break;
			// 市
			case R.id.adress_edit_citytv:
				cs = new ArrayList<String>();
				cs.clear();
				if (provinceList.getProvinces().get(pn).getCitys() != null) {
					for (CityBean bean : provinceList.getProvinces().get(pn)
							.getCitys()) {
						cs.add(bean.getCity());
					}
					if (cs != null) {
						new AlertDialog.Builder(AddressEditActivity.this)
								.setTitle(
										getString(R.string.address_edit_citytitle))
								.setSingleChoiceItems(
										(String[]) cs.toArray(new String[cs
												.size()]), cn,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												cityTv.setText(cs.get(which));
												cn = which;
												xn = 0;
												if (provinceList.getProvinces()
														.get(pn).getCitys()
														.get(cn).getXians() != null) {
													xianTv.setText(provinceList
															.getProvinces()
															.get(pn).getCitys()
															.get(cn).getXians()
															.get(0).getXian());
													xianTv.setVisibility(View.VISIBLE);
												} else {
													xianTv.setVisibility(View.GONE);
												}
												dialog.dismiss();
											}
										}).show();
					}
				}
				break;
			// 县
			case R.id.adress_edit_xian:
				xs = new ArrayList<String>();
				xs.clear();
				if (provinceList.getProvinces().get(pn).getCitys().get(cn)
						.getXians() != null) {
					for (XianBean bean : provinceList.getProvinces().get(pn)
							.getCitys().get(cn).getXians()) {
						xs.add(bean.getXian());
					}
					if (xs != null) {
						new AlertDialog.Builder(AddressEditActivity.this)
								.setTitle(
										getString(R.string.address_edit_xiantitle))
								.setSingleChoiceItems(
										(String[]) xs.toArray(new String[xs
												.size()]), xn,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												xianTv.setText(xs.get(which));
												xn = which;
												dialog.dismiss();
											}
										}).show();
					}
				}
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 响应保存和修改地址
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnSaveEditSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ParentBean bean = (ParentBean) result.getObject();
			if (bean.getServerReturn().equals("StatusSuccess")
					|| bean.getServerReturn() == "StatusSuccess") {
				if (userAddressID != null) {
					showToast(R.string.address_edit_edit);
				} else {
					showToast(R.string.address_edit_susse);
				}
				setResult(1);
				AddressEditActivity.this.finish();
			} else {
				showToast(ServerReturnStatus
						.checkReturn(bean.getServerReturn()));
			}

		}
	}

	/**
	 * 响应提交删除地址
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnDeleteSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ParentBean bean = (ParentBean) result.getObject();
			if (Utile.isEqual(Config.STATUSSUCCESS, bean.getServerReturn())) {
				showToast(R.string.address_edit_delete_suss);
				setResult(1);
				AddressEditActivity.this.finish();
			} else {
				showToast(ServerReturnStatus
						.checkReturn(bean.getServerReturn()));
			}
		}
	}
}
