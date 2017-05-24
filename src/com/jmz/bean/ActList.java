package com.jmz.bean;

import java.util.ArrayList;
import java.util.List;

public class ActList extends ParentBean {
	private ArrayList<ActBean> ActivityList1;
	private ArrayList<ActBean> ActivityList2;
	private ArrayList<ActBean> ActivityList3;
	private ArrayList<ActBean> ActivityList4;
	private ArrayList<ActBean> ActivityList5;

	public ArrayList<ActBean> getActivityList1() {
		return ActivityList1;
	}

	public void setActivityList1(ArrayList<ActBean> activityList1) {
		ActivityList1 = activityList1;
	}

	public ArrayList<ActBean> getActivityList2() {
		return ActivityList2;
	}

	public void setActivityList2(ArrayList<ActBean> activityList2) {
		ActivityList2 = activityList2;
	}

	public ArrayList<ActBean> getActivityList3() {
		return ActivityList3;
	}

	public void setActivityList3(ArrayList<ActBean> activityList3) {
		ActivityList3 = activityList3;
	}

	public ArrayList<ActBean> getActivityList4() {
		return ActivityList4;
	}

	public void setActivityList4(ArrayList<ActBean> activityList4) {
		ActivityList4 = activityList4;
	}


	public ArrayList<ActBean> getActivityList5() {
		return ActivityList5;
	}

	public void setActivityList5(ArrayList<ActBean> activityList5) {
		ActivityList5 = activityList5;
	}
	
	@Override
	public String toString() {
		return "ActList [ActivityList1=" + ActivityList1 + ", ActivityList2="
				+ ActivityList2 + ", ActivityList3=" + ActivityList3
				+ ", ActivityList4=" + ActivityList4 + ", ActivityList5="
				+ ActivityList5 + "]";
	}
}
