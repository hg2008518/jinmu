package com.jmz.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class MyGridView extends GridView {

	public MyGridView(Context context) {
		super(context);
		setCacheColorHint(Color.TRANSPARENT);
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCacheColorHint(Color.TRANSPARENT);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true; // 禁止GridView滑动
		}
		return super.dispatchTouchEvent(ev);

	}

}
