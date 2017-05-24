package com.jmz.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseOpenHelper extends SQLiteOpenHelper {
	// 类没有实例化,是不能用作父类构造器的参数,必须声明为静态
	public static String dbname = "jmz";
	private static int version = 5;
	public static String tbname_user = "jmz_user";
	public static String tbname_order = "jmz_order";
	public static String tbname_id = "_id";
	public static String tbname_user_userId = "userid";
	public static String tbname_user_isLogin = "isLogin";
	public static String tbname_user_useMoney = "usemoney";
	public static String tbname_user_unUseMoney = "unusemoney";
	public static String tbname_user_totolMoney = "totolmoney";
	public static String tbname_user_userName = "username";
	public static String tbname_user_password = "password";
	public static String tbname_user_passwordstring = "passwordstring";
	public static String tbname_user_iscontract = "isContract";
	public static String tbname_user_contractDate = "contractDate";
	public static String tbname_user_isFirstEnterApp = "isfirstenterapp";
	public static String tbname_user_isRemeberPassWord = "isremeberpassword";
	public static String tbname_user_cityName = "cityname";
	public static String tbname_user_cityId = "cityid";
	public static String tbname_order_imageUrl = "imageurl";
	public static String tbname_order_productTitle = "producttitle";
	public static String tbname_order_productPrice = "pruductprice";
	public static String tbname_order_productNumber = "productnumber";
	public static String tbname_order_productId = "productid";
	public static String tbname_order_shopId = "shopid";
	public static String tbname_order_shopTitle = "shoptitle";
	public static String tbname_order_productTotal = "producttotal";
	public static String tbname_order_isSelect = "isselect";
	public static String tbname_order_productFare = "productfare";

	public DataBaseOpenHelper(Context context) {
		// 第一个参数是应用的上下文
		// 第二个参数是应用的数据库名字
		// 第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
		// 第四个参数是数据库版本，必须是大于0的int（即非负数）
		super(context, dbname, null, version);
	}

	public DataBaseOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + tbname_user + " ("
				+ tbname_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ tbname_user_userId + " VARCHAR(20) NOT NULL, "
				+ tbname_user_isLogin + " BOOLEAN NOT NULL, "
				+ tbname_user_isFirstEnterApp + " BOOLEAN NOT NULL, "
				+ tbname_user_isRemeberPassWord + " BOOLEAN NOT NULL, "
				+ tbname_user_useMoney + " FLOAT NOT NULL, "
				+ tbname_user_unUseMoney + " FLOAT NOT NULL, "
				+ tbname_user_totolMoney + " FLOAT NOT NULL, "
				+ tbname_user_userName + " VARCHAR(30) NOT NULL, "
				+ tbname_user_cityName + " VARCHAR(20) NOT NULL, "
				+ tbname_user_cityId + " VARCHAR(6) NOT NULL, "
				+ tbname_user_password + " VARCHAR(50) NOT NULL, "
				+ tbname_user_passwordstring + " VARCHAR(1000) NOT NULL," 
				+tbname_user_iscontract + " BOOLEAN NOT NULL," 
				+tbname_user_contractDate + " VARCHAR(100) NOT NULL);");

		db.execSQL("CREATE TABLE IF NOT EXISTS " + tbname_order + " ("
				+ tbname_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ tbname_order_imageUrl + " VARCHAR(300) NOT NULL, "
				+ tbname_order_productTitle + " VARCHAR(300) NOT NULL, "
				+ tbname_order_productId + " VARCHAR(20) NOT NULL, "
				+ tbname_order_shopId + " VARCHAR(20) NOT NULL, "
				+ tbname_order_shopTitle + " VARCHAR(300) NOT NULL, "
				+ tbname_order_productNumber + " INTEGER NOT NULL, "
				+ tbname_order_productPrice + " FLOAT NOT NULL, "
				+ tbname_order_productTotal + " FLOAT NOT NULL, "
				+ tbname_order_productFare + " FLOAT NOT NULL, "
				+ tbname_order_isSelect + " BOOLEAN NOT NULL);");
	}

	// onUpgrade()方法在数据库版本每次发生变化时都会把用户手机上的数据库表删除，然后再重新创建。
	// 一般在实际项目中是不能这样做的，正确的做法是在更新数据库表结构时，还要考虑用户存放于数据库中的数据不会丢失,从版本几更新到版本几。
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + tbname_user);
		db.execSQL("DROP TABLE IF EXISTS " + tbname_order);
		onCreate(db);
	}

}