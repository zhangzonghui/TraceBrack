package com.tempus.utils;

import com.tempus.traceback.ThisApp;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;



public class LoginUtils {
	
	/**
	 * 保存登录状态
	 * @param context
	 * @param value=1成功，value=2失败
	 * @return
	 */
	public static boolean setLoginFlag(Context context,int value){
		boolean flag =false;
		if(context != null && value != 0){
			SharedPreferences sharedPreferences  = context
					.getSharedPreferences("Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.edit().putInt("logginflag", value).commit();
		}
		
		return flag;			
	}
	
	/**
	 * 获取登录状态
	 * @param context
	 * @return
	 */
	public static boolean getLoginFlag(Context context){
		boolean flag =false;
		if(context != null ){
			
			SharedPreferences sharedPreferences  = context
					.getSharedPreferences("Loginshare", Context.MODE_PRIVATE);
			int value = sharedPreferences.getInt("logginflag", 0);
			if(value == 1){
				flag = true;
			}
		}
		return flag;
	}
	
	
	/**
	 * 保存登录账号
	 * @param context
	 * @param value
	 * @return
	 */
	public static boolean setLoginName(Context context,String name){
		boolean flag =false;
		if(context != null && name != null && !name.equals("")){
			SharedPreferences sharedPreferences  = context
					.getSharedPreferences("Loginshare", Context.MODE_PRIVATE);
			flag = sharedPreferences.edit().putString("logginname", name).commit();
		}
		
		return flag;			
	}
	
	/**
	 * 获取登录账号
	 * @param context
	 * @return
	 */
	public static String getLoginName(Context context){
		String flag = null;
		if(context != null ){
			
			SharedPreferences sharedPreferences  = context
					.getSharedPreferences("Loginshare", Context.MODE_PRIVATE);
		    flag = sharedPreferences.getString("logginname", "");
		}
		return flag;
	}
	
	
	
	
	

}
