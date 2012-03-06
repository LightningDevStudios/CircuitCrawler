package com.ltdev.cc.ui;

public class UIFadeIn extends Control
{
	public UIFadeIn(float xSize, float ySize, UIPosition position)
	{
		super(xSize, ySize, position);
	}
	
	public UIFadeIn(float xSize, float ySize, float xRelative, float yRelative)
	{
		super(xSize, ySize, xRelative, yRelative);
	}
	
	public UIFadeIn(float xSize, float ySize, UIPosition position, float topPad, float leftPad, float bottomPad, float rightPad) 
	{
		super(xSize, ySize, position, topPad, leftPad, bottomPad, rightPad);
	}
	
	public UIFadeIn(float xSize, float ySize, float xRelative, float yRelative, float topPad, float leftPad, float bottomPad, float rightPad)
	{
		super(xSize, ySize, xRelative, yRelative, topPad, leftPad, bottomPad, rightPad);
	}
	
	
}
