package com.example.test;

import com.example.guidao.R;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.EditText;

public class RestartButton{
	Bitmap bp;
	GameView gv;
	Rect rect;
	RectF rectF;
	int x,y;
	int adjustW, adjustH;
	
	public RestartButton(GameView gv){
		this.gv = gv;
		bp = BitmapFactory.decodeResource(gv.getResources(), R.drawable.restart);
		
		adjustH = gv.screenH*1/4;
		double scale = 1.0*adjustH/bp.getHeight();
		adjustW = (int)(bp.getWidth()*scale);
		y = adjustH;	
		x = (gv.screenW-adjustW)/2;			
		rect = new Rect(0, 0, bp.getWidth(), bp.getHeight());
	}
	
	public void draw(Canvas canvas) {
		Paint paint =new Paint(); 
	    paint.setAntiAlias( true );   //���������
		rectF = new RectF(x, y, x+adjustW, y+adjustH); 
	    canvas.drawBitmap(bp, rect, rectF, paint);
	    
	    
	}
}
