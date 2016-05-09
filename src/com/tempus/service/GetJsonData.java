package com.tempus.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.nutz.json.Json;

import com.tempus.entity.LoginGet;
import com.tempus.entity.LoginUpload;
import com.tempus.entity.ReGoodLableList;
import com.tempus.entity.ReGoodList;
import com.tempus.entity.ReQueryEntity;

import android.R.integer;
import android.content.Context;
import android.util.Log;

public class GetJsonData {
	private static GetJsonData instance;
	private Context context;
	public GetJsonData(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public static GetJsonData getInstance(Context context){
		if(instance == null){
			instance = new GetJsonData(context);
		}
		
		return instance;
	}
	
	/**
	 * 生成登录时上传的json 字符串
	 * @param username
	 * @param password
	 * @param mobileLogin
	 * @return
	 */
	public JSONObject getLoginUploadData(String username,String password,boolean mobileLogin){
		
		 JSONObject jsonObject=new JSONObject() ;
		 
		
	       
        try {
                jsonObject.put("username", username);
                jsonObject.put("password", password);
                jsonObject.put("mobileLogin", mobileLogin);
                
         } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.e("=========GetJsonData", "getLoginUploadData ERROR");
         }
		
		return jsonObject;  
	}
	
	/**
	 * 获取登录上传字符串
	 * @param context
	 * @param id
	 * @param loginName
	 * @param name
	 * @param mobileLogin
	 * @param sessionid
	 * @param username
	 * @param rememberMe
	 * @param isValidatjeesiteLogin
	 * @param message
	 * @return
	 */
	
	public String getLoginGetData(Context context,String id
			,String loginName,String name,boolean mobileLogin
			,String sessionid,String username,String rememberMe
			,String isValidatjeesiteLogin
			,String message){
		String data = null;
		LoginGet entity = new LoginGet();
		entity.setId(id);
		entity.setLoginName(loginName);
		entity.setId(isValidatjeesiteLogin);
		entity.setMessage(message);
		entity.setMobileLogin(mobileLogin);
		entity.setName(name);
		entity.setRememberMe(rememberMe);
		entity.setSessionid(sessionid);
		entity.setUsername(username);
		
		data = Json.toJson(entity);
		
		return data;
	}
	
	
	/**
	 * 获取商品列表上传字符串
	 * @param name
	 * @param categoryId
	 * @return
	 */
	
	public JSONObject getGoodListStr(String name,String categoryId){
		
		
		JSONObject jsonObject=new JSONObject() ;
		 
		
	       
        try {
                jsonObject.put("name", name);
                jsonObject.put("categoryId", categoryId);
                
         } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.e("=========GetJsonData", "getGoodListStr ERROR");
         }
        return jsonObject;  
        
	}
	
	/**
	 * 获取商品列表上传字符串
	 * @param name
	 * @param categoryId
	 * @return
	 */
	
	public JSONObject getGoodStr(String id,String barCode){
		
		
		JSONObject jsonObject=new JSONObject() ;
		 
		
	       
        try {
                jsonObject.put("id", id);
                jsonObject.put("barCode", barCode);
                
         } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.e("=========GetJsonData", "getGoodStr ERROR");
         }
        return jsonObject;  
        
	}
	
	/**
	 * 获取商品标签上传字符串
	 * @param goodsId
	 * @param title
	 * @param quantity
	 * @return
	 */
	public JSONObject getReGoodLableList(String goodsId,String title,int quantity){
		
		JSONObject jsonObject=new JSONObject() ;
		 
		 try {
                jsonObject.put("goodsId", goodsId);
                jsonObject.put("title", title);
                jsonObject.put("quantity", quantity);
                
         } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                Log.e("=========GetJsonData", "getReGoodLableList ERROR");
         }
        return jsonObject;  
        
        
       
	}
	
	/**
	 * 获取查询防伪上传JSON字符串
	 * @param securityCode
	 * @param traceCode
	 * @param salt
	 * @param location
	 * @return
	 */
	public JSONObject getReQuery(String securityCode,String nfcid,String traceCode
			,String salt,String location){
		
		JSONObject jsonObject=new JSONObject() ;
		 
		 try {
               jsonObject.put("securityCode", securityCode);
               jsonObject.put("traceCode", traceCode);
               jsonObject.put("salt", salt);
               jsonObject.put("location", location);
               jsonObject.put("nfcId", nfcid);
               
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========GetJsonData", "getReGoodLableList ERROR");
        }
       return jsonObject; 
	}
	
	
	/**
	 * 标签写入反馈JSON字符串
	 * @param securityCode
	 * @param traceCode
	 * @param salt
	 * @param location
	 * @return
	 */
	public JSONObject getWriteLabel(String nfcid,String securityCode
			,String lableId,String traceCode,String salt,String location){
		
		JSONObject jsonObject=new JSONObject() ;
		 
		 try {
               jsonObject.put("id", lableId);
               jsonObject.put("securityCode", securityCode);
               jsonObject.put("traceCode", traceCode);
               jsonObject.put("salt", salt);
               jsonObject.put("location", location);
               jsonObject.put("nfcId", nfcid);
             
               
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========GetJsonData", "getWriteLabel ERROR");
        }
       return jsonObject; 
	}
	
	
	
	/**
	 * 标签写入反馈JSON字符串
	 * @param securityCode
	 * @param traceCode
	 * @param salt
	 * @param location
	 * @return
	 */
	public JSONObject getHtml5Url(String securityCode,String traceCode
			,String salt,String location){
		
		JSONObject jsonObject=new JSONObject() ;
		 
		 try {
               jsonObject.put("securityCode", securityCode);
               jsonObject.put("traceCode", traceCode);
               jsonObject.put("salt", salt);
               jsonObject.put("location", location);
        } catch (JSONException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
               Log.e("=========GetJsonData", "getWriteLabel ERROR");
        }
       return jsonObject; 
	}
	
	
	
   
	
	
	
	
	

}
