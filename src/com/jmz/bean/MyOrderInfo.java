package com.jmz.bean;

import java.math.BigDecimal;
import java.util.List;

public class MyOrderInfo extends ParentBean {
	@Override
	public String toString() {
		return "MyOrderInfo [OrderID=" + OrderID + ", Title=" + Title
				+ ", OrderAmount=" + OrderAmount + ", OrderDate=" + OrderDate
				+ ", OrderStatus=" + OrderStatus + ", RefundStatus="
				+ RefundStatus + ", FinishDate=" + FinishDate + ", Postscript="
				+ Postscript + ", UserAddressID=" + UserAddressID
				+ ", CouponID=" + CouponID + ", ExpressID=" + ExpressID
				+ ", ExpressString=" + ExpressString + ", ExpireDate="
				+ ExpireDate + ", ShopExtend=" + ShopExtend + ", ProductType="
				+ ProductType + ", ValidCode=" + ValidCode
				+ ", OrderProductList=" + OrderProductList + "]";
	}

	private String OrderID;
	private String Title;
	/**
	 * 商品总支付（包括运费）
	 */
	private String OrderAmount;
	private String OrderDate;
	private String OrderStatus;
	private String RefundStatus;
	private String FinishDate;
	private String Postscript;
	private String UserAddressID;
	private String CouponID;
	private String ExpressID;
	private String ExpressString;
	private String ExpireDate;
	private String ShopExtend;
	private String ProductType;
	private String ValidCode;
	private List<OrderProduct> OrderProductList;



	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getOrderAmount() {
		return OrderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		OrderAmount = orderAmount;
	}

	public String getOrderDate() {
		return OrderDate;
	}

	public void setOrderDate(String orderDate) {
		OrderDate = orderDate;
	}

	public String getOrderStatus() {
		return OrderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}

	public String getFinishDate() {
		return FinishDate;
	}

	public void setFinishDate(String finishDate) {
		FinishDate = finishDate;
	}

	public String getPostscript() {
		return Postscript;
	}

	public void setPostscript(String postscript) {
		Postscript = postscript;
	}

	public String getUserAddressID() {
		return UserAddressID;
	}

	public void setUserAddressID(String userAddressID) {
		UserAddressID = userAddressID;
	}

	public String getCouponID() {
		return CouponID;
	}

	public void setCouponID(String couponID) {
		CouponID = couponID;
	}

	public String getExpressID() {
		return ExpressID;
	}

	public void setExpressID(String expressID) {
		ExpressID = expressID;
	}

	public String getExpressString() {
		return ExpressString;
	}

	public void setExpressString(String expressString) {
		ExpressString = expressString;
	}

	public String getShopExtend() {
		return ShopExtend;
	}

	public void setShopExtend(String shopExtend) {
		ShopExtend = shopExtend;
	}

	public List<OrderProduct> getOrderProductList() {
		return OrderProductList;
	}

	public void setOrderProductList(List<OrderProduct> orderProductList) {
		OrderProductList = orderProductList;
	}

	public String getProductType() {
		return ProductType;
	}

	public void setProductType(String productType) {
		ProductType = productType;
	}

	public String getRefundStatus() {
		return RefundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		RefundStatus = refundStatus;
	}

	/**
	 * 获取数量
	 * 
	 * @return
	 */
	public BigDecimal getProductNmber() {
		BigDecimal decimal = new BigDecimal("0");
		if (!getOrderProductList().isEmpty()) {
			for (OrderProduct orderProduct : getOrderProductList()) {
				decimal = decimal
						.add(new BigDecimal(orderProduct.getQuantity()));
			}
		}
		return decimal;
	}

	/**
	 * 获取所有商品的总额
	 * 
	 * @return
	 */
	public BigDecimal getShopMoney() {
		BigDecimal decimal = new BigDecimal("0");
		if (!getOrderProductList().isEmpty()) {
			for (OrderProduct orderProduct : getOrderProductList()) {
				decimal = decimal.add(new BigDecimal(orderProduct
						.getActualPrice()));
			}
		}
		return decimal;
	}

	public String getValidCode() {
		return ValidCode;
	}

	public void setValidCode(String validCode) {
		ValidCode = validCode;
	}

	public String getExpireDate() {
		return ExpireDate;
	}

	public void setExpireDate(String expireDate) {
		ExpireDate = expireDate;
	}

}
