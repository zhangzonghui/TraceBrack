package com.tempus.utils;

import com.tempus.service.HttpUtil;

import android.content.Context;

public class NfcUtil {
	private  Context context;
    private static NfcUtil instance;
	public NfcUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	/**
	 * 单例模式
	 * @param context
	 * @return
	 */
	public static NfcUtil getInstance(Context context) {
		if (instance == null){
			instance = new NfcUtil(context);
		}
			
	   return instance;
	}
	
	
	
	   
	
	

}
