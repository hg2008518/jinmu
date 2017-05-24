package com.jmz.bean;

import java.util.List;


public class CityBean{
	private String city;
	private List<XianBean> xians;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public List<XianBean> getXians() {
		return xians;
	}
	public void setXians(List<XianBean> xians) {
		this.xians = xians;
	}
	@Override
	public String toString() {
		return "CityBean [city=" + city + ", xians=" + xians + "]";
	}

}
