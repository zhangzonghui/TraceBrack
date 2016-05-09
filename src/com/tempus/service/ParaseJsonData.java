package com.tempus.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.nutz.json.Json;

import com.tempus.entity.GoodLableList;
import com.tempus.entity.GoodList;
import com.tempus.entity.GoodsEntity;
import com.tempus.entity.LabelRequestEntity;
import com.tempus.entity.LableEntity;
import com.tempus.entity.LoginGet;
import com.tempus.entity.LoginUpload;
import com.tempus.entity.QueryEntity;


import android.content.Context;
import android.util.Log;

public class ParaseJsonData {
	private static ParaseJsonData instance;
	private Context context;
	public ParaseJsonData(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public static ParaseJsonData getInstance(Context context){
		if(instance == null){
			instance = new ParaseJsonData(context);
		}
		
		return instance;
	}
	
	public LoginGet ParaseLoginGetData(String data){
		 LoginGet entity = null;
		  try{
		       entity = Json.fromJson(LoginGet.class, data);
	    }catch(Exception e){
	    	 Log.e("Tag:ParsedJsonData", "ParaseLoginGetData  ERROR"); 
	     }
	  return entity;
	}
	
	/**
	 * 解析商品列表
	 * @param jsonStr
	 * @return
	 */
	public  ArrayList<GoodsEntity> parseGoodList(String jsonStr){
		ArrayList<GoodsEntity> goodList = null;
		try{
			  String data = "{\"goodList\":" + jsonStr + "}";
			  Log.e("++++++++parseGoodList", data);
			  GoodList obj = Json.fromJson(GoodList.class, data);
			  goodList = obj.getGoodList();
			}catch(Exception e){
				Log.e("Tag:ParsedJsonData", "parseGoodList   ERROR");
			}
		return goodList;
	}
	
	/**
	 * 解析商品
	 * @param jsonStr
	 * @return
	 */
	public GoodsEntity parseGood(String jsonStr){
		GoodsEntity goodList = null;
		try{
			  
			GoodsEntity obj = Json.fromJson(GoodsEntity.class, jsonStr);
			  goodList = obj;
			}catch(Exception e){
				Log.e("Tag:ParsedJsonData", "parseGood   ERROR");
		}
		return goodList;
	}
	
	/**
	 * 解析标签列表
	 * @param jsonStr
	 * @return
	 */
	public  ArrayList<LableEntity> parseGoodLableList(String jsonStr){
		ArrayList<LableEntity> lableList = null;
		try{
			  String data = "{\"lableList\":" + jsonStr + "}";
			  GoodLableList obj = Json.fromJson(GoodLableList.class, data);
			  lableList = obj.getGoodList();
			}catch(Exception e){
				Log.e("Tag:ParsedJsonData", "parseGoodLableList   ERROR");
			}
		return lableList;
	}
	
	/**
	 * 解析查询防伪结果
	 * @param data
	 * @return
	 */
	public LabelRequestEntity ParaseLabelRequestData(String data){
		LabelRequestEntity entity = null;
		  //try{
		       entity = Json.fromJson(LabelRequestEntity.class, data);
	   //  }catch(Exception e){
	    //	 Log.e("Tag:ParsedJsonData", "ParaseLabelRequestData  ERROR"); 
	    //  }
	  return entity;
	}
	
	

}
