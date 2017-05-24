package com.jmz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.jmz.adapter.ProductCalssAdapter;
import com.jmz.bean.Product;
import com.jmz.bean.ProductList;
import com.jmz.bean.Shop;
import com.jmz.bean.ShopInfo;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Utile;
import com.jmz.view.PullToRefreshLayout;
import com.jmz.view.PullableGridView;
import com.jmz.view.PullToRefreshLayout.OnRefreshListener;
import com.jmz.view.PullableListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * 商铺商品列表
 * 
 * @author Administrator
 * 
 */
public class ShopActivity extends ParentActivity implements OnRefreshListener,
		OnClickListener {
	public static final String LOGTAG = "ShopActivity";
	private LinearLayout allProductsLayout;
	private PullableGridView refreView;
	private PullToRefreshLayout refreLayout, pullToRefreshLayout;
	private RadioGroup group;
	private LinearLayout emptyView;
	private ProductCalssAdapter adapter;
	private ImageView shopImg, shopInfoImg;
	private int page = 1;
	private int pageTotal;
	private int tag = Config.DRAG_NORMAL;
	private String type = Config.TYPE_SORT;
	private List<Product> allProductList;
	private boolean isDissDialog = false;
	private RadioButton nomalView, hotView, hitView, newView;
	private String shopId;
	private Dialog shopInfoDialog;
	private TextView address, phone, onlineTime, about, tuwen;
	private LinearLayout shopInfoDialogLayout;
	private Shop shop;
	private SubmitTask shopInfoTask;
	public ProductList productList;
	private SubmitTask ProductListTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(R.string.shop_title);
		initLoadiagle();
		shopId = getIntent().getStringExtra(Config.TAG_SHOPID);
		mainSubmit();
		allProductList = new ArrayList<Product>();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (pullToRefreshLayout != null) {
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
		}
	}

	/**
	 * 初始化组件
	 */
	private void initMyview() {
		allProductsLayout = (LinearLayout) LayoutInflater.from(
				ShopActivity.this).inflate(R.layout.activity_shop_center, null);
		initcenterView(allProductsLayout);
		refreLayout = (PullToRefreshLayout) findViewById(R.id.shop_refrelayout);
		refreLayout.setOnRefreshListener(this);
		shopImg = (ImageView) findViewById(R.id.shop_img);
		group = (RadioGroup) findViewById(R.id.shop_group);
		nomalView = (RadioButton) findViewById(R.id.shop_nomalview);
		hotView = (RadioButton) findViewById(R.id.shop_hotview);
		hitView = (RadioButton) findViewById(R.id.shop_hitview);
		newView = (RadioButton) findViewById(R.id.shop_newview);
		emptyView = (LinearLayout) findViewById(R.id.activity_emptyview);
		refreView = (PullableGridView) findViewById(R.id.shop_listview);
		refreView.setEmptyView(emptyView);
		refreView.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), false, false));
		shopImg.setOnClickListener(this);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkId) {
				page = 1;
				if (nomalView.getId() == checkId) {
					type = Config.TYPE_SORT;
				} else if (hotView.getId() == checkId) {
					type = Config.TYPE_HOT;
				} else if (hitView.getId() == checkId) {
					type = Config.TYPE_HIT;
				} else if (newView.getId() == checkId) {
					type = Config.TYPE_ALL;
				}
				tag = Config.REFRE_OTHER;
				isDissDialog = true;
				mainSubmit();
			}
		});
	}

	/**
	 * 店铺商品列表提交
	 */
	@Override
	protected void mainSubmit() {
		HashMap<String, String> productListMap = new HashMap<String, String>();
		productListMap.put(Config.TAG_TYPE, type);
		productListMap.put(Config.TAG_PAGE, page + "");
		productListMap.put(Config.TAG_PROPERTYID, "0");
		productListMap.put(Config.TAG_PROPERTYTYPE, "1");
		productListMap.put(Config.TAG_NODEID, "");
		if (shopId != null) {
			productListMap.put(Config.TAG_SHOPID, shopId);
			ProductListTask = new SubmitTask(this, Config.PRODUCT_LIST,
					ProductList.class, new onProductListSubmitListener(),
					isDissDialog);
			ProductListTask.execute(productListMap);
		} else {
			finish();
			showToast(R.string.product_exist);
		}
	}

	/**
	 * 店铺详情提交
	 */
	private void shopInfoSubmit() {
		HashMap<String, String> shopInfoMap = new HashMap<String, String>();
		shopInfoMap.put(Config.TAG_SHOPID,
				getIntent().getStringExtra(Config.TAG_SHOPID));
		shopInfoTask = new SubmitTask(this, Config.SHOP_INFO, ShopInfo.class,
				new onShopInfoSubmitListener(), false);
		shopInfoTask.execute(shopInfoMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (shopInfoTask != null) {
			shopInfoTask.destorySelf();
		}
		if (ProductListTask != null) {
			ProductListTask.destorySelf();
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		page = 1;
		tag = 1;
		isDissDialog = false;
		mainSubmit();
		this.pullToRefreshLayout = pullToRefreshLayout;
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		this.pullToRefreshLayout = pullToRefreshLayout;
		isDissDialog = false;
		if (page < pageTotal) {
			page++;
			tag = 2;
			mainSubmit();
		} else {
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
			showToast(R.string.refre_last);
		}
	}

	/**
	 * 响应得到所有product
	 * 
	 * @author Administrator
	 * 
	 */
	private class onProductListSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			productList = (ProductList) result.getObject();
			pageTotal = Integer.parseInt(productList.getData().get(0)
					.getPageTotal());
			if (tag == Config.DRAG_INDEX) {
				allProductList.clear();
				allProductList.addAll(productList.getProductList());
				adapter.notifyDataSetChanged();
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
				refreView.setSelection(0);
			} else if (tag == Config.LOADMORE_INDEX) {
				allProductList.addAll(productList.getProductList());
				adapter.notifyDataSetChanged();
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			} else if (tag == Config.REFRE_OTHER) {
				allProductList.clear();
				allProductList.addAll(productList.getProductList());
				adapter.setProductList(allProductList);
				refreView.requestFocusFromTouch();
				refreView.setSelection(0);
			} else {
				initMyview();
				allProductList.addAll(productList.getProductList());
				adapter = new ProductCalssAdapter(ShopActivity.this,
						allProductList);
				refreView.setAdapter(adapter);
				shopInfoSubmit();
			}
			refreView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(ShopActivity.this,
							ProductActivity.class);
					intent.putExtra(Config.TAG_PUODUCTID,
							allProductList.get(position).getProductID());
					if (Utile.isEqual(allProductList.get(position)
							.getProductStatus(), "OnSale")) {
						startActivity(intent);
					} else {
						showToast(R.string.product_exist);
					}
				}
			});

		}
	}

	/**
	 * 响应得到所有product
	 * 
	 * @author Administrator
	 * 
	 */
	private class onShopInfoSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ShopInfo shopInfo = (ShopInfo) result.getObject();
			shop = shopInfo.getShopInfo().get(0);
			ImageLoader.getInstance().displayImage(
					Config.JMZG + shop.getImageUrl().replace(".", "_0."), shopImg);
			if (shop != null) {
				showShopInfoDialog();
			} else {

			}

		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.shop_img:
			shopInfoDialog.show();
			break;
		case R.id.shopinfo_address:

			break;
		case R.id.shopinfo_phone:

			break;
		case R.id.shopinfo_onlinetime:

			break;
		case R.id.shopinfo_about:

			break;
		case R.id.shopinfo_tuwen:
			intent.setClass(ShopActivity.this, WebViewAcitivity.class);
			intent.putExtra(PostOutActivity.URLSTRING, Config.JMZGSHOPTUWEN
					+ shopId);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	/**
	 * 显示店铺详情
	 */
	private void showShopInfoDialog() {
		shopInfoDialog = new Dialog(this);
		shopInfoDialogLayout = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.activity_shopinfo_center, null);
		shopInfoImg = (ImageView) shopInfoDialogLayout
				.findViewById(R.id.shopinfo_img);
		address = (TextView) shopInfoDialogLayout
				.findViewById(R.id.shopinfo_address);
		phone = (TextView) shopInfoDialogLayout
				.findViewById(R.id.shopinfo_phone);
		onlineTime = (TextView) shopInfoDialogLayout
				.findViewById(R.id.shopinfo_onlinetime);
		about = (TextView) shopInfoDialogLayout
				.findViewById(R.id.shopinfo_about);
		tuwen = (TextView) shopInfoDialogLayout
				.findViewById(R.id.shopinfo_tuwen);
		address.setOnClickListener(this);
		phone.setOnClickListener(this);
		onlineTime.setOnClickListener(this);
		about.setOnClickListener(this);
		tuwen.setOnClickListener(this);
		shopInfoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		shopInfoDialog.setContentView(shopInfoDialogLayout);
		shopInfoDialog.setCancelable(true);
		shopInfoDialog.getWindow()
				.setWindowAnimations(R.style.DialogWindowAnim);
		shopInfoDialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		shopInfoDialog.getWindow().setLayout(screenWidth * 8 / 10,
				LayoutParams.WRAP_CONTENT);
		ImageLoader.getInstance().displayImage(
				Config.JMZG + shop.getImageUrl().replace(".", "_0."), shopInfoImg);
		address.setText(shop.getAddress());
		phone.setText(shop.getTelephone() + "  " + shop.getContact());
		onlineTime.setText(getString(R.string.hop_inshoptime)
				+ shop.getBusinessHours());
	}
}
