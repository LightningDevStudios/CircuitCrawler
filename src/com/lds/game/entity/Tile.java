package com.lds.game.entity;

import com.lds.Enums.TileState;
import com.lds.Texture;
import com.lds.TilesetHelper;


public class Tile extends StaticEnt
{
	public static final int TILE_SIZE = 72;
	public static final float TILE_SIZE_F = 72.0f;
	
	private TileState state;
	public int tileX, tileY, tileID;
	
	public Tile(float size, int tilePosX, int tilePosY, int tilesetX, int tilesetY)
	{
		super(size, 0, 0, false, true);
		TilesetHelper.setInitialTileOffset(this, tilePosY, tilePosX, tilesetX, tilesetY);
	}
	
	@Override
	public void updateTileset(Texture tex, int x, int y)
	{
		
		super.updateTileset(tex, x, y);
		tileX = x;
		tileY = y;
	}
	
	@Override
	public void updateTileset(Texture tex, int tileID)
	{
		super.updateTileset(tex, tileID);
		tileX = TilesetHelper.getTilesetX(tileID, tex);
		tileY = TilesetHelper.getTilesetY(tileID, tex);
	}
	
	@Override
	public void updateTileset(int x, int y)
	{
		super.updateTileset(x, y);
		tileX = x;
		tileY = y;
	}
	
	@Override
	public void updateTileset(int tileID)
	{
		super.updateTileset(tileID);
		if (tex != null)
		{
			tileX = TilesetHelper.getTilesetX(tileID, tex);
			tileY = TilesetHelper.getTilesetY(tileID, tex);
		}
	}
	
	@Override
	public boolean isColliding(Entity ent)
	{
		if (state != TileState.WALL)
			return false;
		else
			return super.isColliding(ent);
	}
	
	public void setAsWall()
	{
		state = TileState.WALL;
		updateTileset(2, 0);
		isSolid = true;
	}
	
	public void setAsFloor()
	{
		state = TileState.FLOOR;
		updateTileset(0, 0);
		isSolid = false;
	}
	
	public void setAsPit()
	{
		state = TileState.PIT;
		updateTileset(0, 1);
		isSolid = false;
	}
	
	public void setAsBridge()
	{
		state = TileState.BRIDGE;
		updateTileset(1, 1);
		isSolid = false;
	}
	
	public boolean isWall()
	{
		if (state == TileState.WALL)
			return true;
		return false;
	}
	
	public boolean isFloor()
	{
		if (state == TileState.FLOOR)
			return true;
		return false;
	}
	
	public boolean isPit()
	{
		if (state == TileState.PIT)
			return true;
		return false;
	}
	
	public boolean isBridge()
	{
		if (state == TileState.BRIDGE)
			return true;
		return false;
	}
	
	public TileState getTileState()
	{
		return state;
	}
	
	public void setTexture(Texture tex)
	{
		this.tex = tex;
	}
}
