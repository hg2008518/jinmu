package com.jmz.submit;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.jmz.ParentActivity;
import com.jmz.http.ApiResponse;
import com.jmz.http.NewApi;
import com.jmz.impl.OnSubmitListener;
import com.jmz.uitl.Logger;
import com.jmz.view.LoadingDialog;

/**
 * 提交访问服务器
 * 
 * @author Administrator
 * 
 */
public class SubmitTask extends
		AsyncTask<HashMap<String, String>, Void, ApiResponse<Object>> {
	private Context context;
	private String url;
	private Class clazz;
	private OnSubmitListener listener;
	private ParentActivity activity;
	private boolean isShow = false;
	private LoadingDialog loadDialog;

	public SubmitTask(Context context, String url, Class clazz,
			OnSubmitListener listener, boolean isShow) {
		this.context = context;
		this.url = url;
		this.clazz = clazz;
		this.listener = listener;
		this.isShow = isShow;
		activity = (ParentActivity) context;
	}

	/**
	 * 弹出对框框
	 */
	public void showLoadDialog() {
		loadDialog = new LoadingDialog(context);
		loadDialog.setCancelable(true);
		loadDialog.show();
		loadDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				cancel(true);
			}
		});
	}

	/**
	 * 销毁自己
	 */
	public void destorySelf() {
		this.cancel(true);
		dissLoadDialog();
	}

	/**
	 * 关闭对话框
	 */
	public void dissLoadDialog() {
		if (loadDialog != null ) {
			if (loadDialog.isShowing())
				loadDialog.dismiss();
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (!activity.isNetworkConnected()) {
			activity.initNetError();
			cancel(true);
		} else if (isShow) {
			showLoadDialog();
		}
	}

	@Override
	protected ApiResponse<Object> doInBackground(HashMap<String, String>... map) {
		ApiResponse<Object> obj = null;
		try {
			obj = NewApi.getInstnace().sendRequest(context, map[0], url, clazz);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	protected void onPostExecute(ApiResponse<Object> result) {
		super.onPostExecute(result);
		dissLoadDialog();
		
		if (result != null && result.getObject() != null) {
			listener.onSubmitSuccess(result);
		} else {
			activity.initNetError();
		}
	}

}
