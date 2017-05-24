package com.jmz.impl;

import com.jmz.http.ApiResponse;

public interface OnSubmitListener {
	/** 提交成功监听 */

	void onSubmitSuccess(ApiResponse<Object> result);


}
