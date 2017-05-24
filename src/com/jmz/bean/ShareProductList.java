package com.jmz.bean;

import java.util.List;

public class ShareProductList extends ParentBean {
	List<ShareProduct> ShareProductReport;
	List<PageData> Data;

	public List<ShareProduct> getShareProductReport() {
		return ShareProductReport;
	}

	public void setShareProductReport(List<ShareProduct> shareProductReport) {
		ShareProductReport = shareProductReport;
	}

	public List<PageData> getData() {
		return Data;
	}

	public void setData(List<PageData> data) {
		Data = data;
	}

	@Override
	public String toString() {
		return "ShareTicketList [ShareProductReport=" + ShareProductReport
				+ ", Data=" + Data + "]";
	}

}
