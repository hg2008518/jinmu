package com.jmz.bean;

import java.util.ArrayList;

public class FansList extends ParentBean {
	private ArrayList<Fans> FansList;
	private ArrayList<FansData> Data;
	
	public ArrayList<Fans> getFansList() {
		return FansList;
	}
	public void setFansList(ArrayList<Fans> fansList) {
		FansList = fansList;
	}
	public ArrayList<FansData> getData() {
		return Data;
	}
	public void setData(ArrayList<FansData> data) {
		Data = data;
	}
	@Override
	public String toString() {
		return "FansList [FansList=" + FansList + ", Data=" + Data
				+ ", getServerReturn()=" + getServerReturn() + "]";
	}
	
}
