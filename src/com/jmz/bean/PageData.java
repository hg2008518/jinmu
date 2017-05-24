package com.jmz.bean;

public class PageData extends ParentBean {
	private String Page;
	private String PageTotal;
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
	@Override
	public String toString() {
		return "PageData [Page=" + Page + ", PageTotal=" + PageTotal + "]";
	}
	
}
