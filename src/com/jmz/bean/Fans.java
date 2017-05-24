package com.jmz.bean;

public class Fans extends ParentBean {
	/*CreateDate = "2014/6/24 16:07:22";
            OrderTotal = 65;
            UserName = "182****1986";
*/
	
	private String CreateDate;
	private String OrderTotal;
	private String UserName;
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}
	public String getOrderTotal() {
		return OrderTotal;
	}
	public void setOrderTotal(String orderTotal) {
		OrderTotal = orderTotal;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	@Override
	public String toString() {
		return "Fans [CreateDate=" + CreateDate + ", OrderTotal=" + OrderTotal
				+ ", UserName=" + UserName + "]";
	}
	
}
