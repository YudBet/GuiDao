package com.example.todolistview;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import com.example.guidao.R;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private static Island_ListViewAdapter listAdapter;
	private static Island island;
	private static ArrayList<Island> mislandlist;
	private static ArrayList<String> islandNames;
	
	private static Context ctx;
	private static ListView expListView;
	private EditText et;
	private DatePickerDialog dpd;
	private int item_position;
	
	
	private static String imgUriStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ActionBar bar = getSupportActionBar();  
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setHomeButtonEnabled(true);
		    
		ctx = this;
		
		islandNames = new ArrayList<String>();
		mislandlist = new ArrayList<Island>();
		
        expListView = (ListView) findViewById(R.id.lvExp);
        
        expListView.setOnItemClickListener(new ListView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        		
        		Toast.makeText(MainActivity.this,"長按可編輯",Toast.LENGTH_SHORT).show();
        	}
        });

        expListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				expListView.showContextMenu(); 
				item_position = position;
				return true;
			}          
        });

        expListView.setOnCreateContextMenuListener(new ListView.OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.setHeaderTitle("操作"); 
				menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "編輯");
			}  
        });
 
        loadSavedPreferences();
        
        listAdapter = new Island_ListViewAdapter(this, mislandlist, islandNames);
        expListView.setAdapter(listAdapter);
        
        island = new Island();
        Button btnNew = (Button)findViewById(R.id.btnNew);
        btnNew.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) 
			{
				startInputAlert();
			}
        });

	}
	
	
	@Override
	protected void onStop() 
	{
		savePreferences();
		super.onStop();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_bar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) 
		{ 
        case R.id.action_delete:
        	deleteItem();
            return true; 
        case android.R.id.home:
        	finish(); // GG
            return true; 
        default:
          return super.onOptionsItemSelected(item);
        } 
	}

	
	@Override
    public boolean onContextItemSelected(MenuItem item){
    	
    	switch (item.getItemId()) {
    	case Menu.FIRST:
    		//start_editting();
    		Toast.makeText(MainActivity.this, "長按可編輯", Toast.LENGTH_SHORT).show(); // edit
    		break;
    	}
    	
    	// update listview
    	
    	return super.onContextItemSelected(item);
    }
	
	public void start_editting() 
	{
		// et.setText() current item's name item_position
		island = (Island)listAdapter.getItem(item_position);
		et.setText(island.getName());
		final Calendar c = Calendar.getInstance();
		dpd = new DatePickerDialog(MainActivity.this, dateListener,
				c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		dpd.show();
	}
	
	
	boolean[] indexToDel;
	public void deleteItem() 
	{
		indexToDel = new boolean[islandNames.size()];
		OnClickListener btnOnClick = new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				for (int i = 0; i < indexToDel.length; i++)
					if (indexToDel[i])
					{
						islandNames.remove(i);
						mislandlist.remove(i);
					}
				
				listAdapter = new Island_ListViewAdapter(ctx, mislandlist, islandNames);
		        expListView.setAdapter(listAdapter);
		        
		        Toast.makeText(ctx, "刪除成功", Toast.LENGTH_SHORT).show();
			}};
			
		OnMultiChoiceClickListener choiceOnClick = new OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				indexToDel[which] = isChecked;
			}};
		
		
		new AlertDialog.Builder(this)
			.setTitle("請選擇")
			.setMultiChoiceItems(islandNames.toArray(new String[islandNames.size()]), null, choiceOnClick)
			.setPositiveButton("刪除", btnOnClick)
			.setNegativeButton("取消", null).show();
	}
	
	
	/** Item setting */
	public void img_setting() 
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT); 
		startActivityForResult(intent, 1);
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	Uri uri = data.getData();
    	MainActivity.imgUriStr = uri.toString(); // GG
		ContentResolver cr = this.getContentResolver();
		
    		if (resultCode == RESULT_OK) 
    		{
    			try 
    			{
    				Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
    			
    				ImageView imageView = (ImageView) findViewById(R.id.ivImg);
    				imageView.setImageBitmap(bitmap);
    			} 
    			catch (FileNotFoundException e) 
    			{
    				Log.e("Exception", e.getMessage(),e);
    			}
    		}
      
    		super.onActivityResult(requestCode, resultCode, data);
    }
    
    
	public void startInputAlert() 
	{
		OnClickListener btnOnClick = new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				if (which == DialogInterface.BUTTON_POSITIVE) 
				{
					final Calendar c = Calendar.getInstance();
					dpd = new DatePickerDialog(MainActivity.this, dateListener,
							c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
					dpd.show();
				}
			}
		};
		
		et = new EditText(ctx);
		et.setHint("鬼島名稱");
		AlertDialog prompt = new AlertDialog.Builder(ctx)
			.setTitle("請輸入")
			.setView(et)
			.setPositiveButton("確定", btnOnClick)
			.setNegativeButton("取消", btnOnClick).create();
	
		prompt.show();
	}
	
	private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() { 
		public void onDateSet(DatePicker picker, int year, int month, int day) 
		{ 
			if (picker.isShown())
			{
				String name = et.getText().toString().replaceAll("\\s", ""); // for island string form
				img_setting();
				
				MainActivity.updateLV(name, year, month, day, imgUriStr);
			}
		} 
	}; 
	
	
	protected static void updateLV(String name, int year, int month,
			int day, String imgUriStr) 
	{
		island = new Island(name, imgUriStr, year, month, day);
		islandNames.add(name);
		mislandlist.add(island);
		listAdapter = new Island_ListViewAdapter(ctx, mislandlist, islandNames);
		expListView.setAdapter(listAdapter);
	}
	
	
	public static ArrayList<String> getIslandNames() 
	{
		return islandNames;
	}
	
	/** SharedPreferences for record */
	private final String mislandlist_key = "MISLANDLIST";
	private Set<String> mislandlist_value = new HashSet<String>();
	
	
	private void loadSavedPreferences(Set<String> value) 
	{
		ArrayList<String> dataRows = new ArrayList<String>(value);
		String[] dataRow;
		
		for (int i = 0; i < dataRows.size(); i++)
		{
			dataRow = dataRows.get(i).split(" ");
	    	
	    	islandNames.add(dataRow[0]);
	    	
	    	island = new Island();
	    	island.setName(dataRow[0]);
	    	island.setDeadline(dataRow[1] + " " + dataRow[2]);
	    	island.setImgUriStr(dataRow[3]);
	    	island.setGrade(Integer.parseInt(dataRow[4]));
	    	mislandlist.add(island);
		}
	}
	

	// load
	@SuppressLint("NewApi")
	private void loadSavedPreferences()
	{
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        Set<String> mislandlist_value_default = new HashSet<String>();
        
        Uri uri = Uri.parse("android.resource://com.example.listviewinputinterface/drawable/ghost");
        imgUriStr = uri.toString();
        mislandlist_value_default.add(new String("Java作業 周一 12/15 " + imgUriStr + " 0"));
        // name + " " + deadline + " " + imgUriStr + " " + grade;
        Set<String> mislandlist_value = sharedPreferences.getStringSet(mislandlist_key, mislandlist_value_default);
        
        loadSavedPreferences(mislandlist_value);
    }

    @SuppressLint("NewApi")
	private void savePreferences(String key, Set<String> value) 
    {
    	SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }
    
    // save
    private void savePreferences()
    {
		for (Island island : mislandlist)
			mislandlist_value.add(island.toString());
		
		savePreferences(mislandlist_key, mislandlist_value);
    }
}
