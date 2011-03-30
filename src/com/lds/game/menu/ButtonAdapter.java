package com.lds.game.menu;

import com.lds.game.R;
import com.lds.game.Run;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;

public class ButtonAdapter extends BaseAdapter
{
	private Context mContext;
	private int[] ints = { 0, 1, 2, 3 };
	public static int numberOfLevels = 4;
	
	public ButtonAdapter(Context mContext)
	{
		this.mContext = mContext;
	}
	
	public int getCount()
	{
		return ints.length;
	}
	
	public Object getItem(int position)
	{
		return null;
	}
	
	public long getItemId(int position)
	{
		return 0;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ImageButton button;
		if (convertView == null)
		{
			button = new ImageButton(mContext);
		}
		else
		{
			button = (ImageButton)convertView;
		}
		if (position <= Run.unlockedLevel)
		{
			//button.setBackgroundColor(Color.GRAY);
			button.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View v)
				{		
					((MainMenu)mContext).runGame(position);
				}
			});
		}
		else
		{
			button.setImageResource(R.raw.lock);
		}
		return button;
	}
}
