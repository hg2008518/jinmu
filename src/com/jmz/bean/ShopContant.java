package com.jmz.bean;


public class ShopContant {
	private String Name;
	private String QQ;
	
	public ShopContant() {
	}
	public ShopContant(String name, String qQ) {
		Name = name;
		QQ = qQ;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getQQ() {
		return QQ;
	}
	public void setQQ(String qQ) {
		QQ = qQ;
	}
	@Override
	public String toString() {
		return "ShopContant [Name=" + Name + ", QQ=" + QQ + "]";
	}
	
}
