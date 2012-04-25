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
 * Automatically generated vertex data for model "ButtonDown.obj".
 * @author Lightning Development Studios
 */
public final class ButtonDownData
{
    /**
     * The number of vertices in the array.
     */
    public static final int VERTEX_COUNT = 56;

    /**
     * The number of floats in the array.
     */
    public static final int VERTEX_FLOAT_COUNT = 448;

    /**
     * The number of floats in the array.
     */
    public static final int VERTEX_BYTE_COUNT = 1792;

    /**
     * Vertex data.
     */
    public static final float[] VERTICES =
    {
        24.59963f, -24.59964f, 0.047122f, 0.971218f, 0.184791f, 0.382684f, -0.923879f, 0f,
        1.5E-05f, -34.78914f, 0.047122f, 0.971218f, 0.08051997f, 0.382684f, -0.923879f, 0f,
        24.59963f, -24.59964f, 5.997374f, 0.924489f, 0.184791f, 0.382684f, -0.923879f, 0f,
        1.5E-05f, -34.78914f, 5.997374f, 0.924489f, 0.08051902f, 0.382683f, -0.92388f, 0f,
        1.5E-05f, -34.78914f, 0.047122f, 0.971218f, 0.914691f, -0.382683f, -0.92388f, 0f,
        -24.5996f, -24.59964f, 0.047122f, 0.971218f, 0.810419f, -0.382684f, -0.923879f, 0f,
        1.5E-05f, -34.78914f, 5.997374f, 0.924489f, 0.914691f, -0.382684f, -0.923879f, 0f,
        -24.5996f, -24.59964f, 5.997374f, 0.924489f, 0.810419f, -0.382684f, -0.923879f, 0f,
        -24.5996f, -24.59964f, 0.047122f, 0.971218f, 0.810419f, -0.92388f, -0.382683f, 0f,
        -34.7891f, -2.1E-05f, 0.047122f, 0.971218f, 0.706148f, -0.92388f, -0.382683f, 0f,
        -24.5996f, -24.59964f, 5.997374f, 0.924489f, 0.810419f, -0.92388f, -0.382683f, 0f,
        -34.7891f, -2.1E-05f, 5.997374f, 0.924489f, 0.706148f, -0.92388f, -0.382683f, 0f,
        -34.7891f, -2.1E-05f, 0.047122f, 0.971218f, 0.706148f, -0.92388f, 0.382683f, 0f,
        -24.5996f, 24.5996f, 0.047122f, 0.971218f, 0.601876f, -0.92388f, 0.382683f, 0f,
        -34.7891f, -2.1E-05f, 5.997374f, 0.924489f, 0.706148f, -0.92388f, 0.382683f, 0f,
        -24.5996f, 24.5996f, 5.997374f, 0.924489f, 0.601876f, -0.92388f, 0.382683f, 0f,
        -24.5996f, 24.5996f, 0.047122f, 0.971218f, 0.601876f, -0.382684f, 0.923879f, 0f,
        1.5E-05f, 34.7891f, 0.047122f, 0.971218f, 0.497605f, -0.382684f, 0.923879f, 0f,
        -24.5996f, 24.5996f, 5.997374f, 0.924489f, 0.601876f, -0.382684f, 0.923879f, 0f,
        1.5E-05f, 34.7891f, 5.997374f, 0.924489f, 0.497605f, -0.382683f, 0.92388f, 0f,
        1.5E-05f, 34.7891f, 0.047122f, 0.971218f, 0.497605f, 0.382683f, 0.92388f, 0f,
        24.59964f, 24.5996f, 0.047122f, 0.971218f, 0.393334f, 0.382683f, 0.92388f, 0f,
        1.5E-05f, 34.7891f, 5.997374f, 0.924489f, 0.497605f, 0.382683f, 0.92388f, 0f,
        24.59964f, 24.5996f, 5.997374f, 0.924489f, 0.393334f, 0.382684f, 0.923879f, 0f,
        24.59964f, 24.5996f, 0.047122f, 0.971218f, 0.393334f, 0.92388f, 0.382683f, 0f,
        34.78913f, -2.1E-05f, 0.047122f, 0.971218f, 0.289062f, 0.92388f, 0.382683f, 0f,
        24.59964f, 24.5996f, 5.997374f, 0.924489f, 0.393334f, 0.92388f, 0.382683f, 0f,
        34.78913f, -2.1E-05f, 5.997374f, 0.924489f, 0.289062f, 0.92388f, 0.382683f, 0f,
        34.78913f, -2.1E-05f, 0.047122f, 0.971218f, 0.289062f, 0.923879f, -0.382684f, 0f,
        24.59963f, -24.59964f, 0.047122f, 0.971218f, 0.184791f, 0.923879f, -0.382684f, 0f,
        34.78913f, -2.1E-05f, 5.997374f, 0.924489f, 0.289062f, 0.923879f, -0.382684f, 0f,
        24.59963f, -24.59964f, 5.997374f, 0.924489f, 0.184791f, 0.923879f, -0.382684f, 0f,
        -20.3082f, -20.30817f, 10.8888f, 0.214344f, 0.305535f, 0f, 0f, 1f,
        -28.72014f, 4.1E-05f, 10.8888f, 0.114776f, 0.545913f, 0f, 0f, 1f,
        1.5E-05f, -28.72239f, 10.8888f, 0.454721f, 0.205914f, 0f, 0f, 1f,
        -20.30819f, 20.30825f, 10.8888f, 0.214344f, 0.78629f, 0f, 0f, 1f,
        1.6E-05f, 28.7202f, 10.8888f, 0.454721f, 0.885857f, 0f, 0f, 1f,
        20.30823f, 20.30825f, 10.8888f, 0.695098f, 0.78629f, 0f, 0f, 1f,
        28.72017f, 4.1E-05f, 10.8888f, 0.794666f, 0.545913f, 0f, 0f, 1f,
        20.30823f, -20.30817f, 10.8888f, 0.695098f, 0.305535f, 0f, 0f, 1f,
        24.59963f, -24.59964f, 5.997374f, 0.753677f, 0.246957f, 0.503108f, -0.381033f, 0.775691f,
        1.5E-05f, -34.78914f, 5.997374f, 0.454721f, 0.123125f, 0.086337f, -0.625275f, 0.775614f,
        20.30823f, -20.30817f, 10.8888f, 0.734599f, 0.266035f, 0.381069f, -0.503152f, 0.775645f,
        1.5E-05f, -28.72239f, 10.8888f, 0.454721f, 0.150105f, -0.08631f, -0.625266f, 0.775624f,
        -24.5996f, -24.59964f, 5.997374f, 0.155765f, 0.246957f, -0.381065f, -0.503141f, 0.775653f,
        -20.3082f, -20.30817f, 10.8888f, 0.174843f, 0.266035f, -0.503123f, -0.381016f, 0.775689f,
        -34.7891f, -2.1E-05f, 5.997374f, 0.031933f, 0.545913f, -0.625184f, -0.086319f, 0.77569f,
        -28.72014f, 4.1E-05f, 10.8888f, 0.058914f, 0.545913f, -0.625185f, 0.086321f, 0.775689f,
        -24.5996f, 24.5996f, 5.997374f, 0.155765f, 0.844869f, -0.503111f, 0.381036f, 0.775687f,
        -20.30819f, 20.30825f, 10.8888f, 0.174843f, 0.825791f, -0.381036f, 0.503112f, 0.775687f,
        1.5E-05f, 34.7891f, 5.997374f, 0.454721f, 0.968701f, -0.086321f, 0.625188f, 0.775686f,
        1.6E-05f, 28.7202f, 10.8888f, 0.454721f, 0.94172f, 0.08632f, 0.625188f, 0.775686f,
        24.59964f, 24.5996f, 5.997374f, 0.753677f, 0.844869f, 0.381036f, 0.503112f, 0.775686f,
        20.30823f, 20.30825f, 10.8888f, 0.734599f, 0.825791f, 0.503111f, 0.381036f, 0.775687f,
        34.78913f, -2.1E-05f, 5.997374f, 0.877509f, 0.545913f, 0.625185f, 0.086321f, 0.775689f,
        28.72017f, 4.1E-05f, 10.8888f, 0.850528f, 0.545913f, 0.625184f, -0.086319f, 0.77569f,
    };

    /**
     * The number of vertices in the array.
     */
    public static final int INDEX_COUNT = 114;

    /**
     * The number of floats in the array.
     */
    public static final int INDEX_BYTE_COUNT = 228;

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
        24, 25, 26, 
        26, 25, 27, 
        28, 29, 30, 
        30, 29, 31, 
        32, 33, 34, 
        33, 35, 34, 
        35, 36, 34, 
        36, 37, 34, 
        37, 38, 34, 
        38, 39, 34, 
        40, 41, 42, 
        42, 41, 43, 
        41, 44, 43, 
        43, 44, 45, 
        44, 46, 45, 
        45, 46, 47, 
        46, 48, 47, 
        47, 48, 49, 
        48, 50, 49, 
        49, 50, 51, 
        50, 52, 51, 
        51, 52, 53, 
        52, 54, 53, 
        53, 54, 55, 
        54, 40, 55, 
        55, 40, 42, 
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
     * Prevents initialization of ButtonDownData.
     */
    private ButtonDownData()
    {
    }

    /**
     * Gets a VBO that contains the vertex data for the model "ButtonDown.obj".
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
     * Gets a VBO that contains the index data for the model "ButtonDown.obj".
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
