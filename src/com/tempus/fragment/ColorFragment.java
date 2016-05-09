package com.tempus.fragment;

import com.tempus.traceback.R;
import com.tempus.traceback.SlidingMenuActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class ColorFragment extends Fragment {
	
	private int mColorRes = -1;
	
	public ColorFragment() { 
		this(R.color.white);
	}
	
	public ColorFragment(int colorRes) {
		mColorRes = colorRes;
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null)
			mColorRes = savedInstanceState.getInt("mColorRes");
		
		int color = getResources().getColor(mColorRes);
		
		// construct the RelativeLayout
		RelativeLayout v = new RelativeLayout(getActivity());
		v.setBackgroundColor(color);
		hideTabLIne();
		return v;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mColorRes", mColorRes);
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
}
