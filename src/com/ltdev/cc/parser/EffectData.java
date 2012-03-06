package com.ltdev.cc.parser;

import com.ltdev.trigger.Effect;

public class EffectData 
{
	private Effect effect;
	private String id;
	
	public EffectData(Effect effect, String id)
	{
		this.effect = effect;
		this.id = id;
	}
	
	public Effect getEffect()
	{
		return effect;
	}
	
	public String getID()
	{
		return id;
	}
}
