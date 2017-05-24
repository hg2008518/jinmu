package com.jmz.bean;

public class QmfOrder extends ParentBean {
	private String TransId;
	private String ChrCode;
	private String Signature;
	private String MerId;
	public String getTransId() {
		return TransId;
	}
	public void setTransId(String transId) {
		TransId = transId;
	}
	public String getChrCode() {
		return ChrCode;
	}
	public void setChrCode(String chrCode) {
		ChrCode = chrCode;
	}
	public String getSignature() {
		return Signature;
	}
	public void setSignature(String signature) {
		Signature = signature;
	}
	public String getMerId() {
		return MerId;
	}
	public void setMerId(String merId) {
		MerId = merId;
	}
	@Override
	public String toString() {
		return "QmfOrder [TransId=" + TransId + ", ChrCode=" + ChrCode
				+ ", Signature=" + Signature + ", MerId=" + MerId
				+ ", getServerReturn()=" + getServerReturn() + "]";
	}
}
