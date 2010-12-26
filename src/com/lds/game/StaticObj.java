package com.lds.game;

public abstract class StaticObj extends Entity //static obejcts are immovable, such as interactive switches and devices and immovable blocks
{
	public StaticObj (float _size, float _xPos, float _yPos, float _angle, float _xScl, float _yScl)
	{
		super(_size, _xPos, _yPos, _angle, _xScl, _yScl, true, true, 0.0f);
	}
}