package com.jmz.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jmz.R;
import com.jmz.bean.WuLiu;
import com.jmz.bean.WuLiuData;

/**
 * 物流信息适配器
 * 
 * @author Administrator
 * 
 */
public class WuliuAdapter extends BaseAdapter {
	private Context context;
	private WuLiu wuLiu;
	private ViewHolder holder;
	private List<WuLiuData> list;

	public WuliuAdapter(Context context, WuLiu wuLiu) {
		this.context = context;
		this.wuLiu = wuLiu;
		list = wuLiu.getData();
	}

	@Override
	public int getCount() {
		return list.size();
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
			view = LayoutInflater.from(context).inflate(R.layout.adapter_wuliu,
					null);
			holder.context = (TextView) view
					.findViewById(R.id.wuliu_adapter_context);
			holder.context.setText(list.get(position).getContext());
			holder.time = (TextView) view.findViewById(R.id.wuliu_adapter_time);
			holder.time.setText(list.get(position).getTime());
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		return view;
	}

	private class ViewHolder {
		protected TextView context, time;
	}

}
