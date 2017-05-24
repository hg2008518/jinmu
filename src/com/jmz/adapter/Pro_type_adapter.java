package com.jmz.adapter;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.jmz.R;
import com.jmz.bean.Child;


public class Pro_type_adapter extends BaseAdapter {
	// ����Context
		private LayoutInflater mInflater;
	    private List<Child> list;
	    private Child childs;
	    private Context context;
		public Pro_type_adapter(Context context,List<Child> list){
			mInflater=LayoutInflater.from(context);
			this.list=list;
			this.context=context;
		}
		
		@Override
		public int getCount() {
			if(list!=null&&list.size()>0)
				return list.size();
			else
			    return 0;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final MyView view;
			if(convertView==null){
				view=new MyView();
				convertView=mInflater.inflate(R.layout.classify_list_pro_type_item, null);
				view.icon=(ImageView)convertView.findViewById(R.id.typeicon);
				view.name=(TextView)convertView.findViewById(R.id.typename);
				convertView.setTag(view);
			}else{
				view=(MyView) convertView.getTag();
			}
			
			if(list!=null&&list.size()>0)
			{
				childs=list.get(position);
				view.name.setText(childs.getChildNa());
			}
	        return convertView;
		}

		
		private class MyView{
			private ImageView icon;		
			private TextView name;
		}
		
}
