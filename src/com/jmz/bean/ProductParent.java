package com.jmz.bean;

import java.util.regex.Pattern;

public class ProductParent extends ParentBean {
	private String ProductID = "0";
	private String ProductTitle;
	private String ShopName;
	private String ImageUrl;
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	public String getProductID() {
		return ProductID;
	}
	public void setProductID(String productID) {
		ProductID = productID;
	}
	public String getProductTitle() {
		return ProductTitle;
	}
	public void setProductTitle(String productTitle) {
		ProductTitle = productTitle;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public Pattern getP() {
		return p;
	}
	public void setP(Pattern p) {
		this.p = p;
	}
	public String getShopName() {
		return ShopName;
	}
	public void setShopName(String shopName) {
		ShopName = shopName;
	}
	@Override
	public String toString() {
		return "ProductParent [ProductID=" + ProductID + ", ProductTitle="
				+ ProductTitle + ", ShopName=" + ShopName + ", ImageUrl="
				+ ImageUrl + ", p=" + p + "]";
	}
	
}
