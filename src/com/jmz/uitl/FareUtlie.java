package com.jmz.uitl;

import java.util.HashMap;

import android.content.Context;

import com.jmz.R;
import com.jmz.bean.FareBean;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;

public class FareUtlie {
	private HashMap<String, String> fareMap;
	public String fare;
	private SubmitTask fareTask;
	private Context context;
	public FareUtlie(Context context,HashMap<String, String> fareMap) {
		this.context = context;
		this.fareMap = fareMap;
	}
	/**
	 * 提交获取运费
	 */
	private void fareSubmit() {
		fareTask = new SubmitTask(context, Config.FARE_PRICE, FareBean.class,
				new onFareSubmitListener(), true);
		fareTask.execute(fareMap);
	}
	
	/**
	 * 响应获取订单号
	 * 
	 * @author Administrator
	 * 
	 */
	private class onFareSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			FareBean fareBean = (FareBean) result.getObject();
			if (fareBean != null) {
				fare = fareBean.getFarePrice();
			} else {
				fare = "5.0";
			}
		}
	}
}
