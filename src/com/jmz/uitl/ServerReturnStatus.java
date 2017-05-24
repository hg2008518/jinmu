package com.jmz.uitl;

import java.util.HashMap;
import java.util.Map;

public class ServerReturnStatus {
	private static Map<String, String> returnMap;
	static {
		returnMap = new HashMap<String, String>();
		returnMap.put("ArgsError", "提交失败，请稍后重试！");
		returnMap.put("TokenError", "请重新登录");
		returnMap.put("IsEnableFalse", "用户已被禁用");
		returnMap.put("IsDeleteTrue", "用户已被删除");
		returnMap.put("UserNameOrPasswordError", "用户或密码错误");
		returnMap.put("ProductNotExist", "商品不存在");
		returnMap.put("UserNotExist", "用户不存在");
		returnMap.put("StatusError", "系统错误，请稍后重试");
		returnMap.put("TradeNotExist", "交易不存在");
		returnMap.put("SingDateError", "系统错误，稍后重试");
		returnMap.put("ServerNoResponse", "服务器没响应");
		returnMap.put("VerifyDataError", "银联验证错误");
		returnMap.put("TradeIDorTransIdorTransCodeError", "缺少必要数据");
		returnMap.put("TransIDorTransCodeNotEmpty", "数据已提交");
		returnMap.put("SoMany", "验证码发送次数太多");
		returnMap.put("SoFast", "验证码发送间隔时间太短");
		returnMap.put("OrderNotExist", "订单不存在");
		returnMap.put("PropertyNotExist", "分类不存在");
		returnMap.put("CommentError", "评论失败或已评论");
		returnMap.put("OrderStatusError", "该订单不支持此功能");
		returnMap.put("ShopNotExist", "店铺不存在");
		returnMap.put("ProductIDAndQuantitysError", "产品和数量不对应");
		returnMap.put("SoldOut", "该商品已售完");
		returnMap.put("OnceMax", "已达到个人的最大购买量");
		returnMap.put("UserAddressNotExist", "用户地址不存在");
		returnMap.put("CommissionNotEnough", "奖金余额不足");
		returnMap.put("Error", "验证错误");
		returnMap.put("FavProductExist", "已收藏过此产品");
		returnMap.put("ValidCodeError", "验证码错误");
		returnMap.put("PhoneNumberError", "用户名不是手机号");
		returnMap.put("ValidCodeNotExist", "验证码不存在，请重新获取");
		returnMap.put("ErrorCountMax", "验证码错误次数超过5次");
		returnMap.put("PasswordLengthError", "密码长度错误，至少需要6位数");
		returnMap.put("ReferrerIDNotExist", "上一级用户不存在");
		returnMap.put("UserExist", "该用户已经注册成功");
		returnMap.put("BuildFormHtmlError", "系统错误，稍后重试");
		returnMap.put("CityNotExist", "城市不存在");
		returnMap.put("FavShopExist", "已收藏过此店铺");
		returnMap.put("IsQmfAccountFalse", "您还不是全民付用户");
		returnMap.put("NotSuccessRetCode", "系统错误，稍后重试");
		returnMap.put("StatusError", "系统错误，稍后重试");
		returnMap.put("ServerNoResponse", "系统错误，稍后重试");
		returnMap.put("VerifyDataError", "系统错误，稍后重试");
		returnMap.put("CommissionLessError", "您输入的金额大于可提现金额");
		returnMap.put("NotSuccessRetCode", "提现失败");
		returnMap.put("UnknownRetCode", "提现失败");
		returnMap.put("OrderStatusUpdateError", "订单状态错误");
		returnMap.put("ValidError", "优惠券不正确");
		returnMap.put("NotAllowBuy", "特殊产品不允许购买");
		returnMap.put("QuantityError", "每次只能购买一个优惠品");
		returnMap.put("ProductExist", "产品已经存在");
		returnMap.put("ParticularProduct", "特殊产品不能加入");
		returnMap.put("WeixinPayOnly", "提交失败，请稍后重试");
		returnMap.put("BindError", "绑定失败");
		returnMap.put("OAuthInfoError", "验证认证信息失败");
		returnMap.put("ContracExpire", "不是合约用户，可能合约已经过期");
	}

	public static String checkReturn(String str) {
		if (returnMap.containsKey(str)) {
			return returnMap.get(str);
		}
		return "系统错误";
	}

}
