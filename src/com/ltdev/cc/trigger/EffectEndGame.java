package com.ltdev.cc.trigger;

import com.ltdev.cc.event.GameOverListener;
import com.ltdev.trigger.Effect;

/**
 * An Effect that ends the level and returns to the main menu.
 * @author Lightning Development Studios
 */
public class EffectEndGame extends Effect
{
	private GameOverListener listener;
	private boolean winning;
	
	/**
	 * Initializes a new instance of the EffectEndGame class.
	 * @param listener The delegate to call when the effect is fired.
	 * @param winning A value indicating whether or not firing this effect is considered winning the level.
	 */
	public EffectEndGame(GameOverListener listener, boolean winning)
	{
		this.listener = listener;
		this.winning = winning;
	}
	
	/**
	 * Sets the OnGameOverListener delegate.
	 * \todo Remove this method, somehow get OnGameOver to be sent to Game in the constructor.
	 * @param listener The delegate to call when the effect is fired.
	 */
	public void setListener(GameOverListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public void fireOutput()
	{
		if (listener != null)
			listener.onGameOver(winning);
	}
	
	@Override
	public void unfireOutput()
	{
	    
	}
}
