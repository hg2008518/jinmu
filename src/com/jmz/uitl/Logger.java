package com.jmz.uitl;

import android.util.Log;

public class Logger {
	private static boolean showLog = true;

	public static void out(String LOGTAG,String msg) {
		if (showLog)
			Log.d(LOGTAG, msg);
	}

	public static void d(String LOGTAG,String msg) {
		if (showLog)
			Log.d(LOGTAG, msg);
	}

	public static void i(String LOGTAG,String msg) {
		if (showLog)
			Log.i(LOGTAG, msg);
	}

	public static void v(String LOGTAG,String msg) {
		if (showLog)
			Log.v(LOGTAG, msg);
	}

	public static void w(String LOGTAG,String msg) {
		if (showLog)
			Log.w(LOGTAG, msg);
	}

	public static void e(String LOGTAG,String msg) {
		if (showLog)
			Log.e(LOGTAG, msg);
	}
	public static void e(String LOGTAG,int msg) {
		if (showLog)
			Log.e(LOGTAG, msg+"");
	}

}