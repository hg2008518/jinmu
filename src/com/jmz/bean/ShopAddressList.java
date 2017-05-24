package com.jmz.bean;

import java.util.List;

import android.util.Log;

public class ShopAddressList extends ParentBean {
	List<ShopAddressBean> ShopAddressList;

	public List<ShopAddressBean> getShopAddressList() {
		return ShopAddressList;
	}

	public void setShopAddressList(List<ShopAddressBean> shopAddressList) {
		ShopAddressList = shopAddressList;
	}

	@Override
	public String toString() {
		return "ShopAddressList [ShopAddressList="+ShopAddressList+",getServerReturn()="+getServerReturn()+"]";
	} 
	
}
