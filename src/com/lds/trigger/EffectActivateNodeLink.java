package com.lds.trigger;

import com.lds.game.ai.Node;

public class EffectActivateNodeLink extends Effect
{
	private Node node1, node2;
	
	public EffectActivateNodeLink (Node node1, Node node2)
	{
		this.node1 = node1;
		this.node2 = node2;
	}
	
	@Override
	public void fireOutput()
	{
		node1.activateLinks(node2);
	}
	
	@Override
	public void unfireOutput()
	{
		node1.deactivateLinks(node2);
	}
}
