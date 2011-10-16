package com.lds.UI;

import com.lds.Enums.Direction;
import com.lds.Enums.UIPosition;
import com.lds.game.entity.Player;

public class UIHealthBar extends UIProgressBar
{
	private Player player;
	
	public UIHealthBar(float xSize, float ySize, UIPosition position, Direction dir, Player player)
	{
		super(xSize, ySize, position, dir, 100, 0, 100);
		this.player = player;
	}
	
	public UIHealthBar(float xSize, float ySize, float xRelative, float yRelative, Direction dir, Player player)
	{
		super(xSize, ySize, xRelative, yRelative, dir, 100, 0, 100);
		this.player = player;
	}
	
	public UIHealthBar(float xSize, float ySize, UIPosition position, Direction dir, Player player, float topPad, float leftPad, float bottomPad, float rightPad) 
	{
		super(xSize, ySize, position, dir, topPad, leftPad, bottomPad, rightPad, 100, 0, 100);
		this.player = player;
	}
	
	public UIHealthBar(float xSize, float ySize, float xRelative, float yRelative, Direction dir, Player player, float topPad, float leftPad, float bottomPad, float rightPad)
	{
		super(xSize, ySize, xRelative, yRelative, dir, topPad, leftPad, bottomPad, rightPad, 100, 0, 100);
		this.player = player;
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	@Override
	public void update()
	{
		setValue(player.getHealth());
		super.update();
		
	}
}
