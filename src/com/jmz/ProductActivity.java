package com.jmz;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jmz.adapter.ProductPagerAdapter;
import com.jmz.bean.DBOrder;
import com.jmz.bean.Product;
import com.jmz.bean.ProductAttrGroupList;
import com.jmz.bean.ProductInfo;
import com.jmz.bean.ProductInfoData;
import com.jmz.bean.RewardList;
import com.jmz.bean.SpreadInfo;
import com.jmz.bean.UserRewardBean;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.listener.MyOnPageChangedListener;
import com.jmz.listener.OnProductPageChangedListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.AddCarUtile;
import com.jmz.uitl.Config;
import com.jmz.uitl.DateUtile;
import com.jmz.uitl.FavUtile;
import com.jmz.uitl.Logger;
import com.jmz.uitl.Utile;
import com.jmz.uitl.YMShare;
import com.jmz.view.JazzyViewPager;
import com.jmz.view.JazzyViewPager.TransitionEffect;
import com.jmz.view.ProductScrollView;
import com.jmz.view.PullToRefreshLayout;
import com.jmz.view.PullToRefreshLayout.OnRefreshListener;
import com.jmz.view.SelectAttributePopupWindow;
import com.jmz.view.ShopContactsPopupWindow;
import com.jmz.view.TileTextView;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 金券详情
 * 
 * @author hhy 2014-7-9
 * 
 */
