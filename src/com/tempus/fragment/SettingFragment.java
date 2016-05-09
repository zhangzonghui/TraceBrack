package com.tempus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tempus.traceback.ActionbarView;
import com.tempus.traceback.ActionbarView.OnActionBtnClickListener;
import com.tempus.traceback.LoginActivity;
import com.tempus.traceback.R;
import com.tempus.traceback.SlidingMenuActivity;
import com.tempus.traceback.StartupActivity;
import com.tempus.utils.LoginUtils;

public class SettingFragment extends Fragment implements OnActionBtnClickListener,OnClickListener {
	private View fragment_view;
	private TextView loginOut;
	private ActionbarView settingActionbarView;
	
	public SettingFragment() {
		setRetainInstance(true);
	}
	
	public static Fragment newInstance() {
		SettingFragment fragment = new SettingFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragment_view = inflater.inflate(R.layout.fragment_setting,
				container, false);
		initView(fragment_view);
		initActionBarView(fragment_view);
		hideTabLIne();
		return fragment_view;
	}
	
	
	public void initView(View rootview){
		loginOut = (TextView) rootview.findViewById(R.id.longin_out);
		loginOut.setOnClickListener(this);
	}
	
	/**@author==ZZH
	 * ����ActionBar
	 */
	public void initActionBarView(View view){
		settingActionbarView = (ActionbarView) view.findViewById(R.id.setting_actionbar);
	    settingActionbarView.setLeftbunttonImage(R.drawable.left_arrow);
	    settingActionbarView.setTitle(R.string.setting);
	    settingActionbarView.setTitleColor(getResources().getColor(R.color.white));
	    settingActionbarView.setOnActionBtnClickListener(this);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	/**
	 * 显示底部导航栏
	 */
	private void hideTabLIne() {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof SlidingMenuActivity) {
			SlidingMenuActivity fca = (SlidingMenuActivity) getActivity();
			fca.HidenTableLine();
		} 
	}
	
	/**
	 * 保存登录转
	 * @return
	 */
    public boolean saveLoginFlag(){
    	boolean flag = false;
    	if(LoginUtils.setLoginFlag(getActivity(),2)){
    		flag = true;
    	};
    	return flag;
    } 

	@Override
	public void onLeftBtnClick(View view) {
		// TODO Auto-generated method stub
		getActivity().getSupportFragmentManager().popBackStack();  
	}

	@Override
	public void onRightBtnClick(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightSecondBtnClick(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.longin_out:
			if(saveLoginFlag()){
				Intent newintent = new Intent(getActivity(),
						LoginActivity.class);
				startActivity(newintent);
				getActivity().finish();
			};
			
			break;

		default:
			break;
		}
	}	
}
