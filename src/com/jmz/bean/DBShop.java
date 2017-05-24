package com.jmz.bean;

import java.util.ArrayList;

public class DBShop extends ParentBean {
	private String shopID;
	private int ShopProductsNumber;
	private float shopAllMoney;
	private String postscript ="";
	private ArrayList<DBOrder> dborders;
	
	public DBShop(String shopID) {
		this.shopID = shopID;
		dborders = new ArrayList<DBOrder>();
	}

	public String getShopID() {
		return shopID;
	}

	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

	public int getShopProductsNumber() {
		return ShopProductsNumber;
	}

	public void setShopProductsNumber(int shopProductsNumber) {
		ShopProductsNumber = shopProductsNumber;
	}

	public float getShopAllMoney() {
		return shopAllMoney;
	}

	public void setShopAllMoney(float shopAllMoney) {
		this.shopAllMoney = shopAllMoney;
	}

	public ArrayList<DBOrder> getDborders() {
		return dborders;
	}

	public void setDborders(ArrayList<DBOrder> dborders) {
		this.dborders = dborders;
	}

	@Override
	public String toString() {
		return "DBShop [shopID=" + shopID + ", ShopProductsNumber="
				+ ShopProductsNumber + ", shopAllMoney=" + shopAllMoney
				+ ", postscript=" + postscript + ", dborders=" + dborders + "]";
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	
	
}
