package com.lds.game;

import android.graphics.Point;

import com.lds.TilesetHelper;
import com.lds.math.MathHelper;
import com.lds.math.Vector2;

import java.util.ArrayList;
import java.util.Map;

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
        SlipperyTile 
    }
    
	public static final int TILE_SIZE = 72;
	public static final float TILE_SIZE_F = 72.0f;
	
	private Point tilesetPos, texPos;
	
	private TileType type;
	//private Shape shape;
	
	private Vector2 pos;
	
	private byte borders;
	
	/**
	 * Initializes a new instance of the Tile class.
	 * @param tilesetPos The position of the current tile in the tileset.
	 * @param tilesetLengthX The length of the tileset in the X direction.
	 * @param tilesetLengthY The length of the tileset in the Y direction.
	 * @param type The way this tile will react with entities/other tiles.
	 */
	public Tile(Point tilesetPos, int tilesetLengthX, int tilesetLengthY, TileType type)
	{
		TilesetHelper.setInitialTileOffset(this, tilesetPos, tilesetLengthX, tilesetLengthY);
		
		//shape = new Rectangle(TILE_SIZE, pos, false);
		//shape.setVertices(vertices);
		this.tilesetPos = tilesetPos;
		this.type = type;
	}
	
	/**
	 * Gets the vertex data for this tile.
	 * \todo Entire vertex data is still being generated, fix so that only necessary data is generated per tile.
	 * @return A float[] containing this tile's vertices.
	 */
	public float[] getVertexData()
	{
	    //calculate vertices
	    float s = TILE_SIZE / 2;
	    float[] vertices =
        {
            -s,  s,
            -s, -s,
             s,  s,
             s, -s
        };
	    
	    //shift vertices to position
        for (int i = 0; i < vertices.length; i++)
        {
            if (i % 2 == 0)
                vertices[i] += pos.x();
            else
                vertices[i] += pos.y();
        }
        
        //get texture vertices
        float[] texCoords = TilesetHelper.getTextureVertices(Game.tilesetworld, texPos);
        
        //2 floats per vert * 4 verts per array * 2 arrays
        float[] vertexData = new float[2 * 4 * 2];
        
        //interleave arrays
        for (int i = 0; i < 4; i++)
        {
            vertexData[i * 4]     = vertices[i * 2];
            vertexData[i * 4 + 1] = vertices[i * 2 + 1];
            vertexData[i * 4 + 2] = texCoords[i * 2];
            vertexData[i * 4 + 3] = texCoords[i * 2 + 1];
        }
        
        return vertexData;
	}
	
	/**
	 * Returns this tile's type.
	 * @return This tile's type.
	 */
	public TileType getTileType()
	{
		return type;
	}
	
	/**
	 * Chooses the right texture for tiles that have borders.
	 * \todo design this better, the only difference between updateBordersWall and updateBordersPit is which HashSet is used to look up points.
	 */
	public void updateTexturePos()
	{	    
	    switch (type)
	    {
	    case FLOOR:
	            texPos = new Point((int)(Math.random() * 4), (int)(Math.random() * 4));
	            break;
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
	        texPos = TilesetHelper.pitTexPoints.get(borders);
	    }
	    else
	    {
	        //set defaults, in case of undefined behavior.
            texPos = new Point(7, 3);
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
                        texPos = entry.getValue();
                        borderBitsSet = entryBitsSet;
                    }
                }
            }
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
            texPos = TilesetHelper.wallTexPoints.get(borders);
        }
        
        //otherwise find a texture tile that contains all the bordering tiles.
        else
        {
            //set defaults, in case of undefined behavior.
            texPos = new Point(3, 7);
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
                        texPos = entry.getValue();
                        borderBitsSet = entryBitsSet;
                    }
                }
            }
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
     * \verbatim
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
     * \endverbatim
     * @param tileset The tileset that contains this tile.
     */
    public void calculateBorders(Tile[][] tileset)
    {
        int x = tilesetPos.x;
        int y = tilesetPos.y;
        //store all the bordering tile indices in top left to bottom right order.
        ArrayList<Point> points = new ArrayList<Point>();
        points.add(new Point(x - 1, y - 1));
        points.add(new Point(x,     y - 1));
        points.add(new Point(x + 1, y - 1));
        points.add(new Point(x - 1, y));
        points.add(new Point(x + 1, y));
        points.add(new Point(x - 1, y + 1));
        points.add(new Point(x,     y + 1));
        points.add(new Point(x + 1, y + 1));
        
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
