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
	
	public NodePath(Node node)
	{
		this();
		add(node);
	}
	
	public NodePath(Node node1, Node node2)
	{
		this();
		add(node1);
		add(node2);
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
	
	public void reverse()
	{
		ArrayList<Node> reversedList = new ArrayList<Node>();
		for (int i = nodeList.size() - 1; i <= 0; i++)
		{
			reversedList.add(nodeList.get(i));
		}
		nodeList = reversedList;
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
