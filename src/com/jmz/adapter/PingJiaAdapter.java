package com.jmz.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.jmz.bean.AddressList;
/**
 * 地址列表适配器
 * @author Administrator
 *
 */
public class PingJiaAdapter extends BaseAdapter {
	private Context context;
	private AddressList addressList;

	public PingJiaAdapter(Context context, AddressList addressList) {
		this.context = context;
		this.addressList = addressList;
	}

	@Override
	public int getCount() {
		return 0;
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
		RelativeLayout layout = null;
		if (view == null) {
		} else {
			layout = (RelativeLayout) view;
		}
		return layout;
	}

}
