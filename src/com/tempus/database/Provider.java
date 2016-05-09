package com.tempus.database;

import android.net.Uri;
import android.provider.BaseColumns;


public class Provider {

	public static final String AUTHORITY = "com.tempus.traceback.database";
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.traceback";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.traceback";

	/**
	 * UserList database table content.
	 *
	 */
	public static final class UserList implements BaseColumns {
		/**
		 * UserList table URI.
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/userlist");
		/**
		 * UserList name
		 */
		public static final String TABLE_NAME = "userlist";
		/**
		 * String parameter key.
		 */
		
		public static final String id = "id";
		public static final String loginName = "loginName";
		public static final String name=  "name";
		public static final String mobileLogin = "mobileLogin";
		public static final String sessionid = "sessionid" ;
		public static final String success = "success";
		public static final String username = "username";
		public static final String rememberMe = "rememberMe";
		public static final String isValidatjeesiteLogin = "isValidatjeesiteLogin";
		public static final String message="message";
		
	}
	
	
	
	

	
	
	
	
	

	
	
}
