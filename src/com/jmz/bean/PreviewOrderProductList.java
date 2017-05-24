package com.jmz.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class PreviewOrderProductList implements Serializable {
	private ProductPromotionInfo ProductPromotionInfo;

	public ProductPromotionInfo getProductPromotionInfo() {
		return ProductPromotionInfo;
	}

	public void setProductPromotionInfo(ProductPromotionInfo productPromotionInfo) {
		ProductPromotionInfo = productPromotionInfo;
	}

	@Override
	public String toString() {
		return "PreviewOrderProductList [ProductPromotionInfo="
				+ ProductPromotionInfo + "]";
	}


}
