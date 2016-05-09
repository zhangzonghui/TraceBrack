package com.tempus.traceback;

import com.tempus.database.OperationDB;
import com.tempus.entity.LoginGet;
import com.tempus.service.GetJsonData;
import com.tempus.service.HttpUtil;
import com.tempus.service.HttpUtil.OnRequestListener;
import com.tempus.service.ParaseJsonData;
import com.tempus.traceback.ActionbarView.OnActionBtnClickListener;
import com.tempus.utils.CustomProgressDialog;
import com.tempus.utils.LoginUtils;
import com.tempus.utils.ToastUtils;

import android.R.integer;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnActionBtnClickListener,OnClickListener {
	
	private EditText accountNum;
	private EditText passwardNum;
	private String account;
	private Button loginBtn;
	private ActionbarView loginActionbarView;
	private CustomProgressDialog progressDialog = null;


	public LoginActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initActionBarView();
		initView();
	}
	
	
	/**@author==ZZH
	 * ����ActionBar
	 */
	public void initActionBarView(){
		loginActionbarView = (ActionbarView) findViewById(R.id.login_actionbar);
	    loginActionbarView.setLeftbunttonImage(R.drawable.title_left_button_bg);
	    loginActionbarView.setTitle(R.string.login);
	    loginActionbarView.setTitleColor(getResources().getColor(R.color.white));
	    loginActionbarView.setOnActionBtnClickListener(this);
	}
	public void initView(){
		accountNum = (EditText) findViewById(R.id.account);
		passwardNum = (EditText) findViewById(R.id.passward);
		accountNum.setText("admin");
		passwardNum.setText("admin");
		loginBtn = (Button) findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(this);
	}  
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
	
	private Handler UIhandle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			     int what = msg.what;
			     stopProgressDialog();
			     switch (what) {
				case 1://网络异常
					ToastUtils.toastMessage(getResources().getString(R.string.net_error));
					break;
				case 2://登录成功
					Intent initent = new Intent(LoginActivity.this,SlidingMenuActivity.class);
					startActivity(initent);
					finish();
					break;
				case 3://登录失败
					ToastUtils.toastMessage(getResources().getString(R.string.login_error));
					break;

				default:
					break;
				}
		}
		
	};
    
	/**
	 * 检测登录账号和密码是否符合
	 * @param account
	 * @param passward
	 * @return
	 */
	public boolean checkLoginInput(String account,String passward){
		boolean flag = false;
		if(account != null && passward !=null 
				&& !account.equals("") && !passward.equals("")){
			flag =true;
		}else{
			ToastUtils.toastMessage(ThisApp.getInstance()
					.getResources().getString(R.string.null_login_message));
		}
		return flag;
	}
	
	/**
	 * 保存登录转
	 * @return
	 */
    public boolean saveLoginFlag(String name){
    	boolean flag = false;
    	if(LoginUtils.setLoginFlag(LoginActivity.this, 1)){
    		flag = true;
    	};
    	LoginUtils.setLoginName(LoginActivity.this, name);
    	return flag;
    }   
    
    /**
     * 开始ProgressDialog()
     */
    private void startProgressDialog(){
		if (progressDialog == null){
    	   progressDialog = CustomProgressDialog.createDialog(this);
    	   progressDialog.setMessage(ThisApp.getInstance()
    			   .getResources().getString(R.string.intalling));
    	}
    	progressDialog.show();
    }
    
    /**
     * 结束ProgressDialog()
     */
    private void stopProgressDialog(){
	  if (progressDialog != null){
    	progressDialog.dismiss();
    	progressDialog = null;
      }
    }

    
    
    public void saveLoginMessage(String data){
    	LoginGet entity = null;
    	entity = ParaseJsonData.getInstance(LoginActivity.this)
    			.ParaseLoginGetData(data);
    	if(entity != null){
    		String ssid = entity.getSessionid();
    		if(ssid != null && !ssid.equals("")){
              if(OperationDB.getInstance(LoginActivity.this)
                      .checkUserExist(ssid)){
            	 OperationDB.getInstance(LoginActivity.this).UpdateUserListData(entity);
              }else{
            	  OperationDB.getInstance(LoginActivity.this).addUserListData(entity); 
              }  
                
    		}
    	}
    	
    /*	LoginGet endata = OperationDB.getInstance(LoginActivity.this)
    			.queryUserData(LoginUtils.getLoginName(LoginActivity.this));
    	Log.e("+++++++++++++++++endata", endata.getSessionid()+"");*/
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_btn:
		    account = accountNum.getText().toString();
			String passward = passwardNum.getText().toString();
			//
			if(checkLoginInput(account, passward)){
				startProgressDialog();
				//HttpUtil.getInstance(this).HttpTest();
				HttpUtil.getInstance(LoginActivity.this)
				  .requestHttp(HttpUtil.URL_LOGIN,GetJsonData.getInstance(LoginActivity.this)
						  .getLoginUploadData(account,passward, true)
						  , new OnRequestListener() {
					
					@Override  
					public void onResult(int statusCode, String str) {
						// TODO Auto-generated method stub
						Message message = new Message();
						if(statusCode == 0){
							saveLoginFlag(account);
							//saveLoginMessage(str);
							message.what = 2;
						}else if(statusCode == 1){
							message.what = 1;
						}else{
							message.what = 3;
						}
						
						UIhandle.sendMessage(message);
					}
				});
			}else{
				ToastUtils.toastMessage(ThisApp.getInstance().getResources()
						.getString(R.string.null_login_message));
			}
			
			break;

		default:
			break;
		}
	}
	
	

}
