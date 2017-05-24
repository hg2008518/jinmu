package com.jmz.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderPreviewInfo implements Serializable {
	private ArrayList<PreviewOrderProductList> OrderProductList;
	private ShopPromotion ShopPromotion;
	private ItemSummaryPromotionInfo ItemSummaryPromotionInfo;
	public ArrayList<PreviewOrderProductList> getOrderProductList() {
		return OrderProductList;
	}
	public void setOrderProductList(
			ArrayList<PreviewOrderProductList> orderProductList) {
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
		return "OrderPreviewInfo [OrderProductList=" + OrderProductList
				+ ", ShopPromotion=" + ShopPromotion
				+ ", ItemSummaryPromotionInfo=" + ItemSummaryPromotionInfo
				+ "]";
	}

	
}
