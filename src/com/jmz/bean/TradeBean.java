package com.jmz.bean;

public class TradeBean {
	private String TradeID;
	private String Title;
	private String TradeType;
	private String Income;
	private String Payout;
	private String PostDate;
	private String PayDate;
	private String EndDate;
	private String ExpireDate;
	private String TradeStatus;
	private String Remark;
	private String TransPlatform;
	private String OrderID;

	@Override
	public String toString() {
		return "TradeBean [TradeID=" + TradeID + ", Title=" + Title
				+ ", TradeType=" + TradeType + ", Income=" + Income
				+ ", Payout=" + Payout + ", PostDate=" + PostDate
				+ ", PayDate=" + PayDate + ", EndDate=" + EndDate
				+ ", ExpireDate=" + ExpireDate + ", TradeStatus=" + TradeStatus
				+ ", Remark=" + Remark + ", TransPlatform=" + TransPlatform
				+ ", OrderID=" + OrderID + "]";
	}

	public String getTradeID() {
		return TradeID;
	}

	public void setTradeID(String tradeID) {
		TradeID = tradeID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getTradeType() {
		return TradeType;
	}

	public void setTradeType(String tradeType) {
		TradeType = tradeType;
	}

	public String getIncome() {
		return Income;
	}

	public void setIncome(String income) {
		Income = income;
	}

	public String getPayout() {
		return Payout;
	}

	public void setPayout(String payout) {
		Payout = payout;
	}

	public String getPostDate() {
		return PostDate;
	}

	public void setPostDate(String postDate) {
		PostDate = postDate;
	}

	public String getPayDate() {
		return PayDate;
	}

	public void setPayDate(String payDate) {
		PayDate = payDate;
	}

	public String getEndDate() {
		return EndDate;
	}

	public void setEndDate(String endDate) {
		EndDate = endDate;
	}

	public String getExpireDate() {
		return ExpireDate;
	}

	public void setExpireDate(String expireDate) {
		ExpireDate = expireDate;
	}

	public String getTradeStatus() {
		return TradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		TradeStatus = tradeStatus;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getTransPlatform() {
		return TransPlatform;
	}

	public void setTransPlatform(String transPlatform) {
		TransPlatform = transPlatform;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}
}
