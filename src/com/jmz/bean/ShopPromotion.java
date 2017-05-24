package com.jmz.bean;

import java.io.Serializable;

public class ShopPromotion implements Serializable{
	private String FarePromotionPrice = "0";
	private String ShopPromotionPrice = "0";
//	private ArrayList PromotionInfoList;
	public String getFarePromotionPrice() {
		return FarePromotionPrice;
	}
	public void setFarePromotionPrice(String farePromotionPrice) {
		FarePromotionPrice = farePromotionPrice;
	}
	public String getShopPromotionPrice() {
		return ShopPromotionPrice;
	}
	public void setShopPromotionPrice(String shopPromotionPrice) {
		ShopPromotionPrice = shopPromotionPrice;
	}
	@Override
	public String toString() {
		return "ShopPromotion [FarePromotionPrice=" + FarePromotionPrice
				+ ", ShopPromotionPrice=" + ShopPromotionPrice + "]";
	}
	
}
