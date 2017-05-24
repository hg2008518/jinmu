package com.jmz.view;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jmz.ParentActivity;
import com.jmz.R;
import com.jmz.adapter.PopShopContactsAdapter;
import com.jmz.bean.ShopContantList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;

/**
 * 商店客服
 * 
 * @author Administrator
 * 
 */
public class ShopContactsPopupWindow extends PopupWindow {

	private static final String LOGTAG = null;
	private LinearLayout layout;
	private ParentActivity activity;
	private String shopId;
	private SubmitTask task;
	private ListView listView;
	private ShopContantList list;
	private Context context;

	public ShopContactsPopupWindow(Context context, String shopId) {
		super(context);
		this.context = context;
		this.activity = (ParentActivity) context;
		this.shopId = shopId;
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.pop_shopcontacts,
				null);
		listView = (ListView) layout.findViewById(R.id.pop_shopcontacts_list);
		// 设置SelectPicPopupWindow的View
		this.setContentView(layout);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(android.R.color.transparent);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// 软键盘不会挡着popupwindow
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		if (shopId != null) {
			ShopContactsSubmit();
		}
		update();
	}

	/**
	 * 获取商店客服
	 */
	private void ShopContactsSubmit() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Config.TAG_SHOPID, shopId);
		task = new SubmitTask(activity, Config.SHOP_CONTACTS,
				ShopContantList.class, new onShopContactsListener(), true);
		task.execute(map);
	}

	/**
	 * 响应商店客服
	 * 
	 * @author Administrator
	 * 
	 */
	private class onShopContactsListener implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			list = (ShopContantList) result.getObject();
			if (list != null
					&& ((list.getShopContactsList() != null && list
							.getShopContactsList().size() > 0) || (list
							.getShopTelList() != null && list
							.getShopContactsList().size() > 0))) {
				PopShopContactsAdapter adapter = new PopShopContactsAdapter(
						activity, list);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent();
						if (position < list.getShopContactsList().size()) {
							
							intent.setAction("android.intent.action.VIEW");
							Uri content_url = Uri
									.parse("http://wpa.qq.com/msgrd?v=3&uin="
											+ list.getShopContactsList()
													.get(position).getQQ()
											+ "&site=qq&menu=yes");
							intent.setData(content_url);
						} else {
							intent.setAction(Intent.ACTION_CALL); 
							intent.setData(Uri.parse("tel:"
									+ list.getShopTelList()
									.get(position
											- list.getShopContactsList()
													.size())
									.getTelephone()));
							
						}
						activity.startActivity(intent);
						ShopContactsPopupWindow.this.dismiss();
					}
				});
			} else {
				ShopContactsPopupWindow.this.dismiss();
			}
		}
	}
}
