package com.jmz.bean;

public class RefreBean {
	private String Type;
	private String Version;
	private String URL;
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	@Override
	public String toString() {
		return "RefreBean [Type=" + Type + ", Version=" + Version + ", URL="
				+ URL + "]";
	}

}
