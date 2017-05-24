package com.jmz;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jmz.bean.DBOrder;
import com.jmz.bean.Product;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.PayUtile;
import com.jmz.uitl.Utile;
import com.jmz.uitl.YMShare;
import com.jmz.view.SelectAttributePopupWindow;
import com.jmz.view.ShopContactsPopupWindow;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 网页加载
 * 
 * @author Administrator
 * 
 */
public class WebViewAcitivity extends ParentActivity {
	public static final String LOGTAG = "WebViewAcitivity";
	private WebView webview;
	private String webUrl;
	private ProgressBar progress;
	private TextView shareBtn, payBtn;
	private Product product;
	private HashMap<String, String> map;
	private SubmitTask shareTask;
	private YMShare ymShare;
	private String title;
	private boolean noCache = false;
	private boolean isQMFWebPay = false;
	private PopupWindow attributePopupWindow;
	private TextView showAddCarBtn;
	private String shopId = null;
	private String tradID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTittleText(R.string.app_name);
		// 设置Web视图
		initcenterView(LayoutInflater.from(WebViewAcitivity.this).inflate(
				R.layout.activity_webview_center, null));
		webview = (WebView) findViewById(R.id.webview_web);
		progress = (ProgressBar) findViewById(R.id.webview_progressbar);
		product = (Product) getIntent().getExtras().getSerializable(
				ProductActivity.TAG_PRODUCT);
		title = getIntent().getStringExtra("title");
		tradID = getIntent().getStringExtra(PayUtile.TRADEID);
		noCache = getIntent().getBooleanExtra(PostOutActivity.CACHE, false);
		webUrl = getIntent().getStringExtra(PostOutActivity.URLSTRING);
		isQMFWebPay = getIntent().getBooleanExtra(PayUtile.ISQMFWEBPAY, false);

