package com.jmz.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmz.R;
import com.jmz.bean.AddressList;
import com.jmz.bean.ShopContant;
import com.jmz.bean.ShopContantList;
import com.jmz.bean.ShopTel;
import com.jmz.uitl.Logger;
import com.jmz.uitl.Utile;

/**
 * 地址列表适配器
 * 
 * @author Administrator
 * 
 */
public class PopShopContactsAdapter extends BaseAdapter {
	private static final String LOGTAG = "PopShopContactsAdapter";
	private Context context;
	private ShopContantList list;
	private ViewHolder holder;
	private List<ShopContant> qqList;
	private List<ShopTel> telList;

	public PopShopContactsAdapter(Context context, ShopContantList list) {
		this.context = context;
		this.list = list;
		qqList = list.getShopContactsList(); 
		telList = list.getShopTelList();
	}

	@Override
	public int getCount() {
		return (qqList.size() + telList.size());
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.pop_shopcontacts_adapter, null);
			holder.name = (TextView) view.findViewById(R.id.pop_adapter_shopcontacts_title);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (position < qqList.size()) {
			holder.name.setBackgroundResource(R.drawable.qq);
			holder.name.setText(qqList.get(position).getName());
		}else {
			holder.name.setBackgroundResource(R.drawable.icon_call_item);
			holder.name.setText(telList.get(position-qqList.size()).getShopName() + ":" + telList.get(position-qqList.size()).getTelephone());
		}
		return view;
	}

	static class ViewHolder {
		protected TextView name;
	}

}
