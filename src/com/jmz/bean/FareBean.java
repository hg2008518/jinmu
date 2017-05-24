package com.jmz.bean;

public class FareBean {
	private String FarePrice;

	public String getFarePrice() {
		return FarePrice;
	}

	public void setFarePrice(String farePrice) {
		FarePrice = farePrice;
	}

	@Override
	public String toString() {
		return "FareBean [FarePrice=" + FarePrice + "]";
	}
	
}
