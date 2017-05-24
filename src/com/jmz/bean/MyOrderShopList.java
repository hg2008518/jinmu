package com.jmz.bean;

import java.util.List;

public class MyOrderShopList extends ParentBean {
	private List<MyOrderShop> OrderList;
	private List<PageData> Data;
	
	public List<PageData> getData() {
		return Data;
	}

	public void setData(List<PageData> data) {
		Data = data;
	}

	public List<MyOrderShop> getOrderList() {
		return OrderList;
	}

	public void setOrderList(List<MyOrderShop> orderList) {
		OrderList = orderList;
	}
	@Override
	public String toString() {
		return "OrderList [OrderList=" + OrderList + ", Data=" + Data + "]";
	}
}
