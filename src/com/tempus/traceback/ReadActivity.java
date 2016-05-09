package com.tempus.traceback;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONObject;

import com.tempus.entity.LabelRequestEntity;
import com.tempus.entity.LablePointsEntity;
import com.tempus.entity.ReQueryEntity;
import com.tempus.location.LocationUtil;
import com.tempus.service.GetJsonData;
import com.tempus.service.HttpUtil;
import com.tempus.service.HttpUtil.OnRequestListener;
import com.tempus.service.ParaseJsonData;
import com.tempus.traceback.ActionbarView.OnActionBtnClickListener;
import com.tempus.utils.ByteUtil;
import com.tempus.utils.DrawView;
import com.tempus.utils.ToastUtils;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.R.integer;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ReadActivity extends Activity implements OnActionBtnClickListener {
	private TextView ifo_NFC;
	private TextView goods_result;
	private NfcAdapter nfcAdapter;
	private String readResult = "";
	private String nfcId = "";
	private PendingIntent pendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	private boolean isFirst = true;
	private TextView toWBtn;
	private IntentFilter ndef;
	private ActionbarView readActionbarView;
	private TextView goodsName;
	private ImageView goodsTitle;
	private WebView line_nfc;
	private RelativeLayout position_line;
	//private TextView position_title;
	private LinearLayout luxianView;
	private String currentUrl = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read);
		initActionBarView();
		initView();
		//该方法完成接收到Intent时的初始化工作
		init();	
	}
	
	
	/**@author==ZZH
	 * ����ActionBar
	 */
	public void initActionBarView(){
		readActionbarView = (ActionbarView) findViewById(R.id.read_actionbar);
	    readActionbarView.setLeftbunttonImage(R.drawable.title_left_button_bg);
	    readActionbarView.setRightbunttonImage(R.drawable.add2);
	    readActionbarView.setTitle(R.string.nfc_title);
	    readActionbarView.setTitleColor(getResources().getColor(R.color.white));
	    readActionbarView.setOnActionBtnClickListener(this);
	}
	
	public void initView(){
		ifo_NFC = (TextView) findViewById(R.id.ifo_NFC);
		//goodsName = (TextView) findViewById(R.id.goods_name);
		line_nfc = (WebView) findViewById(R.id.line_nfc);
		//line_nfc.setVisibility(View.INVISIBLE);
		//position_line = (RelativeLayout) findViewById(R.id.goods_positions);
		//position_title = (TextView) findViewById(R.id.positions_title);
		//luxianView = (LinearLayout) findViewById(R.id.luxian_line);
		isShowResult(false);
	}
	

	
	/**
	 * 检测工作,判断设备的NFC支持情况
	 * @return
	 */
	private Boolean ifNFCUse() {
		// TODO Auto-generated method stub
		if (nfcAdapter == null) {
			ToastUtils.toastMessage(ThisApp.getInstance().getResources()
					.getString(R.string.readActivity_nonfc));
			finish();
			return false;
		}
		if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
			ToastUtils.toastMessage(ThisApp.getInstance().getResources()
					.getString(R.string.readctivity_opennfc));
			finish();
			return false;
		}
		return true;
	}

	/**
	 * 初始NFC化过程
	 */
	private void init() {
		// TODO Auto-generated method stub
		/*toWBtn=(TextView)findViewById(R.id.new_nfc);
		toWBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});*/
		
		//NFC适配器，所有的关于NFC的操作从该适配器进行
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if(!ifNFCUse()){
			return;
		}
		//将被调用的Intent，用于重复被Intent触发后将要执行的跳转
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		//设定要过滤的标签动作，这里只接收ACTION_NDEF_DISCOVERED类型
		ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		ndef.addCategory("*/*");
		mFilters = new IntentFilter[] { ndef };// 过滤器
		mTechLists = new String[][] { new String[] { NfcA.class.getName() },
				new String[] { NfcF.class.getName() },
				new String[] { NfcB.class.getName() },
				new String[] { NfcV.class.getName() },
				new String[] {Ndef.class.getName() }};// 允许扫描的标签类型
		
		if (isFirst) {
			if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent()
					.getAction())) {
				System.out.println(getIntent().getAction());
				if (readFromTag(getIntent())) {
					//ifo_NFC.setText(readResult);
					System.out.println("1.5...");
				} else {
					//ifo_NFC.setText("标签数据为空");
				}
			}
			isFirst = false;
		}
		System.out.println("onCreate...");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		nfcAdapter.disableForegroundDispatch(this);
		System.out.println("onPause...");
		Log.e("==========onPause", "onPause");
	}

	/* 
	 * 重写onResume回调函数的意义在于处理多次读取NFC标签时的情况
	 * (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 前台分发系统,这里的作用在于第二次检测NFC标签时该应用有最高的捕获优先权.==zzh
		// 必须配置否则第二次不能检测到Intent  
		
		nfcAdapter.enableForegroundDispatch(this, pendingIntent, mFilters,
				mTechLists);
		Log.e("==========onResume", "onResume");
		
	}
	
	

	


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		isShowResult(false);
	}
	
	


	/*
	 * 
	 *  (non-Javadoc)
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		
		System.out.println(intent.getAction().toString());
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())||
				NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			System.out.println("onNewIntent2...");
			
			
			if (readFromTag(intent)) {
				//ifo_NFC.setText(readResult);
				isShowResult(true);
				initHtml5Web();
				//HttpQuery(readResult,nfcId,"","");
				System.out.println("onNewIntent3...");
				
			} else {
				ifo_NFC.setText(ThisApp.getInstance().getResources()
						.getString(R.string.error_nfcmessage));
			}    
		}

	}

	/**
	 * 读取NFC标签数据的操作
	 * @param intent
	 * @return
	 */
	private boolean readFromTag(Intent intent) {  
		Parcelable[] rawArray = null;   
		if(rawArray == null){
			rawArray =intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		}
		Tag tag =intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		if (rawArray != null) {
			NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
			NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
			try {
				if (mNdefRecord != null) {
					readResult = new String(mNdefRecord.getPayload(), "UTF-8");
				    nfcId = ByteUtil.ByteArrayToHexString(tag.getId());
				    currentUrl = "securityCode="+readResult+"&"+"nfcId="+nfcId;
					return true;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return false;
		}
		return false;
	}
	
	public void isShowResult(boolean flag){
		if(flag){
			line_nfc.setVisibility(View.VISIBLE);
			ifo_NFC.setVisibility(View.INVISIBLE);
		}else{
			line_nfc.setVisibility(View.INVISIBLE);
			ifo_NFC.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 解析查詢結果
	 */
	public void ParseResultData(String jsondata){
		ArrayList<LablePointsEntity> PointsEntity = null;
		LabelRequestEntity entity = ParaseJsonData.getInstance(this)
				.ParaseLabelRequestData(jsondata);
		if(entity != null){
			goodsName.setText(entity.getTitle());
			PointsEntity = entity.getLablePoints();
			//initdrawPosition(PointsEntity);
			initHtml5Web();  
		}
		
		
	}
	
	
	/**
	 * 画商品路线图
	 */
	public void initdrawPosition(ArrayList<LablePointsEntity> adressData){
	 int layoutHeight =luxianView.getHeight();
	 int layoutWidth = luxianView.getWidth();
	 DrawView view = new DrawView(ReadActivity.this,layoutHeight,layoutWidth,1,adressData.size(),adressData);
	 view.invalidate();
	 ((ViewGroup) luxianView).removeAllViews();
	 luxianView.addView(view); 
	}
	
	/**
	 * 加载Html5网页
	 */
	public void initHtml5Web() {
		WebSettings webSettings = line_nfc.getSettings();
		// 设置WebView属性，能够执行Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		// 加载需要显示的网页
		line_nfc.loadUrl(HttpUtil.URL_HTML5URL+currentUrl);
		Log.e("+++++++++++++++++++++loadUrl"
				, "loadUrl:"+HttpUtil.URL_HTML5URL+currentUrl);
		// 设置Web视图
		line_nfc.setWebViewClient(new webViewClient());

	}
	
	
	//Web视图    
	private class webViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
	    }   
	}

	
	/**
	 * UI handle 跟新
	 */
	private Handler UIhandle = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
			case 1:
				
				break;
				
			case 2:
				isShowResult(true);
				String data =msg.obj.toString();
				ParseResultData(data);
			   break;
			
			case 3:
				ifo_NFC.setText(ThisApp.getInstance()
						.getResources().getString(R.string.fals_message));
				ToastUtils.toastMessage(ThisApp.getInstance()
						.getResources().getString(R.string.no_goods_message));
			   break;

			default:
				break;
			}
		}
		
	};

    /**
     * Actionbar 左边按钮点击事件
     */
	@Override
	public void onLeftBtnClick(View view) {
		// TODO Auto-generated method stub
		if(line_nfc.isShown()){
			if(line_nfc.canGoBack()){
				line_nfc.goBack();
			}else{
				line_nfc.removeAllViews();
	        	isShowResult(false);
			}
        	
        }else{
        	finish();
        }
		
	}

    /**
     * Actionbar右边按钮点击事件
     */
	@Override
	public void onRightBtnClick(View view) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(ReadActivity.this,Write2Nfc.class);
		startActivity(intent);
	}


	@Override
	public void onRightSecondBtnClick(View view) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 监听系统回调事件
	 */
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {    
	        //重写onKeyDown，当浏览网页，WebView可以后退时执行后退操作。
	        if(keyCode == KeyEvent.KEYCODE_BACK && line_nfc.isShown()){
	        	if(line_nfc.canGoBack()){
					line_nfc.goBack();
				}else{
					line_nfc.removeAllViews();
		        	isShowResult(false);
				}
	            return true;
	        } 
	        return super.onKeyDown(keyCode, event);
	    }
	
	
	/**
	 * http请求Html5Url
	 * @param securityCode
	 * @param traceCode
	 * @param salt
	 * @param location
	 */
	public void Html5Url(String securityCode,String nfcid,String traceCode,String salt
			){
		String location = LocationUtil.getInstance(this).getLocationByNetwork();
		JSONObject jsonData = GetJsonData.getInstance(ReadActivity.this)
				.getHtml5Url(securityCode,traceCode
						,salt,location);
		HttpUtil.getInstance(ReadActivity.this)
		.requestHttp(HttpUtil.URL_HTML5URL,jsonData,html5listener);
	}
	
	/**
	 * http请求查询防伪
	 * @param securityCode
	 * @param traceCode
	 * @param salt
	 * @param location
	 */
	public void HttpQuery(String securityCode,String nfcid,String traceCode,String salt
			){
		String location = LocationUtil.getInstance(this).getLocationByNetwork();
		JSONObject jsonData = GetJsonData.getInstance(ReadActivity.this)
				.getReQuery(securityCode,nfcid, traceCode, salt, location);
		HttpUtil.getInstance(ReadActivity.this)
		.requestHttp(HttpUtil.URL_QUERY,jsonData,qureylistener);
	}
	
	/**
	 * 请求查询防伪数据
	 */
	private OnRequestListener qureylistener = new OnRequestListener() {
		
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
	 * 请求查询防伪数据
	 */
	private OnRequestListener html5listener = new OnRequestListener() {
		
		@Override
		public void onResult(int statusCode, String str) {
			// TODO Auto-generated method stub
			Message message = new Message();
			if(statusCode == 0){
				message.what = 4;
				message.obj = str;
			}else{
				message.what = 5;
			}
			UIhandle.sendMessage(message);
		}
	};

	

}
