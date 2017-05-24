package com.jmz.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户
 * 
 * @author Administrator
 * 
 */
public class DBUser implements Serializable {
	private int _id;
	private String userId;
	private int isLogin;
	private int isFirstEnterApp;
	private int isRemeberPassword;
	private float useMoney;
	private float unUseMoney;
	private float totalMoney;
	private String userName;
	private String cityName;
	private String cityId;
	private String passWord;
	private String passWordString;
	private int isContract;
	private String contractDate;
	
	public DBUser(){
	}
	
	public DBUser(int _id, String userId, int isLogin, int isFirstEnterApp,
			int isRemeberPassword, float useMoney, float unUseMoney,
			float totalMoney, String userName, String cityName, String cityId,
			String passWord, String passWordString, int isContract, String contractDate) {
		this._id = _id;
		this.userId = userId;
		this.isLogin = isLogin;
		this.isFirstEnterApp = isFirstEnterApp;
		this.isRemeberPassword = isRemeberPassword;
		this.useMoney = useMoney;
		this.unUseMoney = unUseMoney;
		this.totalMoney = totalMoney;
		this.userName = userName;
		this.cityName = cityName;
		this.cityId = cityId;
		this.passWord = passWord;
		this.passWordString = passWordString;
		this.isContract = isContract;
		this.contractDate = contractDate;
	}

	public DBUser(String userId, int isLogin, int isFirstEnterApp,
			int isRemeberPassword, float useMoney, float unUseMoney,String userName, String cityName, String cityId,
			String passWord, String passWordString, int isContract, String contractDate) {
		this.userId = userId;
		this.isLogin = isLogin;
		this.isFirstEnterApp = isFirstEnterApp;
		this.isRemeberPassword = isRemeberPassword;
		this.useMoney = useMoney;
		this.unUseMoney = unUseMoney;
		this.userName = userName;
		this.cityName = cityName;
		this.cityId = cityId;
		this.passWord = passWord;
		this.passWordString = passWordString;
		this.isContract = isContract;
		this.contractDate = contractDate;
		totalMoney = Float.parseFloat(new BigDecimal(useMoney+"").add(new BigDecimal(this.unUseMoney))+"");
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(int isLogin) {
		this.isLogin = isLogin;
	}
	public float getUseMoney() {
		return useMoney;
	}
	public void setUseMoney(float useMoney) {
		this.useMoney = useMoney;
	}
	public float getUnUseMoney() {
		return unUseMoney;
	}
	public void setUnUseMoney(float unUseMoney) {
		this.unUseMoney = unUseMoney;
	}
	public float getTotolMoney() {
		return Float.parseFloat(new BigDecimal(useMoney+"").add(new BigDecimal(this.unUseMoney))+"");
	}
	public void setTotolMoney(float totolMoney) {
		this.totalMoney = totolMoney;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getPassWordString() {
		return passWordString;
	}
	public void setPassWordString(String passWordString) {
		this.passWordString = passWordString;
	}
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getIsRemeberPassword() {
		return isRemeberPassword;
	}

	public void setIsRemeberPassword(int isRemeberPassword) {
		this.isRemeberPassword = isRemeberPassword;
	}
	
	public int getIsContract() {
		return isContract;
	}

	public void setIsContract(int isContract) {
		this.isContract = isContract;
	}

	public String getContractDate() {
		return contractDate;
	}

	public void setContractDate(String contractDate) {
		this.contractDate = contractDate;
	}

	@Override
	public String toString() {
		return "DBUser [_id=" + _id + ", userId=" + userId + ", isLogin="
				+ isLogin + ", isFirstEnterApp=" + isFirstEnterApp
				+ ", isRemeberPassword=" + isRemeberPassword + ", useMoney="
				+ useMoney + ", unUseMoney=" + unUseMoney + ", totalMoney="
				+ totalMoney + ", userName=" + userName + ", cityName="
				+ cityName + ", cityId=" + cityId + ", passWord=" + passWord
				+ ", passWordString=" + passWordString + ", isContract="
				+ isContract + ", contractDate=" + contractDate + "]";
	}
	public int getIsFirstEnterApp() {
		return isFirstEnterApp;
	}
	public void setIsFirstEnterApp(int isFirstEnterApp) {
		this.isFirstEnterApp = isFirstEnterApp;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

}
