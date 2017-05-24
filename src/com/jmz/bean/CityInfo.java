package com.jmz.bean;

public class CityInfo {
	private String CityID;
	private String CityName;
	private String CityCode;

	public String getCityID() {
		return CityID;
	}

	public void setCityID(String cityID) {
		CityID = cityID;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	public String getCityCode() {
		return CityCode;
	}

	public void setCityCode(String cityCode) {
		CityCode = cityCode;
	}

	@Override
	public String toString() {
		return "CityInfo [CityID=" + CityID + ", CityName=" + CityName
				+ ", CityCode=" + CityCode + "]";
	}

}
