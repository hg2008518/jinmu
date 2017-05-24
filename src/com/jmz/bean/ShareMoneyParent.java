package com.jmz.bean;

public class ShareMoneyParent {
	private String Title;
	private String PostDate;
	private String Remark;
	private String OrderID;
	private String TradeID;

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getPostDate() {
		return PostDate;
	}

	public void setPostDate(String postDate) {
		PostDate = postDate;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getTradeID() {
		return TradeID;
	}

	public void setTradeID(String tradeID) {
		TradeID = tradeID;
	}

	@Override
	public String toString() {
		return "ShareMoneyParent [Title=" + Title + ", PostDate=" + PostDate
				+ ", Remark=" + Remark + ", OrderID=" + OrderID + ", TradeID="
				+ TradeID + "]";
	}

}
