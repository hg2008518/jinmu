package com.jmz.adapter;

import java.math.BigDecimal;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
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
import com.jmz.bean.Product;
import com.jmz.uitl.Config;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 金券列表适配器
 * 
 * @author Administrator
 * 
 */
public class ProductCalssAdapter extends BaseAdapter {
	private static final String LOGTAG = "ProductCalssAdapter";
	private Context context;
	private List<Product> productList;
	private Product product;
	private ParentActivity activity;
	private int height;
	private int width;
	private ViewHolder holder;
	private boolean isRefresh = true;

	public ProductCalssAdapter(Context context, List<Product> productList) {
		this.context = context;
		this.productList = productList;
		activity = (ParentActivity) context;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return productList.size();
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
					R.layout.adapter_productclass, null);
			holder.productImg = (ImageView) view
					.findViewById(R.id.adapter_productclass_img);
			holder.productTitle = (TextView) view
					.findViewById(R.id.adapter_productclass_title);
			holder.tagCar = (TextView) view
					.findViewById(R.id.adapter_productclass_tag_car);
			holder.shareGetMoney = (TextView) view
					.findViewById(R.id.adapter_productclass_sharegetmoney);
			holder.buyGetMoney = (TextView) view
					.findViewById(R.id.adapter_productclass_buygetmoney);
			holder.oldPriceText = (TextView) view
					.findViewById(R.id.adapter_productclass_oldprice);
			holder.priceText = (TextView) view
					.findViewById(R.id.adapter_productclass_price);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		setDataToView(position, holder);
		return view;
	}

	protected void setDataToView(int position, final ViewHolder holder) {
		product = productList.get(position);
		height = activity.screenWidth/2-10;
		width = activity.screenWidth/2-10;
		String productUrl = Config.JMZG
				+ product.getImageUrl().replace(".", "_1.");

		// 设置商品主图
		LayoutParams params = holder.productImg.getLayoutParams();
		params.width = height;
		params.height = height;
		holder.productImg.setLayoutParams(params);
		if (!productUrl.equals(holder.productImg.getTag())) {
			ImageLoader.getInstance().displayImage(productUrl,
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

		// 设置商品属性
		holder.productTitle.setText( product.getProductTitle());
		
		if (new BigDecimal(product.getCommission()).doubleValue() <= 0) {
			holder.shareGetMoney.setTextColor(activity.getResources().getColor(android.R.color.transparent));
			holder.buyGetMoney.setTextColor(activity.getResources().getColor(android.R.color.transparent));
			holder.shareGetMoney.setBackgroundResource(activity.getResources().getColor(android.R.color.transparent));
			holder.buyGetMoney.setBackgroundResource(activity.getResources().getColor(android.R.color.transparent));
		} else {
			holder.shareGetMoney.setTextColor(activity.getResources().getColor(android.R.color.black));
			holder.buyGetMoney.setTextColor(activity.getResources().getColor(android.R.color.black));
			holder.shareGetMoney.setBackgroundResource(R.drawable.bg_share);
			holder.buyGetMoney.setBackgroundResource(R.drawable.bg_gou);
		}
		
//		LayoutParams shareParams = holder.shareGetMoney.getLayoutParams();
//		shareParams.width = height*2/3;
//		holder.shareGetMoney.setLayoutParams(shareParams);
//		LayoutParams buyGetParams = holder.buyGetMoney.getLayoutParams();
//		buyGetParams.width = height*2/3;
//		holder.buyGetMoney.setLayoutParams(buyGetParams);
		
		holder.shareGetMoney.setText("奖金\n¥" + product.getShareMoney());
		holder.buyGetMoney.setText("奖金\n¥" + product.getBuyMoney());
		
		LayoutParams shareParams = holder.shareGetMoney.getLayoutParams();
		shareParams.width = activity.screenWidth/5;
		
		LayoutParams buyParams = holder.buyGetMoney.getLayoutParams();
		buyParams.width = activity.screenWidth/5;
		
		if(new BigDecimal(product.getMarketPrice()).doubleValue()>0){
			holder.oldPriceText.setText(activity.getString(R.string.yuanicon)+product.getMarketPrice());
			holder.oldPriceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
			holder.tagCar.setVisibility(View.GONE);
			holder.priceText.setText(activity.getString(R.string.yuanicon)+product.getShopPrice());
		}else{
//			holder.tagCar.setVisibility(View.VISIBLE);
			holder.oldPriceText.setVisibility(View.GONE);
			holder.tagCar.setText(product.getCoupon().replace(".00", "")+ activity.getString(R.string.yuan));
			holder.priceText.setText("定金"+activity.getString(R.string.yuanicon)+product.getShopPrice());
		}
	}

	/**
	 * 销毁bitmap
	 */
	public void destroy() {
		if (holder != null && holder.productImg != null) {
			holder.productImg.setImageBitmap(null);
		}
		System.gc();
	}

	private static class ViewHolder {
		public TextView tagCar;
		protected ImageView productImg;
		protected TextView buyGetMoney, shareGetMoney, productTitle, priceText,
				oldPriceText;
	}
}
