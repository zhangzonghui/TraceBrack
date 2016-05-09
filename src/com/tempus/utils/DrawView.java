package com.tempus.utils;

import java.util.ArrayList;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.IInterface;
import android.util.Log;
import android.view.View;

import com.tempus.entity.LablePointsEntity;
import com.tempus.traceback.R;

@SuppressLint("ResourceAsColor")
public class DrawView extends View {
    private int layout_height;
    private int layout_width;
    private Context mcontext;
    private int types;
    private int num ;
    private ArrayList<LablePointsEntity> adressData;
    
    
   
	public DrawView(Context context,int height,int width,int type,int num,ArrayList<LablePointsEntity> adressData) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mcontext = context;
		this.layout_height = height;
		this.layout_width = width;
		this.types = type;
		this.num = num;
		this.adressData = adressData;
		
	}
	
	

	
	
	
	 @SuppressLint("ResourceAsColor")
	@Override  
	    protected void onDraw(Canvas canvas) {  
	        super.onDraw(canvas); 
	       
	        if(types == 1){
	        	drawPoint(canvas);
	        }
	        
	        
	       
	   }
	 
	  public void drawPoint(Canvas canvas){
		  
		  
		  
	        int minwidth = (layout_width-50)/num;
	        int minheight = (layout_height-50)/num;
	       // int min = (layout_width-10)/num;
	        Paint p = new Paint();  
	        p.setStrokeWidth(10);
            p.setColor(mcontext.getResources().getColor(R.color.red));// 设置红色  
            Paint ptext = new Paint();  
            ptext.setTextSize(30);
            ptext.setColor(mcontext.getResources().getColor(R.color.black));// 设置H  
            int pointWindth = 0;
            int pointHeight = 0;
            int bepointWindth = 0;
            int beintHeight = 0;
            Log.e("+++++++++++++++++minwidth", minwidth+","+minheight+","+num+","+layout_width+","+layout_height);
            
            	for(int i =0;i<num;i++){
            		if(i == 0){
                    	canvas.drawCircle(50,200, 20, p);
                    	canvas.drawText(adressData.get(0).getLocationName(), 30, 250, ptext);
                    }else{
                    	if(i%2==0){
                    		pointWindth = 10+minwidth*i;
                    		pointHeight = 10+minheight/2*i;
                    		canvas.drawText(adressData.get(i-1).getLocationName(), pointWindth-50, pointHeight-30, ptext);
                    	}else{
                    		pointWindth = 10+minwidth/3*2*i;
                    		pointHeight = 10+minheight*i;
                    		canvas.drawText(adressData.get(i-1).getLocationName(), pointWindth-50, pointHeight+50, ptext);
                    	}
                    	if(i==1){
                    		canvas.drawLine(50, 200, pointWindth, pointHeight, p);
                    	}else if(i>1){
                    		if((i-1)%2==0){
                    			bepointWindth = 10+minwidth*(i-1);
                    			beintHeight = 10+minheight/2*(i-1);
                        	}else{
                        		bepointWindth = 10+minwidth/3*2*(i-1);
                        		beintHeight = 10+minheight*(i-1);
                        	}
                    		canvas.drawLine(bepointWindth, beintHeight, pointWindth, pointHeight, p);
                    	}
                    	
                    	
                    	canvas.drawCircle(pointWindth,pointHeight, 20, p);
                    }
                	
                	
                	//canvas.drawLine(bepointWindth, pointWindth, beintHeight, pointHeight, p);
                	
                	
                
           }
            
	       
	 }
	
	 
	
	

}
