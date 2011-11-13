package com.lds.trigger;

import com.lds.game.Tile;

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
