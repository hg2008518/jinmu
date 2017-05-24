package com.jmz.listener;

import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;

import com.jmz.R;

/**
 * viewpager监听器
 * 
 * @author Administrator
 * 
 */
public class MyOnPageChangedListener implements OnPageChangeListener {
	private Context context;
	private ImageView[] mball;
	public MyOnPageChangedListener(Context context,ImageView[] mball){
		this.context = context;
		this.mball = mball;
	}
	
	public void onPageSelected(int position) {
		setImageBackground(position);
	}

	public void onPageScrollStateChanged(int arg0) {

	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}
	
	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItemsIndex
	 */
	private void setImageBackground(int selectItemsIndex) {
		for (int i = 0; i < mball.length; i++) {
			if (i == selectItemsIndex) {
				mball[i].setBackgroundResource(R.drawable.android_activities_cur);
			} else {
				mball[i].setBackgroundResource(R.drawable.android_activities_bg);
			}
		}
	}

}
