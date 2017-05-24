package com.jmz.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.jmz.CarActivity;
import com.jmz.ParentActivity;
import com.jmz.R;
import com.jmz.bean.CarShop;
import com.jmz.uitl.Logger;

/**
 * 购物车适配器
 * 
 * @author Administrator
 * 
 */
public class CarShopAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<CarShop> carShops;
//	private CarProductAdapter carProductAdapter;
	private ParentActivity activity;
	private ViewHolder holder;
	private Handler handler;

	public CarShopAdapter(Context context, ArrayList<CarShop> carShops,
			Handler handler) {
		this.context = context;
		this.carShops = carShops;
		this.handler = handler;
		activity = (ParentActivity) context;
	}

	@Override
	public int getCount() {
		return carShops.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	public void setCarShops(ArrayList<CarShop> carShops) {
		this.carShops = carShops;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.adapter_car_shop_listadapter, null);
			holder.shopCheckBox = (CheckBox) view
					.findViewById(R.id.adapter_car_shop_checkbox);
			holder.shopTitle = (TextView) view
					.findViewById(R.id.adapter_car_shop_title);
			holder.productList = (ListView) view
					.findViewById(R.id.adapter_car_shop_list);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.shopTitle.setText(carShops.get(position).getShopName());
		holder.shopCheckBox.setChecked(carShops.get(position).isCheck());
		holder.shopCheckBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Message msg = new Message();
				if(carShops.get(position).isCheck()){
					carShops.get(position).setCheck(false);
					msg.obj = false;
				}else{
					carShops.get(position).setCheck(true);
					msg.obj = true;
				}
				msg.what = CarActivity.TAG_CARSHOP_SELECT;
				msg.arg1 = position;
				handler.sendMessage(msg);
			}
		});
		
		holder.carProductAdapter = new CarProductAdapter(context, carShops.get(
				position).getProductList(), handler, position);
		holder.productList.setAdapter(holder.carProductAdapter);
		return view;
	}

	static class ViewHolder {
		protected CarProductAdapter carProductAdapter;
		protected TextView shopTitle, shopNumber, shopMoney;
		protected CheckBox shopCheckBox;
		protected ListView productList;
	}

}
