package com.example.todolistview;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Island {
	
	private String name; // island's name
	private String deadline; // deadline in form "Thu 04/21"
	private String imgUriStr;
	private int year, month, day;
	private int grade = 2; // red: 0, orange: 1, green: 2
	
	public Island() {}
	
	public Island(String name, String imgUriStr, int year, int month, int day) 
	{
		this.name = name;
		this.imgUriStr = imgUriStr;
		this.year = year;
		this.month = month;
		this.day = day;
		deadlineSetting();
	}
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	public void deadlineSetting()
	{
		Calendar cal = Calendar.getInstance();
	    cal.setTime(new Date(year, month, day));
	    cal.add(Calendar.DAY_OF_MONTH, -1);
	    SimpleDateFormat sdf = new SimpleDateFormat("EEE");
	    deadline = sdf.format(cal.getTime());
	    
		sdf = new SimpleDateFormat(" MM/dd");
		deadline += sdf.format(new Date(year, month, day));
	}
	
	
	public String getName()
	{
		return name;
	}
	
	public String getImgUriStr()
	{
		return imgUriStr;
	}
	
	public String getDeadline()
	{
		return deadline;
	}
	
	public int getGrade()
	{
		return grade;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setImgUriStr(String imgUriStr)
	{
		this.imgUriStr = imgUriStr;
	}
	
	public void setDeadline(String deadline)
	{
		this.deadline = deadline;
	}
	
	public void setGrade(int grade)
	{
		this.grade = grade;
	}
	
	@Override
	public String toString() 
	{
		return name + " " + deadline + " " + imgUriStr + " " + grade;
	}
}
