package com.jmz.bean;

import java.io.Serializable;
import java.util.List;

public class Classify implements Serializable{
	private String groupId;
	private String groupName;
	public List<Child> childs;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<Child> getChilds() {
		return childs;
	}

	public void setChilds(List<Child> childs) {
		this.childs = childs;
	}

	@Override
	public String toString() {
		return "Classify [groupId=" + groupId + ", groupName=" + groupName
				+ ", childs=" + childs + "]";
	}
}
