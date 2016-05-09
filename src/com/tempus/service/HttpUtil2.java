package com.tempus.service;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.nutz.json.Json;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tempus.traceback.ThisApp;
import com.tempus.utils.NetworkUtil;


import android.R.integer;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;



/**
 * HTTP连接器。用于以HTTP方式与服务器连接。 启动三个线程，并用线程池管理。
 * 
 * @author Hong
 * 
 */
public class HttpUtil2 {

	private final static String Tag = "HttpUtil";
	
	  public static final String HTTP_URL_BASE = "http://172.16.53.71:8080/web";
	
	//我的
	private final static String URL_LOGIN = "/mobileLogin";//登录
	private final static String URL_GOODLIST = "/goods/list";//商品列表
	private final static String URL_GOODLABLE = "/label/get";//商品标签
	private final static String URL_QUERY = "/label/validation";//查询防伪结果
	
	 
	
	private final static int TIME_OUT = 1000 * 3;
	private final static String STATUSCODE = "StatusCode";
	private final static String REQUESTCODE = "status";
	private final static String DATA = "data";
	
	private final static String SUCCESS = "success";
	private final static String RESULT_ERROR = "请求异常";
	private final static String NETWORD_ERROR = "网络异常";
	private final static String MESSAGE = "msg";
	public final static int RESULT_ERROR_MESSAGE = 100;// 服务器的错误信息
	public final static int RESULT_SERVER_ERROR = -1;// 服务器异常
	public final static int RESULT_SUCCEED = 0;     // ok
	public final static int RESULT_NETWORD_ERROR = 1;// 网络异常
	public final static int RESULT_PARSER_ERROR = 2;// 解析异常

	private static HttpUtil2 instance;
	private String urlBase = HTTP_URL_BASE;
	RequestQueue requestQueue = null;
	// 线程池
	private ExecutorService executor;

	private HttpUtil2(Context context) {
		executor = Executors.newFixedThreadPool(3);
		requestQueue = Volley.newRequestQueue(ThisApp.getInstance());
	}

	public static HttpUtil2 getInstance(Context context) {
		if (instance == null){
			instance = new HttpUtil2(context);
		}
			
	   return instance;
	}

	public void setUrlBase(String urlBase) {
		if (urlBase == null || urlBase.length() == 0)
			this.urlBase = HTTP_URL_BASE;
		else
			this.urlBase = urlBase;

		if (!this.urlBase.startsWith("http://"))
			this.urlBase = "http://" + this.urlBase;

	}

	/**
	 * 请求登录
	 * 
	 * @param account
	 *            ：账号
	 * @param password
	 *            ：密码
	 * @return
	 */
	public boolean requestLogin(final String data,final OnRequestListener listener) {
		if (!NetworkUtil.isNetworkAvailable()){
			listener.onResult(RESULT_NETWORD_ERROR, NETWORD_ERROR);
			return false;
		}
			

		if (listener == null) {  
			return false;
		}
		
		
		Runnable ra = new Runnable() {
			@Override
			public void run() {
				String[] result = new String[1];
				int resultCode = httpPost(URL_LOGIN, data, result);
				Log.e("++++++result[1]", result[0].toString());
				if (resultCode == RESULT_SUCCEED) {
					try {
						JSONTokener jsonParser = new JSONTokener(
								result[0].toString());
						String resultStr = result[0].toString();
						Log.e("++++++result[0]", result[0].toString());
						JSONObject jb = (JSONObject) jsonParser.nextValue();
						int stateCode = jb.getInt(REQUESTCODE);
						String data = jb.getString(DATA);
						Log.e("++++++result[0]", ""+data);
						if(stateCode == 0){
						    listener.onResult(RESULT_SUCCEED,data);
						}else{
							
							listener.onResult(RESULT_ERROR_MESSAGE,resultStr);
						}


					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						listener.onResult(RESULT_SERVER_ERROR,RESULT_ERROR);
					}
				}else{
					listener.onResult(RESULT_SERVER_ERROR,RESULT_ERROR);
				}
			}
		};
		executor.execute(ra);
		return true;
	}
	
	/**
	 * 请求商品列表
	 * @param data
	 * @param listener
	 * @return
	 */
	
