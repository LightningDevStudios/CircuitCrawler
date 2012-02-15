package com.lds.game;

import com.lds.Texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL11;

/**
 * Contains and manages a 2d array of tiles.
 * @author Lightning Development Studios
 *
 */
public class Tileset 
{
    private Tile[][] tiles;
    private Texture tex;
    private int vertCount;
    private int indCount;
    private int vertBuffer;
    
    /**
     * Initializes a new instance of the Tileset class.
     * @param tiles A 2d array of tiles.
     * @param tex The texture that the tileset will use.
     */
    public Tileset(Tile[][] tiles, Texture tex)
    {
        this.tiles = tiles;
        this.tex = tex;
    }
    
    /**
     * Initializes the parts of the tileset that rely on OpenGL.
     * @param gl The OpenGL context to use.
     */
    public void initialize(GL11 gl)
    {
        ArrayList<Float> verts = new ArrayList<Float>();
        
        //store data in a float buffer, as required by Android.
        
        //iterate through tileset, generate vertex data, then append to the buffer.
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[0].length; j++)
            {
                tiles[i][j].calculateBorders(tiles);
                tiles[i][j].updateTexturePos();
                for (Float f : tiles[i][j].getVertexData())
                {
                    verts.add(f);
                }
            }
        }
        
        vertCount = verts.size();
        indCount = vertCount / 4;
        
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertCount * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = byteBuf.asFloatBuffer();
        
        for (float f : verts)
        {
            buffer.put(f);
        }
             
        buffer.position(0);
        
        //generate VBO
        int[] glBuffers = new int[1];
        gl.glGenBuffers(1, glBuffers, 0);
        vertBuffer = glBuffers[0];
        
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertBuffer);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, vertCount * 4, buffer, GL11.GL_STATIC_DRAW);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
    }
    
    /**
     * Draws the tileset.
     * @param gl The OpenGL context to draw with.
     */
    public void draw(GL11 gl)
    {        
        gl.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTexture());
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertBuffer);
        gl.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);
        gl.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 12);
        
        gl.glDrawArrays(GL11.GL_TRIANGLES, 0, indCount);
        
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
    
    /**
     * Gets the tile at a specified location.
     * @param x The X location of a tile.
     * @param y The Y location of a tile.
     * @return The tile at (x, y).
     */
    public Tile get(int x, int y)
    {
        return tiles[y][x];
    }
}
