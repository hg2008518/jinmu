package com.jmz.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class OneOrderPreview implements Serializable{
	private ArrayList<ProductPromotionInfo> OrderProductList;
	private ShopPromotion ShopPromotion;
	private ItemSummaryPromotionInfo ItemSummaryPromotionInfo;
	public ArrayList<ProductPromotionInfo> getOrderProductList() {
		return OrderProductList;
	}
	public void setOrderProductList(ArrayList<ProductPromotionInfo> orderProductList) {
		OrderProductList = orderProductList;
	}
	public ShopPromotion getShopPromotion() {
		return ShopPromotion;
	}
	public void setShopPromotion(ShopPromotion shopPromotion) {
		ShopPromotion = shopPromotion;
	}
	public ItemSummaryPromotionInfo getItemSummaryPromotionInfo() {
		return ItemSummaryPromotionInfo;
	}
	public void setItemSummaryPromotionInfo(
			ItemSummaryPromotionInfo itemSummaryPromotionInfo) {
		ItemSummaryPromotionInfo = itemSummaryPromotionInfo;
	}
	@Override
	public String toString() {
		return "OneOrderPreview [OrderProductList=" + OrderProductList
				+ ", ShopPromotion=" + ShopPromotion
				+ ", ItemSummaryPromotionInfo=" + ItemSummaryPromotionInfo
				+ "]";
	}
	
}
