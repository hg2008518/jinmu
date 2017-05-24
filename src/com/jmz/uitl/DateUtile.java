package com.jmz.uitl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间操作类
 * 
 * @author Administrator
 * 
 */
public class DateUtile {
	public final static int NONE = 0; 
	public final static int DAY = 1; 
	public final static int HOUR = 2; 
	public final static int MINUTE = 3; 
	public final static int SECOND = 4; 
	
	/**
	 * 获取时间差
	 * @param newTime
	 *            新时间
	 * @param oldTime
	 *            旧时间
	 * @param which
	 *            0（无）1（天）2（时）3（分）4（秒）
	 * @return
	 */
	public static long betweenTime(String newTime, String oldTime, int which) {
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long result = 0;
		int select = 1;
		// 当前时间减去测试时间
		// 这个的除以1000得到秒，相应的60000得到分，3600000得到小时
		if (which == NONE) {
			select = 1;
		} else if (which == DAY) {
			select = 24 * 3600000;
		} else if (which == HOUR) {
			select = 3600000;
		} else if (which == MINUTE) {
			select = 60000;
		} else if (which == SECOND) {
			select = 1000;
		}
		try {
			result = (d.parse(chaneTime(newTime)).getTime() - d.parse(
					chaneTime(oldTime)).getTime())
					/ select;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 转换
	 * 
	 * @param str
	 * @return
	 */
	private static String chaneTime(String str) {
		return str.replace("/", "-");
	}
}
