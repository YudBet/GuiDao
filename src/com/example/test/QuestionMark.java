package com.example.test;

import com.example.guidao.R;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class QuestionMark implements Item{
	public final int UP;
	public final int DOWN;
	public static final int NORMAL = 0;
	public static final int INVINCIBLE = 1;
	public static final int FASTER = 2;
	public static final int SLOWER = 3;
	public static final int FLY = 4;
	public static final int TRANSFORM = 5;
	
	GameView gv;
	Bitmap bp;
	static int adjustW, adjustH;
	static int move_offset = (int)(GameView.gv.screenW*0.03);
	static int tmp_move_offset = move_offset;
	Rect rect;
	RectF rectF;
	int x,y;
	boolean up_down;
	
	public QuestionMark(GameView gv, int x, int y){
		this.gv = gv;
		this.bp = BitmapFactory.decodeResource(gv.getResources(), R.drawable.question);
		
		adjustH = GameView.gv.screenH*1/6;
		double scale = 1.0*adjustH/bp.getHeight();
		adjustW = (int)(bp.getWidth()*scale);
		rect = new Rect(0, 0, bp.getWidth(), bp.getHeight());
		
		UP = gv.screenH - (int)(adjustH*2.5); 
		DOWN = gv.screenH-adjustH;
		this.y = y;	
		this.x = x;
		up_down = (this.y == UP)? true : false;
	}
	
	
	
	@Override
	public void draw(Canvas canvas) {
		Paint paint =new Paint(); 
	    paint.setAntiAlias( true );   //���������������������������
		rectF = new RectF(x, y, x+adjustW, y+adjustH); 
	    canvas.drawBitmap(bp, rect, rectF, paint);
	}


	@Override
	public void move() {	
		x -= move_offset;			
		if(x+adjustW<=10) 
			gv.itemList.remove(this);
	}
	
	public int getStatus(){
		return (int)(Math.random()*5)+1;
	}
}
