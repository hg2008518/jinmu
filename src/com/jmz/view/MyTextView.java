package com.jmz.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MyTextView extends TextView implements OnClickListener{
	public MyTextView(Context context) {
		super(context);
		initMyView();
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initMyView();
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initMyView();
	}
	private void initMyView(){
		setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		if (getEllipsize() != null) {
			setEllipsize(null);
			setMaxLines(100);
		} else {
			setEllipsize(TextUtils.TruncateAt.END);
			setMaxLines(1);
		}
	}
	
	
}
