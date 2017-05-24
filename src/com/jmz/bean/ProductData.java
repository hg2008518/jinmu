package com.jmz.bean;

public class ProductData extends ParentBean{
	private String PageTotal;
	private String Page;
	
	public String getPageTotal() {
		return PageTotal;
	}
	public void setPageTotal(String pageTotal) {
		PageTotal = pageTotal;
	}
	public String getPage() {
		return Page;
	}
	public void setPage(String page) {
		Page = page;
	}
	@Override
	public String toString() {
		return "Data [PageTotal=" + PageTotal + ", Page=" + Page + "]";
	}
	
}
