package com.lds.game.menu;

import com.lds.game.R;
import com.lds.game.Run;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class MainMenu extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		//set up ListView
		String[] items = getResources().getStringArray(R.array.menu_items);
		ListView list = (ListView)findViewById(R.id.MM_RightListView);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
		adapter.setNotifyOnChange(true);
		list.setAdapter(adapter);
		
		//set up ViewAnimator
		final ViewAnimator animator = (ViewAnimator)findViewById(R.id.MM_LeftViewAnimator);
		View ccLogo = View.inflate(this, R.layout.circuit_crawler_logo, null);
		animator.addView(ccLogo, 0);
		View aboutScreen = View.inflate(this, R.layout.about, null);
		animator.addView(aboutScreen, 1);
		
		list.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (position == 0)
				{
					//Run Game
					Intent i = new Intent(MainMenu.this, Run.class);
					startActivity(i);
				}
				else if (position == 1)
				{
					//Run Tutorial Level
					//for now, just takes us back to logo
					animator.setDisplayedChild(0);
				}
				else if (position == 2)
				{
					//Show About Screen
					animator.setDisplayedChild(1);
				}
				else if (position == 3)
				{
					//Show YTF Screen
				}
				else if (position == 4)
				{
					//Donate Button
				}
				else
				{
					//Settings Menu
				}
			}	
		});
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
