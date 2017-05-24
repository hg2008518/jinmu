package com.jmz.uitl;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.jmz.MyApplication;
import com.jmz.ParentActivity;
import com.jmz.R;
import com.jmz.bean.OrderProduct;
import com.jmz.bean.ParentBean;
import com.jmz.bean.Product;
import com.jmz.bean.ShareProduct;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.umeng.socialize.bean.CustomPlatform;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.OnSnsPlatformClickListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * 友盟分享
 * 
 * @author Administrator
 * 
 */
public class YMShare {
	private Product product;
	private ShareProduct shareProduct;
	private HashMap<String, String> map;
	private ParentActivity activity;
	private SubmitTask shareTask;
	private Context context;
	private UMSocialService mController;
	private String productTitle;
	private String productId;
	private String imageUrl;

	public static final String WXAPPID = "wx546686929c574b69";
	public static final String WXSECRET = "f17918a9d69d3795135e33435797e499";

	public static final String QQAPPID = "1103993177";
	public static final String QQKEY = "8EQOA04ZG41SFkHz";

	private RefreAdapter listener;
	private OrderProduct orderProduct;
	private String id = "/product/info.aspx?id=";
	private String rid = "&rid=";
	private String pf = "&pf=";

	public YMShare(ParentActivity activity, Product product,
			UMSocialService mController) {
		this.product = product;
		this.activity = activity;
		this.mController = mController;
		context = activity;
		productTitle = product.getProductTitle();
		productId = product.getProductID();
		imageUrl = product.getImageUrl();
	}

	public YMShare(ParentActivity activity, ShareProduct shareProduct,
			UMSocialService mController, RefreAdapter listener) {
		this.shareProduct = shareProduct;
		this.activity = activity;
		this.mController = mController;
		this.listener = listener;
		context = activity;
		productTitle = shareProduct.getProductTitle();
		productId = shareProduct.getProductID();
		imageUrl = shareProduct.getImageUrl();
	}

	public YMShare(ParentActivity activity, OrderProduct orderProduct,
			UMSocialService mController) {
		this.orderProduct = orderProduct;
		this.activity = activity;
		this.mController = mController;
		context = activity;
		productTitle = orderProduct.getProductTitle();
		productId = orderProduct.getProductID();
		imageUrl = orderProduct.getImageUrl();
	}

	/**
	 * 分享功能
	 * 
	 * @param msgTitle
	 *            消息标题
	 * @param msgText
	 *            消息内容
	 */
	public void show() {
		if (mController != null) {
			// 设置分享内容
			mController.setShareContent(context.getString(R.string.share_info)
					+ productTitle);
			// 设置分享图片, 参数2为图片的url地址
			mController.setShareMedia(new UMImage(activity, Config.JMZG
					+ imageUrl));

			// 添加微信平台
			UMWXHandler wxHandler = new UMWXHandler(context, WXAPPID, WXSECRET);
			wxHandler.setTargetUrl(Config.JMZG + id + productId + rid
					+ MyApplication.getInstance().getDbUser().getUserId() + pf
					+ "weixin");
			wxHandler.setTitle(productTitle);
			wxHandler.addToSocialSDK();
			// 支持微信朋友圈
			UMWXHandler wxCircleHandler = new UMWXHandler(context, WXAPPID,
					WXSECRET);
			wxCircleHandler.setToCircle(true);
			wxCircleHandler.setTargetUrl(Config.JMZG + id + productId + rid
					+ MyApplication.getInstance().getDbUser().getUserId() + pf
					+ "weixin");
			wxCircleHandler.setTitle(productTitle);
			wxCircleHandler.addToSocialSDK();
			// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
			UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, QQAPPID,
					QQKEY);
			qqSsoHandler.setTargetUrl(Config.JMZG + id + productId + rid
					+ MyApplication.getInstance().getDbUser().getUserId() + pf
					+ "qq");
			qqSsoHandler.addToSocialSDK();

			// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
			QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
					QQAPPID, QQKEY);
			qZoneSsoHandler.setTargetUrl(Config.JMZG + id + productId + rid
					+ MyApplication.getInstance().getDbUser().getUserId() + pf
					+ "qzone");
			qZoneSsoHandler.addToSocialSDK();
			
