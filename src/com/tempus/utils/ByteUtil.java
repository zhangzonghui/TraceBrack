package com.tempus.utils;

public class ByteUtil {

	public ByteUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static String ByteArrayToHexString(byte [] inarray) { 
		int i, j, in;     
		String [] hex = {"0","1","2","3","4","5","6","7"
				,"8","9","A","B","C","D","E","F"};   
		String out= "";    
		for(j = 0 ; j < inarray.length ; ++j)       
		{          
			in = (int) inarray[j] & 0xff;       
			i = (in >> 4) & 0x0f;      
			out += hex[i];          
			i = in & 0x0f;          
			
			out += hex[i];           
			}       
		return out;  
	}
	

  

}
