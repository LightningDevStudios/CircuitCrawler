package com.lds.game.entity;

public class Door extends StaticEnt
{
	public Door (float xPos, float yPos)
	{
		 super(72.0f, xPos, yPos, 90.0f, 1.0f, 1.0f, true, false, true);
		 enableColorMode(1.0f, 1.0f, 1.0f, 1.0f);
		 colorInterpSpeed = 1.0f;
	}

	public void open()
	{
		initColorInterp(0.0f, 0.0f, 0.0f, 0.0f);
		isSolid = false;
	}
	
	public void close()
	{	
		initColorInterp(1.0f, 1.0f, 1.0f, 1.0f);
		isSolid = true;
	}
	
	@Override
	public boolean doesCollide (Entity ent)
	{
		return (ent instanceof Tile) ? false : true;
	}
}
