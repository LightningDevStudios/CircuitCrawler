package com.ltdev.cc.trigger;

import com.ltdev.cc.ai.Node;
import com.ltdev.trigger.Effect;

/**
 * An Effect that activates a NodeLink when fired and deactivates it when unfired.
 * @author Lightning Development Studios
 * @see EffectDeactivateNodeLink
 */
public class EffectActivateNodeLink extends Effect
{
	private Node node1, node2;
	
	/**
	 * Initializes a new instance of the EffectActivateNodeLink class.
	 * @param node1 The first node in the link.
	 * @param node2 The second node in the link.
	 */
	public EffectActivateNodeLink(Node node1, Node node2)
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
