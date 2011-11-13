package com.lds.game.entity;

import com.lds.game.Tile;
import com.lds.physics.Shape;

public abstract class HoldObject extends Entity
{
	private boolean held;
	
	public HoldObject(Shape shape)
	{
	    super(shape);
		held = false;
	}
	
	/**
	 * \todo fall down pit.
	 * @param tile The tile to interact with.
	 */
	@Override
	public void tileInteract(Tile tile)
	{
		/*if (!held)
		{
			if (moveInterpVec.length() < 1.0f && moveInterpVec.length() > 0.0f)
			{
				if (tile.isPit())
				{
					this.scaleTo(0, 0);
					if (!falling)
						SoundPlayer.playSound(SoundPlayer.PIT_FALL);
					falling = true;
				}
			}
			else if (!gettingPushed)
				super.onTileInteract(tile);
		}*/
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
	}
}
