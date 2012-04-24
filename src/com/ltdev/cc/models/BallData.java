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

import javax.microedition.khronos.opengles.GL11;

/**
 * Automatically generated vertex data for model "Ball.obj".
 * @author Lightning Development Studios
 */
public final class BallData
{
    /**
     * The number of vertices in the array.
     */
    public static final int VERTEX_COUNT = 144;

    /**
     * The number of floats in the array.
     */
    public static final int VERTEX_FLOAT_COUNT = 1152;

    /**
     * The number of floats in the array.
     */
    public static final int VERTEX_BYTE_COUNT = 4608;

    /**
     * Vertex data.
     */
    public static final float[] VERTICES =
    {
        7.999999f, -7.999999f, 4.686292f, 	0f, 0.75f, 	0.589656f, -0.475797f, -0.652628f,
        0f, -11.31371f, 4.686292f, 	0.125f, 0.75f, 	0.08051f, -0.753389f, -0.652628f,
        11.31371f, -11.31371f, 16f, 	0f, 0.5f, 	0.707107f, -0.707107f, 0f,
        11.31371f, -11.31371f, 16f, 	0f, 0.5f, 	0.707107f, -0.707107f, 0f,
        0f, -11.31371f, 4.686292f, 	0.125f, 0.75f, 	0.08051f, -0.753389f, -0.652628f,
        0f, -16f, 16f, 	0.125f, 0.5f, 	0f, -1f, 0f,
        0f, -11.31371f, 4.686292f, 	0.125f, 0.75f, 	0.08051f, -0.753389f, -0.652628f,
        -7.999999f, -7.999999f, 4.686292f, 	0.25f, 0.75f, 	-0.475797f, -0.589656f, -0.652628f,
        0f, -16f, 16f, 	0.125f, 0.5f, 	0f, -1f, 0f,
        0f, -16f, 16f, 	0.125f, 0.5f, 	0f, -1f, 0f,
        -7.999999f, -7.999999f, 4.686292f, 	0.25f, 0.75f, 	-0.475797f, -0.589656f, -0.652628f,
        -11.31371f, -11.31371f, 16f, 	0.25f, 0.5f, 	-0.707107f, -0.707107f, 0f,
        -7.999999f, -7.999999f, 4.686292f, 	0.25f, 0.75f, 	-0.475797f, -0.589656f, -0.652628f,
        -11.31371f, 0f, 4.686292f, 	0.375f, 0.75f, 	-0.753389f, -0.08051f, -0.652628f,
        -11.31371f, -11.31371f, 16f, 	0.25f, 0.5f, 	-0.707107f, -0.707107f, 0f,
        -11.31371f, -11.31371f, 16f, 	0.25f, 0.5f, 	-0.707107f, -0.707107f, 0f,
        -11.31371f, 0f, 4.686292f, 	0.375f, 0.75f, 	-0.753389f, -0.08051f, -0.652628f,
        -16f, 0f, 16f, 	0.375f, 0.5f, 	-1f, 0f, 0f,
        -11.31371f, 0f, 4.686292f, 	0.375f, 0.75f, 	-0.753389f, -0.08051f, -0.652628f,
        -7.999999f, 7.999999f, 4.686292f, 	0.5f, 0.75f, 	-0.589656f, 0.475797f, -0.652628f,
        -16f, 0f, 16f, 	0.375f, 0.5f, 	-1f, 0f, 0f,
        -16f, 0f, 16f, 	0.375f, 0.5f, 	-1f, 0f, 0f,
        -7.999999f, 7.999999f, 4.686292f, 	0.5f, 0.75f, 	-0.589656f, 0.475797f, -0.652628f,
        -11.31371f, 11.31371f, 16f, 	0.5f, 0.5f, 	-0.707107f, 0.707107f, 0f,
        -7.999999f, 7.999999f, 4.686292f, 	0.5f, 0.75f, 	-0.589656f, 0.475797f, -0.652628f,
        0f, 11.31371f, 4.686292f, 	0.625f, 0.75f, 	-0.08051f, 0.753389f, -0.652628f,
        -11.31371f, 11.31371f, 16f, 	0.5f, 0.5f, 	-0.707107f, 0.707107f, 0f,
        -11.31371f, 11.31371f, 16f, 	0.5f, 0.5f, 	-0.707107f, 0.707107f, 0f,
        0f, 11.31371f, 4.686292f, 	0.625f, 0.75f, 	-0.08051f, 0.753389f, -0.652628f,
        0f, 16f, 16f, 	0.625f, 0.5f, 	0f, 1f, 0f,
        0f, 11.31371f, 4.686292f, 	0.625f, 0.75f, 	-0.08051f, 0.753389f, -0.652628f,
        8f, 8f, 4.686292f, 	0.75f, 0.75f, 	0.475797f, 0.589656f, -0.652628f,
        0f, 16f, 16f, 	0.625f, 0.5f, 	0f, 1f, 0f,
        0f, 16f, 16f, 	0.625f, 0.5f, 	0f, 1f, 0f,
        8f, 8f, 4.686292f, 	0.75f, 0.75f, 	0.475797f, 0.589656f, -0.652628f,
        11.31371f, 11.31371f, 16f, 	0.75f, 0.5f, 	0.707107f, 0.707107f, 0f,
        8f, 8f, 4.686292f, 	0.75f, 0.75f, 	0.475797f, 0.589656f, -0.652628f,
        11.31371f, 0f, 4.686292f, 	0.875f, 0.75f, 	0.753389f, 0.08051f, -0.652628f,
        11.31371f, 11.31371f, 16f, 	0.75f, 0.5f, 	0.707107f, 0.707107f, 0f,
        11.31371f, 11.31371f, 16f, 	0.75f, 0.5f, 	0.707107f, 0.707107f, 0f,
        11.31371f, 0f, 4.686292f, 	0.875f, 0.75f, 	0.753389f, 0.08051f, -0.652628f,
        16f, 0f, 16f, 	0.875f, 0.5f, 	1f, 0f, 0f,
        11.31371f, 0f, 4.686292f, 	0.875f, 0.75f, 	0.753389f, 0.08051f, -0.652628f,
        7.999999f, -7.999999f, 4.686292f, 	1f, 0.75f, 	0.589656f, -0.475797f, -0.652628f,
        16f, 0f, 16f, 	0.875f, 0.5f, 	1f, 0f, 0f,
        16f, 0f, 16f, 	0.875f, 0.5f, 	1f, 0f, 0f,
        7.999999f, -7.999999f, 4.686292f, 	1f, 0.75f, 	0.589656f, -0.475797f, -0.652628f,
        11.31371f, -11.31371f, 16f, 	1f, 0.5f, 	0.707107f, -0.707107f, 0f,
        11.31371f, -11.31371f, 16f, 	0f, 0.5f, 	0.707107f, -0.707107f, 0f,
        0f, -16f, 16f, 	0.125f, 0.5f, 	0f, -1f, 0f,
        7.999999f, -7.999999f, 27.31371f, 	0f, 0.25f, 	0.475797f, -0.589656f, 0.652628f,
        7.999999f, -7.999999f, 27.31371f, 	0f, 0.25f, 	0.475797f, -0.589656f, 0.652628f,
        0f, -16f, 16f, 	0.125f, 0.5f, 	0f, -1f, 0f,
        0f, -11.31371f, 27.31371f, 	0.125f, 0.25f, 	-0.08051f, -0.753389f, 0.652628f,
        0f, -16f, 16f, 	0.125f, 0.5f, 	0f, -1f, 0f,
        -11.31371f, -11.31371f, 16f, 	0.25f, 0.5f, 	-0.707107f, -0.707107f, 0f,
        0f, -11.31371f, 27.31371f, 	0.125f, 0.25f, 	-0.08051f, -0.753389f, 0.652628f,
        0f, -11.31371f, 27.31371f, 	0.125f, 0.25f, 	-0.08051f, -0.753389f, 0.652628f,
        -11.31371f, -11.31371f, 16f, 	0.25f, 0.5f, 	-0.707107f, -0.707107f, 0f,
        -7.999999f, -7.999999f, 27.31371f, 	0.25f, 0.25f, 	-0.589656f, -0.475797f, 0.652628f,
        -11.31371f, -11.31371f, 16f, 	0.25f, 0.5f, 	-0.707107f, -0.707107f, 0f,
        -16f, 0f, 16f, 	0.375f, 0.5f, 	-1f, 0f, 0f,
        -7.999999f, -7.999999f, 27.31371f, 	0.25f, 0.25f, 	-0.589656f, -0.475797f, 0.652628f,
        -7.999999f, -7.999999f, 27.31371f, 	0.25f, 0.25f, 	-0.589656f, -0.475797f, 0.652628f,
        -16f, 0f, 16f, 	0.375f, 0.5f, 	-1f, 0f, 0f,
        -11.31371f, 0f, 27.31371f, 	0.375f, 0.25f, 	-0.753389f, 0.08051f, 0.652628f,
        -16f, 0f, 16f, 	0.375f, 0.5f, 	-1f, 0f, 0f,
        -11.31371f, 11.31371f, 16f, 	0.5f, 0.5f, 	-0.707107f, 0.707107f, 0f,
        -11.31371f, 0f, 27.31371f, 	0.375f, 0.25f, 	-0.753389f, 0.08051f, 0.652628f,
        -11.31371f, 0f, 27.31371f, 	0.375f, 0.25f, 	-0.753389f, 0.08051f, 0.652628f,
        -11.31371f, 11.31371f, 16f, 	0.5f, 0.5f, 	-0.707107f, 0.707107f, 0f,
        -7.999999f, 7.999999f, 27.31371f, 	0.5f, 0.25f, 	-0.475797f, 0.589656f, 0.652628f,
        -11.31371f, 11.31371f, 16f, 	0.5f, 0.5f, 	-0.707107f, 0.707107f, 0f,
        0f, 16f, 16f, 	0.625f, 0.5f, 	0f, 1f, 0f,
        -7.999999f, 7.999999f, 27.31371f, 	0.5f, 0.25f, 	-0.475797f, 0.589656f, 0.652628f,
        -7.999999f, 7.999999f, 27.31371f, 	0.5f, 0.25f, 	-0.475797f, 0.589656f, 0.652628f,
        0f, 16f, 16f, 	0.625f, 0.5f, 	0f, 1f, 0f,
        0f, 11.31371f, 27.31371f, 	0.625f, 0.25f, 	0.08051f, 0.753389f, 0.652628f,
        0f, 16f, 16f, 	0.625f, 0.5f, 	0f, 1f, 0f,
        11.31371f, 11.31371f, 16f, 	0.75f, 0.5f, 	0.707107f, 0.707107f, 0f,
        0f, 11.31371f, 27.31371f, 	0.625f, 0.25f, 	0.08051f, 0.753389f, 0.652628f,
        0f, 11.31371f, 27.31371f, 	0.625f, 0.25f, 	0.08051f, 0.753389f, 0.652628f,
        11.31371f, 11.31371f, 16f, 	0.75f, 0.5f, 	0.707107f, 0.707107f, 0f,
        8f, 8f, 27.31371f, 	0.75f, 0.25f, 	0.589656f, 0.475797f, 0.652628f,
        11.31371f, 11.31371f, 16f, 	0.75f, 0.5f, 	0.707107f, 0.707107f, 0f,
        16f, 0f, 16f, 	0.875f, 0.5f, 	1f, 0f, 0f,
        8f, 8f, 27.31371f, 	0.75f, 0.25f, 	0.589656f, 0.475797f, 0.652628f,
        8f, 8f, 27.31371f, 	0.75f, 0.25f, 	0.589656f, 0.475797f, 0.652628f,
        16f, 0f, 16f, 	0.875f, 0.5f, 	1f, 0f, 0f,
        11.31371f, 0f, 27.31371f, 	0.875f, 0.25f, 	0.753389f, -0.08051f, 0.652628f,
        16f, 0f, 16f, 	0.875f, 0.5f, 	1f, 0f, 0f,
        11.31371f, -11.31371f, 16f, 	1f, 0.5f, 	0.707107f, -0.707107f, 0f,
        11.31371f, 0f, 27.31371f, 	0.875f, 0.25f, 	0.753389f, -0.08051f, 0.652628f,
        11.31371f, 0f, 27.31371f, 	0.875f, 0.25f, 	0.753389f, -0.08051f, 0.652628f,
        11.31371f, -11.31371f, 16f, 	1f, 0.5f, 	0.707107f, -0.707107f, 0f,
        7.999999f, -7.999999f, 27.31371f, 	1f, 0.25f, 	0.475797f, -0.589656f, 0.652628f,
        0f, -11.31371f, 4.686292f, 	0.125f, 0.75f, 	0.08051f, -0.753389f, -0.652628f,
        7.999999f, -7.999999f, 4.686292f, 	0f, 0.75f, 	0.589656f, -0.475797f, -0.652628f,
        0f, 0f, 0f, 	0.0625f, 1f, 	0f, 0f, -1f,
        -7.999999f, -7.999999f, 4.686292f, 	0.25f, 0.75f, 	-0.475797f, -0.589656f, -0.652628f,
        0f, -11.31371f, 4.686292f, 	0.125f, 0.75f, 	0.08051f, -0.753389f, -0.652628f,
        0f, 0f, 0f, 	0.1875f, 1f, 	0f, 0f, -1f,
        -11.31371f, 0f, 4.686292f, 	0.375f, 0.75f, 	-0.753389f, -0.08051f, -0.652628f,
        -7.999999f, -7.999999f, 4.686292f, 	0.25f, 0.75f, 	-0.475797f, -0.589656f, -0.652628f,
        0f, 0f, 0f, 	0.3125f, 1f, 	0f, 0f, -1f,
        -7.999999f, 7.999999f, 4.686292f, 	0.5f, 0.75f, 	-0.589656f, 0.475797f, -0.652628f,
        -11.31371f, 0f, 4.686292f, 	0.375f, 0.75f, 	-0.753389f, -0.08051f, -0.652628f,
        0f, 0f, 0f, 	0.4375f, 1f, 	0f, 0f, -1f,
        0f, 11.31371f, 4.686292f, 	0.625f, 0.75f, 	-0.08051f, 0.753389f, -0.652628f,
        -7.999999f, 7.999999f, 4.686292f, 	0.5f, 0.75f, 	-0.589656f, 0.475797f, -0.652628f,
        0f, 0f, 0f, 	0.5625f, 1f, 	0f, 0f, -1f,
        8f, 8f, 4.686292f, 	0.75f, 0.75f, 	0.475797f, 0.589656f, -0.652628f,
        0f, 11.31371f, 4.686292f, 	0.625f, 0.75f, 	-0.08051f, 0.753389f, -0.652628f,
        0f, 0f, 0f, 	0.6875f, 1f, 	0f, 0f, -1f,
        11.31371f, 0f, 4.686292f, 	0.875f, 0.75f, 	0.753389f, 0.08051f, -0.652628f,
        8f, 8f, 4.686292f, 	0.75f, 0.75f, 	0.475797f, 0.589656f, -0.652628f,
        0f, 0f, 0f, 	0.8125f, 1f, 	0f, 0f, -1f,
        7.999999f, -7.999999f, 4.686292f, 	1f, 0.75f, 	0.589656f, -0.475797f, -0.652628f,
        11.31371f, 0f, 4.686292f, 	0.875f, 0.75f, 	0.753389f, 0.08051f, -0.652628f,
        0f, 0f, 0f, 	0.9375f, 1f, 	0f, 0f, -1f,
        7.999999f, -7.999999f, 27.31371f, 	0f, 0.25f, 	0.475797f, -0.589656f, 0.652628f,
        0f, -11.31371f, 27.31371f, 	0.125f, 0.25f, 	-0.08051f, -0.753389f, 0.652628f,
        0f, 0f, 32f, 	0.0625f, 0f, 	0f, 0f, 1f,
        0f, -11.31371f, 27.31371f, 	0.125f, 0.25f, 	-0.08051f, -0.753389f, 0.652628f,
        -7.999999f, -7.999999f, 27.31371f, 	0.25f, 0.25f, 	-0.589656f, -0.475797f, 0.652628f,
        0f, 0f, 32f, 	0.1875f, 0f, 	0f, 0f, 1f,
        -7.999999f, -7.999999f, 27.31371f, 	0.25f, 0.25f, 	-0.589656f, -0.475797f, 0.652628f,
        -11.31371f, 0f, 27.31371f, 	0.375f, 0.25f, 	-0.753389f, 0.08051f, 0.652628f,
        0f, 0f, 32f, 	0.3125f, 0f, 	0f, 0f, 1f,
        -11.31371f, 0f, 27.31371f, 	0.375f, 0.25f, 	-0.753389f, 0.08051f, 0.652628f,
        -7.999999f, 7.999999f, 27.31371f, 	0.5f, 0.25f, 	-0.475797f, 0.589656f, 0.652628f,
        0f, 0f, 32f, 	0.4375f, 0f, 	0f, 0f, 1f,
        -7.999999f, 7.999999f, 27.31371f, 	0.5f, 0.25f, 	-0.475797f, 0.589656f, 0.652628f,
        0f, 11.31371f, 27.31371f, 	0.625f, 0.25f, 	0.08051f, 0.753389f, 0.652628f,
        0f, 0f, 32f, 	0.5625f, 0f, 	0f, 0f, 1f,
        0f, 11.31371f, 27.31371f, 	0.625f, 0.25f, 	0.08051f, 0.753389f, 0.652628f,
        8f, 8f, 27.31371f, 	0.75f, 0.25f, 	0.589656f, 0.475797f, 0.652628f,
        0f, 0f, 32f, 	0.6875f, 0f, 	0f, 0f, 1f,
        8f, 8f, 27.31371f, 	0.75f, 0.25f, 	0.589656f, 0.475797f, 0.652628f,
        11.31371f, 0f, 27.31371f, 	0.875f, 0.25f, 	0.753389f, -0.08051f, 0.652628f,
        0f, 0f, 32f, 	0.8125f, 0f, 	0f, 0f, 1f,
        11.31371f, 0f, 27.31371f, 	0.875f, 0.25f, 	0.753389f, -0.08051f, 0.652628f,
        7.999999f, -7.999999f, 27.31371f, 	1f, 0.25f, 	0.475797f, -0.589656f, 0.652628f,
        0f, 0f, 32f, 	0.9375f, 0f, 	0f, 0f, 1f,
    };

