package com.tempus.zxing.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ParseException;
import android.view.Display;
import android.view.WindowManager;

public class Utils {
	//以下方法用于判断邮箱格式是否正确
	public static boolean isEmail(String strEmail) {
		String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}
	//以下方法用于判断输入的手机号码是否有�?
	public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
	
	   //身份证号码验证：start
	   /**
	     * 功能：身份证的有效验�?
	     * @param IDStr 身份证号
	     * @return 有效：返�?"" 无效：返回String信息
	     * @throws ParseException
	 * @throws java.text.ParseException 
	 * @throws NumberFormatException 
	     */
	    public static String IDCardValidate(String IDStr) throws ParseException, NumberFormatException, java.text.ParseException { 
	        String errorInfo = "";// 记录错误信息 
	        String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4", 
	                "3", "2" }; 
	        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", 
	                "9", "10", "5", "8", "4", "2" }; 
	        String Ai = ""; 
	        // ================ 号码的长�? 15位或18�? ================ 
	        if (IDStr.length() != 15 && IDStr.length() != 18) { 
	            errorInfo = "身份证号码长度应该为15位或18位�??"; 
	            return errorInfo; 
	        } 
	        // =======================(end)======================== 
	 
	        // ================ 数字 除最后以为都为数�? ================ 
	        if (IDStr.length() == 18) { 
	            Ai = IDStr.substring(0, 17); 
	        } else if (IDStr.length() == 15) { 
	            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15); 
	        } 
	        if (isNumeric(Ai) == false) { 
	            errorInfo = "身份�?15位号码都应为数字 ; 18位号码除�?后一位外，都应为数字�?"; 
	            return errorInfo; 
	        } 
	        // =======================(end)======================== 
	 
	        // ================ 出生年月是否有效 ================ 
	        String strYear = Ai.substring(6, 10);// 年份 
	        String strMonth = Ai.substring(10, 12);// 月份 
	        String strDay = Ai.substring(12, 14);// 月份 
	        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) { 
	            errorInfo = "身份证生日无效�??"; 
	            return errorInfo; 
	        } 
	        GregorianCalendar gc = new GregorianCalendar(); 
	        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd"); 
	        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
	                || (gc.getTime().getTime() - s.parse( 
	                        strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) { 
	            errorInfo = "身份证生日不在有效范围�??"; 
	            return errorInfo; 
	        } 
	        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) { 
	            errorInfo = "身份证月份无�?"; 
	            return errorInfo; 
	        } 
	        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) { 
	            errorInfo = "身份证日期无�?"; 
	            return errorInfo; 
	        } 
	        // =====================(end)===================== 
	 
	        // ================ 地区码时候有�? ================ 
	        Hashtable h = GetAreaCode(); 
	        if (h.get(Ai.substring(0, 2)) == null) { 
	            errorInfo = "身份证地区编码错误�??"; 
	            return errorInfo; 
	        } 
	        // ============================================== 
	 
	        // ================ 判断�?后一位的�? ================ 
	        int TotalmulAiWi = 0; 
	        for (int i = 0; i < 17; i++) { 
	            TotalmulAiWi = TotalmulAiWi 
	                    + Integer.parseInt(String.valueOf(Ai.charAt(i))) 
	                    * Integer.parseInt(Wi[i]); 
	        } 
	        int modValue = TotalmulAiWi % 11; 
	        String strVerifyCode = ValCodeArr[modValue]; 
	        Ai = Ai + strVerifyCode; 
	 
	        if (IDStr.length() == 18) { 
	             if (Ai.equals(IDStr) == false) { 
	                 errorInfo = "身份证无效，不是合法的身份证号码"; 
	                 return errorInfo; 
	             } 
	         } else { 
	             return ""; 
	         } 
	         // =====================(end)===================== 
	         return ""; 
	     }
	  
	    /**
	      * 功能：判断字符串是否为数�?
	      * @param str
	      * @return
	      */
	     private static boolean isNumeric(String str) { 
	         Pattern pattern = Pattern.compile("[0-9]*"); 
	         Matcher isNum = pattern.matcher(str); 
	         if (isNum.matches()) { 
	             return true; 
	         } else { 
	             return false; 
	         } 
	     }
	   
	     /**
	      * 功能：设置地区编�?
	      * @return Hashtable 对象
	      */
	     private static Hashtable GetAreaCode() { 
	         Hashtable hashtable = new Hashtable(); 
	         hashtable.put("11", "北京"); 
	         hashtable.put("12", "天津"); 
	         hashtable.put("13", "河北"); 
	         hashtable.put("14", "山西"); 
	         hashtable.put("15", "内蒙�?"); 
	         hashtable.put("21", "辽宁"); 
	         hashtable.put("22", "吉林"); 
	         hashtable.put("23", "黑龙�?"); 
	         hashtable.put("31", "上海"); 
	         hashtable.put("32", "江苏"); 
	         hashtable.put("33", "浙江"); 
	         hashtable.put("34", "安徽"); 
	         hashtable.put("35", "福建"); 
	         hashtable.put("36", "江西"); 
	         hashtable.put("37", "山东"); 
	         hashtable.put("41", "河南"); 
	         hashtable.put("42", "湖北"); 
	         hashtable.put("43", "湖南"); 
	         hashtable.put("44", "广东"); 
	         hashtable.put("45", "广西"); 
	         hashtable.put("46", "海南"); 
	         hashtable.put("50", "重庆"); 
	         hashtable.put("51", "四川"); 
	         hashtable.put("52", "贵州"); 
	         hashtable.put("53", "云南"); 
	         hashtable.put("54", "西藏"); 
	         hashtable.put("61", "陕西"); 
	         hashtable.put("62", "甘肃"); 
	         hashtable.put("63", "青海"); 
	         hashtable.put("64", "宁夏"); 
	         hashtable.put("65", "新疆"); 
	         hashtable.put("71", "台湾"); 
	         hashtable.put("81", "香港"); 
	         hashtable.put("82", "澳门"); 
	         hashtable.put("91", "国外"); 
	         return hashtable; 
	     } 

	    /**验证日期字符串是否是YYYY-MM-DD格式
	      * @param str
	      * @return
	      */
	    public static boolean isDataFormat(String str){
	      boolean flag=false;
	       //String regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
	      String regxStr="^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
	      Pattern pattern1=Pattern.compile(regxStr);
	      Matcher isNo=pattern1.matcher(str);
	      if(isNo.matches()){
	        flag=true;
	      }
	      return flag;
	   }

//	    /**
//	      * 功能：判断字符串是否为数�?
//	      * @param str
//	      * @return
//	      */
//	     private static boolean isNumeric(String str) { 
//	         Pattern pattern = Pattern.compile("[0-9]*"); 
//	         Matcher isNum = pattern.matcher(str); 
//	         if (isNum.matches()) { 
//	             return true; 
//	         } else { 
//	             return false; 
//	         } 
//	     }
	    //数字 格式�?
	    public static Double numberFormat(double data,int scope){
	    	  //10的位数次�? 如保�?2位则 tempDouble=100
	    	  double tempDouble=Math.pow(10, scope);
	    	  //原始数据先乘tempDouble再转成整型，作用是去小数�?
	    	  data=data*tempDouble;
	    	  int tempInt=(int) data;
	    	  //返回去小数之后再除tempDouble的结�?
	    	  return tempInt/tempDouble;
	    	 }
	    
	    
		// 获取屏幕的宽�?
		public static int getScreenWidth(Context context) {
			WindowManager manager = (WindowManager) context.getSystemService(
					context.WINDOW_SERVICE);
			Display display = manager.getDefaultDisplay();
			return display.getWidth();
		}
	
	
}
