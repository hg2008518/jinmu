package com.jmz.bean;

public class Umspay {
	// 处理返回结果----<?xml version="1.0" encoding="UTF-8" ?>
				// <umspay application="PayResult.Rsp" version="1.0.0">
				// <transId>662014080801892366</transId>
				// <merchantId>898310048164027</merchantId>
				// <merchantOrderId>0000938560000628</merchantOrderId>
				// <merchantOrderAmt>6720</merchantOrderAmt>
				// <respCode>2222</respCode>
				// <respDesc>中途退出</respDesc></umspay>
	private String transId;
	private String merchantId;
	private String merchantOrderId;
	private String merchantOrderAmt;
	private String respCode;
	private String respDesc;
	
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}
	public String getMerchantOrderAmt() {
		return merchantOrderAmt;
	}
	public void setMerchantOrderAmt(String merchantOrderAmt) {
		this.merchantOrderAmt = merchantOrderAmt;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}
	@Override
	public String toString() {
		return "Umspay [transId=" + transId + ", merchantId=" + merchantId
				+ ", merchantOrderId=" + merchantOrderId
				+ ", merchantOrderAmt=" + merchantOrderAmt + ", respCode="
				+ respCode + ", respDesc=" + respDesc + "]";
	}
	
}
