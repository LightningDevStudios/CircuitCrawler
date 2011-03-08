package com.lds.game.entity;

public class Door extends StaticEnt
{
	public Door (float xPos, float yPos)
	{
		 super(72.0f, xPos, yPos, 0.0f, 1.0f, 0.5f, true, false, true);
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
		if (ent instanceof Tile)
			return false;
		else
			return super.doesCollide(ent);
	}
}
