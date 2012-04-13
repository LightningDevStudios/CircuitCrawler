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

package com.ltdev.trigger;

import java.util.ArrayList;

/**
 * An Effect that fires a list of Effects when fired and unfires the same list of Effects when unfired.
 * 
 * It is not recommended to use an EffectList for two Effects or fewer. Instead use an EffectAND.
 * @author Lightning Development Studios
 * @see EffectAND
 */
public class EffectList extends Effect
{
	private ArrayList<Effect> effectList;
	
	/**
	 * Initializes a new instance of the EffectList class with only one Effect.
	 * 
	 * More Effects can be added with {@link #addEffect(Effect)}.
	 * @param effect The first effect that gets fired.
	 * @see #addEffect(Effect)
	 */
	public EffectList(Effect effect)
	{
		effectList = new ArrayList<Effect>();
		effectList.add(effect);
	}

	/**
	 * Initializes a new instance of the EffectList class.
	 * @param effectList The list of Effects to use.
	 */
	public EffectList(ArrayList<Effect> effectList)
	{
		this.effectList = effectList;
	}
	
	/**
	 * Adds a new Effect to the list of Effects being used.
	 * @param effect The new Effect to add.
	 */
	public void addEffect(Effect effect)
	{
		effectList.add(effect);
	}
	
	@Override
	public void fireOutput()
	{
		for (Effect effect : effectList)
			effect.fireOutput();
	}
	
	@Override
	public void unfireOutput()
	{
		for (Effect effect : effectList)
			effect.unfireOutput();
	}
}
