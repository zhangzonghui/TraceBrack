package com.tempus.traceback;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ActionbarView extends RelativeLayout {
	
	private Context context;
	
	private TextView titleText;
	
	private ImageButton leftBtn;
	
	private ImageButton rightBtn;
	
	private ImageButton rightSecondBtn;
	
	private OnActionBtnClickListener onActionBtnClickListener;

	public ActionbarView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public ActionbarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}


	public ActionbarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}
	
	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.actionbar_view, this);
		leftBtn = (ImageButton) view.findViewById(R.id.actionbar_left_btn);
		leftBtn.setOnClickListener(onClick);
		titleText = (TextView) view.findViewById(R.id.actionbar_title);
		rightBtn = (ImageButton) view.findViewById(R.id.actionbar_right_btn);
		rightBtn.setOnClickListener(onClick);
		rightSecondBtn = (ImageButton) view.findViewById(R.id.actionbar_right_second_btn);
		rightSecondBtn.setOnClickListener(onClick);
	}
	
	/**
	 * 锟斤拷锟矫憋拷锟斤拷
	 * @param resText 锟斤拷源
	 */
	public void setTitle(int resText) {
		titleText.setText(resText);
	}
	
	/**
	 * 锟斤拷锟矫憋拷锟斤拷
	 * @param text 锟斤拷锟斤拷
	 */
	public void setTitle(CharSequence text) {
		titleText.setText(text);
	}
	/**
	 * 锟斤拷锟矫憋拷锟斤拷锟斤拷锟斤拷锟叫�
	 * @param text 锟斤拷锟斤拷
	 */
	public void setTitleSize(int size) {
		titleText.setTextSize(size);
	}
	
	/**
	 * 锟斤拷锟矫憋拷锟斤拷锟斤拷锟斤拷锟斤拷色
	 * @param text 锟斤拷锟斤拷
	 */
	public void setTitleColor(int size) {
		titleText.setTextColor(size);
	}
	
	/**
	 * 锟斤拷锟斤拷leftbutton锟侥憋拷锟斤拷 
	 * 
	 */
	public void setLeftbunttonImage(int resText) {
		leftBtn.setBackgroundResource(resText);
	}
	
	public void setRightbunttonImage(int resText) {
		rightBtn.setBackgroundResource(resText);
		rightBtn.setVisibility(View.VISIBLE);
		invalidate();
	}
	
	
	public void setRightSecondbunttonImage(int resText) {
		rightSecondBtn.setBackgroundResource(resText);
		rightSecondBtn.setVisibility(View.VISIBLE);
		invalidate();
	}
	/**
	 * 锟斤拷锟斤拷锟揭憋拷button
	 * @param isHidden 锟角凤拷锟斤拷锟斤拷
	 */
	public void hiddenRightBtn(boolean isHidden) {
		if(isHidden) {
			rightBtn.setVisibility(View.GONE);
		} else {
			rightBtn.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 锟斤拷锟斤拷锟揭边第讹拷锟斤拷button
	 * @param isHidden 锟角凤拷锟斤拷锟斤拷
	 */
	public void hiddenRightSecondBtn(boolean isHidden) {
		if(isHidden) {
			rightSecondBtn.setVisibility(View.GONE);
		} else {
			rightSecondBtn.setVisibility(View.VISIBLE);
		}
	}
    
    public OnClickListener onClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.actionbar_left_btn:
				
				if(onActionBtnClickListener == null) {
					/*FragmentActivity activity = (FragmentActivity) context;
					if(activity instanceof BaseSlidingActivity) {
						BaseSlidingActivity slidingActivity = (BaseSlidingActivity) activity;
						slidingActivity.getSlidingMenu().toggle();
				     }*/
				}
				if(onActionBtnClickListener != null) {
					onActionBtnClickListener.onLeftBtnClick(v);
				}
				break;
			case R.id.actionbar_right_btn:
				if(onActionBtnClickListener != null) {
					 
					onActionBtnClickListener.onRightBtnClick(v);
				}
				
				//onActionBtnClickListener.onRightBtnClick(v);
				break;
			case R.id.actionbar_right_second_btn:
				if(onActionBtnClickListener != null) {
					onActionBtnClickListener.onRightSecondBtnClick(v);
				}
				break;
			default:
				break;
			}
		}
	};
	
	public void setOnActionBtnClickListener(
			OnActionBtnClickListener onActionBtnClickListener) {
		this.onActionBtnClickListener = onActionBtnClickListener;
	}
	
	/**
	 * actionbar锟斤拷锟斤拷录锟�
	 */
	public interface OnActionBtnClickListener {
		/**
		 * 锟斤拷button锟斤拷钮锟斤拷锟�
		 * @param view 锟斤拷button
		 */
		void onLeftBtnClick(View view);
		/**
		 * 锟斤拷button锟斤拷锟�
		 * @param view 锟斤拷button
		 */
		void onRightBtnClick(View view);
		/**
		 * 锟揭讹拷button锟斤拷锟�
		 * @param view 锟揭讹拷button
		 */
		void onRightSecondBtnClick(View view);
	}
}
