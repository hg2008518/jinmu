package com.jmz.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jmz.bean.DBOrder;

public class OrderService {

	private DataBaseOpenHelper dbOpenHelper;

	public OrderService(Context context) {
		dbOpenHelper = new DataBaseOpenHelper(context);
	}

	/**
	 * 插入一行数据
	 * 
	 * @param order
	 */
	public void insert(DBOrder order) {
		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
		database.beginTransaction();
		database.execSQL(
				"insert into " + DataBaseOpenHelper.tbname_order + "("
						+ DataBaseOpenHelper.tbname_order_imageUrl + ","
						+ DataBaseOpenHelper.tbname_order_productTitle + ","
						+ DataBaseOpenHelper.tbname_order_productId + ","
						+ DataBaseOpenHelper.tbname_order_shopId + ","
						+ DataBaseOpenHelper.tbname_order_shopTitle + ","
						+ DataBaseOpenHelper.tbname_order_productNumber + ","
						+ DataBaseOpenHelper.tbname_order_productPrice + ","
						+ DataBaseOpenHelper.tbname_order_productTotal + ","
						+ DataBaseOpenHelper.tbname_order_productFare + ","
						+ DataBaseOpenHelper.tbname_order_isSelect
						+ ")values(?,?,?,?,?,?,?,?,?,?)",
				new Object[] { order.getImageUrl(), order.getProductTitle(),
						order.getProductId(), order.getShopId(),
						order.getShopTitle(), order.getProductNumber(),
						order.getProductPrice(), order.getProductTotal(),
						order.getProductFare(), order.getIsSelect() });
		// database.close();可以不关闭数据库，他里面会缓存一个数据库对象，如果以后还要用就直接用这个缓存的数据库对象。但通过
		// context.openOrCreateDatabase(arg0, arg1, arg2)打开的数据库必须得关闭
		database.setTransactionSuccessful();
		database.endTransaction();

	}

	/**
	 * 更新某行数据
	 * 
	 * @param order
	 */
	public void update(DBOrder order) {
		SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
		database.execSQL(
				"update " + DataBaseOpenHelper.tbname_order + " set "
						+ DataBaseOpenHelper.tbname_order_imageUrl + "=?,"
						+ DataBaseOpenHelper.tbname_order_productTitle + "=?,"
						+ DataBaseOpenHelper.tbname_order_productId + "=?,"
						+ DataBaseOpenHelper.tbname_order_shopId + "=?,"
						+ DataBaseOpenHelper.tbname_order_shopTitle + "=?,"
						+ DataBaseOpenHelper.tbname_order_productNumber + "=?,"
						+ DataBaseOpenHelper.tbname_order_productPrice + "=?,"
						+ DataBaseOpenHelper.tbname_order_productTotal + "=?,"
						+ DataBaseOpenHelper.tbname_order_productFare + "=?,"
						+ DataBaseOpenHelper.tbname_order_isSelect + "=?"
						+ " where " + DataBaseOpenHelper.tbname_id + "=?",
				new Object[] { order.getImageUrl(), order.getProductTitle(),
						order.getProductId(), order.getShopId(),
						order.getShopTitle(), order.getProductNumber(),
						order.getProductPrice(), order.getProductTotal(),
						order.getProductFare(), order.getIsSelect(),
						order.get_id() });
		database.close();
	}

