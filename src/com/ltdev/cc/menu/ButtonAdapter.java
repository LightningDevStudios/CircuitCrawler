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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.ltdev.cc.R;

/**
 * A subclass of BaseAdapter that provides the buttons for the level selection list.
 * @author Lighting Development Studios
 */
public class ButtonAdapter extends BaseAdapter
{
    public static final int NUMBER_OF_LEVELS = 7;
    
	private Context mContext;
	private int[] ints = 
	{ 
	    0, 1, 2, 3, 4, 5, 6, 7, 8, 9
	};
	
	/**
	 * Initializes a new instance of the ButtonAdapter class.
	 * @param mContext An Android context.
	 */
	public ButtonAdapter(Context mContext)
	{
		this.mContext = mContext;
	}
	
	/**
	 * Gets the number of buttons.
	 * @return The number of buttons.
	 */
	public int getCount()
	{
		return ints.length;
	}
	
	/**
	 * Gets the button at location position.
	 * @param position The button the get.
	 * @return The button at location position.
	 * \todo don't return null.
	 */
	public Object getItem(int position)
	{
		return null;
	}
	
	/**
	 * Gets the ID of an item at location position.
	 * @param position The location of the item.
	 * @return An ID.
	 * \todo make it so this doesn't return 0?
	 */
	public long getItemId(int position)
	{
		return 0;
	}
	
	/**
	 * Creates a properly formatted button that launches a Circuit Crawler level.
	 * @param position The level that the button will load.
	 * @param convertView The View that will become the button. Can be null.
	 * @param parent The parent of the view containing the buttons.
	 * @return A button already set up to launch a level.
	 */
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
		if (position <= ((MainMenu)mContext).getUnlockedLevel())
		{
			button.setImageResource(R.raw.unlocked);
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