		if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
			synCookies(this, Config.JMZGM);
		}

		setTittleText(title != null ? title : getString(R.string.app_name));
		if (isQMFWebPay) {
			base_topView.setVisibility(View.GONE);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			layoutParams.setMargins(0, 0, 0, 0);// 4个参数按顺序分别是左上右下
			webview.setLayoutParams(layoutParams);

			String chrCode = getIntent().getStringExtra(PayUtile.CHRCODE);
			String tranID = getIntent().getStringExtra(PayUtile.TRANID);
			String merSign = getIntent().getStringExtra(PayUtile.MERSIGN);
			if (chrCode.length() != 0 && tranID.length() != 0
					&& merSign.length() != 0)
				webview.loadData(createWebForm(chrCode, tranID, merSign),
						"text/html", "UTF-8");
			else
				finish();
		} else {
			// 加载需要显示的网页
			if (product != null) {
				// // 设置底部视图
				// initbottomView(LayoutInflater.from(WebViewAcitivity.this).inflate(
				// R.layout.activity_product_bottom, null));
				// shopId = getIntent().getStringExtra(Config.TAG_SHOPID);
				// shareBtn = (TextView)
				// findViewById(R.id.product_bottom_share);
				// showAddCarBtn = (TextView)
				// findViewById(R.id.product_bottom_showaddcar);
				// payBtn = (TextView) findViewById(R.id.product_bottom_order);
				// shareBtn.setOnClickListener(new OnWebClickListenerimpl());
				// showAddCarBtn.setOnClickListener(new
				// OnWebClickListenerimpl());
				// payBtn.setOnClickListener(new OnWebClickListenerimpl());
				webUrl = Config.JMZGTICKETUWEN + product.getProductID();
				setTittleText(product.getProductTitle());
				// determineProductState();
				// DBOrder d = new DBOrder(product.getImageUrl(),
				// product.getProductTitle(), product.getProductID(),
				// product.getShopID(), product.getProductTitle(), 0,
				// Float.parseFloat(product.getShopPrice()), 0.0f, 0.0f, 0);
			} else if (webUrl == null) {
				showToast(getString(R.string.webview_exist));
				finish();
			}
			webview.loadUrl(webUrl);
			// 隐藏顶部Logo和返回栏
			if ((webUrl.startsWith(Config.JMZGM)
					&& !webUrl
							.startsWith("http://m.jmzgo.com/games/ticketgames/yqc/gameset.aspx") || webUrl
						.contains("/act/phone/tuhuojie/index.html"))) {
				base_logoView.setVisibility(View.GONE);
				base_topView.setVisibility(View.GONE);
			}
		}

		// 设置可以访问文件
		webview.getSettings().setAllowFileAccess(true);
		// 设置WebView属性，能够执行Javascript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		// 设置Web视图
		webview.getSettings().setBuiltInZoomControls(true);// 双击缩放
		webview.getSettings().setUseWideViewPort(true);// 可缩放
		webview.getSettings().setLoadWithOverviewMode(true);// 自适应
		// 不使用缓存
		if (noCache) {
			webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		}
		// 设置Web视图
		webview.setWebViewClient(new MyWebViewClient());
		// 设置Web浏览器监听
		webview.setWebChromeClient(new ChromeWebViewClient());

		// mController = UMServiceFactory.getUMSocialService("com.umeng.share");

	}

	/**
	 * 同步网页的Cookie
	 * 
	 * @param context
	 * @param url
	 */
	private static void synCookies(Context context, String url) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setCookie(url, "jmzgo_m=userName="
				+ MyApplication.getInstance().getDbUser().getUserName()
				+ "&token="
				+ MyApplication.getInstance().getDbUser().getPassWordString());
		CookieSyncManager.getInstance().sync();
	}

	// /**
	// * 判断当前商品属性
	// */
	// private void determineProductState() {
	// switch (product.getCurrentState()) {
	// case 0:
	// payBtn.setEnabled(false);
	// // addCarBtn.setEnabled(false);
	// payBtn.setText(R.string.product_wait);
	// break;
	// case 1:
	// payBtn.setEnabled(false);
	// // addCarBtn.setEnabled(false);
	// payBtn.setText(R.string.product_fished);
	// break;
	// case 2:
	// payBtn.setEnabled(true);
	// // addCarBtn.setEnabled(true);
	// payBtn.setText(R.string.product_pay);
	// break;
	// case 3:
	// // addCarBtn.setEnabled(false);
	// payBtn.setEnabled(false);
	// payBtn.setText(R.string.product_no_stock);
	// break;
	//
	// default:
	// break;
	// }
	// }
	//
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// if (mController != null) {
	// /** 使用SSO授权必须添加如下代码 */
	// UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
	// requestCode);
	// if (ssoHandler != null) {
	// ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	// }
	// if (requestCode == 2 && resultCode == 2) {
	// ymShare = new YMShare(WebViewAcitivity.this, product,
	// mController);
	// ymShare.show();
	// }
	// }
	//
	// }

	private String createWebForm(String chrCode, String tranId, String merSign) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>").append("\n");
		sb.append(
				"<form action=" + "\'" + Config.QMFWEB_PAY + "\' "
						+ "method='post' id='qmfpaysubmit'>").append("\n");
		sb.append(
				"<input type='hidden' name='chrCode' value=" + "\'" + chrCode
						+ "\'" + "/>").append("\n");
		sb.append(
				"<input type='hidden' name='tranId' value=" + "\'" + tranId
						+ "\'" + "/>").append("\n");
		sb.append(
				"<input type='hidden' name='merSign' value=" + "\'" + merSign
						+ "\'" + "/>").append("\n");
		sb.append(
				"<input type='hidden' name='url' value=" + "\'"
						+ Config.QMFWEB_PAY_RETURN + "\'" + "/>").append("\n");
		sb.append("</form>").append("\n");
		sb.append("<script>")
				.append("document.forms['qmfpaysubmit'].submit();")
				.append("</script>").append("\n");
		sb.append("</html>");

		return sb.toString();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if (mController != null) {
			// if (mController.isOpenShareBoard()) {
			// mController.dismissShareBoard();
			// } else if (!mController.isOpenShareBoard()
			// && webview.canGoBack()) {
			// webview.goBack(); // goBack()表示返回WebView的上一页面
			// } else {
			// if (isFinishAvtivity) {
			// finish();
			// }else{
			// if(getParent() != null){
			// getParent().onKeyDown(keyCode, event);
			// }
			// }
			// }
			//
			// }
			if (webview.canGoBack()) {
				webview.goBack(); // goBack()表示返回WebView的上一页面
			} else {
				if (isFinishAvtivity) {
					finish();
				} else {
					if (getParent() != null) {
						getParent().onKeyDown(keyCode, event);
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 创建监听
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			progress.setVisibility(View.VISIBLE);
			Log.i("WebViewActivity",
					"shouldOverrideUrlLoading-->getCurrentUrl:" + url);
			if (url.startsWith(Config.QMFWEB_PAY_RETURN)) {
				qmfPayResultUrl(url);
			} else if (Utile.isEqual(url, "http://m.jmzgo.com/")
					|| (Utile.isEqual(url, "http://m.jmzgo.com/index.aspx"))) {
				finish();
			} else if (url.startsWith("http://m.jmzgo.com/product/info.aspx")) {
				StringTokenizer token = new StringTokenizer(url,
						"http://m.jmzgo.com/product/info.aspx?id=&");
				Intent intent = new Intent();
				intent.setClass(WebViewAcitivity.this, ProductActivity.class);
				intent.putExtra(Config.TAG_PUODUCTID, token.nextToken());
				WebViewAcitivity.this.startActivity(intent);
			} else if (url.contains("/my/order/ticket_list.aspx")) {
				Intent intent = new Intent();
				intent.setClass(WebViewAcitivity.this, MyTicketActivity.class);
				WebViewAcitivity.this.startActivity(intent);
			} else
				view.loadUrl(url);

			return true;
		}

	}

	/**
	 * 浏览器监听
	 * 
	 * @author Administrator
	 * 
	 */
	private class ChromeWebViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			progress.setProgress(newProgress);
			// if (newProgress == 100) {
			// progress.setVisibility(View.GONE);
			// } else {
			// progress.setVisibility(View.VISIBLE);
			// }
			progress.setVisibility(newProgress == 100 ? View.GONE
					: View.VISIBLE);
			super.onProgressChanged(view, newProgress);
		}

	}

	/**
	 * 全民付返web支付后返回的结果
	 */
	private void qmfPayResultUrl(String url) {
		if (!url.contains("merchantOrderId")) {
			finish();
			return;
		}

		Pattern pattern = Pattern.compile("(^|&|\\?)+merchantOrderId=+([^&]*)");//
		Matcher matcher = pattern.matcher(url);
		while (matcher.find()) {
			String merchantorderId = matcher.group();
			String returntradeID = merchantorderId.substring(
					merchantorderId.indexOf("=") + 1, merchantorderId.length());

			int reTradeID_int = Integer.valueOf(returntradeID);
			int tradeID_int = Integer.valueOf(tradID);
			if (reTradeID_int == tradeID_int)
				System.out.print("交易成功");

		}

		finish();
	}

	/**
	 * Sync Cookie
	 */
	// private void syncCookie(Context context, String url) {
	// if (!url.contains("merchantOrderId")) {
	// finish();
	// return;
	// }
	//
	// try {
	// Logger.i("Nat: webView.syncCookie.url", url);
	//
	// CookieSyncManager.createInstance(context);
	//
	// CookieManager cookieManager = CookieManager.getInstance();
	// cookieManager.setAcceptCookie(true);
	// cookieManager.removeSessionCookie();// 移除
	// // cookieManager.removeAllCookie();
	// String oldCookie = cookieManager.getCookie(url);
	// if (oldCookie != null) {
	// Logger.i("Nat: webView.syncCookieOutter.oldCookie", oldCookie);
	// }
	//
	// StringBuilder sbCookie = new StringBuilder();
	// sbCookie.append(String.format("JSESSIONID=%s",
	// "INPUT YOUR JSESSIONID STRING"));
	// sbCookie.append(String.format(";domain=%s",
	// "INPUT YOUR DOMAIN STRING"));
	// sbCookie.append(String.format(";path=%s", "INPUT YOUR PATH STRING"));
	//
	// String cookieValue = sbCookie.toString();
	// cookieManager.setCookie(url, cookieValue);
	// CookieSyncManager.getInstance().sync();
	// webview.loadUrl(url);
	//
	// String newCookie = cookieManager.getCookie(url);
	// if (newCookie != null) {
	// Logger.i("Nat: webView.syncCookie.newCookie", newCookie);
	// }
	// } catch (Exception e) {
	// Logger.e("Nat: webView.syncCookie failed", e.toString());
	// }
	// }

	/**
	 * 单击事件
	 * 
	 * @author Administrator
	 * 
	 */
	// private class OnWebClickListenerimpl implements OnClickListener {
	//
	// private Intent intent = new Intent();
	// Bundle bundle = new Bundle();
	//
	// @Override
	// public void onClick(View view) {
	// switch (view.getId()) {
	// case R.id.product_bottom_share:
	// if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
	// ymShare = new YMShare(WebViewAcitivity.this, product,
	// mController);
	// ymShare.show();
	// } else {
	// intent.setClass(WebViewAcitivity.this, LoginActivity.class);
	// bundle.putSerializable(ProductActivity.TAG_PRODUCT, product);
	// bundle.putString(ProductActivity.TAG_ACIVITY,
	// WebViewAcitivity.LOGTAG);
	// intent.putExtras(bundle);
	// startActivityForResult(intent, 2);
	// }
	// break;
	// case R.id.product_bottom_showaddcar:
	// attributePopupWindow.showAtLocation(base_bottomView,
	// Gravity.BOTTOM, 0, 0);
	// break;
	// case R.id.product_bottom_order:
	// if (MyApplication.getInstance().getDbUser().getIsLogin() == 1) {
	// intent.setClass(WebViewAcitivity.this, OrderActivity.class);
	// intent.putExtras(bundle);
	// startActivity(intent);
	// } else {
	// intent.setClass(WebViewAcitivity.this, LoginActivity.class);
	// bundle.putSerializable(ProductActivity.TAG_PRODUCT, product);
	// bundle.putString(ProductActivity.TAG_ACIVITY,
	// TradeActivity.LOGTAG);
	// intent.putExtras(bundle);
	// startActivity(intent);
	// }
	// break;
	// }
	// }
	// }
}
