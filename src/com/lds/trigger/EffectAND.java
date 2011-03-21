package com.lds.trigger;

public class EffectAND extends Effect
{
	private Effect effect1, effect2;
	
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
