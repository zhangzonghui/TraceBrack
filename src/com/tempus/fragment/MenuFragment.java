package com.tempus.fragment;

import java.util.ArrayList;

import com.tempus.adapter.MenuListAadapter;
import com.tempus.entity.MenuEntity;
import com.tempus.traceback.R;
import com.tempus.traceback.SlidingMenuActivity;

import android.R.integer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MenuFragment extends Fragment {
    private ListView memulist;
    private ImageView personImg;
    private TextView personName;
    private TextView systemName;
    private MenuListAadapter adapter;
    private ArrayList<MenuEntity> listData = new ArrayList<MenuEntity>();
    private String[] str_entity = new String[]{"设置","版本"};
    private int[] img_entity = new int[]{R.drawable.setup,R.drawable.version};
    private int[] type_entity = new int[]{1,2};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.menulist_frame, container, false);
		initView(rootView);
		initData();
		initMenuListData();
		return rootView;
	}

	public void initView(View root){
		memulist = (ListView) root.findViewById(R.id.menu_list);
		personImg = (ImageView) root.findViewById(R.id.persion_img);
		personName = (TextView) root.findViewById(R.id.persion_name);
		systemName = (TextView) root.findViewById(R.id.system_name);
	}
	
	public void initData(){
		for(int i=0;i<str_entity.length;i++){
			MenuEntity entity = new MenuEntity();
			entity.setName(str_entity[i]);
			entity.setIcion(img_entity[i]);
			entity.setType(type_entity[i]);
			listData.add(entity);
		}   
	}
	
	public void initMenuListData(){
		adapter = new MenuListAadapter(getActivity(),listData);
		memulist.setAdapter(adapter);
		memulist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Fragment newContent = null;
				/*int type = 0;
				MenuEntity entity = (MenuEntity) adapter.getItem(arg2);
				if(entity != null){
					type = entity.getType();
				}*/
				Log.e("+++++++++++onItemClick", "arg2:"+arg2);
				switch (arg2) {
				case 0:
					newContent = SettingFragment.newInstance();
					break;
				
				case 1:
					newContent = VersionFragment.newInstance();
					break;
				
				}
				Log.e("+++++++++++onItemClick", "newContent:"+""+newContent.toString());
				if (newContent != null)
					switchFragment(newContent);  
			}
		});
	}

	

	// �л�Fragment��ͼ��ring
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null){
			Log.e("+++++++++++switchFragment", "fragment:"+""+fragment.toString());
			return;
		}
		if (getActivity() instanceof SlidingMenuActivity) {
			
			SlidingMenuActivity fca = (SlidingMenuActivity) getActivity();
			Log.e("+++++++++++getActivity", "fca:"+""+fca.getClass());
			fca.switchContent(fragment);
		} 
	}


}
