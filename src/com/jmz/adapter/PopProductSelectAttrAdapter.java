package com.jmz.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.jmz.R;
import com.jmz.bean.ProductAttrGroup;
import com.jmz.uitl.Logger;
import com.jmz.view.FlowRadioGroup;

/**
 * 地址列表适配器
 * 
 * @author Administrator
 * 
 */
public class PopProductSelectAttrAdapter extends BaseAdapter {
	public static final String LOGTAG = "PopProductSelectAttrAdapter";
	public static final String SELECTEDATTR = "SelectedAttr";
	private Context context;
	private Handler handler;
	private ViewHolder holder;
	private ArrayList<ProductAttrGroup> productAttrGroups;

	public PopProductSelectAttrAdapter(Context context,
			ArrayList<ProductAttrGroup> productAttrGroups, Handler handler) {
		this.context = context;
		this.handler = handler;
		this.productAttrGroups = productAttrGroups;
	}

	public void setProductAttrGroups(
			ArrayList<ProductAttrGroup> productAttrGroups) {
		this.productAttrGroups = productAttrGroups;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return productAttrGroups.size();
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
					R.layout.pop_adapter_product_selectattr, null);
			holder.name = (TextView) view
					.findViewById(R.id.pop_adapter_product_select_textview);
			holder.radioGroup = (FlowRadioGroup) view
					.findViewById(R.id.pop_adapter_product_select_Radiogroup);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.name.setText(productAttrGroups.get(position).getGroupName());
		holder.radioGroup.removeAllViews();
		for (int i = 0; i < productAttrGroups.get(position).getAttrs().size(); i++) {
			RadioButton tempButton = (RadioButton) LayoutInflater.from(context)
					.inflate(R.layout.adapter_pop_selectattr_radiobuttom, null);
			tempButton.setText(productAttrGroups.get(position).getAttrs()
					.get(i).getAttrName());
			if (productAttrGroups.get(position).getAttrs().get(i).isEnable()) {
				tempButton.setEnabled(true);
			} else {
				tempButton.setEnabled(false);
			}
			if (productAttrGroups.get(position).getAttrs().get(i).isCheck()) {
				tempButton.setChecked(true);
			} else {
				tempButton.setChecked(false);
				tempButton
				.setOnClickListener(new OnClickListenerImpl(position));
			}
			tempButton.setTag(i);
			if (holder.radioGroup.getChildCount() < productAttrGroups
					.get(position).getAttrs().size()) {
				holder.radioGroup.addView(tempButton);
			}
		}
		return view;
	}

	static private class ViewHolder {
		protected TextView name;
		protected FlowRadioGroup radioGroup;
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnClickListenerImpl implements OnClickListener {
		int position;

		public OnClickListenerImpl(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			RadioButton button = (RadioButton) v;
			productAttrGroups.get(position).setAttrsChildCheck(
					(Integer) button.getTag());
			Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putSerializable(SELECTEDATTR, productAttrGroups);
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
	}

}
