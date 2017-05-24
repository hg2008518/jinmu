package com.jmz.impl;

import com.tencent.mm.sdk.modelpay.PayReq;

public interface OnBalancePayListener {
	/**
	 * 提交成功
	 * @param result
	 */

	public void onBalancePaySuccess();
	/**
	 * 提交失败
	 * @param result
	 */
	
	public void onBalancePayFail();
	
	public void WXPaySingeSuccess(PayReq req);
	
}
