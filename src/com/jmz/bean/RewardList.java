package com.jmz.bean;

public class RewardList extends ParentBean {
	/* ActionUrl = "";
            Amount = "0.90";
            EndTime = "2015/9/30 0:00:00";
            ExpireDate = "2015/10/28 0:00:00";
            IsPublish = True;
            LowerLimit = 10;
            ProductID = 1262;
            RewardName = "9\U6298\U4f18\U60e0";
            RewardNum = 1;
            RewardProductID = 0;
            RewardProductName = "";
            RewardType = Discount;
            RuleName = "\U6ee110\U6b219\U6298";
            SpreadRemainTotal 还剩下多少人
            SpreadProdID = 9;
            SpreadProdRewardID = 2;
            SpreadRuleID = 8;
            StartTime = "2015/8/19 0:00:00";
            Unit = "\U6298";
            UpperLimit = 0;
*/
	private String Amount = "1";
	private String ExpireDate;
	private String StartTime;
	private String RewardType;
	private String SpreadRemainTotal;
	
	public String getSpreadRemainTotal() {
		return SpreadRemainTotal;
	}
	public void setSpreadRemainTotal(String spreadRemainTotal) {
		SpreadRemainTotal = spreadRemainTotal;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	public String getExpireDate() {
		return ExpireDate;
	}
	public void setExpireDate(String expireDate) {
		ExpireDate = expireDate;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	
	public String getRewardType() {
		return RewardType;
	}
	public void setRewardType(String rewardType) {
		RewardType = rewardType;
	}
	@Override
	public String toString() {
		return "RewardList [Amount=" + Amount + ", ExpireDate=" + ExpireDate
				+ ", StartTime=" + StartTime + ", RewardType=" + RewardType
				+ ", SpreadRemainTotal=" + SpreadRemainTotal + "]";
	}
	
}
