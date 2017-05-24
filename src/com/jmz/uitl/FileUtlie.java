package com.jmz.uitl;

import java.io.File;

import android.os.Environment;

import com.jmz.MyApplication;

/**
 * 使用余额额支付
 * 
 * @author Administrator
 * 
 */
public class FileUtlie {

	
	/**
	 * 取得文件路径
	 * @return
	 */
	public  static File getJmzFile() {
		String path ;
		if (Utile.isEqual(Environment.getExternalStorageState(),
				Environment.MEDIA_MOUNTED)) {
			path = Environment.getExternalStorageDirectory().toString()
					+ File.separator + "jmzgo" + File.separator + "tmp";
		} else {
			path = MyApplication.getInstance().getCacheDir().toString() + File.separator + "jmzgo"
					+ File.separator + "tmp";
		}
		return new File(path);
	}

}
