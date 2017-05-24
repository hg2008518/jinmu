package com.jmz.bean;

import com.jmz.uitl.Utile;

public class CarProduct extends ParentBean {
	private String ShoppingCartID;
	private String ProductID;
	private String Discount = "1";
	private String DiscountPrice = "0";
	private String UserID;
	private String ProductName;
	private String Quantity = "1";
	private String Price = "0";
	private String FirstPrice= "0";
	private String MarketPrice= "0";
	private String ProductImage;
	private String AttrExtendID;
	private String AttrName;
	private String ReferrerUserID;
	private String PostTime;
	private String ReferrerUserName;
	private String ProductStatus;
	private boolean isCheck;

	public String getReferrerUserName() {
		return ReferrerUserName;
	}

	public void setReferrerUserName(String referrerUserName) {
		ReferrerUserName = referrerUserName;
	}

	public String getDiscount() {
		return Discount;
	}

	public void setDiscount(String discount) {
		Discount = discount;
	}

	public String getDiscountPrice() {
		return DiscountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		DiscountPrice = discountPrice;
	}

	public String getProductStatus() {
		return ProductStatus;
	}

	public void setProductStatus(String productStatus) {
		ProductStatus = productStatus;
	}

	public String getMarketPrice() {
		return MarketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		MarketPrice = marketPrice;
	}

	public String getAttrName() {
		return AttrName;
	}

	public void setAttrName(String attrName) {
		AttrName = attrName;
	}

	public String getShoppingCartID() {
		return ShoppingCartID;
	}

	public void setShoppingCartID(String shoppingCartID) {
		ShoppingCartID = shoppingCartID;
	}

	public String getProductID() {
		return ProductID;
	}

	public void setProductID(String productID) {
		ProductID = productID;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getQuantity() {
		return Quantity;
	}

	public void setQuantity(String quantity) {
		Quantity = quantity;
	}

	public String getPrice() {
		return Price;
	}

	public void setPrice(String price) {
		Price = price;
	}

	public String getFirstPrice() {
		return FirstPrice;
	}

	public void setFirstPrice(String firstPrice) {
		FirstPrice = firstPrice;
	}

	public String getProductImage() {
		return ProductImage;
	}

	public void setProductImage(String productImage) {
		ProductImage = productImage;
	}

	public String getAttrExtendID() {
		return AttrExtendID;
	}

	public void setAttrExtendID(String attrExtendID) {
		AttrExtendID = attrExtendID;
	}

	public String getReferrerUserID() {
		return ReferrerUserID;
	}

	public void setReferrerUserID(String referrerUserID) {
		ReferrerUserID = referrerUserID;
	}

	public String getPostTime() {
		return PostTime;
	}

	public void setPostTime(String postTime) {
		PostTime = postTime;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		if(Utile.isEqual("OnSale", getProductStatus())){
			this.isCheck = isCheck;
		}
	}

	@Override
	public String toString() {
		return "CarProduct [ShoppingCartID=" + ShoppingCartID + ", ProductID="
				+ ProductID + ", Discount=" + Discount + ", DiscountPrice="
				+ DiscountPrice + ", UserID=" + UserID + ", ProductName="
				+ ProductName + ", Quantity=" + Quantity + ", Price=" + Price
				+ ", FirstPrice=" + FirstPrice + ", MarketPrice=" + MarketPrice
				+ ", ProductImage=" + ProductImage + ", AttrExtendID="
				+ AttrExtendID + ", AttrName=" + AttrName + ", ReferrerUserID="
				+ ReferrerUserID + ", PostTime=" + PostTime
				+ ", ReferrerUserName=" + ReferrerUserName + ", ProductStatus="
				+ ProductStatus + ", isCheck=" + isCheck + "]";
	}
}
