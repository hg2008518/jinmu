package com.jmz.listener;

import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

/**
 * viewpager监听器
 * 
 * @author Administrator
 * 
 */
public class OnProductPageChangedListener implements OnPageChangeListener {
	private Context context;
	private TextView textLayout;
	private int size;
	public OnProductPageChangedListener(Context context,TextView textLayout,int size){
		this.context = context;
		this.textLayout = textLayout;
		this.size = size;
	}
	
	public void onPageSelected(int position) {
		setImageBackground(position);
	}

	public void onPageScrollStateChanged(int arg0) {

	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}
	
	/**
	 * 设置选中的显示
	 * 
	 * @param selectItemsIndex
	 */
	private void setImageBackground(int selectItemsIndex) {
//		for (int i = 0; i < mball.length; i++) {
//			if (i == selectItemsIndex) {
//				mball[i].setBackgroundResource(R.drawable.icon_redball);
//			} else {
//				mball[i].setBackgroundResource(R.drawable.umspay_loading_gray_bg);
//			}
//		}
		textLayout.setText((selectItemsIndex+1)+"/"+size);
	}

}
