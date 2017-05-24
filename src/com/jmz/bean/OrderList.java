package com.jmz.bean;

import java.util.List;

import com.jmz.uitl.OrderState;
import com.jmz.uitl.Utile;

public class OrderList extends ParentBean {
	private List<Order> OrderList;

	public List<Order> getOrderList() {
		return OrderList;
	}

	public void setOrderList(List<Order> orderList) {
		OrderList = orderList;
	}
	/**
	 * 取得订单状态
	 * @return
	 */
	public String getOrderStatus(){
		for(Order order:getOrderList()){
			if(Utile.isEqual(OrderState.WaitBuyerPay.name(),order.getOrderStatus())){
				return order.getOrderStatus();
			}
		}
		return OrderState.WaitSellerSendGoods.name();
	}
	/**
	 * 取得订单号拼接
	 * @return
	 */
	public String getOrderIds(){
		StringBuffer buffer = new StringBuffer();
		for (Order order : getOrderList()) {
			buffer.append(order.getOrderID() + ",");
		}
		return buffer.substring(0,
				buffer.length() - 1);
	} 
	@Override
	public String toString() {
		return "OrderList [OrderList=" + OrderList + ", getServerReturn()="
				+ getServerReturn() + "]";
	}

}
