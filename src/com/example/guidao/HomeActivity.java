package com.example.guidao;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.test.*;
import com.example.todolistview.*;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		setContentView(R.layout.activity_home);
		
		ImageButton ibStartGame = (ImageButton) findViewById(R.id.imageButton1);
		ImageButton ibTextBook = (ImageButton) findViewById(R.id.imageButton2);
		ImageButton ibExit = (ImageButton) findViewById(R.id.imageButton3);
		ibStartGame.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, GameActivity.class);
				startActivity(intent);
			}			
		});
		
		ibTextBook.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, MainActivity.class);
				startActivity(intent);
			}			
		});
		
		ibExit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Log.d("myLog","123");
				finish(); 
			}			
		});
		
	}
	
	
}
