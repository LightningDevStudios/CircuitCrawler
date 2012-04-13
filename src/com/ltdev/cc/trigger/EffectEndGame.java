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

package com.ltdev.cc.trigger;

import com.ltdev.cc.event.GameOverListener;
import com.ltdev.trigger.Effect;

/**
 * An Effect that ends the level and returns to the main menu.
 * @author Lightning Development Studios
 */
public class EffectEndGame extends Effect
{
	private GameOverListener listener;
	private boolean winning;
	
	/**
	 * Initializes a new instance of the EffectEndGame class.
	 * @param listener The delegate to call when the effect is fired.
	 * @param winning A value indicating whether or not firing this effect is considered winning the level.
	 */
	public EffectEndGame(GameOverListener listener, boolean winning)
	{
		this.listener = listener;
		this.winning = winning;
	}
	
	/**
	 * Sets the OnGameOverListener delegate.
	 * \todo Remove this method, somehow get OnGameOver to be sent to Game in the constructor.
	 * @param listener The delegate to call when the effect is fired.
	 */
	public void setListener(GameOverListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public void fireOutput()
	{
		if (listener != null)
			listener.onGameOver(winning);
	}
	
	@Override
	public void unfireOutput()
	{
	    
	}
}
