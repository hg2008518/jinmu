package com.jmz.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.text.Html;

public class OneOrderPreviewList extends ParentBean {
	private ArrayList<OrderPreviewInfo> OrderPreviewInfo;
	private OneOrderPreviewData Data;
	private String orderID;
	private String userPayMoney;
	private String productType;
	private String orderStatus;
	private boolean paySuss;
	private AddressBean addressBean;

	public ArrayList<OrderPreviewInfo> getOrderPreviewInfo() {
		return OrderPreviewInfo;
	}

	public void setOrderPreviewInfo(ArrayList<OrderPreviewInfo> orderPreviewInfo) {
		OrderPreviewInfo = orderPreviewInfo;
	}

	public OneOrderPreviewData getData() {
		return Data;
	}

	public void setData(OneOrderPreviewData data) {
		Data = data;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getUserPayMoney() {
		return userPayMoney;
	}

	public void setUserPayMoney(String userPayMoney) {
		this.userPayMoney = userPayMoney;
	}

//	/**
//	 * 取得所有商品的名字和属性
//	 * 
//	 * @return
//	 */
//	public String getAllProductTitle() {
//		StringBuffer buffer = new StringBuffer();
//		for (PreviewOrderProductList list : OrderPreviewInfo.get(0)
//				.getOrderProductList()) {
//			buffer.append(list.getProductPromotionInfo().getProductTitle()
//					+ "\r\n"
//					+ Html.fromHtml("<font color='#A9A9A9'>"
//							+ list.getProductPromotionInfo().getAttrName()
//							+ "</font>") + "\r\n");
//		}
//		return buffer.toString();
//	}

	@Override
	public String toString() {
		return "OneOrderPreviewList [OrderPreviewInfo=" + OrderPreviewInfo
				+ ", Data=" + Data + ", orderID=" + orderID + ", userPayMoney="
				+ userPayMoney + ", productType=" + productType
				+ ", orderStatus=" + orderStatus + ", paySuss=" + paySuss
				+ ", addressBean=" + addressBean + ", getServerReturn()="
				+ getServerReturn() + "]";
	}

	public boolean isPaySuss() {
		return paySuss;
	}

	public void setPaySuss(boolean paySuss) {
		this.paySuss = paySuss;
	}

	public AddressBean getAddressBean() {
		return addressBean;
	}

	public void setAddressBean(AddressBean addressBean) {
		this.addressBean = addressBean;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

}
