package com.lds.game.ai;

import java.util.ArrayList;

import com.lds.Vector2f;
import com.lds.game.entity.Entity;

public class NodePath 
{
	private ArrayList<Node> nodeList;
	
	public NodePath()
	{
		this.nodeList = new ArrayList<Node>();
	}
	
	public Node getClosestNode(Entity ent)
	{
		Node closestNode = nodeList.get(0);
		Vector2f distanceVec = Vector2f.sub(nodeList.get(0).getPos(), ent.getPos());
		Vector2f newDistanceVec;
		for (Node node : nodeList)
		{
			newDistanceVec = Vector2f.sub(node.getPos(), ent.getPos());
			if (distanceVec.mag() > newDistanceVec.mag())
				closestNode = node;
		}
		return closestNode;
	}
	
	public int getSize()
	{
		return nodeList.size();
	}
	
	public void add(Node node)
	{
		nodeList.add(node);
	}
	
	public ArrayList<Node> getNodeList()
	{
		return nodeList;
	}
}
