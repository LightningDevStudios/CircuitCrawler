 package com.ltdev.cc.ai;

import com.ltdev.math.Vector2;

public class NodeLink 
{
	private Node thisNode, linkedNode;
	private Vector2 nodeVec;
	private boolean active;

	public NodeLink(Node thisNode, Node linkedNode, boolean active)
	{
		this.thisNode = thisNode;
		this.linkedNode = linkedNode;
		nodeVec = Vector2.subtract(linkedNode.getPos(), thisNode.getPos());
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
	
	public Vector2 getNodeVec()
	{
		return nodeVec;
	}
	
	public boolean isActive()
	{
		return active;
	}
	
	public void activate()
	{
		active = true;
	}
	
	public void deactivate()
	{
		active = false;
	}
}
