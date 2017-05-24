package com.jmz.bean;

public class Order extends ParentBean {
	private String OrderID;
	private String OrderStatus;
	private String ProductType;

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getOrderStatus() {
		return OrderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "Order [OrderID=" + OrderID + ", OrderStatus=" + OrderStatus
				+ ", ProductType=" + ProductType + "]";
	}

	public String getProductType() {
		return ProductType;
	}

	public void setProductType(String productType) {
		ProductType = productType;
	}
}
