package com.jmz.bean;

public class ProductInfoData extends ParentBean {
	private String OrderTotal;
	private String ProductCommentTotal;
	private String ShareProductTotal;

	public String getShareProductTotal() {
		return ShareProductTotal;
	}

	public void setShareProductTotal(String shareProductTotal) {
		ShareProductTotal = shareProductTotal;
	}

	public String getOrderTotal() {
		return OrderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		OrderTotal = orderTotal;
	}

	public String getProductCommentTotal() {
		return ProductCommentTotal;
	}

	public void setProductCommentTotal(String productCommentTotal) {
		ProductCommentTotal = productCommentTotal;
	}

	@Override
	public String toString() {
		return "TicketInfoData [OrderTotal=" + OrderTotal
				+ ", ProductCommentTotal=" + ProductCommentTotal
				+ ", ShareProductTotal=" + ShareProductTotal + "]";
	}

}
