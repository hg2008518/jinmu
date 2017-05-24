package com.jmz.bean;

import java.util.List;

public class ProvinceBean {
	private String province;
	private List<CityBean> citys;
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public List<CityBean> getCitys() {
		return citys;
	}
	public void setCitys(List<CityBean> citys) {
		this.citys = citys;
	}
	@Override
	public String toString() {
		return "ProvinceBean [province=" + province + ", citys=" + citys + "]";
	}
	
}
