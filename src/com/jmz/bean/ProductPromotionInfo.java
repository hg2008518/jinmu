package com.jmz.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductPromotionInfo implements Serializable{
	private String  ProductID;
	private String  ProductTitle;
	private String  ShopPrice;
	private String  MarketPrice;
	private String  AttrExtendID;
	private String  AttrName;
	private String  Discount = "1";
	private String  DiscountPrice;
	private String  ImageUrl;
	private String  Quantity;
	private String  PromotionPrice;
	private String  ActualPrice;
//    ArrayList<>ProductPromotionList
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
	public String getMarketPrice() {
		return MarketPrice;
	}
	public void setMarketPrice(String marketPrice) {
		MarketPrice = marketPrice;
	}
	public String getAttrExtendID() {
		return AttrExtendID;
	}
	public void setAttrExtendID(String attrExtendID) {
		AttrExtendID = attrExtendID;
	}
	public String getAttrName() {
		return AttrName;
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

	public void setAttrName(String attrName) {
		AttrName = attrName;
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
	public String getPromotionPrice() {
		return PromotionPrice;
	}
	public void setPromotionPrice(String promotionPrice) {
		PromotionPrice = promotionPrice;
	}
	public String getActualPrice() {
		return ActualPrice;
	}
	public void setActualPrice(String actualPrice) {
		ActualPrice = actualPrice;
	}
	@Override
	public String toString() {
		return "ProductPromotionInfo [ProductID=" + ProductID
				+ ", ProductTitle=" + ProductTitle + ", ShopPrice=" + ShopPrice
				+ ", MarketPrice=" + MarketPrice + ", AttrExtendID="
				+ AttrExtendID + ", AttrName=" + AttrName + ", Discount="
				+ Discount + ", DiscountPrice=" + DiscountPrice + ", ImageUrl="
				+ ImageUrl + ", Quantity=" + Quantity + ", PromotionPrice="
				+ PromotionPrice + ", ActualPrice=" + ActualPrice + "]";
	}
}
