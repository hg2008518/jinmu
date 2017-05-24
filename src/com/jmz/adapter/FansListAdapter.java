package com.jmz.adapter;

import java.util.ArrayList;
import java.util.List;

import com.jmz.R;
import com.jmz.adapter.AddShareMoneyAdapter.ViewHolder;
import com.jmz.bean.Fans;
import com.jmz.bean.Increase;
import com.jmz.view.MyTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FansListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Fans> list;
	private ViewHolder holder;
	
	public FansListAdapter (Context context, ArrayList<Fans> list) {
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.adapter_fans_list, null);
			holder.name = (TextView) view.findViewById(R.id.fans_name);
			holder.time = (TextView) view.findViewById(R.id.fans_time);
			holder.ordersHeat=(TextView) view.findViewById(R.id.fans_ordersHeat);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.name.setText(list.get(position).getUserName());
		holder.time.setText(list.get(position).getCreateDate());
		holder.ordersHeat.setText("下单"+list.get(position).getOrderTotal()+"笔");
	
		return view;
	}
	
	static class ViewHolder {
		protected TextView time,  name,ordersHeat;
	}

}
