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
public class HttpUtil {  

	private final static String Tag = "HttpUtil";
	
	public static final String HTTP_URL_BASE = "http://172.16.53.209:8080/web";
	
	public final static String URL_LOGIN = "http://172.16.53.209:8080/web/mobileLogin";//登录
	public final static String URL_GOODLIST = "http://172.16.53.209:8080/web/goods/list";//商品列表
	public final static String URL_GOODLABLE = "http://172.16.53.209:8080/web/label/get";//商品标签
	public final static String URL_QUERY = "http://172.16.53.209:8080/web/label/validation";//查询防伪结果
	public final static String URL_WRITELABEL = "http://172.16.53.209:8080/web/label/written";//查询防伪结果
	public final static String URL_GOODS = "http://172.16.53.209:8080/web/goods";//查商品详情
	public final static String URL_HTML5URL = "http://172.16.53.209:8080/web/search?";//查商品详情
	
	 
	
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

	private static HttpUtil instance;
	private String urlBase = HTTP_URL_BASE;
	RequestQueue requestQueue = null;
	// 线程池
	private ExecutorService executor;

	private HttpUtil(Context context) {
		executor = Executors.newFixedThreadPool(3);
		requestQueue = Volley.newRequestQueue(ThisApp.getInstance());
	}

	public static HttpUtil getInstance(Context context) {
		if (instance == null){
			instance = new HttpUtil(context);
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
	 * 
	 * @param data
	 * @param listener
	 * @return
	 */
	
	public boolean requestHttp(final String URL,final JSONObject data,final OnRequestListener listener){
		boolean flag= false;
		if (!NetworkUtil.isNetworkAvailable()){
			listener.onResult(RESULT_NETWORD_ERROR, NETWORD_ERROR);
			return false;
		}
		if (listener == null) {  
			return false;
		}
		
		Log.e("++++++++++++++++++++++++requestHttp", "url:"+URL);
		Log.e("++++++++++++++++++++++++requestHttp", "JSONObject:"+data.toString());
		
		 JsonObjectRequest jsonObjectRequest ;
		    //发送post请求
		      try{
		         jsonObjectRequest = new JsonObjectRequest(
		                Request.Method.POST, URL, data,
		                new Response.Listener<JSONObject>() {
		                    @Override
		                    public void onResponse(JSONObject response) {
		                        //打印请求后获取的json数据
		                        Log.e("bbb", response.toString());
		                        
								try {
									JSONTokener jsonParser = new JSONTokener(
			                        		response.toString());
									JSONObject jb = (JSONObject) jsonParser.nextValue();
									int stateCode = jb.getInt(REQUESTCODE);
									String data = "";
									if(stateCode == 0){
										if(response.toString().indexOf(DATA) >0){
										 data = jb.getString(DATA);
										}
										
										Log.e("RESULT_SUCCEED",""+ data);
										
									listener.onResult(RESULT_SUCCEED,data);
									}else{
										listener.onResult(RESULT_ERROR_MESSAGE,"");
									}
									
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									Log.e("onResponse","onResponse");
								}
							}
		                   
		                }, new Response.ErrorListener() {
		                    @Override
		                    public void onErrorResponse(VolleyError arg0) {
		                        // System.out.println("sorry,Error");
		                    	 Log.e("onErrorResponse",arg0.toString());
		                        listener.onResult(RESULT_ERROR_MESSAGE,arg0.toString());
		                    }
		                });
		         
		            requestQueue.add(jsonObjectRequest);
		      } catch (Exception e) {
		          e.printStackTrace();
		          System.out.println(e + "");
		          listener.onResult(RESULT_ERROR_MESSAGE,"");
		      }
		      requestQueue.start();
		return flag;
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
	
	
	private int flag = 2;
	private String turesult = null;
	public int HttpTest(String url, JSONObject jsonObject,String[] result){
		
		  url = urlBase + url;
		  Log.e("++++url", url+"");
          Log.e("post",jsonObject.toString());
          
          JsonObjectRequest jsonObjectRequest ;
    //发送post请求
      try{
         jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //打印请求后获取的json数据
                        Log.e("bbb", response.toString());
                            turesult= response.toString();
                            flag = RESULT_SUCCEED;
            		}
                   
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        // System.out.println("sorry,Error");
                    	 Log.e("onErrorResponse",arg0.toString());
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
      
      result[0] = turesult;
      
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
