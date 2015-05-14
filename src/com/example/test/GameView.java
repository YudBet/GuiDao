package com.example.test;

import java.util.ArrayList;

import com.example.guidao.R;
import com.example.test.Ghost.GhostListener;
import com.example.todolistview.MainActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;


public class GameView extends View {
	int screenW, screenH;  //������������������������������������������������������
	int showW; //���������������������������������������������,������������������������������������������������������"������������������������������������������������������������������������"
	int startX;  
	Bitmap bg;  // ���������������������������
	Bitmap placeName;
	static int placeNameW, placeNameH;
	static GameActivity act;
	static Player player;
	ArrayList<Item> itemList;
	RestartButton restartButton;
	static GameView gv;
	static boolean gameOver = false;
	static BgThread bgThread;
	Grade grade;
	int player_grade;
	static final int bgSleepTime = 40;
	UpdateSpecialStatueThread  usst;
	boolean ismove = false;
	static int hightestGrade;
	boolean hasChange = false;
	
	public GameView(GameActivity act) {
		super(act); 
		this.act = act;
		this.gv = this;
		setFocusable(true); 
		
		// ������������������������������������������������������
		DisplayMetrics metrics = new DisplayMetrics();  
		act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenW = metrics.widthPixels;
		screenH = metrics.heightPixels;
						
		
		
		initial();		
		this.setOnClickListener(ock);
		this.setOnTouchListener(otl);
	}
	
	public void setBG(Bitmap newBP){
		bg = newBP;	
		showW = (bg.getHeight()<screenH)?  (int)(screenW/(1.0*screenH/bg.getHeight())) : screenW;
		bgThread.tmp_offset = bgThread.offset = (int)(showW*0.02);
 
	}
	
	public void setPlaceName(Bitmap newBpNAME){
		placeName = newBpNAME;	
	}
	
	public void initial(){	
		// ���������������������������������������������
		bg = BitmapFactory.decodeResource(getResources(), R.drawable.game_bg1);	
		showW = (bg.getHeight()<screenH)?  (int)(screenW/(1.0*screenH/bg.getHeight())) : screenW;
		//������������������������������������
		placeName = BitmapFactory.decodeResource(getResources(), R.drawable.pingtung);	
				
		grade = new Grade(this);
		hasChange = false;
		player_grade = 0;
		itemList = new ArrayList<Item>();
		gameOver = false;		
		bgThread = new BgThread();
		bgThread.start();
		Ghost.gl = new Ghost.GhostListener();
		Ghost.gl.start();
		restartButton = new RestartButton(this);
		player = new Player(this);
		creatItem();
		SpecialStatus.initial();
		usst = new UpdateSpecialStatueThread();
		usst.start();
	}
	
	OnClickListener ock = new OnClickListener(){
		@Override
		public void onClick(View v) {
			if(SpecialStatus.statusTime[QuestionMark.FLY] != 0){
				SpecialStatus.statusTime[QuestionMark.FLY] = 0;
				SpecialStatus.reset(QuestionMark.FLY);
			}
			else if(!ismove && !gameOver)
				player.status = Item.status_jump;
			ismove = false;
		}	
	};
	
	OnTouchListener otl = new View.OnTouchListener() {
		int tmpY;
		
        @Override
        public boolean onTouch(View v, MotionEvent event) {
        	if(player.status == Item.status_jump)
        		return false;
        
        	int x = (int)event.getX();
			int y = (int)event.getY();
        	if(event.getAction() == MotionEvent.ACTION_UP){
        		if(gameOver){
        			if(restartButton.rectF.intersect(x, y, x+1, y+1)){
        				initial();
        				return true;
        			}
        		}
        		
 
        			
        	}
        	else if(event.getAction() == MotionEvent.ACTION_DOWN){
        		tmpY = y;
        	}
        	else if (event.getAction() == MotionEvent.ACTION_MOVE){
        		if(SpecialStatus.statusTime[QuestionMark.FLY] != 0)
        			return false;
        		if(y-tmpY > 20){
        			//down
        			player.y = player.DOWN;
        			player.up_down = false;
        		}
        		else if(tmpY-y>20){
        			//UP
        			player.y = player.UP;
        			player.up_down = true;
        		}  
        		ismove = true;
        	}

            return false;
        }
    };
	
	
	@Override  
	protected void onDraw(Canvas canvas) {  
	    super.onDraw(canvas);         
	    Paint paint =new Paint(); 
	    paint.setAntiAlias( true );   //���������������������������
	    
	    // ������������������������������������
	    if(bg.getWidth()-startX < showW){
	    	Rect rect1 = new Rect(startX,0,bg.getWidth(),bg.getHeight());	    	
	    	double ratio = 1.0*rect1.width()/showW;
	    	RectF rectF1 = new RectF(0,0,(int)(screenW*ratio),screenH);
	    	
	    	double ratio2 = 1-ratio;
	    	Rect rect2 = new Rect(0,0,(int)(ratio2*(bg.getWidth()-startX)/ratio),bg.getHeight());
	    	RectF rectF2 = new RectF(rectF1.width(),0,screenW,screenH);
	    	canvas.drawBitmap(bg, rect1, rectF1, paint);
	    	canvas.drawBitmap(bg, rect2, rectF2, paint);
	    }
	    else{
	    	// ������������������������������������������������������������������������
	    	Rect rect = new Rect(startX, 0, startX+showW,bg.getHeight());
	    	// ���������������������������������������������
	    	RectF rectF = new RectF(0, 0, screenW, screenH); 
	    	canvas.drawBitmap(bg, rect, rectF, paint);
	    }
	    
	    //���������������������������
    	Rect rect = new Rect(0, 0, placeName.getWidth(),placeName.getHeight());
    	placeNameW = (int)(1.0 * screenW/5);
    	placeNameH = (int)(1.0 * placeName.getHeight()/placeName.getWidth() * placeNameW);
    	RectF rectF = new RectF(0, 0, placeNameW, placeNameH); 
    	canvas.drawBitmap(placeName, rect, rectF, paint);
	    
	    if(itemList.size() == 0)
	    	creatItem();
	    for(int i=0; i<itemList.size();i++){
	    	itemList.get(i).move();	    		    		
	    }	
	    for(int i=0; i<itemList.size();i++){
	    	//if(itemList.get(i).x > screenW)
	    		//break;
	    	itemList.get(i).draw(canvas);
	    	checkIntersects(player, itemList.get(i));
	    }
	    player.draw(canvas);
	    if(gameOver)
	    {
	    	restartButton.draw(canvas);
	    	
	    	// feedback
	    	ArrayList<String> islandNames = MainActivity.getIslandNames();
	    	if (islandNames.isEmpty()) islandNames.add("Java作業");
	    	int ran = (int)(Math.random() * islandNames.size());
	    	String taskStr = islandNames.get(ran);
			AlertDialog prompt = new AlertDialog.Builder(act)
				.setTitle("遊戲結束")
				.setMessage("無論你怎麼逃~\n終究逃不過" + taskStr + "~~").create();
		
			prompt.show();
	    }
	    
	    SpecialStatus.draw(canvas);
	    
	    grade.draw(canvas, player_grade);
	} 
	
	
	
