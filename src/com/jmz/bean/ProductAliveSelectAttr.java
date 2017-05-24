package com.jmz.bean;

import java.util.List;

public class ProductAliveSelectAttr {
	private List<ProductAttrIsAlive> attrIsAlive;
	private ProductAttrSelected getAttr;
	public List<ProductAttrIsAlive> getAttrIsAlive() {
		return attrIsAlive;
	}
	public void setAttrIsAlive(List<ProductAttrIsAlive> attrIsAlive) {
		this.attrIsAlive = attrIsAlive;
	}
	public ProductAttrSelected getGetAttr() {
		return getAttr;
	}
	public void setGetAttr(ProductAttrSelected getAttr) {
		this.getAttr = getAttr;
	}
	@Override
	public String toString() {
		return "ProductAliveSelectAttr [attrIsAlive=" + attrIsAlive
				+ ", getAttr=" + getAttr + "]";
	}
	
}
