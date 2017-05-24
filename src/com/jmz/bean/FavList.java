package com.jmz.bean;

import java.util.List;

public class FavList extends ParentBean{
	private List<FavBean> FavProductList;
	private List<PageData> Data;
	public List<FavBean> getFavProductList() {
		return FavProductList;
	}
	public void setFavProductList(List<FavBean> favProductList) {
		FavProductList = favProductList;
	}
	public List<PageData> getData() {
		return Data;
	}
	public void setData(List<PageData> data) {
		Data = data;
	}
	@Override
	public String toString() {
		return "FavList [FavProductList=" + FavProductList + ", Data=" + Data
				+ ", getServerReturn()=" + getServerReturn() + "]";
	}

}
