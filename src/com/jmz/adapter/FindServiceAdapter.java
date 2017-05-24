package com.jmz.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jmz.R;

/**
 * 客服适配器
 * 
 * @author Administrator
 * 
 */
public class FindServiceAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, Object>> list;
	private ViewHolder holder;

	public FindServiceAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		holder = new ViewHolder();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		RelativeLayout layout;
		layout = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.adapter_findservice_center_list, null);
		holder.text = (TextView) layout.findViewById(R.id.findservice_adapter_text);
		holder.left = (ImageView) layout.findViewById(R.id.findservice_adapter_left);
		holder.right = (ImageView) layout.findViewById(R.id.findservice_adapter_right);
		holder.text.setText(Html.fromHtml((String) list.get(position).get("info")));
		if ((Boolean) list.get(position).get("who")) {
			layout.setGravity(Gravity.LEFT);
			holder.left.setImageResource(R.drawable.wawa);
			holder.right.setVisibility(view.GONE);
			holder.left.setVisibility(view.VISIBLE);
			holder.text.setBackgroundResource(R.drawable.left_bg);
			if (holder.text.getText().toString().contains("400-871-7108")) {
				SpannableString sp = new SpannableString(holder.text.getText());
				// 设置超链接
				sp.setSpan(new URLSpan("tel:4008717108"), holder.text.getText()
						.toString().indexOf("400-871-7108"), holder.text.getText()
						.toString().indexOf("400-871-7108") + 12,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				// SpannableString对象设置给TextView
				holder.text.setText(sp);
				// 设置TextView可点击
				holder.text.setMovementMethod(LinkMovementMethod.getInstance());
			}
		} else {
			layout.setGravity(Gravity.RIGHT);
			holder.right.setImageResource(R.drawable.icon_user);
			holder.right.setVisibility(view.VISIBLE);
			holder.left.setVisibility(view.GONE);
			holder.text.setBackgroundResource(R.drawable.right_bg);
		}
		layout.setTag(holder);
		return layout;
	}
	private static class ViewHolder{
		protected TextView text;
		protected ImageView left;
		protected ImageView right;
	}
}
