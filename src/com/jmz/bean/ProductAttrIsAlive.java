package com.jmz.bean;

public class ProductAttrIsAlive {
	private String attrValue; 
	private String isAlive;
	public String getAttrValue() {
		return attrValue;
	}
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}
	public String getIsAlive() {
		return isAlive;
	}
	public void setIsAlive(String isAlive) {
		this.isAlive = isAlive;
	}
	@Override
	public String toString() {
		return "ProductAttrIsAlive [attrValue=" + attrValue + ", isAlive="
				+ isAlive + "]";
	}
}
