package com.lds.game.entity;

import com.lds.Enums.RenderMode;
import com.lds.Enums.TileStates;
import com.lds.Texture;
import com.lds.TilesetHelper;


public class Tile extends StaticEnt
{
	public static final int TILE_SIZE = 72;
	public static final float TILE_SIZE_F = 72.0f;
	
	private TileStates state;
	public int tileX, tileY, tileID;
	
	public Tile(float size, int tilePosX, int tilePosY, int tilesetX, int tilesetY)
	{
		super(size, 0, 0, false, RenderMode.TILESET);
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
		if (state != TileStates.WALL)
			return false;
		else
			return super.isColliding(ent);
	}
	
	public void setAsWall()
	{
		state = TileStates.WALL;
		setTilesetMode(tex, 2, 0);
	}
	
	public void setAsFloor()
	{
		state = TileStates.FLOOR;
		setTilesetMode(tex, 0, 0);
	}
	
	public void setAsPit()
	{
		state = TileStates.PIT;
		setTilesetMode(tex, 0, 1);
	}
	
	public void setAsBridge()
	{
		state = TileStates.BRIDGE;
		setTilesetMode(tex, 1, 1);
	}
	
	public boolean isWall()
	{
		if (state == TileStates.WALL)
			return true;
		return false;
	}
	
	public boolean isFloor()
	{
		if (state == TileStates.FLOOR)
			return true;
		return false;
	}
	
	public boolean isPit()
	{
		if (state == TileStates.PIT)
			return true;
		return false;
	}
	
	public boolean isBridge()
	{
		if (state == TileStates.BRIDGE)
			return true;
		return false;
	}
	
	public TileStates getTileState()
	{
		return state;
	}
	
	public void setTexture(Texture tex)
	{
		this.tex = tex;
	}
}
