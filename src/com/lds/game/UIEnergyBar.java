package com.lds.game;

import com.lds.Enums.Direction;
import com.lds.Enums.UIPosition;

public class UIEnergyBar extends UIProgressBar
{
	public UIEnergyBar(float xSize, float ySize, UIPosition position, Direction dir)
	{
		super(xSize, ySize, position, dir, 100, 0, 100);
	}
	
	public UIEnergyBar(float xSize, float ySize, float xRelative, float yRelative, Direction dir)
	{
		super (xSize, ySize, xRelative, yRelative, dir, 100, 0, 100);
	}
	
	public UIEnergyBar(float xSize, float ySize, UIPosition position, Direction dir, float topPad, float leftPad, float bottomPad, float rightPad) 
	{
		super(xSize, ySize, position, dir, topPad, leftPad, bottomPad, rightPad, 100, 0, 100);
	}
	
	public UIEnergyBar(float xSize, float ySize, float xRelative, float yRelative, Direction dir, float topPad, float leftPad, float bottomPad, float rightPad)
	{
		super(xSize, ySize, xRelative, yRelative, dir, topPad, leftPad, bottomPad, rightPad, 100, 0, 100);
	}
	
	public void setEnergy(Player player)
	{
		setValue(player.getEnergy());
	}
}
