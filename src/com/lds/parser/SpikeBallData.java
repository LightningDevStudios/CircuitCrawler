package com.lds.parser;

import com.lds.game.entity.Entity;
import com.lds.game.entity.SpikeBall;

import java.util.ArrayList;
import java.util.HashMap;

public class SpikeBallData extends PhysEntData
{
	protected float moveX, moveY, rotateDir;
	private SpikeBall spikeBallRef;
	
	public SpikeBallData(HashMap<String, String> sbdHM)
	{
		super(sbdHM);
		
		if (sbdHM.get("moveY") != null)
			moveY = Float.parseFloat(sbdHM.get("moveY"));
		if (sbdHM.get("moveX") != null)
			moveX = Float.parseFloat(sbdHM.get("moveY"));
		if (sbdHM.get("rotateDir") != null)
			rotateDir = Float.parseFloat(sbdHM.get("rotateDir"));
	}
	
	public  void createInst(ArrayList<Entity> entData)
	{
		spikeBallRef = new SpikeBall(size, xPos, yPos, circular, willCollide, moveSpeed, rotSpeed, sclSpeed, friction, moveX, moveY, rotateDir);
		
		//COLOR
		if (color != null)
			spikeBallRef.enableColorMode(color[0], color[1], color[2], color[3]);
		
		//GRADIENT
		if (gradient != null)
			spikeBallRef.enableGradientMode(gradient);
		
		//TEXTURE
		if (textureModeEnabled)
			spikeBallRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			spikeBallRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(spikeBallRef);
		ent = spikeBallRef;
	}
}
