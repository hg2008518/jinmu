package com.jmz.adapter;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.jmz.R;
import com.jmz.StartActivity;
import com.jmz.view.JazzyViewPager;
import com.jmz.view.OutlineContainer;

/**
 * 引导viewpager适配器
 * 
 * @author Administrator
 * 
 */
public class StartPagerAdapter extends PagerAdapter {

	public List<View> mImageViews;
	private JazzyViewPager mViewPager;
	private Context context;
	private StartActivity activity;
	private int width;
	private int height;

	public StartPagerAdapter(Context context, JazzyViewPager mViewPager) {
		this.context = context;
		this.mViewPager = mViewPager;
		activity = (StartActivity) context;
		mImageViews = new ArrayList<View>();
		mImageViews.add(getImageView(R.drawable.start_viewpager_1));
		mImageViews.add(getImageView(R.drawable.start_viewpager_2));
		mImageViews.add(getImageView(R.drawable.start_viewpager_3));
		mImageViews.add(getGifImageView(R.drawable.start_viewpager_4));
		width = activity.getWindowManager().getDefaultDisplay().getWidth();
		height = activity.getWindowManager().getDefaultDisplay().getHeight();
	}

	/**
	 * 取得imageview
	 * 
	 * @param id
	 * @return
	 */
	private ImageView getImageView(Integer id) {
		ImageView imageView = new ImageView(context);
		LayoutParams params = new LayoutParams(width, height, 1);
		imageView.setLayoutParams(params);
		imageView.setBackgroundResource(id);
		return imageView;
	}
	
	
	/**
	 * 取得imageview
	 * 
	 * @param id
	 * @return
	 */
	private ImageView getGifImageView(Integer id) {
		GifImageView imageView = new GifImageView(context);
		LayoutParams params = new LayoutParams(width, height, 1);
		imageView.setLayoutParams(params);
		imageView.setBackgroundResource(id);
		return imageView;
	}
	

	@Override
	public int getCount() {
		return mImageViews.size();
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
		((ViewPager) container).addView(mImageViews.get(position), 0);
		mViewPager.setObjectForPosition(mImageViews.get(position), position);
		return mImageViews.get(position);
	}
}
