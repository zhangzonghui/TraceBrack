package com.tempus.adapter;

import java.util.ArrayList;

import com.tempus.entity.GoodsEntity;
import com.tempus.traceback.R;

import android.content.Context;
import android.content.Entity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodListAdapter extends BaseAdapter {
    private ArrayList<GoodsEntity> listData;
    private Context context;
	public GoodListAdapter(Context context,ArrayList<GoodsEntity> listData) {
		// TODO Auto-generated constructor stub
		this.listData = listData;
		this.context = context;
	}
	
	public void updateListViewData(ArrayList<GoodsEntity> listData){
		this.listData = listData;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData==null?0:listData.size();
	}

	@Override
	public GoodsEntity getItem(int position) {
		// TODO Auto-generated method stub
		return listData==null?null:listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GoodsEntity Entity = listData.get(position);
		ViewHodler viewHodler;  
		
		if(convertView == null){
			viewHodler = new ViewHodler();
			convertView = View.inflate(context, R.layout.goodslist_adapter,null);
			viewHodler.name = (TextView) convertView.findViewById(R.id.goodsName);
			
			convertView.setTag(viewHodler);
		}else{
			viewHodler = (ViewHodler) convertView.getTag();
		}
		
		viewHodler.name.setText(" "+Entity.getName());
		
		// TODO Auto-generated method stub
		return convertView;
	}
	
	static class ViewHodler {
		TextView name;
	}
}
