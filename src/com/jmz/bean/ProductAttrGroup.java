package com.jmz.bean;

import java.util.List;

import com.jmz.uitl.Utile;

public class ProductAttrGroup {
	private String groupId;
	private String groupName;
	private List<ProductAttr> attrs;

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

	public List<ProductAttr> getAttrs() {
		for (int i = 0; i < attrs.size() - 1; i++) {
			for (int j = attrs.size() - 1; j > i; j--) {
				if (Utile.isEqual(attrs.get(j).getAttrName(), attrs.get(i)
						.getAttrName())) {
					attrs.remove(j);
				}
			}
		}
		return attrs;
	}

	public void setAttrs(List<ProductAttr> attrs) {

		this.attrs = attrs;
	}

	/**
	 * 设置选中
	 * 
	 * @param position
	 */
	public void setAttrsChildCheck(int position) {
		for (ProductAttr attr : getAttrs()) {
			attr.setCheck(false);
		}
		getAttrs().get(position).setCheck(true);
	}

	/**
	 * 是否全部已选
	 * 
	 * @return
	 */
	public boolean isAttrsSelect() {
		for (ProductAttr attr : getAttrs()) {
			if (attr.isCheck()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否全部是不可用
	 * 
	 * @return
	 */
	public boolean isAttrsEnable() {
		for (ProductAttr attr : getAttrs()) {
			if (attr.isEnable()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "ProductAttrGroup [groupId=" + groupId + ", groupName="
				+ groupName + ", attrs=" + attrs + "]";
	}

}
