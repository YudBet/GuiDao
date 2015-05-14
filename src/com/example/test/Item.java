package com.example.test;

import android.graphics.Canvas;

public interface Item {
	int status_run = 0;
	int status_jump = 1;
	int status_die = 2;

	
	void move();
	void draw(Canvas canvas);
}
