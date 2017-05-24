package com.jmz.bean;

public class Less extends ShareMoneyParent {

	private String CommissionLessID;
	private String Less;

	public String getCommissionLessID() {
		return CommissionLessID;
	}

	public void setCommissionLessID(String commissionLessID) {
		CommissionLessID = commissionLessID;
	}

	public String getLess() {
		return Less;
	}

	public void setLess(String less) {
		Less = less;
	}

	@Override
	public String toString() {
		return "Less [CommissionLessID=" + CommissionLessID + ", Less=" + Less
				+ ", getTitle()=" + getTitle() + ", getPostDate()="
				+ getPostDate() + ", getRemark()=" + getRemark()
				+ ", getOrderID()=" + getOrderID() + ", getTradeID()="
				+ getTradeID() + "]";
	}

}
