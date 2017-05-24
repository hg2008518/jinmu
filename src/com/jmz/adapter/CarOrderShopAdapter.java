package com.jmz.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jmz.R;
import com.jmz.bean.CarShop;
import com.jmz.bean.OneOrderPreviewList;
import com.jmz.uitl.Logger;

/**
 * 购物车商店交易订单预览适配器
 * 
 * @author Administrator
 * 
 */
public class CarOrderShopAdapter extends BaseAdapter {
	public static final String HANDLERORDER = "handlerorder";
	private Context context;
	public ArrayList<CarShop> carShop;
	private ViewHolder holder;
	private String[] statesCn = new String[2];
	private String[] statesEn = new String[2];
	private int dialogPosition;
	private Handler handler;
	private OneOrderPreviewList oneOrderPreviewList;

	public CarOrderShopAdapter(Context context, ArrayList<CarShop> carShop,
			Handler handler, OneOrderPreviewList oneOrderPreviewList) {
		this.context = context;
		this.carShop = carShop;
		//Logger.i("test", "-->carShop:" + carShop);
		this.handler = handler;
		this.oneOrderPreviewList = oneOrderPreviewList;
		statesCn[0] = context.getString(R.string.order_express);
		statesCn[1] = context.getString(R.string.order_selfget);

		statesEn[0] = "Express";
		statesEn[1] = "SelfGet";
	}

	@Override
	public int getCount() {
		return carShop.size();
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
	public View getView(final int position, View view, ViewGroup parent) {
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.adapter_car_order_shop_listadapter, null);
			holder.shopTitle = (TextView) view
					.findViewById(R.id.adapter_car_order_shop_title);
			holder.typeRadioGrop = (RadioGroup) view
					.findViewById(R.id.adapter_car_order_radiogroup);
			holder.expressTypeBtn = (RadioButton) view
					.findViewById(R.id.adapter_car_order_express_type_button);
			holder.selfGetTypeBtn = (RadioButton) view
					.findViewById(R.id.adapter_car_order_selfget_type_button);
			holder.shopFare = (TextView) view
					.findViewById(R.id.adapter_car_order_shop_fare);
			// holder.expressType = (TextView) view
			// .findViewById(R.id.adapter_car_trade_shop_express);
			holder.shopMoney = (TextView) view
					.findViewById(R.id.adapter_car_order_shop_money);
			holder.shopPostscript = (EditText) view
					.findViewById(R.id.adapter_car_order_shop_postscript);
			holder.productList = (ListView) view
					.findViewById(R.id.adapter_car_order_shop_list);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.typeRadioGrop.setTag(position);
		
		setDataToview(position);
		return view;
	}

	/**
	 * 为组件赋值
	 */
	private void setDataToview(final int position) {
		holder.shopTitle.setText(carShop.get(position).getShopName());
		holder.shopMoney.setText(context.getString(R.string.yuanicon)
				+ oneOrderPreviewList.getOrderPreviewInfo().get(position)
						.getItemSummaryPromotionInfo().getShopActualPrice());
		holder.productList.setAdapter(new CarOrderProductAdapter(context,
				carShop.get(position).getProductList()));
		holder.shopFare.setText(oneOrderPreviewList.getOrderPreviewInfo()
				.get(position).getItemSummaryPromotionInfo()
				.getMyFareActualPrice(carShop.get(position).getExpressType()));
		holder.shopFare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(context).setTitle(
						context.getString(R.string.msg_selectfare))
						.setSingleChoiceItems(statesCn, dialogPosition,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialogPosition = which;
										if (dialogPosition == 0) {
											holder.shopFare
													.setText(statesCn[dialogPosition]
															+ ":"
															+ carShop
																	.get(position)
																	.getFareTotalPrice());
										}

										carShop.get(position).setExpressType(
												statesEn[dialogPosition]);
										notifyDataSetChanged();
										Message msg = new Message();
										msg.what = 1;
										msg.obj = carShop;
										handler.sendMessage(msg);
										dialog.dismiss();
									}
								});// .show()
			}
		});
		holder.typeRadioGrop.setOnCheckedChangeListener(null);
		if (carShop != null) {
			if (carShop.get(position).isExpressTypeisCheck()) {
				holder.typeRadioGrop.check(holder.expressTypeBtn.getId());
			} else {
				holder.typeRadioGrop.check(holder.selfGetTypeBtn.getId());
			}
		}
		
		holder.typeRadioGrop
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int childID) {
				// TODO Auto-generated method stub
				CarShop carShopInfo = carShop.get(position);
				//Logger.i("test", "--->position:" + position + " carShopInfo:" + carShopInfo);
				//Logger.i("test", "---> expressID:" + holder.expressTypeBtn.getId() + " selfgetID:" +  holder.selfGetTypeBtn.getId() +  " childID:" + childID);
				if (holder.expressTypeBtn.getId() == childID) {
					carShopInfo.setExpressType(statesEn[0]);
					carShopInfo.setExpressTypeisCheck(true);
				} else if (holder.selfGetTypeBtn.getId() == childID) {
					carShopInfo.setExpressType(statesEn[1]);
					carShopInfo.setExpressTypeisCheck(false);
				}

				notifyDataSetChanged();
				Message msg = new Message();
				msg.what = 1;
				msg.obj = carShop;
				handler.sendMessage(msg);
			}
		});

		holder.shopPostscript.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {// 输入时监控
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {// 输入前监控
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {// 输入后监控
				// TODO Auto-generated method stub
				carShop.get(position).setPostMsg(arg0.toString());
			}
		});
	}

	private static class ViewHolder {
		// public TextView expressType;
		protected EditText shopPostscript;
		private RadioGroup typeRadioGrop;
		private RadioButton expressTypeBtn, selfGetTypeBtn;
		protected TextView shopFare;
		protected TextView shopTitle, shopMoney;
		protected ListView productList;
	}

}
