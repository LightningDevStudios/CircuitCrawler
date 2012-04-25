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

package com.ltdev.cc.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL11;

/**
 * Automatically generated vertex data for model "Block.obj".
 * @author Lightning Development Studios
 */
public final class BlockData
{
    /**
     * The number of vertices in the array.
     */
    public static final int VERTEX_COUNT = 24;

    /**
     * The number of floats in the array.
     */
    public static final int VERTEX_FLOAT_COUNT = 192;

    /**
     * The number of floats in the array.
     */
    public static final int VERTEX_BYTE_COUNT = 768;

    /**
     * Vertex data.
     */
    public static final float[] VERTICES =
    {
        -25f, 25f, 0f, 0f, 1f, 0f, 1f, 0f,
        25f, 25f, 0f, 1f, 1f, 0f, 1f, 0f,
        -25f, 25f, 50f, 0f, 0f, 0f, 1f, 0f,
        25f, 25f, 50f, 1f, 0f, 0f, 1f, 0f,
        -25f, 25f, 50f, 0f, 1f, 0f, 0f, 1f,
        25f, 25f, 50f, 1f, 1f, 0f, 0f, 1f,
        -25f, -25f, 50f, 0f, 0f, 0f, 0f, 1f,
        25f, -25f, 50f, 1f, 0f, 0f, 0f, 1f,
        -25f, -25f, 50f, 0f, 0f, 0f, -1f, 0f,
        25f, -25f, 50f, 1f, 0f, 0f, -1f, 0f,
        -25f, -25f, 0f, 0f, 1f, 0f, -1f, 0f,
        25f, -25f, 0f, 1f, 1f, 0f, -1f, 0f,
        -25f, -25f, 0f, 0f, 0f, 0f, 0f, -1f,
        25f, -25f, 0f, 1f, 0f, 0f, 0f, -1f,
        -25f, 25f, 0f, 0f, 1f, 0f, 0f, -1f,
        25f, 25f, 0f, 1f, 1f, 0f, 0f, -1f,
        25f, 25f, 0f, 0f, 1f, 1f, 0f, 0f,
        25f, -25f, 0f, 1f, 1f, 1f, 0f, 0f,
        25f, 25f, 50f, 0f, 0f, 1f, 0f, 0f,
        25f, -25f, 50f, 1f, 0f, 1f, 0f, 0f,
        -25f, -25f, 0f, 1f, 1f, -1f, 0f, 0f,
        -25f, 25f, 0f, 0f, 1f, -1f, 0f, 0f,
        -25f, -25f, 50f, 1f, 0f, -1f, 0f, 0f,
        -25f, 25f, 50f, 0f, 0f, -1f, 0f, 0f,
    };

    /**
     * The number of vertices in the array.
     */
    public static final int INDEX_COUNT = 36;

    /**
     * The number of floats in the array.
     */
    public static final int INDEX_BYTE_COUNT = 72;

    /**
     * Vertex data.
     */
    public static final short[] INDICES =
    {
        0, 1, 2, 
        2, 1, 3, 
        4, 5, 6, 
        6, 5, 7, 
        8, 9, 10, 
        10, 9, 11, 
        12, 13, 14, 
        14, 13, 15, 
        16, 17, 18, 
        18, 17, 19, 
        20, 21, 22, 
        22, 21, 23, 
    };
    /**
     * A VBO handle for vertices.
     */
    private static int vertVbo;

    /**
     * A VBO handle for vertices.
     */
    private static int indVbo;

    /**
     * Prevents initialization of BlockData.
     */
    private BlockData()
    {
    }

    /**
     * Gets a VBO that contains the vertex data for the model "Block.obj".
     * @param gl The OpenGL context.
     * @return A VBO handle.
     */
    public static int getVertexBufferId(GL11 gl)
    {
        if (vertVbo == 0)
        {
            ByteBuffer byteBuf = ByteBuffer.allocateDirect(VERTEX_BYTE_COUNT);
            byteBuf.order(ByteOrder.nativeOrder());
            FloatBuffer buffer = byteBuf.asFloatBuffer();
            buffer.put(VERTICES);
            buffer.position(0);

            //generate a VBO.
            int[] tempPtr = new int[1];
            gl.glGenBuffers(1, tempPtr, 0);
            vertVbo = tempPtr[0];

            //send data to GPU.
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertVbo);
            gl.glBufferData(GL11.GL_ARRAY_BUFFER, VERTEX_BYTE_COUNT, buffer, GL11.GL_STATIC_DRAW);
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        }

        return vertVbo;
    }

    /**
     * Gets a VBO that contains the index data for the model "Block.obj".
     * @param gl The OpenGL context.
     * @return A VBO handle.
     */
    public static int getIndexBufferId(GL11 gl)
    {
        if (indVbo == 0)
        {
            ByteBuffer byteBuf = ByteBuffer.allocateDirect(INDEX_BYTE_COUNT);
            byteBuf.order(ByteOrder.nativeOrder());
            ShortBuffer buffer = byteBuf.asShortBuffer();
            buffer.put(INDICES);
            buffer.position(0);

            //generate a VBO.
            int[] tempPtr = new int[1];
            gl.glGenBuffers(1, tempPtr, 0);
            indVbo = tempPtr[0];

            //send data to GPU.
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, indVbo);
            gl.glBufferData(GL11.GL_ARRAY_BUFFER, INDEX_BYTE_COUNT, buffer, GL11.GL_STATIC_DRAW);
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        }

        return indVbo;
    }

    /**
     * Unloads the model data from the VBO.
     * @param gl The OpenGL context.
     */
    public static void unload(GL11 gl)
    {
        /*if (vertVbo != 0)
        {
            int[] buffers = { vertVbo };
            gl.glDeleteBuffers(1, buffers, 0);
            vertVbo = 0;
        }

        if (indVbo != 0)
        {
            int[] buffers = { indVbo };
            gl.glDeleteBuffers(1, buffers, 0);
            indVbo = 0;
        }*/
        
        vertVbo = 0;
        indVbo = 0;
    }
}
