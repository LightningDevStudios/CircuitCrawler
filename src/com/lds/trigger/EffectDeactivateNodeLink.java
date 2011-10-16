package com.lds.trigger;

import com.lds.game.ai.Node;

/**
 * An Effect that deactivates a NodeLink when fired and activates it when unfired.
 * @author Lightning Development Studios
 */
public class EffectDeactivateNodeLink extends Effect
{
	private Node node1, node2;
	
	/**
	 * Initializes a new instance of the EffectDeactivateNodeLink class.
	 * @param node1 The first Node in the link.
	 * @param node2 The second Node in the link.
	 */
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
