package com.lds.game.menu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.lds.game.GameRenderer;
import com.lds.game.R;
import com.lds.game.Run;
import com.lds.game.SoundPlayer;
import com.lds.game.entity.Player;
import com.lds.trigger.EffectEndGame;

public class MainMenu extends Activity
{	
	public boolean vibrateSettingMain = true, test = true;
	public Context context;
	public SeekBar mSeekBar;
	private ViewAnimator animator;
	private ProgressDialog pd;
	GridView levelList;
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		//set up ListView
		String[] items = getResources().getStringArray(R.array.menu_items);
		final ListView list = (ListView)findViewById(R.id.MM_RightListView);
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
		adapter.setNotifyOnChange(true);
		list.setAdapter(adapter);
		
		//set up Level ListView
		levelList = (GridView)View.inflate(this, R.layout.level_grid, null);
		levelList.setAdapter(new ButtonAdapter(this));
		
		//set up ViewAnimator with animations
		animator = (ViewAnimator)findViewById(R.id.MM_LeftViewAnimator);
		final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		animator.setInAnimation(fadeIn);
		final Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
		animator.setOutAnimation(fadeOut);
		animator.setAnimateFirstView(true);
		
		//add views to ViewAnimator
		animator.addView(levelList, 0);
		final View tutorial = View.inflate(this, R.layout.tutorial, null);
		animator.addView(tutorial, 1);
		final View settings = View.inflate(this, R.layout.settings, null);
		animator.addView(settings, 2);
		final View aboutYTF = View.inflate(this, R.layout.about_ytf, null);
		animator.addView(aboutYTF, 3);
		final View aboutLDS = View.inflate(this, R.layout.about_lds, null);
		animator.addView(aboutLDS, 4);
		final View credits = View.inflate(this, R.layout.credits, null);
		animator.addView(credits, 5);
		final View ccLogo = View.inflate(this, R.layout.circuit_crawler_logo, null);
		animator.addView(ccLogo, 6);
		animator.setDisplayedChild(6);
		
		//Boxes n' Shit
		final CheckBox vibrationCheckbox = (CheckBox) findViewById(R.id.checkbox);
		final CheckBox volumeCheckbox = (CheckBox) findViewById(R.id.volumeCheckbox);
		final CheckBox enableMusic = (CheckBox) findViewById(R.id.EnableMusic);
		final SeekBar volumeControl = (SeekBar)findViewById(R.id.volume);
		final SeekBar musicVolumeControl = (SeekBar)findViewById(R.id.volume);
		final TextView musicVolumeSeekBarText = (TextView)findViewById(R.id.volumeText);
		final Button ldsButton = (Button)findViewById(R.id.LDS_Button);
		final Button ytfButton = (Button)findViewById(R.id.YTF_Button);
		final Button cheatButton = (Button)findViewById(R.id.Cheats);
		final TextView seekBarValue = (TextView)findViewById(R.id.volumeText);
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final EditText input = new EditText(this);
		final CheckBox godMode = (CheckBox) findViewById(R.id.god);
		final CheckBox noclip = (CheckBox) findViewById(R.id.noclip);
		final TextView cheatText = (TextView)findViewById(R.id.cheatText);
		final SeekBar effectVolumeControl = (SeekBar)findViewById(R.id.effectVolume);
		final TextView effectVolumeSeekBarText = (TextView)findViewById(R.id.effectVolumeText);
		//final CheckBox enableShaders = (CheckBox) findViewById(R.id.enableShaders);
		//final SeekBar mSeekBar = (SeekBar)findViewById(R.id.seek);
		
		
		//Internal Storage Data stuff
		try
		{
			FileInputStream fis = openFileInput("effect_volume");
			byte[] buffer = new byte[4];
			fis.read(buffer, 0, 4);
			SoundPlayer.effectVolume = StorageHelper.byteArrayToFloat(buffer);
			fis.close();
		}
		catch (FileNotFoundException e) { SoundPlayer.effectVolume = 0.5f;  } 
		catch (IOException e) { e.printStackTrace(); }
		catch (ArrayIndexOutOfBoundsException e) { e.printStackTrace(); }
		
