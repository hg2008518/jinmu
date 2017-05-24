package com.jmz.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmz.MyApplication;
import com.jmz.ParentActivity;
import com.jmz.R;
import com.jmz.bean.CarProduct;
import com.jmz.bean.DBOrder;
import com.jmz.uitl.BitmapUtlie;
import com.jmz.uitl.Config;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 购物车订单预览商品适配器
 * 
 * @author Administrator
 * 
 */
public class CarOrderProductAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<CarProduct> carProducts;
	private ViewHolder holder;
	public ParentActivity activity;

	public CarOrderProductAdapter(Context context,
			ArrayList<CarProduct> carProducts) {
		this.context = context;
		this.carProducts = carProducts;
		activity = (ParentActivity) context;
	}

	@Override
	public int getCount() {
		return carProducts.size();
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
	public View getView(final int position, View view, ViewGroup parent) {
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.adapter_car_order_product_listadapter, null);
			holder.productImg = (ImageView) view
					.findViewById(R.id.adapter_car_order_product_img);
			holder.productTitle = (TextView) view
					.findViewById(R.id.adapter_car_order_product_title);
			holder.productNumber = (TextView) view
					.findViewById(R.id.adapter_car_order_product_number);
			holder.productPrice = (TextView) view
					.findViewById(R.id.adapter_car_order_product_price);
			holder.productOldPrice = (TextView) view
					.findViewById(R.id.adapter_car_trade_product_oldprice);
			holder.productAttribute = (TextView) view
					.findViewById(R.id.adapter_car_order_product_attr);
			holder.productSpread = (TextView) view
					.findViewById(R.id.adapter_car_order_product_spread);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.productTitle.setText(carProducts.get(position).getProductName());
		holder.productNumber.setText(context.getString(R.string.trade_number)
				+ carProducts.get(position).getQuantity());
		holder.productPrice.setText(context.getString(R.string.yuanicon)
				+ carProducts.get(position).getPrice());
		holder.productAttribute
				.setText(carProducts.get(position).getAttrName());
		holder.productOldPrice.setText(context.getString(R.string.yuanicon)
				+ carProducts.get(position).getMarketPrice());
		holder.productOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		
		BigDecimal discount = new BigDecimal(carProducts.get(position).getDiscount());
		if(discount.doubleValue() < 1) {
			holder.productSpread.setText((discount.floatValue()*10)+"折");
			holder.productSpread.setVisibility(View.VISIBLE);
		}else 
			holder.productSpread.setVisibility(View.GONE);
		
		LayoutParams ImgParams = holder.productImg.getLayoutParams();
		ImgParams.width = activity.screenWidth * 3 / 10;
		ImgParams.height = activity.screenWidth * 3 / 10;
		holder.productImg.setLayoutParams(ImgParams);
		String url = Config.JMZG
				+ carProducts.get(position).getProductImage()
						.replace(".jpg", "_1.jpg");
		holder.productImg.setTag(url);
		ImageLoader.getInstance().loadImage(url,
				MyApplication.getInstance().getOptions(),
				new SimpleImageLoadingListener() {

					@Override
					public void onLoadingComplete(String imageUrl, View view,
							Bitmap loadedImage) {
						super.onLoadingComplete(imageUrl, view, loadedImage);
						if (imageUrl.equals(holder.productImg.getTag())) {
							holder.productImg.setImageBitmap(loadedImage);
						}
					}
				});
		return view;
	}

	private static class ViewHolder {
		protected TextView productTitle, productNumber, productPrice,
				productAttribute, productOldPrice, productSpread;
		protected ImageView productImg;
	}

}