	public boolean requestGoodList(final String data,final OnRequestListener listener){
		boolean flag= false;
		if (!NetworkUtil.isNetworkAvailable()){
			listener.onResult(RESULT_NETWORD_ERROR, NETWORD_ERROR);
			return false;
		}
		if (listener == null) {  
			return false;
		}
		Runnable ra = new Runnable() {
			@Override
			public void run() {
				String[] result = new String[1];
				int resultCode = httpPost(URL_GOODLIST, data, result);
				Log.e("++++++result[1]", result[0].toString());
				if (resultCode == RESULT_SUCCEED) {
					try {
						JSONTokener jsonParser = new JSONTokener(
								result[0].toString());
						String resultStr = result[0].toString();
						
						JSONObject jb = (JSONObject) jsonParser.nextValue();
						int stateCode = jb.getInt(REQUESTCODE);
						String data = jb.getJSONArray(DATA).toString();
						Log.e("++++++result[0]", ""+data);
						if(stateCode == 0){
						    listener.onResult(RESULT_SUCCEED,data);
						}else{
							
							listener.onResult(RESULT_ERROR_MESSAGE,resultStr);
						}
                      } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						listener.onResult(RESULT_SERVER_ERROR,RESULT_ERROR);
					}
				}else{
					listener.onResult(RESULT_SERVER_ERROR,RESULT_ERROR);
				}
			}
		};
		executor.execute(ra);
		
