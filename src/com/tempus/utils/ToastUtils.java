package com.tempus.utils;

import com.tempus.traceback.ThisApp;

import android.os.Build;



public class ToastUtils {
	static TipsToast tipsToast;
	public static void toastMessage(String message) {
		showTips(message);
	}
	private static void showTips(String message) {
		if (tipsToast != null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				tipsToast.cancel();
			}
		} else {
			tipsToast = TipsToast.makeText(ThisApp.getInstance(), message, TipsToast.LENGTH_LONG);
		}
		tipsToast.show();
		tipsToast.setText(message);
	}
	

}
