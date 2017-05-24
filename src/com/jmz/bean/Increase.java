package com.jmz.bean;

public class Increase extends ShareMoneyParent {
	private String CommissionIncreaseID;
	private String Increase;
	private String ExpireDate;
	private String IsLock;

	public String getCommissionIncreaseID() {
		return CommissionIncreaseID;
	}

	public void setCommissionIncreaseID(String commissionIncreaseID) {
		CommissionIncreaseID = commissionIncreaseID;
	}

	public String getIncrease() {
		return Increase;
	}

	public void setIncrease(String increase) {
		Increase = increase;
	}

	public String getExpireDate() {
		return ExpireDate;
	}

	public void setExpireDate(String expireDate) {
		ExpireDate = expireDate;
	}

	public String getIsLock() {
		return IsLock;
	}

	public void setIsLock(String isLock) {
		IsLock = isLock;
	}

	@Override
	public String toString() {
		return "Increase [CommissionIncreaseID=" + CommissionIncreaseID
				+ ", Increase=" + Increase + ", ExpireDate=" + ExpireDate
				+ ", IsLock=" + IsLock + ", getTitle()=" + getTitle()
				+ ", getPostDate()=" + getPostDate() + ", getRemark()="
				+ getRemark() + ", getOrderID()=" + getOrderID()
				+ ", getTradeID()=" + getTradeID() + "]";
	}

}
