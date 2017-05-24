package com.jmz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmz.bean.CarShopList;
import com.jmz.bean.OneOrderPreviewList;
import com.jmz.uitl.OrderState;
import com.jmz.uitl.Utile;

/**
 * 支付结果
 * 
 * @author Administrator
 * 
 */
public class CarTradeResultActivity extends ParentActivity {
	private static final String LOGTAG = "CarTradeResultActivity";
	private LinearLayout sussLayout;
	private TextView money, adress, titleTv, qmfNumber, myOrder, fail;
	private OneOrderPreviewList oneOrderPreviewList;
	private CarShopList carShopList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		oneOrderPreviewList = (OneOrderPreviewList) getIntent().getExtras()
				.get(CarOrderActivity.TAG_ONEORDERPREVIEWLIST);
		carShopList = (CarShopList) getIntent().getExtras().get(
				CarActivity.TAG_CARSHOPLIST);
		top_Home.setVisibility(View.VISIBLE);
		initMyView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		CarTradeResultActivity.this.finish();
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(CarTradeResultActivity.this)
				.inflate(R.layout.activity_trade_result, null));
		sussLayout = (LinearLayout) findViewById(R.id.traderesutl_suss);
		fail = (TextView) findViewById(R.id.traderesutl_fail);
		money = (TextView) findViewById(R.id.traderesutl_money);
		adress = (TextView) findViewById(R.id.traderesutl_adress);
		titleTv = (TextView) findViewById(R.id.traderesutl_product_title);
		qmfNumber = (TextView) findViewById(R.id.traderesutl_qmfnumber);
		myOrder = (TextView) findViewById(R.id.traderesutl_gomyorder);

		if (Utile.isEqual(oneOrderPreviewList.getProductType(), "Ticket")) {
			myOrder.setText(R.string.myticket_tittle);
		} else {
			myOrder.setText(R.string.myorder_tittle);
		}
		if (Utile.isEqual(oneOrderPreviewList.getOrderStatus(),
				OrderState.WaitSellerSendGoods.name())) {
			fail.setVisibility(View.GONE);
			sussLayout.setVisibility(View.VISIBLE);
		} else if (Utile.isEqual(oneOrderPreviewList.getOrderStatus(),
				OrderState.WaitBuyerPay.name())) {
			sussLayout.setVisibility(View.GONE);
			fail.setVisibility(View.VISIBLE);
		}
		myOrder.setOnClickListener(new OrderResutlClick());
		money.setText(oneOrderPreviewList.getData().getOrderAmountTotal());
		adress.setText(oneOrderPreviewList.getAddressBean().getMyExpress());
		titleTv.setText(carShopList.getAllSelectArg(6));
		qmfNumber.setText(oneOrderPreviewList.getOrderID());
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
				if (Utile.isEqual(oneOrderPreviewList.getProductType(), "Ticket")) {
					startActivity(new Intent(CarTradeResultActivity.this,
							MyTicketActivity.class));
				} else {
					startActivity(new Intent(CarTradeResultActivity.this,
							MyOrderActivity.class));
				}
				break;

			default:
				break;
			}
		}

	}

}
