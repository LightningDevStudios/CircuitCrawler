package com.lds.game.entity;

import android.graphics.Point;

import com.lds.TilesetHelper;
import com.lds.game.Game;
import com.lds.math.MathHelper;
import com.lds.math.Vector2;
import com.lds.physics.Rectangle;
import com.lds.physics.Shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Map;

import javax.microedition.khronos.opengles.GL11;

/**
 * A tile class.
 * \todo rewrite the update borders methods and get them to update the texture coordinate buffer.
 * @author Lightning Development Studios
 */
public class Tile
{
    /**
     * An enum of different types of tiles.
     * @author Lightning Development Studios
     */
    public enum TileType
    {
        FLOOR,
        WALL,
        PIT,
        BRIDGE,
        SlipperyTile 
    }
    
	public static final int TILE_SIZE = 72;
	public static final float TILE_SIZE_F = 72.0f;
	
	private int tileX, tileY, xIndex, yIndex;
	
	private TileType type;
	private boolean tempBridge;
	private int origTileX, origTileY;
	private Shape shape;
	
	private Vector2 pos;	
	
	private int vertVBO, texVBO, indexVBO;
	
	private byte borders;
	
	public Tile(GL11 gl, float size, int tileIndexX, int tileIndexY, int tilesetLengthX, int tilesetLengthY, TileType type)
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
				vertices[i] += pos.x();
			else
				vertices[i] += pos.y();
		}
		
		shape = new Rectangle(size, pos, false);
		//shape.setVertices(vertices);
		xIndex = tileIndexX;
		yIndex = tileIndexY;
		this.type = type;
		
		switch (type)
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
		type = TileType.WALL;
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
		type = TileType.FLOOR;
		updateTileset((int)(Math.random() * 4), (int)(Math.random() * 4));
		shape.setSolid(false);
	}
	
	public void setAsSlipperyTile()
	{
		type = TileType.SlipperyTile;
		updateTileset(15, 0);
		shape.setSolid(false);
	}
	
	public void setAsPit()
	{
		type = TileType.PIT;
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
		type = TileType.BRIDGE;
		
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
		return type == TileType.WALL;
	}
	
	public boolean isFloor()
	{
	    return type == TileType.FLOOR;
	}
	
	public boolean isPit()
	{
		return type == TileType.PIT;
	}
	
	public boolean isSlipperyTile()
	{
		return type == TileType.SlipperyTile;
	}
	
	public boolean isBridge()
	{
		return type == TileType.BRIDGE;
	}
	
	public TileType getTileType()
	{
		return type;
	}
	
	/**
	 * Chooses the right texture for tiles that have borders.
	 * \todo design this better, the only difference between updateBordersWall and updateBordersPit is which HashSet is used to look up points.
	 */
	public void updateBorders()
	{	    
	    switch (type)
	    {
	        case WALL:
	            updateBordersWall();
	            break;
	        case PIT:
	            updateBordersPit();
	            break;
            default:
                break;
	    }
	}
	
	/**
	 * Calculates which texture tile to use based on the bordering tileset tiles.
     * The texture tileset has all it's combinations of bordering tiles defined at TilesetHelper.pitTexPoints.
	 */
	public void updateBordersPit()
    {    
	    if (TilesetHelper.pitTexPoints.containsKey(borders))
	    {
	        Point p = TilesetHelper.pitTexPoints.get(borders);
	        updateTileset(p.x, p.y);
	    }
	    else
	    {
	      //set defaults, in case of undefined behavior.
            Point finalPt = new Point(7, 3);
            int borderBitsSet = 8;
            
            for (Map.Entry<Byte, Point> entry : TilesetHelper.pitTexPoints.entrySet())
            {
                if ((entry.getKey() & borders) == borders)
                {
                    //Since there will be multiple texture tiles that contain all the borders,
                    //we want to look for the one with the least number of bordering tiles.
                    //This gives us the closest match. Repeat until the smallest texture tile is found.
                    int entryBitsSet = MathHelper.numberOfBitsSet(entry.getKey());
                    if (entryBitsSet < borderBitsSet)
                    {
                        finalPt = entry.getValue();
                        borderBitsSet = entryBitsSet;
                    }
                }
            }

            updateTileset(finalPt.x, finalPt.y);
	    }
    }

	/**
	 * Calculates which texture tile to use based on the bordering tileset tiles.
	 * The texture tileset has all it's combinations of bordering tiles defined at TilesetHelper.wallTexPoints.
	 */
    public void updateBordersWall()
    {
        //if the bordering tiles are the exact arrangement as one of the texture tiles, use that directly.
        if (TilesetHelper.wallTexPoints.containsKey(borders))
        {
            Point po = TilesetHelper.wallTexPoints.get(borders);
            updateTileset(po.x, po.y);
        }
        
        //otherwise find a texture tile that contains all the bordering tiles.
        else
        {
            //set defaults, in case of undefined behavior.
            Point finalPt = new Point(3, 7);
            int borderBitsSet = 8;
            
            for (Map.Entry<Byte, Point> entry : TilesetHelper.wallTexPoints.entrySet())
            {
                if ((entry.getKey() & borders) == borders)
                {
                    //Since there will be multiple texture tiles that contain all the borders,
                    //we want to look for the one with the least number of bordering tiles.
                    //This gives us the closest match. Repeat until the smallest texture tile is found.
                    int entryBitsSet = MathHelper.numberOfBitsSet(entry.getKey());
                    if (entryBitsSet < borderBitsSet)
                    {
                        finalPt = entry.getValue();
                        borderBitsSet = entryBitsSet;
                    }
                }
            }

            updateTileset(finalPt.x, finalPt.y);
        }
    }
    
    /**
     * Calculates the bordering tile bitfield. 
     * A tile is considered a "bordering tile" of a given tile if it meets the following criteria:
     * <ul>
     * <li> The bordering tile shares an edge or vertex with the given tile.</li>
     * <li> The bordering tile is not of the same type as the given tile.</li>
     * </ul>
     * The result of this method is stored as a byte. Each potential bordering tile is represented as a flag in a bitfield.
     * The flags are defined as follows:
     * +------+------+------+
     * |      |      |      |
     * | 0x01 | 0x02 | 0x04 |
     * |      |      |      |
     * +------+------+------+
     * |      |      |      |
     * | 0x08 |      | 0x10 |
     * |      |      |      |
     * +------+------+------+
     * |      |      |      |
     * | 0x20 | 0x40 | 0x80 |
     * |      |      |      |
     * +------+------+------+
     * @param tileset The tileset that contains this tile.
     */
    public void calculateBorders(Tile[][] tileset)
    {
        //store all the bordering tile indices in top left to bottom right order.
        ArrayList<Point> points = new ArrayList<Point>();
        points.add(new Point(xIndex - 1, yIndex - 1));
        points.add(new Point(xIndex,     yIndex - 1));
        points.add(new Point(xIndex + 1, yIndex - 1));
        points.add(new Point(xIndex - 1, yIndex));
        points.add(new Point(xIndex + 1, yIndex));
        points.add(new Point(xIndex - 1, yIndex + 1));
        points.add(new Point(xIndex,     yIndex + 1));
        points.add(new Point(xIndex + 1, yIndex + 1));
        
        //check each of the bordering tiles, set it's corresponding bit to 1 if it's not the same type of tile.
        for (int i = 0; i < points.size(); i++)
        {
            Point p = points.get(i);
            
            //make sure tileset index is within tileset bounds
            if (p.x >= 0 && p.x < tileset[0].length && p.y >= 0 && p.y < tileset.length)
            {
                Tile t = tileset[p.y][p.x];
                
                //if the tile type is not the same, it's considered a bordering tile.
                if (t != null && t.getTileType() != type)
                    borders |= 0x01 << i;
            }
        }
    }
    
    /**
     * Sets the tile's position.
     * @param pos The tile's new position.
     */
    public void setPos(Vector2 pos)
    {
        this.pos = pos;
    }
}
