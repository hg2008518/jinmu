package com.jmz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmz.bean.MyOrderInfo;
import com.jmz.bean.OrderList;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.OrderState;
import com.jmz.uitl.Utile;

/**
 * 支付结果
 * 
 * @author Administrator
 * 
 */
public class TradeResultActivity extends ParentActivity {
	private static final String LOGTAG = "TradeResultActivity";
	private LinearLayout sussLayout;
	private TextView money, adress, titleTv, qmfNumber, myOrder,fail;
	private MyOrderInfo myOrderInfo;
	private OrderList orderList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myOrderInfo = (MyOrderInfo) getIntent().getExtras().getSerializable(
				TradeActivity.MYORDERINFO);
		top_Home.setVisibility(View.VISIBLE);
		initMyView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		TradeResultActivity.this.finish();
	}
	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(TradeResultActivity.this).inflate(
				R.layout.activity_trade_result, null));
		sussLayout = (LinearLayout) findViewById(R.id.traderesutl_suss);
		fail = (TextView) findViewById(R.id.traderesutl_fail);
		money = (TextView) findViewById(R.id.traderesutl_money);
		adress = (TextView) findViewById(R.id.traderesutl_adress);
		titleTv = (TextView) findViewById(R.id.traderesutl_product_title);
		qmfNumber = (TextView) findViewById(R.id.traderesutl_qmfnumber);
		myOrder = (TextView) findViewById(R.id.traderesutl_gomyorder);
		
		if(Utile.isEqual(myOrderInfo.getProductType(),"Ticket")){
			myOrder.setText(R.string.myticket_tittle);
		}else{
			myOrder.setText(R.string.myorder_tittle);
		}
		if(Utile.isEqual(myOrderInfo.getOrderStatus(), OrderState.WaitSellerSendGoods.name())){
			fail.setVisibility(View.GONE);
			sussLayout.setVisibility(View.VISIBLE);
		}else if(Utile.isEqual(myOrderInfo.getOrderStatus(), OrderState.WaitBuyerPay.name())){
			sussLayout.setVisibility(View.GONE);
			fail.setVisibility(View.VISIBLE);
		}
		myOrder.setOnClickListener(new OrderResutlClick());
		money.setText(myOrderInfo.getOrderAmount());
		adress.setText(myOrderInfo.getExpressString());
		titleTv.setText(myOrderInfo.getTitle());
		qmfNumber.setText(myOrderInfo.getOrderID());
	}

	/**
	 * 单击事件
	 * 
	 * @author Administrator
	 * 
	 */
	private class OrderResutlClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.traderesutl_gomyorder:
				if(Utile.isEqual(myOrderInfo.getProductType(),"Ticket")){
					startActivity(new Intent(TradeResultActivity.this,
							MyTicketActivity.class));
				}else{
					startActivity(new Intent(TradeResultActivity.this,
							MyOrderActivity.class));
				}
				break;

			default:
				break;
			}
		}

	}

}
