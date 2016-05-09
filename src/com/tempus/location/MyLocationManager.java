package com.tempus.location;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONObject;


import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

public class MyLocationManager {

	private final static int CHECK_POSITION_INTERVAL = 60 * 1000;
	
	private LocationManager locationManager;
	
	private Context mContext;
	
	private Handler mHandler;
	
	private myListen myListenGPS;
	
	private myListen myListenNetwork;
	
	
	
	public MyLocationManager(Context context, Handler handler)//
	{
		mContext = context;
		
		mHandler = handler;
		
		locationManager=(LocationManager)context.getSystemService(context.LOCATION_SERVICE);
		
		mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	/*
	 * 获取GPS�?关状态״�?
	 */
	 public boolean getGPSState()
	 {
    	ContentResolver resolver = mContext.getContentResolver();
        boolean open = Settings.Secure.isLocationProviderEnabled(resolver, LocationManager.GPS_PROVIDER);
        
        return open;
	 }
	   
	 /*
	  * 切换GPS�?关状态�?
	  */
	 public void  toggleGPS() {

		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		
		try {
			PendingIntent.getBroadcast(mContext, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}
	
	
	 
	public Location getLocationByGps()
	{
		Location  location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		return location;
	}
	
	public Location getLocationByNetwork()
	{
		Location  location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		return location;
	}
	 
	
	public void registerListen()
	{
		if (myListenGPS == null)
		{
			myListenGPS = new myListen();
			
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					CHECK_POSITION_INTERVAL, 0, myListenGPS);
			
			
		}
		
		if (myListenNetwork == null)
		{
			myListenNetwork = new myListen();
			
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					CHECK_POSITION_INTERVAL, 0, myListenNetwork);
		}
	}
	
	public void unRegisterListen()
	{
		if(myListenGPS != null)
		{
			locationManager.removeUpdates(myListenGPS);
			
			myListenGPS = null;
			
		}
		
		if(myListenNetwork != null)
		{
			locationManager.removeUpdates(myListenNetwork);
			
			myListenNetwork = null;
			
		}
	}
	
	
	class myListen implements LocationListener
	{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
			//MyUtil.display("onLocationChanged.......");

			
			if (mHandler != null)
			{
				Message msg = mHandler.obtainMessage(LocationUtil.REFRESH_LOCATION, location);
				msg.sendToTarget();
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	/** 谷歌查询地址URL*/									// %s: latitude, %s: longitude
	private static final String GOOGLE_QUERY_ADDRESS_URL = "http://maps.google.cn/maps/geo?key=abcdefg&q=%s,%s";
	
	private static final int TIME_OUT_INTERVAL = 3 * 1000;
	
	private TelephonyManager mTelephonyManager;

	private GsmCellLocation mGsmCellLocation;

	private int cid;								
		
	private int lac;								

	private String mcc = "";						
	
	private String mnc = "";						
	
	
	/*
	 * 获取基站信息
	 */
	public CellIDInfo getCellInfo()
	{
		mGsmCellLocation = ((GsmCellLocation) mTelephonyManager.getCellLocation());

		if (mGsmCellLocation == null)
			return null;
		
		cid = mGsmCellLocation.getCid();
		lac = mGsmCellLocation.getLac();
	
		
		String netWorkOperator = mTelephonyManager.getNetworkOperator();
		
		mcc = netWorkOperator.substring(0, 3);	
		mnc = netWorkOperator.substring(3, 5);
		
		CellIDInfo cellIDInfo = new CellIDInfo();
		cellIDInfo.cellId = cid;
		cellIDInfo.mobileCountryCode = mcc;
		cellIDInfo.mobileNetworkCode = mnc;
		cellIDInfo.locationAreaCode = lac;
		
		return cellIDInfo;
	}
	
	
	
	/**
	 * 通过谷歌获取地理位置
	 * 
	 * @throws Exception
	 */
	public String queryAddressByGoogle(Double lat, Double lon){
		String resultString = "";

		/** 这里采用get方法，直接将参数加到URL�? */
		String urlString = String.format(GOOGLE_QUERY_ADDRESS_URL, lat,lon);

		/** 新建HttpClient */
		HttpClient client = new DefaultHttpClient();
		
		/** 设置超时 */
	    client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT_INTERVAL);//请求超时
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIME_OUT_INTERVAL);//读取超时
		
		
		/** 采用GET方法 */
		HttpGet get = new HttpGet(urlString);
		try {
			/** 发起GET请求并获得返回数�? */
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(entity.getContent()));
			StringBuffer strBuff = new StringBuffer();
			String result = null;
			while ((result = buffReader.readLine()) != null) {
				strBuff.append(result);
			}
			resultString = strBuff.toString();

			/** 解析JSON数据，获得物理地�? */
			if (resultString != null && resultString.length() > 0) {
				JSONObject jsonobject = new JSONObject(resultString);
				JSONArray jsonArray = new JSONArray(jsonobject.get("Placemark").toString());
				resultString = "";
				for (int i = 0; i < jsonArray.length(); i++) {
					resultString = jsonArray.getJSONObject(i).getString("address");
				}
			}
		} catch (Exception e) {
			return null;
		} finally {
			get.abort();
			client = null;
		}

		return resultString;
	}
	
}
