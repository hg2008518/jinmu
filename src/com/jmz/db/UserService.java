package com.jmz.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmz.bean.DBUser;
import com.jmz.uitl.Logger;

public class UserService {

	private DataBaseOpenHelper dbOpenHelper;


	public UserService(Context context) {
		dbOpenHelper = new DataBaseOpenHelper(context);
	}

	/**
	 * 插入一行数据
	 * 
	 * @param user
	 */
	public void insert(DBUser user) {
		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
		database.beginTransaction();
		database.execSQL(
				"insert into " + DataBaseOpenHelper.tbname_user + "("
						+ DataBaseOpenHelper.tbname_user_userId + ","
						+ DataBaseOpenHelper.tbname_user_isLogin + ","
						+ DataBaseOpenHelper.tbname_user_isFirstEnterApp + ","
						+ DataBaseOpenHelper.tbname_user_isRemeberPassWord+ "," 
						+ DataBaseOpenHelper.tbname_user_useMoney + ","
						+ DataBaseOpenHelper.tbname_user_unUseMoney + ","
						+ DataBaseOpenHelper.tbname_user_totolMoney + ","
						+ DataBaseOpenHelper.tbname_user_userName + ","
						+ DataBaseOpenHelper.tbname_user_cityName + ","
						+ DataBaseOpenHelper.tbname_user_cityId + ","
						+ DataBaseOpenHelper.tbname_user_password + ","
						+ DataBaseOpenHelper.tbname_user_passwordstring  + ","
						+ DataBaseOpenHelper.tbname_user_iscontract + "," 
						+ DataBaseOpenHelper.tbname_user_contractDate +")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[] { user.getUserId(), user.getIsLogin(),
						user.getIsFirstEnterApp(), user.getIsRemeberPassword(),
						user.getUseMoney(), user.getUnUseMoney(),
						user.getTotolMoney(), user.getUserName(),user.getCityName(), user.getCityId(),
						user.getPassWord(), user.getPassWordString(), user.getIsContract(), user.getContractDate() });
		// database.close();可以不关闭数据库，他里面会缓存一个数据库对象，如果以后还要用就直接用这个缓存的数据库对象。但通过
		// context.openOrCreateDatabase(arg0, arg1, arg2)打开的数据库必须得关闭
		database.setTransactionSuccessful();
		database.endTransaction();

	}

	/**
	 * 更新某行数据
	 * 
	 * @param user
	 */
	public void update(DBUser user) {
		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
		database.execSQL(
				"update " + DataBaseOpenHelper.tbname_user + " set "
						+ DataBaseOpenHelper.tbname_user_userId + "=?,"
						+ DataBaseOpenHelper.tbname_user_isLogin + "=?,"
						+ DataBaseOpenHelper.tbname_user_isFirstEnterApp+"=?,"
						+ DataBaseOpenHelper.tbname_user_isRemeberPassWord+ "=?,"
						+ DataBaseOpenHelper.tbname_user_useMoney + "=?,"
						+ DataBaseOpenHelper.tbname_user_unUseMoney + "=?,"
//						+ DataBaseOpenHelper.tbname_user_totolMoney	+ "=?,"
						+ DataBaseOpenHelper.tbname_user_userName	+ "=?," 
						+ DataBaseOpenHelper.tbname_user_cityName	+ "=?," 
						+ DataBaseOpenHelper.tbname_user_cityId	+ "=?," 
						+ DataBaseOpenHelper.tbname_user_password	+ "=?,"
						+ DataBaseOpenHelper.tbname_user_passwordstring	+ "=?, "
						+ DataBaseOpenHelper.tbname_user_iscontract	+ "=?, "
						+ DataBaseOpenHelper.tbname_user_contractDate + "=? "
						+ "where " + DataBaseOpenHelper.tbname_id
						+ "=?",
				new Object[] { user.getUserId(), user.getIsLogin(),
						user.getIsFirstEnterApp(),user.getIsRemeberPassword(), user.getUseMoney(),
						user.getUnUseMoney(), 
						//user.getTotolMoney(),
						user.getUserName(),user.getCityName(),user.getCityId(), user.getPassWord(),
						user.getPassWordString(), user.getIsContract(), user.getContractDate(),user.get_id() });
		database.close();
	}

	/**
	 * 查找某行数据
	 * 
	 * @param id
	 * @return
	 */
	public DBUser find(int _id) {
		SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from "
				+ DataBaseOpenHelper.tbname_user + " where "
				+ DataBaseOpenHelper.tbname_id + "=?",
				new String[] { String.valueOf(_id) });
		if (cursor.moveToNext()) {
			return new DBUser(cursor.getInt(0),cursor.getString(1), cursor.getInt(2),
					cursor.getInt(3), cursor.getInt(4), cursor.getFloat(5),
					cursor.getFloat(6),cursor.getFloat(7), cursor.getString(8),
					cursor.getString(9), cursor.getString(10),cursor.getString(11), cursor.getString(12), cursor.getInt(13), cursor.getString(14));
		}
		database.close();
		return null;
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<DBUser> findAllData() {
		List<DBUser> Users = new ArrayList<DBUser>();
		SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from "
				+ DataBaseOpenHelper.tbname_user, new String[] {});
		while (cursor.moveToNext()) {
			Users.add(new DBUser(cursor.getInt(0),cursor.getString(1), cursor.getInt(2),
					cursor.getInt(3), cursor.getInt(4), cursor.getFloat(5),
					cursor.getFloat(6), cursor.getFloat(7),cursor.getString(8),
					cursor.getString(9), cursor.getString(10),cursor.getString(11), cursor.getString(12), cursor.getInt(13), cursor.getString(14)));
		}
		database.close();
		return Users;
	}
	// public void delete(Integer... ids) {
	// if (ids.length > 0) {
	// StringBuffer sb = new StringBuffer();
	// for (Integer id : ids) {
	// sb.append('?').append(',');
	// }
	// sb.deleteCharAt(sb.length() - 1);
	// SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
	// database.execSQL(
	// "delete from User where Userid in(" + sb.toString()
	// + ")", ids);
	// }
	// }

	// 获取分页数据，提供给SimpleCursorAdapter使用。
	// public Cursor getRawScrollData(int startResult, int maxResult) {
	// List<User> Users = new ArrayList<User>();
	// SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
	// return database.rawQuery(
	// "select Userid as _id ,name,age from User limit ?,?",
	// new String[] { String.valueOf(startResult),
	// String.valueOf(maxResult) });
	//
	// }

	// public long getCount() {
	// SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
	// Cursor cursor = database.rawQuery("select count(*) from User", null);
	// if (cursor.moveToNext()) {
	// return cursor.getLong(0);
	// }
	// return 0;
	// }

}