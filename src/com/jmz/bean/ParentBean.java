package com.jmz.bean;

import java.io.Serializable;

public class ParentBean implements Serializable{
	private String ServerReturn;

	public String getServerReturn() {
		return ServerReturn;
	}

	public void setServerReturn(String serverReturn) {
		ServerReturn = serverReturn;
	}

	@Override
	public String toString() {
		return "ParentBean [ServerReturn=" + ServerReturn + "]";
	}
}
