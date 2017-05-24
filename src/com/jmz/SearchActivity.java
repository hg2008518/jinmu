package com.jmz;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import com.jmz.adapter.SearchExpandableListAdapter;
import com.jmz.bean.Child;
import com.jmz.bean.Classify;
import com.jmz.bean.ClassifyList;
import com.jmz.fragment.Fragment_pro_type;
import com.jmz.fragment.Fragment_pro_type.OnArticleSelectedListener;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.view.ClearEditTextView;

/**
 * 查找
 * 
 * @author Administrator
 * 
 */
public class SearchActivity extends ParentActivity implements OnArticleSelectedListener {
	public static final String PRODUCTTITLE = "ProductTitle";
	public static final String CHILD = "child";
	public static final String LOGTAG = "SearchActivity";
	public static final String ISDESTROY = "isDestroy";
	private ClearEditTextView edit;
	private ImageView goImg, backImg;
	private ExpandableListView ebListview;
	private SubmitTask submitTask;
	public ClassifyList classifyList;
	private List<Child> childs;
	private Child child;
	private boolean isDestroy = false;
	
	private TextView classifyTextViews[];
	private View views[];
	private LayoutInflater inflater;
	private ScrollView scrollView;
	private int scrllViewWidth = 0, scrollViewMiddle = 0;
	private ViewPager shop_pager;
	private int currentItem = 0;
	private CalssifyAdapter classifyAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initTopView(LayoutInflater.from(SearchActivity.this).inflate(
				R.layout.activity_search_top, null));
		initLoadiagle();
		child = (Child) (getIntent().getExtras() != null ? getIntent()
				.getExtras().get(CHILD) : new Child());
		isDestroy = getIntent().getBooleanExtra(ISDESTROY, false);

