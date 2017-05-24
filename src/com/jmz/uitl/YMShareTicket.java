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
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * 友盟分享电子券
 * 
 * @author Administrator
 * 
 */
public class YMShareTicket {
	private ParentActivity activity;
	private Context context;
	private UMSocialService mController;

	private static final String WXAPPID = "wx09cbb3d11d0ab10c";
	private static final String WXSECRET = "b785befd4ce00689f0bed7b86b85f515";

	private static final String QQAPPID = "1103993177";
	private static final String QQKEY = "8EQOA04ZG41SFkHz";

	private OrderProduct orderProduct;
	private String productTitle;
	private String productId;
	private String imageUrl;

	public YMShareTicket(ParentActivity activity,OrderProduct orderProduct,
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
	public void showImg(Bitmap bitmap) {
		if (mController != null) {
			// 设置分享内容
			mController.setShareContent(context
					.getString(R.string.share_info_other) + productTitle);
			// 设置分享图片, 参数2为图片的url地址
			mController.setShareMedia(new UMImage(activity, bitmap));

			// 添加微信平台
			UMWXHandler wxHandler = new UMWXHandler(context, WXAPPID, WXSECRET);
			wxHandler.setTitle(productTitle);
			wxHandler.addToSocialSDK();
			// 支持微信朋友圈
			UMWXHandler wxCircleHandler = new UMWXHandler(context, WXAPPID,
					WXSECRET);
			wxCircleHandler.setToCircle(true);
			wxCircleHandler.setTitle(productTitle);
			wxCircleHandler.addToSocialSDK();
			// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
			UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, QQAPPID,
					QQKEY);
			qqSsoHandler.addToSocialSDK();

			// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
			QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
					QQAPPID, QQKEY);
			qZoneSsoHandler.addToSocialSDK();

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
						activity.showToast(R.string.share_susse);
					}
				}
			});
			mController.getConfig().setPlatforms(SHARE_MEDIA.QQ,
					SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
					SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
					SHARE_MEDIA.TENCENT, SHARE_MEDIA.RENREN);
			mController.openShare(activity, false);
		}
	}
}
