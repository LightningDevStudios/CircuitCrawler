package com.lds.game.menu;

import com.lds.game.R;
import com.lds.game.Run;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
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
		
		//set up ViewAnimator with animations
		final ViewAnimator animator = (ViewAnimator)findViewById(R.id.MM_LeftViewAnimator);
		final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		animator.setInAnimation(fadeIn);
		final Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		animator.setOutAnimation(fadeOut);
		animator.setAnimateFirstView(true);
		//add views to ViewAnimator
		final View ccLogo = View.inflate(this, R.layout.circuit_crawler_logo, null);
		animator.addView(ccLogo, 0);
		final View aboutYTF = View.inflate(this, R.layout.about_ytf, null);
		animator.addView(aboutYTF, 1);
		final View aboutLDS = View.inflate(this, R.layout.about_lds, null);
		animator.addView(aboutLDS, 2);
		final View credits = View.inflate(this, R.layout.credits, null);
		animator.addView(credits, 3);
		
		//LDS Button
		final Button ldsButton = (Button)findViewById(R.id.LDS_Button);
		ldsButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://lightningdevelopment.wordpress.com"));
				startActivity(browserIntent);
			}
		});
		
		//YTF Button
		final Button ytfButton = (Button)findViewById(R.id.YTF_Button);
		ytfButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.youthfortechnology.org"));
				startActivity(browserIntent);
			}
		});
		list.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				switch (position)
				{
					case 0:
						//Run Game
						Intent i = new Intent(MainMenu.this, Run.class);
						startActivity(i);
						break;
					case 1:
						//Run Tutorial Level
						//for now, just takes us back to logo
						animator.setDisplayedChild(0);
						break;
					case 2:
						//Settings
						break;
					case 3:
						//Donate Button
						Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.networkforgood.org/donation/ExpressDonation.aspx?ORGID2=912125886"));
						startActivity(browserIntent);
						break;
					case 4:
						//About YTF
						animator.setDisplayedChild(1);
						break;
					case 5:
						//About LDS
						animator.setDisplayedChild(2);
						break;
					case 6:
						//Credits
						animator.setDisplayedChild(3);
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