		try
		{
			FileInputStream fis = openFileInput("music_volume");
			byte[] buffer = new byte[4];
			fis.read(buffer, 0, 4);
			SoundPlayer.musicVolume = StorageHelper.byteArrayToFloat(buffer);
			fis.close();
		}
		catch (FileNotFoundException e) { SoundPlayer.musicVolume = 0.5f; } 
		catch (IOException e) { e.printStackTrace(); }
		catch (ArrayIndexOutOfBoundsException e) { e.printStackTrace(); }
		
		try
		{
			FileInputStream fis = openFileInput("vibration_enabled");
			byte[] buffer = new byte[1];
			fis.read(buffer, 0, 1);
			GameRenderer.vibrateSetting = (buffer[0] != 0);
			fis.close();
		}
		catch (FileNotFoundException e) { e.printStackTrace(); } 
		catch (IOException e) { e.printStackTrace(); }
		catch (ArrayIndexOutOfBoundsException e) { e.printStackTrace(); }
		
		try
		{
			FileInputStream fis = openFileInput("music_enabled");
			byte[] buffer = new byte[1];
			fis.read(buffer, 0, 1);
			SoundPlayer.enableMusic = (buffer[0] != 0);
			fis.close();
		}
		catch (FileNotFoundException e) { e.printStackTrace(); } 
		catch (IOException e) { e.printStackTrace(); }
		catch (ArrayIndexOutOfBoundsException e) { e.printStackTrace(); }
		
		try
		{
			FileInputStream fis = openFileInput("sound_enabled");
			byte[] buffer = new byte[1];
			fis.read(buffer, 0, 1);
			SoundPlayer.enableSound = (buffer[0] != 0);
			fis.close();
		}
		catch (FileNotFoundException e) { e.printStackTrace(); } 
		catch (IOException e) { e.printStackTrace(); }
		catch (ArrayIndexOutOfBoundsException e) { e.printStackTrace(); }
		
		try
		{
			FileInputStream fis = openFileInput("unlocked_level");
			byte[] buffer = new byte[4];
			fis.read(buffer, 0, 4);
			Run.unlockedLevel = (int)StorageHelper.byteArrayToFloat(buffer);
			fis.close();
		}
		catch (FileNotFoundException e) { SoundPlayer.effectVolume = 0.5f;  } 
		catch (IOException e) { e.printStackTrace(); }
		catch (ArrayIndexOutOfBoundsException e) { e.printStackTrace(); }
		
		pd = ProgressDialog.show(this,"Loading","...Please wait.",true, false);
		pd.hide();
		
		//Defaults, settings, and other stuff
		musicVolumeControl.setMax(100);
		final int volume = (int)(SoundPlayer.musicVolume * 100);
		musicVolumeControl.setProgress(volume);
		musicVolumeSeekBarText.setText("Music Volume: " + String.valueOf(volume) + "%");
		final int effectVolume = (int)(SoundPlayer.effectVolume * 100);
		effectVolumeControl.setProgress(effectVolume);
		effectVolumeSeekBarText.setText("Sound Effect Volume: " + String.valueOf(effectVolume) + "%");
		vibrationCheckbox.setChecked(GameRenderer.vibrateSetting);
		volumeCheckbox.setChecked(SoundPlayer.enableSound);
		enableMusic.setChecked(SoundPlayer.enableMusic);
		godMode.setVisibility(View.INVISIBLE);
		noclip.setVisibility(View.INVISIBLE);
		//mSeekBar.setMax(100);
		//mSeekBar.setProgress(1);

		
		/*******************
		 *  Action Classes *
		 *******************/
		
		
		/******************************************************************************************************************************/
		
		//Anti Aliasing
       /* mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch)	
        	{	
        		antiTextbar.setText("Anti Aliasing: " + String.valueOf(progress) + "%");
        		System.out.println("TROLOLOLOLOLOLOLOLOLOLOL");
        	}
            public void onStartTrackingTouch(SeekBar seekBar)	{	}
            public void onStopTrackingTouch(SeekBar seekBar)	{	}	
        });*/
		/******************************************************************************************************************************/
		
