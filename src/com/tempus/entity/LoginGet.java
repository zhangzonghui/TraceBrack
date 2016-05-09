package com.tempus.entity;

public class LoginGet {
    
	//请求成功时
	private String id;//（ID生成策略为UUID，字符串格式，系统自带用户为数值序列）
    private String loginName;//登录名
	private String name;//用户姓名
	private boolean mobileLogin;//是否是手机登录
	private String sessionid;//当前用户SESSIONID
	private boolean success;
	
	//请求失败时
	private String username;//登录用户名
	private String rememberMe;//是否选择了记住我
	private String isValidatjeesiteLogin;//是否是手机登录
	private String message;//登录失败信息
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getMobileLogin() {
		return mobileLogin;
	}
	public void setMobileLogin(boolean mobileLogin) {
		this.mobileLogin = mobileLogin;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRememberMe() {
		return rememberMe;
	}
	public void setRememberMe(String rememberMe) {
		this.rememberMe = rememberMe;
	}
	public String getIsValidatjeesiteLogin() {
		return isValidatjeesiteLogin;
	}
	public void setIsValidatjeesiteLogin(String isValidatjeesiteLogin) {
		this.isValidatjeesiteLogin = isValidatjeesiteLogin;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean getSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	
	
	
	
	
	
	

}
