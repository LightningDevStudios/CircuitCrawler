package com.lds.parser;

import java.util.ArrayList;
import java.util.HashMap;

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
		blobRef = new Blob(xPos, yPos, type);
		if (rgba != null)
			blobRef.enableColorMode(rgba[0],rgba[1],rgba[2],rgba[3]);
		if (tex != null && xy != null)
			blobRef.enableTilesetMode(tex, xy[1], xy[2]);
		entData.add(blobRef);
	}
}
