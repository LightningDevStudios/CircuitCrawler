package com.lds.game;

import com.lds.Texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL11;

public class Tileset 
{
    private Tile[][] tiles;
    private Texture tex;
    private int vertCount;
    private int vbo;
    
    public Tileset(Tile[][] tiles, Texture tex)
    {
        this.tiles = tiles;
        this.tex = tex;
    }
    
    public void initialize(GL11 gl)
    {
        //4 vertices * number of tiles
        vertCount = 4 * tiles.length * tiles[0].length;
        //4 bytes per float * 4 floats per vertex (vert AND texcoord) * number of vertices
        int byteCount = 4 * 4 * vertCount;
        
        //store data in a float buffer, as required by Android.
        //4 bytes per float * floats in tileset buffer
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(byteCount);
        byteBuf.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = byteBuf.asFloatBuffer();
        
        //iterate through tileset, generate vertex data, then append to the buffer.
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[0].length; j++)
            {
                tiles[i][j].calculateBorders(tiles);
                tiles[i][j].updateTexturePos();
                buffer.put(tiles[i][j].getVertexData());
            }
        }
        
        buffer.position(0);
        
        //generate VBO
        int[] glBuffers = new int[1];
        gl.glGenBuffers(1, glBuffers, 0);
        vbo = glBuffers[0];
        
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, byteCount, buffer, GL11.GL_STATIC_DRAW);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
    }
    
    public void draw(GL11 gl)
    {
        gl.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTexture());
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo);
        gl.glVertexPointer(2, GL11.GL_FLOAT, 16, 0);
        gl.glTexCoordPointer(2, GL11.GL_FLOAT, 16, 8);
        
        gl.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, vertCount);
        
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
    
    public Tile get(int x, int y)
    {
        return tiles[y][x];
    }
}
