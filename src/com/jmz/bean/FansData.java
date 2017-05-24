package com.jmz.bean;

public class FansData extends ParentBean {
	private String Page;
	private String PageTotal;
	private String FansTotal;
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
	public String getFansTotal() {
		return FansTotal;
	}
	public void setFansTotal(String fansTotal) {
		FansTotal = fansTotal;
	}
	@Override
	public String toString() {
		return "FansData [Page=" + Page + ", PageTotal=" + PageTotal
				+ ", FansTotal=" + FansTotal + "]";
	}
	
}
