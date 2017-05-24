package com.jmz.bean;

public class Trade extends ParentBean {
	private String TradeID;
	private String TradeStatus;
	private String Payout;

	public String getTradeID() {
		return TradeID;
	}

	public void setTradeID(String tradeID) {
		TradeID = tradeID;
	}

	public String getTradeStatus() {
		return TradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		TradeStatus = tradeStatus;
	}

	@Override
	public String toString() {
		return "Trade [TradeID=" + TradeID + ", TradeStatus=" + TradeStatus
				+ ", Payout=" + Payout + "]";
	}

	public String getPayout() {
		return Payout;
	}

	public void setPayout(String payout) {
		Payout = payout;
	}

}
