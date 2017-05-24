package com.jmz.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.jmz.bean.MyOrderProduct;
import com.jmz.uitl.Config;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 我的订单商品列表适配器
 * 
 * @author Administrator
 * 
 */
public class MyOrderProductAdapter extends BaseAdapter {

	private Context context;
	private List<MyOrderProduct> myOrderProducts;
	private MyOrderProduct myOrderProduct;
	private ParentActivity activity;
	private ViewHolder holder;

	public MyOrderProductAdapter(Context context,
			List<MyOrderProduct> myOrderProducts) {
		this.context = context;
		this.myOrderProducts = myOrderProducts;
		activity = (ParentActivity) context;
	}

	@Override
	public int getCount() {
		return myOrderProducts.size();
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
		myOrderProduct = myOrderProducts.get(position);
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.adapter_myorder_center_product, null);
			holder.title = (TextView) view
					.findViewById(R.id.adapter_myorder_product_title);
			holder.productAttr = (TextView) view
					.findViewById(R.id.adapter_myorder_product_productattr);
			holder.priceAndNumber = (TextView) view
					.findViewById(R.id.adapter_myorder_product_priceandnumber);
			holder.productImg = (ImageView) view
					.findViewById(R.id.adapter_myorder_product_img);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		LayoutParams ImgParams = holder.productImg.getLayoutParams();
		ImgParams.width = activity.screenWidth * 3 / 10;
		ImgParams.height = activity.screenWidth * 3 / 10;
		holder.productImg.setLayoutParams(ImgParams);
		holder.title.setText(myOrderProduct.getProductTitle());
		holder.productAttr.setText(myOrderProduct.getProductAttribute());
		holder.priceAndNumber.setText("单价:"
				+ context.getString(R.string.yuanicon)
				+ myOrderProduct.getShopPrice() + "  共"
				+ myOrderProduct.getQuantity() + "件" + "\r\n小计："
				+ myOrderProduct.getActualPrice());
		String url = Config.JMZG
				+ myOrderProduct.getImageUrl().replace(".jpg", "_1.jpg");
		if (!url.equals(holder.productImg.getTag())) {
			ImageLoader.getInstance().displayImage(url, holder.productImg,
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
		return view;
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

	static class ViewHolder {
		public TextView productAttr;
		protected TextView title, priceAndNumber, time;
		protected ImageView productImg;
	}
}
