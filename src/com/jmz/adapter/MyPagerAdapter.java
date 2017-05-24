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
public class MyPagerAdapter extends PagerAdapter {

	private ImageView[] mImageViews;
	private JazzyViewPager mViewPager;
	private List<ActBean> actBeans;
	private Context context;
	private ParentActivity activity;
	private Intent intent;

	public MyPagerAdapter(Context context, JazzyViewPager mViewPager,
			List<ActBean> actBeans) {
		this.context = context;
		this.mViewPager = mViewPager;
		this.actBeans = actBeans;
		activity = (ParentActivity) context;
		intent = new Intent();
		init();
	}

	public void setActBeans(List<ActBean> actBeans) {
		this.actBeans = actBeans;
		init();
		notifyDataSetChanged();
	}

	/**
	 * 初始化
	 */
	private void init() {
		mImageViews = null;
		mImageViews = new ImageView[actBeans.size()];
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
				Config.JMZG + actBeans.get(position).getActivityImgURL().replace(".", "_0."),
				mImageViews[position],
				MyApplication.getInstance().getOptions(),
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						mImageViews[position].setImageBitmap(BitmapUtlie
								.getNewImage(loadedImage, activity.screenWidth,
										activity.screenHeight * 1 / 4));
					}
				});

		((ViewPager) container).addView(mImageViews[position], 0);
		mViewPager.setObjectForPosition(mImageViews[position], position);

		mImageViews[position].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (actBeans.get(position) != null) {
					if (actBeans.get(position).getSubjID() != null
							&& actBeans.get(position).getPropertyTypeID() != null
							&& Integer.parseInt(actBeans.get(position)
									.getSubjID()) > 0
							&& Integer.parseInt(actBeans.get(position)
									.getPropertyTypeID()) > 0) {
						intent.setClass(context, ProductClassActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable(SearchActivity.CHILD, new Child(
								actBeans.get(position).getSubjID(), "",
								actBeans.get(position).getActivityName(),
								actBeans.get(position).getPropertyTypeID()));
						intent.putExtras(bundle);
						context.startActivity(intent);
					} else if (actBeans.get(position).getNodeID() != null
							&& actBeans.get(position).getPropertyTypeID() != null
							&& Integer.parseInt(actBeans.get(position)
									.getNodeID()) > 0
							&& Integer.parseInt(actBeans.get(position)
									.getPropertyTypeID()) > 0) {
						intent.setClass(context, ProductClassActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable(SearchActivity.CHILD, new Child(
								actBeans.get(position).getNodeID(), "",
								actBeans.get(position).getActivityName(),
								actBeans.get(position).getPropertyTypeID()));
						intent.putExtras(bundle);
						context.startActivity(intent);
					} else if (actBeans.get(position).getPropertyID() != null
							&& Integer.parseInt(actBeans.get(position)
									.getPropertyID()) > 0) {
						intent.setClass(context, ProductClassActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable(SearchActivity.CHILD, new Child(
								"", actBeans.get(position).getPropertyID(),
								actBeans.get(position).getActivityName(), "1"));
						intent.putExtras(bundle);
						context.startActivity(intent);
					} else if (actBeans.get(position).getProductID() != null
							&& Integer.parseInt(actBeans.get(position)
									.getProductID()) > 0) {
						intent.setClass(activity, ProductActivity.class);
						intent.putExtra(Config.TAG_PUODUCTID,
								actBeans.get(position).getProductID());
						activity.startActivity(intent);
					} else if (actBeans.get(position).getMobileURL() != null
							&& !Utile.isEqual(actBeans.get(position)
									.getMobileURL(), "-1")) {
						intent.setClass(activity, WebViewAcitivity.class);
						intent.putExtra(PostOutActivity.URLSTRING, actBeans
								.get(position).getMobileURL());
						activity.startActivity(intent);
					}
				}
			}
		});
		return mImageViews[position];
	}
}
