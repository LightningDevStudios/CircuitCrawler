package com.lds.trigger;

import com.lds.game.ai.NodeLink;

public class EffectDeactivateNodeLink extends Effect
{
	private NodeLink link;
	
	public EffectDeactivateNodeLink (NodeLink link)
	{
		this.link = link;
	}
	
	@Override
	public void fireOutput()
	{
		link.deactivate();
	}
	
	@Override
	public void unfireOutput()
	{
		link.activate();
	}
}
