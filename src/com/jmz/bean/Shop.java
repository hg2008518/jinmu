package com.jmz.bean;

public class Shop extends ParentBean {
	private String ShopID;
	private String ShopName;
	private String Summary;
	private String ImageUrl;
	private String ProductImageUrls;
	private String Address;
	private String Telephone;
	private String Contact;
	private String CreateDate;
	private String BusinessHours;
	public String getShopID() {
		return ShopID;
	}
	public void setShopID(String shopID) {
		ShopID = shopID;
	}
	public String getShopName() {
		return ShopName;
	}
	public void setShopName(String shopName) {
		ShopName = shopName;
	}
	public String getSummary() {
		return Summary;
	}
	public void setSummary(String summary) {
		Summary = summary;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public String getProductImageUrls() {
		return ProductImageUrls;
	}
	public void setProductImageUrls(String productImageUrls) {
		ProductImageUrls = productImageUrls;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getTelephone() {
		return Telephone;
	}
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	public String getContact() {
		return Contact;
	}
	public void setContact(String contact) {
		Contact = contact;
	}
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}
	public String getBusinessHours() {
		return BusinessHours;
	}
	public void setBusinessHours(String businessHours) {
		BusinessHours = businessHours;
	}
	@Override
	public String toString() {
		return "Shop [ShopID=" + ShopID + ", ShopName=" + ShopName
				+ ", Summary=" + Summary + ", ImageUrl=" + ImageUrl
				+ ", ProductImageUrls=" + ProductImageUrls + ", Address="
				+ Address + ", Telephone=" + Telephone + ", Contact=" + Contact
				+ ", CreateDate=" + CreateDate + ", BusinessHours="
				+ BusinessHours + "]";
	}
	
}