		//Cheat Button
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
						if(value.toString().compareTo("PASSW0rd;") == 0) // DONT LOOK AT THIS! //lol UMADBRO? - Devin
						{
							godMode.setVisibility(View.VISIBLE);
							noclip.setVisibility(View.VISIBLE);
							cheatText.setVisibility(View.VISIBLE);
							cheatText.setText("Correct!");
						}
						else if(EffectEndGame.cheatsUnlocked)
						{
							godMode.setVisibility(View.VISIBLE);
							noclip.setVisibility(View.VISIBLE);
							cheatText.setVisibility(View.VISIBLE);
							cheatText.setText("Unlocked!");
						}
						else
						{
							cheatText.setVisibility(View.VISIBLE);
							cheatText.setText("Wrong!");
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
        /******************************************************************************************************************************/
        
        //Music Volume Slider
        musicVolumeControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
        	public void onProgressChanged(SeekBar volumeControl, int progress, boolean fromTouch)	
        	{	
        		musicVolumeSeekBarText.setText("Music Volume: " + String.valueOf(progress) + "%");
        		final float newVol = ((float)(Integer.parseInt(String.valueOf(progress))))/100;
        		SoundPlayer.musicVolume = ((float)(Integer.parseInt(String.valueOf(progress))))/100;
        		try 
        		{
        			FileOutputStream fos = openFileOutput("music_volume", MODE_PRIVATE);
        			fos.write(StorageHelper.floatToByteArray(newVol));
        			fos.close();
        		} 
        		catch (FileNotFoundException e) { e.printStackTrace(); } 
        		catch (IOException e) { e.printStackTrace(); }
        	}
            public void onStartTrackingTouch(SeekBar volumeControl)	{	}
            public void onStopTrackingTouch(SeekBar volumeControl)	{	}	
        });
        /******************************************************************************************************************************/
        
        //Music Volume Slider
        effectVolumeControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
        	public void onProgressChanged(SeekBar volumeControl, int progress, boolean fromTouch)	
        	{	
        		effectVolumeSeekBarText.setText("Sound Effect Volume: " + String.valueOf(progress) + "%");
        		final float newVol = ((float)(Integer.parseInt(String.valueOf(progress))))/100;
        		SoundPlayer.effectVolume = ((float)(Integer.parseInt(String.valueOf(progress))))/100;
        		try 
        		{
        			FileOutputStream fos = openFileOutput("effect_volume", MODE_PRIVATE);
        			fos.write(StorageHelper.floatToByteArray(newVol));
        			fos.close();
        		} 
        		catch (FileNotFoundException e) { e.printStackTrace(); } 
        		catch (IOException e) { e.printStackTrace(); }
        	}
            public void onStartTrackingTouch(SeekBar volumeControl)	{	}
            public void onStopTrackingTouch(SeekBar volumeControl)	{	}	
        });
        /******************************************************************************************************************************/
        
        //Vibration CheckBox
		vibrationCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		    	GameRenderer.vibrateSetting = isChecked;
		    	byte[] value = new byte[1];
		    	if (isChecked) value[0] = 1;
		    	else value[0] = 0;
		    	try 
        		{
        			FileOutputStream fos = openFileOutput("vibration_enabled", MODE_PRIVATE);
        			fos.write(value);
        			fos.close();
        		} 
        		catch (FileNotFoundException e) { e.printStackTrace(); } 
        		catch (IOException e) { e.printStackTrace(); }
		    }
		});
		/******************************************************************************************************************************/
		
		//Noclip CheckBox
		noclip.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	Player.noclip = true;
		        }
		        else
		        {
		        	Player.noclip = false;
		        }
		    }
		});
		/******************************************************************************************************************************/
		
		//GodMode CheckBox
		godMode.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if ( isChecked )
		        {
		        	Player.godMode = true;
		        }
		        else
		        {
		        	Player.godMode = false;
		        }
		    }
		});
		/******************************************************************************************************************************/
		
		//Enable Shaders CheckBox
		/*enableShaders.setOnCheckedChangeListener(new OnCheckedChangeListener()
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
		});*/
		/******************************************************************************************************************************/
		
		//Enable Music CheckBox
		enableMusic.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		    	SoundPlayer.enableMusic = isChecked;
		    	byte[] value = new byte[1];
		    	if (isChecked) value[0] = 1;
		    	else value[0] = 0;
		    	try 
        		{
        			FileOutputStream fos = openFileOutput("music_enabled", MODE_PRIVATE);
        			fos.write(value);
        			fos.close();
        		} 
        		catch (FileNotFoundException e) { e.printStackTrace(); } 
        		catch (IOException e) { e.printStackTrace(); }
		    }
		});
		/******************************************************************************************************************************/
		
		//Enable Sound Effects CheckBox
		volumeCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		    	SoundPlayer.enableSound = isChecked;
		    	byte[] value = new byte[1];
		    	if (isChecked) value[0] = 1;
		    	else value[0] = 0;
		    	try 
        		{
        			FileOutputStream fos = openFileOutput("sound_enabled", MODE_PRIVATE);
        			fos.write(value);
        			fos.close();
        		} 
        		catch (FileNotFoundException e) { e.printStackTrace(); } 
        		catch (IOException e) { e.printStackTrace(); }
		    }
		});
		/******************************************************************************************************************************/  
		
		//LDS Button
		ldsButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://lightningdevelopment.wordpress.com"));
				startActivity(browserIntent);
			}
		});
		/******************************************************************************************************************************/	
		
		//YTF Button
		ytfButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.youthfortechnology.org"));
				startActivity(browserIntent);
			}
		}); 
		/******************************************************************************************************************************/
		
		
		
		//Side Menu
		list.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				
				switch (position)
				{
					case 0:
						//Run Game
						if (animator.getDisplayedChild() != 0)
							animator.setDisplayedChild(0);
						break;
					case 1:
						//Run Tutorial Level
						if (animator.getDisplayedChild() != 1)
							animator.setDisplayedChild(1);
						break;
					case 2:
						//Settings
						if (animator.getDisplayedChild() != 2)
							animator.setDisplayedChild(2);
						break;
					case 3:
						//Donate Button
						Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.networkforgood.org/donation/ExpressDonation.aspx?ORGID2=912125886"));
						startActivity(browserIntent);
						break;
					case 4:
						//About YTF
						if (animator.getDisplayedChild() != 3)
							animator.setDisplayedChild(3);
						break;
					case 5:
						//About LDS
						if (animator.getDisplayedChild() != 4)
							animator.setDisplayedChild(4);
						break;
					case 6:
						//Credits
						if (animator.getDisplayedChild() != 5)
							animator.setDisplayedChild(5);
						break;
					case 7:
						//Quit
						moveTaskToBack(true);
						finish();
						System.exit(0);
						android.os.Process.killProcess(android.os.Process.myPid());
				}
			}	
		});
		
		//Level Grid
		levelList.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				runGame(position);
			}
		});
	}
	
	public void restart()
	{
		levelList.setAdapter(new ButtonAdapter(this));
	}
	
	@Override
	public void onBackPressed()
	{
		if (animator.getDisplayedChild() != 6)
			animator.setDisplayedChild(6);
		else
			super.onBackPressed();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		restart();
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
	
	public void runGame(int levelIndex)
	{
		Run.levelIndex = levelIndex;
		Intent i = new Intent(MainMenu.this, Run.class);
		i.putExtra("levelID", levelIndex);
		startActivityForResult(i, 1);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == 2)
		{
			try 
    		{
    			FileOutputStream fos = openFileOutput("unlocked_level", MODE_PRIVATE);
    			fos.write(StorageHelper.floatToByteArray((float)Run.unlockedLevel + 1));
    			fos.close();
    		} 
    		catch (FileNotFoundException e) { e.printStackTrace(); } 
    		catch (IOException e) { e.printStackTrace(); }
    		Run.unlockedLevel++;
		}
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
