package com.jmz.bean;

import java.util.ArrayList;

public class ProductList extends ParentBean {
	private ArrayList<Product> ProductList;
	private ArrayList<PageData> Data;
	public ArrayList<Product> getProductList() {
		return ProductList;
	}
	public void setProductList(ArrayList<Product> productList) {
		ProductList = productList;
	}
	public ArrayList<PageData> getData() {
		return Data;
	}
	public void setData(ArrayList<PageData> data) {
		Data = data;
	}
	@Override
	public String toString() {
		return "TicketList [ProductList=" + ProductList + ", Data=" + Data
				+ "]";
	}
}
