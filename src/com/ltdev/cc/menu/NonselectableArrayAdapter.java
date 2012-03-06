package com.ltdev.cc.menu;

import android.content.Context;
import android.widget.ArrayAdapter;

public class NonselectableArrayAdapter<T> extends ArrayAdapter<T>
{
	public NonselectableArrayAdapter(Context context, int textViewResourceId, T[] objects)
	{
		super(context, textViewResourceId, objects);
	}
	
	@Override
	public boolean isEnabled(int position)
	{
		return false;
	}
}
