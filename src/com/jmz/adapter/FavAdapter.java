package com.jmz.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmz.MyApplication;
import com.jmz.ParentActivity;
import com.jmz.R;
import com.jmz.bean.FavBean;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 我的订单列表适配器
 * 
 * @author Administrator
 * 
 */
public class FavAdapter extends BaseAdapter {
	private List<FavBean> favList;
	private Context context;
	private ViewHolder holder;
	private FavBean favBean;
	private ParentActivity activity;
	private Handler handler;
	public boolean isClear; 
	

	public FavAdapter(Context context, List<FavBean> favList,Handler handler) {
		this.context = context;
		this.favList = favList;
		this.handler = handler;
		activity = (ParentActivity) context;
	}

	@Override
	public int getCount() {
		return favList.size();
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
		
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.adapter_fav_center_list, null);
			holder.title = (TextView) view
					.findViewById(R.id.fav_listadapter_name);
			holder.oldPrice = (TextView) view
					.findViewById(R.id.fav_listadapter_orderprice);
			holder.price = (TextView) view
					.findViewById(R.id.fav_listadapter_price);
			holder.faved = (TextView) view
					.findViewById(R.id.fav_listadapter_faved);
			holder.unfav = (TextView) view
					.findViewById(R.id.fav_listadapter_unfav);
			holder.productImg = (ImageView) view
					.findViewById(R.id.fav_listadapter_img);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		setDataToView(position, holder);
		return view;
	}
	
	/**
	 * 设置数据
	 * @param position
	 * @param holder
	 */
	private void setDataToView(final int position,final ViewHolder holder){
		favBean = favList.get(position);
		holder.title.setText(favBean.getProductTitle());
		String oldPrice ="全国统一零售价:"+favBean.getMarketPrice();
		SpannableString oldPriceSpan = new SpannableString(oldPrice);
		oldPriceSpan.setSpan(new ForegroundColorSpan(context.getResources()
				.getColor(R.color.black)), 4, oldPrice.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		oldPriceSpan.setSpan(new StrikethroughSpan(), 8, oldPrice.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		oldPriceSpan.setSpan(new StyleSpan(Typeface.BOLD), 4, 7,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.oldPrice.setText(oldPriceSpan);
		
		String priceStr = "抵扣金券现价:"+context.getString(R.string.yuanicon)
				+ favBean.getShopPrice();
		SpannableString priceSpan = new SpannableString(priceStr);
		priceSpan.setSpan(new ForegroundColorSpan(context.getResources()
				.getColor(R.color.black)), 4, 6,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		priceSpan.setSpan(new ForegroundColorSpan(context.getResources()
				.getColor(R.color.orange)), 7, priceStr.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		priceSpan.setSpan(new RelativeSizeSpan(1.3f), 7, priceStr.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		priceSpan.setSpan(new StyleSpan(Typeface.BOLD), 4, 6,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.price.setText(priceSpan);
		
		holder.faved.setText("已收藏:"+favBean.getFavProductTotal());
		
		LayoutParams ImgParams = holder.productImg.getLayoutParams();
		ImgParams.width = activity.screenWidth * 4 / 10;
		ImgParams.height = activity.screenWidth * 4 / 10;
		holder.productImg.setLayoutParams(ImgParams);
		String url = Config.JMZG
				+ favBean.getImageUrl().replace(".jpg", "_1.jpg");
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
		holder.unfav.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				handler.sendEmptyMessage(position);
			}
		});
		
	}
	/**
	 * 销毁bitmap
	 */
	public void destroy() {
		if (holder.productImg != null) {
			holder.productImg.setImageBitmap(null);
		}
		System.gc();
	}

	protected class ViewHolder {
		protected TextView title, oldPrice, price, faved, unfav;
		protected ImageView productImg;
	}
}
