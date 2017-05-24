package com.jmz.uitl;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * 页面退出管理
 * 
 * @author Administrator
 * 
 */
public class BitmapUtlie {

	public BitmapUtlie() {
	}

	/**
	 * 定义图片相对显示器的宽高
	 * 
	 * @param oldBitmap
	 * @return
	 */
	public static Bitmap getNewImage(Bitmap oldBitmap, int w, int h) {
		int width = oldBitmap.getWidth();
		int height = oldBitmap.getHeight();
		float scaleWidth = (float) w / width;
		float scaleHeight = (float) h / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, width, height,
				matrix, true);
		return newBitmap;
	}
}
