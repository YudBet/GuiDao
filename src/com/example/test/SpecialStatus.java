package com.example.test;

import com.example.guidao.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

public class SpecialStatus {
	private static int max_timeMillis = 7001;
	private static int NORMAL = 0;
	private static int INVINCIBLE = 1;
	private static int FASTER = 2;
	private static int SLOWER = 3;
	private static int FLY = 4;
	private static int TRANSFORM = 5;
	public static int[] statusTime = new int[]{0,0,0,0,0,0};
	private static Bitmap[] bpStatus = new Bitmap[statusTime.length];
	static int w,h;
	static Paint paint =new Paint(); 
    
	static{		
		Bitmap tmp = BitmapFactory.decodeResource(GameView.gv.getResources(), R.drawable.invincible);
		w = tmp.getWidth();
		h = tmp.getHeight();
		float destH = (float)(1.0 * GameView.gv.screenH/8);
		float destW = (float)(1.0 * tmp.getWidth()/tmp.getHeight() * destH);
		float scaleWidth =  destW / tmp.getWidth();
		float scaleHeight = destH / tmp.getHeight();		
		Matrix m = new Matrix(); 
		m.postScale(scaleWidth, scaleHeight);//������������������
		bpStatus[1] = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), m ,true);
		
		tmp = BitmapFactory.decodeResource(GameView.gv.getResources(), R.drawable.faster);
		bpStatus[2] = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), m ,true);
		
		tmp = BitmapFactory.decodeResource(GameView.gv.getResources(), R.drawable.slower);
		bpStatus[3] = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), m ,true);
		
		tmp = BitmapFactory.decodeResource(GameView.gv.getResources(), R.drawable.fly);
		bpStatus[4] = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), m ,true);
		
		tmp = BitmapFactory.decodeResource(GameView.gv.getResources(), R.drawable.transform);
		bpStatus[5] = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), m ,true);
		
		paint.setAntiAlias( true );
		paint.setColor(Color.RED);
		paint.setFakeBoldText(true);
		paint.setTextSize(GameView.gv.screenW/20);
	}
	
	
	public static void initial(){
		for(int i=0; i<statusTime.length; i++){
			statusTime[i] = 0;
			reset(i);
		}
	}
	public static void draw(Canvas canvas){
		int top = GameView.placeNameH;
		for(int i=1;i<bpStatus.length;i++){
			if(statusTime[i] != 0){
				canvas.drawBitmap(bpStatus[i], 0, top, null);
				top += bpStatus[i].getHeight();
				canvas.drawText(""+((statusTime[i]+2)/1000), 0, top, paint);
				
			}
		}
	}
	
	public static void updateTime(int millis){
		for(int i=1; i<statusTime.length; i++){
			if(statusTime[i] == 0){
				continue;
			}else if(statusTime[i] < millis){				
				reset(i);
			}else
				statusTime[i] -= millis;
		}
	}
	
	
	public static void reset(int status){
		if(status == NORMAL){
			
		}else if(status == INVINCIBLE){
			
		}else if(status == FASTER){
			Ghost.move_offset = Ghost.tmp_move_offset;
			//QuestionMark.move_offset = QuestionMark.tmp_move_offset;
			//GameView.bgThread.offset = GameView.bgThread.tmp_offset;
		}else if(status == SLOWER){ //Log.d("myLog", "move_offset" + Ghost.move_offset);
			Ghost.move_offset = Ghost.tmp_move_offset;
			//QuestionMark.move_offset = QuestionMark.tmp_move_offset;
			//GameView.bgThread.offset = GameView.bgThread.tmp_offset;
		}else if(status == FLY){
			if(GameView.gv.player.y == GameView.gv.player.UP || GameView.gv.player.y == GameView.gv.player.DOWN )
				GameView.player.status = Item.status_run;
			else
				GameView.player.status = Item.status_jump;
			reset(TRANSFORM);
		}else if(status == TRANSFORM){
			GameView.player.bp = BitmapFactory.decodeResource(GameView.gv.getResources(), R.drawable.rolerun1);
			GameView.player.initially();
		}
		statusTime[status] = 0;
	}
	
	public static void setStatus(int status){
		statusTime[status] = max_timeMillis;
		if(status == NORMAL){
			
		}else if(status == INVINCIBLE){
			
		}else if(status == FASTER){
			if(statusTime[SLOWER] != 0)
				reset(SLOWER);
			Ghost.move_offset = (int)(Ghost.tmp_move_offset * 1.4);
			//QuestionMark.move_offset = (int)(QuestionMark.tmp_move_offset * 1.3);
			//GameView.bgThread.offset = (int)(GameView.bgThread.tmp_offset * 0.5);
		}else if(status == SLOWER){
			if(statusTime[FASTER] != 0)
				reset(FASTER);
			Ghost.move_offset = (int)(Ghost.tmp_move_offset * 0.7);
			//QuestionMark.move_offset = (int)(QuestionMark.tmp_move_offset * 0.85);
			//GameView.bgThread.offset = (int)(GameView.bgThread.tmp_offset * 0.5);
		}else if(status == FLY){
			GameView.player.status = Item.status_jump;
			setStatus(TRANSFORM);

		}else if(status == TRANSFORM){
			GameView.player.bp = BitmapFactory.decodeResource(GameView.gv.getResources(), R.drawable.ghost2);
			GameView.player.initially();
		}
	}
	
}
