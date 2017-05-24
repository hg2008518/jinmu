package com.jmz.bean;

import java.io.Serializable;

public class Child implements Serializable {
	/**
	 * 相当于PropertyTypeID，用于普通分类和商圈分类的下级目录，因为后台名称混乱
	 */
	private String childId;
	/**
	 * 只用于商圈分类
	 */
	private String nodeId;
	/**
	 * 用于判断是否是下级分类, value为"2"时,有下级分类
	 */
	private String propertyType = "1";

	private String childNa;

	public Child() {
	}

	/**
	 * 
	 * @param nodeId
	 *            商圈分类
	 * @param childId
	 *            普通分类，可做为商圈分类的子分类 (当商圈nodeId小于等于0时，配合propertyType等于1一起用)
	 * @param childNa
	 *            名字
	 * @param propertyType
	 *            标识  (1、普通分类、2是商圈、3是主题)
	 */
	public Child(String nodeId, String childId, String childNa,
			String propertyType) {
		this.childId = childId;
		this.nodeId = nodeId;
		this.childNa = childNa;
		this.propertyType = propertyType;
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getChildNa() {
		return childNa;
	}

	public void setChildNa(String childNa) {
		this.childNa = childNa;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	@Override
	public String toString() {
		return "Child [childId=" + childId + ", nodeId=" + nodeId
				+ ", propertyType=" + propertyType + ", childNa=" + childNa
				+ "]";
	}
}
