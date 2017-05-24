package com.jmz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jmz.adapter.ProductCalssAdapter;
import com.jmz.bean.Child;
import com.jmz.bean.Product;
import com.jmz.bean.ProductList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.OnTabActivityResultListener;
import com.jmz.uitl.Utile;
import com.jmz.view.PullToRefreshLayout;
import com.jmz.view.PullToRefreshLayout.OnRefreshListener;
import com.jmz.view.PullableGridView;
import com.jmz.view.TileTextView;

/**
 * 播金券.分奖金
 * 
 * @author Administrator
 * 
 */
public class ProductClassActivity extends ParentActivity implements
		OnRefreshListener, OnClickListener,OnTabActivityResultListener {
	public static final String LOGTAG = "ProductClassActivity";
	private PullableGridView refreView;
	private LinearLayout emptyView;
	private PullToRefreshLayout refreLayout;
	private PullToRefreshLayout pullToRefreshLayout;
	private RadioGroup group;
	private RadioButton sortView, hotView, hitView, newView;
	private ProductCalssAdapter adapter;
	private List<Product> allProductList;
	private int page = 1;
	private int pageTotal;
	private int tag = Config.DRAG_NORMAL;
	private String productTitle;
	private Child bigChild;
	private Child smallChild;
	private String type = Config.TYPE_HIT;
	private boolean isDissDialog = true;
	private TileTextView topImg;
	private SubmitTask productListTask;
	private ImageView classView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initLoadiagle();
		setTittleText(R.string.product_title);

		productTitle = getIntent().getStringExtra(SearchActivity.PRODUCTTITLE);

		if (productTitle != null) {
			setTittleText(productTitle);
		}

		if (getIntent().getExtras() != null) {
			bigChild = (Child) getIntent().getExtras().get(SearchActivity.CHILD);
			if (bigChild != null) {
				setTittleText(bigChild.getChildNa());
			}else{
				bigChild = new Child("", "0", "", "1");
			}
		}
		smallChild = bigChild;
		// serchImg.setVisibility(View.VISIBLE);
		allProductList = new ArrayList<Product>();
		isDissDialog = false;
		mainSubmit();

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (pullToRefreshLayout != null) {
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && resultCode == 1) {
			reloadProductList(data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 初始化组件
	 */
	protected void initMyView() {
		initcenterView(LayoutInflater.from(ProductClassActivity.this).inflate(
				R.layout.activity_productclass_center, null));
		refreLayout = (PullToRefreshLayout) findViewById(R.id.productclass_center_refrelayout);
		refreView = (PullableGridView) findViewById(R.id.productclass_center_gridview);
		emptyView = (LinearLayout) findViewById(R.id.activity_emptyview);
		topImg = (TileTextView) findViewById(R.id.productclass_center_topimg);
		refreLayout.setOnRefreshListener(this);
		topImg.setOnClickListener(this);
		refreView.setEmptyView(emptyView);
		// refreView.setOnScrollListener(new
		// PauseOnScrollListener(ImageLoader.getInstance(), false, false));

		group = (RadioGroup) findViewById(R.id.productclass_center_group);
		classView = (ImageView) findViewById(R.id.productclass_center_class);
		sortView = (RadioButton) findViewById(R.id.productclass_center_nomalview);
		hotView = (RadioButton) findViewById(R.id.productclass_center_hotview);
		hitView = (RadioButton) findViewById(R.id.productclass_center_hitview);
		hitView.setChecked(true);
		newView = (RadioButton) findViewById(R.id.productclass_center_newview);
		classView.setOnClickListener(this);

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkId) {
				page = 1;
				isDissDialog = true;
				if (pullToRefreshLayout != null) {
					pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
				}
				if (sortView.getId() == checkId) {
					type = Config.TYPE_SORT;
				} else if (hotView.getId() == checkId) {
					type = Config.TYPE_HOT;
				} else if (hitView.getId() == checkId) {
					type = Config.TYPE_HIT;
				} else if (newView.getId() == checkId) {
					type = Config.TYPE_ALL;
				}
				tag = Config.REFRE_OTHER;
				mainSubmit();
			}
		});
		refreView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ProductClassActivity.this,
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
	/**
	 * 提交获取商品列表
	 */
	@Override
	protected void mainSubmit() {
		HashMap<String, String> productMap = new HashMap<String, String>();
		productMap.put(Config.TAG_CITYID, MyApplication.getInstance().getDbUser().getCityId());
		productMap.put(Config.TAG_PAGE, "" + page);
		productMap.put(Config.TAG_TYPE, type);
		productMap.put(Config.TAG_PROPERTYID, smallChild.getChildId());
		productMap.put(Config.TAG_PROPERTYTYPE, smallChild.getPropertyType());
		productMap.put(Config.TAG_NODEID, smallChild.getNodeId());
		productMap.put(Config.TAG_SHOPID, "");
		productMap.put(SearchActivity.PRODUCTTITLE, productTitle);
		if (productListTask != null) {
			productListTask.destorySelf();
		}
		Logger.e("hhy", productMap.toString());
		productListTask = new SubmitTask(this, Config.PRODUCT_LIST,
				ProductList.class, new OnProductListimpl(), isDissDialog);
		productListTask.execute(productMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (productListTask != null) {
			productListTask.destorySelf();
		}
	}

	/**
	 * 获取数据
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnProductListimpl implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ProductList list = (ProductList) result.getObject();
			pageTotal = Integer.parseInt(((ProductList) result.getObject())
					.getData().get(0).getPageTotal());
			isDissDialog = false;
			if (tag == Config.DRAG_INDEX) {
				topImg.setVisibility(View.GONE);
				allProductList.clear();
				allProductList.addAll(list.getProductList());
				adapter.setProductList(allProductList);
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			} else if (tag == Config.LOADMORE_INDEX) {
				allProductList.addAll(list.getProductList());
				adapter.setProductList(allProductList);
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				topImg.setVisibility(View.VISIBLE);
			} else if (tag == Config.REFRE_OTHER) {
				topImg.setVisibility(View.GONE);
				allProductList.clear();
				allProductList.addAll(list.getProductList());
				adapter.setProductList(allProductList);
				refreView.requestFocusFromTouch();
				refreView.setSelection(0);
			} else {
				initMyView();
				topImg.setVisibility(View.GONE);
				allProductList.addAll(list.getProductList());
				adapter = new ProductCalssAdapter(ProductClassActivity.this,
						allProductList);
				refreView.setAdapter(adapter);
			}
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		this.pullToRefreshLayout = pullToRefreshLayout;
		page = 1;
		tag = Config.DRAG_INDEX;
		mainSubmit();
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		this.pullToRefreshLayout = pullToRefreshLayout;
		if (page < pageTotal) {
			page++;
			tag = Config.LOADMORE_INDEX;
			mainSubmit();
		} else {
			showToast(R.string.msg_more_to_refre);
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.productclass_center_topimg:
			topImg.setVisibility(View.GONE);
			if (refreView != null) {
				refreView.setSelection(0);
			}
			break;
		case R.id.productclass_center_class:
			Intent intent = new Intent(ProductClassActivity.this,
					SearchActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(SearchActivity.CHILD, bigChild);
			intent.putExtra(SearchActivity.ISDESTROY, true);
			intent.putExtras(bundle);
			if (getParent() == null) {
				startActivityForResult(intent, 1);
			}else
				getParent().startActivityForResult(intent, 1);
			break;
		default:
			break;
		}

	}

	@Override
	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1 && resultCode == 1) {
			reloadProductList(data);
		}
	}
	
	private void reloadProductList(Intent data) {
		smallChild = (Child) data.getExtras().get(SearchActivity.CHILD);
		smallChild.setNodeId(bigChild.getNodeId());
		smallChild.setPropertyType(bigChild.getPropertyType());
		if (smallChild != null) {
			setTittleText(smallChild.getChildNa());
			isDissDialog = true;
			tag = Config.REFRE_OTHER;
			mainSubmit();
		}
	}
}
