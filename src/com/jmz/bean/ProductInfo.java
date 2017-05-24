package com.jmz.bean;

import java.util.List;

public class ProductInfo extends ParentBean {
	private List<Product> ProductInfo;
	private List<ProductInfoData> Data;
	public List<Product> getProductInfo() {
		return ProductInfo;
	}
	public void setProductInfo(List<Product> productInfo) {
		ProductInfo = productInfo;
	}
	public List<ProductInfoData> getData() {
		return Data;
	}
	public void setData(List<ProductInfoData> data) {
		Data = data;
	}
	@Override
	public String toString() {
		return "TicketInfo [ProductInfo=" + ProductInfo + ", Data=" + Data
				+ "]";
	}
	
	
	
}
