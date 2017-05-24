package com.jmz.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jmz.R;
import com.jmz.uitl.Logger;

public class LoadingDialog extends Dialog {
	private Context context;
	public LoadingDialog(Context context) {
		super(context, R.style.loadingDialogStyle);
		this.context = context;
	}

	private LoadingDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
		ImageView img = (ImageView)layout.findViewById(R.id.loadpageimg);
		RotateAnimation anim = new RotateAnimation(0f, 359f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(1000);
		img.setAnimation(anim);
		setContentView(layout);
	}
}

