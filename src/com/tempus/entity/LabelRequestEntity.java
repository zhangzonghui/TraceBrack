package com.tempus.entity;

import java.util.ArrayList;

import android.R.integer;

public class LabelRequestEntity {

	  private boolean enable;
      private String securityCode;
      private String id;
      private String goodsId;
      private ArrayList<LablePointsEntity> labelPoints;  
      private String message;
      private int status;
      private String title;
      private String nfcId;
      
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public ArrayList<LablePointsEntity> getLablePoints() {
		return labelPoints;
	}
	public void setLablePoints(ArrayList<LablePointsEntity> lablePoints) {
		this.labelPoints = lablePoints;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNfcId() {
		return nfcId;
	}
	public void setNfcId(String nfcId) {
		this.nfcId = nfcId;
	}
      
      
      


}
