package com.jmz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jmz.adapter.BanerAdapter;
import com.jmz.adapter.MyPagerAdapter;
import com.jmz.adapter.ProductCalssAdapter;
import com.jmz.bean.ActBean;
import com.jmz.bean.ActList;
import com.jmz.bean.Child;
import com.jmz.bean.Product;
import com.jmz.bean.ProductList;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.listener.MyOnPageChangedListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.CityUtile;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.Utile;
import com.jmz.view.HomeScrollView;
import com.jmz.view.JazzyViewPager;
import com.jmz.view.JazzyViewPager.TransitionEffect;
import com.jmz.view.MyGridView;
import com.jmz.view.PullToRefreshLayout;
import com.jmz.view.PullToRefreshLayout.OnRefreshListener;
import com.jmz.view.TileTextView;
import com.zxing.acitivity.MipcaActivityCapture;

/**
 * 首页
 * 
 * @author Administrator
 * 
 */
public class HomeActivity extends ParentActivity implements OnRefreshListener,
		OnClickListener {
	public static final String LOGTAG = "HomeActivity";
	public static final String LOCOALCITY = "locoalCity";

	// ============== 广告切换 ===================
	private JazzyViewPager mViewPager = null;
	/**
	 * 装指引的ImageView数组
	 */
	private ImageView[] mball;

	/**
	 * 装ViewPager中ImageView的数组
	 */
	private LinearLayout ballLayout = null;
	private MyPagerAdapter myPagerAdapter;

	private static final int MSG_CHANGE_PHOTO = 1;
	/** 图片自动切换时间 */
	private static final int PHOTO_CHANGE_TIME = 5000;
	private Handler mHandler = null;
	// ============== 广告切换 ===================

	private MyGridView refreView;
	private ProductCalssAdapter adapter;
	private LinearLayout emptyView;
	private int page = 1;
	private int pageTotal;
	private int scrollY = 0;
	private int tag = Config.DRAG_NORMAL;
	private ProductList productListBean;
	private RotateAnimation anim;
	private HashMap<String, String> actMap;
	private HashMap<String, String> productListMap;
	private SubmitTask actTask;
	private RadioButton homeRadioButtom;
	private boolean isShowDialog = false;
	private PullToRefreshLayout refreLayout, pullToRefreshLayout;
	private HomeScrollView abScrollView;
	private List<Product> productList;
	private List<Product> allProductList;
	private TileTextView topImg;
	private SubmitTask productListTask;
	private RadioGroup typeGroup;
	private RadioButton nomalView;
	private RadioButton hotView;
	private RadioButton newView;
	private String type = Config.TYPE_SORT;
	private ImageView homeClass;
	private ImageView qrCode;
	private EditText search;
	private ImageView searchImg;
	private MyGridView banerGridView;
	private MyGridView banerShopGridView;
	private MyGridView banerThemeGridView;
	private List<ActBean> viewPagerList;
	private List<ActBean> banerList;
	private List<ActBean> banerShopList;
	private List<ActBean> banerThemeList;
	private ArrayList<ActBean> banerAcitivityList;
	private BanerAdapter banerAdapter;
	private BanerAdapter banerShopAdapter;
	private BanerAdapter banerThemeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initLoadiagle();
		base_topView.setVisibility(View.GONE);
		allProductList = new ArrayList<Product>();
		mainSubmit();
		isFinishAvtivity = false;
		anim = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(1000);
		mHandler = new Handler(getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case MSG_CHANGE_PHOTO:
					if (mViewPager != null) {
						int index = mViewPager.getCurrentItem();
						if (index == viewPagerList.size() - 1) {
							index = -1;
						}
						mViewPager.setCurrentItem(index + 1);
					}
					mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
							PHOTO_CHANGE_TIME);

					break;
				case CityUtile.TAG_CITY_MSG:
					onParentResult();
					break;
				}
			}

		};
		mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (homeRadioButtom != null) {
			homeRadioButtom.setChecked(true);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (pullToRefreshLayout != null) {
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
		}
	}

	/**
	 * 设置父容器返回
	 */
	protected void onParentResult() {
		isShowDialog = true;
		top_logoCityText.setText(MyApplication.getInstance().getDbUser()
				.getCityName());
		tag = Config.REFRE_OTHER;
		page = 1;
		// banerList.clear();
		// banerShopList.clear();
		// banerThemeList.clear();
		// viewPagerList.clear();
		mainSubmit();
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(HomeActivity.this).inflate(
				R.layout.activity_homepage_center, null));
		refreLayout = (PullToRefreshLayout) findViewById(R.id.home_center_refrelayout);
		abScrollView = (HomeScrollView) findViewById(R.id.home_center_refreScrollView);
		homeClass = (ImageView) findViewById(R.id.home_center_class);
		qrCode = (ImageView) findViewById(R.id.home_center_qrscan);
		search = (EditText) findViewById(R.id.home_center_search);
		searchImg = (ImageView) findViewById(R.id.home_center_searchimg);
		topImg = (TileTextView) findViewById(R.id.home_center_topimg);
		banerGridView = (MyGridView) findViewById(R.id.home_baner);
		banerShopGridView = (MyGridView) findViewById(R.id.home_baner_shop);
		banerThemeGridView = (MyGridView) findViewById(R.id.home_baner_theme);
		refreView = (MyGridView) findViewById(R.id.home_productgridview);
		emptyView = (LinearLayout) findViewById(R.id.activity_emptyview);
		typeGroup = (RadioGroup) findViewById(R.id.home_center_group);
		nomalView = (RadioButton) findViewById(R.id.home_center_nomalview);
		hotView = (RadioButton) findViewById(R.id.home_center_hotview);
		newView = (RadioButton) findViewById(R.id.home_center_newview);
		ballLayout = (LinearLayout) findViewById(R.id.home_center_ball);
		mViewPager = (JazzyViewPager) findViewById(R.id.home_center_viewpager);

		banerAdapter = new BanerAdapter(this, banerList, 1);
		banerGridView.setAdapter(banerAdapter);

		banerShopAdapter = new BanerAdapter(this, banerShopList, 2);
		banerShopGridView.setAdapter(banerShopAdapter);

		banerThemeAdapter = new BanerAdapter(this, banerThemeList, 3);
		banerThemeGridView.setAdapter(banerThemeAdapter);

		myPagerAdapter = new MyPagerAdapter(HomeActivity.this, mViewPager,
				viewPagerList);
		mViewPager.setAdapter(myPagerAdapter);
		
		setDataToView();

	}

	/**
	 * 为组件赋值
	 */
	private void setDataToView() {

		initPagerView();

		refreLayout.setOnRefreshListener(this);
		qrCode.setOnClickListener(this);
		homeClass.setOnClickListener(this);
		searchImg.setOnClickListener(this);
		topImg.setOnClickListener(this);
		top_logoCityText.setOnClickListener(this);

		// 城市组件
		top_logoCityText.setText(MyApplication.getInstance().getDbUser()
				.getCityName());
		top_logoCityText.setVisibility(View.VISIBLE);

		// 大控件
		banerGridView.setOnItemClickListener(new OnItemClickListenerImpl(
				banerList));
		banerGridView.setNumColumns(banerList.size());
		// 商圈
		banerShopGridView.setOnItemClickListener(new OnItemClickListenerImpl(
				banerShopList));
		banerShopGridView.setNumColumns(banerShopList.size());
		// 主题
		banerThemeGridView.setOnItemClickListener(new OnItemClickListenerImpl(
				banerThemeList));

		refreView.setEmptyView(emptyView);
		refreView.setSelector(R.drawable.bg_white_gray);
		typeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkId) {
				page = 1;
				if (pullToRefreshLayout != null) {
					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
				if (nomalView.getId() == checkId) {
					type = Config.TYPE_SORT;
				} else if (hotView.getId() == checkId) {
					type = Config.TYPE_HOT;
				} else if (newView.getId() == checkId) {
					type = Config.TYPE_ALL;
				}
				tag = Config.REFRE_OTHER;
				productListSubmit();
			}
		});
	}

	/**
	 * 初始化viewpager
	 */
	private void initPagerView() {
		ballLayout.removeAllViews();
		mball = new ImageView[viewPagerList.size()];
		if (mball.length <= 1) {
			ballLayout.setVisibility(View.GONE);
		} else {
			ballLayout.setVisibility(View.VISIBLE);
		}

		for (int i = 0; i < mball.length; i++) {
			ImageView imageView = new ImageView(this);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, 1);
			if (i != 0) {
				params.leftMargin = 5;
			}
			imageView.setLayoutParams(params);
			mball[i] = imageView;
			if (i == 0) {
				mball[i].setBackgroundResource(R.drawable.android_activities_cur);
			} else {
				mball[i].setBackgroundResource(R.drawable.android_activities_bg);
			}
			ballLayout.addView(imageView);
		}
		if(mball.length<=0){
			mViewPager.setVisibility(View.GONE);
		}else{
			mViewPager.setVisibility(View.VISIBLE);
		}
		mViewPager.setTransitionEffect(TransitionEffect.Standard);
		mViewPager.setCurrentItem(0);
		ViewGroup.LayoutParams mViewPagerParams = mViewPager.getLayoutParams();
		mViewPagerParams.height = screenHeight * 1 / 4;
		mViewPager.setLayoutParams(mViewPagerParams);
		mViewPager.setOnPageChangeListener(new MyOnPageChangedListener(this,
				mball));

		// mViewPager.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if (mImageViews.length == 0 || mImageViews.length == 1)
		// return true;
		// else
		// return false;
		// }
		// });
	}

	@Override
	protected void mainSubmit() {
		actMap = new HashMap<String, String>();
		actMap.put(Config.TAG_CITYID, MyApplication.getInstance().getDbUser()
				.getCityId());
		actTask = new SubmitTask(HomeActivity.this, Config.ACT_LIST,
				ActList.class, new OnActListSubmitListener(), isShowDialog);
		actTask.execute(actMap);
	}

	/**
	 * 请求商品列表
	 */
	protected void productListSubmit() {
		productListMap = new HashMap<String, String>();
		productListMap.put(Config.TAG_CITYID, MyApplication.getInstance()
				.getDbUser().getCityId());
		productListMap.put(Config.TAG_TYPE, type);
		productListMap.put(Config.TAG_PAGE, page + "");
		productListMap.put(Config.TAG_PROPERTYTYPE, "1");
		productListMap.put(Config.TAG_NODEID, "");
		productListMap.put(Config.TAG_PROPERTYID, "0");
		productListMap.put(Config.TAG_SHOPID, "");
		productListMap.put(SearchActivity.PRODUCTTITLE, "");
		if (productListTask != null) {
			productListTask.destorySelf();
		}
		productListTask = new SubmitTask(HomeActivity.this,
				Config.PRODUCT_LIST, ProductList.class,
				new ProductListSubmitListener(), isShowDialog);
		productListTask.execute(productListMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (productListTask != null) {
			productListTask.destorySelf();
		}
		if (actTask != null) {
			actTask.destorySelf();
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		this.pullToRefreshLayout = pullToRefreshLayout;
		isShowDialog = false;
		top_logoCityText.setText(MyApplication.getInstance().getDbUser()
				.getCityName());
		page = 1;
		tag = Config.REFRE_OTHER;
		// banerList.clear();
		// banerShopList.clear();
		// banerThemeList.clear();
		// viewPagerList.clear();
		mainSubmit();
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		this.pullToRefreshLayout = pullToRefreshLayout;
		isShowDialog = false;
		if (page < pageTotal) {
			page++;
			tag = Config.LOADMORE_INDEX;
			productListSubmit();
		} else {
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
			//当是金券特推的时候加载到最后一页则跳转到源头货
			if (Utile.isEqual(type, Config.TYPE_SORT)) {
				for (ActBean actBean : banerList) {
					if (actBean.getActivityName().contains("源头")) {
						ActivityAction(actBean);
					}
				}
			}else
				showToast(R.string.msg_more_to_refre);
		}
	}

	/**
	 * 提交获取活动列表
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnActListSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ActList actList = (ActList) result.getObject();
			viewPagerList = actList.getActivityList1();
			banerList = actList.getActivityList2();
			banerShopList = actList.getActivityList3();
			banerThemeList = actList.getActivityList4();
			banerAcitivityList = actList.getActivityList5();
			productListSubmit();
		}
	}

	/**
	 * 提交获取商品列表
	 * 
	 * @author Administrator
	 * 
	 */
	private class ProductListSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			isShowDialog = true;
			productListBean = ((ProductList) result.getObject());
			productList = productListBean.getProductList();
			pageTotal = Integer.parseInt(productListBean.getData().get(0)
					.getPageTotal());
			if (tag == Config.DRAG_INDEX) {
				topImg.setVisibility(View.GONE);
				allProductList.clear();
				allProductList.addAll(productList);
				adapter.setProductList(allProductList);
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			} else if (tag == Config.LOADMORE_INDEX) {
				topImg.setVisibility(View.VISIBLE);
				allProductList.addAll(productList);
				adapter.setProductList(allProductList);
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			} else if (tag == Config.REFRE_OTHER) {
				topImg.setVisibility(View.GONE);
				allProductList.clear();
				allProductList.addAll(productList);
				adapter.setProductList(allProductList);
				banerAdapter.setAcBeans(banerList);
				banerShopAdapter.setAcBeans(banerShopList);
				banerThemeAdapter.setAcBeans(banerThemeList);
				myPagerAdapter.setActBeans(viewPagerList);
				setDataToView();
				if (pullToRefreshLayout != null) {
					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			} else {
				initMyView();
				topImg.setVisibility(View.GONE);
				allProductList.addAll(productList);
				adapter = new ProductCalssAdapter(HomeActivity.this,
						allProductList);
				refreView.setAdapter(adapter);
				new CityUtile(HomeActivity.this, mHandler).sinaSubmit();
			}
			refreView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(HomeActivity.this,
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
	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_center_class:
			intent.setClass(HomeActivity.this, SearchActivity.class);
			bundle.putSerializable(SearchActivity.CHILD, new Child("", "", "",
					"1"));
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.home_center_searchimg:
			intent.setClass(HomeActivity.this, ProductClassActivity.class);
			if (search.getText().toString().replace(" ", "").length() > 0) {
				intent.putExtra(SearchActivity.PRODUCTTITLE, search.getText()
						.toString());
			} else {
				intent.putExtra(SearchActivity.PRODUCTTITLE, search.getHint()
						.toString());
			}
			startActivity(intent);
			break;
		case R.id.logo_citytext:
			intent.setClass(HomeActivity.this, CityActivity.class);
			intent.putExtra(LOCOALCITY, MyApplication.getInstance().getDbUser()
					.getCityName());
			getParent().startActivityForResult(intent, 2);
			break;
		case R.id.home_center_topimg:
			topImg.setVisibility(View.GONE);
			if (abScrollView != null) {
				abScrollView.smoothScrollTo(0, scrollY);
			}
			break;
		case R.id.home_center_qrscan:
			intent.setClass(HomeActivity.this, MipcaActivityCapture.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		Logger.e("hhy", "--------------"+bundle.toString());
	}

	/**
	 * 广告选项监听
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnItemClickListenerImpl implements OnItemClickListener {
		private List<ActBean> list;
		private Intent itemIntent = new Intent();
		private Bundle itemBundle = new Bundle();
		public OnItemClickListenerImpl(List<ActBean> list) {
			this.list = list;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ActBean actBean = list.get(position);
			itemBundle.clear();
			if (actBean != null) {
				if (actBean.getNote() != null
						&& Utile.isEqual(actBean.getNote(), "GSC")) {
					itemIntent.setClass(HomeActivity.this, BanerActivity.class);
					itemBundle.putSerializable(BanerActivity.BANER_LIST,
							banerAcitivityList);
					itemIntent.putExtras(itemBundle);
					HomeActivity.this.startActivity(itemIntent);
				} else if (actBean.getSubjID() != null
						&& actBean.getPropertyTypeID() != null
						&& Integer.parseInt(actBean.getSubjID()) > 0
						&& Integer.parseInt(actBean.getPropertyTypeID()) > 0) {
					itemIntent.setClass(HomeActivity.this,
							ProductClassActivity.class);
					itemBundle.putSerializable(SearchActivity.CHILD, new Child(
							actBean.getSubjID(), "", actBean.getActivityName(),
							actBean.getPropertyTypeID()));
					itemIntent.putExtras(itemBundle);
					HomeActivity.this.startActivity(itemIntent);
				} else if (actBean.getNodeID() != null
						&& actBean.getPropertyTypeID() != null
						&& Integer.parseInt(actBean.getNodeID()) > 0
						&& Integer.parseInt(actBean.getPropertyTypeID()) > 0) {
					itemIntent.setClass(HomeActivity.this,
							ProductClassActivity.class);
					itemBundle.putSerializable(SearchActivity.CHILD, new Child(
							actBean.getNodeID(), "", actBean.getActivityName(),
							actBean.getPropertyTypeID()));
					itemIntent.putExtras(itemBundle);
					HomeActivity.this.startActivity(itemIntent);
				} else if (actBean.getPropertyID() != null
						&& Integer.parseInt(actBean.getPropertyID()) > 0) {
					itemIntent.setClass(HomeActivity.this,
							ProductClassActivity.class);
					itemBundle.putSerializable(SearchActivity.CHILD, new Child("",
							actBean.getPropertyID(), actBean.getPropertyName(),
							"1"));
					itemIntent.putExtras(itemBundle);
					HomeActivity.this.startActivity(itemIntent);
				} else if (actBean.getProductID() != null
						&& Integer.parseInt(actBean.getProductID()) > 0) {
					itemIntent.setClass(HomeActivity.this, ProductActivity.class);
					itemIntent.putExtra(Config.TAG_PUODUCTID,
							actBean.getProductID());
					HomeActivity.this.startActivity(itemIntent);
				} else if (actBean.getMobileURL() != null
						&& !Utile.isEqual(actBean.getMobileURL(), "-1")) {
					itemIntent.setClass(HomeActivity.this, WebViewAcitivity.class);
					itemIntent.putExtra(PostOutActivity.URLSTRING,
							actBean.getMobileURL());
					HomeActivity.this.startActivity(itemIntent);
				} else {
					showToast(R.string.msg_build);
				}
			}
		}

	}
	
	/**
	 * 跳转到源头货界面
	 * @param actBean
	 */
	private void ActivityAction(ActBean actBean) {
		Intent itemIntent = new Intent();
		Bundle itemBundle = new Bundle();
		itemBundle.clear();
		if (actBean != null) {
			if (actBean.getSubjID() != null
					&& actBean.getPropertyTypeID() != null
					&& Integer.parseInt(actBean.getSubjID()) > 0
					&& Integer.parseInt(actBean.getPropertyTypeID()) > 0) {
				itemIntent.setClass(HomeActivity.this,
						ProductClassActivity.class);
				itemBundle.putSerializable(SearchActivity.CHILD, new Child(
						actBean.getSubjID(), "", actBean.getActivityName(),
						actBean.getPropertyTypeID()));
				itemIntent.putExtras(itemBundle);
				HomeActivity.this.startActivity(itemIntent);
			} else if (actBean.getNodeID() != null
					&& actBean.getPropertyTypeID() != null
					&& Integer.parseInt(actBean.getNodeID()) > 0
					&& Integer.parseInt(actBean.getPropertyTypeID()) > 0) {
				itemIntent.setClass(HomeActivity.this,
						ProductClassActivity.class);
				itemBundle.putSerializable(SearchActivity.CHILD, new Child(
						actBean.getNodeID(), "", actBean.getActivityName(),
						actBean.getPropertyTypeID()));
				itemIntent.putExtras(itemBundle);
				HomeActivity.this.startActivity(itemIntent);
			} else if (actBean.getPropertyID() != null
					&& Integer.parseInt(actBean.getPropertyID()) > 0) {
				itemIntent.setClass(HomeActivity.this,
						ProductClassActivity.class);
				itemBundle.putSerializable(SearchActivity.CHILD, new Child("",
						actBean.getPropertyID(), actBean.getPropertyName(),
						"1"));
				itemIntent.putExtras(itemBundle);
				HomeActivity.this.startActivity(itemIntent);
			}else {
				showToast(R.string.msg_build);
			}
		}
	}
}
