package com.lds.trigger;

import com.lds.game.entity.Tile;

public class EffectRaiseBridge extends Effect
{
	private Tile t;
	
	public EffectRaiseBridge(Tile t)
	{
		this.t = t;
	}
	
	@Override
	public void fireOutput()
	{
		t.setAsBridge();
	}
	
	public void unfireOutput()
	{
		//t.setAsPit();
	}
}
