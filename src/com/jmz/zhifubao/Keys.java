/*﻿*
 *
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */
package com.jmz.zhifubao;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	// 合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088411812487035";

	// 收款支付宝账号
	public static final String DEFAULT_SELLER = "jmzjdz123@sina.com";

	// 商户私钥，自助生成
	public static final String PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALvsm4o6tpM6ZfB6MQmUrtG7JUJPu0/h3EzDRp1kFhRr7SwKBJIXis8TsEq8Y/EaxFG7TFDkgnAbMnegHRgo/eeivfis+WJHeCRFGJpwR/2m7uck9mvQ/nFRrLCNrz4zWjnMvERCIhedVQ5bpoNSk5CdtMe1tPOg7ZFz6P/6lVAbAgMBAAECgYBqrkMOEY6bvcco4PT9j14+yZK5NjILgxhgq2pm5u5x4WLeYUF+/cZbD75Abvn9YUiZbU5BjbdOlOHtbwOVN5AK+TbkZuySh6fFbQG3N5UvTeTOqBW9czZrXhuxXeoLjA8Ao0x13LX09zI70UAT7l/xzBQGo1cFkmgafp1maTnoIQJBAO7BhATeshXJg8UFIvnTm1LPH4doNKjJp6b311TeEaYNHj/v0GQjBdYRsPu7iQdhIOWxkkBuU2chPz6a1Rqvit0CQQDJfzz9pFNMnLqdej1LtQuidNo3Ke9sVCy/0uF30smy9sKhw6ob2Adr46SxVZjlsIzBoiQKMkeIq4+7pnwUYitXAkEAlEe/ND8SnxFeMvhztnDDYHSegjwnqERFSR9Xrq+UimpPRSotZGOT9lVac50PB3Q9OCpGSJm8VqqBWuJSQ5aOSQJAWGJak3ugGkvpQ6hZ+LpqZxFQor/fSKpHksDiq5enZ7v7tc9I77u7zaRmGm9MiPv/UfRYUKyrWhS/EHW/mYVSMwJAdkQAV+8JcHB/ol/3M87HkzD/MfKe0069ft9XsZIiO+3y5DR0OEAUo1asAvX+RoEYcLZdn1pLX0WhPgYNxUV+Xg==";

	// 支付宝公钥
	public static final String PUBLIC = "-----BEGIN PUBLIC KEY-----MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC77JuKOraTOmXwejEJlK7RuyVCT7tP4dxMw0adZBYUa+0sCgSSF4rPE7BKvGPxGsRRu0xQ5IJwGzJ3oB0YKP3nor34rPliR3gkRRiacEf9pu7nJPZr0P5xUaywja8+M1o5zLxEQiIXnVUOW6aDUpOQnbTHtbTzoO2Rc+j/+pVQGwIDAQAB-----END PUBLIC KEY-----";
	

}
