package com.tempus.traceback;

import java.io.File;
import java.util.LinkedList;
import java.util.List;



import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;

/**
 * @author==ZZH
 * @author lenovo
 *
 */

public class ThisApp extends Application {

	private static Context context;
	private List<Activity> mList = new LinkedList<Activity>();
	private static ThisApp instance;
	
    @Override
	public void onCreate() {
		super.onCreate();
		instance = this;

	 }
	
	/**
	 * ��ȡ������
	 * ==ZZH
	 * @return
	 */
   public synchronized static ThisApp getInstance() {
		if (null == instance) {
			instance = new ThisApp();
		}
		return instance;   
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}
    
	
	/**
	 * �˳�APP����
	 * ==ZZH
	 */
	public void exit() {

		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}
	
	
}
