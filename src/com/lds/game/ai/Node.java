package com.lds.game.ai;

import java.util.ArrayList;
import com.lds.game.ai.NodeLink;
import com.lds.Vector2f;

public class Node 
{
	private Vector2f posVec;
	
	
	public Node(float xPos, float yPos, ArrayList<NodeLink> linkList)
	{
		posVec = new Vector2f(xPos, yPos);
	}
}
