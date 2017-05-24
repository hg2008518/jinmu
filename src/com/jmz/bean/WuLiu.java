package com.jmz.bean;

import java.util.List;

public class WuLiu {
	private String status;
	private String message;
	private String errCode;
	private List<WuLiuData> data;
	private String html;
	private String mailNo;
	private String expTextName;
	private String expSpellName;
	private String update;
	private String cache;
	private String ord;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public List<WuLiuData> getData() {
		return data;
	}
	public void setData(List<WuLiuData> data) {
		this.data = data;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getMailNo() {
		return mailNo;
	}
	public void setMailNo(String mailNo) {
		this.mailNo = mailNo;
	}
	public String getExpTextName() {
		return expTextName;
	}
	public void setExpTextName(String expTextName) {
		this.expTextName = expTextName;
	}
	public String getExpSpellName() {
		return expSpellName;
	}
	public void setExpSpellName(String expSpellName) {
		this.expSpellName = expSpellName;
	}
	public String getUpdate() {
		return update;
	}
	public void setUpdate(String update) {
		this.update = update;
	}
	public String getCache() {
		return cache;
	}
	public void setCache(String cache) {
		this.cache = cache;
	}
	public String getOrd() {
		return ord;
	}
	public void setOrd(String ord) {
		this.ord = ord;
	}
	@Override
	public String toString() {
		return "WuLiu [status=" + status + ", message=" + message
				+ ", errCode=" + errCode + ", data=" + data + ", html=" + html
				+ ", mailNo=" + mailNo + ", expTextName=" + expTextName
				+ ", expSpellName=" + expSpellName + ", update=" + update
				+ ", cache=" + cache + ", ord=" + ord + "]";
	}
	
}
