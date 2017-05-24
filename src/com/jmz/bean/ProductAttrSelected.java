package com.jmz.bean;

public class ProductAttrSelected {
	private String attrExtendID;
	private String goodsNum;
	/**
	 * 库存
	 */
	private String Invaentory;
	/**
	 * 编号
	 */
	private String goodsPrice;
	/**
	 * 销售量
	 */
	private String orderTotal;
	public String getAttrExtendID() {
		return attrExtendID;
	}
	public void setAttrExtendID(String attrExtendID) {
		this.attrExtendID = attrExtendID;
	}
	public String getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(String goodsNum) {
		this.goodsNum = goodsNum;
	}
	public String getInvaentory() {
		return Invaentory;
	}
	public void setInvaentory(String invaentory) {
		Invaentory = invaentory;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}
	@Override
	public String toString() {
		return "ProductAttrSelected [attrExtendID=" + attrExtendID
				+ ", goodsNum=" + goodsNum + ", Invaentory=" + Invaentory
				+ ", goodsPrice=" + goodsPrice + ", orderTotal=" + orderTotal
				+ "]";
	}
}
