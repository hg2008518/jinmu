package com.jmz.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.jmz.R;
import com.jmz.adapter.Pro_type_adapter;
import com.jmz.bean.Child;
import com.jmz.uitl.Logger;

public class Fragment_pro_type extends Fragment {
	private List<Child> list;
	private ImageView hint_img;
	private GridView listView;
	private Pro_type_adapter adapter;
	private ProgressBar progressBar;
	private String typename;
	private OnArticleSelectedListener mListener;
	
	// Container Activity must implement this interface
    public interface OnArticleSelectedListener {
        public void onArticleSelected(Child child);
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.classify_fragment_pro_type, null);
		progressBar=(ProgressBar) view.findViewById(R.id.progressBar);
		hint_img=(ImageView) view.findViewById(R.id.hint_img);
		listView = (GridView) view.findViewById(R.id.listView);
		typename=getArguments().getString("typename");
		list =(List<Child>) getArguments().getSerializable("childs");
		((TextView)view.findViewById(R.id.toptype)).setText(typename);
		adapter=new Pro_type_adapter(getActivity(), list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
              Logger.i("test", "--->chidName:" + list.get(position).getChildNa() + " childID:" + list.get(position).getChildId());
           // Send the event and Uri to the host activity
              mListener.onArticleSelected(list.get(position));
			}
		});
		
		return view;
	}
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnArticleSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }
	
	/*private class LoadTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			String name[]=new String[]{"shopid","type"};
			String value[]=new String[]{"0","store"};
			return NetworkHandle.requestBypost("app=u_favorite&act=index",name,value);
		}
		
		@Override
		protected void onPostExecute(String result) {	
			progressBar.setVisibility(View.GONE);
			list=new ArrayList<Shop>();
			try {
				if(Constant.isDebug)System.out.println("result:"+result);
				JSONObject ob=new JSONObject(result);
				if(ob.getString("state").equals("1")){
					arrayToList(ob.getJSONArray("list"));
					adapter=new Love_shop_adapter(getActivity(), list,listView);
					listView.setAdapter(adapter);
					listView.onRefreshComplete();
					if(list.size()<20)
						listView.onPullUpRefreshFail();
					if(list.size()==0)hint_img.setVisibility(View.VISIBLE);
					else hint_img.setVisibility(View.GONE);
				}else{
					//if(tradestate.equals("0"))
						//ResultUtils.handle((Activity_order)getActivity(), ob);
				}
			} catch (Exception e) {
			//	if(tradestate.equals("0"))
					//ResultUtils.handle((Activity_order)getActivity(), "");
				e.printStackTrace();
			}	
		}
	}
	
	private void arrayToList(JSONArray array) throws JSONException{
		JSONObject ob;
		for (int i = 0; i < array.length(); i++) {
			ob=array.getJSONObject(i);
			shop=new Shop(ob.getString("shopid"),ob.getString("shopname"), ob.getString("shoplogo"), ob.getString("weixin"), ob.getString("shopurl"));
			list.add(shop);	
		   }
		}
	*/
	
	/*private class LoadTaskMore extends AsyncTask<Void, Void, String>{
		@Override
		protected String doInBackground(Void... params) {
			String name[]=new String[]{"shopid","type"};
			String value[]=new String[]{list.get(list.size()-1).getShopid(),"store"};
			return NetworkHandle.requestBypost("app=u_favorite&act=index",name,value);
		}
		@Override
		protected void onPostExecute(String result) {
			if(Constant.isDebug)System.out.println("result:"+result);
			try {
				JSONObject ob=new JSONObject(result);
				if(ob.getString("state").equals("1")){
					JSONArray array=ob.getJSONArray("list");
					arrayToList(array);
					if(array.length()>0)
						adapter.notifyDataSetChanged();
					if(array.length()<20)
						listView.onPullUpRefreshFail();
					else 
						listView.onPullUpRefreshComplete();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}*/
	
	
	
}
