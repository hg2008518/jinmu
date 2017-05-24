package com.jmz.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.jmz.MyApplication;
import com.jmz.ParentActivity;
import com.jmz.PostOutActivity;
import com.jmz.ProductActivity;
import com.jmz.ProductClassActivity;
import com.jmz.SearchActivity;
import com.jmz.WebViewAcitivity;
import com.jmz.bean.ActBean;
import com.jmz.bean.Child;
import com.jmz.uitl.BitmapUtlie;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.Utile;
import com.jmz.view.JazzyViewPager;
import com.jmz.view.OutlineContainer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * viewpager适配器
 * 
 * @author Administrator
 * 
 */
public class ProductPagerAdapter extends PagerAdapter {

	private ImageView[] mImageViews;
	private JazzyViewPager mViewPager;
	private List<String> productImageUrls;
	private Context context;
	private ParentActivity activity;


	public ProductPagerAdapter(Context context, JazzyViewPager mViewPager,
			List<String> productImageUrls) {
		this.context = context;
		this.mViewPager = mViewPager;
		this.productImageUrls = productImageUrls;
		activity = (ParentActivity) context;
		init();
	}

	public void setActBeans(List<String> productImageUrls) {
		this.productImageUrls = productImageUrls;
		init();
		notifyDataSetChanged();
	}

	/**
	 * 初始化
	 */
	private void init() {
		mImageViews = null;
		mImageViews = new ImageView[productImageUrls.size()];
		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(context);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			mImageViews[i] = imageView;
		}
	}

	@Override
	public int getCount() {
		return mImageViews.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		if (view instanceof OutlineContainer) {
			return ((OutlineContainer) view).getChildAt(0) == obj;
		} else {
			return view == obj;
		}
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(mViewPager
				.findViewFromObject(position));
	}

	@Override
	public Object instantiateItem(View container, final int position) {
		ImageLoader.getInstance().displayImage(
				Config.JMZG + productImageUrls.get(position),
				mImageViews[position],
				MyApplication.getInstance().getOptions(),
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						mImageViews[position].setImageBitmap(BitmapUtlie
								.getNewImage(loadedImage, activity.screenWidth,
										activity.screenWidth ));
					}
				});
		((ViewPager) container).addView(mImageViews[position], 0);
		mViewPager.setObjectForPosition(mImageViews[position], position);
		return mImageViews[position];
	}
}
