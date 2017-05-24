package com.jmz;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.google.gson.Gson;
import com.jmz.adapter.CityAdapter;
import com.jmz.bean.AllCity;
import com.jmz.bean.DBUser;
import com.jmz.db.UserService;
import com.jmz.http.ApiResponse;
import com.jmz.impl.OnSubmitListener;
import com.jmz.submit.SubmitTask;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;
import com.jmz.uitl.Utile;

/**
 * 获取城市
 * 
 * @author hhy 2014-7-11
 */
public class CityActivity extends ParentActivity implements OnSubmitListener {
	protected static final String CITY_RESUIT = "CityResult";
	protected static final String LOGTAG = "CityActivity";
	protected static final String CITY_NAME = "city_name";
	protected static final String CITY_ID = "city_id";
	public static final String CITY_SHAREPREFER_NAME = "city_list";
	public static final String TAG_CITYlIST_NAME = "city_list";
	private ListView listView;
	private AllCity allCity;
	private CityAdapter adapter;
	private HashMap<String, String> actMap;
	private SubmitTask cityListTask;
	private DBUser dbUser;
	private SharedPreferences sharedPreferences;
	private Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLoadiagle();
		setTittleText(R.string.home_city_list);
		sharedPreferences = getSharedPreferences(CITY_SHAREPREFER_NAME,
				Activity.MODE_PRIVATE);
		// 实例化SharedPreferences.Editor对象（第二步）
		editor = sharedPreferences.edit();
		mainSubmit();
		dbUser = MyApplication.getInstance().getDbUser();
	}

	/**
	 * 提交
	 */
	@Override
	protected void mainSubmit() {
		actMap = new HashMap<String, String>();
		cityListTask = new SubmitTask(CityActivity.this, Config.CITY_LIST,
				AllCity.class, this, false);
		cityListTask.execute(actMap);
	}

	@Override
	public void destroyTask() {
		super.destroyTask();
		if (cityListTask != null) {
			cityListTask.destorySelf();
		}
	}

	@Override
	public void onSubmitSuccess(ApiResponse<Object> result) {
		initcenterView(LayoutInflater.from(CityActivity.this).inflate(
				R.layout.activity_city_center, null));
		listView = (ListView) findViewById(R.id.city_center_listview);
		allCity = (AllCity) result.getObject();
		
		editor.putString(TAG_CITYlIST_NAME, new Gson().toJson(allCity));
		// 提交当前数据
		editor.commit();
		
		adapter = new CityAdapter(this, allCity, dbUser.getCityName());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				cityId = allCity.getCityList().get(position).getCityID();
//				cityName = allCity.getCityList().get(position).getCityName();
				if(!Utile.isEqual(MyApplication.getInstance().getDbUser().getCityId(), allCity.getCityList().get(position).getCityID())){
					setResult(1);
					dbUser.setCityId(allCity.getCityList().get(position).getCityID());
					dbUser.setCityName(allCity.getCityList().get(position).getCityName());
					MyApplication.getInstance().setDbUser(dbUser);
				}
				CityActivity.this.finish();
				
				Set<String> set = new HashSet<String>();
				set.add(allCity.getCityList().get(position).getCityName());
				JPushInterface.setAliasAndTags(getApplicationContext(), null,
						set, mTagsCallback);
			}
		});
	}
	/**
	 * 添加城市回调
	 */
	private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			switch (code) {
			case 0:
				Log.e("hhy", "设置tag成功");
				break;

			case 6002:
				Log.e("hhy", "设置tag失败");
				break;

			default:
				Log.e("hhy", "设置tag失败");
				break;
			}
		}

	};
}
