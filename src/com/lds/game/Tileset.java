package com.lds.game;

import com.lds.Texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL11;

public class Tileset 
{
    private Tile[][] tiles;
    private Texture tex;
    private int vertCount;
    private int indCount;
    private int vertBuffer;
    private int indexBuffer;
    
    public Tileset(Tile[][] tiles, Texture tex)
    {
        this.tiles = tiles;
        this.tex = tex;
    }
    
    public void initialize(GL11 gl)
    {
        //4 vertices * number of tiles
        vertCount = 4 * tiles.length * tiles[0].length;
        indCount = 6 * tiles.length * tiles[0].length;
        
        //4 bytes per float * 4 floats per vertex attribute(vert AND texcoord) * number of vertices
        int byteCount = 4 * 4 * vertCount;
        
        //2 bytes per short * number of indices
        int indByteCount = 2 * indCount;
        
        //store data in a float buffer, as required by Android.
        //4 bytes per float * floats in tileset buffer
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(byteCount);
        byteBuf.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = byteBuf.asFloatBuffer();
        
        ByteBuffer indByteBuf = ByteBuffer.allocateDirect(indByteCount);
        byteBuf.order(ByteOrder.nativeOrder());
        ShortBuffer indBuffer = indByteBuf.asShortBuffer();
        
        short maxIndex = 0;
        
        //iterate through tileset, generate vertex data, then append to the buffer.
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[0].length; j++)
            {
                tiles[i][j].calculateBorders(tiles);
                tiles[i][j].updateTexturePos();
                buffer.put(tiles[i][j].getVertexData());
                short[] indices = tiles[i][j].getIndices();
                
                //shift indices
                for (int k = 0; k < indices.length; k++)
                    indices[k] += maxIndex + 1;
                
                indBuffer.put(indices);
                
                //calculate the highest index for the next tile's indices
                for (short s : indices)
                {
                    if (s > maxIndex)
                        maxIndex = s;
                }
            }
        }
        
        buffer.position(0);
        indBuffer.position(0);
        
        //generate VBO
        int[] glBuffers = new int[2];
        gl.glGenBuffers(2, glBuffers, 0);
        vertBuffer = glBuffers[0];
        indexBuffer = glBuffers[1];
        
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertBuffer);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, byteCount, buffer, GL11.GL_STATIC_DRAW);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
        gl.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indByteCount, indBuffer, GL11.GL_STATIC_DRAW);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
    
    public void draw(GL11 gl)
    {
        gl.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTexture());
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertBuffer);
        gl.glVertexPointer(2, GL11.GL_FLOAT, 16, 0);
        gl.glTexCoordPointer(2, GL11.GL_FLOAT, 16, 8);
        
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
        //gl.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, vertCount);
        gl.glDrawElements(GL11.GL_TRIANGLES, indCount, GL11.GL_UNSIGNED_SHORT, 0);
        
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
        gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
    
    public Tile get(int x, int y)
    {
        return tiles[y][x];
    }
}
