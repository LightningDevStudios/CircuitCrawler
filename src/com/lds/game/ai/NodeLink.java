package com.lds.game.ai;

import com.lds.Vector2f;

public class NodeLink 
{
	private Node thisNode, linkedNode;
	private Vector2f nodeVec;
	private boolean active;

	public NodeLink(Node thisNode, Node linkedNode, boolean active)
	{
		this.thisNode = thisNode;
		this.linkedNode = linkedNode;
		nodeVec = Vector2f.sub(linkedNode.getPos(), thisNode.getPos());
		this.active = active;
	}
	
	public NodeLink(Node thisNode, Node linkedNode)
	{
		this(thisNode, linkedNode, true);
	}
	
	public Node getThisNode()
	{
		return thisNode;
	}
	
	public Node getLinkedNode()
	{
		return linkedNode;
	}
	
	public Vector2f getNodeVec()
	{
		return nodeVec;
	}
	
	public boolean isActive()
	{
		return active;
	}
}
