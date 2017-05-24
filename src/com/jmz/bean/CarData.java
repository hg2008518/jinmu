package com.jmz.bean;

import java.util.ArrayList;

public class CarData extends ParentBean{
	private String Page;
	private String pageSize;
	private String PageTotal;
	public String getPage() {
		return Page;
	}
	public void setPage(String page) {
		Page = page;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getPageTotal() {
		return PageTotal;
	}
	public void setPageTotal(String pageTotal) {
		PageTotal = pageTotal;
	}
	@Override
	public String toString() {
		return "CarData [Page=" + Page + ", pageSize=" + pageSize
				+ ", PageTotal=" + PageTotal + "]";
	}
	
}
