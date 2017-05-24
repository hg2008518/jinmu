package com.jmz.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jmz.AddressEditActivity;
import com.jmz.LoginActivity;
import com.jmz.MyApplication;
import com.jmz.OrderActivity;
import com.jmz.ParentActivity;
import com.jmz.ProductActivity;
import com.jmz.R;
import com.jmz.adapter.PopProductSelectAttrAdapter;
import com.jmz.bean.ParentBean;
import com.jmz.bean.Product;
import com.jmz.bean.ProductAliveSelectAttr;
import com.jmz.bean.ProductAttrGroup;
import com.jmz.bean.ProductAttrGroupList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.AddCarUtile;
import com.jmz.uitl.Config;
import com.jmz.uitl.ServerReturnStatus;
import com.jmz.uitl.Utile;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 选择商品属性
 * 
 * @author Administrator
 * 
 */
public class SelectAttributePopupWindow extends PopupWindow implements
		OnClickListener {
	public static final String RETURNSELECTEDATTR = "ReturnSelectedAttr";
	private LinearLayout layout;
	private TextView addImg;
	private TextView subImg;
	private EditText numberEditText;
	private ParentActivity activity;
	private TextView sureAdd;
	private TextView stockTextView;
	private TextView sureOrder;
	private SubmitTask task;
	private Context context;
	private ImageView img;
	private TextView title;
	private TextView price;
	private TextView selectAttred;
	private ListView listView;
	private ProductAttrGroupList productGroupList;
	private ArrayList<ProductAttrGroup> productGroupLists;
	private Product product;
	private String flag = "2";
	private int totalStock;
	private int currSelctNumber;
	private Intent intent;
	private Bundle bundle;
	private PopProductSelectAttrAdapter adapter;
	private ProductAliveSelectAttr productAliveSelectAttr;
	private Handler phandler;//回调的handler

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ArrayList<ProductAttrGroup> handProductGroupLists = (ArrayList<ProductAttrGroup>) msg
					.getData().getSerializable(
							PopProductSelectAttrAdapter.SELECTEDATTR);
			productGroupList.setGetAttr(handProductGroupLists);
			product.setAttrText(productGroupList.getSelectAttr(false));
			selectAttred.setText(productGroupList.getSelectAttr(false));
			if (productGroupList.isAttrAllSelect()) {
				flag = "1";
			} else {
				flag = "2";
			}
			productSelectedSubmit(productGroupList.getSelectAttr(true));
			if (productGroupList.getSelectAttr().getAttrImg() != null
					&& productGroupList.getSelectAttr().getAttrImg().length() > 0) {
				ImageLoader.getInstance()
						.displayImage(
								Config.JMZG
										+ productGroupList.getSelectAttr()
												.getAttrImg(), img,
								MyApplication.getInstance().getOptions());
			}
			
			//将选择的属性返回
			Message msg1 = new Message();
			Bundle bundle = new Bundle();
			bundle.putString(RETURNSELECTEDATTR, selectAttred.getText().toString());
			msg1.setData(bundle);
			phandler.sendMessage(msg1);
		}
	};
	private AddCarUtile addCarUtile;

	public SelectAttributePopupWindow(Context context, Handler phander, Product product,
			ProductAttrGroupList productGroupList) {
		super(context);
		this.context = context;
		this.phandler = phander;
		this.activity = (ParentActivity) context;
		this.productGroupList = productGroupList;
		this.totalStock = product.getAllAttrTotalStock();
		this.product = product;
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(
				R.layout.pop_product_selectattr, null);
		img = (ImageView) layout.findViewById(R.id.pop_product_img);
		title = (TextView) layout.findViewById(R.id.pop_product_title);
		price = (TextView) layout.findViewById(R.id.pop_product_price);
		selectAttred = (TextView) layout
				.findViewById(R.id.pop_product_selectattr);
		listView = (ListView) layout.findViewById(R.id.pop_product_listview);
		subImg = (TextView) layout.findViewById(R.id.pop_product_subbtn);
		addImg = (TextView) layout.findViewById(R.id.pop_product_addbtn);
		numberEditText = (EditText) layout
				.findViewById(R.id.pop_product_numberedit);
		sureAdd = (TextView) layout.findViewById(R.id.pop_product_sure_addcar);
		sureOrder = (TextView) layout.findViewById(R.id.pop_product_sure_order);
		stockTextView = (TextView) layout.findViewById(R.id.product_pop_stock);
		productGroupLists = productGroupList.getGetAttr();
		adapter = new PopProductSelectAttrAdapter(context, productGroupLists,
				handler);
		listView.setAdapter(adapter);
		// stockTextView.setText("库存：" + totalStock);
		if (totalStock <= 0) {
			stockTextView.setText("库存：无货");
		} else {
			stockTextView.setText("库存：" + totalStock);
		}
		LayoutParams params = img.getLayoutParams();
		params.width = activity.screenWidth * 3 / 10;
		params.height = activity.screenWidth * 3 / 10;
		img.setLayoutParams(params);
		ImageLoader.getInstance().displayImage(
				Config.JMZG + product.getImageUrl(), img,
				MyApplication.getInstance().getOptions());
		title.setText(product.getProductTitle());
		price.setText(product.getShopPrice());
		selectAttred.setText(productGroupList.getSelectAttr(false));
		// 设置按钮监听
		subImg.setOnClickListener(this);
		addImg.setOnClickListener(this);
		sureAdd.setOnClickListener(this);
		sureOrder.setOnClickListener(this);
		// 设置SelectPicPopupWindow的View
		this.setContentView(layout);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupAnimation);
		// view.setAnimation(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(android.R.color.transparent);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

		// 软键盘不会挡着popupwindow
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		intent = new Intent();
		bundle = new Bundle();
		update();
		switch (product.getCurrentState()) {
		case 0:
			// countdownLayout.setVisibility(View.VISIBLE);
			// countdownTitle.setText(R.string.product_timetostart);
			sureOrder.setEnabled(false);
			// addCarBtn.setEnabled(false);
			sureOrder.setText(R.string.product_wait);
			break;
		case 1:
			// countdownLayout.setVisibility(View.GONE);
			sureOrder.setEnabled(false);
			// addCarBtn.setEnabled(false);
			sureOrder.setText(R.string.product_fished);
			break;
		case 2:
			// countdownLayout.setVisibility(View.VISIBLE);
			// countdownTitle.setText(R.string.product_timetoend);
			sureOrder.setEnabled(true);
			// addCarBtn.setEnabled(true);
			sureOrder.setText(R.string.product_pay);
			sureOrder.setEnabled(productGroupList.isAttrAllSelect() ? true
					: false);
			break;
		case 3:
			// countdownLayout.setVisibility(View.GONE);
			// addCarBtn.setEnabled(false);
			sureOrder.setEnabled(false);
			sureOrder.setText(R.string.product_no_stock);
			break;

		default:
			break;
		}
	}

	/**
	 * 提交获取
	 */
	private void productSelectedSubmit(String str) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Config.TAG_PRODUCTID, product.getProductID());
		map.put(Config.TAG_ATTRS, str);
		map.put(Config.TAG_FLAG, flag);
		task = new SubmitTask(activity, Config.PRODUCT_SELECTATTR,
				ProductAliveSelectAttr.class,
				new onProductSelectedSubmitListener(), true);
		task.execute(map);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
			task.cancel(true);
		}
	}

	/**
	 * 响应获取属性
	 * 
	 * @author Administrator
	 * 
	 */
	private class onProductSelectedSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			productAliveSelectAttr = (ProductAliveSelectAttr) result
					.getObject();
			product.setAttrId(productAliveSelectAttr.getGetAttr()
					.getAttrExtendID());
			adapter.setProductAttrGroups(productGroupList
					.getProductEnableAttr(productAliveSelectAttr));
			try {
				totalStock = Integer.parseInt(productAliveSelectAttr
						.getGetAttr().getInvaentory());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (productGroupList.isAttrAllSelect()) {
				if (totalStock <= 0) {
					stockTextView.setText("库存：无货");
				} else {
					stockTextView.setText("库存：" + totalStock);
				}
			}
			price.setText(productAliveSelectAttr.getGetAttr().getGoodsPrice());
			if (totalStock <= 0) {
				sureOrder.setEnabled(false);
				sureAdd.setEnabled(false);
				//sureOrder.setText(R.string.product_no_stock);
				//sureAdd.setText(R.string.product_no_stock);
			} else {
				sureOrder.setEnabled(true);
				sureAdd.setEnabled(true);
				sureOrder.setText(R.string.product_pay);
				sureAdd.setText(R.string.product_add_car);
			}
		}
	}

	@Override
	public void onClick(View v) {
		currSelctNumber = Integer.parseInt(numberEditText.getText().toString());
		switch (v.getId()) {
		case R.id.pop_product_addbtn:
			if (currSelctNumber < totalStock) {
				currSelctNumber++;
				numberEditText.setText(currSelctNumber + "");
			}
			break;
		case R.id.pop_product_subbtn:
			if (1 < currSelctNumber) {
				currSelctNumber--;
				numberEditText.setText(currSelctNumber + "");
			}
			break;
		case R.id.pop_product_sure_addcar:
			bundle.putSerializable(ProductActivity.TAG_PRODUCT, product);
			if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
				if (productAliveSelectAttr != null
						&& productAliveSelectAttr.getGetAttr()
								.getAttrExtendID() != null) {
					product.setCurrQuantity(currSelctNumber);
					addCarUtile = new AddCarUtile(context, product);
					addCarUtile.addCarSubmit();
					dismiss();
				} else {
					activity.showToast("请选择属性");
				}
			} else {
				intent.setClass(context, LoginActivity.class);
				bundle.putString(ProductActivity.TAG_ACIVITY,
						ProductActivity.LOGTAG);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}

			break;
		case R.id.pop_product_sure_order:
			bundle.putSerializable(ProductActivity.TAG_PRODUCT, product);
			if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
				intent.setClass(context, OrderActivity.class);
				bundle.putInt(ProductActivity.TAG_SELCETNUMBERED,
						currSelctNumber);
				intent.putExtras(bundle);
				context.startActivity(intent);
			} else {
				intent.setClass(context, LoginActivity.class);
				bundle.putInt(ProductActivity.TAG_SELCETNUMBERED,
						currSelctNumber);
				bundle.putString(ProductActivity.TAG_ACIVITY,
						OrderActivity.LOGTAG);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
			dismiss();
			break;

		default:
			break;
		}
	}

}
