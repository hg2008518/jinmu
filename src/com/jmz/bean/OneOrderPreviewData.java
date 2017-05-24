package com.jmz.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class OneOrderPreviewData implements Serializable{
	private String OrderAmountTotal;

	public String getOrderAmountTotal() {
		return OrderAmountTotal;
	}

	public void setOrderAmountTotal(String orderAmountTotal) {
		OrderAmountTotal = orderAmountTotal;
	}

	@Override
	public String toString() {
		return "OneOrderPreviewData [OrderAmountTotal=" + OrderAmountTotal
				+ "]";
	}
	
}
