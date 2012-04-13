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

import com.ltdev.cc.entity.Entity;
import com.ltdev.math.Vector2;

import java.util.ArrayList;

public class NodePath 
{
	private ArrayList<Node> nodeList;
	private String id;
	
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
	    this.id = id;
	}
	
	public String getID()
	{
	    return id;
	}
}
