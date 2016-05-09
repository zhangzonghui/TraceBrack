package com.tempus.traceback;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Entity;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.tempus.adapter.GoodListAdapter;
import com.tempus.entity.GoodsEntity;
import com.tempus.entity.LableEntity;
import com.tempus.location.LocationUtil;
import com.tempus.service.GetJsonData;
import com.tempus.service.HttpUtil;
import com.tempus.service.HttpUtil.OnRequestListener;
import com.tempus.service.ParaseJsonData;
import com.tempus.traceback.ActionbarView.OnActionBtnClickListener;
import com.tempus.utils.AlterDialogUtil;
import com.tempus.utils.AlterDialogUtil.OrequestAlterDialog;
import com.tempus.utils.ByteUtil;
import com.tempus.utils.CustomProgressDialog;
import com.tempus.utils.ToastUtils;


public class Write2Nfc extends Activity implements OnClickListener,OnActionBtnClickListener {
	private TextView noteText;
	private IntentFilter[] mWriteTagFilters;
	private NfcAdapter nfcAdapter;
	PendingIntent pendingIntent;
	String[][] mTechLists;
	private Boolean ifWrite;
    private ActionbarView writeActionbar;
    private ListView goodsList;
    private ArrayList<GoodsEntity> listviewData = new ArrayList<GoodsEntity>();
    private GoodListAdapter listAdapter;
    private EditText search_goods;
    private TextView searchBt;
    private CustomProgressDialog progressDialog = null;
    private String Lablestr;
    private String lableId;
    private String goodsId;
    private String goodsName;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_ifo);
		initActionBarView();
		initView();
		initNfc();
		initGoodsListView();
		displayControl(2);
		System.out.println("0....");
	}
	
	/**@author==ZZH
	 * ����ActionBar
	 */
	public void initActionBarView(){
		writeActionbar = (ActionbarView) findViewById(R.id.write_actionbar);
	    writeActionbar.setLeftbunttonImage(R.drawable.title_left_button_bg);
	    writeActionbar.setRightbunttonImage(R.drawable.saomiao);
	    writeActionbar.setTitle(R.string.writenfc_title);
	    writeActionbar.setTitleColor(getResources().getColor(R.color.white));
	    writeActionbar.setOnActionBtnClickListener(this);
	}
	 /**
	  * 初始化页面
	  */
	public void initView(){
		noteText=(TextView)findViewById(R.id.noteText);
		goodsList = (ListView) findViewById(R.id.goods_list);
		search_goods = (EditText) findViewById(R.id.search_ed);
		searchBt = (TextView) findViewById(R.id.search_tx);
		searchBt.setOnClickListener(this);
		
		search_goods.setText("测试商品");//测试
	}
	
	/**
	 * 加载商品列表数据
	 */
	public void initGoodsListView(){
		
		listAdapter = new GoodListAdapter(this,listviewData);
		goodsList.setAdapter(listAdapter);
		
		//商品列表点击事件
		goodsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Message mess = new Message();
				mess.what =4;
				mess.obj = listAdapter.getItem(arg2).getName();
				goodsName = listAdapter.getItem(arg2).getName();
				goodsId = listAdapter.getItem(arg2).getId();
				UIhandle.sendMessage(mess);
			}
		});
		
		
	}
    
	/***
	 * 加载NFC数据
	 */
	private void initNfc() {
		// TODO Auto-generated method stub
		ifWrite = false;
		
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		ndef.addCategory("*/*");
		mWriteTagFilters = new IntentFilter[] { ndef };
		mTechLists = new String[][] { new String[] { NfcA.class.getName() },
				new String[] { NfcF.class.getName() },
				new String[] { NfcB.class.getName() },
				new String[] { NfcV.class.getName() } };
	}
	
	
	/**
	 * 隐藏和显示商品列表和提示语
	 * @param ifWriting
	 */
	public void displayControl(int flag){
		if(flag == 1){			
			noteText.setVisibility(View.VISIBLE);
			goodsList.setVisibility(View.GONE);
		}else if(flag==2){
			noteText.setVisibility(View.GONE);
			goodsList.setVisibility(View.VISIBLE);
		}else{
			goodsList.setVisibility(View.GONE);
			noteText.setVisibility(View.GONE);
		}  
		
	}
	
	
	 /**
     * 开始ProgressDialog()
     */
    private void startProgressDialog(String mess){
		if (progressDialog == null){
    	   progressDialog = CustomProgressDialog.createDialog(Write2Nfc.this);
    	   progressDialog.setMessage(mess);
    	}
    	progressDialog.show();
    }
    
    /**
     * 结束ProgressDialog()
     */
    private void stopProgressDialog(){    
	  if (progressDialog != null){
    	  progressDialog.dismiss();      
		  //progressDialog.cancel();
         progressDialog = null;
      }
    }
    
    /**
     * UI更新数据Handle
     */
    private Handler UIhandle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			int what = msg.what;
			switch (what) {
			case 1://开始搜索商品
				startProgressDialog(ThisApp.getInstance()
		    			   .getResources().getString(R.string.intalling));
				break;
			case 2://跟新商品列表
				String requeststr = msg.obj.toString();
				listviewData = initGoodListData(requeststr);
				if(listviewData != null && listviewData.size()>0){
					listAdapter.updateListViewData(listviewData);
				}else{
					ToastUtils.toastMessage(ThisApp.getInstance().getResources()
							.getString(R.string.search_fail));
				}
				displayControl(2);
				stopProgressDialog();
			    break;
			case 3://搜索商品失败，停止对话框
				stopProgressDialog();
				ToastUtils.toastMessage(ThisApp.getInstance().getResources()
						.getString(R.string.search_fail));
				break;
			case 4://show 商品确认对话框
				String title = msg.obj.toString();
				if(title != null && !title.equals("")){
					AlterDialogUtil.getInstance(Write2Nfc.this).dialog(title,AlterListener);
				}
				break;
			case 5://确定/请求获取写入标签/show 请求进度框
				if(goodsName != null && !goodsName.equals("")
				&&goodsId != null && !goodsId.equals("")){
					getlableData(goodsId,goodsName);
				}else{
					ToastUtils.toastMessage(ThisApp.getInstance()
			    			   .getResources().getString(R.string.no_goodName));
				}
				
				startProgressDialog(ThisApp.getInstance()
			    			   .getResources().getString(R.string.getgoodlabel));
				
				break;
			case 6://响应对话框取消按钮  
				
				break;
			case 7: //解析获取到标签
				String lablerstr = msg.obj.toString();
				
				ArrayList<LableEntity> lableList = ParaseJsonData
						.getInstance(Write2Nfc.this).parseGoodLableList(lablerstr);
				if(lableList != null){
					if(lableList.size()>0){
						Lablestr = lableList.get(0).getSecurityCode();
						lableId = lableList.get(0).getId();
						ifWrite = true;
						displayControl(1);  
					}
				}
				stopProgressDialog();
				 break;
			   
			case 8 ://请求标签失败
				ToastUtils.toastMessage(ThisApp.getInstance().getResources()
						.getString(R.string.no_goods_label));
				stopProgressDialog();
			    break;
			case 9 :
				Lablestr =null;
				lableId  = null;
				displayControl(3);  
				ToastUtils.toastMessage(ThisApp.getInstance().getResources()
						.getString(R.string.write_label_sucess));
				stopProgressDialog();
			 break;
			case 10:
				Lablestr =null;
				lableId  = null;
				displayControl(3);  
				ToastUtils.toastMessage(ThisApp.getInstance().getResources()
						.getString(R.string.write_label_failed));
				stopProgressDialog();
			 break;
			case 11:
				String str = msg.obj.toString();
				
				GoodsEntity Entity = initGoodData(str);
				if(Entity != null){
					listviewData.clear();
					listviewData.add(Entity);
				}else{
					ToastUtils.toastMessage(ThisApp.getInstance().getResources()
							.getString(R.string.search_fail));
				}
				listAdapter.updateListViewData(listviewData);
				displayControl(2);
				stopProgressDialog();
				break;
			case 12:
				stopProgressDialog();
				ToastUtils.toastMessage(ThisApp.getInstance().getResources()
						.getString(R.string.search_fail));
				break;

			default:
				break;
			}
		}
    	
    };
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		nfcAdapter.enableForegroundDispatch(this, pendingIntent,
				mWriteTagFilters, mTechLists);
		System.out.println("1....");
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Write2Tag(intent);
	}
	
	/**
	 * 通過Intent向Tag寫入標籤
	 * @param intent
	 */
	public void Write2Tag(Intent intent){
		String text = Lablestr;//;editText.getText().toString();
		if (text == null) {
			ToastUtils.toastMessage(ThisApp.getInstance()
					.getResources().getString(R.string.no_message));
            return;
		}
		if (ifWrite == true) {
			ifWrite = false;
			if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())||
					NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
				Tag tag =intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				
				Ndef ndef = Ndef.get(tag);
				try {
					//数据的写入过程一定要有连接操作
					if(ndef != null){
						ndef.connect();  
						String nfcId = ByteUtil.ByteArrayToHexString(tag.getId());
						//构建数据包，也就是要写入标签的数据
						NdefRecord ndefRecord = new NdefRecord(
								NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
								new byte[] {}, text.getBytes());
						NdefRecord[] records = { ndefRecord };
						NdefMessage ndefMessage = new NdefMessage(records);
						ndef.writeNdefMessage(ndefMessage);
						ToastUtils.toastMessage(ThisApp.getInstance()
								.getResources().getString(R.string.write_message_sucess));
						
						displayControl(1);
						
						startProgressDialog(ThisApp.getInstance()
				    			   .getResources().getString(R.string.uploadgoodlabel));
						Writelable(nfcId,Lablestr,lableId,"","");
					}else{
						ToastUtils.toastMessage(ThisApp.getInstance()
								.getResources().getString(R.string.write_message_fail));
					}  
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FormatException e) {
					
				}
			}
		}else{
			ToastUtils.toastMessage(getResources()
					.getString(R.string.no_goods_lable));
		}
	}
	
	
	/**
	 * 请求获取商品列表  
	 */
	public void getSearchData(){
		
			if(search_goods != null){
				String editStr = search_goods.getText().toString();
				if(editStr != null && !editStr.equals("")){
					JSONObject jsonString = GetJsonData.getInstance(this)
						       .getGoodListStr(editStr,"");
					HttpUtil.getInstance(Write2Nfc.this).requestHttp(HttpUtil.URL_GOODLIST
							,jsonString,listener);
				 }else{
					 ToastUtils.toastMessage(ThisApp.getInstance().getResources()
							 .getString(R.string.no_input));
				 }
			}
	}
	
	/**
	 * 通过条码请求商品
	 */
	public void getSaoData(String editStr){
		 
		
		if(editStr != null && !editStr.equals("")){
			 JSONObject jsonString = GetJsonData.getInstance(this)
				       .getGoodStr("",editStr);
			HttpUtil.getInstance(Write2Nfc.this).requestHttp(HttpUtil.URL_GOODS
					,jsonString,saolistener);
		 }else{
			 ToastUtils.toastMessage(ThisApp.getInstance().getResources()
					 .getString(R.string.no_input));
		 } 
		
	}
	
	
	
	/**
	 * 请求获取标签列表
	 */
	public void getlableData(String id,String str){
		
			 if(str != null && !str.equals("")){
				 JSONObject jsonString = GetJsonData.getInstance(this)
						.getReGoodLableList(id, str, 1);
				HttpUtil.getInstance(Write2Nfc.this)
				.requestHttp(HttpUtil.URL_GOODLABLE,jsonString,Lablelistener);
			 }else{
				 ToastUtils.toastMessage(ThisApp.getInstance().getResources()
						 .getString(R.string.no_input));
			 }        
		
	}
	
	
	/**
	 * 写入标签反馈
	 */
	public void Writelable(String nfcId,String securityCode
			,String lableId,String traceCode,String salt){
		      String location = LocationUtil.getInstance(this).getLocationByNetwork();
			 if(nfcId != null && !nfcId.equals("")){
				 JSONObject jsonString = GetJsonData.getInstance(this)
						.getWriteLabel(nfcId, securityCode,lableId, traceCode,salt, location);
				HttpUtil.getInstance(Write2Nfc.this).requestHttp(HttpUtil.URL_WRITELABEL,jsonString,WriteLablelistener);
			 }else{
				 ToastUtils.toastMessage(ThisApp.getInstance().getResources()
						 .getString(R.string.no_input));
			 }        
	}
	
	/**
	 * 解析获取商品列表实体
	 */
	public ArrayList<GoodsEntity> initGoodListData(String data){
		 ArrayList<GoodsEntity> entity = null;
		 if(data != null && !data.equals("")){
			 entity = ParaseJsonData.getInstance(Write2Nfc.this).parseGoodList(data);
		 }
		return entity;
	}
	
	/**
	 * 解析获取商品实体
	 */
	public GoodsEntity initGoodData(String data){
		GoodsEntity entity = null;
		 if(data != null && !data.equals("")){
			 entity = ParaseJsonData.getInstance(Write2Nfc.this).parseGood(data);
		 }
		return entity;
	}
	
	
	public void getAdress(){
		String adress = LocationUtil.getInstance(this).getLocationByNetwork();
		Log.e("++++++++adress", adress);
	}
	
   
	/**
	 * ActionBar 点击事件
	 */
	@Override
	public void onLeftBtnClick(View view) {
		// TODO Auto-generated method stub
		finish();
	}

	/**
	 * ActionBar 点击事件
	 */
	@Override
	public void onRightBtnClick(View view) {
		// TODO Auto-generated method stub
		Intent bintent = new Intent(Write2Nfc.this, CaptureActivity.class);
		bintent.putExtra(CaptureActivity.intentTypestr, 1);
		startActivityForResult(bintent,0); 
		
	}
    
	/**
	 * ActionBar 点击事件
	 */
	@Override
	public void onRightSecondBtnClick(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Message message = new Message();
		switch (v.getId()) {
		
		case R.id.search_tx://搜索按钮点击事件
			message.what = 1;
			UIhandle.sendMessage(message);
			getSearchData();  
			break;

		default:
			break;
		}
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 0){
			Message message = new Message();
			switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
			 case RESULT_OK:
			 message.what = 1;
			 String str=data.getStringExtra(CaptureActivity.requertdata);//
			 Log.e("+++++++++++++++onActivityResult", ""+str);
			 getSaoData(str);
			 break;
			 default:
			 break;
			 }	
			UIhandle.sendMessage(message);
		}
		 
	}
	
	
	
	/**
	 * 请求商品列表回调接口
	 */
	private OnRequestListener listener = new OnRequestListener() {
		
		@Override
		public void onResult(int statusCode, String str) {
			// TODO Auto-generated method stub
			Message message = new Message();
			if(statusCode == 0){
				message.what = 2;
				message.obj = str;
			}else{
				message.what = 3;
			}
			UIhandle.sendMessage(message);
		}
	};
	
	/**
	 * 请求商品列表回调接口
	 */
	private OnRequestListener saolistener = new OnRequestListener() {
		
		@Override
		public void onResult(int statusCode, String str) {
			// TODO Auto-generated method stub
			Message message = new Message();
			if(statusCode == 0){
				message.what = 11;
				message.obj = str;
			}else{
				message.what = 12;
			}
			UIhandle.sendMessage(message);
		}
	};
	
	/**
	 * 请求商品标签回调接口
	 */
	private OnRequestListener Lablelistener = new OnRequestListener() {
		
		@Override
		public void onResult(int statusCode, String str) {
			// TODO Auto-generated method stub
			Message message = new Message();
			if(statusCode == 0){
			  message.what = 7;
			  message.obj = str;
			  
			}else{
				message.what = 8;
			}
			UIhandle.sendMessage(message);
		}
	};
	 
	/**
	 * 对话框回掉接口
	 */
	private OrequestAlterDialog AlterListener = new OrequestAlterDialog(){

		@Override
		public void onResult(int statcode) {
			// TODO Auto-generated method stub
			Message message = new Message();
			if(statcode == 1){//确认按钮
				message.what = 5;
			}else if(statcode == 2){//取消按钮
				message.what = 6;
			}
			UIhandle.sendMessage(message);
		}
		
	};
	
	/**
	 * 写入标签反馈回掉接口
	 */
	private OnRequestListener WriteLablelistener = new OnRequestListener() {
		
		@Override
		public void onResult(int statusCode, String str) {
			// TODO Auto-generated method stub
			Message message = new Message();
			if(statusCode == 0){
			  message.what = 9;
			  message.obj = str;
			  
			}else{
				message.what = 10;
			}
			UIhandle.sendMessage(message);
		}
	};

}
