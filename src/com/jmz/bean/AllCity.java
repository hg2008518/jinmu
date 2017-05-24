package com.jmz.bean;

import java.util.List;

public class AllCity extends ParentBean {
	private List<City> CityList;

	public List<City> getCityList() {
		return CityList;
	}

	public void setCityList(List<City> cityList) {
		CityList = cityList;
	}

	@Override
	public String toString() {
		return "AllCity [CityList=" + CityList + "]";
	}

}
