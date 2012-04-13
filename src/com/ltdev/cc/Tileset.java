/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev.cc;

import com.ltdev.Texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
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
    private int indBuffer;
    
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
        ArrayList<Short> indices = new ArrayList<Short>();
        
        int indPos = 0;
        
        //iterate through tileset, generate vertex data, then append to the buffer.
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[0].length; j++)
            {
                tiles[i][j].calculateBorders(tiles);
                float[] vertData = tiles[i][j].getVertexData();
                int[] indData = tiles[i][j].getIndexData(indPos);
                
                indPos += vertData.length / 8;
                
                for (Float f : vertData)
                    verts.add(f);
                
                for (Integer ind : indData)
                    indices.add(ind.shortValue());
            }
        }
        
        vertCount = verts.size();
        indCount = indices.size();
        
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertCount * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = byteBuf.asFloatBuffer();
        
        for (float f : verts)
            buffer.put(f);
             
        buffer.position(0);
        
        ByteBuffer byteBufI = ByteBuffer.allocateDirect(indCount * 2);
        byteBufI.order(ByteOrder.nativeOrder());
        ShortBuffer bufferI = byteBufI.asShortBuffer();
        
        for (short s : indices)
            bufferI.put(s);
        
        bufferI.position(0);
        
        //generate VBO
        int[] glBuffers = new int[2];
        gl.glGenBuffers(2, glBuffers, 0);
        vertBuffer = glBuffers[0];
        indBuffer = glBuffers[1];
        
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertBuffer);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, vertCount * 4, buffer, GL11.GL_STATIC_DRAW);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indBuffer);
        gl.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indCount * 2, bufferI, GL11.GL_STATIC_DRAW);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
    
    /**
     * Draws the tileset.
     * @param gl The OpenGL context to draw with.
     */
    public void draw(GL11 gl)
    {        
        gl.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTexture());
        
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertBuffer);
        gl.glVertexPointer(3, GL11.GL_FLOAT, 32, 0);
        gl.glTexCoordPointer(2, GL11.GL_FLOAT, 32, 12);
        gl.glNormalPointer(GL11.GL_FLOAT, 32, 20);
        
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indBuffer);
        gl.glDrawElements(GL11.GL_TRIANGLES, indCount, GL11.GL_UNSIGNED_SHORT, 0);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
        //gl.glDrawArrays(GL11.GL_TRIANGLES, 0, indCount);
        
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        gl.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
    
    /**
     * Gets the tile at a specified location.
     * @param x The X location of a tile.
     * @param y The Y location of a tile.
     * @return The tile at (x, y).
     */
    public Tile getTileAt(int x, int y)
    {
        if (y < 0 || y > tiles.length || x < 0 || x > tiles[0].length)
            return null;
        
        return tiles[y][x];
    }
    
    /**
     * Gets the number of tiles in the X direction.
     * @return The tileset width.
     */
    public int getWidth()
    {
        return tiles[0].length;
    }
    
    /**
     * Gets the number of tiles in the Y direction.
     * @return The tileset height.
     */
    public int getHeight()
    {
        return tiles.length;
    }
}
