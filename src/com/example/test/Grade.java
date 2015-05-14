package com.example.test;

import com.example.guidao.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Grade {
	private GameView gv;
	private Bitmap[] number = new Bitmap[10];
	private int center;
	private int y = 10;
	
	public Grade(GameView gv){
		this.gv = gv;
		//������
		Bitmap tmp = BitmapFactory.decodeResource(gv.getResources(), R.drawable.number);
		Bitmap tmp2 = null;
		int w = tmp.getWidth()/10;
		int x=0;
		tmp2 = Bitmap.createBitmap(tmp, x, 0, w, tmp.getHeight());
		float tmpW = (float)(1.0 * gv.screenW/15);
		float tmpH = (float)(1.0 * tmp2.getHeight()/tmp2.getWidth() * tmpW);
		float scaleWidth =  tmpW / tmp2.getWidth();
		float scaleHeight = tmpH / tmp2.getHeight();		
		Matrix m = new Matrix(); 
		m.postScale(scaleWidth, scaleHeight);//������������������
		number[0] = Bitmap.createBitmap(tmp2, 0, 0, tmp2.getWidth(), tmp2.getHeight(), m ,true);
		x+=w;
		for(int i=1; i<number.length;i++){
			tmp2 = Bitmap.createBitmap(tmp, x, 0, w, tmp.getHeight());
			number[i] = Bitmap.createBitmap(tmp2, 0, 0, tmp2.getWidth(), tmp2.getHeight(), m ,true);
			x+=w;
		}	
		//������������
		center = gv.screenW/2;
	}
	
	public void draw(Canvas canvas, int grade) {
		String s = String.valueOf(grade);
		int startX = center - number[0].getWidth() * s.length()/2;
		int[] splitGrade = new int[s.length()];
		for(int i= splitGrade.length-1; i>=0; i--){
			splitGrade[i] = grade%10;
			grade /= 10;
		}
		
		for(int i=0; i<splitGrade.length;i++){
			canvas.drawBitmap(number[splitGrade[i]], startX, y, null);
			startX += number[i].getWidth()+3;
		}
	}
}
