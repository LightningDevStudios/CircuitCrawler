package com.lds.trigger;

import java.util.ArrayList;

public class EffectList extends Effect
{
	private ArrayList<Effect> effectList;
	
	public EffectList(Effect effect)
	{
		effectList = new ArrayList<Effect>();
		effectList.add(effect);
	}

	public EffectList(ArrayList<Effect> effectList)
	{
		this.effectList = effectList;
	}
	
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
