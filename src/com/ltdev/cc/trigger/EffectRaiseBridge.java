package com.ltdev.cc.trigger;

import com.ltdev.cc.Tile;
import com.ltdev.trigger.Effect;

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
