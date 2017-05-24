package com.jmz.bean;

import java.util.ArrayList;

import com.jmz.uitl.Logger;
import com.jmz.uitl.Utile;

public class ProductAttrGroupList {
	private ArrayList<ProductAttrGroup> getAttr;


	public ArrayList<ProductAttrGroup> getGetAttr() {
		return getAttr;
	}

	
	public void setGetAttr(ArrayList<ProductAttrGroup> getAttr) {
		this.getAttr = getAttr;
	}

	/**
	 * 获取选好属性的拼接
	 * 
	 * @param isId
	 *            是否是代号或者文字
	 * @return
	 */
	public String getSelectAttr(boolean isId) {
		StringBuffer buffer = new StringBuffer();
		for (ProductAttrGroup attrGroup : getGetAttr()) {
			for (ProductAttr attr : attrGroup.getAttrs()) {
				if (attr.isCheck()) {
					if (isId) {
						buffer.append(attrGroup.getGroupId()+","+attr.getAttrId()+"|");
					} else {
						buffer.append(attrGroup.getGroupName()+":"+attr.getAttrName()+"  ");
					}
				}
			}
		}
		return buffer.length()>1?buffer.substring(0, buffer.length()-1):"";
	}
	/**
	 * 获取选好的属性
	 * 
	 * @return
	 */
	public ProductAttr getSelectAttr() {
		StringBuffer buffer = new StringBuffer();
		for (ProductAttrGroup attrGroup : getGetAttr()) {
			for (ProductAttr attr : attrGroup.getAttrs()) {
				if (attr.isCheck()) {
					return attr;
				}
			}
		}
		return null;
	}
	/**
	 * 判断所有属性是否被选完了
	 * @param isId
	 * @return
	 */
	public boolean isAttrAllSelect() {
		for (ProductAttrGroup attrGroup : getGetAttr()) {
			if(attrGroup.isAttrsEnable()){
				if(attrGroup.isAttrsSelect()){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 设置某些属性不可用
	 */
	public ArrayList<ProductAttrGroup> getProductEnableAttr(ProductAliveSelectAttr aliveSelectAttr){
		for(ProductAttrIsAlive attrIsAlive:aliveSelectAttr.getAttrIsAlive()){
			for(ProductAttrGroup attrGroup:getAttr){
				if(Utile.isEqual(attrGroup.getGroupId(), attrIsAlive.getAttrValue().split(",")[0])){
					for(ProductAttr attr:attrGroup.getAttrs()){
						if(Utile.isEqual(attr.getAttrId(), attrIsAlive.getAttrValue().split(",")[1])){
							if(Utile.isEqual(attrIsAlive.getIsAlive(), "true")){
								attr.setEnable(true);
							}else if(Utile.isEqual(attrIsAlive.getIsAlive(), "false")){
								attr.setEnable(false);
							}
//							Logger.e("hhy", "组：---" + attrGroup.getGroupName()
//									+"属性：---" + attr.getAttrName()
//									+"可用状态：---" + attr.isEnable()
//									+"选择状态：---" + attr.isCheck());
						}
					}
					
				}
			}
		}
		return getAttr;
	}
	
	@Override
	public String toString() {
		return "ProductAttrGroupList [getAttr=" + getAttr + "]";
	}

}
