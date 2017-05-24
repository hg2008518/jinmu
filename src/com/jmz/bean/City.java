package com.jmz.bean;

import com.jmz.uitl.Utile;

public class City extends ParentBean {
	private String CityID;
	private String CityName;
	private String CityCode;
	private String OpenFlg;

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

	public String getOpenFlg() {
		return OpenFlg;
	}

	public void setOpenFlg(String openFlg) {
		OpenFlg = openFlg;
	}

	/**
	 * 判断是否开通
	 * @return
	 */
	public boolean getOpenFlgBool() {
		if (Utile.isEqual(OpenFlg, "0")) {
			return false;
		} else if (Utile.isEqual(OpenFlg, "1")) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "City [CityID=" + CityID + ", CityName=" + CityName
				+ ", CityCode=" + CityCode + ", OpenFlg=" + OpenFlg + "]";
	}
}
