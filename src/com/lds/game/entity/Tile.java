package com.lds.game.entity;

import android.graphics.Point;

import com.lds.Enums;
import com.lds.Enums.*;
import com.lds.TilesetHelper;
import com.lds.game.Game;
import com.lds.math.Vector2;
import com.lds.physics.Rectangle;
import com.lds.physics.Shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.EnumSet;

import javax.microedition.khronos.opengles.GL11;

/**
 * A tile class.
 * \todo rewrite the update borders methods and get them to update the texture coordinate buffer.
 * @author Lightning Development Studios
 */
public class Tile
{
	public static final int TILE_SIZE = 72;
	public static final float TILE_SIZE_F = 72.0f;
	
	public int tileX, tileY, xIndex, yIndex;
	
	private TileState state;
	private boolean tempBridge;
	private int origTileX, origTileY;
	private Shape shape;
	
	private Vector2 pos;	
	
	private int vertVBO, texVBO, indexVBO;
	
	public Tile(GL11 gl, float size, int tileIndexX, int tileIndexY, int tilesetLengthX, int tilesetLengthY, TileState state)
	{
		TilesetHelper.setInitialTileOffset(this, tileIndexY, tileIndexX, tilesetLengthX, tilesetLengthY);
		float x = size / 2;
		float[] vertices =
	    {
		    -x, x,
		    -x, -x,
		    x, x,
		    x, -x
	    };
		for (int i = 0; i < vertices.length; i++)
		{
			if (i % 2 == 0)
				vertices[i] += pos.getX();
			else
				vertices[i] += pos.getY();
		}
		
		shape = new Rectangle(size, pos, false);
		//shape.setVertices(vertices);
		xIndex = tileIndexX;
		yIndex = tileIndexY;
		this.state = state;
		
		switch (state)
		{
		case WALL:
		    setAsWall();
		    break;
		case FLOOR:
		    setAsFloor();
		    break;
		case PIT:
		    setAsPit();
		    break;
		case BRIDGE:
		    setAsBridge();
		    break;
	    default:
	        break;
		}
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		FloatBuffer vertBuffer = byteBuf.asFloatBuffer();
		vertBuffer.put(vertices);
		vertBuffer.position(0);
		
		float[] texCoords = TilesetHelper.getTextureVertices(Game.tilesetworld, tileX, tileY);
		ByteBuffer texByteBuf = ByteBuffer.allocateDirect(texCoords.length * 4);
		texByteBuf.order(ByteOrder.nativeOrder());
		FloatBuffer texBuffer = texByteBuf.asFloatBuffer();
		texBuffer.put(texCoords);
		texBuffer.position(0);
		
		byte[] indices =
		{
		    0, 1, 2, 3
		};
		ByteBuffer indBuf = ByteBuffer.allocateDirect(indices.length);
		indBuf.order(ByteOrder.nativeOrder());
		indBuf.put(indices);
		indBuf.position(0);
		
		int[] buffers = new int[3];
		gl.glGenBuffers(3, buffers, 0);
		vertVBO = buffers[0];
		texVBO = buffers[1];
		indexVBO = buffers[2];
		
		//TODO interleave into one buffer!
		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertVBO);
		gl.glBufferData(GL11.GL_ARRAY_BUFFER, vertices.length * 4, vertBuffer, GL11.GL_STATIC_DRAW);
		
		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, texVBO);
		gl.glBufferData(GL11.GL_ARRAY_BUFFER, texCoords.length * 4, texBuffer, GL11.GL_STATIC_DRAW);
		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		
		gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indexVBO);
		gl.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indices.length, indBuf, GL11.GL_STATIC_DRAW);
		gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void draw(GL11 gl)
	{
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertVBO);
        gl.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
        
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, texVBO);
        gl.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
        
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indexVBO);
        gl.glDrawElements(GL11.GL_TRIANGLE_STRIP, 4, GL11.GL_UNSIGNED_BYTE, 0);
        
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void updateTileset(int x, int y)
	{
		tileX = x;
		tileY = y;
	}
	
	public void regenTexCoords(GL11 gl)
	{
	    float[] texCoords = TilesetHelper.getTextureVertices(Game.tilesetworld, tileX, tileY);
        ByteBuffer texByteBuf = ByteBuffer.allocateDirect(texCoords.length * 4);
        texByteBuf.order(ByteOrder.nativeOrder());
        FloatBuffer texBuffer = texByteBuf.asFloatBuffer();
        texBuffer.put(texCoords);
        texBuffer.position(0);
        
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, texVBO);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, texCoords.length * 4, texBuffer, GL11.GL_STATIC_DRAW);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
	}
	
	public void setAsWall()
	{
		state = TileState.WALL;
		if (tempBridge)
		{
			updateTileset(origTileX, origTileY);
			tempBridge = false;
		}
		else
		{
			updateTileset(3, 7);
		}
		shape.setSolid(true);
	}
	
	public void setAsFloor()
	{
		state = TileState.FLOOR;
		updateTileset((int)(Math.random() * 4), (int)(Math.random() * 4));
		shape.setSolid(false);
	}
	
	public void setAsSlipperyTile()
	{
		state = TileState.SlipperyTile;
		updateTileset(15, 0);
		shape.setSolid(false);
	}
	
	public void setAsPit()
	{
		state = TileState.PIT;
		if (tempBridge)
		{
			updateTileset(origTileX, origTileY);
			tempBridge = false;
		}
		else
		{
			updateTileset(7, 3);
		}
		shape.setSolid(false);
	}
	
	public void setAsBridge()
	{
		state = TileState.BRIDGE;
		
		if (tempBridge)
		{
			updateTileset(origTileX, origTileY);
			tempBridge = false;
		}
		else
		{
			origTileX = tileX;
			origTileY = tileY;
			updateTileset(1, 1);
			tempBridge = true;
		}
		shape.setSolid(false);
	}
	
	public boolean isWall()
	{
		return state == TileState.WALL;
	}
	
	public boolean isFloor()
	{
	    return state == TileState.FLOOR;
	}
	
	public boolean isPit()
	{
		return state == TileState.PIT;
	}
	
	public boolean isSlipperyTile()
	{
		return state == TileState.SlipperyTile;
	}
	
	public boolean isBridge()
	{
		return state == TileState.BRIDGE;
	}
	
	public TileState getTileState()
	{
		return state;
	}
	
	public void updateBordersPit(Tile[][] tileset, int x, int y)
    {
	    //a bitfield for the 8 bordering tiles.
	    /*byte covered = 0;

	    //store all the bordering tile indices in top left to bottom right order.
	    ArrayList<Point> points = new ArrayList<Point>();
	    points.add(new Point(x - 1, y - 1));
	    points.add(new Point(x, y - 1));
	    points.add(new Point(x + 1, y - 1));
	    points.add(new Point(x - 1, y));
	    points.add(new Point(x + 1, y));
	    points.add(new Point(x - 1, y + 1));
	    points.add(new Point(x, y + 1));
	    points.add(new Point(x + 1, y + 1));
	    
	    //check each of the bordering tiles
	    for (int i = 0; i < points.size(); i++)
	    {
	        Point p = points.get(i);
	        
	        //make sure tileset index is within tileset bounds
	        if (p.x > 0 && p.x < tileset[0].length && p.y > 0 && p.y < tileset.length)
	        {
	            Tile t = tileset[p.y][p.x];
	            
	            //if the bordering tile is not a pit, it's considered a border.
	            if (t != null && (t.getTileState() != TileState.PIT))
	                covered |= 1 << i;
	        }
	    }
	    
	    if (TilesetHelper.pitTexPoints.containsKey(covered))
	    {
	        Point p = TilesetHelper.pitTexPoints.get(covered);
	        updateTileset(p.x, p.y);
	    }
	    else
	    {
	        updateTileset(7, 3);
	    }*/
	    
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
    }

    public void updateBordersWall(Tile[][] tileset, int x, int y)
    {
        //a bitfield for the 8 bordering tiles.
        byte covered = 0;

        //store all the bordering tile indices in top left to bottom right order.
        ArrayList<Point> points = new ArrayList<Point>();
        points.add(new Point(x - 1, y - 1));
        points.add(new Point(x, y - 1));
        points.add(new Point(x + 1, y - 1));
        points.add(new Point(x - 1, y));
        points.add(new Point(x + 1, y));
        points.add(new Point(x - 1, y + 1));
        points.add(new Point(x, y + 1));
        points.add(new Point(x + 1, y + 1));
        
        points.add(new Point(x + 1, y + 1));
        points.add(new Point(x, y + 1));
        points.add(new Point(x - 1, y + 1));
        points.add(new Point(x + 1, y));
        points.add(new Point(x - 1, y));
        points.add(new Point(x + 1, y - 1));
        points.add(new Point(x, y - 1));
        points.add(new Point(x - 1, y - 1));
        
        //check each of the bordering tiles
        for (int i = 0; i < points.size(); i++)
        {
            Point p = points.get(i);
            
            //make sure tileset index is within tileset bounds
            if (p.x > 0 && p.x < tileset[0].length && p.y > 0 && p.y < tileset.length)
            {
                Tile t = tileset[p.y][p.x];
                
                //if the bordering tile is not a pit, it's considered a border.
                if (t != null && (t.getTileState() != TileState.WALL))
                    covered |= (1 << i);
                    //covered += (byte)Math.pow(2, i) - 1;
            }
        }
        
        if (TilesetHelper.wallTexPoints.containsKey(covered))
        {
            Point po = TilesetHelper.wallTexPoints.get(covered);
            updateTileset(po.x, po.y);
        }
        else
        {
            updateTileset(7, 3);
        }
        
        /*EnumSet<Direction> dirsCovered = EnumSet.noneOf(Direction.class);

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

        if (dirsCovered.contains(Direction.LEFT))
        {
        	if (dirsCovered.contains(Direction.RIGHT))
        	{
        		if (dirsCovered.contains(Direction.UP))
        		{
        			if (dirsCovered.contains(Direction.DOWN))
        			{
        				updateTileset(0, 4);
        			}
        			else
        			{
        				updateTileset(3, 4);
        			}
        		}
        		else if (dirsCovered.contains(Direction.DOWN))
        		{
        			updateTileset(3, 6);
        		}
        		else
        		{
        			updateTileset(3, 5);
        		}
        	}
        	else if (dirsCovered.contains(Direction.UP))
        	{
        		if (dirsCovered.contains(Direction.DOWN))
        		{
        			updateTileset(0, 7);
        		}
        		else
        		{
        			if (x < tileset[0].length - 1 && y < tileset.length - 1)
                    {
                        if (tileset[y + 1][x + 1].getTileState() == Enums.TileState.PIT || tileset[y + 1][x + 1].getTileState() == Enums.TileState.FLOOR)
                            updateTileset(8, 4);
                        else
                            updateTileset(1, 5);
                    }
                    else
                        updateTileset(1, 5);
        		}
        	}
        	else if (dirsCovered.contains(Direction.DOWN))
        	{
        		if (x < tileset[0].length - 1 && y > 0)
                {
                    if (tileset[y - 1][x + 1].getTileState() == Enums.TileState.PIT || tileset[y - 1][x + 1].getTileState() == Enums.TileState.FLOOR)
                        updateTileset(8, 5);
                    else
                        updateTileset(1, 6);
                }
                else
                    updateTileset(1, 6);
        	}
        	else
        	{
        		leftInnerWallEdgeDetection(tileset, x, y);
        	}
        }
        else if (dirsCovered.contains(Direction.RIGHT))
        {
        	if (dirsCovered.contains(Direction.UP))
        	{
        		if (dirsCovered.contains(Direction.DOWN))
        		{
        			updateTileset(2, 7);
        		}
        		else
        		{
        			if (x > 0 && y < tileset.length - 1)
                    {
                        if (tileset[y + 1][x - 1].getTileState() == Enums.TileState.PIT || tileset[y + 1][x - 1].getTileState() == Enums.TileState.FLOOR)
                            updateTileset(9, 4);
                        else
                            updateTileset(2, 5);
                    }
                    else
                        updateTileset(2, 5);
        		}
        	}
        	else if (dirsCovered.contains(Direction.DOWN))
        	{
        		if (x > 0 && y > 0)
                {
                    if (tileset[y - 1][x - 1].getTileState() == Enums.TileState.PIT || tileset[y - 1][x - 1].getTileState() == Enums.TileState.FLOOR)
                        updateTileset(9, 5);
                    else
                        updateTileset(2, 6);
                }
                else
                    updateTileset(2, 6);
        	}
        	else
        	{
        		rightInnerWallEdgeDetection(tileset, x, y);
        	}
        }
        else if (dirsCovered.contains(Direction.UP))
        {
        	if (dirsCovered.contains(Direction.DOWN))
        	{
        		updateTileset(1, 7);
        	}
        	else
        	{
        		upInnerWallEdgeDetection(tileset, x, y);
        	}
        }
        else if (dirsCovered.contains(Direction.DOWN))
        {
        	downInnerWallEdgeDetection(tileset, x, y);
        }
        else
        {
        	//updateTileset(3, 7);
            noneInnerWallEdgeDetection(tileset, x, y);
        }*/
    }

    /*private void noneInnerWallEdgeDetection(Tile[][] tileset, int x, int y)
    {
        EnumSet<DiagDir> dirsCovered = EnumSet.noneOf(DiagDir.class);

        Tile tlTile = null, trTile = null, blTile = null, brTile = null;

        if (x > 0 && y > 0)
            tlTile = tileset[y - 1][x - 1];
        if (x < tileset[0].length - 1 && y > 0)
            trTile = tileset[y - 1][x + 1];
        if (x > 0 && y < tileset.length - 1)
            blTile = tileset[y + 1][x - 1];
        if (x < tileset[0].length - 1 && y < tileset.length - 1)
            brTile = tileset[y + 1][x + 1];

        if (tlTile != null && (tlTile.getTileState() == Enums.TileState.FLOOR || tlTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.TOPLEFT);
        }
        if (trTile != null && (trTile.getTileState() == Enums.TileState.FLOOR || trTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.TOPRIGHT);
        }
        if (blTile != null && (blTile.getTileState() == Enums.TileState.FLOOR || blTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.BOTTOMLEFT);
        }
        if (brTile != null && (brTile.getTileState() == Enums.TileState.FLOOR || brTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.BOTTOMRIGHT);
        }

        //LEFT RIGHT UP DOWN
        //TL   TR    BL BR
        if (dirsCovered.contains(DiagDir.TOPLEFT))
        {
        	if (dirsCovered.contains(DiagDir.TOPRIGHT))
        	{
        		if (dirsCovered.contains(DiagDir.BOTTOMLEFT))
        		{
        			if (dirsCovered.contains(DiagDir.BOTTOMRIGHT))
        			{
        				updateTileset(6, 6);
        			}
        			else
        			{
        				updateTileset(7, 5);
        			}
        		}
        		else if (dirsCovered.contains(DiagDir.BOTTOMRIGHT))
        		{
        			updateTileset(6, 5);
        		}
        		else
        		{
        			updateTileset(5, 7);
        		}
        	}
        	else if (dirsCovered.contains(DiagDir.BOTTOMLEFT))
        	{
        		if (dirsCovered.contains(DiagDir.BOTTOMRIGHT))
        		{
        			updateTileset(7, 4);
        		}
        		else
        		{
        			updateTileset(5, 6);
        		}
        	}
        	else if (dirsCovered.contains(DiagDir.BOTTOMRIGHT))
        	{
        		updateTileset(11, 5);
        	}
        	else
        	{
        		updateTileset(5, 5);
        	}
        }
        else if (dirsCovered.contains(DiagDir.TOPRIGHT))
        {
        	if (dirsCovered.contains(DiagDir.BOTTOMLEFT))
        	{
        		if (dirsCovered.contains(DiagDir.BOTTOMRIGHT))
        		{
        			updateTileset(6, 4);
        		}
        		else
        		{
        			updateTileset(11, 6);
        		}
        	}
        	else if (dirsCovered.contains(DiagDir.BOTTOMRIGHT))
        	{
        		updateTileset(4, 6);
        	}
        	else
        	{
        		updateTileset(4, 5);
        	}
        }
        else if (dirsCovered.contains(DiagDir.BOTTOMLEFT))
        {
        	if (dirsCovered.contains(DiagDir.BOTTOMRIGHT))
        	{
        		updateTileset(4, 7);
        	}
        	else
        	{
        		updateTileset(5, 4);
        	}
        }
        else if (dirsCovered.contains(DiagDir.BOTTOMRIGHT))
        {
        	updateTileset(4, 4);
        }
        else
        {
        	updateTileset(3, 7);
        }
    }

    private void leftInnerWallEdgeDetection(Tile[][] tileset, int x, int y)
    {
        EnumSet<DiagDir> dirsCovered = EnumSet.noneOf(DiagDir.class);

        Tile trTile = null, brTile = null;

        if (x < tileset[0].length - 1 && y > 0)
            trTile = tileset[y - 1][x + 1];
        if (x < tileset[0].length - 1 && y < tileset.length - 1)
            brTile = tileset[y + 1][x + 1];

        if (trTile != null && (trTile.getTileState() == Enums.TileState.FLOOR || trTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.TOPRIGHT);
        }
        if (brTile != null && (brTile.getTileState() == Enums.TileState.FLOOR || brTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.BOTTOMRIGHT);
        }

        if (dirsCovered.contains(DiagDir.BOTTOMRIGHT))
        {
        	if (dirsCovered.contains(DiagDir.TOPRIGHT))
        	{
        		updateTileset(6, 7);
        	}
        	else
        	{
        		updateTileset(7, 6);
        	}
        }
        else if (dirsCovered.contains(DiagDir.TOPRIGHT))
        {
        	updateTileset(7, 7);
        }
        else
        {
        	updateTileset(1, 4);
        }
    }

    private void rightInnerWallEdgeDetection(Tile[][] tileset, int x, int y)
    {
        EnumSet<DiagDir> dirsCovered = EnumSet.noneOf(DiagDir.class);

        Tile tlTile = null, blTile = null;

        if (x > 0 && y > 0)
            tlTile = tileset[y - 1][x - 1];
        if (x > 0 && y < tileset.length - 1)
            blTile = tileset[y + 1][x - 1];

        if (tlTile != null && (tlTile.getTileState() == Enums.TileState.FLOOR || tlTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.TOPLEFT);
        }
        if (blTile != null && (blTile.getTileState() == Enums.TileState.FLOOR || blTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.BOTTOMLEFT);
        }

        if (dirsCovered.contains(DiagDir.BOTTOMLEFT))
        {
        	if (dirsCovered.contains(DiagDir.TOPLEFT))
        	{
        		updateTileset(9, 7);
        	}
        	else
        	{
        		updateTileset(8, 6);
        	}
        }
        else if (dirsCovered.contains(DiagDir.TOPLEFT))
        {
        	updateTileset(8, 7);
        }
        else
        {
        	updateTileset(2, 4);
        }
    }

    private void downInnerWallEdgeDetection(Tile[][] tileset, int x, int y)
    {
        EnumSet<DiagDir> dirsCovered = EnumSet.noneOf(DiagDir.class);

        Tile tlTile = null, trTile = null;

        if (x > 0 && y > 0)
            tlTile = tileset[y - 1][x - 1];
        if (x < tileset[0].length - 1 && y > 0)
            trTile = tileset[y - 1][x + 1];

        if (tlTile != null && (tlTile.getTileState() == Enums.TileState.FLOOR || tlTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.TOPLEFT);
        }
        if (trTile != null && (trTile.getTileState() == Enums.TileState.FLOOR || trTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.TOPRIGHT);
        }

        if (dirsCovered.contains(DiagDir.TOPRIGHT))
        {
        	if (dirsCovered.contains(DiagDir.TOPLEFT))
        	{
        		updateTileset(9, 6);
        	}
        	else
        	{
        		updateTileset(10, 7);
        	}
        }
        else if (dirsCovered.contains(DiagDir.TOPLEFT))
        {
        	updateTileset(10, 6);
        }
        else
        {
        	updateTileset(0, 6);
        }
    }

    private void upInnerWallEdgeDetection(Tile[][] tileset, int x, int y)
    {
        EnumSet<DiagDir> dirsCovered = EnumSet.noneOf(DiagDir.class);

        Tile blTile = null, brTile = null;

        if (x > 0 && y < tileset.length - 1)
            blTile = tileset[y + 1][x - 1];
        if (x < tileset[0].length - 1 && y < tileset.length - 1)
            brTile = tileset[y + 1][x + 1];

        if (blTile != null && (blTile.getTileState() == Enums.TileState.FLOOR || blTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.BOTTOMLEFT);
        }
        if (brTile != null && (brTile.getTileState() == Enums.TileState.FLOOR || brTile.getTileState() == Enums.TileState.PIT))
        {
            dirsCovered.add(DiagDir.BOTTOMRIGHT);
        }

        if (dirsCovered.contains(DiagDir.BOTTOMLEFT))
        {
        	if (dirsCovered.contains(DiagDir.BOTTOMRIGHT))
        	{
        		updateTileset(10, 5);
        	}
        	else
        	{
        		updateTileset(11, 7);
        	}
        }
        else if (dirsCovered.contains(DiagDir.BOTTOMRIGHT))
        {
        	updateTileset(10, 4);
        }
        else
        {
        	updateTileset(0, 5);
        }
    }*/
    
    public void setPos(Vector2 pos)
    {
        this.pos = pos;
    }
}
