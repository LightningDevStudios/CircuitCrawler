package com.lds.game.ai;

import java.util.ArrayList;
import com.lds.Vector2f;

public class Node 
{
	private Vector2f posVec;
	private ArrayList<NodeLink> linkList;

	public Node(float xPos, float yPos)
	{
		posVec = new Vector2f(xPos, yPos);
		linkList = new ArrayList<NodeLink>();
	}
	
	public void addLinkedNode(Node node)
	{
		linkList.add(new NodeLink(this, node));
		node.linkList.add(new NodeLink(node, this));
	}
	
	public Node getLinkedNode(int index)
	{
		return linkList.get(index).getLinkedNode();
	}
	
	public Vector2f getLinkVector(int index)
	{
		return linkList.get(index).getNodeVec();
	}
	
	public NodeLink getNodeLink(int index)
	{
		return linkList.get(index);
	}
	
	public ArrayList<Node> getLinkedNodes()
	{
		ArrayList<Node> nodeList = new ArrayList<Node>();
		for (NodeLink link : linkList)
		{
			nodeList.add(link.getLinkedNode());
		}
		return nodeList;
	}
	
	public ArrayList<Vector2f> getLinkedVecs()
	{
		ArrayList<Vector2f> vecList = new ArrayList<Vector2f>();
		for (NodeLink link : linkList)
		{
			vecList.add(link.getNodeVec());
		}
		return vecList;
	}
	
	public ArrayList<NodeLink> getLinkList()
	{
		return linkList;
	}
	
	public Vector2f getPos()
	{
		return posVec;
	}
	
	public float getXPos()
	{
		return posVec.getX();
	}
	
	public float getYPos()
	{
		return posVec.getY();
	}
}
