package com.jmz.bean;

public class FavBean {
	private String FavProductID;
	private String ProductID;
	private String PostTime;
	private String Remark;
	private String ProductTitle;
	private String ShopPrice;
	private String MarketPrice;
	private String ImageUrl;
	public String getMarketPrice() {
		return MarketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		MarketPrice = marketPrice;
	}

	public String getFavProductTotal() {
		return FavProductTotal;
	}

	public void setFavProductTotal(String favProductTotal) {
		FavProductTotal = favProductTotal;
	}

	private String FavProductTotal;

	public String getFavProductID() {
		return FavProductID;
	}

	public void setFavProductID(String favProductID) {
		FavProductID = favProductID;
	}

	public String getProductID() {
		return ProductID;
	}

	public void setProductID(String productID) {
		ProductID = productID;
	}

	public String getPostTime() {
		return PostTime;
	}

	public void setPostTime(String postTime) {
		PostTime = postTime;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
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

	@Override
	public String toString() {
		return "FavBean [FavProductID=" + FavProductID + ", ProductID="
				+ ProductID + ", PostTime=" + PostTime + ", Remark=" + Remark
				+ ", ProductTitle=" + ProductTitle + ", ShopPrice=" + ShopPrice
				+ ", MarketPrice=" + MarketPrice + ", ImageUrl=" + ImageUrl
				+ ", FavProductTotal=" + FavProductTotal + "]";
	}

}
