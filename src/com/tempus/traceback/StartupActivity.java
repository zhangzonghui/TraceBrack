package com.tempus.traceback;

import java.lang.ref.WeakReference;

import com.tempus.utils.LoginUtils;



import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class StartupActivity extends Activity {
	private int total = 0;
	public int UPDATE_LOADING_MSG = 1;
	MyInnerHandler myHandler = new MyInnerHandler(this);
	public StartupActivity() {
		// TODO Auto-generated constructor stub
	}

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {  
			finish();
			return;
		 }
	}
	
	private static class MyInnerHandler extends Handler {
		WeakReference<StartupActivity> mActivity;

		MyInnerHandler(StartupActivity activity) {
			mActivity = new WeakReference<StartupActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			StartupActivity activity = mActivity.get();
			activity.doHandlerJob();
		}
	}
	
	protected void doHandlerJob() {
		if (total < 3) {
			total++;
			myHandler.sendEmptyMessageDelayed(total, 500);
		} else {
			/*
			 * UserPreferences mPreferencesr = new UserPreferences(this); String
			 * loginflag = mPreferencesr.getCurrentLoginStatus();
			 */
			//LoginUtils.getLoginFlag(this)
			if (LoginUtils.getLoginFlag(this)) {
				// initData(mPreferencesr);// 加载用户数据（将基本数据加载到内存中THISAPP中）
				Intent intent = new Intent(StartupActivity.this,
						SlidingMenuActivity.class);
				startActivity(intent);
				this.finish();
			} else {
				// 进入登录、注册页面
				Intent newintent = new Intent(StartupActivity.this,
						LoginActivity.class);
				startActivity(newintent);
				this.finish();
			}
		}
	}



	@Override
	public void onPause() {
		super.onPause();
		if(myHandler != null)
			myHandler.removeCallbacksAndMessages(null);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(myHandler != null)
			myHandler.removeCallbacksAndMessages(null);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		total = 0;
		if(myHandler != null)
			myHandler.sendEmptyMessageDelayed(UPDATE_LOADING_MSG, 0);
	}
	
	

}
