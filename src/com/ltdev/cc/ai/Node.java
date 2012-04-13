/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev.cc.ai;

import com.ltdev.math.Vector2;

import java.util.ArrayList;

public class Node 
{
	private Vector2 posVec;
	private ArrayList<NodeLink> linkList;
	private NodeLink parentNodeLink;
	private float f, g, h;

	public Node(float xPos, float yPos)
	{
		posVec = new Vector2(xPos, yPos);
		linkList = new ArrayList<NodeLink>();
		f = 0;
		g = 0;
		h = 0;
	}
	
	public Node(Vector2 posVec)
	{
		this(posVec.x(), posVec.y());
	}
	
	public void setParentNode(Node parent)
	{
		if (parent == null)
			parentNodeLink = null;
		else
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
	
	public void addNodeLink(Node node, boolean active)
	{
		linkList.add(new NodeLink(this, node, active));
		node.linkList.add(new NodeLink(node, this, active));
	}
	
	public void addOneWayNodeLink(Node node)
	{
		linkList.add(new NodeLink(this, node));
	}
	
	public Node getLinkedNode(int index)
	{
		return linkList.get(index).getLinkedNode();
	}
	
	public Vector2 getLinkVector(int index)
	{
		return linkList.get(index).getNodeVec();
	}
	
	public Vector2 getLinkVector(Node node)
	{
		return getNodeLink(node).getNodeVec();
	}
	
	public void clearLinks()
	{
		for (NodeLink nl : linkList)
		{
			nl.getLinkedNode().linkList.remove(nl.getLinkedNode().getLink(this));
		}
		linkList.clear();
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
	
	public ArrayList<Vector2> getLinkedVecs()
	{
		ArrayList<Vector2> vecList = new ArrayList<Vector2>();
		for (NodeLink link : linkList)
		{
			vecList.add(link.getNodeVec());
		}
		return vecList;
	}
	
	public NodeLink getLink(Node node)
	{
		for (NodeLink link : linkList)
		{
			if (link.getLinkedNode() == node)
				return link;
		}
		return null;
	}
	
	public void activateLinks(Node node)
	{
		for (NodeLink nl : linkList)
		{
			if (nl.getLinkedNode() == node)
			{
				nl.activate();
				node.getLink(this).activate();
			}
		}
	}
	
	public void deactivateLinks(Node node)
	{
		for (NodeLink nl : linkList)
		{
			if (nl.getLinkedNode() == node)
			{
				nl.deactivate();
				node.getLink(this).deactivate();
			}
		}
	}
	
	public int getNodeCount()
	{
		return linkList.size();
	}
	
	public ArrayList<NodeLink> getLinkList()
	{
		return linkList;
	}
	
	public Vector2 getPos()
	{
		return posVec;
	}
	
	public float getXPos()
	{
		return posVec.x();
	}
	
	public float getYPos()
	{
		return posVec.y();
	}
	
	public float getF()
	{
		return f;
	}
	
	public void setF(float f)
	{
		this.f = f;
	}
	public float getG()
	{
		return g;
	}
	
	public void setG(float g)
	{
		this.g = g;
	}
	public float getH()
	{
		return h;
	}
	
	public void setH(float h)
	{
		this.h = h;
	}
}
