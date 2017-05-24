package com.jmz.bean;

public class LoginBean extends ParentBean  {
	
	/**
	 * result:{ "ServerReturn": "StatusSuccess", "PasswordString": "18BA5D25", "UserID": "378", "UserName":"15800001145", "IsContract":"True", "ContractDate":"2015-01-01 15:11" }
	 */
	private static final long serialVersionUID = 1L;
	private String PasswordString;
	private String UserID;
	private String UserName;
	private String IsContract;
	private String ContractDate;

	public String getPasswordString() {
		return PasswordString;
	}

	public void setPasswordString(String PasswordString) {
		this.PasswordString = PasswordString;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String UserID) {
		this.UserID = UserID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getIsContract() {
		return IsContract;
	}

	public void setIsContract(String isContract) {
		IsContract = isContract;
	}

	public String getContractDate() {
		return ContractDate;
	}

	public void setContractDate(String contractDate) {
		ContractDate = contractDate;
	}

	@Override
	public String toString() {
		return "LoginBean [PasswordString=" + PasswordString + ", UserID="
				+ UserID + ", UserName=" + UserName + ", IsContract="
				+ IsContract + ", ContractDate=" + ContractDate
				+ ", getServerReturn()=" + getServerReturn() + "]";
	}
}
