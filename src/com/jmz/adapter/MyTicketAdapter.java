package com.jmz.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmz.MyApplication;
import com.jmz.ParentActivity;
import com.jmz.ProductActivity;
import com.jmz.R;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tencent.mm.sdk.modelpay.PayReq;

/**
 * 地址列表适配器
 * 
 * @author Administrator
 * 
 */
public class MyTicketAdapter extends BaseAdapter implements
		OnDeleteUtileListener, OnBalancePayListener {
	// , OnUpdataUtileListener {
	private Context context;
	private List<MyOrderShop> myTicketShops;
	private ViewHolder holder;
	private ParentActivity activity;

	public MyTicketAdapter(Context context, List<MyOrderShop> myOrderShops) {
		this.context = context;
		this.myTicketShops = myOrderShops;
		activity = (ParentActivity) context;
	}

	/**
	 * 重新加载数据源
	 * 
	 * @param myOrderShops
	 */
	public void setMyOrderShops(List<MyOrderShop> myOrderShops) {
		this.myTicketShops = myOrderShops;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return myTicketShops.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.adapter_myticket_center, null);
			holder.title = (TextView) view
					.findViewById(R.id.adapter_myticket_title);
			holder.orderState = (TextView) view
					.findViewById(R.id.adapter_myticket_orderstate);
			holder.refundState = (TextView) view
					.findViewById(R.id.adapter_myticket_refundstate);
			holder.delete = (TextView) view
					.findViewById(R.id.adapter_myticket_delete);
			holder.productImg = (ImageView) view
					.findViewById(R.id.adapter_myticket_img);
			holder.numberPrcie = (TextView) view
					.findViewById(R.id.adapter_myticket_number_price);
			holder.orderId = (TextView) view
					.findViewById(R.id.adapter_myticket_orderid);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		setDataToView(position);
		return view;
	}

	/**
	 * 为组件赋值
	 */
	private void setDataToView(int position) {
		holder.title.setText(myTicketShops.get(position).getTitle());
		holder.orderId.setText("订单号："
				+ myTicketShops.get(position).getOrderID());
		holder.numberPrcie.setText("共"
				+ myTicketShops.get(position).getOrderProductList().get(0)
						.getQuantity() + "件,合计："
				+ myTicketShops.get(position).getShopMoney() + "元");
		showBtn(myTicketShops.get(position));
		// 设置图片
		LayoutParams ImgParams = holder.productImg.getLayoutParams();
		ImgParams.width = activity.screenWidth * 3 / 10;
		ImgParams.height = activity.screenWidth * 3 / 10;
		holder.productImg.setLayoutParams(ImgParams);

		String url = Config.JMZG
				+ myTicketShops.get(position).getOrderProductList().get(0)
						.getImageUrl().replace(".jpg", "_1.jpg");
		if (!url.equals(holder.productImg.getTag())) {
			ImageLoader.getInstance().displayImage(url, holder.productImg,
					MyApplication.getInstance().getOptions(),
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingComplete(String imageUrl,
								View view, Bitmap loadedImage) {
							super.onLoadingComplete(imageUrl, view, loadedImage);
							holder.productImg.setTag(imageUrl);
						}
					});
		}
		holder.orderState.setOnClickListener(new OnMyTicketOnclick(position));
		holder.delete.setOnClickListener(new OnMyTicketOnclick(position));
		holder.refundState.setOnClickListener(new OnMyTicketOnclick(position));
	}

	private class OnMyTicketOnclick implements OnClickListener {
		int position = 0;
		private DeleteOrderUtile deleteOrderUtile;
		private int doWhat = 0;
		// private UpdateOrderStateUtile updateOrderStateUtile;
		private PayUtile payUtile;
		private MyOrderShop myTicketShop;

		public OnMyTicketOnclick(int position) {
			this.position = position;
			myTicketShop = myTicketShops.get(position);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.adapter_myticket_delete:
				deleteOrderUtile = new DeleteOrderUtile(activity, myTicketShop,
						MyTicketAdapter.this);
				activity.showMyAlertDialog(R.string.myorder_msg_delete, this,
						View.GONE);
				doWhat = 1;
				break;
			// 确认收货
			case R.id.adapter_myticket_orderstate:
				if (Utile.isEqual(myTicketShop.getOrderStatus(),
						OrderState.WaitBuyerPay.name())) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(Config.TAG_TITLE, myTicketShop.getTitle());
					map.put(Config.TAG_TRADETYPE, Config.TRADE_TYPE_GOUWU);
					map.put(Config.TAG_ORDERIDS, myTicketShop.getOrderID());
					map.put(Config.TAG_TRADEID, myTicketShop.getTrade_TradeID());
					payUtile = new PayUtile(activity, map, MyTicketAdapter.this);
					payUtile.tradeSubmit();
				}
				// else {
				// updateOrderStateUtile = new UpdateOrderStateUtile(activity,
				// myTicketShops.get(position), MyTicketAdapter.this);
				// if (Utile.isEqual(myTicketShop.getOrderStatus(),
				// OrderState.WaitBuyerPay.name())) {
				// activity.showMyAlertDialog(
				// R.string.myorder_msg_confirmgoods, this);
				// doWhat = 2;
				// }
				// }
				break;
			// 申请退款
			case R.id.adapter_myticket_refundstate:
				// updateOrderStateUtile = new UpdateOrderStateUtile(activity,
				// myTicketShops.get(position), MyTicketAdapter.this);
				// if (Utile.isEqual(myOrderShop.getOrderStatus(),
				// OrderState.WaitSellerSendGoods.name())) {
				// activity.showMyAlertDialog(R.string.myorder_msg_refund,
				// this);
				// doWhat = 3;
				// }
				Intent intent = new Intent(activity, ProductActivity.class);
				intent.putExtra(Config.TAG_PRODUCTID, myTicketShop
						.getOrderProductList().get(0).getProductID());
				activity.startActivity(intent);
				break;
			case R.id.myalerdialog_sure:
				if (doWhat == 1) {
					deleteOrderUtile.delete();
				}
				// else if (doWhat == 3) {
				// updateOrderStateUtile.submit(true);
				// }
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

	/**
	 * 根据情况显示按钮的不同情况
	 */
	private void showBtn(MyOrderShop mos) {
		// mos.setOrderStatus(OrderState.WaitBuyerConfirmGoods.name());
		// mos.setRefundStatus(OrderState.WaitSellerAgree.name());
		if (Utile.isEqual(mos.getRefundStatus(), OrderState.None.name())) {
			if (Utile.isEqual(mos.getOrderStatus(),
					OrderState.WaitSellerSendGoods.name())) {
				holder.orderState.setText(R.string.myticket_nouse);
				holder.refundState.setVisibility(View.VISIBLE);
				holder.orderState.setEnabled(false);
			} else if (Utile.isEqual(mos.getOrderStatus(),
					OrderState.WaitBuyerConfirmGoods.name())
					|| Utile.isEqual(mos.getOrderStatus(),
							OrderState.TradeFinished.name())) {
				if (Utile.isEqual(mos.getOrderProductList().get(0)
						.getQuantity().toString(), mos.getOrderProductList()
						.get(0).getSendGoodsQuantity().toString())) {
					holder.orderState.setText(R.string.myticket_useed);
					
				} else {
					holder.orderState.setText(R.string.myticket_enable);
					
				}
				
				holder.refundState.setVisibility(View.GONE);
				holder.orderState.setEnabled(false);
			} else if (Utile.isEqual(mos.getOrderStatus(),
					OrderState.TradeClosed.name())
					|| Utile.isEqual(mos.getOrderStatus(),
							OrderState.TradeClosedBySystem.name())) {
				holder.orderState.setText(R.string.myticket_enable);
				holder.refundState.setVisibility(View.GONE);
				holder.orderState.setEnabled(false);
			} else {
				holder.orderState.setText(OrderState.valueOf(
						mos.getOrderStatus()).getName());
				holder.refundState.setVisibility(View.GONE);
				holder.orderState.setEnabled(true);
			}

			if (Utile.isEqual(mos.getOrderStatus(),
					OrderState.WaitBuyerConfirmGoods.name())
					|| Utile.isEqual(mos.getOrderStatus(),
							OrderState.TradeFinished.name())
					|| Utile.isEqual(mos.getOrderStatus(),
							OrderState.WaitSellerSendGoods.name())) {
				holder.delete.setVisibility(View.GONE);
			} else {
				holder.delete.setVisibility(View.VISIBLE);
			}
		} else {
			holder.orderState.setText(OrderState.valueOf(mos.getRefundStatus())
					.getName());
			holder.orderState.setEnabled(false);
			holder.refundState.setVisibility(View.GONE);
			holder.delete.setVisibility(View.VISIBLE);
		}
	}

	private static class ViewHolder {
		protected TextView orderId;
		protected TextView delete;
		protected TextView numberPrcie, title, orderState, refundState;
		protected ImageView productImg;
	}

	@Override
	public void onDeleteUtileSuccess(MyOrderShop myOrderShop) {
		myTicketShops.remove(myOrderShop);
		setMyOrderShops(myTicketShops);
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
		
	}

	// @Override
	// public void onUpdataUtileSuccess(boolean b,String OrderId) {
	//
	// }

}
