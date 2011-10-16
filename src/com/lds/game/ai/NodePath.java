package com.lds.game.ai;

import com.lds.game.entity.Entity;
import com.lds.math.Vector2;

import java.util.ArrayList;

public class NodePath 
{
	private ArrayList<Node> nodeList;
	private String ID;
	
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
	
	public void activateLink(int index1, int index2)
	{
		nodeList.get(index1).deactivateLinks(nodeList.get(index2));
	}
	
	public void deactivateLink(int index1, int index2)
	{
		nodeList.get(index1).activateLinks(nodeList.get(index2));
	}
	
	public Node getClosestNode(Entity ent)
	{
		Node closestNode = nodeList.get(0);
		Vector2 distanceVec = Vector2.subtract(nodeList.get(0).getPos(), ent.getPos());
		Vector2 newDistanceVec;
		for (Node node : nodeList)
		{
			newDistanceVec = Vector2.subtract(node.getPos(), ent.getPos());
			if (distanceVec.length() > newDistanceVec.length())
				closestNode = node;
		}
		return closestNode;
	}
	
	public void reverse()
	{
		ArrayList<Node> reversedList = new ArrayList<Node>();
		for (int i = nodeList.size() - 1; i >= 0; i--)
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
	
	public Node getNode(int index)
	{
		return nodeList.get(index);
	}
	
	public void setID(String id)
	{
	    ID = id;
	}
	
	public String getID()
	{
	    return ID;
	}
}
