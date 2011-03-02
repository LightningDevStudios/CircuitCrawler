package com.lds.game.entity;

import javax.microedition.khronos.opengles.GL10;

import com.lds.Enums.RenderMode;
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
		for(int i = 0; i < vertices.length; i++)
		{
			if (i % 2 == 0)
				vertices[i] += posVec.getX();
			else
				vertices[i] += posVec.getY();
		}
		
		vertexBuffer = setBuffer(vertexBuffer, vertices);
	}
	
	@Override
	public void draw(GL10 gl)
	{
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
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
	
	public void rotateTilesetCoords()
	{
		float negX = texture[0];
		float negY = texture[1];
		float posX = texture[2];
		float posY = texture[5];
		
		float[] coords = { 	posX, negY,
							posX, posY,
							negX, negY,
							negX, posY };
		
		this.texture = coords;
		textureBuffer = setBuffer(textureBuffer, texture);
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
		rotateTilesetCoords();
	}
	
	public void setAsFloor()
	{
		state = TileState.FLOOR;
		updateTileset(0, 0);
		isSolid = false;
		rotateTilesetCoords();
	}
	
	public void setAsPit()
	{
		state = TileState.PIT;
		updateTileset(0, 1);
		isSolid = false;
		rotateTilesetCoords();
	}
	
	public void setAsBridge()
	{
		state = TileState.BRIDGE;
		updateTileset(1, 1);
		isSolid = false;
		rotateTilesetCoords();
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
