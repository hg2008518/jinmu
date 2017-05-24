package com.jmz.bean;

public class FindPassWord extends ParentBean{
	private String PasswordString;

	public String getPasswordString() {
		return PasswordString;
	}

	public void setPasswordString(String passwordString) {
		PasswordString = passwordString;
	}

	@Override
	public String toString() {
		return "FindPassWord [PasswordString=" + PasswordString + "]";
	}
	
}
