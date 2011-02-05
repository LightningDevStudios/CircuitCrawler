package com.lds.trigger;

import com.lds.game.event.OnGameOverListener;

public class EffectEndGame extends Effect
{
	private OnGameOverListener listener;
	
	public EffectEndGame(OnGameOverListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public void fireOutput()
	{
		listener.onGameOver();
	}
}
