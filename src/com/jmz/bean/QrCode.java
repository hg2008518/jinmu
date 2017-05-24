package com.jmz.bean;

public class QrCode extends ParentBean{
	private String QrCodeID;
	private String ValidCode;
	public String getQrCodeID() {
		return QrCodeID;
	}
	public void setQrCodeID(String qrCodeID) {
		QrCodeID = qrCodeID;
	}
	public String getValidCode() {
		return ValidCode;
	}
	public void setValidCode(String validCode) {
		ValidCode = validCode;
	}
	@Override
	public String toString() {
		return "QrCode [QrCodeID=" + QrCodeID + ", ValidCode=" + ValidCode
				+ ", getServerReturn()=" + getServerReturn() + "]";
	}
	
	
}