    /**
     * A VBO handle.
     */
    private static int vbo;

    /**
     * Prevents initialization of BallData.
     */
    private BallData()
    {
    }

    /**
     * Gets a VBO that contains the vertex data for the model "Ball.obj".
     * @param gl The OpenGL context.
     * @return A VBO handle.
     */
    public static int getBufferId(GL11 gl)
    {
        if (vbo == 0)
        {
            ByteBuffer byteBuf = ByteBuffer.allocateDirect(VERTEX_BYTE_COUNT);
            byteBuf.order(ByteOrder.nativeOrder());
            FloatBuffer buffer = byteBuf.asFloatBuffer();
            buffer.put(VERTICES);
            buffer.position(0);

            //generate a VBO.
            int[] tempPtr = new int[1];
            gl.glGenBuffers(1, tempPtr, 0);
            vbo = tempPtr[0];

            //send data to GPU.
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo);
            gl.glBufferData(GL11.GL_ARRAY_BUFFER, VERTEX_BYTE_COUNT, buffer, GL11.GL_STATIC_DRAW);
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        }

        return vbo;
    }

    /**
     * Unloads the model data from the VBO.
     * @param gl The OpenGL context.
     */
    public static void unload(GL11 gl)
    {
        if (vbo != 0)
        {
            int[] buffers = { vbo };
            gl.glDeleteBuffers(1, buffers, 0);
            vbo = 0;
        }
    }
}
