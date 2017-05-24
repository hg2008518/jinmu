package com.jmz.bean;

import java.util.List;

public class MyOrderInfoList {
	private List<MyOrderInfo> OrderInfo;
	private List<PageData> Data;
	public List<MyOrderInfo> getOrderInfo() {
		return OrderInfo;
	}
	public void setOrderInfo(List<MyOrderInfo> orderInfo) {
		OrderInfo = orderInfo;
	}
	
	public List<PageData> getData() {
		return Data;
	}
	public void setData(List<PageData> data) {
		Data = data;
	}
	@Override
	public String toString() {
		return "MyOrderInfoList [OrderInfo=" + OrderInfo + ", Data=" + Data
				+ "]";
	}

}
