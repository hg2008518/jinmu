package com.jmz.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ListView;

/***
 * @author Administrator
 *
 */
public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
		setCacheColorHint(Color.TRANSPARENT);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCacheColorHint(Color.TRANSPARENT);
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}