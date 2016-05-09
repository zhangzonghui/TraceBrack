package com.tempus.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;

public class LocationUtil {
    /** Called when the activity is first created. */
   
	public final static int REFRESH_LOCATION = 0x0100;
	
	private Handler mHandler;
	private MyLocationManager myLocationManager;
	
	private long curTimeGPS;
	private long curTimeNetwork;
	private Context context;
	private static LocationUtil instance;
	
    private LocationUtil(Context context) {
    	this.context = context;
    	initLogic();
	}

	public static LocationUtil getInstance(Context context) {
		if (instance == null){
			instance = new LocationUtil(context);
		}
		
		return instance;
	}
    
  





    public void initLogic()
    {
    	mHandler = new Handler()
    	{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what)
				{
				case REFRESH_LOCATION:
					Location location = (Location) msg.obj;
					if (location.getProvider().equals(LocationManager.GPS_PROVIDER))
					{
						long inteval = System.currentTimeMillis() - curTimeGPS;
						curTimeGPS = System.currentTimeMillis(); 
						
						String address = null;
						if (location != null)
						{
							address = myLocationManager.queryAddressByGoogle(location.getLatitude(), location.getLongitude());
						}		
						//showChangeGPSLocation(location, address, inteval);
					}else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER))
					{
						long inteval = System.currentTimeMillis() - curTimeNetwork;
						curTimeNetwork = System.currentTimeMillis(); 
						
						String address = null;
						if (location != null)
						{
							address = myLocationManager.queryAddressByGoogle(location.getLatitude(), location.getLongitude());
						}		
						//showChangeNetworkLocation(location, address, inteval);
					}
					
					break;
					default:
						break;
				}
			}
    		
    	};
    	
    	myLocationManager = new MyLocationManager( context, mHandler);
    }

	
	
	
	

	public String getLocationByGPS()
	{
		
		String Longitude = null;
		String Latitude =null;
		Location location = myLocationManager.getLocationByGps();
		String address = null;
		if (location != null)
		{
			address = myLocationManager.queryAddressByGoogle(location.getLatitude(), location.getLongitude());
		}
		if(location != null){
			 Longitude = location.getLongitude() +"";
			 Latitude = location.getLatitude() +"";
		}
		
		if(Longitude == null || Longitude.trim().length() == 0 
				|| Latitude == null || Latitude.trim().length() == 0){
			return null;
		}
		
		return Longitude + "," + Latitude;
	}
	

	public String getLocationByNetwork()
	{
		String Longitude = null;
		String Latitude =null;
		Location location = myLocationManager.getLocationByNetwork();
		String address = null;
		if (location != null)
		{
			address = myLocationManager.queryAddressByGoogle(location.getLatitude(), location.getLongitude());
		}  
		
		if(location != null){
			 Longitude = location.getLongitude() +"";
			 Latitude = location.getLatitude() +"";
		}
		
		if(Longitude == null || Longitude.trim().length() == 0 
				|| Latitude == null || Latitude.trim().length() == 0){
			return null;
		}
		
		return Longitude + "," + Latitude;  
		
	}
	
	
	/**
	 * ��ȡ������γ��֮��ľ���
	 */
	
	public int getDistance(String Longitude1,String Latitude1,String Longitude2,String Latitude2){
		int distance = 0;
		
		
		return distance;
	}
	
	
	
	
	

	
	
	
	
}