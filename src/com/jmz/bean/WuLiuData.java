package com.jmz.bean;

public class WuLiuData {
	private String time;
	private String context;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	@Override
	public String toString() {
		return "WuLiuData [time=" + time + ", context=" + context + "]";
	}

}
