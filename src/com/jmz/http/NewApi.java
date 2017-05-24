package com.jmz.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.jmz.MyApplication;
import com.jmz.uitl.Config;
import com.jmz.uitl.Logger;

/**
 * api解析
 * 
 * @author Administrator
 * 
 */
public class NewApi {
	private static final String LOGTAG = "NewApi";
	private static NewApi sInstnace;
	private JSONObject jsonObject;
	private Context context;

	public static NewApi getInstnace() {
		if (sInstnace == null) {
			sInstnace = new NewApi();
		}
		return sInstnace;
	}

//	/**
//	 * 登陆
//	 * 
//	 * @param context
//	 * @param url
//	 * @param params
//	 * @return
//	 * @throws ClientProtocolException
//	 * @throws IOException
//	 * @throws JSONException
//	 */
//	public ApiResponse<LoginBean> requestTicket(Context context, String url,
//			Map<String, String> params) throws ClientProtocolException,
//			IOException, JSONException {
//		return sendRequest(context, params, url, LoginBean.class,false);
//	}

	/**
	 * 解析原始字符串
	 * 
	 * @param context
	 *            acivity
	 * @param map
	 *            参数
	 * @param url
	 *            地址
	 * @param clazz
	 *            bean对象
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public <T> ApiResponse<T> sendRequest(Context context,
			Map<String, String> map, String url, Class<T> clazz)
			throws ClientProtocolException, IOException, JSONException {
		this.context = context;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		String line = null;
		Gson son = new Gson();
		ApiResponse<T> apiResponse = null;
//		Logger.e("hhy",map.toString());
		post.setEntity(new UrlEncodedFormEntity(getList(map), HTTP.UTF_8));
		HttpResponse response = client.execute(post);
//		 Logger.e("hhy",context.getClass().getName()
//		 +"--"+response.getStatusLine().getStatusCode()+"");
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				String str = EntityUtils.toString(entity, HTTP.UTF_8);
				String str1 = str.substring(0, str.lastIndexOf("}") + 1).trim();
//				Logger.e("hhy", str1);
				apiResponse = son.fromJson(str1, ApiResponse.class);
				if (apiResponse != null) {
					apiResponse.setObject(son.fromJson(str1, clazz));
				}
			} else {
				apiResponse = new ApiResponse<T>();
			}
		return apiResponse;
	}

	/**
	 * 封装参数操作
	 * 
	 * @param map
	 * @return
	 */
	public List<NameValuePair> getList(Map<String, String> map) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		map.put(Config.TAG_TOKEN, Config.TOKEN);
		if(MyApplication.getInstance().getDbUser().getIsLogin() == 1){
			map.put(Config.TAG_USERNAME, MyApplication.getInstance().getDbUser().getUserName()); 
			map.put(Config.TAG_PASSWORD, MyApplication.getInstance().getDbUser().getPassWordString());
		}
//		Logger.e("hhy", map.toString());
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			list.add(new BasicNameValuePair(key, value));
		}
		return list;
	}
}
