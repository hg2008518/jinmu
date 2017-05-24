package com.jmz.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmz.MyApplication;
import com.jmz.ParentActivity;
import com.jmz.ProductActivity;
import com.jmz.R;
import com.jmz.bean.ShareProduct;
import com.jmz.uitl.BitmapUtlie;
import com.jmz.uitl.Config;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 分享列表适配器
 * 
 * @author Administrator
 * 
 */
public class ShareRecordAdapter extends BaseAdapter {
	private Context context;
	private ShareProduct product;
	private HashMap<Integer, Bitmap> map = new HashMap<Integer, Bitmap>();
	private List<ShareProduct> shareProducts;
	private Handler handler;
	private ParentActivity activity;

	private ViewHolder holder;

	public ShareRecordAdapter(Context context, List<ShareProduct> shareTickets,
			Handler handler) {
		this.context = context;
		this.shareProducts = shareTickets;
		this.handler = handler;
		activity = (ParentActivity) context;

	}

	public void setShareProducts(List<ShareProduct> shareProducts) {
		this.shareProducts = shareProducts;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return shareProducts.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		product = shareProducts.get(position);
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.adapter_sharerecord_center_list, null);

			holder.timesText = (TextView) view
					.findViewById(R.id.sharerecord_listadater_times);
			holder.spreadTotal = (TextView) view.findViewById(R.id.sharerecord_listadater_spread_total);
			holder.operateText = (TextView) view
					.findViewById(R.id.sharerecord_listadater_operate);
			holder.productImg = (ImageView) view
					.findViewById(R.id.sharerecord_listadater_ticket);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.timesText.setText(product.getTotal());
		holder.spreadTotal.setText(product.getSpreadTotal());
		LayoutParams params = holder.productImg.getLayoutParams();
		params.width = activity.screenWidth * 3 / 10;
		params.height = activity.screenWidth * 3 / 10;
		holder.productImg.setLayoutParams(params);
		String url = Config.JMZG
				+ product.getImageUrl().replace(".jpg", "_1.jpg");
		if (!url.equals(holder.productImg.getTag())) {
			ImageLoader.getInstance().displayImage(url,
					holder.productImg,
					MyApplication.getInstance().getOptions(),
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUrl,
								View view, Bitmap loadedImage) {
							super.onLoadingComplete(imageUrl, view, loadedImage);
							holder.productImg.setTag(imageUrl);
						}
					});
		}
		holder.operateText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(position);
			}
		});

		return view;
	}

	/**
	 * 销毁bitmap
	 */
	public void destroy() {
		if(holder.productImg != null)
		holder.productImg.setImageBitmap(null);
		System.gc();
	}

	static class ViewHolder {
		protected TextView timesText, spreadTotal, operateText;
		protected ImageView productImg;
	}
}
