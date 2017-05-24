package com.jmz.bean;

public class WXOrder extends ParentBean {
	//{ "trade_type": "trade_type", "prepay_id": "prepay_id", "noncestr": "mod.nonce_str", "trade_id": "modTrade.TradeID", "sign":"signData", "package": "Sign=WXPay"
	//, "appid": "mod.appid", "partnerid": "mod.mch_id", "timestamp": "1438593295" }
	private String appid;
	private String trade_type;
	private String prepay_id;
	private String partnerid;
	private String noncestr;
	private String trade_id;
	private String sign;
	private String Package;
	private String timestamp;
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getPrepay_id() {
		return prepay_id;
	}
	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getTrade_id() {
		return trade_id;
	}
	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getPackage() {
		return Package;
	}
	public void setPackage(String package1) {
		Package = package1;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "WXOrder [appid=" + appid + ", trade_type=" + trade_type
				+ ", prepay_id=" + prepay_id + ", partnerid=" + partnerid
				+ ", noncestr=" + noncestr + ", trade_id=" + trade_id
				+ ", sign=" + sign + ", Package=" + Package + ", timestamp="
				+ timestamp + ", getServerReturn()=" + getServerReturn() + "]";
	}
	
}
