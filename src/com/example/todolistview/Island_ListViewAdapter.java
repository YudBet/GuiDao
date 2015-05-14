package com.example.todolistview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.guidao.R;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Island_ListViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater; 
	private ArrayList<Island> mislandlist;
	private ArrayList<String> islandNames;
	private ItemView itemView;
	private final int[] valueViewID = {
			R.id.ItemName, R.id.ItemWeek, R.id.ItemMD, 
			R.id.btnRed, R.id.btnOrange, R.id.btnGreen, R.id.ivImg};
	boolean m_bRed, m_bOrange, m_bGreen;
	
	public Island_ListViewAdapter(Context context, 
			ArrayList<Island> mislandlist, ArrayList<String> islandNames) 
	{
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mislandlist = mislandlist;
		this.islandNames = islandNames;
	}
	
	@Override
	public int getCount() 
	{
		return mislandlist.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return mislandlist.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView != null)
			itemView = (ItemView) convertView.getTag();
		else
		{
			convertView = mInflater.inflate(R.layout.adapter, null);
			itemView = new ItemView();
			itemView.itemName = (TextView) convertView.findViewById(valueViewID[0]);
			itemView.itemWeek = (TextView) convertView.findViewById(valueViewID[1]);
			itemView.itemMD = (TextView) convertView.findViewById(valueViewID[2]);
			itemView.btnRed = (Button) convertView.findViewById(valueViewID[3]);
			itemView.btnOrange = (Button) convertView.findViewById(valueViewID[4]);
			itemView.btnGreen = (Button) convertView.findViewById(valueViewID[5]);
			itemView.ivImg = (ImageView) convertView.findViewById(valueViewID[6]);
			convertView.setTag(itemView);
		}
		
		
		ArrayList<Island> list = mislandlist;
		if (list != null) 
		{
			Island island = list.get(position);
		    String name = island.getName();
		    String[] deadline = island.getDeadline().split(" ");
		    String week = deadline[0];
		    String md = deadline[1];
		    int grade = island.getGrade();
		    
		    itemView.itemName.setText(name);
		    itemView.itemWeek.setText(week);
		    itemView.itemMD.setText(md);
	
		    /** Image setting */
		    ContentResolver cr = context.getContentResolver();
		    try 
		    {
		   	 	String imgUriStr = island.getImgUriStr();
		   	 	Uri uri = Uri.parse(imgUriStr);
		   	 	Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
		   	 	itemView.ivImg.setImageBitmap(bitmap);
		    } 
		    catch (FileNotFoundException e) 
		    {
		    	Log.e("FileNotFoundException", e.getMessage(),e);
		    }
		    catch (IOException e) 
		    {
		    	Log.e("IOException", e.getMessage(),e);
		    }
		     
		    /** buttons setting: r-o-g */
		    itemView.btnRed.setOnClickListener(new OnClickListener() {
		    	
				@SuppressLint("NewApi")
				@Override
				public void onClick(View v) {
					m_bRed = !m_bRed;
					itemView.btnRed.setBackgroundResource(
							m_bRed ? R.drawable.red_round_button_on : R.drawable.red_round_button);
					Toast.makeText(context,"GG " + m_bRed,Toast.LENGTH_SHORT).show();
				}
		    });
		}

		return convertView;
	}
	
	private class ItemView
	{
		TextView itemName;
		TextView itemWeek, itemMD;
		Button btnRed, btnOrange, btnGreen;
		ImageView ivImg;
	}

}
