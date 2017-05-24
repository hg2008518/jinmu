package com.jmz.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jmz.MyApplication;
import com.jmz.MyOrderActivity;
import com.jmz.ParentActivity;
import com.jmz.R;
import com.jmz.RefundActivity;
import com.jmz.bean.DBUser;
import com.jmz.bean.MyOrderShop;
import com.jmz.db.UserService;
import com.jmz.impl.OnBalancePayListener;
import com.jmz.impl.OnDeleteUtileListener;
import com.jmz.impl.OnUpdataUtileListener;
import com.jmz.uitl.Config;
import com.jmz.uitl.DeleteOrderUtile;
import com.jmz.uitl.OrderState;
import com.jmz.uitl.PayUtile;
import com.jmz.uitl.UpdateOrderStateUtile;
import com.jmz.uitl.Utile;
import com.jmz.view.MyListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * 我的订单商品列表适配器
 * 
 * @author Administrator
 * 
 */
public class MyOrderShopAdapter extends BaseAdapter implements
		OnDeleteUtileListener, OnUpdataUtileListener ,OnBalancePayListener{

	private Context context;
	private Handler handler;
	private List<MyOrderShop> myOrderShops;
	private MyOrderShop myOrderShop;
	private ParentActivity activity;
	private ViewHolder holder;
	private HashMap<Integer, MyOrderProductAdapter> adapterMap;

	public MyOrderShopAdapter(Context context, List<MyOrderShop> myOrderShops,
			Handler handler) {
		this.context = context;
		this.myOrderShops = myOrderShops;
		this.handler = handler;
		activity = (ParentActivity) context;
		adapterMap = new HashMap<Integer, MyOrderProductAdapter>();
	}

	public void setMyOrderShops(List<MyOrderShop> myOrderShops) {
		this.myOrderShops = myOrderShops;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return myOrderShops.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {

		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.adapter_myorder_center_shop, null);
			holder.titleTv = (TextView) view
					.findViewById(R.id.adapter_myorder_shop_title);
			holder.time = (TextView) view
					.findViewById(R.id.adapter_myorder_shop_time);
			holder.deleteBtn = (TextView) view
					.findViewById(R.id.adapter_myorder_shop_delete);
			holder.expTv = (TextView) view
					.findViewById(R.id.adapter_myorder_shop_exp);
			holder.orderStateBtn = (TextView) view
					.findViewById(R.id.adapter_myorder_shop_orderstate);
			holder.refundStateBtn = (TextView) view
					.findViewById(R.id.adapter_myorder_shop_refundstate);
			holder.myListView = (MyListView) view
					.findViewById(R.id.adapter_myorder_shop_list);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		setDataToView(position);
		return view;
	}

	/**
	 * 给控件赋值
	 * 
	 * @param position
	 */
	private void setDataToView(final int position) {
		myOrderShop = myOrderShops.get(position);

		holder.titleTv.setText(myOrderShop.getShopName());
		holder.time.setText("下单时间：" + myOrderShop.getOrderDate());
		MyOrderProductAdapter myOrderProductAdapter = new MyOrderProductAdapter(
				context, myOrderShop.getOrderProductList());
		holder.myListView.setOnScrollListener(new PauseOnScrollListener(
				ImageLoader.getInstance(), false, false));
		holder.myListView.setAdapter(myOrderProductAdapter);
		adapterMap.put(position, myOrderProductAdapter);
		showBtn(myOrderShop);
		holder.deleteBtn.setOnClickListener(new OnAdapterclickImpl(position));
		holder.expTv.setOnClickListener(new OnAdapterclickImpl(position));
		holder.orderStateBtn
				.setOnClickListener(new OnAdapterclickImpl(position));
		holder.refundStateBtn.setOnClickListener(new OnAdapterclickImpl(
				position));
		holder.myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				handler.sendEmptyMessage(position);
			}
		});
	}

	/**
	 * 根据情况显示按钮的不同情况
	 */
	private void showBtn(MyOrderShop mos) {
		// 测试
		// mos.setOrderStatus(Config.WAITBUYERCONFIRMGOODS);
		// mos.setRefundStatus(Config.NONE);

		// 设置是否显示删除按钮
		if (Utile.isEqual(mos.getOrderStatus(),
				OrderState.WaitBuyerConfirmGoods.name())
				|| Utile.isEqual(mos.getOrderStatus(),
						OrderState.TradeFinished.name())
				|| Utile.isEqual(mos.getOrderStatus(),
						OrderState.WaitSellerSendGoods.name())) {
			holder.deleteBtn.setVisibility(View.GONE);
		} else {
			holder.deleteBtn.setVisibility(View.VISIBLE);
		}
		// 设置是否显示退款按钮
		if (Utile.isEqual(mos.getOrderStatus(),
				OrderState.WaitSellerSendGoods.name())
				|| Utile.isEqual(mos.getOrderStatus(),
						OrderState.WaitBuyerConfirmGoods.name())) {
			holder.refundStateBtn.setVisibility(View.VISIBLE);
		} else {
			holder.refundStateBtn.setVisibility(View.GONE);
		}
		if (!Utile.isEqual(mos.getRefundStatus(), OrderState.None.name())) {
			if (Utile.isEqual(mos.getRefundStatus(),
					OrderState.SellerRefuseBuyer.name())) {
				holder.refundStateBtn.setVisibility(View.VISIBLE);
			} else {
				holder.refundStateBtn.setVisibility(View.GONE);
			}
		}
		// 设置在正常按钮显示什么内容
		if (Utile.isEqual(mos.getRefundStatus(), OrderState.None.name())) {
			holder.orderStateBtn.setText(OrderState.valueOf(
					mos.getOrderStatus()).getName());
		} else {
			holder.orderStateBtn.setText(OrderState.valueOf(
					mos.getRefundStatus()).getName());
		}
		// 设置正常状态下按钮不可用
		if (Utile.isEqual(mos.getOrderStatus(),
				OrderState.WaitBuyerConfirmGoods.name())
				|| Utile.isEqual(mos.getOrderStatus(),
						OrderState.WaitBuyerPay.name())
				|| Utile.isEqual(mos.getOrderStatus(),
						OrderState.WaitBuyerConfirmGoods.name())
				|| Utile.isEqual(mos.getRefundStatus(),
						OrderState.WaitBuyerReturnGoods.name())) {
			holder.orderStateBtn.setEnabled(true);
		} else {
			holder.orderStateBtn.setEnabled(false);
		}
	}

	/**
	 * 清除bitmap
	 */
	public void clearBitmap() {
		for (Map.Entry<Integer, MyOrderProductAdapter> entry : adapterMap
				.entrySet()) {
			if (entry.getValue() != null) {
				entry.getValue().destroy();
			}
		}
	}

	protected class ViewHolder {
		protected TextView titleTv, deleteBtn, expTv, orderStateBtn, time,
				refundStateBtn;
		protected MyListView myListView;
	}

	/**
	 * 单击事件
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnAdapterclickImpl implements OnClickListener {
		int position = 0;
		private DeleteOrderUtile deleteOrderUtile;
		private int doWhat = 0;
		private UpdateOrderStateUtile updateOrderStateUtile;
		private PayUtile payUtile;
		private MyOrderShop myOrderShop;

		public OnAdapterclickImpl(int position) {
			this.position = position;
			myOrderShop = myOrderShops.get(position);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.adapter_myorder_shop_delete:
				deleteOrderUtile = new DeleteOrderUtile(activity, myOrderShop,
						MyOrderShopAdapter.this);
				activity.showMyAlertDialog(R.string.myorder_msg_delete, this,View.GONE);
				doWhat = 1;
				break;
			case R.id.adapter_myorder_shop_exp:
				break;
			case R.id.adapter_myorder_shop_orderstate:
				if (Utile.isEqual(myOrderShop.getRefundStatus(),
						OrderState.WaitBuyerReturnGoods.name())) {
					Intent intent = new Intent(activity, RefundActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(MyOrderActivity.TAG_MYORDERSHOP,
							myOrderShop);
					intent.putExtras(bundle);
					activity.startActivityForResult(intent, 3);
				} else if (Utile.isEqual(myOrderShop.getOrderStatus(),
						OrderState.WaitBuyerPay.name())) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(Config.TAG_TITLE, myOrderShop.getTitle());
					map.put(Config.TAG_TRADETYPE, Config.TRADE_TYPE_GOUWU);
					map.put(Config.TAG_ORDERIDS, myOrderShop.getOrderID());
					map.put(Config.TAG_TRADEID, myOrderShop.getTrade_TradeID());
					payUtile = new PayUtile(activity, map,MyOrderShopAdapter.this);
					payUtile.tradeSubmit();
				} else if (Utile.isEqual(myOrderShop.getOrderStatus(),
						OrderState.WaitBuyerConfirmGoods.name())) {
					
					activity.showMyAlertDialog(
							R.string.myorder_msg_confirmgoods, this,View.GONE);
					doWhat = 2;
				}
				break;
			case R.id.adapter_myorder_shop_refundstate:
				activity.showMyAlertDialog(R.string.myorder_msg_refund, this,View.VISIBLE);
				doWhat = 3;
				break;
			case R.id.myalerdialog_sure:
				if (doWhat == 1) {
					deleteOrderUtile.delete();
				} else if (doWhat == 2) {
					updateOrderStateUtile = new UpdateOrderStateUtile(activity,MyOrderShopAdapter.this,
							myOrderShop.getOrderID(),activity.getMsgEditText());
					updateOrderStateUtile.submit(false);
				} else if (doWhat == 3) {
					updateOrderStateUtile = new UpdateOrderStateUtile(activity,MyOrderShopAdapter.this,
							myOrderShop.getOrderID(), activity.getMsgEditText());
					updateOrderStateUtile.submit(true);
				}
				activity.dissMyAlertDialog();
				break;
			case R.id.myalerdialog_cancel:
				activity.dissMyAlertDialog();
				break;
			default:
				break;
			}
		}

	}

	@Override
	public void onDeleteUtileSuccess(MyOrderShop myOrderShop) {
		myOrderShops.remove(myOrderShop);
		setMyOrderShops(myOrderShops);
	}

	@Override
	public void onUpdataUtileSuccess(boolean b,String orderId) {
		for (MyOrderShop mos : myOrderShops) {
			if (Utile.isEqual(orderId, mos.getOrderID())) {
				if(b){
					mos.setRefundStatus(OrderState.WaitSellerAgree.name());
				}else {
					mos.setOrderStatus(OrderState.TradeFinished.name());
				}
			}
		}
		setMyOrderShops(myOrderShops);
	}

	@Override
	public void onBalancePaySuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBalancePayFail() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void WXPaySingeSuccess(PayReq req) {
		// TODO Auto-generated method stub
		activity.sendPayReq(req);
	}

}
