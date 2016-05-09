package com.tempus.traceback;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.tempus.fragment.ColorFragment;
import com.tempus.fragment.ContentFragmen;
import com.tempus.fragment.ContentFragmen.OnMenuListener;
import com.tempus.fragment.MenuFragment;
import com.tempus.fragment.NfcFragmen;
import com.tempus.fragment.NfcFragmen.OnnfcMenuListener;

/**
 * 
 * @author yechao
 * @说明 通过Fragment来改变主视图的界面内容。
 */
public class SlidingMenuActivity extends SlidingFragmentActivity implements OnClickListener {

	private Fragment mContent;
	private TextView erweimaTextView;
	private TextView nfcTextView;
	private LinearLayout tableLinear;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Change Fragment");
		setContentView(R.layout.activity_main);
		initView();
		initSlidingMenu(savedInstanceState);

		//getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void initView(){
		tableLinear = (LinearLayout) findViewById(R.id.table_line);
		erweimaTextView = (TextView) findViewById(R.id.erweima_scan);
		nfcTextView = (TextView) findViewById(R.id.nfc_scan);
		erweimaTextView.setOnClickListener(this);
		nfcTextView.setOnClickListener(this);
		erweimaTextView.setBackgroundColor(getResources().getColor(R.color.table));
		
	}
	
	

	/**
	 * 初始化滑动菜单
	 */
	private void initSlidingMenu(Bundle savedInstanceState) {
		// 如果保存的状态不为空则得到ColorFragment，否则实例化ColorFragment
		
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null)
			mContent =  ContentFragmen.newInstance(listener);
		// 设置主视图界面
		
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_fragment, mContent).commit();

		// 设置滑动菜单视图界面
		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MenuFragment()).commit();

		// 设置滑动菜单的属性值
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);
		getSlidingMenu().setShadowDrawable(R.drawable.shadow);
		getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
		getSlidingMenu().setFadeDegree(0.35f);

	}
	
	/**
	 * 隐藏底部导航栏
	 */
	public void HidenTableLine(){
		if(tableLinear != null){
			tableLinear.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 展示底部导航栏
	 */
	public void showTableLine(){
		if(tableLinear != null){
			tableLinear.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 切换Fragment，也是切换视图的内容
	 */
	public void switchContent(Fragment fragment) {

		FragmentTransaction trans = getSupportFragmentManager()
				.beginTransaction();
		if (fragment.getClass() == NfcFragmen.class
				|| fragment.getClass() == ContentFragmen.class) {
			if (mContent.getClass() != fragment.getClass()) {
				// getSupportFragmentManager().get
				mContent = fragment;

				if (fragment.getClass() != ContentFragmen.class
						&& fragment.getClass() != NfcFragmen.class) {
					trans.addToBackStack(null);
				}  

			}
		} else {
			if (fragment.getClass() != ContentFragmen.class
					&& fragment.getClass() != NfcFragmen.class) {
				trans.addToBackStack(null);
			}

		}

		trans.replace(R.id.content_fragment, fragment).commit();
		getSlidingMenu().showContent();

	}
	
	/**
	 * 判断当前FragMent
	 */
	public void containFragment(Fragment fragment) {
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		trans.addToBackStack(null);
		trans.replace(R.id.content_fragment, fragment).commit();
		fragment.getClass();
		getSlidingMenu().showContent();
	}

	/**
	 * 保存Fragment的状态
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
	private OnnfcMenuListener nfclistener = new OnnfcMenuListener(){

		@Override
		public void onResult(int statusCode) {
			// TODO Auto-generated method stub
			toggle();
		}
		
	};
	
	private OnMenuListener listener = new OnMenuListener(){

		@Override
		public void onResult(int statusCode) {
			// TODO Auto-generated method stub
			toggle();
		}
		
	};

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.erweima_scan:
			nfcTextView.setBackgroundColor(0);
        	erweimaTextView.setBackgroundColor(getResources().getColor(R.color.table));
        	switchContent(ContentFragmen.newInstance(listener));
			break;
        case R.id.nfc_scan:
        	erweimaTextView.setBackgroundColor(0);
        	nfcTextView.setBackgroundColor(getResources().getColor(R.color.table));
        	switchContent( NfcFragmen.newInstance(nfclistener));
			break;

		default:
			break;
		}
	}
	
	
	
	
}
