package com.tempus.utils;


import com.tempus.traceback.R;
import com.tempus.traceback.ThisApp;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;


public class TipsToast extends Toast {

	public TipsToast(Context context) {
		super(context);
	}
	
	public static TipsToast makeText(Context context,
			CharSequence text, int duration) {
		TipsToast result = new TipsToast(context);
		// LayoutInflater inflate = (LayoutInflater)
		// context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View v = inflate.inflate(R.layout.view_tips, null);
		// TextView tv = (TextView) v.findViewById(R.id.tips_msg);
		TextView tv = new TextView(context);
		tv.setTag("textview");
		tv.setPadding(20, 20, 20, 20);
		tv.setBackgroundColor(ThisApp.getInstance()
				.getResources().getColor(R.color.black));
		tv.setText(text);
		tv.setTextSize(15);
		result.setView(tv);
		
		// setGravity方法用于设置位置，此处为垂直居中
		result.setGravity(Gravity.BOTTOM, 0, 100);
		result.setDuration(duration);
		return result;
	}

	@Override
	public void setText(CharSequence s) {
		if (getView() == null) {
			throw new RuntimeException(
					"This Toast was not created with Toast.makeText()");
		}
		TextView tv = (TextView) getView().findViewWithTag("textview");
		if (tv == null) {
			throw new RuntimeException(
					"This Toast was not created with Toast.makeText()");
		}
		if(s.toString() != tv.getText().toString()){
			tv.setText(s);
		}
	}
}
