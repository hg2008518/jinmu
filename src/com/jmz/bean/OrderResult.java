package com.jmz.bean;


public class OrderResult extends ParentBean  {
	private String TransState;

	public String getTransState() {
		return TransState;
	}

	public void setTransState(String transState) {
		TransState = transState;
	}

	@Override
	public String toString() {
		return "OrderResult [TransState=" + TransState + ", getServerReturn()="
				+ getServerReturn() + "]";
	}
	
}
