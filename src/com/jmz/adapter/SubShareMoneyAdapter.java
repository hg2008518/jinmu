package com.jmz.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmz.R;
import com.jmz.bean.Less;

/**
 * sharemoney减少适配器
 * 
 * @author Administrator
 * 
 */
public class SubShareMoneyAdapter extends BaseAdapter {
	private Context context;
	private List<Less> lesses;
	private LinearLayout layout;
	private TextView time,from,money;
	private TextView state;

	public SubShareMoneyAdapter(Context context, List<Less> lesses) {
		this.context = context;
		this.lesses = lesses;
	}

	@Override
	public int getCount() {
		return lesses.size();
	}

	@Override
	public Object getItem(int position) {
		return lesses.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		layout = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.adapter_sharemoney_center_list, null);
		time = (TextView)layout.findViewById(R.id.sharemoney_listadater_time);
		from = (TextView)layout.findViewById(R.id.sharemoney_listadater_from);
		money = (TextView)layout.findViewById(R.id.sharemoney_listadater_money);
		state = (TextView)layout.findViewById(R.id.sharemoney_listadater_state);
		time.setText(lesses.get(position).getPostDate());
		from.setText(lesses.get(position).getTitle());
		money.setText("-"+lesses.get(position).getLess());
		state.setVisibility(View.GONE);
		return layout;
	}

}
