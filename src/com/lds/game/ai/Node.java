package com.lds.game.ai;

import java.util.ArrayList;
import com.lds.Vector2f;

public class Node 
{
	private Vector2f posVec;
	private ArrayList<NodeLink> linkList;
	private NodeLink parentNodeLink;
	private float f, g, h;

	public Node(float xPos, float yPos)
	{
		posVec = new Vector2f(xPos, yPos);
		linkList = new ArrayList<NodeLink>();
		f = 0;
		g = 0;
		h = 0;
	}
	
	public Node(Vector2f posVec)
	{
		this(posVec.getX(), posVec.getY());
	}
	
	public void setParentNode(Node parent)
	{
		parentNodeLink = new NodeLink(this, parent);
	}
	
	public Node getParentNode()
	{
		return parentNodeLink.getLinkedNode();
	}
	
	public NodeLink getParentNodeLink()
	{
		return parentNodeLink;
	}
	
	public void addNodeLink(Node node)
	{
		linkList.add(new NodeLink(this, node));
		node.linkList.add(new NodeLink(node, this));
	}
	
	public void addOneWayNodeLink(Node node)
	{
		linkList.add(new NodeLink(this, node));
	}
	
	public Node getLinkedNode(int index)
	{
		return linkList.get(index).getLinkedNode();
	}
	
	public Vector2f getLinkVector(int index)
	{
		return linkList.get(index).getNodeVec();
	}
	
	public Vector2f getLinkVector(Node node)
	{
		return getNodeLink(node).getNodeVec();
	}
	
	public NodeLink getNodeLink(Node node)
	{
		for (NodeLink link : linkList)
		{
			if (link.getLinkedNode() == node)
				return link;
		}
		return null;
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
	
	public int getNodeCount()
	{
		return linkList.size();
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
	
	public float getF ()
	{
		return f;
	}
	
	public void setF (float f)
	{
		this.f = f;
	}
	public float getG ()
	{
		return g;
	}
	
	public void setG (float g)
	{
		this.g = g;
	}
	public float getH ()
	{
		return h;
	}
	
	public void setH (float h)
	{
		this.h = h;
	}
}
