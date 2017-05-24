package com.jmz.uitl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.content.Context;
import android.os.Environment;

import com.jmz.zhifubao.Keys;
import com.jmz.zhifubao.Rsa;

/**
 * 操作类
 * 
 * @author Administrator
 * 
 */
public class Utile {
	/**
	 * 判断字符串对象是否相等
	 * 
	 * @param newStr
	 * @param oldStr
	 * @return
	 */
	public static boolean isEqual(String newStr, String oldStr) {
		if (newStr != null || oldStr != null) {
			return newStr == oldStr || newStr.equals(oldStr);
		}
		return false;
	}
	
	/**
	 * 判断是否有新版本更新
	 * 
	 * @param serviceVersion  服务器上面的版本号字符串
	 * @param currentAPPVer	 当前APP的版本号字符串
	 * @return 有新版本的时候返回true
	 */
	public static boolean isNewVersion(String serviceVersion, String currentAPPVer) {
		String serviceStr = serviceVersion.replace(".", "");
		int serviceInt = Integer.parseInt(serviceStr);
		
		String currentVerStr = currentAPPVer.replace(".", "");
		int currentInt = Integer.parseInt(currentVerStr);
		
		Logger.i("-->Utile", "serviceInt:" + serviceInt + " currentInt:" + currentInt);
		
		if (currentInt < serviceInt) 
			return true;
		else
			return false;
	}

	/**
	 * 判断字符串是否是价格字符串
	 * 
	 * @param money
	 * @return
	 */
	public static String isMoneyCH(float str) {
		DecimalFormat df = new DecimalFormat("0.00元");
		return df.format(str);
	}

	/**
	 * 判断字符串是否是价格字符串
	 * 
	 * @param money
	 * @return
	 */
	public static String isMoneyEN(float str) {
		DecimalFormat df = new DecimalFormat("¥0.00");
		return df.format(str);
	}

	/**
	 * 判断字符串是否是价格字符串
	 * 
	 * @param money
	 * @return
	 */
	public static String isMoneyEN(String str) {
		float a = Float.parseFloat(str);
		DecimalFormat df = new DecimalFormat("¥0.00");
		return df.format(a);
	}

	/**
	 * 读取信息
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getFromAssets(Context context, String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(context
					.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 得到ftp上的信息
	 * 
	 * @return
	 */
	public static String getTxtInfo(String url) {
		String str = null;
		StringBuffer buffer = new StringBuffer();
		/* 定义我们要访问的地址url */
		URL uri;
		try {
			uri = new URL(url);
			/* 打开这个url连接 */
			URLConnection ucon = uri.openConnection();
			/* 从上面的链接中取得InputStream */
			InputStream is = ucon.getInputStream();
			BufferedReader stream = new BufferedReader(
					new InputStreamReader(is));
			while ((str = stream.readLine()) != null) {
				buffer.append(str);
			}
			// Logger.e(buffer.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}

	/**
	 * 支付宝参数信息整合
	 */
	private static String getNewOrderInfo(String tradeNo, String subject,
			String body, String price) {
		StringBuilder sb = new StringBuilder();

		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(tradeNo);
		sb.append("\"&subject=\"");
		sb.append(subject);
		sb.append("\"&body=\"");
		sb.append(body);
		sb.append("\"&total_fee=\"");
		sb.append(price);
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder
				.encode("http://www.jmzgo.com/thirdparty/alipay/mobile_app/notify_url.aspx"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder
				.encode("http://www.jmzgo.com/order/m/alipay_return_url.aspx"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	@SuppressWarnings("deprecation")
	public static String info(String tradeNo, String subject, String body,
			String price) {
		return getNewOrderInfo(tradeNo, subject, body, price)
				+ "&sign=\""
				+ URLEncoder.encode(Rsa.sign(
						getNewOrderInfo(tradeNo, subject, body, price),
						Keys.PRIVATE)) + "\"&" + "sign_type=\"RSA\"";
	}
	
	

	// public static HashMap<String, String> getStateMap() {
	// HashMap<String, String> stateMap = new HashMap<String, String>();
	// stateMap.put(Config.WAITBUYERPAY, "等待付款");
	// stateMap.put(Config.WAITSELLERSENDGOODS, "等待发货");
	// stateMap.put(Config.WAITBUYERCONFIRMGOODS, "确认收货");
	// stateMap.put(Config.TRADEFINISHED, "交易完成");
	// stateMap.put(Config.TRADECLOSED, "交易关闭");
	// stateMap.put(Config.TRADECLOSEDBYSYSTEM, "交易关闭");
	// stateMap.put(Config.WAITSELLERAGREE, "等待卖家退款");
	// stateMap.put(Config.WAITBUYERRETURNGOODS, "等待买家退货");
	// stateMap.put(Config.REFUNDSUCCESS, "退款成功");
	// stateMap.put(Config.SELLERREFUSEBUYER, "卖家拒绝退款");
	// stateMap.put(Config.REFUNDCLOSED, "退款关闭");
	// stateMap.put(Config.WAITSELLERCONFIRMGOODS, "等待卖家收货");
	// return stateMap;
	// }
	
	public static void writeLog(String log, String filename) {
		if (isSdCardExist()) {
			try {
				File file = new File(Environment.getExternalStorageDirectory() ,
						filename);
				// 第二个参数意义是说是否以append方式添加内容
				BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
				bw.write(log);
				bw.flush();
				System.out.println("写入成功");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else
			Logger.i("-->Utile", "没有SD卡");
		
	}
	
	/**
	 * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
	 * 
	 * @return
	 */
	public static boolean isSdCardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 解密数据
	 * 
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static String decrypt(String message, String key) throws Exception {

		byte[] bytesrc = convertHexString(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte);
	}

	/**
	 * 加密数据
	 * 
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(String message, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		return cipher.doFinal(message.getBytes("UTF-8"));
	}

	/**
	 * 将数据解析，为下一步解密做准备
	 * 
	 * @param ss
	 * @return
	 */
	private static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
	}

	/**
	 * 将数据解析，为下一步加密做准备
	 * 
	 * @param b
	 * @return
	 */
	private static String toHexString(byte b[]) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}
		return hexString.toString();
	}

	/**
	 * 得到加密數據
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String jiami(String value) throws Exception {
		String jiami = URLEncoder.encode(value, "utf-8").toLowerCase();
		String a = toHexString(encrypt(jiami, Config.KEY)).toUpperCase();
		return a;
	}

	/**
	 * 得到解密數據
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String jiemi(String value) throws Exception {
		return URLDecoder.decode(decrypt(value, Config.KEY), "utf-8");
	}
	
	/**
	 * 比较两个数的大小
	 * @param x
	 * @param y
	 * @return
	 */
	public static int getMax(int x, int y) {
		int max;
		if (x > y) max = x;
		else max = y;
		return max;
	}

}
