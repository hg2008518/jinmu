package com.jmz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

/**
 * ��ʽ���ֵ�RadioGroup
 */
public class FlowRadioGroup extends RadioGroup {

	public FlowRadioGroup(Context context) {
		super(context);
	}

	public FlowRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
		int childCount = getChildCount();
		int x = 0;
		int y = 0;
		int row = 0;

		for (int index = 0; index < childCount; index++) {
			final View child = getChildAt(index);
			if (child.getVisibility() != View.GONE) {
				child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				int width = child.getMeasuredWidth();
				int height = child.getMeasuredHeight();
				x += width+30;
				y = row * height + height +20;
				if (x > maxWidth) {
					x = width +30;
					row++;
					y = row * height + height +20;
				}
			}
		}
		setMeasuredDimension(maxWidth, y);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int childCount = getChildCount();
		int maxWidth = r - l;
		int x = 0;
		int y = 0;
		int row = 0;
		for (int i = 0; i < childCount; i++) {
			final View child = this.getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				int width = child.getMeasuredWidth();
				int height = child.getMeasuredHeight();
				x += width + 30;
				y = row * height + height+20;
				if (x > maxWidth) {
					x = width + 30;
					row++;
					y = row * height + height+20;
				}
				child.layout(x - width, y - height, x, y);
			}
		}
	}
}