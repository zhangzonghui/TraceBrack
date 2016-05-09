package com.tempus.adapter;

import java.util.ArrayList;

import com.tempus.entity.MenuEntity;
import com.tempus.traceback.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuListAadapter extends BaseAdapter {
    private ArrayList<MenuEntity> listdata;
    private Context context;
	public MenuListAadapter(Context context,ArrayList<MenuEntity> listdata) {
		// TODO Auto-generated constructor stub
		this.listdata = listdata;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listdata.size();
	}

	
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listdata==null?null:listdata.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MenuEntity entity = listdata.get(position);
		ViewHodler viewHodler;  
		if (convertView == null) {
			viewHodler = new ViewHodler();
			convertView = View.inflate(context, R.layout.menulist_adapter,null);
			viewHodler.imageView=(ImageView) convertView.findViewById(R.id.icion_img);
			viewHodler.name=(TextView) convertView.findViewById(R.id.control_name);

			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		
		if(entity.getName() != null && !entity.getName().equals("")){
			viewHodler.name.setText(entity.getName());
		}
		if(entity.getIcion() != 0){
			viewHodler.imageView.setImageResource(entity.getIcion());
		}
		
		
		return convertView;
	}
	
	static class ViewHodler {
		ImageView imageView;// 鍥剧墖
		TextView name;
	}

}
