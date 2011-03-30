package com.lds.trigger;

import com.lds.game.event.OnGameOverListener;

public class EffectEndGame extends Effect
{
	private OnGameOverListener listener;
	private boolean winning;
	
	public EffectEndGame(OnGameOverListener listener, boolean winning)
	{
		this.listener = listener;
		this.winning = winning;
	}
	
	@Override
	public void fireOutput()
	{
		listener.onGameOver(winning);
	}
}
