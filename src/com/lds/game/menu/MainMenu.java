package com.lds.game.menu;

import com.lds.game.R;
import com.lds.game.Run;
import com.lds.game.R.id;
import com.lds.game.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button b = (Button)findViewById(R.id.Button1);
		b.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) 
			{
				Intent i = new Intent(MainMenu.this, Run.class);
				startActivity(i);
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
