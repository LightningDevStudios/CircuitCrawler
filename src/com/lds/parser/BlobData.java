package com.lds.parser;

import java.util.ArrayList;
import java.util.HashMap;

import com.lds.game.entity.Door;
import com.lds.game.entity.Entity;

import com.lds.game.entity.Blob;

public class BlobData extends EnemyData
{
	private Blob blobRef;
	public BlobData(HashMap<String, String> blobHM)
	{
		super(blobHM);
	}
	
	public  void createInst(ArrayList<Entity> entData)
	{
		blobRef = new Blob(xPos, yPos, type, true); //TODO allow for active boolean
		blobRef.setAngle(angle);
		
		//COLOR
		if (color != null)
			blobRef.enableColorMode(color[0],color[1],color[2],color[3]);
		
		//GRADIENT
		if (gradient != null)
			blobRef.enableGradientMode(gradient);
		
		//TEXTURE
		if (textureModeEnabled)
			blobRef.enableTextureMode(tex, texture);
		
		//TILESET
		if (tilesetModeEnabled)
			blobRef.enableTilesetMode(tex, tileX, tileY);
		
		entData.add(blobRef);
		ent = blobRef;
	}
}
