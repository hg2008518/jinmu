package com.jmz.bean;

import java.math.BigDecimal;

public class MyOrderProduct extends ParentBean {
			/*
			ActualPrice = "0.01";
            ImageUrl = "/upload/image/2015/04/29/111105708242.jpg";
            IsComment = False;
            ProductAttribute = "";
            ProductAttributeID = 0;
            ProductID = 1731;
            ProductTitle = test1;
            QrCodeID = 957;
            Quantity = 1;
            SendGoodsQuantity = 0;
            ShopPrice = "0.01";
            ValidCode = 18616374;*/
	 private String ProductID;
	 private String ProductTitle;
	 private String ShopPrice;
	 private String ImageUrl;
	 private String Quantity;
	 private String SendGoodsQuantity;
	 private String ActualPrice;
	 private String IsComment;
	 private String ProductAttributeID;
	 private String ProductAttribute;
	 
	 
	public String getProductAttribute() {
		return ProductAttribute;
	}
	public void setProductAttribute(String productAttribute) {
		ProductAttribute = productAttribute;
	}
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
	public String getShopPrice() {
		return ShopPrice;
	}
	public void setShopPrice(String shopPrice) {
		ShopPrice = shopPrice;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public String getQuantity() {
		return Quantity;
	}
	public void setQuantity(String quantity) {
		Quantity = quantity;
	}
	public String getSendGoodsQuantity() {
		return SendGoodsQuantity;
	}
	public void setSendGoodsQuantity(String sendGoodsQuantity) {
		SendGoodsQuantity = sendGoodsQuantity;
	}
	public String getActualPrice() {
		return ActualPrice;
	}
	public void setActualPrice(String actualPrice) {
		ActualPrice = actualPrice;
	}
	public String getIsComment() {
		return IsComment;
	}
	public void setIsComment(String isComment) {
		IsComment = isComment;
	}
	@Override
	public String toString() {
		return "MyOrderProduct [ProductID=" + ProductID + ", ProductTitle="
				+ ProductTitle + ", ShopPrice=" + ShopPrice + ", ImageUrl="
				+ ImageUrl + ", Quantity=" + Quantity + ", SendGoodsQuantity="
				+ SendGoodsQuantity + ", ActualPrice=" + ActualPrice
				+ ", IsComment=" + IsComment + ", ProductAttributeID="
				+ ProductAttributeID + ", ProductAttribute=" + ProductAttribute
				+ "]";
	}
    
}
