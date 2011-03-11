 package com.lds.game.entity;

import com.lds.Animation;

import com.lds.game.ai.*;

public class Sprite extends PhysEnt
{
	public Animation anim;
	public NodePath path;
	public int pathIndex;
	
	public Sprite(float size, float xPos, float yPos)
	{
		this(size, xPos, yPos, 0.0f, 0.0f, 0.0f, null);
		pathIndex = 0;
	}

	public Sprite(float size, float xPos, float yPos, float moveSpeed, float rotSpeed, float sclSpeed, Animation anim)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, moveSpeed, rotSpeed, sclSpeed, anim);
		pathIndex = 0;
	}
	
	public Sprite(float size, float xPos, float yPos, float angle, float xScl, float yScl, float moveSpeed, float rotSpeed, float sclSpeed, Animation anim)
	{
		super(size, xPos, yPos, angle, xScl, yScl, false, false, true, moveSpeed, rotSpeed, sclSpeed, 0.0f);
		pathIndex = 0;
		this.anim = anim;
		//texture = anim.getCurrentFrame();

		//this.textureBuffer = setBuffer(textureBuffer, texture);
	}
	
	public void setNodePath(NodePath path)
	{
		this.path = path;
	}
	
	@Override
	public void update()
	{
		super.update();
		if (pathIndex < path.getSize() && posVec.equals(path.getNodeList().get(pathIndex).getPos()))
		{
			pathIndex++;
			moveTo(path.getNodeList().get(pathIndex).getPos());
		}
		
	}
	
	@Override
	public void renderNextFrame()
	{
		//this.texture = anim.getCurrentFrame();
		//this.textureBuffer = setBuffer(textureBuffer, texture);
	}
}
