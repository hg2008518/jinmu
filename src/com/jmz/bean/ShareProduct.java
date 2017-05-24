package com.jmz.bean;


public class ShareProduct extends ProductParent {
	private String UserID;
	private String Total;
	private String ShareProductTotal;
	private String SpreadTotal;
	
	public String getShareProductTotal() {
		return ShareProductTotal;
	}
	public void setShareProductTotal(String shareProductTotal) {
		ShareProductTotal = shareProductTotal;
	}
	
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getTotal() {
		return Total;
	}
	public void setTotal(String total) {
		Total = total;
	}
	
	public String getSpreadTotal() {
		return SpreadTotal;
	}
	public void setSpreadTotal(String spreadTotal) {
		SpreadTotal = spreadTotal;
	}
	@Override
	public String toString() {
		return "ShareProduct [UserID=" + UserID + ", Total=" + Total
				+ ", ShareProductTotal=" + ShareProductTotal + ", SpreadTotal="
				+ SpreadTotal + ", getProductID()=" + getProductID()
				+ ", getProductTitle()=" + getProductTitle()
				+ ", getImageUrl()=" + getImageUrl() + ", getP()=" + getP()
				+ ", getShopName()=" + getShopName() + ", getServerReturn()="
				+ getServerReturn() + "]";
	}
	
	
	
}
