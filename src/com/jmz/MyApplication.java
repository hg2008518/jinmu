package com.jmz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import android.app.Application;
import android.graphics.Bitmap;

import cn.jpush.android.api.JPushInterface;

import com.jmz.bean.DBUser;
import com.jmz.db.UserService;
import com.jmz.uitl.CrashHandler;
import com.jmz.uitl.FileUtlie;
import com.jmz.uitl.Logger;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.testin.agent.TestinAgent;

public class MyApplication extends Application {
	private static MyApplication instance = null;
	protected List<ParentActivity> activities = new ArrayList<ParentActivity>();
	private UserService userService;
	private DBUser dbUser;
	private List<DBUser> userList;
	
	

	public List<ParentActivity> getActivities() {
		return activities;
	}

	public void setActivities(List<ParentActivity> activities) {
		this.activities = activities;
	}

	public synchronized static MyApplication getInstance() {
		return instance;
	}

	/**
	 * 取得默认用户
	 * 
	 * @return
	 */
	public DBUser getDbUser() {
		return dbUser;
	}

	/**
	 * 设置默认用户
	 * 
	 * @param dbUser
	 */
	public void setDbUser() {
		userService = new UserService(getApplicationContext());
		dbUser = userService.findAllData().get(0);
	}

	/**
	 * 先更新新用户，再设置默认用户
	 * 
	 * @param dbUser
	 */
	public void setDbUser(DBUser dbUser) {
		userService.update(dbUser);
		setDbUser();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		initImageLoader();
		
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// 日志保存
		 CrashHandler crashHandler = CrashHandler.getInstance();
		 crashHandler.init(getApplicationContext(),activities);

		userService = new UserService(getApplicationContext());
		userList = userService.findAllData();
		if (userList.isEmpty()) {
			userService.insert(new DBUser("0", 0, 0, 0, 0f, 0f, "", "广西", "15",
					"", "",0,""));
			userList = userService.findAllData();
		}
		dbUser = userList.get(0);

		dbUser.setIsLogin(0);
		dbUser.setIsContract(0);
		userService.update(dbUser);
		initPush();
		
	}

	/**
	 * 初始化极光推送
	 */
	private void initPush() {
		try {
			JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
			JPushInterface.init(this); // 初始化 JPush
			Set<String> set = new HashSet<String>();
			set.add("玉林");
			JPushInterface.setAliasAndTags(this, dbUser.getUserId(), set);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 初始化 ImageLoader
	 */
	public void initImageLoader() {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				// .memoryCacheExtraOptions(screenWidth, screenWidth)
				// 即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				// .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 *
				// 1024))
				.memoryCache(new WeakMemoryCache())
				// 你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(100)
				// 缓存的文件数量
				.discCache(new UnlimitedDiscCache(FileUtlie.getJmzFile()))
				// 自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout
																			// (5
																			// s),
																			// readTimeout
																			// (30
																			// s)超时时间
				.writeDebugLogs() // Remove for release app
				.build();// 开始构建
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 取得Options
	 * 
	 * @return
	 */
	public DisplayImageOptions getOptions() {
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.nullimg) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.nullimg)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.nullimg) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
				.build();// 构建完成
	}

}
