/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev.cc.menu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.ltdev.Vibrator;
import com.ltdev.cc.LevelActivity;
import com.ltdev.cc.R;
import com.ltdev.cc.SoundPlayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * The Main Menu activity.
 * @author Lightning Development Studios
 */
public class MainMenu extends Activity
{	
	private ViewAnimator animator;
	//private ProgressDialog pd;
	private int unlockedLevel;
	private GridView levelList;

    
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
		final View settings = View.inflate(this, R.layout.settings, null);
		animator.addView(settings, 1);
		final View aboutYTF = View.inflate(this, R.layout.about_ytf, null);
		animator.addView(aboutYTF, 2);
		final View aboutLDS = View.inflate(this, R.layout.about_lds, null);
		animator.addView(aboutLDS, 3);
		final View credits = View.inflate(this, R.layout.credits, null);
		animator.addView(credits, 4);
		final View ccLogo = View.inflate(this, R.layout.circuit_crawler_logo, null);
		animator.addView(ccLogo, 5);
		animator.setDisplayedChild(5);
		
		//Boxes n' Shit
		final CheckBox vibrationCheckbox = (CheckBox) findViewById(R.id.checkbox);
		final CheckBox volumeCheckbox = (CheckBox) findViewById(R.id.volumeCheckbox);
		final CheckBox enableMusic = (CheckBox) findViewById(R.id.EnableMusic);
		//final SeekBar volumeControl = (SeekBar)findViewById(R.id.volume);
		final SeekBar musicVolumeControl = (SeekBar)findViewById(R.id.volume);
		final TextView musicVolumeSeekBarText = (TextView)findViewById(R.id.volumeText);
		final Button ldsButton = (Button)findViewById(R.id.LDS_Button);
		final Button ytfButton = (Button)findViewById(R.id.YTF_Button);
		//final Button reset = (Button)findViewById(R.id.reset);
		//final TextView seekBarValue = (TextView)findViewById(R.id.volumeText);
		final SeekBar effectVolumeControl = (SeekBar)findViewById(R.id.effectVolume);
		final TextView effectVolumeSeekBarText = (TextView)findViewById(R.id.effectVolumeText);
		//final TextView ResetText = (TextView)findViewById(R.id.ResetText);
		//final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		//final CheckBox enableShaders = (CheckBox) findViewById(R.id.enableShaders);
		//final SeekBar mSeekBar = (SeekBar)findViewById(R.id.seek);
		
		
		//Internal Storage Data stuff
		try
		{
			FileInputStream fis = openFileInput("effect_volume");
			byte[] buffer = new byte[4];
			fis.read(buffer, 0, 4);
			SoundPlayer.setEffectVolume(StorageHelper.byteArrayToFloat(buffer));
			fis.close();
		}
		catch (FileNotFoundException e)
		{
		    SoundPlayer.setEffectVolume(0.5f);
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
		    e.printStackTrace();
		}
		
		try
		{
			FileInputStream fis = openFileInput("music_volume");
			byte[] buffer = new byte[4];
			fis.read(buffer, 0, 4);
			SoundPlayer.setMusicVolume(StorageHelper.byteArrayToFloat(buffer));
			fis.close();
		}
		catch (FileNotFoundException e)
		{
		    SoundPlayer.setMusicVolume(0.5f);
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
		    e.printStackTrace();
		}
		
		try
		{
			FileInputStream fis = openFileInput("vibration_enabled");
			byte[] buffer = new byte[1];
			fis.read(buffer, 0, 1);
			Vibrator.setEnableState(buffer[0] != 0);
			fis.close();
		}
		catch (FileNotFoundException e)
		{
		    e.printStackTrace();
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
		    e.printStackTrace();
		}
		
		try
		{
			FileInputStream fis = openFileInput("music_enabled");
			byte[] buffer = new byte[1];
			fis.read(buffer, 0, 1);
			SoundPlayer.setMusicEnabled(buffer[0] != 0);
			fis.close();
		}
		catch (FileNotFoundException e)
		{
		    e.printStackTrace();
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
		    e.printStackTrace();
		}
		
