package com.jmz.bean;

import java.util.List;

public class ProvinceList {
	private List<ProvinceBean> provinces;

	public List<ProvinceBean> getProvinces() {
		return provinces;
	}

	public void setProvinces(List<ProvinceBean> provinces) {
		this.provinces = provinces;
	}

	@Override
	public String toString() {
		return "ProvinceList [provinces=" + provinces + "]";
	}
	
}
