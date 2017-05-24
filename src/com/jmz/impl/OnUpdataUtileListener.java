package com.jmz.impl;



public interface OnUpdataUtileListener {
	/**
	 * 提交成功
	 * @param result True申请退款，False确认收货
	 * @param orderId 订单号
	 */
	void onUpdataUtileSuccess(boolean result,String orderId);

}
