package com.lds.trigger;

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
