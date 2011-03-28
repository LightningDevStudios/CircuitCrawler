package com.lds.game.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.lds.game.Game;
import com.lds.game.GameRenderer;
import com.lds.game.R;
import com.lds.game.Run;
import com.lds.game.SoundPlayer;
import com.lds.game.entity.Player;


public class MainMenu extends Activity
{	
	public boolean vibrateSettingMain = true;
	public Context context;
	public SeekBar mSeekBar;
    
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
		final View settings = View.inflate(this, R.layout.settings, null);
		animator.addView(settings, 1);
		final View aboutYTF = View.inflate(this, R.layout.about_ytf, null);
		animator.addView(aboutYTF, 2);
		final View aboutLDS = View.inflate(this, R.layout.about_lds, null);
		animator.addView(aboutLDS, 3);
		final View credits = View.inflate(this, R.layout.credits, null);
		animator.addView(credits, 4);
		
		//Boxes n' Shit
		final CheckBox checkbox = (CheckBox) findViewById(R.id.checkbox);
		final CheckBox volumeCheckbox = (CheckBox) findViewById(R.id.volumeCheckbox);
		final CheckBox enableMusic = (CheckBox) findViewById(R.id.EnableMusic);
		final CheckBox enableShaders = (CheckBox) findViewById(R.id.enableShaders);
		final SeekBar mSeekBar = (SeekBar)findViewById(R.id.seek);
		final SeekBar volumeControl = (SeekBar)findViewById(R.id.volume);
		final Button ldsButton = (Button)findViewById(R.id.LDS_Button);
		final Button ytfButton = (Button)findViewById(R.id.YTF_Button);
		final Button cheatButton = (Button)findViewById(R.id.Cheats);
		final TextView seekBarValue = (TextView)findViewById(R.id.volumeText);
		final TextView antiTextbar = (TextView)findViewById(R.id.antiText);
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		final CheckBox godMode = (CheckBox) findViewById(R.id.god);
		godMode.setVisibility(View.INVISIBLE);
		
		//suffs
		volumeControl.setMax(100);
		volumeControl.setProgress(1);
		mSeekBar.setMax(100);
		mSeekBar.setProgress(1);
		
		//Action Suffs
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)	
        	{	
        		antiTextbar.setText("Anti Aliasing: " + String.valueOf(progress) + "%");
        		System.out.println("TROLOLOLOLOLOLOLOLOLOLOL");
        	}
            public void onStartTrackingTouch(SeekBar seekBar)	{	}
            public void onStopTrackingTouch(SeekBar seekBar)	{	}	
        });
        
        cheatButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				alert.setTitle("Cheat");
				alert.setMessage("Input Code");

				// Set an EditText view to get user input 
				alert.setView(input);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton) 
					{
						Editable value = input.getText();
						if(value.toString().compareTo("PASSW0rd;") == 0)
								{
									godMode.setVisibility(View.VISIBLE);
								}
					}
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
				{
				  public void onClick(DialogInterface dialog, int whichButton) {	}
				});

				alert.show();
			}
		});      
        
        volumeControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
        	public void onProgressChanged(SeekBar volumeControl, int progress, boolean fromTouch)	
        	{	
        		seekBarValue.setText("Volume: " + String.valueOf(progress) + "%");
        		SoundPlayer.effectVolume = ((float)(Integer.parseInt(String.valueOf(progress))))/100;
        		System.out.println(SoundPlayer.effectVolume);
        	}
            public void onStartTrackingTouch(SeekBar volumeControl)	{	}
            public void onStopTrackingTouch(SeekBar volumeControl)	{	}	
        });
       	
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	vibrator(100);
		            GameRenderer.vibrateSetting = true;
		            System.out.println("TRUE IS VIBRATING");
		        }
		        else
		        {
		        	vibrator(100);
		        	GameRenderer.vibrateSetting = false;
		        	System.out.println("FALSE IS VIBRATING");
		        }
		    }
		});
		
		godMode.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	Player.health = 999999;
		        }
		        else
		        {
		        	Player.health = Player.getHealth();
		        }
		    }
		});
		
		enableShaders.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	vibrator(100);
		        }
		        else
		        {
		        	vibrator(100);
		        }
		    }
		});
		
		enableMusic.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	vibrator(100);
		        	SoundPlayer.enableMusic = true;
		        }
		        else
		        {
		        	vibrator(100);
		        	SoundPlayer.enableMusic = false;
		        }
		    }
		});
		
		volumeCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	vibrator(100);
		        	SoundPlayer.enableSound = true;
		        }
		        else
		        {
		        	vibrator(100);
		        	SoundPlayer.enableSound = false;
		        }
		    }
		});
		    
		//LDS Button
		ldsButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://lightningdevelopment.wordpress.com"));
				startActivity(browserIntent);
			}
		});
			
		//YTF Button
		
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
						animator.setDisplayedChild(1);
						break;
					case 3:
						//Donate Button
						Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.networkforgood.org/donation/ExpressDonation.aspx?ORGID2=912125886"));
						startActivity(browserIntent);
						break;
					case 4:
						//About YTF
						animator.setDisplayedChild(2);
						break;
					case 5:
						//About LDS
						animator.setDisplayedChild(3);
						break;
					case 6:
						//Credits
						animator.setDisplayedChild(4);
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
	
	public void vibrator(int time)
	{
			Vibrator vibrator = null; 
			try 
			{ 
				vibrator=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE); 
			} 
			catch (Exception e) {}
			
			if (vibrator != null)
			{ 
			  try 
			  { 
				  vibrator.vibrate(((long)time)); 
			  } 
			  catch (Exception e) {} 
			} 
	}
}
