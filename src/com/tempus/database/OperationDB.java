package com.tempus.database;

import java.util.ArrayList;
import java.util.List;

import com.tempus.entity.LoginGet;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;



public class OperationDB {

	private static final String TAG = "operationdb";

	private Context mContext;

	private static OperationDB operationdb = null;
	private SQLiteDatabase database;

	public OperationDB(Context context) {
		mContext = context;
	}

	/**
	 * You can at anywhere use this function to get the operationdb instance.
	 * @param context
	 * @return
	 */
	public static OperationDB getInstance(Context context)
	{
		if(operationdb == null)
		{
			operationdb = new OperationDB(context);
		}
		return operationdb;
	}

	/**
	 * This function only for inside operationdb class.
	 * It must be called before using database.
	 * @throws SQLException
	 */
	@SuppressWarnings("deprecation")
	private void open() throws SQLException
	{
		if (database == null || !database.isOpen()){
			database = getConnection();
		}
	}
	
	public void close()
	{
		if(database != null && database.isOpen())
		{
			database.close();
			database = null;
		}
	}

	/**
	 * Create new database or open exist database.
	 * @return
	 */
	private SQLiteDatabase getConnection()
	{
		SQLiteDatabase sqliteDatabase = null;
		try {
			sqliteDatabase = new DatabaseHelper(mContext).getWritableDatabase();
	 } catch (Exception e) {
		 Log.e("+++++++++++++++SQLiteDatabase getConnection", "ERROR");
     }
		return sqliteDatabase;  
	}     
	
	
	
	  
	/**
	 * 增加一个用户
	 * @param data
	 * @return
	 */
	public synchronized long addUserListData(LoginGet data) {
		long rowID;
		open();

		try {
			database.beginTransaction();
			ContentValues values = new ContentValues();
			
			if(data.getId() != null){
			values.put(Provider.UserList.id, data.getId());
			}
			if(data.getIsValidatjeesiteLogin() != null){
				values.put(Provider.UserList.isValidatjeesiteLogin, data.getIsValidatjeesiteLogin());
			}
			if(data.getLoginName() != null){
				values.put(Provider.UserList.loginName, data.getLoginName());
			}
			
			if(data.getMessage() != null){
				values.put(Provider.UserList.message, data.getMessage());
			}
			/*if(data.getMobileLogin() != null){
				values.put(Provider.UserList.mobileLogin, data.getMobileLogin());
			}*/
			if(data.getName() != null){
				values.put(Provider.UserList.name, data.getName());
			}
			if(data.getRememberMe() != null){
				values.put(Provider.UserList.rememberMe, data.getRememberMe());
			}
			
			if(data.getSessionid() != null){
				values.put(Provider.UserList.sessionid, data.getSessionid());
			}
			
			/*if(data.getSuccess() != null){
				values.put(Provider.UserList.success, data.getSuccess().to);
			}*/
			
			if(data.getUsername() != null){
				values.put(Provider.UserList.username, data.getUsername());
			}
			
			rowID = database.insertWithOnConflict(
					Provider.UserList.TABLE_NAME, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
			
			mContext.getContentResolver().notifyChange(
					Provider.UserList.CONTENT_URI, null);

			database.setTransactionSuccessful();
		} finally {
			if (database != null) {
				database.endTransaction();
				////database.close();
			}
		}
         return rowID;
	}
	
	
	/**
	 * 增加一个用户
	 * @param data
	 * @return
	 */
	public synchronized long UpdateUserListData(LoginGet data) {
		long rowID;
		open();

		try {
			database.beginTransaction();
			ContentValues values = new ContentValues();
			
			if(data.getId() != null){
			values.put(Provider.UserList.id, data.getId());
			}
			if(data.getIsValidatjeesiteLogin() != null){
				values.put(Provider.UserList.isValidatjeesiteLogin, data.getIsValidatjeesiteLogin());
			}
			if(data.getLoginName() != null){
				values.put(Provider.UserList.loginName, data.getLoginName());
			}
			
			if(data.getMessage() != null){
				values.put(Provider.UserList.message, data.getMessage());
			}
			/*if(data.getMobileLogin() != null){
				values.put(Provider.UserList.mobileLogin, data.getMobileLogin());
			}*/
			if(data.getName() != null){
				values.put(Provider.UserList.name, data.getName());
			}
			if(data.getRememberMe() != null){
				values.put(Provider.UserList.rememberMe, data.getRememberMe());
			}
			
			if(data.getSessionid() != null){
				values.put(Provider.UserList.sessionid, data.getSessionid());
			}
			
			/*if(data.getSuccess() != null){
				values.put(Provider.UserList.success, data.getSuccess());
			}*/
			
			if(data.getUsername() != null){
				values.put(Provider.UserList.username, data.getUsername());
			}
			
			rowID = database.insertWithOnConflict(
					Provider.UserList.TABLE_NAME, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
			
			mContext.getContentResolver().notifyChange(
					Provider.UserList.CONTENT_URI, null);

			database.setTransactionSuccessful();
		} finally {
			if (database != null) {
				database.endTransaction();
				////database.close();
			}
		}
         return rowID;
	}
	
	/**
	 * 根据ID查找用户
	 * @return
	 */
	public synchronized LoginGet queryUserData(String LoginName) {
		LoginGet data = null;
		open();
		Cursor cursor = null;
		try { 
			database.beginTransaction();
			
			String sql = "select * from '" + Provider.UserList.TABLE_NAME +
					"' where " + Provider.UserList.loginName + " = '"
					+ LoginName + "'";
			cursor = database.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				 data = createLoginGetData(cursor); 
			}

			database.setTransactionSuccessful();  
		} finally {
			if (database != null)
				database.endTransaction();
			if (null != cursor) {
				cursor.close();
			}
		}

       return data;
	}
	
	/**
	 * 判断是否存在消息列表数据
	 */
	
	public synchronized boolean checkUserExist(String ID) {

		open();
		Cursor cursor = null;
		boolean ret = false;
		try {
			database.beginTransaction();
			cursor = database.query(Provider.UserList.TABLE_NAME, null,
					Provider.UserList.sessionid + "='" + ID + "'", null,
					null, null, null);
			if (cursor != null && cursor.moveToFirst())
				ret = true;
			else
				ret = false;

			database.setTransactionSuccessful();
		} finally {
			if (database != null)
				database.endTransaction();
			if (database != null)
				//database.close();
			if (null != cursor) {
				cursor.close();
			}
		}
		
		return ret;
	}
	
	/**
	 * ���ɾ���Ϣ��һ��ʵ��
	 * @param cursor
	 * @return
	 */
    public LoginGet createLoginGetData(Cursor cursor){
    	LoginGet data = new LoginGet();
    	data.setId(cursor.getString(1));
    	data.setIsValidatjeesiteLogin(cursor.getString(2));
    	data.setLoginName(cursor.getString(3));
    	data.setMessage(cursor.getString(4));
    	//data.setMobileLogin(cursor.getString(5));
    	data.setName(cursor.getString(6));
    	data.setRememberMe(cursor.getString(7));
    	data.setSessionid(cursor.getString(8));
    	//data.setSuccess(cursor.getString(9));
    	data.setUsername(cursor.getString(10));
    	return data;
    }
    
    
	
	
	}
