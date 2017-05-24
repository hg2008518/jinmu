package com.jmz.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

public class CarShopList extends ParentBean {
	private ArrayList<CarShop> ShoppingCartList;
	private ArrayList<CarData> Data;
	private boolean isCheck;

	public ArrayList<CarShop> getShoppingCartList() {
		return ShoppingCartList;
	}
	/**
	 * 返回选中的购物车列表
	 * @return
	 */
	public ArrayList<CarShop> getCheckShoppingCartList() {
		Iterator<CarShop> iterator = getShoppingCartList().iterator();
		while (iterator.hasNext()) {
			if(iterator.next().getCheckProductList().size() <= 0){
				iterator.remove();
			}
		}
		return getShoppingCartList();
	}
	public void setShoppingCartList(ArrayList<CarShop> shoppingCartList) {
		ShoppingCartList = shoppingCartList;
	}

	public ArrayList<CarData> getData() {
		return Data;
	}

	public void setData(ArrayList<CarData> data) {
		Data = data;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
		for (CarShop shop : ShoppingCartList) {
			shop.setCheck(isCheck);
			shop.selectAllProduct(isCheck);
		}
	}

	/**
	 * 判断是否全选
	 * 
	 * @return
	 */
	public boolean isAllSelect() {
		for (CarShop shop : ShoppingCartList) {
			if (!shop.isCheck()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断所有选中商品的总金额，不含运费
	 */
	public BigDecimal allSelectMoney() {
		BigDecimal bigDecimal = new BigDecimal("0");
		for (CarShop shop : ShoppingCartList) {
			for (CarProduct product : shop.getProductList()) {
				if (product.isCheck()) {
					bigDecimal = bigDecimal.add(new BigDecimal(product
							.getPrice()).multiply(new BigDecimal(product.getQuantity())));
				}
			}

		}
		return bigDecimal;
	}

	/**
	 * 判断所有选中商品的总金额，含运费
	 */
	public BigDecimal allSelectMoneyIncludeFare() {
		BigDecimal bigDecimal = new BigDecimal("0");
		for (CarShop shop : ShoppingCartList) {
			for (CarProduct product : shop.getProductList()) {
				if (product.isCheck()) {
					bigDecimal = bigDecimal.add(new BigDecimal(product
							.getPrice()));
				}
			}

		}
		return bigDecimal;
	}

	/**
	 * 判断所有选中商品的个数
	 */
	public int getSelectNumber() {
		int a = 0;
		for (CarShop shop : ShoppingCartList) {
			for (CarProduct product : shop.getProductList()) {
				if (product.isCheck()) {
					a++;
				}
			}
		}
		return a;
	}

	/**
	 * 返回所有选中商品的参数的拼接
	 * 
	 * @param which
	 *            0(productId)、1(QUANTITYS)、2(REFERRERUSERID)、3(POSTSCRIPT--格式:
	 *            %ShopID_32%留言test1%-Break-%%ShopID_33%留言%-Break-%%ShopID_34%
	 *            留言test3)、4(ATTRS)、5(ExpressType)、6(ProductName)、7(ShoppingCartID)
	 * 			  
	 */
	public String getAllSelectArg(int which) {
		StringBuffer buffer = new StringBuffer();
		for (CarShop shop : ShoppingCartList) {
			for (CarProduct product : shop.getProductList()) {
				if (product.isCheck()) {
					if (which == 0) {
						buffer.append(product.getProductID() + ",");
					} else if (which == 1) {
						buffer.append(product.getQuantity() + ",");
					} else if (which == 2) {
						buffer.append(product.getReferrerUserID() + ",");
					} else if (which == 3) {
						buffer.append("%ShopID_" + shop.getShopID() + "%"
								+ shop.getPostMsg() + "%-Break-%");
					} else if (which == 4) {
						buffer.append(product.getAttrExtendID() + "|");
					} else if (which == 5) {
						buffer.append("ShopID_" + shop.getShopID() + "%"
								+ shop.getExpressType() + "|");
					}else if (which == 6) {
						buffer.append(product.getProductName()+",");
					}else if (which == 7) {
						buffer.append(product.getShoppingCartID()+",");
					}
					
				}
			}
		}
		if (which == 3) {
			return buffer.substring(0, buffer.length() - 9);
		} else {
			return buffer.substring(0, buffer.length() - 1);
		}

	}
	
	@Override
	public String toString() {
		return "CarShopList [ShoppingCartList=" + ShoppingCartList + ", Data="
				+ Data + ", isCheck=" + isCheck + "]";
	}

}
