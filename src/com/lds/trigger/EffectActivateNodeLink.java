package com.lds.trigger;

import com.lds.game.ai.NodeLink;

public class EffectActivateNodeLink extends Effect
{
	private NodeLink link;
	
	public EffectActivateNodeLink (NodeLink link)
	{
		this.link = link;
	}
	
	@Override
	public void fireOutput()
	{
		link.activate();
	}
	
	@Override
	public void unfireOutput()
	{
		link.deactivate();
	}
}
