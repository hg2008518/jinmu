package com.jmz.bean;

import java.util.List;

public class ShopContantList {
	private String ShopTelephone;
	private List<ShopContant> ShopContactsList;
	private List<ShopTel> ShopTelList;
	public String getShopTelephone() {
		return ShopTelephone;
	}
	public void setShopTelephone(String shopTelephone) {
		ShopTelephone = shopTelephone;
	}
	public List<ShopContant> getShopContactsList() {
		return ShopContactsList;
	}
	public void setShopContactsList(List<ShopContant> shopContactsList) {
		ShopContactsList = shopContactsList;
	}
	public List<ShopTel> getShopTelList() {
		return ShopTelList;
	}
	public void setShopTelList(List<ShopTel> shopTelList) {
		ShopTelList = shopTelList;
	}
	@Override
	public String toString() {
		return "ShopContantList [ShopTelephone=" + ShopTelephone
				+ ", ShopContactsList=" + ShopContactsList + ", ShopTelList="
				+ ShopTelList + "]";
	}
	
	
}
