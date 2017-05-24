package com.jmz.adapter;

import java.util.List;

import com.jmz.R;
import com.jmz.bean.ShopAddressBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class ShopViewAdapter extends BaseAdapter{
	
	private Context context;
	private List<ShopAddressBean> data;

	public ShopViewAdapter(Context context, List<ShopAddressBean> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(context,R.layout.activity_shopadress_view, null);
		// 通过列表项位置从集合中获取要显示的对象
		ShopAddressBean item = data.get(position);
		// 绑定数据到view
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			View listItem = adapter.getView(i, null, schedleListView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = schedleListView.getLayoutParams();
		params.height = totalHeight
				+ (schedleListView.getDividerHeight() * (adapter.getCount() - 1));
		schedleListView.setLayoutParams(params);
		
		return view;
	}


}
