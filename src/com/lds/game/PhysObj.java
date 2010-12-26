package com.lds.game;

public abstract class PhysObj extends Entity //physics objects are movable, such as doors, blocks, etc.
{
	public PhysObj (float _size, float _xPos, float _yPos, float _angle, float _xScl, float _yScl, float _speed)
	{
		super(_size, _xPos, _yPos, _angle, _xScl, _yScl, false, true, _speed);
	}
}