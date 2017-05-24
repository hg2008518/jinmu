package com.jmz.bean;

import java.util.List;

public class ShopInfo extends ParentBean {
	private List<Shop> ShopInfo;

	public List<Shop> getShopInfo() {
		return ShopInfo;
	}

	public void setShopInfo(List<Shop> shopInfo) {
		ShopInfo = shopInfo;
	}

	@Override
	public String toString() {
		return "ShopInfo [ShopInfo=" + ShopInfo + "]";
	}

	
}
