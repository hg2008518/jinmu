package com.jmz.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.jmz.bean.OrderProduct;
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
public class MyOrderProductInfoAdapter extends BaseAdapter {
	private Context context;
	private List<OrderProduct> orderProducts;
	private ViewHolder holder;
	public ParentActivity activity;
	private HashMap<Integer, Bitmap> map ;

	public MyOrderProductInfoAdapter(Context context, List<OrderProduct> orderProducts) {
		this.context = context;
		this.orderProducts = orderProducts;
		activity = (ParentActivity) context;
		map = new HashMap<Integer, Bitmap>();
	}

	@Override
	public int getCount() {
		return orderProducts.size();
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
					R.layout.adapter_myorderproductinfo_product_listadapter, null);
			holder.productImg = (ImageView) view
					.findViewById(R.id.adapter_myorderinfo_product_img);
			holder.productTitle = (TextView) view
					.findViewById(R.id.adapter_myorderinfo_product_title);
			holder.productAttr = (TextView) view
					.findViewById(R.id.adapter_myorderinfo_product_attr);
			holder.productNumber = (TextView) view
					.findViewById(R.id.adapter_myorderinfo_product_number);
			holder.productPrice = (TextView) view
					.findViewById(R.id.adapter_myorderinfo_product_price);
			holder.productMoney = (TextView) view
					.findViewById(R.id.adapter_myorderinfo_product_money);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.productTitle.setText(orderProducts.get(position).getProductTitle());
		holder.productAttr.setText(orderProducts.get(position).getProductAttribute());
		holder.productNumber.setText("数量："
				+ orderProducts.get(position).getQuantity()+"件");
		holder.productPrice.setText("价格："
				+ context.getString(R.string.yuanicon)
				+ orderProducts.get(position).getShopPrice());
		holder.productMoney.setText("小计："
				+ orderProducts.get(position).getShopMoney());
		//设置图片
		LayoutParams ImgParams = holder.productImg.getLayoutParams();
		ImgParams.width = activity.screenWidth * 3 / 10;
		ImgParams.height = activity.screenWidth * 3 / 10;
		holder.productImg.setLayoutParams(ImgParams);
		ImageLoader.getInstance().displayImage(Config.JMZG
				+ orderProducts.get(position).getImageUrl()
						.replace(".jpg", "_1.jpg"), holder.productImg,
						MyApplication.getInstance().getOptions());
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
	private static class ViewHolder {
		protected TextView productAttr;
		protected TextView productTitle, productNumber, productPrice, productMoney;
		protected ImageView productImg;
	}

}