	/**
	 * 删除某行数据
	 * 
	 * @param ids
	 */
	public void delete(Integer... ids) {
		if (ids.length > 0) {
			StringBuffer sb = new StringBuffer();
			for (Integer id : ids) {
				sb.append('?').append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
			SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
			database.execSQL(
					"delete from " + DataBaseOpenHelper.tbname_order
							+ " where " + DataBaseOpenHelper.tbname_id + " in("
							+ sb.toString() + ")", ids);
			database.close();
		}
	}

	/**
	 * 查找某行数据
	 * 
	 * @param id
	 * @return
	 */
	public DBOrder find(int _id) {
		SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from "
				+ DataBaseOpenHelper.tbname_order + " where "
				+ DataBaseOpenHelper.tbname_id + "=?",
				new String[] { String.valueOf(_id) });
		if (cursor.moveToNext()) {
			return new DBOrder(cursor.getInt(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3),
					cursor.getString(4), cursor.getString(5), cursor.getInt(6),
					cursor.getFloat(7), cursor.getFloat(8),cursor.getFloat(9),cursor.getInt(10));
		}
		cursor.close();
		database.close();
		return null;
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public ArrayList<DBOrder> findAllData() {
		ArrayList<DBOrder> orders = new ArrayList<DBOrder>();
		SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from "
				+ DataBaseOpenHelper.tbname_order + " order by "
				+ DataBaseOpenHelper.tbname_order_shopId + " desc,"
				+ DataBaseOpenHelper.tbname_id + " desc", new String[] {});
		while (cursor.moveToNext()) {
			orders.add(new DBOrder(cursor.getInt(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3),
					cursor.getString(4), cursor.getString(5), cursor.getInt(6),
					cursor.getFloat(7), cursor.getFloat(8),cursor.getFloat(9),cursor.getInt(10)));
		}
		cursor.close();
		database.close();
		return orders;
	}

