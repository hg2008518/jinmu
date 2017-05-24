package com.jmz.bean;

import java.io.Serializable;

public class AwardList implements Serializable{
	private String AwardExpire;
	private String AwardActionURL;
	private String CMCCExpire;
	public String getAwardExpire() {
		return AwardExpire;
	}
	public void setAwardExpire(String awardExpire) {
		AwardExpire = awardExpire;
	}
	public String getAwardActionURL() {
		return AwardActionURL;
	}
	public void setAwardActionURL(String awardActionURL) {
		AwardActionURL = awardActionURL;
	}
	public String getCMCCExpire() {
		return CMCCExpire;
	}
	public void setCMCCExpire(String cMCCExpire) {
		CMCCExpire = cMCCExpire;
	}
	@Override
	public String toString() {
		return "AwardList [AwardExpire=" + AwardExpire + ", AwardActionURL="
				+ AwardActionURL + ", CMCCExpire=" + CMCCExpire + "]";
	}
	
	
}
