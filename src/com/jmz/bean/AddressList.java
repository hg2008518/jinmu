package com.jmz.bean;

import java.util.List;

public class AddressList extends ParentBean {
	List<AddressBean> UserAddressList;

	public List<AddressBean> getUserAddressList() {
		return UserAddressList;
	}

	public void setUserAddressList(List<AddressBean> userAddressList) {
		UserAddressList = userAddressList;
	}

	@Override
	public String toString() {
		return "AddressList [UserAddressList=" + UserAddressList + "]";
	} 
}
