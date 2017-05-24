package com.jmz.bean;

public class OnlyMoney extends ParentBean {
	private String IncreaseSum = "0";
	private String LockSum = "0";
	private String LessSum = "0";
	private String Available = "0";
	private String BoGoTotal = "0";
	private String BoGoAvailableSum = "0";
	private String BoGoNotAvailableSum = "0";
	private String ContractTotal = "0";
	private String ContractAvailableSum = "0";
	private String ContractNotAvailableSum = "0";

	public String getIncreaseSum() {
		return IncreaseSum;
	}

	public void setIncreaseSum(String increaseSum) {
		IncreaseSum = increaseSum;
	}

	public String getLockSum() {
		return LockSum;
	}

	public void setLockSum(String lockSum) {
		LockSum = lockSum;
	}

	public String getLessSum() {
		return LessSum;
	}

	public void setLessSum(String lessSum) {
		LessSum = lessSum;
	}

	public String getAvailable() {
		return Available;
	}

	public void setAvailable(String available) {
		Available = available;
	}
	
	public String getBoGoTotal() {
		return BoGoTotal;
	}

	public void setBoGoTotal(String boGoTotal) {
		BoGoTotal = boGoTotal;
	}

	public String getBoGoAvailableSum() {
		return BoGoAvailableSum;
	}

	public void setBoGoAvailableSum(String boGoAvailableSum) {
		BoGoAvailableSum = boGoAvailableSum;
	}

	public String getBoGoNotAvailableSum() {
		return BoGoNotAvailableSum;
	}

	public void setBoGoNotAvailableSum(String boGoNotAvailableSum) {
		BoGoNotAvailableSum = boGoNotAvailableSum;
	}

	public String getContractTotal() {
		return ContractTotal;
	}

	public void setContractTotal(String contractTotal) {
		ContractTotal = contractTotal;
	}

	public String getContractAvailableSum() {
		return ContractAvailableSum;
	}

	public void setContractAvailableSum(String contractAvailableSum) {
		ContractAvailableSum = contractAvailableSum;
	}

	public String getContractNotAvailableSum() {
		return ContractNotAvailableSum;
	}

	public void setContractNotAvailableSum(String contractNotAvailableSum) {
		ContractNotAvailableSum = contractNotAvailableSum;
	}

	@Override
	public String toString() {
		return "OnlyMoney [IncreaseSum=" + IncreaseSum + ", LockSum=" + LockSum
				+ ", LessSum=" + LessSum + ", Available=" + Available
				+ ", BoGoTotal=" + BoGoTotal + ", BoGoAvailableSum="
				+ BoGoAvailableSum + ", BoGoNotAvailableSum="
				+ BoGoNotAvailableSum + ", ContractTotal=" + ContractTotal
				+ ", ContractAvailableSum=" + ContractAvailableSum
				+ ", ContractNotAvailableSum=" + ContractNotAvailableSum
				+ ", getServerReturn()=" + getServerReturn() + "]";
	}

}
