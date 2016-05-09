package com.tempus.traceback;


import com.tempus.traceback.ActionbarView.OnActionBtnClickListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/***
 * 
 * @author==ZZH
 * ��ҳ��
 */

public class MainActivity extends Activity implements OnClickListener,OnActionBtnClickListener {
	public static final String Tag = MainActivity.class.getName()+"tag";
	private ActionbarView mainActionbarView;
	private TextView erweima_scan;
	private ImageView nfc_scan;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_content);
		initActionBarView();
		initView();
	}
	
	public void initView(){
		//erweima_scan= (TextView) findViewById(R.id.erweima);
	//	nfc_scan = (ImageView) findViewById(R.id.nfc);
		erweima_scan.setOnClickListener(this);
		nfc_scan.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	/**@author==ZZH
	 * ����ActionBar
	 */
	public void initActionBarView(){
		mainActionbarView = (ActionbarView) findViewById(R.id.main_actionbar);
	    mainActionbarView.setLeftbunttonImage(R.drawable.title_left_button_bg);
	    mainActionbarView.setTitle(R.string.nfc_title);
	    mainActionbarView.setTitleColor(getResources().getColor(R.color.white));
	    mainActionbarView.setOnActionBtnClickListener(this);
	}

	@Override
	public void onLeftBtnClick(View view) {
		// TODO Auto-generated method stub
		finish();
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
     * 页面点击响应处理
     */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.erweima_bt:
			JumpActivity(CaptureActivity.class);
			break;
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
		Intent intent = new Intent(MainActivity.this,context);
		
		startActivity(intent);
	}
	
	
}