		mainSubmit();
	}
	/**
	 * 初始化组件
	 */
	private void initMyView() {
		initTopView(LayoutInflater.from(SearchActivity.this).inflate(
				R.layout.activity_search_top, null));
		initcenterView(LayoutInflater.from(SearchActivity.this).inflate(
				R.layout.activity_search_center, null));
		edit = (ClearEditTextView) findViewById(R.id.serch_edit);
		goImg = (ImageView) findViewById(R.id.serch_logo);
		backImg = (ImageView) findViewById(R.id.serch_back);
//		ebListview = (ExpandableListView) findViewById(R.id.search_center_expendlist);
		goImg.setOnClickListener(new onSerchAcivityClickListener());
		backImg.setOnClickListener(new onSerchAcivityClickListener());
		
		scrollView = (ScrollView) findViewById(R.id.classify1_scrlllview);
		classifyAdapter = new CalssifyAdapter(getSupportFragmentManager(), classifyList.classify);
		inflater = LayoutInflater.from(this);
		
		showClassifyView();
		initPager();
		
//		ebListview.setAdapter(new SearchExpandableListAdapter(this, classifyList));
//		ebListview.setOnChildClickListener(new OnChildClickListener() {
//
//			@Override
//			public boolean onChildClick(ExpandableListView parent, View view,
//					int groupPosition, int childPosition, long id) {
//				Intent intent = new Intent();
//				Bundle bundle = new Bundle();
//				Child cd = classifyList.getClassify().get(groupPosition)
//						.getChilds().get(childPosition);
//				bundle.putSerializable(CHILD, cd);
//				intent.putExtras(bundle);
//				if (isDestroy) {
//					setResult(1, intent);
//					Log.i("zwt test", "getCallingActivity():" + getCallingActivity());
//					getCallingActivity().getClass();
//					SearchActivity.this.finish();
//				} else {
//					intent.setClass(SearchActivity.this,
//							ProductClassActivity.class);
//					startActivity(intent);
//				}
//				return false;
//			}
//		});
	}
	
	/**
	 * 提交
	 */
	@Override
	protected void mainSubmit() {
		HashMap<String, String> classlyMap = new HashMap<String, String>();
		classlyMap.put(Config.TAG_PROPERTYTYPEID, child.getPropertyType());
		classlyMap.put(Config.TAG_PROPERTYID, child.getNodeId());
//		Logger.e("hhy", LOGTAG+"----------"+child.toString());
		classlyMap.put(Config.TAG_CITYID, MyApplication.getInstance().getDbUser().getCityId());
		submitTask = new SubmitTask(this, Config.PROPERTYTYPE_LIST,
				ClassifyList.class, new onClasslyListSubmitListener(), false);
		submitTask.execute(classlyMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (submitTask != null) {
			submitTask.destorySelf();
		}
	}

	/**
	 * 单击事件
	 * 
	 * @author Administrator
	 * 
	 */
	private class onSerchAcivityClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.serch_back:
				SearchActivity.this.finish();
				break;
			case R.id.serch_logo:
				if (edit.getText().toString().replace(" ", "").length() > 0) {
					Intent intent = new Intent(SearchActivity.this,
							ProductClassActivity.class);
					intent.putExtra(PRODUCTTITLE, edit.getText().toString());
					startActivity(intent);
				} else {
					showToast(R.string.msg_search_error);
				}
				break;

			default:
				break;
			}
		}

	}

	/**
	 * 响应获取所有分类
	 * 
	 * @author Administrator
	 * 
	 */
	private class onClasslyListSubmitListener implements OnSubmitListener {
		@Override
		public void onSubmitSuccess(ApiResponse<Object> result) {
			classifyList = (ClassifyList) result.getObject();
			initMyView();
		}
	}
	
	/**
	 * 动态生成显示items中的textview
	 */
	private void showClassifyView() {
//		toolsList = new String[] { "常用分类", "潮流女装", "品牌男装", "内衣配饰", "家用电器",
//				"手机数码", "电脑办公", "个护化妆", "母婴频道", "食物生鲜", "酒水饮料", "家居家纺", "整车车品",
//				"鞋靴箱包", "运动户外", "图书", "玩具乐器", "钟表", "居家生活", "珠宝饰品", "音像制品",
//				"家具建材", "计生情趣", "营养保健", "奢侈礼品", "生活服务", "旅游出行" };
		LinearLayout toolsLayout = (LinearLayout) findViewById(R.id.classify1);
		classifyTextViews = new TextView[classifyList.classify.size()];
		views = new View[classifyList.classify.size()];

		for (int i = 0; i < classifyList.classify.size(); i++) {
			View view = inflater.inflate(R.layout.classify1_layout, null);
			view.setId(i);
			view.setOnClickListener(classifyItemListener);
			TextView textView = (TextView) view.findViewById(R.id.text);
			textView.setText(classifyList.classify.get(i).getGroupName());
			toolsLayout.addView(view);
			classifyTextViews[i] = textView;
			views[i] = view;
		}
		changeTextColor(0);
	}

	private View.OnClickListener classifyItemListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			shop_pager.setCurrentItem(v.getId());
		}
	};

	/**
	 * initPager<br/>
	 * 初始化ViewPager控件相关内容
	 */
	private void initPager() {
		shop_pager = (ViewPager) findViewById(R.id.classify2_pager);
		shop_pager.setAdapter(classifyAdapter);
		shop_pager.setOnPageChangeListener(onPageChangeListener);
	}

	/**
	 * OnPageChangeListener<br/>
	 * 监听ViewPager选项卡变化事的事件
	 */

	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			if (shop_pager.getCurrentItem() != arg0)
				shop_pager.setCurrentItem(arg0);
			if (currentItem != arg0) {
				changeTextColor(arg0);
				changeTextLocation(arg0);
				
			}
			currentItem = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	

	/**
	 * ViewPager 加载选项卡
	 * 
	 * @author Administrator
	 * 
	 */
	private class CalssifyAdapter extends FragmentPagerAdapter {
		List<Classify> classify;
		public CalssifyAdapter(FragmentManager fm) {
			super(fm);
		}
		
		public CalssifyAdapter(FragmentManager fragmentManager, List<Classify> classify) {
			super(fragmentManager);
			this.classify = classify;
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment = new Fragment_pro_type();
			Bundle bundle = new Bundle();
			String str = classifyList.getClassify().get(arg0).getGroupName();
			childs = classifyList.getClassify().get(arg0).getChilds();
			bundle.putString("typename", str);
			bundle.putSerializable("childs", (Serializable)childs);
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getCount() {
			return classifyList.getClassify().size();
		}
	}

	/**
	 * 改变textView的颜色
	 * 
	 * @param id
	 */
	private void changeTextColor(int id) {
		for (int i = 0; i < classifyList.getClassify().size(); i++) {
			if (i != id) {
				classifyTextViews[i]
						.setBackgroundResource(android.R.color.transparent);
				classifyTextViews[i].setTextColor(0xff000000);
			}
		}
		classifyTextViews[id].setBackgroundResource(android.R.color.white);
		classifyTextViews[id].setTextColor(0xffff5d5e);
	}

	/**
	 * 改变栏目位置
	 * 
	 * @param clickPosition
	 */
	private void changeTextLocation(int clickPosition) {

		int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
		scrollView.smoothScrollTo(0, x);
	}

	/**
	 * 返回scrollview的中间位置
	 * 
	 * @return
	 */
	private int getScrollViewMiddle() {
		if (scrollViewMiddle == 0)
			scrollViewMiddle = getScrollViewheight() / 2;
		return scrollViewMiddle;
	}

	/**
	 * 返回ScrollView的宽度
	 * 
	 * @return
	 */
	private int getScrollViewheight() {
		if (scrllViewWidth == 0)
			scrllViewWidth = scrollView.getBottom() - scrollView.getTop();
		return scrllViewWidth;
	}

	/**
	 * 返回view的宽度
	 * 
	 * @param view
	 * @return
	 */
	private int getViewheight(View view) {
		return view.getBottom() - view.getTop();
	}
	
	/* 分类中的点击回调
	 * @see com.jcwl.jdshop.fragment.Fragment_pro_type.OnArticleSelectedListener#onArticleSelected(int)
	 */
	@Override
	public void onArticleSelected(Child child) {
		// TODO Auto-generated method stub
		Log.i("test", "--->name:" + child.getChildNa() + " ID:" + child.getChildId() );
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable(CHILD, child);
		intent.putExtras(bundle);
		if (isDestroy) {
			setResult(1, intent);
			Log.i("zwt test", "getCallingActivity():" + getCallingActivity());
			getCallingActivity().getClass();
			SearchActivity.this.finish();
		} else {
			intent.setClass(SearchActivity.this,
					ProductClassActivity.class);
			startActivity(intent);
		}
	}

}
