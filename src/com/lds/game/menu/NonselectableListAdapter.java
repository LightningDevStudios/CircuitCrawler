package com.lds.game.menu;

import android.content.Context;
import android.widget.ArrayAdapter;

public class NonselectableListAdapter<T> extends ArrayAdapter<T>
{
	public NonselectableListAdapter(Context context, int textViewResourceId, T[] objects)
	{
		super(context, textViewResourceId, objects);
	}
	
	@Override
	public boolean isEnabled(int position)
	{
		return false;
	}
}
