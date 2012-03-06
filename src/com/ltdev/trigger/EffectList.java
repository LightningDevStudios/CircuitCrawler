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
