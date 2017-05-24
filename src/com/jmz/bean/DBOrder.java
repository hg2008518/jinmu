package com.jmz.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import com.jmz.uitl.Logger;

/**
 * 订单
 * 
 * @author Administrator
 * 
 */
public class DBOrder implements Serializable {
	private int _id;
	private String imageUrl;
	private String productTitle;
	private String productId;
	private String shopId;
	private String shopTitle;
	private int productNumber = 1;
	private float productPrice;
	private float productTotal;
	private float productFare;
	private int isSelect;

	public DBOrder() {

	}
	public DBOrder(int _id, String imageUrl, String productTitle,
			String productId, String shopId, String shopTitle,
			int productNumber, float productPrice, float productTotal,
			float productFare, int isSelect) {
		this._id = _id;
		this.imageUrl = imageUrl;
		this.productTitle = productTitle;
		this.productId = productId;
		this.shopId = shopId;
		this.shopTitle = shopTitle;
		this.productNumber = productNumber;
		this.productPrice = productPrice;
		this.productTotal = productTotal;
		this.productFare = productFare;
		this.isSelect = isSelect;
	}
	public DBOrder( String imageUrl, String productTitle,
			String productId, String shopId, String shopTitle,
			int productNumber, float productPrice, float productTotal,
			float productFare, int isSelect) {
		this.imageUrl = imageUrl;
		this.productTitle = productTitle;
		this.productId = productId;
		this.shopId = shopId;
		this.shopTitle = shopTitle;
		this.productNumber = productNumber;
		this.productPrice = productPrice;
		this.productTotal = productTotal;
		this.productFare = productFare;
		this.isSelect = isSelect;
	}
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopTitle() {
		return shopTitle;
	}

	public void setShopTitle(String shopTitle) {
		this.shopTitle = shopTitle;
	}

	public int getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(int productNumber) {
		this.productNumber = productNumber;
	}

	public float getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(float productPrice) {
		this.productPrice = productPrice;
	}

	public int getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(int isSelect) {
		this.isSelect = isSelect;
	}

	public float getProductTotal() {
		return getProductMoney(productPrice, productNumber);
	}

	/**
	 * 取得每个商品的所有金额
	 */
	private float getProductMoney(float price, int number) {
		if (price > 0 && number > 0) {
			BigDecimal priceDecimal = new BigDecimal(price+"");
			BigDecimal numberDecimal = new BigDecimal(number+"");
			return Float.parseFloat(priceDecimal.multiply(numberDecimal) + "");
		}
		return 1000.0f;
	}

	@Override
	public String toString() {
		return "DBOrder [_id=" + _id + ", imageUrl=" + imageUrl
				+ ", productTitle=" + productTitle + ", productId=" + productId
				+ ", shopId=" + shopId + ", shopTitle=" + shopTitle
				+ ", productNumber=" + productNumber + ", productPrice="
				+ productPrice + ", productTotal=" + productTotal
				+ ", isSelect=" + isSelect + "]";
	}

	public float getProductFare() {
		return productFare;
	}

	public void setProductFare(float productFare) {
		this.productFare = productFare;
	}
}
