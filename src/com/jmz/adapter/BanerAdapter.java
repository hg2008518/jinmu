package com.jmz.adapter;

import java.util.List;

import android.annotation.SuppressLint;
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
import com.jmz.bean.ActBean;
import com.jmz.uitl.BitmapUtlie;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 首页baner适配器
 * 
 * @author Administrator
 * 
 */
public class BanerAdapter extends BaseAdapter {

	private Context context;
	private ParentActivity activity;
	private ViewHolder holder;
	private List<ActBean> acBeans;
	private int type;

	public BanerAdapter(Context context, List<ActBean> acBeans, int type) {
		this.context = context;
		activity = (ParentActivity) context;
		this.acBeans = acBeans;
		this.type = type;
	}

	public void setAcBeans(List<ActBean> acBeans) {
		this.acBeans = acBeans;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return acBeans.size();
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
					R.layout.adapter_homepage_baner, null);
			holder.img = (ImageView) view.findViewById(R.id.home_baner_img);
			holder.title = (TextView) view.findViewById(R.id.home_baner_title);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		setaDataToView(position);
		return view;
	}

	/**
	 * 组件赋值
	 * 
	 * @param position
	 */
	@SuppressLint("ResourceAsColor")
	private void setaDataToView(int position) {
		String url = Config.JMZG + acBeans.get(position).getActivityImgURL();
		// .replace(".jpg", "_1.jpg");
		LayoutParams ImgParams = holder.img.getLayoutParams();
		ImgParams.width = activity.screenWidth /acBeans.size()-10;
		if (type == 1) {
			holder.title.setVisibility(View.VISIBLE);
		} else if (type == 2) {
			holder.title.setVisibility(View.GONE);
		} else if (type == 3) {
			holder.title.setVisibility(View.GONE);
			ImgParams.width = activity.screenWidth * 5/10;
			holder.img.setScaleType(ImageView.ScaleType.FIT_XY);
		}
		holder.img.setLayoutParams(ImgParams);
		ImageLoader.getInstance().displayImage(
				url,
				holder.img,
				MyApplication.getInstance().getOptions());
		
		// 设置商品主图
//		LayoutParams params = holder.img.getLayoutParams();
//		params.width = activity.screenWidth/2-10;
//		holder.img.setLayoutParams(params);
//		if (!url.equals(holder.img.getTag())) {
//			ImageLoader.getInstance().displayImage(url,
//					holder.img,
//					MyApplication.getInstance().getOptions(),
//					new SimpleImageLoadingListener() {
//						@Override
//						public void onLoadingComplete(String imageUrl,
//								View view, Bitmap loadedImage) {
//							super.onLoadingComplete(imageUrl, view, loadedImage);
//							holder.img.setTag(imageUrl);
//							if (type == 3){
//								holder.img.setImageBitmap(BitmapUtlie.getNewImage(loadedImage, activity.screenWidth/2-10, loadedImage.getHeight()));
//							}
//						}
//					});
//		}
		holder.title.setText(acBeans.get(position).getActivityName());
	}

	static class ViewHolder {
		protected ImageView img;
		protected TextView title;
	}

}
