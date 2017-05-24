package com.jmz.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import android.util.Log;

import com.jmz.uitl.Utile;

public class ItemSummaryPromotionInfo implements Serializable{
	private String ShopID;
	private String ShopName;
	private String FarePrice="0";
	private String FarePromotionPrice="0";
	private String FareActualPrice="0";
	private String ShopPrice="0";
	private String ShopPromotionPrice="0";
	private String ShopActualPrice="0";
	private String ItemSummaryAmountTotal;

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

	public String getFarePrice() {
		return FarePrice;
	}

	public void setFarePrice(String farePrice) {
		FarePrice = farePrice;
	}

	public String getFarePromotionPrice() {
		return FarePromotionPrice;
	}

	public void setFarePromotionPrice(String farePromotionPrice) {
		FarePromotionPrice = farePromotionPrice;
	}

	public String getFareActualPrice() {
		return FareActualPrice;
	}
	/**
	 * 判断快递
	 * @return
	 */
	public String getMyFareActualPrice(String expressType) {
		if (Utile.isEqual(expressType, "Express")) {
			if (getFareActualPrice() != null) {
				if (new BigDecimal(getFareActualPrice()).doubleValue() > 0) {
					return "¥" + getFareActualPrice();
				} else {
					return "包邮";
				}
			}
		} else if (Utile.isEqual(expressType, "SelfGet")) {
			return "自提";
		}
		return "¥" + getFareActualPrice();
	}
	public void setFareActualPrice(String fareActualPrice) {
		FareActualPrice = fareActualPrice;
	}

	public String getShopPrice() {
		return ShopPrice;
	}

	public void setShopPrice(String shopPrice) {
		ShopPrice = shopPrice;
	}

	public String getShopPromotionPrice() {
		return ShopPromotionPrice;
	}

	public void setShopPromotionPrice(String shopPromotionPrice) {
		ShopPromotionPrice = shopPromotionPrice;
	}

	public String getShopActualPrice() {
		return ShopActualPrice;
	}

	public void setShopActualPrice(String shopActualPrice) {
		ShopActualPrice = shopActualPrice;
	}

	public String getItemSummaryAmountTotal() {
		return ItemSummaryAmountTotal;
	}

	public void setItemSummaryAmountTotal(String itemSummaryAmountTotal) {
		ItemSummaryAmountTotal = itemSummaryAmountTotal;
	}

	@Override
	public String toString() {
		return "ItemSummaryPromotionInfo [ShopID=" + ShopID + ", ShopName="
				+ ShopName + ", FarePrice=" + FarePrice
				+ ", FarePromotionPrice=" + FarePromotionPrice
				+ ", FareActualPrice=" + FareActualPrice + ", ShopPrice="
				+ ShopPrice + ", ShopPromotionPrice=" + ShopPromotionPrice
				+ ", ShopActualPrice=" + ShopActualPrice
				+ ", ItemSummaryAmountTotal=" + ItemSummaryAmountTotal + "]";
	}

}
