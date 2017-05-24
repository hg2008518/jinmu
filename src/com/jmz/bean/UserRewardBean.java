package com.jmz.bean;

import java.util.List;

public class UserRewardBean extends ParentBean {
	/*IsSpreadProduct是否为推广产品
SpreadTotal 查看您信息的好友
SpreadCheckTotal 链接被浏览总数
SpreadRewardTotal 当前产品获奖用户数

RewardList 奖品列表
SpreadRemainTotal 还剩下多少人*/
	private String IsSpreadProduct;
	private String SpreadTotal;
	private String SpreadCheckTotal;
	private String SpreadRewardTotal;
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

	private List<RewardList> RewardList;
	
	public String getIsSpreadProduct() {
		return IsSpreadProduct;
	}

	public void setIsSpreadProduct(String isSpreadProduct) {
		IsSpreadProduct = isSpreadProduct;
	}

	public String getSpreadTotal() {
		return SpreadTotal;
	}

	public void setSpreadTotal(String spreadTotal) {
		SpreadTotal = spreadTotal;
	}

	public List<RewardList> getRewardList() {
		return RewardList;
	}

	public void setRewardList(List<RewardList> rewardList) {
		RewardList = rewardList;
	}

	@Override
	public String toString() {
		return "UserRewardBean [IsSpreadProduct=" + IsSpreadProduct
				+ ", SpreadTotal=" + SpreadTotal + ", SpreadCheckTotal="
				+ SpreadCheckTotal + ", SpreadRewardTotal=" + SpreadRewardTotal
				+ ", RewardList=" + RewardList + "]";
	}
	
	
}