	/**
	 * 查询所有选中
	 * 
	 * @return
	 */
	public ArrayList<DBOrder> findAllSelectData() {
		ArrayList<DBOrder> orders = new ArrayList<DBOrder>();
		SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from "
				+ DataBaseOpenHelper.tbname_order + " where "
				+ DataBaseOpenHelper.tbname_order_isSelect + " = 1 order by "
				+ DataBaseOpenHelper.tbname_order_shopId + " desc,"
				+ DataBaseOpenHelper.tbname_id + " desc", new String[] {});
		while (cursor.moveToNext()) {
			orders.add(new DBOrder(cursor.getInt(0), cursor.getString(1),
					cursor.getString(2), cursor.getString(3),
					cursor.getString(4), cursor.getString(5), cursor.getInt(6),
					cursor.getFloat(7), cursor.getFloat(8),cursor.getFloat(9),cursor.getInt(10)));
		}
		cursor.close();
		database.close();
		return orders;
	}

	// /**
	// * 查询某个店铺下的所有 isPay 是否是支付 whichOrderBy false（desc降序）true (asc升序)
	// *
	// * @return
	// */
	// public List<DBOrder> findShopAllData(String shopId, boolean isPay,
	// boolean whichOrderBy) {
	// List<DBOrder> orders = new ArrayList<DBOrder>();
	// SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
	// String sql = null;
	// String[] strs = null;
	// String end = "desc";
	// if (!whichOrderBy) {
	// end = " desc";
	// } else {
	// end = " asc";
	// }
	// if (isPay) {
	// sql = "select * from " + DataBaseOpenHelper.tbname_order
	// + " where " + DataBaseOpenHelper.tbname_order_shopId
	// + " = ? and " + DataBaseOpenHelper.tbname_order_isSelect
	// + " = 1 order by " + DataBaseOpenHelper.tbname_id + end;
	// } else {
	// sql = "select * from " + DataBaseOpenHelper.tbname_order
	// + " where " + DataBaseOpenHelper.tbname_order_shopId
	// + " = ? order by " + DataBaseOpenHelper.tbname_id + end;
	// strs = new String[] { shopId };
	//
	// }
	// Cursor cursor = database.rawQuery(sql, strs);
	// while (cursor.moveToNext()) {
	// orders.add(new DBOrder(cursor.getInt(0), cursor.getString(1),
	// cursor.getString(2), cursor.getString(3), cursor
	// .getString(4), cursor.getString(5), cursor
	// .getInt(6), cursor.getFloat(7), cursor.getFloat(8),
	// cursor.getInt(9)));
	// }
	// cursor.close();
	// database.close();
	// return orders;
	// }
	//
	// // /**
	// // * 查询某个店铺下的所有 升序
	// // *
	// // * @return
	// // */
	// // public List<DBOrder> findShopAllDataAsc(String shopId,int isSelect) {
	// // List<DBOrder> orders = new ArrayList<DBOrder>();
	// // SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
	// // Cursor cursor = database.rawQuery("select * from "
	// // + DataBaseOpenHelper.tbname_order + " where "
	// // + DataBaseOpenHelper.tbname_order_shopId + " = ? order by "
	// // + DataBaseOpenHelper.tbname_id + " asc",
	// // new String[] { shopId });
	// // while (cursor.moveToNext()) {
	// // orders.add(new DBOrder(cursor.getInt(0), cursor.getString(1),
	// // cursor.getString(2), cursor.getString(3), cursor
	// // .getString(4), cursor.getString(5), cursor
	// // .getInt(6), cursor.getFloat(7), cursor.getFloat(8),
	// cursor.getInt(9)));
	// // }
	// // cursor.close();
	// // database.close();
	// // return orders;
	// // }
	//
	// /**
	// * 取得购物车或者某个店铺的某个字段的和 valus = 6(总数) or = 7(总金额)
	// * shopIdOrCar 只取店铺还是取全部 null(全部)否则传店铺的id
	// *
	// * @return
	// */
	// public String getSum(int valus, String shopIdOrCar,boolean isSelect) {
	// SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
	// String sql = null;
	// String sqlValus = null;
	// String end = "";
	// String[] strs = null;
	// int intSum = 0;
	// float floatSum = 0.0f;
	// if (valus == 6) {
	// sqlValus = DataBaseOpenHelper.tbname_order_productNumber;
	// } else if (valus == 7) {
	// sqlValus = DataBaseOpenHelper.tbname_order_producttotal;
	// }
	// if(isSelect){
	// end = " and "+DataBaseOpenHelper.tbname_order_isSelect +"= 1";
	// }else{
	// end = "";
	// }
	// if (shopIdOrCar == null) {
	// if(isSelect){
	// end = " where "+DataBaseOpenHelper.tbname_order_isSelect +"= 1";
	// }
	// sql = "select " + sqlValus + " from "
	// + DataBaseOpenHelper.tbname_order + end;
	// } else {
	// sql = "select " + sqlValus + " from "
	// + DataBaseOpenHelper.tbname_order + " where "
	// + DataBaseOpenHelper.tbname_order_shopId + " = ?" + end;
	// strs = new String[] { shopIdOrCar };
	// }
	// Cursor cursor = database.rawQuery(sql, strs);
	// while (cursor.moveToNext()) {
	// if (valus == 6) {
	// intSum += cursor.getInt(0);
	// } else if (valus == 7) {
	// floatSum += cursor.getFloat(0);dw
	// }
	// }
	// if (valus == 6) {
	// return intSum + "";
	// } else if (valus == 7) {
	// return String.format("%.2f", floatSum);
	// }
	// cursor.close();
	// database.close();
	// return "1000.00";
	// }
	//
	// /**
	// * 取得选中商品总金额
	// *
	// * @return
	// */
	// public float getSelectProductMoney(Integer... ids) {
	// if (ids.length > 0) {
	// StringBuffer sb = new StringBuffer();
	// String[] idsStr = new String[ids.length];
	// for (int i = 0; i < ids.length; i++) {
	// sb.append('?').append(',');
	// idsStr[i] = ids[i] + "";
	// }
	// sb.deleteCharAt(sb.length() - 1);
	// SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
	// Cursor cursor = database.rawQuery("select sun("
	// + DataBaseOpenHelper.tbname_order_producttotal + ") from "
	// + DataBaseOpenHelper.tbname_order + " where "
	// + DataBaseOpenHelper.tbname_id + " in(" + sb.toString()
	// + ")", idsStr);
	// if (cursor.moveToNext()) {
	// return cursor.getFloat(8);
	// }
	// cursor.close();
	// database.close();
	// }
	// return 0.0f;
	// }

}