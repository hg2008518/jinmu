package com.jmz.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hxcr.umspay.activity.MainActivity;
import com.jmz.R;
import com.jmz.bean.Child;
import com.jmz.bean.Classify;
import com.jmz.bean.ClassifyList;

/**
 * 数据源
 * 
 * @author Administrator
 * 
 */
public class SearchExpandableListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private LayoutInflater inflater;
	private ClassifyList classifyList;
	private List<Classify> classifies;

	public SearchExpandableListAdapter(Context context, ClassifyList classifyList) {
		this.context = context;
		this.classifyList = classifyList;
		inflater = LayoutInflater.from(context);
		classifies = classifyList.getClassify();
	}

	// 返回父列表个数
	@Override
	public int getGroupCount() {
		return classifies.size();
	}

	// 返回子列表个数
	@Override
	public int getChildrenCount(int groupPosition) {
		return classifies.get(groupPosition).getChilds().size();
	}

	@Override
	public Classify getGroup(int groupPosition) {

		return classifies.get(groupPosition);
	}

	@Override
	public Child getChild(int groupPosition, int childPosition) {
		return classifies.get(groupPosition).getChilds().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {

		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder groupHolder = null;
		if (convertView == null) {
			groupHolder = new GroupHolder();
			convertView = inflater.inflate(R.layout.adapter_search_group, null);
			groupHolder.textView = (TextView) convertView
					.findViewById(R.id.adapter_search_title);
			groupHolder.imageView = (ImageView) convertView
					.findViewById(R.id.adapter_search_image);
			groupHolder.textView.setTextSize(15);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}

		groupHolder.textView.setText(getGroup(groupPosition).getGroupName());
		if (isExpanded) {
			groupHolder.imageView.setImageResource(R.drawable.expanded);
			groupHolder.textView.setTextColor(context.getResources().getColor(R.color.deeporange));
		} else {
			groupHolder.imageView.setImageResource(R.drawable.collapse);
			groupHolder.textView.setTextColor(context.getResources().getColor(R.color.black));
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_search_child, null);
		}
		TextView textView = (TextView) convertView
				.findViewById(R.id.adapter_search_item_title);
		textView.setTextSize(13);
		textView.setText(getChild(groupPosition, childPosition).getChildNa());
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class GroupHolder {
		TextView textView;
		ImageView imageView;
	}
}