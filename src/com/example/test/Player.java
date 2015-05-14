package com.example.test;

import com.example.guidao.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;


public class Player implements Item{ //Log.d("myLog", "run");	
	int UP;
	int DOWN;
	Bitmap[] bms = new Bitmap[2];
	Bitmap bp;
	GameView gv;
	Rect rect;
	RectF rectF;
	int x,y;
	int adjustW, adjustH;
	int status;
	//int question_status;//������������ ArrayList<Integer> specialStatus = new ArrayList<Integer>();//������������
	boolean up_down; //  true ������ , false ������
	RoleListener roleListener;
	RunBPThread rbt;
	
	public Player(GameView gv){
		this.gv = gv;
		
		bms[0] = BitmapFactory.decodeResource(gv.getResources(), R.drawable.rolerun1);
		bms[1] = BitmapFactory.decodeResource(gv.getResources(), R.drawable.rolerun2);	
		bp = bms[0];
		
		initially();
		
		y = DOWN;
		up_down = (this.y == UP)? true : false;
		
		roleListener = new RoleListener();
		roleListener.start();
		
		rbt = new RunBPThread();
		rbt.start();
	}
	
	public void initially(){
		adjustH = gv.screenH*1/3;
		double scale = 1.0*adjustH/bp.getHeight();
		adjustW = (int)(bp.getWidth()*scale);
		//y = gv.screenH-adjustH;	
		x=gv.screenW/10;				
		rect = new Rect(0, 0, bp.getWidth(), bp.getHeight());
		
		UP = (int)(gv.screenH-adjustH*1.7);
		DOWN = gv.screenH-adjustH;

	}
	class RunBPThread extends Thread{
		@Override
		public void run(){
			while(!gv.act.isFinishing() && !gv.gameOver){
				if(status == Item.status_run && SpecialStatus.statusTime[QuestionMark.TRANSFORM] == 0){
					bp = (bp==bms[0])? bms[1] : bms[0];
					try {
						this.sleep(150);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	class RoleListener extends Thread{
		int vertical_UP_Offset = (int)(gv.screenH*0.07);
		int vertical_DOWN_Offset = (int)(gv.screenH*0.04);
		int tmp_vertical_UP_Offset = vertical_UP_Offset;
		int tmp_vertical_DOWN_Offset = vertical_DOWN_Offset;
		int skyTime = 100; //������������(������)
		
		@Override
		public void run() {
			while(!gv.act.isFinishing() && !gv.gameOver){	
				try {
					
					if(status == Item.status_jump){			//Log.d("myLog", "DOWN"+DOWN);
						int tmpY = y;								//Log.d("myLog", "y"+y);
						int ceil = (tmpY==DOWN)? adjustH/2 : 0;		//Log.d("myLog", "adjustH/2"+adjustH/2);
						//������������
						while(y > ceil){
							y -= vertical_UP_Offset;
							this.sleep(30);
						}
						
						//������
						do{
							this.sleep(skyTime);
						}while(SpecialStatus.statusTime[QuestionMark.FLY] != 0);
						
						//������������
						while(y < tmpY){
							y += vertical_DOWN_Offset;
							this.sleep(30);
						}
						// ������
						y = tmpY;	
						if(status != Player.status_die)
							status = Player.status_run;
					}
					else if(status == Item.status_die){
						break;
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
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
		
	}
}
