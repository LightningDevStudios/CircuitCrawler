package com.lds.game.entity;

import java.util.EnumSet;

import javax.microedition.khronos.opengles.GL10;

import com.lds.Enums;
import com.lds.Enums.DiagDir;
import com.lds.Enums.Direction;
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
	
	public void updateBordersPit(Tile[][] tileset, int x, int y)
    {
        EnumSet<Direction> dirsCovered = EnumSet.noneOf(Direction.class);

        Tile leftTile = null, rightTile = null, upTile = null, downTile = null;

        if (x > 0)
            leftTile = tileset[y][x - 1];
        if (x < tileset[0].length - 1)
            rightTile = tileset[y][x + 1];
        if (y > 0)
            upTile = tileset[y - 1][x];
        if (y < tileset.length - 1)
            downTile = tileset[y + 1][x];

        if (leftTile != null && (leftTile.getTileState() == Enums.TileState.FLOOR || leftTile.getTileState() == Enums.TileState.WALL))
        {
            dirsCovered.add(Direction.LEFT);
        }
        if (rightTile != null && (rightTile.getTileState() == Enums.TileState.FLOOR || rightTile.getTileState() == Enums.TileState.WALL))
        {
            dirsCovered.add(Direction.RIGHT);
        }
        if (upTile != null && (upTile.getTileState() == Enums.TileState.FLOOR || upTile.getTileState() == Enums.TileState.WALL))
        {
            dirsCovered.add(Direction.UP);
        }
        if (downTile != null && (downTile.getTileState() == Enums.TileState.FLOOR || downTile.getTileState() == Enums.TileState.WALL))
        {
            dirsCovered.add(Direction.DOWN);
        }

        if (dirsCovered.contains(Direction.LEFT))
        {
        	if (dirsCovered.contains(Direction.RIGHT))
        	{
        		if (dirsCovered.contains(Direction.UP))
        		{
        			if (dirsCovered.contains(Direction.DOWN))
        			{
        				updateTileset(4, 0);
        			}
        			else
        			{
        				updateTileset(7, 0);
        			}
        		}
        		else if (dirsCovered.contains(Direction.DOWN))
        		{
        			updateTileset(7, 2);
        		}
        		else
        		{
        			updateTileset(7, 1);
        		}
        	}
        	else if (dirsCovered.contains(Direction.UP))
        	{
        		if (dirsCovered.contains(Direction.DOWN))
        		{
        			updateTileset(4, 3);
        		}
        		else
        		{
        			updateTileset(5, 1);
        		}
        	}
        	else if (dirsCovered.contains(Direction.DOWN))
        	{
        		updateTileset(5, 2);
        	}
        	else
        	{
        		updateTileset(5, 0);
        	}
        }
        else if (dirsCovered.contains(Direction.RIGHT))
        {
        	if (dirsCovered.contains(Direction.UP))
        	{
        		if (dirsCovered.contains(Direction.DOWN))
        		{
        			updateTileset(6, 3);
        		}
        		else
        		{
        			updateTileset(6, 1);
        		}
        	}
        	else if (dirsCovered.contains(Direction.DOWN))
        	{
        		updateTileset(6, 2);
        	}
        	else
        	{
        		updateTileset(6, 0);
        	}
        }
        else if (dirsCovered.contains(Direction.UP))
        {
        	if (dirsCovered.contains(Direction.DOWN))
        	{
        		updateTileset(5, 3);
        	}
        	else
        	{
        		updateTileset(4, 1);
        	}
        }
        else if (dirsCovered.contains(Direction.DOWN))
        {
        	updateTileset(4, 2);
        }
        else
        {
        	updateTileset(7, 3);
        }

        rotateTilesetCoords();
    }

    /*public void updateBordersWall(Tile[][] tileset, int x, int y)
    {
        EnumSet<Direction> dirsCovered = EnumSet.noneOf(Direction.class);

        Tile leftTile = null, rightTile = null, upTile = null, downTile = null;

        if (x > 0)
            leftTile = tileset[y][x - 1];
        if (x < tileset[0].length - 1)
            rightTile = tileset[y][x + 1];
        if (y > 0)
            upTile = tileset[y - 1][x];
        if (y < tileset.length - 1)
            downTile = tileset[y + 1][x];

        if (leftTile != null && (leftTile.getTileState() == Enums.TileState.FLOOR || leftTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(Direction.LEFT);
        }
        if (rightTile != null && (rightTile.getTileState() == Enums.TileState.FLOOR || rightTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(Direction.RIGHT);
        }
        if (upTile != null && (upTile.getTileState() == Enums.TileState.FLOOR || upTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(Direction.UP);
        }
        if (downTile != null && (downTile.getTileState() == Enums.TileState.FLOOR || downTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(Direction.DOWN);
        }

        switch (dirsCovered)
        {
            case Enums.Direction.LEFT:
                leftInnerWallEdgeDetection(tileset, x, y);
                break;
            case Enums.Direction.RIGHT:
                rightInnerWallEdgeDetection(tileset, x, y);
                break;
            case Enums.Direction.UP:
                upInnerWallEdgeDetection(tileset, x, y);
                break;
            case Enums.Direction.DOWN:
                downInnerWallEdgeDetection(tileset, x, y);
                break;
            case Enums.Direction.LEFT | Enums.Direction.RIGHT:
                updateTileset(3, 5);
                break;
            case Enums.Direction.LEFT | Enums.Direction.UP:
                if (x < tileset[0].length - 1 && y < tileset.length - 1)
                {
                    if (tileset[y + 1][x + 1].getTileState() == Enums.TileState.PIT || tileset[y + 1][x + 1].getTileState() == Enums.TileState.FLOOR)
                        updateTileset(8, 4);
                    else
                        updateTileset(1, 5);
                }
                else
                    updateTileset(1, 5);
                break;
            case Enums.Direction.LEFT | Enums.Direction.DOWN:
                if (x < tileset[0].length - 1 && y > 0)
                {
                    if (tileset[y - 1][x + 1].getTileState() == Enums.TileState.PIT || tileset[y - 1][x + 1].getTileState() == Enums.TileState.FLOOR)
                        updateTileset(8, 5);
                    else
                        updateTileset(1, 6);
                }
                else
                    updateTileset(1, 6);
                break;
            case Enums.Direction.RIGHT | Enums.Direction.UP:
                if (x > 0 && y < tileset.length - 1)
                {
                    if (tileset[y + 1][x - 1].getTileState() == Enums.TileState.PIT || tileset[y + 1][x - 1].getTileState() == Enums.TileState.FLOOR)
                        updateTileset(9, 4);
                    else
                        updateTileset(2, 5);
                }
                else
                    updateTileset(2, 5);
                break;
            case Enums.Direction.RIGHT | Enums.Direction.DOWN:
                if (x > 0 && y > 0)
                {
                    if (tileset[y - 1][x - 1].getTileState() == Enums.TileState.PIT || tileset[y - 1][x - 1].getTileState() == Enums.TileState.FLOOR)
                        updateTileset(9, 5);
                    else
                        updateTileset(2, 6);
                }
                else
                    updateTileset(2, 6);
                break;
            case Enums.Direction.UP | Enums.Direction.DOWN:
                updateTileset(1, 7);
                break;
            case Enums.Direction.LEFT | Enums.Direction.RIGHT | Enums.Direction.UP:
                updateTileset(3, 4);
                break;
            case Enums.Direction.LEFT | Enums.Direction.RIGHT | Enums.Direction.DOWN:
                updateTileset(3, 6);
                break;
            case Enums.Direction.LEFT | Enums.Direction.UP | Enums.Direction.DOWN:
                updateTileset(0, 7);
                break;
            case Enums.Direction.RIGHT | Enums.Direction.UP | Enums.Direction.DOWN:
                updateTileset(2, 7);
                break;
            case Enums.Direction.LEFT | Enums.Direction.RIGHT | Enums.Direction.UP | Enums.Direction.DOWN:
                updateTileset(0, 4);
                break;
            default:
                //updateTileset(3, 7);
                noneInnerWallEdgeDetection(tileset, x, y);
                break;
        }

        rotateTilesetCoords();
    }

    private void noneInnerWallEdgeDetection(Tile[][] tileset, int x, int y)
    {
        EnumSet<DiagDir> dirsCovered = EnumSet.noneOf(DiagDir.class);

        Tile TLTile = null, TRTile = null, BLTile = null, BRTile = null;

        if (x > 0 && y > 0)
            TLTile = tileset[y - 1][x - 1];
        if (x < tileset[0].length - 1 && y > 0)
            TRTile = tileset[y - 1][x + 1];
        if (x > 0 && y < tileset.length - 1)
            BLTile = tileset[y + 1][x - 1];
        if (x < tileset[0].length - 1 && y < tileset.length - 1)
            BRTile = tileset[y + 1][x + 1];

        if (TLTile != null && (TLTile.getTileState() == Enums.TileState.FLOOR || TLTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.TOPLEFT);
        }
        if (TRTile != null && (TRTile.getTileState() == Enums.TileState.FLOOR || TRTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.TOPRIGHT);
        }
        if (BLTile != null && (BLTile.getTileState() == Enums.TileState.FLOOR || BLTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.BOTTOMLEFT);
        }
        if (BRTile != null && (BRTile.getTileState() == Enums.TileState.FLOOR || BRTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.BOTTOMRIGHT);
        }

        switch (dirsCovered)
        {
            case DiagDir.BOTTOMLEFT:
                updateTileset(5, 4);
                break;
            case DiagDir.BOTTOMRIGHT:
                updateTileset(4, 4);
                break;
            case DiagDir.TOPLEFT:
                updateTileset(5, 5);
                break;
            case DiagDir.TOPRIGHT:
                updateTileset(4, 5);
                break;
            case DiagDir.BOTTOMLEFT | DiagDir.BOTTOMRIGHT:
                updateTileset(4, 7);
                break;
            case DiagDir.BOTTOMLEFT | DiagDir.TOPLEFT:
                updateTileset(5, 6);
                break;
            case DiagDir.BOTTOMLEFT | DiagDir.TOPRIGHT:
                updateTileset(11, 6);
                break;
            case DiagDir.BOTTOMRIGHT | DiagDir.TOPLEFT:
                updateTileset(11, 5);
                break;
            case DiagDir.BOTTOMRIGHT | DiagDir.TOPRIGHT:
                updateTileset(4, 6);
                break;
            case DiagDir.TOPLEFT | DiagDir.TOPRIGHT:
                updateTileset(5, 7);
                break;
            case DiagDir.BOTTOMLEFT | DiagDir.BOTTOMRIGHT | DiagDir.TOPLEFT:
                updateTileset(7, 4);
                break;
            case DiagDir.BOTTOMLEFT | DiagDir.BOTTOMRIGHT | DiagDir.TOPRIGHT:
                updateTileset(6, 4);
                break;
            case DiagDir.BOTTOMLEFT | DiagDir.TOPLEFT | DiagDir.TOPRIGHT:
                updateTileset(7, 5);
                break;
            case DiagDir.BOTTOMRIGHT | DiagDir.TOPLEFT | DiagDir.TOPRIGHT:
                updateTileset(6, 5);
                break;
            case DiagDir.BOTTOMLEFT | DiagDir.BOTTOMRIGHT | DiagDir.TOPLEFT | DiagDir.TOPRIGHT:
                updateTileset(6, 6);
                break;
            default:
                updateTileset(3, 7);
                break;
        }
    }

    private void leftInnerWallEdgeDetection(Tile[][] tileset, int x, int y)
    {
        EnumSet<DiagDir> dirsCovered = EnumSet.noneOf(DiagDir.class);

        Tile TRTile = null, BRTile = null;

        if (x < tileset[0].length - 1 && y > 0)
            TRTile = tileset[y - 1][x + 1];
        if (x < tileset[0].length - 1 && y < tileset.length - 1)
            BRTile = tileset[y + 1][x + 1];

        if (TRTile != null && (TRTile.getTileState() == Enums.TileState.FLOOR || TRTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.TOPRIGHT);
        }
        if (BRTile != null && (BRTile.getTileState() == Enums.TileState.FLOOR || BRTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.BOTTOMRIGHT);
        }

        switch (dirsCovered)
        {
            case DiagDir.BOTTOMRIGHT:
                updateTileset(7, 6);
                break;
            case DiagDir.TOPRIGHT:
                updateTileset(7, 7);
                break;
            case DiagDir.BOTTOMRIGHT | DiagDir.TOPRIGHT:
                updateTileset(6, 7);
                break;
            default:
                updateTileset(1, 4);
                break;
        }
    }

    private void rightInnerWallEdgeDetection(Tile[][] tileset, int x, int y)
    {
        EnumSet<DiagDir> dirsCovered = EnumSet.noneOf(DiagDir.class);

        Tile TLTile = null, BLTile = null;

        if (x > 0 && y > 0)
            TLTile = tileset[y - 1][x - 1];
        if (x > 0 && y < tileset.length - 1)
            BLTile = tileset[y + 1][x - 1];

        if (TLTile != null && (TLTile.getTileState() == Enums.TileState.FLOOR || TLTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.TOPLEFT);
        }
        if (BLTile != null && (BLTile.getTileState() == Enums.TileState.FLOOR || BLTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.BOTTOMLEFT);
        }

        switch (dirsCovered)
        {
            case DiagDir.BOTTOMLEFT:
                updateTileset(8, 6);
                break;
            case DiagDir.TOPLEFT:
                updateTileset(8, 7);
                break;
            case DiagDir.BOTTOMLEFT | DiagDir.TOPLEFT:
                updateTileset(9, 7);
                break;
            default:
                updateTileset(2, 4);
                break;
        }
    }

    private void downInnerWallEdgeDetection(Tile[][] tileset, int x, int y)
    {
        EnumSet<DiagDir> dirsCovered = EnumSet.noneOf(EnumSet.class);

        Tile TLTile = null, TRTile = null;

        if (x > 0 && y > 0)
            TLTile = tileset[y - 1][x - 1];
        if (x < tileset[0].length - 1 && y > 0)
            TRTile = tileset[y - 1][x + 1];

        if (TLTile != null && (TLTile.getTileState() == Enums.TileState.FLOOR || TLTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.TOPLEFT);
        }
        if (TRTile != null && (TRTile.getTileState() == Enums.TileState.FLOOR || TRTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.TOPRIGHT);
        }

        switch (dirsCovered)
        {
            case DiagDir.TOPRIGHT:
                updateTileset(10, 7);
                break;
            case DiagDir.TOPLEFT:
                updateTileset(10, 6);
                break;
            case DiagDir.TOPRIGHT | DiagDir.TOPLEFT:
                updateTileset(9, 6);
                break;
            default:
                updateTileset(0, 6);
                break;
        }
    }

    private void upInnerWallEdgeDetection(Tile[][] tileset, int x, int y)
    {
        EnumSet<DiagDir> dirsCovered = EnumSet.noneOf(DiagDir.class);

        Tile BLTile = null, BRTile = null;

        if (x > 0 && y < tileset.length - 1)
            BLTile = tileset[y + 1][x - 1];
        if (x < tileset[0].length - 1 && y < tileset.length - 1)
            BRTile = tileset[y + 1][x + 1];

        if (BLTile != null && (BLTile.getTileState() == Enums.TileState.FLOOR || BLTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.BOTTOMLEFT);
        }
        if (BRTile != null && (BRTile.getTileState() == Enums.TileState.FLOOR || BRTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.BOTTOMRIGHT);
        }

        switch (dirsCovered)
        {
            case DiagDir.BOTTOMLEFT:
                updateTileset(11, 7);
                break;
            case DiagDir.BOTTOMRIGHT:
                updateTileset(10, 4);
                break;
            case DiagDir.BOTTOMLEFT | DiagDir.BOTTOMRIGHT:
                updateTileset(10, 5);
                break;
            default:
                updateTileset(0, 5);
                break;
        }
    }*/
}
