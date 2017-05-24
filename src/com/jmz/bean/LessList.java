package com.jmz.bean;

import java.util.List;

public class LessList extends ParentBean {
	private List<Less> CommissionLessList;
	private List<PageData> Data;
	public List<Less> getCommissionLessList() {
		return CommissionLessList;
	}
	public void setCommissionLessList(List<Less> commissionLessList) {
		CommissionLessList = commissionLessList;
	}
	public List<PageData> getData() {
		return Data;
	}
	public void setData(List<PageData> data) {
		Data = data;
	}
	@Override
	public String toString() {
		return "LessList [CommissionLessList=" + CommissionLessList + ", Data="
				+ Data + "]";
	}
	
}
