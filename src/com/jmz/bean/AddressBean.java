package com.jmz.bean;

public class AddressBean extends ParentBean {
	private String UserAddressID;
	/**
	 * 区域
	 */
	private String Region;
	/**
	 * 路段
	 */
	private String Street;
	/**
	 * 邮编
	 */
	private String ZipCode;
	/**
	 * 电话
	 */
	private String Telephone;
	/**
	 * 收货人
	 */
	private String Consignee;
	
	private String IsDefault;

	public String getUserAddressID() {
		return UserAddressID;
	}

	public void setUserAddressID(String userAddressID) {
		UserAddressID = userAddressID;
	}

	/**
	 * 城市
	 * 
	 * @return
	 */
	public String getRegion() {
		return Region;
	}

	public void setRegion(String region) {
		Region = region;
	}

	public String getStreet() {
		return Street;
	}

	public void setStreet(String street) {
		Street = street;
	}

	public String getZipCode() {
		return ZipCode;
	}

	public void setZipCode(String zipCode) {
		ZipCode = zipCode;
	}

	public String getTelephone() {
		return Telephone;
	}

	public void setTelephone(String telephone) {
		Telephone = telephone;
	}

	/**
	 * 收货人
	 * 
	 * @return
	 */
	public String getConsignee() {
		return Consignee;
	}

	public void setConsignee(String consignee) {
		Consignee = consignee;
	}

	public String getIsDefault() {
		return IsDefault;
	}

	public void setIsDefault(String isDefault) {
		IsDefault = isDefault;
	}
	/**
	 * 自定义地址
	 * @return
	 */
	public String getMyExpress(){
		return getRegion()+"  "+getStreet()+"  "+getZipCode()+"\r\n"+getTelephone()+"  "+getConsignee();
	}
	@Override
	public String toString() {
		return "AddressBean [UserAddressID=" + UserAddressID + ", Region="
				+ Region + ", Street=" + Street + ", ZipCode=" + ZipCode
				+ ", Telephone=" + Telephone + ", Consignee=" + Consignee
				+ ", IsDefault=" + IsDefault + "]";
	}
}
