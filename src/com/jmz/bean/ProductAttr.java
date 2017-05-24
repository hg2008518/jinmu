package com.jmz.bean;

public class ProductAttr {
	private String attrId;
	private String attrName;
	private String attrImg;
	private boolean isCheck = false;
	private boolean isEnable = true;

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public String getAttrId() {
		return attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrImg() {
		return attrImg;
	}

	public void setAttrImg(String attrImg) {
		this.attrImg = attrImg;
	}

	public boolean isCheck() {
		if (isEnable) {
			return isCheck;
		}
		isCheck = false;
		return false;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	@Override
	public String toString() {
		return "ProductAttr [attrId=" + attrId + ", attrName=" + attrName
				+ ", attrImg=" + attrImg + ", isSelect=" + isCheck
				+ ", isEnable=" + isEnable + "]";
	}
}
