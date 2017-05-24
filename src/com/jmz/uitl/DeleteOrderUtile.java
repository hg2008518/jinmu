package com.jmz.uitl;

import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.jmz.MyOrderActivity;
import com.jmz.ParentActivity;
import com.jmz.R;
import com.jmz.bean.DBUser;
import com.jmz.bean.MyOrderShop;
import com.jmz.bean.ParentBean;
import com.jmz.db.UserService;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnDeleteUtileListener;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
/**
 * 删除订单
 * @author Administrator
 *
 */
public class DeleteOrderUtile {
	private Context context;
	private HashMap<String, String> deleteOrderMap;
	private SubmitTask deleteOrderTask;
	private ParentActivity activity;
	private HashMap<String, String> closeOrderMap;
	private SubmitTask closeOrderTask;
	private MyOrderShop myOrderShop;
	private OnDeleteUtileListener listener;

	public DeleteOrderUtile(Context context,MyOrderShop myOrderShop,OnDeleteUtileListener listener) {
		this.context = context;
		this.activity = (ParentActivity)context;
		this.myOrderShop = myOrderShop;
		this.listener = listener;
		
	}
	/**
	 * 销毁task
	 */
	public void destroy(){
		if (deleteOrderTask != null) {
			deleteOrderTask.destorySelf();
		}
		if (closeOrderTask != null
				) {
			closeOrderTask.destorySelf();
		}
	}
	/**
	 * 启动删除
	 */
	public void delete(){
		if(Utile.isEqual(myOrderShop.getOrderStatus(),OrderState.WaitBuyerPay.name())){
			closeSubmit();
		}else{
			deleteSubmit();
		}
		
	}
	/**
	 * 先关闭订单
	 */
	private void closeSubmit() {
		closeOrderMap = new HashMap<String, String>();
		closeOrderMap.put(Config.TAG_ORDERID, myOrderShop.getOrderID());
		closeOrderMap.put(Config.TAG_ORDERSTATUS, OrderState.TradeClosedBySystem.name());
		closeOrderMap.put(Config.TAG_TYPE, "User");
		closeOrderTask = new SubmitTask(context, Config.UPDATE_ORDERSTATUS, 
				ParentBean.class, new OnCloseOrderSubmitListenerImpl(), true);
		closeOrderTask.execute(closeOrderMap);
	}
	/**
	 * 提交删除订单
	 */
	private void deleteSubmit() {
		deleteOrderMap = new HashMap<String, String>();
		deleteOrderMap.put(Config.TAG_ORDERID, myOrderShop.getOrderID());
		deleteOrderTask = new SubmitTask(context, Config.DELETE_ORDER,
				ParentBean.class,
				new OnDeleteOrderSubmitListenerImpl(), true);
		deleteOrderTask.execute(deleteOrderMap);
	}
	/**
	 * 关闭订单
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnCloseOrderSubmitListenerImpl implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ParentBean parentBean = (ParentBean) result.getObject();
			if (parentBean != null) {
				if (Utile.isEqual(parentBean.getServerReturn(),
						Config.STATUSSUCCESS)) {
					deleteSubmit();
				} else {
					activity.showToast(ServerReturnStatus.checkReturn(parentBean
							.getServerReturn()));
				}
			}
		}
	}
	/**
	 * 响应删除订单
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnDeleteOrderSubmitListenerImpl implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ParentBean parentBean = (ParentBean) result.getObject();
			if (parentBean != null) {
				if (Utile.isEqual(parentBean.getServerReturn(),
						Config.STATUSSUCCESS)) {
					activity.showToast(R.string.myorder_msg_deletesuccess);
					listener.onDeleteUtileSuccess(myOrderShop);
				} else {
					activity.showToast(ServerReturnStatus.checkReturn(parentBean
							.getServerReturn()));
				}
			}
		}
	}
}
