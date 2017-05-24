package com.jmz.bean;

import java.util.List;

public class TradeInfo extends ParentBean{
	private List<TradeBean> TradeInfo;
	private List<PageData> Data;
	public List<PageData> getData() {
		return Data;
	}
	public void setData(List<PageData> data) {
		Data = data;
	}
	public List<TradeBean> getTradeInfo() {
		return TradeInfo;
	}
	public void setTradeInfo(List<TradeBean> tradeInfo) {
		TradeInfo = tradeInfo;
	}
	
	@Override
	public String toString() {
		return "TradeInfo [TradeInfo=" + TradeInfo + ", Data=" + Data + "]";
	}

}
