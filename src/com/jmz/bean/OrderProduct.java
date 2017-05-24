package com.jmz.bean;

import java.math.BigDecimal;

import com.jmz.uitl.Logger;

public class OrderProduct extends ParentBean {
	private String ProductID;
	private String ProductTitle;
	private String ShopPrice;
	private String ImageUrl;
	private String Quantity;
	private String ActualPrice;
	private String IsComment;
	private String shopMoney;
	private String QrCodeID;
	private String SendGoodsQuantity = "0";
	private String ValidCode;
	private String ProductAttributeID;
	private String ProductAttribute;

	public String getProductAttribute() {
		return ProductAttribute;
	}

	public void setProductAttribute(String productAttribute) {
		ProductAttribute = productAttribute;
	}

	public String getSendGoodsQuantity() {
		return SendGoodsQuantity;
	}

	public void setSendGoodsQuantity(String sendGoodsQuantity) {
		SendGoodsQuantity = sendGoodsQuantity;
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

	public String getShopMoney() {
		BigDecimal a = new BigDecimal(getShopPrice());
		BigDecimal b = new BigDecimal(getQuantity());
		return a.multiply(b).toString();
	}

	public void setShopMoney(String shopMoney) {
		this.shopMoney = shopMoney;
	}

	public String getQrCodeID() {
		return QrCodeID;
	}

	public void setQrCodeID(String qrCodeID) {
		QrCodeID = qrCodeID;
	}

	public String getValidCode() {
		return ValidCode;
	}

	public void setValidCode(String validCode) {
		ValidCode = validCode;
	}

	@Override
	public String toString() {
		return "OrderProduct [ProductID=" + ProductID + ", ProductTitle="
				+ ProductTitle + ", ShopPrice=" + ShopPrice + ", ImageUrl="
				+ ImageUrl + ", Quantity=" + Quantity + ", ActualPrice="
				+ ActualPrice + ", IsComment=" + IsComment + ", shopMoney="
				+ shopMoney + ", QrCodeID=" + QrCodeID + ", SendGoodsQuantity="
				+ SendGoodsQuantity + ", ValidCode=" + ValidCode
				+ ", ProductAttributeID=" + ProductAttributeID
				+ ", ProductAttribute=" + ProductAttribute + "]";
	}
}
