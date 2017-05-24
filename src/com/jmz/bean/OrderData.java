package com.jmz.bean;

public class OrderData {
	private String Page;
	private String PageTotal;
	@Override
	public String toString() {
		return "Data [Page=" + Page + ", PageTotal=" + PageTotal + "]";
	}
	public String getPage() {
		return Page;
	}
	public void setPage(String page) {
		Page = page;
	}
	public String getPageTotal() {
		return PageTotal;
	}
	public void setPageTotal(String pageTotal) {
		PageTotal = pageTotal;
	}
}