			TencentWbShareContent tencentContent = new TencentWbShareContent();
			// 设置分享到腾讯微博的文字内容
			tencentContent.setShareContent(context.getString(R.string.share_info)
					+ productTitle + " " + Config.JMZG + id + productId + rid
					+ MyApplication.getInstance().getDbUser().getUserId() + pf
					+ "tencetWb");
			//设置分享的图片
			tencentContent.setShareImage(new UMImage(activity, Config.JMZG
					+ imageUrl));
			// 设置分享到腾讯微博的多媒体内容
			mController.setShareMedia(tencentContent);
			
			SinaShareContent sinaContent = new SinaShareContent();
			// 设置分享到新浪微博的文字内容
			sinaContent.setShareContent(context.getString(R.string.share_info)
					+ productTitle + " " + Config.JMZG + id + productId + rid
					+ MyApplication.getInstance().getDbUser().getUserId() + pf
					+ "sina");
			//设置分享的图片
			sinaContent.setShareImage(new UMImage(activity, Config.JMZG
					+ imageUrl));
			// 设置分享到新浪微博的多媒体内容
			mController.setShareMedia(sinaContent);

			// 设置新浪SSO handler
			mController.getConfig().setSsoHandler(new SinaSsoHandler());

			// 设置腾讯微博SSO handler
			mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

			mController.getConfig().registerListener(new SnsPostListener() {

				@Override
				public void onStart() {
				}

				@Override
				public void onComplete(SHARE_MEDIA platform, int stCode,
						SocializeEntity entity) {
					if (stCode == 200) {
						shareSubmit();
						activity.showToast(R.string.share_susse);
					}
				}
			});

			CustomPlatform customPlatform = new CustomPlatform("other", "更多",
					R.drawable.icon_other);
			customPlatform.mClickListener = new OnSnsPlatformClickListener() {
				@Override
				public void onClick(Context context, SocializeEntity entity,
						SnsPostListener listener) {
					share();
				}
			};
			mController.getConfig().addCustomPlatform(customPlatform);
			// mController.getConfig().setPlatforms(SHARE_MEDIA.QQ,
			// SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
			// SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
			// SHARE_MEDIA.TENCENT,SHARE_MEDIA.RENREN);
			mController.openShare(activity, false);
		}
	}

	/**
	 * 分享功能
	 * 
	 * @param msgTitle
	 *            消息标题
	 * @param msgText
	 *            消息内容
	 */
	public void share() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain"); // 纯文本
		intent.putExtra(Intent.EXTRA_SUBJECT, "金拇指购");
		intent.putExtra(Intent.EXTRA_TEXT, Config.JMZG + id + productId + rid
				+ MyApplication.getInstance().getDbUser().getUserId() + pf
				+ "android" + activity.getString(R.string.share_info)
				+ productTitle);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivityForResult(
				Intent.createChooser(intent,
						context.getString(R.string.select_type)), 1);
	}

	/**
	 * 提交分享信息
	 */
	public void shareSubmit() {
		map = new HashMap<String, String>();
		map.put(Config.TAG_PLATFORM, "");
		map.put(Config.TAG_PUODUCTID, productId);
		shareTask = new SubmitTask(activity, Config.SHARE_POST,
				ParentBean.class, new OnShareSubmitListener(), false);
		shareTask.execute(map);
	}

	/**
	 * 响应分享
	 * 
	 * @author Administrator
	 * 
	 */
	private class OnShareSubmitListener implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			ParentBean bean = (ParentBean) result.getObject();
			if (bean.getServerReturn().equals("StatusSuccess")
					|| bean.getServerReturn() == "StatusSuccess") {
				if (listener != null) {
					listener.onSuccess(shareProduct);
				}
			} else {
				activity.showToast(ServerReturnStatus.checkReturn(bean
						.getServerReturn()));
			}
		}
	}

	public interface RefreAdapter {
		void onSuccess(ShareProduct product);
	}
}
