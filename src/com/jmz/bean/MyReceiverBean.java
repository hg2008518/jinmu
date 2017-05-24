package com.jmz.bean;

public class MyReceiverBean {
	/**
	 * nodeID
	 */
	private String a;
	/**
	 * 
	 * propertyTypeID
	 */
	private String b;
	/**
	 * title 标题
	 */
	private String c;
	/**
	 * propertyID 
	 */
	private String d;
	/**
	 * productID 产品ID
	 */
	private String e;
	/**
	 * url 连接
	 */
	private String f;
	public String getE() {
		return e;
	}
	public void setE(String e) {
		this.e = e;
	}
	public String getF() {
		return f;
	}
	public void setF(String f) {
		this.f = f;
	}
	public String getD() {
		return d;
	}
	public void setD(String d) {
		this.d = d;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		this.b = b;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	@Override
	public String toString() {
		return "MyReceiverBean [e=" + e + ", f=" + f + ", d=" + d + ", b=" + b
				+ ", a=" + a + ", c=" + c + "]";
	}

}
