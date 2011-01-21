package com.lds.UI;

import com.lds.Enums.Direction;
import com.lds.Enums.UIPosition;
import com.lds.game.entity.Player;

public class UIHealthBar extends UIProgressBar
{
	public UIHealthBar(float xSize, float ySize, UIPosition position, Direction dir)
	{
		super(xSize, ySize, position, dir, 100, 0, 100);
	}
	
	public UIHealthBar(float xSize, float ySize, float xRelative, float yRelative, Direction dir)
	{
		super (xSize, ySize, xRelative, yRelative, dir, 100, 0, 100);
	}
	
	public UIHealthBar(float xSize, float ySize, UIPosition position, Direction dir, float topPad, float leftPad, float bottomPad, float rightPad) 
	{
		super(xSize, ySize, position, dir, topPad, leftPad, bottomPad, rightPad, 100, 0, 100);
	}
	
	public UIHealthBar(float xSize, float ySize, float xRelative, float yRelative, Direction dir, float topPad, float leftPad, float bottomPad, float rightPad)
	{
		super(xSize, ySize, xRelative, yRelative, dir, topPad, leftPad, bottomPad, rightPad, 100, 0, 100);
	}
	
	public void updateHealth(Player player)
	{
		setValue(player.getHealth());
	}
}
