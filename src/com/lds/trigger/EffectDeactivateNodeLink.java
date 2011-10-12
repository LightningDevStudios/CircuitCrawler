package com.lds.trigger;

import com.lds.game.ai.Node;

public class EffectDeactivateNodeLink extends Effect
{
	private Node node1, node2;
	
	public EffectDeactivateNodeLink(Node node1, Node node2)
	{
		this.node1 = node1;
		this.node2 = node2;
	}
	
	@Override
	public void fireOutput()
	{
		node1.deactivateLinks(node2);
	}
	
	@Override
	public void unfireOutput()
	{
		node1.activateLinks(node2);
	}
}