package com.tempus.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tempus.traceback.ActionbarView;
import com.tempus.traceback.ActionbarView.OnActionBtnClickListener;
import com.tempus.traceback.R;
import com.tempus.traceback.SlidingMenuActivity;

public class VersionFragment extends Fragment implements OnActionBtnClickListener,OnClickListener {
	private View fragment_view;
	private ActionbarView versionActionbarView;
	
	public VersionFragment() {
		setRetainInstance(true);
	}
	
	public static Fragment newInstance() {
		VersionFragment fragment = new VersionFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragment_view = inflater.inflate(R.layout.fragment_version,
				container, false);
		initView(fragment_view);
		initActionBarView(fragment_view);
		hideTabLIne();
		return fragment_view;
	}
	
	
	public void initView(View rootview){
		
	}
	
	/**@author==ZZH
	 * ����ActionBar
	 */
	public void initActionBarView(View view){
		versionActionbarView = (ActionbarView) view.findViewById(R.id.version_actionbar);
	    versionActionbarView.setLeftbunttonImage(R.drawable.left_arrow);
	    versionActionbarView.setTitle(R.string.setting);
	    versionActionbarView.setTitleColor(getResources().getColor(R.color.white));
	    versionActionbarView.setOnActionBtnClickListener(this);
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
		
	}	
}
