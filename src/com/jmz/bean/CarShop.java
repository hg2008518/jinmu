package com.jmz.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import com.jmz.uitl.Logger;
import com.jmz.uitl.Utile;

public class CarShop extends ParentBean {
	private String ShopID;
	private String ShopName;
	private String postMsg = "";
	private ArrayList<CarProduct> ProductList;
	private String OrderTotal;
	private String FareTotalPrice = "0";
	private String ExpressType = "Express";
	private boolean isCheck;
	private boolean expressTypeisCheck = true;

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

	public ArrayList<CarProduct> getProductList() {
		return ProductList;
	}

	public ArrayList<CarProduct> getCheckProductList() {
		Iterator<CarProduct> iterator = getProductList().iterator();
		while (iterator.hasNext()) {
			if (!iterator.next().isCheck()) {
				iterator.remove();
			}
		}
		return getProductList();
	}

	public void setProductList(ArrayList<CarProduct> productList) {
		ProductList = productList;
	}

	public String getOrderTotal() {
		return OrderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		OrderTotal = orderTotal;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	
	public boolean isExpressTypeisCheck() {
		return expressTypeisCheck;
	}

	public void setExpressTypeisCheck(boolean expressTypeisCheck) {
		this.expressTypeisCheck = expressTypeisCheck;
	}

	/**
	 * 判断商品是否全选
	 * 
	 */
	public void setAllCheck() {
		for (CarProduct carProduct : ProductList) {
			if (!carProduct.isCheck()) {
				setCheck(false);
				return;
			}
		}
		setCheck(true);
	}

	/**
	 * 全选或反选商品
	 * 
	 * @param b
	 */
	public void selectAllProduct(boolean b) {
		for (CarProduct carProduct : ProductList) {
			carProduct.setCheck(b);
		}
	}

	/**
	 * 选中商品的总价
	 */
	public BigDecimal selectAllProductMoney() {
		BigDecimal decimal = new BigDecimal("0");
		for (CarProduct carProduct : ProductList) {
			if (carProduct.isCheck()) {
				decimal = decimal.add(new BigDecimal(carProduct.getPrice())
				.multiply(new BigDecimal(carProduct.getQuantity())));
			}
		}
		return decimal;
	}

	public String getPostMsg() {
		return postMsg;
	}

	public void setPostMsg(String postMsg) {
		this.postMsg = postMsg;
	}

	public String getFareTotalPrice() {
		return FareTotalPrice;
	}

	public void setFareTotalPrice(String fareTotalPrice) {
		FareTotalPrice = fareTotalPrice;
	}

	public void setExpressType(String expressType) {
		ExpressType = expressType;
	}

	public String getExpressType() {
		return ExpressType;
	}
//	/**
//	 * 判断快递
//	 * @return
//	 */
//	public String getMyExpressType() {
//		if (Utile.isEqual(getExpressType(), "Express")) {
//			if (getFareTotalPrice() != null) {
//				if (new BigDecimal(getFareTotalPrice()).doubleValue() > 0) {
//					return "¥" + getFareTotalPrice();
//				} else {
//					return "包邮";
//				}
//			}
//		} else if (Utile.isEqual(getExpressType(), "SelfGet")) {
//			return "自提";
//		}
//		return "¥" + getFareTotalPrice();
//	}

	@Override
	public String toString() {
		return "CarShop [ShopID=" + ShopID + ", ShopName=" + ShopName
				+ ", postMsg=" + postMsg + ", ProductList=" + ProductList
				+ ", OrderTotal=" + OrderTotal + ", FareTotalPrice="
				+ FareTotalPrice + ", ExpressType=" + ExpressType
				+ ", isCheck=" + isCheck + ", expressTypeisCheck="
				+ expressTypeisCheck + "]";
	}

}
