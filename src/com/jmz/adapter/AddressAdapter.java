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
import com.jmz.bean.AddressBean;
import com.jmz.bean.AddressList;
import com.jmz.uitl.Utile;

/**
 * 地址列表适配器
 * 
 * @author Administrator
 * 
 */
public class AddressAdapter extends BaseAdapter {
	private Context context;
	private List<AddressBean> addressBeans;
	private boolean isPay;
	public String userAddressId;
	private ViewHolder holder;

	public AddressAdapter(Context context, AddressList addressList,
			boolean isPay, String userAddressId) {
		this.context = context;
		this.isPay = isPay;
		this.userAddressId = userAddressId;
		addressBeans = addressList.getUserAddressList();
	}

	@Override
	public int getCount() {
		return addressBeans.size();
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
					R.layout.adapter_adress_center_listad, null);
			holder.city = (TextView) view
					.findViewById(R.id.adress_listadapter_city);
			holder.city.setText(addressBeans.get(position).getRegion());
			holder.street = (TextView) view
					.findViewById(R.id.adress_listadapter_street);
			holder.street.setText(addressBeans.get(position).getStreet());
			holder.name = (TextView) view
					.findViewById(R.id.adress_listadapter_name);
			holder.name.setText(addressBeans.get(position).getConsignee());
			holder.phone = (TextView) view
					.findViewById(R.id.adress_listadapter_phone);
			holder.phone.setText(addressBeans.get(position).getTelephone());
			holder.auto = (TextView) view
					.findViewById(R.id.adress_listadapter_auto);
			holder.img = (ImageView) view
					.findViewById(R.id.adress_listadapter_select);
			if (Utile
					.isEqual(addressBeans.get(position).getIsDefault(), "True")) {
				holder.auto.setText(context.getString(R.string.address_auto));
			}
			if (isPay) {
				if (Utile.isEqual(
						addressBeans.get(position).getUserAddressID(),
						userAddressId)) {
					holder.img.setImageResource(R.drawable.icon_suss);
				} else {
					holder.img.setVisibility(View.GONE);
				}
			}
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		return view;
	}

	static class ViewHolder {
		protected TextView city, street, name, phone, auto;
		protected ImageView img;
	}

}
