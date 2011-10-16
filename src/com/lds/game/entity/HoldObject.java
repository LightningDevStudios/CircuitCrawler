package com.lds.game.entity;

import com.lds.game.SoundPlayer;
import com.lds.math.Vector2;

public abstract class HoldObject extends PhysEnt //and object that is held (blocks, balls, etc.)
{
	private boolean held;
	
	public HoldObject(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, float friction)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, true, 10.0f, 90.0f, 2.0f, friction);
		held = false;
	}
	
	@Override
	public void onTileInteract(Tile tile)
	{
		if (!held)
		{
			if (moveInterpVec.length() < 1.0f && moveInterpVec.length() > 0.0f)
			{
				if (tile.isPit())
				{
					this.scaleTo(0, 0);
					if (!falling)
						SoundPlayer.getInstance().playSound(SoundPlayer.PIT_FALL);
					falling = true;
				}
			}
			else if (!gettingPushed)
				super.onTileInteract(tile);
		}
	}
	
	public boolean isHeld()
	{
		return held;
	}
	
	public void hold()
	{
		held = true;
	}
	
	public void drop()
	{
		held = false;
		stop();
	}
	
	public void push()
	{
		held = false;
		push(new Vector2(angle).scale(3));
	}
}
