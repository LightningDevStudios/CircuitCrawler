package com.lds.trigger;

import com.lds.game.entity.Tile;

/**
 * An Effect that raises a bridge when fired and lowers it when unfired.
 * @author Lightning Development Studios
 */
public class EffectRaiseBridge extends Effect
{
	private Tile t;
	
	/**
	 * Initializes a new instance of the EffectRaiseBridge class.
	 * @param t The tile to raise and unraise.
	 */
	public EffectRaiseBridge(Tile t)
	{
		this.t = t;
	}
	
	@Override
	public void fireOutput()
	{
		t.setAsBridge();
	}
	
	@Override
	public void unfireOutput()
	{
		t.setAsPit();
	}
}
