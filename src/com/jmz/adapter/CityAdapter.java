package com.jmz.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jmz.bean.AllCity;
import com.jmz.bean.City;

public class CityAdapter extends BaseAdapter {
	private Context context;
	private AllCity allCity;
	private List<City> cityList;
	private City city;
	private String cityName;

	public CityAdapter(Context context, AllCity allCity,String cityName) {
		this.context = context;
		this.allCity = allCity;
		this.cityName = cityName;
		cityList = allCity.getCityList();
	}

	@Override
	public int getCount() {
		return cityList.size();
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
		TextView textView;
		city = allCity.getCityList().get(position);
		if (view == null) {
			textView = new TextView(context);
			textView.setLayoutParams(new ListView.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		} else {
			textView = (TextView) view;
		}
		textView.setText(cityList.get(position).getCityName());
		textView.setTextSize(17);
		textView.setPadding(8, 8, 8, 8);
		return textView;
	}

}
