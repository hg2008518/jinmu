package com.jmz.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jmz.APPVersionInfoActivity;
import com.jmz.AddressActivity;
import com.jmz.FindServiceActivity;
import com.jmz.MyFavActivity;
import com.jmz.MyOrderActivity;
import com.jmz.MyTicketActivity;
import com.jmz.PostOutActivity;
import com.jmz.R;
import com.jmz.ShareMoneyActivity;
import com.jmz.ShareRecordActivity;
import com.jmz.UserActivity;
import com.jmz.WebViewAcitivity;
import com.jmz.uitl.Config;
import com.jmz.uitl.RefreUtile;

/**
 * 地址列表适配器
 * 
 * @author Administrator
 * 
 */
public class UserAdapter extends BaseAdapter {
	private Context context;
	private ViewHolder holder;
	private ArrayList<HashMap<String, Integer>> list;
	private UserActivity activity;
	private HashMap<String, Integer> map;

	public UserAdapter(Context context) {
		this.context = context;
		initData();
		activity = (UserActivity) context;
	}

	private void initData() {
		list = new ArrayList<HashMap<String, Integer>>();
		map = new HashMap<String, Integer>();
		map.put("name", R.string.user_myorder);
		map.put("icon", R.drawable.icon_order);
		map.put("tag", 1);
		list.add(map);
		map = new HashMap<String, Integer>();
		map.put("name", R.string.user_myticket);
		map.put("icon", R.drawable.icon_ticket);
		map.put("tag", 2);
		list.add(map);
		map = new HashMap<String, Integer>();
		map.put("name", R.string.user_fav);
		map.put("icon", R.drawable.icon_fav);
		map.put("tag", 3);
		list.add(map);
		map = new HashMap<String, Integer>();
		map.put("name", R.string.user_adress);
		map.put("icon", R.drawable.icon_adress);
		map.put("tag", 4);
		list.add(map);

		map = new HashMap<String, Integer>();
		map.put("name", R.string.user_sharegetmoney);
		map.put("icon", R.drawable.icon_money);
		map.put("tag", 5);
		list.add(map);
		map = new HashMap<String, Integer>();
		map.put("name", R.string.user_sharerecord);
		map.put("icon", R.drawable.icon_record);
		map.put("tag", 6);
		list.add(map);

		map = new HashMap<String, Integer>();
		map.put("name", R.string.user_checkrefre);
		map.put("icon", R.drawable.icon_refre);
		map.put("tag", 7);
		list.add(map);
		map = new HashMap<String, Integer>();
		map.put("name", R.string.user_findservice);
		map.put("icon", R.drawable.icon_findservice);
		map.put("tag", 8);
		list.add(map);
		// map = new HashMap<String, Integer>();
		// map.put("name", R.string.user_aboutjmz);
		// map.put("icon", R.drawable.icon_about);
		// map.put("tag", 9);
		// list.add(map);

		// 这个没有的，只是为了增加宽度
		map = new HashMap<String, Integer>();
		map.put("name", R.string.user_aboutjmz);
		map.put("icon", R.drawable.icon_about);
		map.put("tag", 9);
		list.add(map);

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
			view = LayoutInflater.from(context).inflate(
					R.layout.adapter_user_gridview, null);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.title = (TextView) view.findViewById(R.id.user_adapter_title);
		holder.kongge = (View) view.findViewById(R.id.user_adapter_bottom);
		holder.title
				.setText(activity.getString(list.get(position).get("name")));
		Drawable drawableLeft = context.getResources().getDrawable(
				list.get(position).get("icon"));
		Drawable drawableRight = context.getResources().getDrawable(
				R.drawable.arrow_right);
		// / 这一步必须要做,否则不会显示.
		drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(),
				drawableLeft.getMinimumHeight());
		drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(),
				drawableRight.getMinimumHeight());
		holder.title.setCompoundDrawables(drawableLeft, null, drawableRight,
				null);
		view.setOnClickListener(new AdapterOclickImpl(list.get(position).get(
				"tag")));
		if (list.get(position).get("tag") == 4
				|| list.get(position).get("tag") == 6) {
			holder.kongge.setVisibility(View.VISIBLE);
			view.setVisibility(View.VISIBLE);
		} else if (list.get(position).get("tag") == 8) {
			holder.kongge.setVisibility(View.VISIBLE);
			view.setVisibility(View.VISIBLE);
			// holder.kongge.setPadding(0, 0, 0, 500);
		} else if (list.get(position).get("tag") == 9) {
			view.setVisibility(View.GONE);
		} else {
			holder.kongge.setVisibility(View.GONE);
		}
		return view;
	}

	private class ViewHolder {
		protected TextView title;
		protected View kongge;
	}

	/**
	 * 单击监听
	 * 
	 * @author Administratorb2
	 * 
	 */
	private class AdapterOclickImpl implements OnClickListener {
		Intent intent = new Intent();
		int position;

		public AdapterOclickImpl(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (position) {
			case 1:
				intent.setClass(activity, MyOrderActivity.class);
				activity.startActivity(intent);
				break;
			case 2:
				intent.setClass(activity, MyTicketActivity.class);
				activity.startActivity(intent);
				break;
			case 3:
				intent.setClass(activity, MyFavActivity.class);
				activity.startActivity(intent);
				break;
			case 4:
				intent.setClass(activity, AddressActivity.class);
				activity.startActivity(intent);
				break;
			case 5:
				intent.setClass(activity, ShareMoneyActivity.class);
				activity.startActivity(intent);
				break;
			case 6:
				intent.setClass(activity, ShareRecordActivity.class);
				activity.startActivity(intent);
				break;
			case 7:
//				new RefreUtile(context, true).refreSubmit();
				intent.setClass(activity, APPVersionInfoActivity.class);
				activity.startActivity(intent);
				break;
			case 8:
				intent.setClass(activity, FindServiceActivity.class);
				activity.startActivity(intent);
				break;
			case 9:
				intent.setClass(activity, WebViewAcitivity.class);
				intent.putExtra(PostOutActivity.URLSTRING, Config.JMZGO_URL);
				activity.startActivity(intent);
				break;
			default:
				break;
			}
		}
	}

}
