package com.jmz.bean;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.jmz.uitl.Utile;

public class MyOrderShop extends ParentBean {
	private String OrderID;
	private String Title;
	private String OrderAmount;
	private String OrderDate;
	private String OrderStatus;
	private String RefundStatus;
	private String FinishDate;
	private String Postscript;
	private String UserAddressID;
	private String CouponID;
	private String ExpressID;
	private String Trade_TradeID;
	private String ShopName;
	private ArrayList<MyOrderProduct> OrderProductList;
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
	
	public String getTrade_TradeID() {
		return Trade_TradeID;
	}
	public void setTrade_TradeID(String trade_TradeID) {
		Trade_TradeID = trade_TradeID;
	}
	public String getRefundStatus() {
		return RefundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		RefundStatus = refundStatus;
	}
	public String getShopName() {
		return ShopName;
	}
	public void setShopName(String shopName) {
		ShopName = shopName;
	}
	public ArrayList<MyOrderProduct> getOrderProductList() {
		return OrderProductList;
	}
	public void setOrderProductList(ArrayList<MyOrderProduct> orderProductList) {
		OrderProductList = orderProductList;
	}
	/**
	 * 获取数量
	 * @return
	 */
	public BigDecimal getProductNmber(){
		BigDecimal decimal =new BigDecimal("0");
		if(!getOrderProductList().isEmpty()){
			for(MyOrderProduct myOrderProduct:getOrderProductList()){
				decimal = decimal.add(new BigDecimal(myOrderProduct.getQuantity()));
			}
		}
		return decimal;
	}
	/**
	 * 获取所有商品的总额
	 * @return
	 */
	public BigDecimal getShopMoney(){
		BigDecimal decimal =new BigDecimal("0");
		if(!getOrderProductList().isEmpty()){
			for(MyOrderProduct myOrderProduct:getOrderProductList()){
				decimal = decimal.add(new BigDecimal(myOrderProduct.getActualPrice()));
			}
		}
		return decimal;
	}
	
	@Override
	public String toString() {
		return "MyOrder [OrderID=" + OrderID + ", Title=" + Title
				+ ", OrderAmount=" + OrderAmount + ", OrderDate=" + OrderDate
				+ ", OrderStatus=" + OrderStatus + ", RefundStatus="
				+ RefundStatus + ", FinishDate=" + FinishDate + ", Postscript="
				+ Postscript + ", UserAddressID=" + UserAddressID
				+ ", CouponID=" + CouponID + ", ExpressID=" + ExpressID
				+ ", Trade_TradeID=" + Trade_TradeID + ", OrderProductList="
				+ OrderProductList + "]";
	}
	
}
