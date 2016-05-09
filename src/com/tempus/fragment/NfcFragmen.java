package com.tempus.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tempus.traceback.ActionbarView;
import com.tempus.traceback.ActionbarView.OnActionBtnClickListener;
import com.tempus.traceback.R;
import com.tempus.traceback.ReadActivity;
import com.tempus.traceback.SlidingMenuActivity;

/***
 * 
 * @author==ZZH
 * ��ҳ��
 */

 public class NfcFragmen extends Fragment implements OnClickListener,OnActionBtnClickListener {
	public static final String Tag = NfcFragmen.class.getName()+"tag";
	private ActionbarView mainActionbarView;
	private ImageView nfc_scan;
	private View fragment_view;
	private OnnfcMenuListener menuListener;
    
	public NfcFragmen() {
	}
	
	public NfcFragmen(OnnfcMenuListener listener) {
		this.menuListener = listener;
		setRetainInstance(true);
	}
	
	public static Fragment newInstance(OnnfcMenuListener listener) {
		NfcFragmen fragment = new NfcFragmen(listener);
		//fragment.setArguments(new Bundle());
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		// construct the RelativeLayout
		fragment_view = inflater.inflate(R.layout.fragment_nfc,
				container, false);
		initActionBarView(fragment_view);
		initView(fragment_view);
		showTabLIne();
		return fragment_view;
	}
	
	public void initView(View view){
		nfc_scan = (ImageView) view.findViewById(R.id.img_bt);
		nfc_scan.setOnClickListener(this);
	}

	
	
	/**@author==ZZH
	 * ����ActionBar
	 */
	public void initActionBarView(View view){
		mainActionbarView = (ActionbarView) view.findViewById(R.id.main_actionbar);
	    mainActionbarView.setLeftbunttonImage(R.drawable.actionbar_list_ico);
	    mainActionbarView.setTitle(R.string.nfc_title);
	    mainActionbarView.setTitleColor(getResources().getColor(R.color.white));
	    mainActionbarView.setOnActionBtnClickListener(this);
	}

	@Override
	public void onLeftBtnClick(View view) {
		// TODO Auto-generated method stub
		//finish();
		menuListener.onResult(1);
	}

	@Override
	public void onRightBtnClick(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRightSecondBtnClick(View view) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 显示底部导航栏
	 */
	private void showTabLIne() {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof SlidingMenuActivity) {
			SlidingMenuActivity fca = (SlidingMenuActivity) getActivity();
			fca.showTableLine();
		} 
	}

	
    /**
     * 页面点击响应处理
     */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
        case R.id.img_bt:
        	JumpActivity(ReadActivity.class);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 页面跳转方法
	 * @param context
	 */
	public void JumpActivity(Class<?> context){
		Intent intent = new Intent(getActivity(),context);
		
		startActivity(intent);
	}
	
	/**
	 * 请求回调
	 * @author lenovo
	 *
	 */
	public interface OnnfcMenuListener {
		/**
		 * 
		 * @param statusCode:0为成功直接解析就可以，其他直接将错误信息taost给用户
		 * @param str：服务器返回的数据或者msg错误信息
		 */
		void onResult(int statusCode);
	}
	
	
}
