package com.jmz.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmz.R;
import com.jmz.bean.Increase;
import com.jmz.uitl.Utile;
import com.jmz.view.MyTextView;

/**
 * sharemoney增加适配器
 * 
 * @author Administrator
 * 
 */
public class AddShareMoneyAdapter extends BaseAdapter {
	private static final String LOGTAG = "AddShareMoneyAdapter";
	private Context context;
	private List<Increase> increases;
	private ViewHolder holder;

	public AddShareMoneyAdapter(Context context, List<Increase> increases) {
		this.context = context;
		this.increases = increases;
	}

	@Override
	public int getCount() {
		return increases.size();
	}

	@Override
	public Object getItem(int position) {
		return increases.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	@SuppressLint("NewApi")
	public View getView(final int position, View view, ViewGroup parent) {
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.adapter_sharemoney_center_list, null);
			holder.time = (TextView) view
					.findViewById(R.id.sharemoney_listadater_time);
			holder.from = (MyTextView) view
					.findViewById(R.id.sharemoney_listadater_from);
			holder.money = (TextView) view
					.findViewById(R.id.sharemoney_listadater_money);
			holder.state = (TextView) view
					.findViewById(R.id.sharemoney_listadater_state);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.time.setText(increases.get(position).getPostDate());
		holder.from.setText(increases.get(position).getTitle());
		holder.money.setText("+"+increases.get(position).getIncrease());
		holder.state.setVisibility(View.VISIBLE);
		if (Utile.isEqual(increases.get(position).getIsLock(), "True")) {
			holder.state.setText("不可用");
		} else {
			holder.state.setText("可用");
		}
		return view;
	}

	static class ViewHolder {
		protected TextView time, money;
		protected TextView state;
		protected MyTextView from;
	}

}
