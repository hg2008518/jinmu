package com.jmz.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmz.CarActivity;
import com.jmz.MyApplication;
import com.jmz.ParentActivity;
import com.jmz.ProductActivity;
import com.jmz.R;
import com.jmz.bean.CarProduct;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.Utile;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 购物车适配器
 * 
 * @author Administrator
 * 
 */
public class CarProductAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<CarProduct> carProducts;
	private ViewHolder holder;
	private ParentActivity activity;
	private Handler handler;
	private int productNumber = 0;
	private int shopPosition;

	public CarProductAdapter(Context context,
			ArrayList<CarProduct> carProducts, Handler handler, int shopPosition) {
		this.context = context;
		this.carProducts = carProducts;
		this.shopPosition = shopPosition;
		this.handler = handler;
		activity = (ParentActivity) context;
	}

	public void setCarProducts(ArrayList<CarProduct> carProducts) {
		this.carProducts = carProducts;
		notifyDataSetChanged();
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
					R.layout.adapter_car_product_listadapter, null);
			holder.productCheckBox = (CheckBox) view
					.findViewById(R.id.car_productadapter_checkbox);
			holder.productLine = view
					.findViewById(R.id.car_productadapter_line);
			holder.productImg = (ImageView) view
					.findViewById(R.id.car_productadapter_img);
			holder.productTitle = (TextView) view
					.findViewById(R.id.car_productadapter_title);
			holder.productAttr = (TextView) view
					.findViewById(R.id.car_productadapter_attr);
			holder.productSub = (TextView) view
					.findViewById(R.id.car_productadapter_subbtn);
			holder.productEdit = (TextView) view
					.findViewById(R.id.car_productadapter_numberedit);
			holder.productAdd = (TextView) view
					.findViewById(R.id.car_productadapter_addbtn);
			holder.productPrice = (TextView) view
					.findViewById(R.id.car_productadapter_price);
			holder.productOldPrice = (TextView) view
					.findViewById(R.id.car_productadapter_oldprice);
			holder.productDelete = (TextView) view
					.findViewById(R.id.car_productadapter_delete);
			holder.productMesk = (TextView) view
					.findViewById(R.id.car_productadapter_mask);
			holder.productState = (TextView) view
					.findViewById(R.id.car_productadapter_state);
			holder.productSpread = (TextView) view
					.findViewById(R.id.car_productadapter_spread);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		setDataToview(position);
		return view;
	}

	/**
	 * 为组件赋值
	 */
	private void setDataToview(final int position) {
		if (position == 0) {
			holder.productLine.setVisibility(View.GONE);
		}
		holder.productCheckBox.setChecked(carProducts.get(position).isCheck());
		holder.productEdit.setText(carProducts.get(position).getQuantity());
		holder.productTitle.setText(carProducts.get(position).getProductName());
		holder.productAttr.setText(carProducts.get(position).getAttrName());
		holder.productPrice.setText(context.getString(R.string.yuanicon)
				+ carProducts.get(position).getPrice());
		holder.productOldPrice.setText(context.getString(R.string.yuanicon)
				+ carProducts.get(position).getMarketPrice());
		BigDecimal decimal =  new BigDecimal(carProducts.get(position).getFirstPrice()).subtract( new BigDecimal(carProducts.get(position)
		.getPrice()));
		if (decimal.doubleValue() > 0) {
			holder.productSpread.setText("下降"+decimal.toString()+"元");
			holder.productSpread.setVisibility(View.VISIBLE);
		} else if (decimal.doubleValue() < 0) {
			holder.productSpread.setText("上升"+decimal.negate().toString()+"元");
			holder.productSpread.setVisibility(View.VISIBLE);
		} else {
			holder.productSpread.setVisibility(View.GONE);
		}
		
		BigDecimal discount = new BigDecimal(carProducts.get(position).getDiscount());
		if(discount.doubleValue() < 1) {
			holder.productSpread.setText((discount.floatValue()*10)+"折");
			holder.productSpread.setVisibility(View.VISIBLE);
		}
		
		holder.productOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		if(Utile.isEqual("OnSale", carProducts.get(position).getProductStatus())){
			holder.productMesk.setVisibility(View.GONE);
			holder.productState.setVisibility(View.GONE);
		}else{
			holder.productMesk.setVisibility(View.VISIBLE);
			holder.productState.setVisibility(View.VISIBLE);
		}
		holder.productMesk.setOnClickListener(new CarProductImpl(position));
		LayoutParams ImgParams = holder.productImg.getLayoutParams();
		ImgParams.width = activity.screenWidth * 2 / 10;
		ImgParams.height = activity.screenWidth * 2 / 10;
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
		holder.productAdd.setOnClickListener(new CarProductImpl(position));
		holder.productImg.setOnClickListener(new CarProductImpl(position));
		holder.productTitle.setOnClickListener(new CarProductImpl(position));
		holder.productSub.setOnClickListener(new CarProductImpl(position));
		holder.productDelete.setOnClickListener(new CarProductImpl(position));
		holder.productCheckBox.setOnClickListener(new CarProductImpl(position));
	}

	/**
	 * 单击监听
	 * 
	 * @author Administrator
	 * 
	 */
	private class CarProductImpl implements OnClickListener {
		private int position;

		public CarProductImpl(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.car_productadapter_addbtn:
				productNumber = Integer.parseInt(carProducts.get(position)
						.getQuantity());
				productNumber++;
				carProducts.get(position).setQuantity(productNumber + "");
				Message addMsg = new Message();
				addMsg.obj = carProducts.get(position);
				addMsg.what = CarActivity.TAG_CARPRODUCT_UPDATE;
				addMsg.arg1 = productNumber;
				handler.sendMessage(addMsg);
				notifyDataSetChanged();
				break;
			case R.id.car_productadapter_subbtn:
				productNumber = Integer.parseInt(carProducts.get(position)
						.getQuantity());
				if (productNumber > 1) {
					productNumber--;
				}
				carProducts.get(position).setQuantity(productNumber + "");
				Message subMsg = new Message();
				subMsg.obj = carProducts.get(position);
				subMsg.what = CarActivity.TAG_CARPRODUCT_UPDATE;
				subMsg.arg1 = productNumber;
				handler.sendMessage(subMsg);
				notifyDataSetChanged();
				break;
			case R.id.car_productadapter_delete:
				Message msg = new Message();
				msg.obj = carProducts.get(position);
				msg.what = CarActivity.TAG_CARPRODUCT_DELETE;
				handler.sendMessage(msg);
				break;
			case R.id.car_productadapter_img:
			case R.id.car_productadapter_title:
				Intent intent = new Intent();
				intent.setClass(activity, ProductActivity.class);
				intent.putExtra(Config.TAG_PUODUCTID, carProducts.get(position)
						.getProductID());
				activity.startActivity(intent);
				break;
			case R.id.car_productadapter_checkbox:
				Message checkMsg = new Message();
				if (carProducts.get(position).isCheck()) {
					carProducts.get(position).setCheck(false);
					checkMsg.obj = false;
				} else {
					carProducts.get(position).setCheck(true);
					checkMsg.obj = true;
				}
				checkMsg.what = CarActivity.TAG_CARPRODUCT_SELECT;
				checkMsg.arg1 = shopPosition;
				checkMsg.arg2 = position;
				handler.sendMessage(checkMsg);
				break;
			case R.id.car_productadapter_mask:
				
				break;

			default:
				break;
			}

		}
	}

	static class ViewHolder {
		protected TextView productSpread;
		protected View productLine;
		protected TextView line;
		protected TextView productTitle;
		protected CheckBox productCheckBox;
		protected ImageView productImg;
		protected TextView productAttr;
		protected TextView productAdd;
		protected TextView productEdit;
		protected TextView productSub;
		protected TextView productPrice;
		protected TextView productOldPrice;
		protected TextView productDelete;
		protected TextView productMesk;
		protected TextView productState;
	}

}
