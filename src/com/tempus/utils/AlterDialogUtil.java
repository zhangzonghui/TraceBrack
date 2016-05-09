package com.tempus.utils;


import com.tempus.traceback.R;
import com.tempus.traceback.ThisApp;

import android.R.integer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;


public class AlterDialogUtil {
	
	private static AlterDialogUtil instance;
	private Context context;
	public AlterDialogUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public static AlterDialogUtil getInstance(Context context){
		if(instance == null){
			instance = new AlterDialogUtil(context);
		}
		
		return instance;
	}
	
	
	public void dialog(String title,final OrequestAlterDialog listener) {
		  AlertDialog.Builder builder = new Builder(context);
		  builder.setMessage(title);
		 
		  builder.setTitle(ThisApp.getInstance().getResources()
				  .getString(R.string.sure_good));
		 
		  builder.setPositiveButton(ThisApp.getInstance().getResources()
				  .getString(R.string.sure_btn), new OnClickListener() {
		 
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
			   
			   listener.onResult(1);
		    dialog.dismiss();
		 
		   }
		  });
		 
		  builder.setNegativeButton(ThisApp.getInstance().getResources()
				  .getString(R.string.cancle_btn), new OnClickListener() {
		 
		   @Override
		   public void onClick(DialogInterface dialog, int which) {
			   listener.onResult(2);
		         dialog.dismiss();
		    
		   }
		  });
		 
		  builder.create().show();
	}
	
	
	public interface OrequestAlterDialog{
		
		void onResult(int statcode);
	}
	
	

	
	

}