		return flag;
	}
	
	
	/**
	 * 请求商品标签
	 * @param data
	 * @param listener
	 * @return
	 */
	public boolean requestGoodLabel(final String data,final OnRequestListener listener){
		boolean flag= false;    
		if (!NetworkUtil.isNetworkAvailable()){
			listener.onResult(RESULT_NETWORD_ERROR, NETWORD_ERROR);
			return false;
		}
		if (listener == null) {  
			return false;
		}
		Runnable ra = new Runnable() {
			@Override
			public void run() {
				String[] result = new String[1];
				int resultCode = httpPost(URL_GOODLABLE, data, result);
				Log.e("++++++result[1]", result[0].toString());
				if (resultCode == RESULT_SUCCEED) {
					try {
						JSONTokener jsonParser = new JSONTokener(
								result[0].toString());
						String resultStr = result[0].toString();
						JSONObject jb = (JSONObject) jsonParser.nextValue();
						int stateCode = jb.getInt(REQUESTCODE);
						String data = jb.getJSONArray(DATA).toString();
						Log.e("++++++result[0]", ""+data);
						if(stateCode == 0){
						    listener.onResult(RESULT_SUCCEED,data);
						}else{
							
							listener.onResult(RESULT_ERROR_MESSAGE,resultStr);
						}
                      } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						listener.onResult(RESULT_SERVER_ERROR,RESULT_ERROR);
					}
				}else{
					listener.onResult(RESULT_SERVER_ERROR,RESULT_ERROR);
				}
			}
		};
		executor.execute(ra);
		
		return flag;
	}
	
	/**
	 * 请求商品标签
	 * @param data
	 * @param listener
	 * @return
	 */
	public boolean requestQuery(final String data,final OnRequestListener listener){
		boolean flag= false;    
		if (!NetworkUtil.isNetworkAvailable()){
			listener.onResult(RESULT_NETWORD_ERROR, NETWORD_ERROR);
			return false;
		}
		if (listener == null) {  
			return false;
		}
		Runnable ra = new Runnable() {
			@Override
			public void run() {
				String[] result = new String[1];
				int resultCode = httpPost(URL_QUERY, data, result);
				Log.e("++++++result[1]", result[0].toString());
				if (resultCode == RESULT_SUCCEED) {
					try {
						JSONTokener jsonParser = new JSONTokener(
								result[0].toString());
						String resultStr = result[0].toString();
						JSONObject jb = (JSONObject) jsonParser.nextValue();
						int stateCode = jb.getInt(REQUESTCODE);
						String data = jb.getJSONArray(DATA).toString();
						Log.e("++++++result[0]", ""+data);
						if(stateCode == 0){
						    listener.onResult(RESULT_SUCCEED,data);
						}else{
							
							listener.onResult(RESULT_ERROR_MESSAGE,resultStr);
						}
                      } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						listener.onResult(RESULT_SERVER_ERROR,RESULT_ERROR);
					}
				}else{
					listener.onResult(RESULT_SERVER_ERROR,RESULT_ERROR);
				}
			}
		};
		executor.execute(ra);
		
		return flag;
	}

	

	
	
	/**
	 * 网络共用请求勿动
	 * 
	 * @param url：请求后加URL
	 * @param data：封装好的json数据
	 * @param result：网络返回数据
	 * @return int：返回状态码： -1：服务器异常 0成功 1：网络异常 2：解析异常
	 */
	private int httpPost(String url, String data, String[] result) {
		url = urlBase + url;
        Log.e("++++url", url+"");
        Log.e("++++data", data+"");
		try {
			HttpPost httpRequest = new HttpPost(url);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			/*params.add(new BasicNameValuePair("", URLEncoder.encode(data,
					HTTP.UTF_8)));*/
			params.add(new BasicNameValuePair("JSON",data));
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			//httpRequest.addHeader("Content-Type", "application/json");
			//httpRequest.setEntity(new StringEntity(data, "UTF_8"));
			Log.e("+++++statusCode", httpRequest+"");    
			HttpClient client = new DefaultHttpClient();  
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, TIME_OUT);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					TIME_OUT);
			HttpResponse httpResponse = client.execute(httpRequest);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.e("+++++statusCode", statusCode+"");
			String ret = null;
			if (statusCode == HttpStatus.SC_OK) {
				ret = EntityUtils.toString(httpResponse.getEntity());
			} else {
				String error = EntityUtils.toString(httpResponse.getEntity());
			}  

			if (result != null) {
				result[0] = ret;
				return RESULT_SUCCEED;
			} else {
				return RESULT_NETWORD_ERROR;
			}

		} catch (ClientProtocolException e) {
			return RESULT_NETWORD_ERROR;
		} catch (UnsupportedEncodingException e) {
			return RESULT_NETWORD_ERROR;
		} catch (IOException e) {
			return RESULT_NETWORD_ERROR;
		} catch (Exception e) {
			// TODO: handle exception
			return RESULT_NETWORD_ERROR;
		}
	}
	
	/**
	 * 请求回调
	 * @author lenovo
	 *
	 */
	public interface OnRequestListener {
		/**
		 * 
		 * @param statusCode:0为成功直接解析就可以，其他直接将错误信息taost给用户
		 * @param str：服务器返回的数据或者msg错误信息
		 */
		void onResult(int statusCode,String str);
	}
	
	
	private int flag = 0;
	public int HttpTest(String url, JSONObject jsonObject, final String[] result){
		

          JsonObjectRequest jsonObjectRequest ;
          /*JSONObject jsonObject=new JSONObject() ;
       
          try {
                  jsonObject.put("username", "admin");
                  jsonObject.put("password", "admin");
           } catch (JSONException e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
           }//打印前台向后台要提交的post数据
*/        Log.e("post",jsonObject.toString());
    //发送post请求
      try{
         jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, HTTP_URL_BASE+URL_LOGIN, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //打印请求后获取的json数据
                        Log.e("bbb", response.toString());
                            if(result != null){
                            	result[0] = response.toString();
                            }
            				
                            flag = RESULT_SUCCEED;
            			
                     }
                   
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        // System.out.println("sorry,Error");
                        Log.e("aaa", arg0.toString());
                        flag = RESULT_NETWORD_ERROR;
                    }
                });
         
            requestQueue.add(jsonObjectRequest);
      } catch (Exception e) {
          e.printStackTrace();
          System.out.println(e + "");
          flag = RESULT_NETWORD_ERROR;
      }
      requestQueue.start();
      
	   return flag;
	}

	
	
	


	
	/**
	 * 去除转义字符
	 * 
	 * @param s
	 * @return
	 */
	private String clearMystring(String s) {
		s = s.replace("\"{", "{");
		s = s.replace("}\"", "}");
//		s = s.replace("\\", "");
		return s;
	}

	
	/**
	 *   
	 * @author lenovo
	 *
	 */
	public interface ImageOnRequestListener {
		/**
		 * 
		 * @param statusCode:0为成功直接解析就可以，其他直接将错误信息taost给用户
		 * @param TalkId:说说id
		 * @param page：页数
		 */
		void onImageResult(int statusCode,String TalkId,int page,List<byte[]> imagesArray);
	}

	
	
	
	
		
		
		
		
		
		
		
		
		
		
		
}
