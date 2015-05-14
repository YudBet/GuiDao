package com.example.test;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class GameActivity extends Activity {
	GameView gv;
	boolean isRun = true;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //���������
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //���������������������
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
        
        gv = new GameView(this);
        setContentView(gv);
              
        SharedPreferences pref = getSharedPreferences("birthday", MODE_PRIVATE); 
        GameView.hightestGrade = pref.getInt("GRADE", 1);
    }
    
    
    public void recordGrade(int n){
		SharedPreferences pref = getSharedPreferences("birthday", MODE_PRIVATE);
		SharedPreferences.Editor preEdt = pref.edit(); 
		preEdt.putInt("GRADE", n);
		preEdt.commit();
	}
    
    
    
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}


	@Override
	protected void onStop() {
		isRun = false;
		super.onStop();
	}
}
