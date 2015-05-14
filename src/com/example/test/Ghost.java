package com.example.test;


import com.example.guidao.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class Ghost implements Item{
	public final int UP;
	public final int DOWN;
	Ghost ghost;
	GameView gv;
	static Bitmap[] bms = new Bitmap[2];
	static Bitmap bp;
	static int adjustW, adjustH;
	static int move_offset;
	static int tmp_move_offset;
	static Rect rect;
	static GhostListener gl;
	RectF rectF;
	int x,y;
	boolean up_down;
	
	static{
		bms[0] = BitmapFactory.decodeResource(GameView.gv.getResources(), R.drawable.ghost);
		bms[1] = BitmapFactory.decodeResource(GameView.gv.getResources(), R.drawable.ghost);	//������������2������������
		bp = bms[0];	
		adjustH = GameView.gv.screenH*1/6;
		double scale = 1.0*adjustH/bp.getHeight();
		adjustW = (int)(bp.getWidth()*scale);
		tmp_move_offset = move_offset = (int)(GameView.gv.screenW*0.03);
		rect = new Rect(0, 0, bp.getWidth(), bp.getHeight());
		gl = new GhostListener();
		gl.start();
	}
	
	public Ghost(GameView gv, int x, int y){
		this.gv = gv;
		this.ghost = this;
		
		UP = gv.screenH - (int)(adjustH*2.5); 
		DOWN = gv.screenH-adjustH;
		this.y = y;	
		this.x = x;
		up_down = (this.y == UP)? true : false;
	}
	
	@Override
	public void draw(Canvas canvas) {
		Paint paint =new Paint(); 
	    paint.setAntiAlias( true );   //���������
		rectF = new RectF(x, y, x+adjustW, y+adjustH); 
	    canvas.drawBitmap(bp, rect, rectF, paint);
	}
	
	@Override
	public void move() {	
		x -= move_offset;			
		if(x+adjustW<=10) //x<=adjustW   x+adjustW<=0
			gv.itemList.remove(ghost);
	}
	
	static class GhostListener extends Thread{
		int sleep_time = 200;
		@Override
		public void run() {
			while(!GameView.gv.act.isFinishing() && !GameView.gameOver){
				try {
					bp = (bp==bms[0])? bms[1] : bms[0];
					this.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}	
	}
}