		try
		{
			FileInputStream fis = openFileInput("sound_enabled");
			byte[] buffer = new byte[1];
			fis.read(buffer, 0, 1);
			SoundPlayer.setSoundEnabled(buffer[0] != 0);
			fis.close();
		}
		catch (FileNotFoundException e)
		{
		    e.printStackTrace();
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
		    e.printStackTrace();
		}
		
		try
		{
			FileInputStream fis = openFileInput("unlocked_level");
			byte[] buffer = new byte[4];
			fis.read(buffer, 0, 4);
			unlockedLevel = (int)StorageHelper.byteArrayToFloat(buffer);
			fis.close();
		}
		
		catch (FileNotFoundException e)
		{
		    SoundPlayer.setEffectVolume(0.5f);
		}
		
		catch (IOException e)
		{
		    e.printStackTrace();
		}
		
		catch (ArrayIndexOutOfBoundsException e)
		{
		    e.printStackTrace();
		}
		
		//ProgressDialog pd = ProgressDialog.show(this, "Loading", "...Please wait.", true, false);
		//pd.hide();
		
		//Defaults, settings, and other stuff
		musicVolumeControl.setMax(100);
		final int volume = (int)(SoundPlayer.getMusicVolume() * 100);
		musicVolumeControl.setProgress(volume);
		musicVolumeSeekBarText.setText("Music Volume: " + String.valueOf(volume) + "%");
		final int effectVolume = (int)(SoundPlayer.getEffectVolume() * 100);
		effectVolumeControl.setProgress(effectVolume);
		effectVolumeSeekBarText.setText("Sound Effect Volume: " + String.valueOf(effectVolume) + "%");
		vibrationCheckbox.setChecked(Vibrator.getEnableState());
		volumeCheckbox.setChecked(SoundPlayer.getSoundEnabled());
		enableMusic.setChecked(SoundPlayer.getMusicEnabled());
		//godMode.setVisibility(View.INVISIBLE);
		//noclip.setVisibility(View.INVISIBLE);
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
        	}
            public void onStartTrackingTouch(SeekBar seekBar)	{	}
            public void onStopTrackingTouch(SeekBar seekBar)	{	}	
        });*/
		/******************************************************************************************************************************/
		
		//Cheat Button
		/*
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
						if(value.toString().compareTo("PASSW0rd;") == 0) // DONT LOOK AT THIS! //lol UMADBRO? - Devin //IMAD
						{
							godMode.setVisibility(View.VISIBLE);
							noclip.setVisibility(View.VISIBLE);
							cheatText.setVisibility(View.VISIBLE);
							cheatText.setText("Correct!");
						}
						else if(Run.onLastLevel)
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

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								dialog.cancel();
							}
						});
				alert.show();
			}
		});      */
        /******************************************************************************************************************************/
        
        //Music Volume Slider
        musicVolumeControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
        	public void onProgressChanged(SeekBar volumeControl, int progress, boolean fromTouch)	
        	{	
        		musicVolumeSeekBarText.setText("Music Volume: " + progress + "%");
        		final float newVol = ((float)progress) / 100;
        		SoundPlayer.setMusicVolume(((float)progress) / 100);
        		try 
        		{
        			FileOutputStream fos = openFileOutput("music_volume", MODE_PRIVATE);
        			fos.write(StorageHelper.floatToByteArray(newVol));
        			fos.close();
        		} 
        		catch (FileNotFoundException e)
        		{
        		    e.printStackTrace();
        		}
        		catch (IOException e)
        		{
        		    e.printStackTrace();
        		}
        	}
            public void onStartTrackingTouch(SeekBar volumeControl){}
            public void onStopTrackingTouch(SeekBar volumeControl){}
        });
        /******************************************************************************************************************************/
        
        //Music Volume Slider
        effectVolumeControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
        	public void onProgressChanged(SeekBar volumeControl, int progress, boolean fromTouch)	
        	{	
        		effectVolumeSeekBarText.setText("Sound Effect Volume: " + progress + "%");
        		final float newVol = ((float)progress) / 100;
        		SoundPlayer.setEffectVolume(((float)progress) / 100);
        		try 
        		{
        			FileOutputStream fos = openFileOutput("effect_volume", MODE_PRIVATE);
        			fos.write(StorageHelper.floatToByteArray(newVol));
        			fos.close();
        		} 
        		catch (FileNotFoundException e)
        		{
        		    e.printStackTrace();
        		}
        		catch (IOException e)
        		{
        		    e.printStackTrace();
        		}
        	}
            public void onStartTrackingTouch(SeekBar volumeControl){}
            public void onStopTrackingTouch(SeekBar volumeControl){}
        });
        /******************************************************************************************************************************/
        
        //Vibration CheckBox
		vibrationCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		    	Vibrator.setEnableState(isChecked);
		    	byte[] value = new byte[1];
		    	if (isChecked) value[0] = 1;
		    	else value[0] = 0;
		    	try 
        		{
        			FileOutputStream fos = openFileOutput("vibration_enabled", MODE_PRIVATE);
        			fos.write(value);
        			fos.close();
        		} 
        		catch (FileNotFoundException e)
        		{
        		    e.printStackTrace();
        		}
        		catch (IOException e)
        		{
        		    e.printStackTrace();
        		}
		    }
		});
		/******************************************************************************************************************************/
		
		/******************************************************************************************************************************/

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
		    	SoundPlayer.setSoundEnabled(isChecked);
		    	byte[] value = new byte[1];
		    	if (isChecked) value[0] = 1;
		    	else value[0] = 0;
		    	try 
        		{
        			FileOutputStream fos = openFileOutput("music_enabled", MODE_PRIVATE);
        			fos.write(value);
        			fos.close();
        		} 
        		catch (FileNotFoundException e)
        		{
        		    e.printStackTrace();
        		}
        		catch (IOException e)
        		{
        		    e.printStackTrace();
        		}
		    }
		});
		/******************************************************************************************************************************/
		
		//Enable Sound Effects CheckBox
		volumeCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		    	SoundPlayer.setSoundEnabled(isChecked);
		    	byte[] value = new byte[1];
		    	if (isChecked) value[0] = 1;
		    	else value[0] = 0;
		    	try 
        		{
        			FileOutputStream fos = openFileOutput("sound_enabled", MODE_PRIVATE);
        			fos.write(value);
        			fos.close();
        		} 
        		catch (FileNotFoundException e)
        		{
        		    e.printStackTrace();
        		}
        		catch (IOException e)
        		{
        		    e.printStackTrace();
        		}
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
		/*
		//Reset Button
		reset.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) 
			{
				ResetText.setText("Settings Reset");
				File dir = getFilesDir();
				if(dir.isDirectory())
				{
					String[] files = dir.list();
					for(int i = 0; i < files.length; i++)
					{
						String str = files[i];
						File file = new File(dir, str);
						boolean deleted = file.delete();
					}
				}
			}
		});*/
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
					finish();
                default:
                    break;
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
	
	/**
	 * Reloads the level list.
	 */
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
			finish();
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
	
	/**
	 * Starts a new level.
	 * @param levelIndex The level to start playing.
	 * \todo check if the level is unlocked?
	 */
	public void runGame(int levelIndex)
	{
		Intent i = new Intent(MainMenu.this, LevelActivity.class);
		i.putExtra("levelIndex", levelIndex);
		i.putExtra("unlockedLevel", unlockedLevel);
		startActivityForResult(i, 1);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == 2)
		{
			unlockedLevel++;
			try 
    		{
    			FileOutputStream fos = openFileOutput("unlocked_level", MODE_PRIVATE);
    			fos.write(StorageHelper.floatToByteArray((float)unlockedLevel));
    			fos.close();
    		} 
    		catch (FileNotFoundException e)
    		{
    		    e.printStackTrace();
    		}
    		catch (IOException e)
    		{
    		    e.printStackTrace();
    		}
		}
		
		else if (resultCode == 3)
		{
			finish();
		}
		else if (resultCode >= 100)
		{
			runGame(resultCode - 100);
		}
	}
	
	/**
	 * Gets the highest unlocked level.
	 * @return The index of the highest unlocked level.
	 */
	public int getUnlockedLevel()
	{
		return unlockedLevel;
	}
}
