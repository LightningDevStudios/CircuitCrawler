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

/**
 * An Effect that fires two other effects.
 * 
 * It is not recommended to chain EffectANDs together, but rather to use an EffectList.
 * @author Lightning Development Studios
 * @see EffectList
 */
public class EffectAND extends Effect
{
	private Effect effect1, effect2;
	
	/**
	 * Initializes a new instance of the EffectAND class.
	 * @param effect1 The first effect to fire.
	 * @param effect2 The second effect to fire.
	 */
	public EffectAND(Effect effect1, Effect effect2)
	{
		this.effect1 = effect1;
		this.effect2 = effect2;
	}
	
	@Override
	public void fireOutput()
	{
		effect1.fireOutput();
		effect2.fireOutput();
	}
	
	@Override
	public void unfireOutput()
	{
		effect1.unfireOutput();
		effect2.unfireOutput();
	}
}