public class ProductActivity extends ParentActivity implements OnClickListener,
		OnRefreshListener {
	public static final String LOGTAG = "ProductActivity";
	public static final String TAG_SELCETNUMBERED = "SelectNumbered";
	public static final String TAG_PRODUCT = "product";
	public static final String TAG_ACIVITY = "activity";
	private TextView shareBtn, orderBtn;
	private TextView gotoShop;
	private TextView productTitle, productPrice, fareNumber,
			shareNumber, lookNumber, soldNumber, tuwen,
			shopContacets;
	private TextView productInfo;

	private JazzyViewPager mViewPager;
	private ImageView[] mball;
	private TextView textLayout = null;
	private ProductPagerAdapter productPagerAdapter;

	private String shopId;
	private Product product;
	private ProductInfoData productInfoData;
	private String referrerUserID;
	private TextView shareGetMoney;
	private TextView buyGetMoney;
	private LinearLayout rewardLayout;
	private TextView userReward;
	private TextView remainTotal;
	private TextView rewardTotle;
	private YMShare ymShare;
	private String productId;
	private TileTextView favBtn;
	private int totalStock = 0;
	private TextView addCarBtn;
	private SelectAttributePopupWindow attributePopupWindow;
	// 倒计时
	private TextView countdownTitle, countdownHour, countdownMinte,
			countdownSecond;
	private LinearLayout countdownLayout;
	private DecimalFormat df;
	private long hour, minte, second;
	// private int currentState = 0;
	private SubmitTask productInfoTask;
	private SubmitTask productAttrTask;
	private SubmitTask userRewardTask;
	private SubmitTask spreadInfoTask;
	private TextView selectAttr;
	private TextView evaluation;
	private TextView oldPrice;
	private ProductAttrGroupList productGroupList;
	private TextView stock;
	private TextView validTime;
	private PullToRefreshLayout refreLayout, pullToRefreshLayout;
	private ProductScrollView scrollView;

	private Handler phandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			String attr = msg.getData().getString(attributePopupWindow.RETURNSELECTEDATTR);
			selectAttr.setText(attr);
			
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadiagle();
		setTittleText(R.string.product_title);
		productId = getIntent().getStringExtra(Config.TAG_PUODUCTID);
		referrerUserID = getIntent().getStringExtra(Config.TAG_REFERRERUSERID);
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		String pattern = "00";
		df = new DecimalFormat(pattern);
		mainSubmit();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (pullToRefreshLayout != null) {
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		spreadInfoSubmit();
		if (mController != null) {
			/** 使用SSO授权必须添加如下代码 */
			UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
					requestCode);
			if (ssoHandler != null) {
				ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			}
			if (requestCode == 2 && resultCode == 2) {
				ymShare = new YMShare(ProductActivity.this, product,
						mController);
				ymShare.show();
			}
			if (requestCode == 3 && resultCode == 2) {
				FavUtile favUtile = new FavUtile(ProductActivity.this, null);
				favUtile.submit(1, productId);
			}
			if (requestCode == 4 && resultCode == 2) {
				intent = new Intent(ProductActivity.this, CarActivity.class);
				startActivity(intent);
			}
		}
	}

	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initcenterView(LayoutInflater.from(ProductActivity.this).inflate(
				R.layout.activity_product_center, null));
		initbottomView(LayoutInflater.from(ProductActivity.this).inflate(
				R.layout.activity_product_bottom, null));
		top_Car.setVisibility(View.VISIBLE);
		top_Home.setVisibility(View.VISIBLE);
		refreLayout = (PullToRefreshLayout) findViewById(R.id.product_center_refrelayout);
		scrollView = (ProductScrollView) findViewById(R.id.product_center_scrollview);
		// 倒计时控件
		countdownLayout = (LinearLayout) findViewById(R.id.product_center_countdown);
		countdownTitle = (TextView) findViewById(R.id.product_center_countdown_title);
		countdownHour = (TextView) findViewById(R.id.product_center_countdown_hour);
		countdownMinte = (TextView) findViewById(R.id.product_center_countdown_minte);
		countdownSecond = (TextView) findViewById(R.id.product_center_countdown_second);
		//
		mViewPager = (JazzyViewPager) findViewById(R.id.product_center_viewpager);
		textLayout = (TextView) findViewById(R.id.product_center_ball);
		productTitle = (TextView) findViewById(R.id.product_center_title);
		productPrice = (TextView) findViewById(R.id.product_center_price);
		oldPrice = (TextView) findViewById(R.id.product_center_oldprice);
		favBtn = (TileTextView) findViewById(R.id.product_center_fav);
		fareNumber = (TextView) findViewById(R.id.product_center_fareprice);
		soldNumber = (TextView) findViewById(R.id.product_center_soldnumber);
		shareNumber = (TextView) findViewById(R.id.product_center_sharenumber);
		lookNumber = (TextView) findViewById(R.id.product_center_looknumber);
		stock = (TextView) findViewById(R.id.product_center_stock);
		validTime = (TextView) findViewById(R.id.product_center_validtime);
		shareGetMoney = (TextView) findViewById(R.id.product_sharegetmoney);
		buyGetMoney = (TextView) findViewById(R.id.product_buygetmoney);
		rewardLayout = (LinearLayout) findViewById(R.id.product_center_reward_layout);
		rewardLayout.setVisibility(View.GONE);
		rewardTotle = (TextView) findViewById(R.id.product_center_reward_Total);
		userReward = (TextView) findViewById(R.id.product_center_user_reward);
		remainTotal = (TextView) findViewById(R.id.product_center_remain_total);

		selectAttr = (TextView) findViewById(R.id.product_center_selectattr);
		evaluation = (TextView) findViewById(R.id.product_center_evaluation);
		tuwen = (TextView) findViewById(R.id.product_center_tuwen);

		productInfo = (TextView) findViewById(R.id.product_center_productinfo);

		shopContacets = (TextView) findViewById(R.id.product_center_kefu);
		gotoShop = (TextView) findViewById(R.id.product_center_shop);

		shareBtn = (TextView) findViewById(R.id.product_bottom_share);
		addCarBtn = (TextView) findViewById(R.id.product_bottom_showaddcar);
		orderBtn = (TextView) findViewById(R.id.product_bottom_order);
	}

	/**
	 * 初始化viewpager
	 */
	private void initPagerView() {
		if (product.getProductImageUrlsList().size() <= 0) {
			product.getProductImageUrlsList().add(product.getImageUrl());
		}
		textLayout.setText("1/" + product.getProductImageUrlsList().size());
		mViewPager.setTransitionEffect(TransitionEffect.Standard);
		mViewPager.setCurrentItem(0);
		ViewGroup.LayoutParams mViewPagerParams = mViewPager.getLayoutParams();
		mViewPagerParams.height = screenWidth;
		mViewPager.setLayoutParams(mViewPagerParams);
		mViewPager.setOnPageChangeListener(new OnProductPageChangedListener(
				this, textLayout, product.getProductImageUrlsList().size()));

		productPagerAdapter = new ProductPagerAdapter(ProductActivity.this,
				mViewPager, product.getProductImageUrlsList());
		mViewPager.setAdapter(productPagerAdapter);
	}

	/**
	 * 提交获取商品信息
	 */
	@Override
	protected void mainSubmit() {
		if (productId != null) {
			HashMap<String, String> productInfoMap = new HashMap<String, String>();
			productInfoMap.put(Config.TAG_PUODUCTID, productId);
			productInfoTask = new SubmitTask(this, Config.PRODUCT_INFO,
					ProductInfo.class, new ProductInfoSubmitListener(), false);
			productInfoTask.execute(productInfoMap);
		}
	}

	/**
	 * 提交获取商品属性
	 */
	protected void attrSubmit() {
		HashMap<String, String> productAttrMap = new HashMap<String, String>();
		productAttrMap.put(Config.TAG_PUODUCTID, productId);
		productAttrTask = new SubmitTask(this, Config.PRODUCT_ATTR,
				ProductAttrGroupList.class, new ProductAttrSubmitListener(),
				false);
		productAttrTask.execute(productAttrMap);
	}
	
	/**
	 * 获取用户的获奖信息
	 */
	protected void userRewardSubmit() {
		if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
			HashMap<String, String> userRewardMap = new HashMap<String, String>();
			userRewardMap.put(Config.TAG_PUODUCTID, productId);
			userRewardTask = new SubmitTask(this, Config.USER_REWARD,
					UserRewardBean.class, new UserRewardSubmitListener(),
					false);
			userRewardTask.execute(userRewardMap);
		}
	}
	
	/**
	 * 获取产品是否是参加活动的信息
	 */
	protected void spreadInfoSubmit() {
		HashMap<String, String> productInfoMap = new HashMap<String, String>();
		productInfoMap.put(Config.TAG_PUODUCTID, productId);
		userRewardTask = new SubmitTask(this, Config.SPREAD_INFO,
				ProductInfo.class, new ProductInfoSubmitListenter(),
				false);
		userRewardTask.execute(productInfoMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (productInfoTask != null) {
			productInfoTask.destorySelf();
		}
		if (productAttrTask != null) {
			productAttrTask.destorySelf();
		}
		if (userRewardTask != null) {
			userRewardTask.destorySelf();
		}
		if (spreadInfoTask != null) {
			spreadInfoTask.destorySelf();
		}
	}
	
	/**
	 * 设置用户奖品信息
	 * @param SpreadTotal 被点赞的数量
	 * @param rewardTotal 还剩下多少人
	 */
	private void setUserRewardInfo(String spreadTotal, String rewardTotal) {	
		userReward.setText("查看信息的好友" + spreadTotal + "人");
		SpannableStringBuilder builder = new SpannableStringBuilder(userReward.getText().toString());  
		//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色  
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED); 
		builder.setSpan(redSpan, 7, userReward.getText().toString().length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		userReward.setText(builder);  
		
		remainTotal.setText("，还差" + rewardTotal + "人");
		SpannableStringBuilder builder1 = new SpannableStringBuilder(remainTotal.getText().toString());  
		//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色  
		ForegroundColorSpan redSpan1 = new ForegroundColorSpan(R.color.small_blue); 
		builder1.setSpan(redSpan1, 3, remainTotal.getText().toString().length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		remainTotal.setText(builder1);  
		
		/*if (amount == null) {
			priceDiscount.setText("");
		}else {
			BigDecimal discount = new BigDecimal(amount);
			priceDiscount.setText("，将获得" + (discount.floatValue()*10) + "折的优惠");
			priceDiscount.setVisibility(View.VISIBLE);
			SpannableStringBuilder builder1 = new SpannableStringBuilder(priceDiscount.getText().toString());  
			//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色  
			ForegroundColorSpan redSpan1 = new ForegroundColorSpan(Color.RED); 
			builder1.setSpan(redSpan1, 4, priceDiscount.getText().toString().length() - 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			priceDiscount.setText(builder1);  
		}*/
	}

	/**
	 * 为组件赋值
	 * 
	 * @param product
	 */
	public void setDateToView() {

		mViewPager.setOnClickListener(this);
		favBtn.setOnClickListener(this);
		selectAttr.setOnClickListener(this);
		evaluation.setOnClickListener(this);
		tuwen.setOnClickListener(this);
		gotoShop.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		top_Car.setOnClickListener(this);
		shopContacets.setOnClickListener(this);
		refreLayout.setOnRefreshListener(this);

		initPagerView();

		//setTittleText(product.getProductTitle());
		// LayoutParams params = mViewPager.getLayoutParams();
		// params.width = screenWidth;
		// params.height = screenWidth;
		// mViewPager.setLayoutParams(params);
		// ImageLoader.getInstance().displayImage(
		// Config.JMZG + product.getImageUrl(), productpager,
		// MyApplication.getInstance().getOptions());

		shopId = product.getShopID();
		productTitle.setText(product.getProductTitle());

		if (new BigDecimal(product.getMarketPrice()).doubleValue() > 0) {
			oldPrice.setText(getString(R.string.myorderinfo_oldprice)
					+ getString(R.string.yuanicon) + product.getMarketPrice().trim());
			oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			productPrice.setText(getString(R.string.myorderinfo_shopprice) + getString(R.string.yuanicon)
					+ product.getShopPrice());
		} else {
			oldPrice.setText("抵现金：" + getString(R.string.yuanicon)
					+ product.getCoupon());
			productPrice.setText("定金" + getString(R.string.yuanicon)
					+ product.getShopPrice());
			fareNumber.setVisibility(View.GONE);
		}
		
//		if (new BigDecimal(product.getFarePrice()).doubleValue() <= 0) {
//			fareNumber.setText(getString(R.string.free_postage));
//		} else {
//			fareNumber.setText(getString(R.string.fareprice)
//					+ getString(R.string.yuanicon) + product.getFarePrice());
//		}
		
		soldNumber.setText(getString(R.string.allpay)
				+ productInfoData.getOrderTotal().trim() + getString(R.string.jian));
		shareNumber.setText(getString(R.string.allshare)
				+ productInfoData.getShareProductTotal().trim()
				+ getString(R.string.ren));
		lookNumber.setText(getString(R.string.alllook)
				+ product.getClickTotal().trim() + "次");
		
		totalStock = Integer.parseInt(product.getProductNumber())
				- Integer.parseInt(productInfoData.getOrderTotal());
		product.setAllAttrTotalStock(totalStock);
		if (totalStock > 0) {
			stock.setText(getString(R.string.myorderinfo_stock) + totalStock
					+ getString(R.string.jian));
			selectAttr.setVisibility(View.VISIBLE);
		} else {
			stock.setText(getString(R.string.myorderinfo_stock) + "已售罄");
			selectAttr.setVisibility(View.GONE);
		}
		validTime.setText(product.getOpenDate() + "至" + product.getCloseDate());
		fareNumber.setText(getString(R.string.fareprice) + product.getShopFarePriceStr());

		if (Utile.isEqual(product.getIsProductReward(), "True")) {
			totalStock = Integer.parseInt(product.getProductNumber())
		    - Integer.parseInt(productInfoData.getOrderTotal());
			product.setAllAttrTotalStock(totalStock);
			if (totalStock > 0) {
				stock.setText(getString(R.string.myorderinfo_stock) + totalStock
						+ getString(R.string.jian));
				selectAttr.setVisibility(View.VISIBLE);
			} else {
				stock.setText(getString(R.string.myorderinfo_stock) + "已送完");
				selectAttr.setVisibility(View.GONE);
			}
		}
		// evaluation.setText("");
		// Spannable WordtoSpan = new SpannableString("图文详情       建议WiFi环境下使用");
		// WordtoSpan.setSpan(new AbsoluteSizeSpan(17, true), 0, 4,
		// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// WordtoSpan.setSpan(new AbsoluteSizeSpan(13, true), 4,
		// WordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// WordtoSpan.setSpan(
		// new ForegroundColorSpan(getResources().getColor(
		// R.color.deeporange)), 0, 4,
		// Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		tuwen.setText("图文详情");
		productInfo.setText(Html.fromHtml(product.getInstruction()));
//		productInfo.loadDataWithBaseURL(null, product.getInstruction(), "text/html", "utf-8", null); 
//		productInfo.getSettings().setJavaScriptEnabled(true);
		if (new BigDecimal(product.getCommission()).doubleValue() > 0) {
			shareGetMoney.setText("奖金\n¥" + product.getShareMoney());
			buyGetMoney.setText("奖金\n¥" + product.getBuyMoney());
			shareBtn.setText("播金券");
		} else {
			shareGetMoney.setVisibility(View.GONE);
			buyGetMoney.setVisibility(View.GONE);
			shareBtn.setText("分享");
			//zwt
			//当没有播金券的时候播券的已播金券变为已浏览，已浏览隐藏   
			shareNumber.setText(getString(R.string.alllook)
					+ product.getClickTotal().trim());
			lookNumber.setVisibility(View.INVISIBLE);
		}
		
		Logger.i("zwt", "---->productPrice:" + productPrice.getText() + " soldNumber：" + soldNumber.getText());
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				scrollView.smoothScrollTo(0, 0);
			}
		});

		if (totalStock <= 0) {
			product.setCurrentState(Product.PRODUCT_CURR_STATE_NO_STOCK);
		} else {
			if (DateUtile.betweenTime(product.getServerTime(),
					product.getOpenDate(), DateUtile.SECOND) < 0) {
				product.setCurrentState(Product.PRODUCT_CURR_STATE_WAIT);
			} else if (DateUtile.betweenTime(product.getServerTime(),
					product.getCloseDate(), DateUtile.SECOND) > 0) {
				product.setCurrentState(Product.PRODUCT_CURR_STATE_FISHED);
			} else {
				product.setCurrentState(Product.PRODUCT_CURR_STATE_BUY);
			}
		}
		countDown();
	}

	/**
	 * 倒计时
	 */
	private void countDown() {
		if (DateUtile.betweenTime(product.getServerTime(),
				product.getOpenDate(), DateUtile.SECOND) < 0) {
			second = DateUtile.betweenTime(product.getOpenDate(),
					product.getServerTime(), DateUtile.SECOND) % 60;
			minte = DateUtile.betweenTime(product.getOpenDate(),
					product.getServerTime(), DateUtile.MINUTE) % 60;
			hour = DateUtile.betweenTime(product.getOpenDate(),
					product.getServerTime(), DateUtile.HOUR);
		} else if (DateUtile.betweenTime(product.getServerTime(),
				product.getCloseDate(), DateUtile.SECOND) < 0) {
			second = DateUtile.betweenTime(product.getCloseDate(),
					product.getServerTime(), DateUtile.SECOND) % 60;
			minte = DateUtile.betweenTime(product.getCloseDate(),
					product.getServerTime(), DateUtile.MINUTE) % 60;
			hour = DateUtile.betweenTime(product.getCloseDate(),
					product.getServerTime(), DateUtile.HOUR);
		}
		countdownSecond.setText("" + df.format(second));
		countdownMinte.setText("" + df.format(minte));
		countdownHour.setText("" + df.format(hour));
		Message message = handler.obtainMessage(1);

		switch (product.getCurrentState()) {
		case 0:
			// countdownLayout.setVisibility(View.VISIBLE);
			// countdownTitle.setText(R.string.product_timetostart);
			orderBtn.setEnabled(false);
			addCarBtn.setEnabled(false);
			orderBtn.setText(R.string.product_wait);
			handler.sendMessageDelayed(message, 1000);
			break;
		case 1:
			// countdownLayout.setVisibility(View.GONE);
			orderBtn.setEnabled(false);
			addCarBtn.setEnabled(false);
			orderBtn.setText(R.string.product_fished);
			break;
		case 2:
			// countdownLayout.setVisibility(View.VISIBLE);
			// countdownTitle.setText(R.string.product_timetoend);
			orderBtn.setEnabled(true);
			addCarBtn.setEnabled(true);
			addCarBtn.setOnClickListener(this);
			orderBtn.setOnClickListener(this);
			orderBtn.setText(R.string.product_pay);
			addCarBtn.setText(R.string.product_add_car);
			handler.sendMessageDelayed(message, 1000);
			break;
		case 3:
			// countdownLayout.setVisibility(View.GONE);
			orderBtn.setEnabled(false);
			addCarBtn.setEnabled(false);
			orderBtn.setText(R.string.product_no_stock);
			break;

		default:
			break;
		}

	}

	/**
	 * 秒杀倒计时
	 */
	final Handler handler = new Handler() {

		public void handleMessage(Message msg) { // handle message
			switch (msg.what) {
			case 1:
				second--;
				if (second <= 0 && minte > 0) {
					minte--;
					second = 59;
				}
				if (minte <= 0 && hour > 0) {
					hour--;
					minte = 59;
				}
				if (hour <= 0 && minte <= 0 && second <= 0) {
					if (product.getCurrentState() == Product.PRODUCT_CURR_STATE_WAIT) {
						product.setCurrentState(Product.PRODUCT_CURR_STATE_BUY);
					} else if (product.getCurrentState() == Product.PRODUCT_CURR_STATE_BUY) {
						product.setCurrentState(Product.PRODUCT_CURR_STATE_FISHED);
					} else {
						countdownLayout.setVisibility(View.GONE);
					}
					countDown();
				} else {
					Message message = handler.obtainMessage(1);
					handler.sendMessageDelayed(message, 1000);
				}

				countdownSecond.setText("" + df.format(second));
				countdownMinte.setText("" + df.format(minte));
				countdownHour.setText("" + df.format(hour));
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 响应获取商品信息
	 * 
	 * @author Administrator
	 * 
	 */
	private class ProductInfoSubmitListener implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			initMyView();
//			spreadInfoSubmit();
			ProductInfo productInfo = (ProductInfo) result.getObject();
			//Product spread = (Product) result.getObject();
			if (productInfo.getProductInfo() != null
					&& productInfo.getProductInfo().size() > 0
					&& !productInfo.getProductInfo().isEmpty()) {
				product = productInfo.getProductInfo().get(0);
//				Log.d("jmz", product+"");
				product.setReferrerUserID(referrerUserID);
				productInfoData = productInfo.getData().get(0);
				if (product != null && productInfoData != null) {
					setDateToView();
					attrSubmit();
				} else {
					ProductActivity.this.finish();
					showToast(getString(R.string.product_exist));
				}
			} else {
				ProductActivity.this.finish();
				showToast(getString(R.string.product_exist));
			}
//			if (Utile.isEqual(product.getIsProductReward(), "True")) {
//							totalStock = Integer.parseInt(product.getProductNumber())
//						    - Integer.parseInt(productInfoData.getOrderTotal());
//							product.setAllAttrTotalStock(totalStock);
//			}
			
		}
	}

	/**
	 * 响应获取商品属性
	 * 
	 * @author Administrator
	 * 
	 */
	private class ProductAttrSubmitListener implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			productGroupList = (ProductAttrGroupList) result.getObject();
			attributePopupWindow = new SelectAttributePopupWindow(
					ProductActivity.this,phandler, product, productGroupList);
			if (pullToRefreshLayout != null) {
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
			if (productGroupList != null
					&& productGroupList.getGetAttr() != null
					&& productGroupList.getGetAttr().size() <= 0) {
				selectAttr.setVisibility(View.GONE);
			}
		}
	}
	
	/**
	 * 响应获取用户获奖信息
	 * @author Administrator
	 *
	 */
	private class UserRewardSubmitListener implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			// TODO Auto-generated method stub
			UserRewardBean userReward = (UserRewardBean) result.getObject();
			if (userReward != null) {
				String rewardTotal  = "0";
				for (RewardList list : userReward.getRewardList()) {
					if (Utile.isEqual(list.getRewardType(), "Product")) {
						rewardTotal = list.getSpreadRemainTotal();
					}
				}
				
				setUserRewardInfo(userReward.getSpreadTotal(), rewardTotal);
			}
		}
		
	}
	
	/**
	 * 响应获取产品是否是参加活动的信息
	 * @author Administrator
	 *
	 */
	private class ProductInfoSubmitListenter implements OnSubmitListener {

		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			// TODO Auto-generated method stub
			Product spread = (Product) result.getObject();
			if (spread != null) {
				if(Utile.isEqual(spread.getIsSpreadProduct(), "True")) {
					userRewardSubmit();
					shareNumber.setText("已领取：" + spread.getSpreadRewardTotal() + "人");
					soldNumber.setText(stock.getText());
					stock.setText(lookNumber.getText());
					lookNumber.setVisibility(View.GONE);
					fareNumber.setVisibility(View.GONE);
					rewardLayout.setVisibility(View.VISIBLE);
					rewardTotle.setText(spread.getSpreadCheckTotal() + "人");
					SpannableStringBuilder builder = new SpannableStringBuilder(rewardTotle.getText().toString());  
					//ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色  
					ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED); 
					builder.setSpan(redSpan, 0, rewardTotle.getText().toString().length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
					rewardTotle.setText(builder);  
				}
			}
		}
		
	}

	private Intent intent = new Intent();
	private Bundle bundle = new Bundle();
	private AddCarUtile addCarUtile;

	@Override
	public void onClick(View view) {
		bundle.putSerializable(TAG_PRODUCT, product);
		switch (view.getId()) {
		case R.id.product_bottom_share:
			if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
				ymShare = new YMShare(ProductActivity.this, product,
						mController);
				ymShare.show();
			} else {
				intent.setClass(ProductActivity.this, LoginActivity.class);
				bundle.putString(ProductActivity.TAG_ACIVITY, LOGTAG);
				intent.putExtras(bundle);
				startActivityForResult(intent, 2);
			}
			break;
		case R.id.product_center_shop:
			intent = new Intent(ProductActivity.this, ShopActivity.class);
			intent.putExtra(Config.TAG_SHOPID, shopId);
			startActivity(intent);
			break;
		case R.id.product_center_kefu:
			ShopContactsPopupWindow contactsPopupWindow = new ShopContactsPopupWindow(
					ProductActivity.this, shopId);
			contactsPopupWindow.showAtLocation(base_bottomView, Gravity.BOTTOM,
					0, 0);
			break;
		case R.id.top_car:
			if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
				intent = new Intent(ProductActivity.this, CarActivity.class);
				startActivity(intent);
			} else {
				intent.setClass(ProductActivity.this, LoginActivity.class);
				bundle.putInt(ProductActivity.TAG_SELCETNUMBERED, 1);
				bundle.putString(ProductActivity.TAG_ACIVITY,
						CarActivity.LOGTAG);
				intent.putExtras(bundle);
				startActivityForResult(intent,4);
			}
			break;
		case R.id.product_bottom_showaddcar:
			if (productGroupList != null
					&& productGroupList.getGetAttr() != null
					&& productGroupList.getGetAttr().size() > 0) {
				attributePopupWindow.showAtLocation(base_bottomView,
						Gravity.BOTTOM, 0, 0);
			} else {
				if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
					addCarUtile = new AddCarUtile(ProductActivity.this, product);
					addCarUtile.addCarSubmit();
				} else {
					intent.setClass(ProductActivity.this, LoginActivity.class);
					bundle.putInt(ProductActivity.TAG_SELCETNUMBERED, 1);
					bundle.putString(ProductActivity.TAG_ACIVITY,
							ProductActivity.LOGTAG);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}

			break;
		case R.id.product_bottom_order:
		case R.id.product_center_selectattr:
			if (productGroupList != null
					&& productGroupList.getGetAttr() != null
					&& productGroupList.getGetAttr().size() > 0) {
				attributePopupWindow.showAtLocation(base_bottomView,
						Gravity.BOTTOM, 0, 0);
			} else {
				if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
					intent.setClass(ProductActivity.this, OrderActivity.class);
					bundle.putInt(ProductActivity.TAG_SELCETNUMBERED, 1);
					intent.putExtras(bundle);
					startActivity(intent);
				} else {
					intent.setClass(ProductActivity.this, LoginActivity.class);
					bundle.putInt(ProductActivity.TAG_SELCETNUMBERED, 1);
					bundle.putString(ProductActivity.TAG_ACIVITY,
							OrderActivity.LOGTAG);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
			break;
		case R.id.product_center_tuwen:
//		case R.id.product_center_img:
			intent = new Intent(ProductActivity.this, WebViewAcitivity.class);
			intent.putExtra(Config.TAG_SHOPID, shopId);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.product_center_fav:
			if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
				FavUtile favUtile = new FavUtile(ProductActivity.this, null);
				favUtile.submit(1, productId);
			} else {
				intent.setClass(ProductActivity.this, LoginActivity.class);
				bundle.putString(ProductActivity.TAG_ACIVITY, LOGTAG);
				intent.putExtras(bundle);
				startActivityForResult(intent, 3);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		this.pullToRefreshLayout = pullToRefreshLayout;
		mainSubmit();
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		// 没用到
	}

}
