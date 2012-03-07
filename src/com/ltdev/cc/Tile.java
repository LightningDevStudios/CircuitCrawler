package com.ltdev.cc;

import android.graphics.Point;

import com.ltdev.TilesetHelper;
import com.ltdev.math.Vector2;

import java.util.ArrayList;

/**
 * A tile class.
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
	
	private Point tilesetPos;
	
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

		this.tilesetPos = tilesetPos;
		this.type = type;
	}
	
	/**
	 * Gets the vertex data for this tile.
	 * @return The tile's vertices.
	 */
	public float[] getVertexData()
	{
	    //calculate vertices
	    float s = TILE_SIZE / 2;
	    
	    float[] vertices = null;
        float[] texCoords = null;
        float[] normals = null;
	    
	    switch (type)
	    {
	        case FLOOR:
	            vertices = new float[]
                {
                    -s,  s, 0,
                    -s, -s, 0,
                     s, -s, 0,
                     s,  s, 0
                };
	            texCoords = new float[]
                {
                    0, 0,
                    0, 64f / 256f - 1f / 512f,
                    64f / 512f - 1f / 1024f, 64.0f / 256.0f - 1f / 512f,
                    64f / 512f - 1f / 1024f, 0
                };
	            normals = new float[]
                {
	                0, 0, 1,
	                0, 0, 1,
	                0, 0, 1,
	                0, 0, 1
                };
	            break;
	        case WALL:
	            vertices = TilesetHelper.getTileVertices(this.borders, s, 0.2f);
	            texCoords = TilesetHelper.getWallTexCoords(this.borders);
	            normals = TilesetHelper.getTileNormals(this.borders);
	            break;
	        case PIT:
	            vertices = TilesetHelper.getTileVertices(this.borders, s, -0.2f);
	            texCoords = TilesetHelper.getPitTexCoords(this.borders);
	            normals = TilesetHelper.getTileNormals(this.borders);
	            break;
	        default:
	            //by default use the same vertices as the floor.
	            vertices = new float[]
	            {
                    -s,  s, 0,
                    -s, -s, 0,
                     s, -s, 0,
                     s,  s, 0
	            };
	            texCoords = new float[]
                {
                    0, 0,
                    0, 64f / 256f - 1f / 512f,
                    64f / 512f - 1f / 1024f, 64.0f / 256.0f - 1f / 512f,
                    64f / 512f - 1f / 1024f, 0
                };
	            normals = new float[]
                {
                    0, 0, 1,
                    0, 0, 1,
                    0, 0, 1,
                    0, 0, 1
                };
	    }
	    
	    
	    
	    //shift vertices to position
        for (int i = 0; i < vertices.length; i += 3)
        {
            vertices[i] += pos.x();
            vertices[i + 1] += pos.y();
        }
        
        float[] vertexData = new float[(vertices.length / 3) * 8];
        
        //interleave arrays
        for (int i = 0; i < vertices.length / 3; i++)
        {
            vertexData[i * 8]     = vertices[i * 3];
            vertexData[i * 8 + 1] = vertices[i * 3 + 1];
            vertexData[i * 8 + 2] = vertices[i * 3 + 2];
            vertexData[i * 8 + 3] = texCoords[i * 2];
            vertexData[i * 8 + 4] = texCoords[i * 2 + 1];
            vertexData[i * 8 + 5] = normals[i * 3];
            vertexData[i * 8 + 6] = normals[i * 3 + 1];
            vertexData[i * 8 + 7] = normals[i * 3 + 2];
        }
        
        return vertexData;
	}
	
	/**
	 * Gets the index data for this tile.
	 * @param startVert The starting index for this tile.
	 * @return An int array containing the tile's indices.
	 */
	public int[] getIndexData(int startVert)
	{
	    int[] order;
        
        switch (type)
        {
            case FLOOR:
                order = new int[] { 0, 1, 2, 0, 2, 3 };
                break;
            case WALL:
                order = TilesetHelper.getTileIndices(this.borders);
                break;
            case PIT:
                order = TilesetHelper.getTileIndices(this.borders);
                break;
            default:
                order = new int[] { 0, 1, 2, 0, 2, 3 };
        }
        
        for (int i = 0; i < order.length; i++)
        {
            order[i] += startVert;
        }
        
        return order;
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
