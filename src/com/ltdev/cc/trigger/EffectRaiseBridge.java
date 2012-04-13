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

import com.ltdev.cc.Tile;
import com.ltdev.trigger.Effect;

/**
 * An Effect that raises a bridge when fired and lowers it when unfired.
 * \todo get an Entity to raise that prevents falling into pits
 * @author Lightning Development Studios
 * @deprecated Tileset is now static. Can't set tiles to a different state.
 */
public class EffectRaiseBridge extends Effect
{	
	/**
	 * Initializes a new instance of the EffectRaiseBridge class.
	 * @param t The tile to raise and unraise.
	 */
	public EffectRaiseBridge(Tile t)
	{
	    
	}
	
	@Override
	public void fireOutput()
	{
	    
	}
	
	@Override
	public void unfireOutput()
	{
	    
	}
}
