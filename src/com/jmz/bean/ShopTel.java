package com.jmz.bean;

public class ShopTel {
	private String ShopName;
	private String Telephone;
	public String getShopName() {
		return ShopName;
	}
	public void setShopName(String shopName) {
		ShopName = shopName;
	}
	public String getTelephone() {
		return Telephone;
	}
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	@Override
	public String toString() {
		return "ShopTel [ShopName=" + ShopName + ", Telephone=" + Telephone
				+ "]";
	}
	
}
