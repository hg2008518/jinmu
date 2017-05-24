package com.jmz.bean;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class Product extends ProductParent {

	public final static int PRODUCT_CURR_STATE_WAIT = 0;
	public final static int PRODUCT_CURR_STATE_FISHED = 1;
	public final static int PRODUCT_CURR_STATE_BUY = 2;
	public final static int PRODUCT_CURR_STATE_NO_STOCK = 3;

	private String ShopID;
	private String ProductName;
	private String Instruction;
	private String Content;
	private String PrintSummary;
	private String ShopPrice;
	private String PrePrice;
	private String MarketPrice;
	private String ProductImageUrls;
	private String Shop_ImageUrl;
	private String OnceMaxNumber;
	private String ProductNumber;
	private String ProductType;
	private String OpenDate;
	private String ServerTime;
	private String CloseDate;
	private String SortOrder;
	private String ClickTotal;
	private String ProductStatus;
	private String IsDelete;
	private String Commission;
	private String CommissionPay;
	private String CouponPay;
	private String OrderTotal;
	private String FarePrice;
	private String ShopFarePriceStr;
	private String referrerUserID;
	private String Coupon;
	
	/**IsSpreadProduct 是否为当前推广的产品,
	   SpreadTotal 查看您信息的好友
	   SpreadCheckTotal 链接被浏览总数
	   SpreadRewardTotal 当前产品获奖用户数*/
	private String IsSpreadProduct;
	private String SpreadCheckTotal;
	private String SpreadRewardTotal;
	private String IsProductReward;
	/**
	 * 选中属性id的文本
	 */
	private String attrText;
	/**
	 * 选中属性id
	 */
	private String attrId;
	private ArrayList<String> ProductImageUrlsList;
	private int allAttrTotalStock;
	/**
	 * 当前购买数量
	 */
	private int currQuantity = 1;
	/**
	 * 0、预售；1、活动结束；2正在销售；3、售罄
	 */
	private int currentState;
	private DecimalFormat format = new DecimalFormat("0.##");

	public String getAttrId() {
		return attrId;
	}

	public void setAttrId(String attrId) {
		this.attrId = attrId;
	}

	public String getAttrText() {
		return attrText;
	}

	public void setAttrText(String attrText) {
		this.attrText = attrText;
	}

	public String getReferrerUserID() {
		return referrerUserID;
	}

	public void setReferrerUserID(String referrerUserID) {
		this.referrerUserID = referrerUserID;
	}

	public String getShopID() {
		return ShopID;
	}

	public void setShopID(String shopID) {
		ShopID = shopID;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getInstruction() {
		return Instruction;
	}

	public void setInstruction(String instruction) {
		Instruction = instruction;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getPrintSummary() {
		return PrintSummary;
	}

	public void setPrintSummary(String printSummary) {
		PrintSummary = printSummary;
	}

	public String getShopPrice() {
		return ShopPrice;
	}

	public void setShopPrice(String shopPrice) {
		ShopPrice = shopPrice;
	}

	public String getPrePrice() {
		return PrePrice;
	}

	public void setPrePrice(String prePrice) {
		PrePrice = prePrice;
	}

	public String getMarketPrice() {
		return MarketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		MarketPrice = marketPrice;
	}

	public String getProductImageUrls() {
		Matcher m = getP().matcher(ProductImageUrls);
		return m.replaceAll("");
	}

	public void setProductImageUrls(String productImageUrls) {
		ProductImageUrls = productImageUrls;
	}

	public String getOnceMaxNumber() {
		return OnceMaxNumber;
	}

	public void setOnceMaxNumber(String onceMaxNumber) {
		OnceMaxNumber = onceMaxNumber;
	}

	public String getProductNumber() {
		return ProductNumber;
	}

	public void setProductNumber(String productNumber) {
		ProductNumber = productNumber;
	}

	public String getOpenDate() {
		return OpenDate;
	}

	public void setOpenDate(String openDate) {
		OpenDate = openDate;
	}

	public String getCloseDate() {
		return CloseDate;
	}

	public void setCloseDate(String closeDate) {
		CloseDate = closeDate;
	}

	public String getSortOrder() {
		return SortOrder;
	}

	public void setSortOrder(String sortOrder) {
		SortOrder = sortOrder;
	}

	public String getClickTotal() {
		return ClickTotal;
	}

	public void setClickTotal(String clickTotal) {
		ClickTotal = clickTotal;
	}

	public String getProductStatus() {
		return ProductStatus;
	}

	public void setProductStatus(String productStatus) {
		ProductStatus = productStatus;
	}

	public String getIsDelete() {
		return IsDelete;
	}

	public void setIsDelete(String isDelete) {
		IsDelete = isDelete;
	}

	public String getCommission() {
		return Commission;
	}

	public void setCommission(String commission) {
		Commission = commission;
	}

	public String getCommissionPay() {
		return CommissionPay;
	}

	public void setCommissionPay(String commissionPay) {
		CommissionPay = commissionPay;
	}

	public String getCouponPay() {
		return CouponPay;
	}

	public void setCouponPay(String couponPay) {
		CouponPay = couponPay;
	}

	public String getOrderTotal() {
		return OrderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		OrderTotal = orderTotal;
	}

	public String getFarePrice() {
		return FarePrice;
	}

	public void setFarePrice(String farePrice) {
		FarePrice = farePrice;
	}
	
	public String getShopFarePriceStr() {
		return ShopFarePriceStr;
	}

	public void setShopFarePriceStr(String shopFarePriceStr) {
		ShopFarePriceStr = shopFarePriceStr;
	}

	public String getShop_ImageUrl() {
		return Shop_ImageUrl;
	}

	public void setShop_ImageUrl(String shop_ImageUrl) {
		Shop_ImageUrl = shop_ImageUrl;
	}

	public String getProductType() {
		return ProductType;
	}

	public void setProductType(String productType) {
		ProductType = productType;
	}

	/**
	 * 
	 * @return 差价
	 */
	public String getBetweenPrice() {
		BigDecimal marketDecimal = new BigDecimal(getMarketPrice());
		BigDecimal shopDecimal = new BigDecimal(getShopPrice());
		return marketDecimal.subtract(shopDecimal).doubleValue() + "";
	}

	/**
	 * 
	 * @return 分享奖金
	 */
	public String getShareMoney() {
		BigDecimal commissionDecimal = new BigDecimal(getCommission());
		BigDecimal decimal = new BigDecimal("" + 0.8);
		if (commissionDecimal.doubleValue() <= 0) {
			return "0";
		}
		// return
		// String.format("%.2f",commissionDecimal.multiply(decimal).doubleValue());
		return format.format(commissionDecimal.multiply(decimal)) + "";
	}

	/**
	 * 
	 * @return 购买奖金
	 */
	public String getBuyMoney() {
		BigDecimal commissionDecimal = new BigDecimal(getCommission());
		BigDecimal decimal = new BigDecimal("" + 0.2);
		if (commissionDecimal.doubleValue() <= 0) {
			return "0";
		}
		// return
		// String.format("%.2f",commissionDecimal.multiply(decimal).doubleValue());
		return format.format(commissionDecimal.multiply(decimal)) + "";
	}

	public String getServerTime() {
		return ServerTime;
	}

	public void setServerTime(String serverTime) {
		ServerTime = serverTime;
	}

	public String getCoupon() {
		return Coupon;
	}

	public void setCoupon(String coupon) {
		Coupon = coupon;
	}

	public int getAllAttrTotalStock() {
		return allAttrTotalStock;
	}

	public void setAllAttrTotalStock(int allAttrTotalStock) {
		this.allAttrTotalStock = allAttrTotalStock;
	}

	public int getCurrentState() {
		return currentState;
	}

	public void setCurrentState(int currentState) {
		this.currentState = currentState;
	}

	public ArrayList<String> getProductImageUrlsList() {
		return ProductImageUrlsList;
	}

	public void setProductImageUrlsList(ArrayList<String> productImageUrlsList) {
		ProductImageUrlsList = productImageUrlsList;
	}
	public int getCurrQuantity() {
		return currQuantity;
	}
	
	
	public void setCurrQuantity(int currQuantity) {
		this.currQuantity = currQuantity;
	}
	
	/**
	 * 当前推广的产品
	 * */
	public String getIsSpreadProduct() {
		return IsSpreadProduct;
	}
	public void setIsSpreadProduct(String isSpreadProduct) {
		IsSpreadProduct = isSpreadProduct;
	}
	
	/**
	 * 
	 * */
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
	public String getIsProductReward(){
		return IsProductReward;
	}
	public void setIsProductReward(String isProductReward){
		IsProductReward = isProductReward;
	}
	
	@Override
	public String toString() {
		return "Product [ShopID=" + ShopID + ", ProductName=" + ProductName
				+ ", Instruction=" + Instruction + ", Content=" + Content
				+ ", PrintSummary=" + PrintSummary + ", ShopPrice=" + ShopPrice
				+ ", PrePrice=" + PrePrice + ", MarketPrice=" + MarketPrice
				+ ", ProductImageUrls=" + ProductImageUrls + ", Shop_ImageUrl="
				+ Shop_ImageUrl + ", OnceMaxNumber=" + OnceMaxNumber
				+ ", ProductNumber=" + ProductNumber + ", ProductType="
				+ ProductType + ", OpenDate=" + OpenDate + ", ServerTime="
				+ ServerTime + ", CloseDate=" + CloseDate + ", SortOrder="
				+ SortOrder + ", ClickTotal=" + ClickTotal + ", ProductStatus="
				+ ProductStatus + ", IsDelete=" + IsDelete + ", Commission="
				+ Commission + ", CommissionPay=" + CommissionPay
				+ ", CouponPay=" + CouponPay + ", OrderTotal=" + OrderTotal
				+ ", FarePrice=" + FarePrice + ", ShopFarePriceStr="
				+ ShopFarePriceStr + ", referrerUserID=" + referrerUserID
				+ ", Coupon=" + Coupon + ", attrText=" + attrText + ", attrId="
				+ attrId + ", ProductImageUrlsList=" + ProductImageUrlsList
				+ ", allAttrTotalStock=" + allAttrTotalStock
				+ ", currQuantity=" + currQuantity + ", currentState="
				+ currentState + ", format=" + format 
				+"IsSpreadProduct=" + IsSpreadProduct
				+ ", SpreadCheckTotal=" + SpreadCheckTotal
				+ ", SpreadRewardTotal=" + SpreadRewardTotal
				+", IsProductReward=" + IsProductReward + "]";
	}


}
