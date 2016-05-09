package com.tempus.database;




import android.R.integer;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Create database.
 * 
 * @author zxq
 * 
 */
class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "database_traceback.db";
	
	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override      
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Provider.UserList.TABLE_NAME + " ("
				+ Provider.UserList._ID
				+ " INTEGER PRIMARY KEY autoincrement,"
				+ Provider.UserList.id + " TEXT DEFAULT \"\","
				+ Provider.UserList.isValidatjeesiteLogin + " TEXT DEFAULT \"\","
				+ Provider.UserList.loginName + " TEXT DEFAULT \"\","
				+ Provider.UserList.message + " TEXT DEFAULT \"\","
				+ Provider.UserList.mobileLogin + " TEXT DEFAULT \"\","
				+ Provider.UserList.name + " TEXT DEFAULT \"\","
				+ Provider.UserList.rememberMe + " TEXT DEFAULT \"\","
				+ Provider.UserList.sessionid + " TEXT DEFAULT \"\","
				+ Provider.UserList.success + " TEXT DEFAULT false,"
				+ Provider.UserList.username + " TEXT DEFAULT \"\""
				+ ");"); 
		
		
		
		
		
		}   

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		  
		
		
	}
}