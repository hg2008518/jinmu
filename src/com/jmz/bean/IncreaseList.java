package com.jmz.bean;

import java.util.List;

public class IncreaseList extends ParentBean{

	private List<Increase> CommissionIncreaseList;
	private List<PageData> Data;
	public List<Increase> getCommissionIncreaseList() {
		return CommissionIncreaseList;
	}
	public void setCommissionIncreaseList(List<Increase> commissionIncreaseList) {
		CommissionIncreaseList = commissionIncreaseList;
	}
	public List<PageData> getData() {
		return Data;
	}
	public void setData(List<PageData> data) {
		Data = data;
	}
	@Override
	public String toString() {
		return "IncreaseList [CommissionIncreaseList=" + CommissionIncreaseList
				+ ", Data=" + Data + "]";
	}
	
	
}
