package com.jmz.bean;

public class SpreadInfo extends ParentBean {

	/*  // IsSpreadProduct 是否为当前推广的产品,
        SpreadTotal 查看您信息的好友
SpreadCheckTotal 链接被浏览总数
SpreadRewardTotal 当前产品获奖用户数*/
	private String IsSpreadProduct;
	private String SpreadCheckTotal;
	private String SpreadRewardTotal;
	public String getIsSpreadProduct() {
		return IsSpreadProduct;
	}
	public void setIsSpreadProduct(String isSpreadProduct) {
		IsSpreadProduct = isSpreadProduct;
	}
	public String getSpreadCheckTotal() {
		return SpreadCheckTotal;
	}
	public void setSpreadCheckTotal(String spreadCheckTotal) {
		SpreadCheckTotal = spreadCheckTotal;
	}
	public String getSpreadRewardTotal() {
		return SpreadRewardTotal;
	}
	public void setSpreadRewardTotal(String spreadRewardTotal) {
		SpreadRewardTotal = spreadRewardTotal;
	}
	@Override
	public String toString() {
		return "SpreadInfo [IsSpreadProduct=" + IsSpreadProduct
				+ ", SpreadCheckTotal=" + SpreadCheckTotal
				+ ", SpreadRewardTotal=" + SpreadRewardTotal + "]";
	}
	
}