	public void checkIntersects(Player player, Item other){
		if(other instanceof Ghost){
			Ghost g = (Ghost)other;
			if(player.up_down != g.up_down) //������������������������������������������������������,���������������������������������������������
				return;
			
			if ((int)(1.1*player.x) > g.x+g.adjustW || (int)(0.9*player.x) + player.adjustW < g.x
					|| player.y > g.y + g.adjustH || player.y + (int)(1.0*player.adjustH*0.75) < g.y) {
                return;
			}
			else{
				if(SpecialStatus.statusTime[QuestionMark.INVINCIBLE] != 0){
					itemList.remove(other);
				}
				else{
					player.status = Item.status_die;
					gameOver = true;
					if(hightestGrade < this.player_grade)
						act.recordGrade(this.player_grade);
				}
				
			}
		}
		else if(other instanceof QuestionMark){
			QuestionMark g = (QuestionMark)other;
			if(player.up_down != g.up_down) //������������������������������������������������������,���������������������������������������������
				return;
			
			if ((int)(1.1*player.x) > g.x+g.adjustW || (int)(0.9*player.x) + player.adjustW < g.x
					|| player.y > g.y + g.adjustH || player.y + (int)(1.0*player.adjustH*0.75) < g.y) {
                return;
			}
			else{
				SpecialStatus.setStatus(g.getStatus());
				itemList.remove(g);
				
				
			}
			
		}
	}
	
	public Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
               case 0 :postInvalidate();   // ������������������
               break;
            }
        }
    };
	
    public void creatItem(){
    	String[][] mode = { {"00110000200000000",
    						 "20000000001000000"},
    						 
    						{"111111111111111111",
    					     "100000000100000001"},
    					     
    						{"0001000020000",
    					     "0000000111000"}};
    						//{"000011111110000000001","000011000000000111111"}};
    	int ghostWidth = Ghost.adjustW; 	
    	int x = screenW;
    	int UP_y = screenH - (int)(Ghost.adjustH*2.5); 
    	int random = (int)(Math.random()*mode.length);
    	for(int i=0; i<mode[random][0].length(); i++){    						
			if(mode[random][0].charAt(i) == '1') 						
				itemList.add(new Ghost(gv,x,UP_y));
			else if(mode[random][0].charAt(i) == '2') 						
				itemList.add(new QuestionMark(gv,x,UP_y));
			if(mode[random][1].charAt(i) == '1') 						
				itemList.add(new Ghost(gv,x,screenH-Ghost.adjustH)); 
			else if(mode[random][1].charAt(i) == '2') 						
				itemList.add(new QuestionMark(gv,x,screenH-QuestionMark.adjustH));
			x += ghostWidth;
		}  
    }
    
    class BgThread extends Thread{
    	int offset = (int)(showW*0.02);
    	int tmp_offset = offset;
    	    	
    	@Override
    	public void run(){  
    		try {
    			while(!gameOver && act.isRun){	
    				startX += offset;	// ������������������������������������������������������������������������
    				player_grade += offset;
    				if(!hasChange && player_grade > 20000 ){
    					hasChange = true;
    					setBG(BitmapFactory.decodeResource(getResources(), R.drawable.game_bg2));
    					setPlaceName(BitmapFactory.decodeResource(getResources(), R.drawable.tainan_name));
    				}
    				if(bg.getWidth()-startX<=0){	// ���������������������������������������������������������������,������������������������������������
    					startX = 0;
    				}
    				Message msg = new Message();
    				msg.what = 0;
    				mHandler.sendMessage(msg);
    						
    				sleep(bgSleepTime);
    			
    			}
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    class UpdateSpecialStatueThread extends Thread{
		@Override
		public void run(){
			while(GameView.act.isRun && !gameOver){				
				try {
					SpecialStatus.updateTime(1000);
					this.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
