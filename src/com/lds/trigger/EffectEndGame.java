package com.lds.trigger;

import com.lds.game.Run;
import com.lds.game.event.OnGameOverListener;
import com.lds.game.menu.ButtonAdapter;

public class EffectEndGame extends Effect
{
	private OnGameOverListener listener;
	private boolean winning;
	public static boolean cheatsUnlocked = false;
	
	public EffectEndGame(OnGameOverListener listener, boolean winning)
	{
		this.listener = listener;
		this.winning = winning;
	}
	
	public void setListener(OnGameOverListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public void fireOutput()
	{
		//Run.unlockedLevel++;
		if(Run.unlockedLevel > ButtonAdapter.numberOfLevels)
		{
			cheatsUnlocked = true;
		}
		listener.onGameOver(true);
	}
}
