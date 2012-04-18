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

package com.ltdev.cc.ui;

import android.util.Log;

import com.ltdev.cc.Game;
import com.ltdev.graphics.Texture;
import com.ltdev.graphics.TilesetHelper;
import com.ltdev.math.Matrix4;
import com.ltdev.math.Vector2;
import com.ltdev.math.Vector4;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.EnumSet;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

/**
 * Base class for the UI class tree.
 * @author Lightning Developemnt Studios
 * \todo allow relative sizing to scale for different monitors
 */
public abstract class Control
{    
    /**
     * An enum of different relative positions that a UI Control can be located at.
     * @author Lightning Development Studios
     */
    public enum UIPosition
    {
        TOP(0),
        LEFT(1),
        BOTTOM(2),
        RIGHT(3),
        CENTER(4),
        TOPLEFT(5),
        TOPRIGHT(6),
        BOTTOMLEFT(7),
        BOTTOMRIGHT(8);
        
        private int value;
        
        private UIPosition(int value)
        {
            this.value = value;
        }
        
        public int getValue()
        {
            return value;
        }
    }

    /**
     * clamped between -1 and 1, turns UIPosition enum to relative coords.
     */
	protected static final float[] UI_POSITION_F =
	{
	    0.0f, 1.0f,
		-1.0f, 0.0f,
		0.0f, -1.0f,
		1.0f, 0.0f,
		0.0f, 0.0f,
		-1.0f, 1.0f,
		1.0f, 1.0f,
		-1.0f, -1.0f,
		1.0f, -1.0f };
	
	protected Matrix4 model;
	
	//graphics data
	protected Vector2 size, pos, relativePos, halfSize;
	protected UIPosition position;
	protected Texture tex;
	
	protected float[] vertices;
	protected float[] texture;
	protected float[] color;
	
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer textureBuffer;
	protected FloatBuffer colorBuffer;
	
	protected int vboVertPtr, vboGradientPtr, vboTexturePtr;
	protected boolean needToUpdateTexVBO, needToUpdateGradientVBO, needToUpdateVertexVBO;
	
	private float topPad, leftPad, bottomPad, rightPad;
    private Vector4 colorVec;
	
	//constructors
	public Control(float xSize, float ySize, UIPosition position)
	{
		this(xSize, ySize, UI_POSITION_F[position.getValue() * 2], UI_POSITION_F[(position.getValue() * 2) + 1], 0, 0, 0, 0);
		this.position = position;
	}

	public Control(float xSize, float ySize, float xRelative, float yRelative) 
	{
		this(xSize, ySize, xRelative, yRelative, 0, 0, 0, 0);
	}
	
	public Control(float xSize, float ySize, UIPosition position, float topPad, float leftPad, float bottomPad, float rightPad)
	{
		this (xSize, ySize, UI_POSITION_F[position.getValue() * 2], UI_POSITION_F[(position.getValue() * 2) + 1], topPad, leftPad, bottomPad, rightPad);
		this.position = position;
	}
	
	public Control(float xSize, float ySize, float xRelative, float yRelative, float topPad, float leftPad, float bottomPad, float rightPad) 
	{
		this.size = new Vector2(xSize, ySize);
		this.relativePos = new Vector2(xRelative, yRelative);
		this.topPad = topPad;
		this.leftPad = leftPad;
		this.bottomPad = bottomPad;
		this.rightPad = rightPad;
		
		this.halfSize = Vector2.scale(size, 0.5f);
		this.colorVec = new Vector4(1, 1, 1, 1);
		
		float x = halfSize.x();
		float y = halfSize.y();
		
		float[] initVerts =
		{
		    x, y,
			x, -y,
			-x, y,
			-x, -y
		};
		
		float[] initTexcoords =
	    {
	        1, 1,
	        1, 0,
	        0, 1,
	        0, 0
	    };
		
		this.pos = new Vector2(0, 0);
		rebuildModelMatrix();
		
		this.vertices = initVerts;
		this.texture = initTexcoords;
		this.vertexBuffer = setBuffer(vertexBuffer, vertices);
		this.textureBuffer = setBuffer(textureBuffer, texture);
		needToUpdateVertexVBO = true;
		needToUpdateTexVBO = true;
	}
	
	public void draw(GL11 gl)
	{
	    gl.glMultMatrixf(model.array(), 0);
		gl.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTexture());
		gl.glColor4f(colorVec.x(), colorVec.y(), colorVec.z(), colorVec.w());

		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vboVertPtr);
		gl.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vboTexturePtr);
		gl.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);

		gl.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}
	
	public void update()
	{
	}
	
	public void touch(int x, int y)
	{
		
	}
	
	protected FloatBuffer setBuffer(FloatBuffer buffer, float[] values)
	{
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(values.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		buffer = byteBuf.asFloatBuffer();
		buffer.put(values);
		buffer.position(0);
		
		return buffer;
	}

	public void setPadding(float topPad, float leftPad, float bottomPad, float rightPad)
	{
		this.topPad = topPad;
		this.leftPad = leftPad;
		this.bottomPad = bottomPad;
		this.rightPad = rightPad;
	}
	
	public void autoPadding()
	{
		autoPadding(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	public void autoPadding(float topPad, float leftPad, float bottomPad, float rightPad)
	{
		if (position != null)
		{
			switch (position)
			{
				case TOP:
				case TOPLEFT:
				case TOPRIGHT:
					this.topPad = halfSize.y() + topPad;
					break;
            default:
                break;
			}
			
			switch (position)
			{
				case LEFT:
				case TOPLEFT:
				case BOTTOMLEFT:
					this.leftPad = halfSize.x() + leftPad;
					break;
				default:
				    break;
			}
			
			switch (position)
			{
				case BOTTOM:
				case BOTTOMLEFT:
				case BOTTOMRIGHT:
					this.bottomPad = halfSize.y() + bottomPad;
					break;
				default:
			}
			
			switch (position)
			{
				case RIGHT:
				case TOPRIGHT:
				case BOTTOMRIGHT:
					this.rightPad = halfSize.x() + rightPad;
					break;
				default:
			}
			updatePosition();
		}
		else
		{
			Log.w("Circuit Crawler", "Warning: Current UIEntity is not using positioning with UIPosition. No padding changes made!");
		}
	}
	
	public void updatePosition()
	{
	    setPos(new Vector2((Game.screenW / 2.0f * relativePos.x()) + leftPad - rightPad, (Game.screenH / 2 * relativePos.y()) + bottomPad - topPad));
	    rebuildModelMatrix();
	}
	
	public void rotateTilesetCoords()
	{
		float negX = texture[0];
		float negY = texture[1];
		float posX = texture[2];
		float posY = texture[5];
		
		float[] coords = 
		{
		    posX, negY,
			posX, posY,
			negX, negY,
			negX, posY 
		};
		
		this.texture = coords;
		textureBuffer = setBuffer(textureBuffer, texture);
	}

	public void genHardwareBuffers(GL11 gl)
	{		
		int[] tempPtr = new int[1];
		
		//VERTEX
		gl.glGenBuffers(1, tempPtr, 0);
		vboVertPtr = tempPtr[0];
		
		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vboVertPtr);
		final int vertSize = vertexBuffer.capacity() * 4;
		gl.glBufferData(GL11.GL_ARRAY_BUFFER, vertSize, vertexBuffer, GL11.GL_STATIC_DRAW); //\TODO choose static/draw settings..?

		gl.glGenBuffers(1, tempPtr, 0);
		vboTexturePtr = tempPtr[0];
		needToUpdateTexVBO = true;
		updateTextureVBO(gl);
		
		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
	}
	
	public void updateTextureVBO(GL11 gl)
	{
		if (needToUpdateTexVBO)
		{
		    if (vboTexturePtr == 0)
		    {
		        int[] values = new int[1];
		        gl.glGenBuffers(1, values, 0);
		        vboTexturePtr = values[0];
		    }
		    
			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vboTexturePtr);
			final int textureSize = textureBuffer.capacity() * 4;
			gl.glBufferData(GL11.GL_ARRAY_BUFFER, textureSize, textureBuffer, GL11.GL_STATIC_DRAW);
		}
		needToUpdateTexVBO = false;
	}
	
	public void updateGradientVBO(GL10 gl)
	{
		if (needToUpdateGradientVBO)
		{
			GL11 gl11 = (GL11)gl;
			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, vboGradientPtr);
			final int gradientSize = colorBuffer.capacity() * 4;
			gl11.glBufferData(GL11.GL_ARRAY_BUFFER, gradientSize, colorBuffer, GL11.GL_STATIC_DRAW);
		}
		needToUpdateGradientVBO = false;
	}

	public void updateVertexVBO(GL11 gl) 
	{
		if (needToUpdateVertexVBO)
		{
			gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vboVertPtr);
			final int vertSize = vertexBuffer.capacity() * 4;
			gl.glBufferData(GL11.GL_ARRAY_BUFFER, vertSize, vertexBuffer, GL11.GL_STATIC_DRAW); //TODO choose static/draw settings..?
		}
		needToUpdateVertexVBO = false;
	}

	public Vector2 getSize()
	{
	    return size;
	}
	
	public Vector2 getPos()
	{
	    return pos;
	}
	
	public Vector4 getColor()
	{
	    return colorVec;
	}
	
	public Texture getTexture()
	{
	    return tex;
	}
	
	public UIPosition getPosEnum()
	{
	    return position;
	}
	
	public float[] getVertices()
	{
	    return vertices;
	}
	
	public float[] getGradientCoords()
	{
	    return color;
	}
	
	public float[] getTextureCoords()
	{
	    return texture;
	}
	
	public void setSize(Vector2 size)
	{
	    this.size = size;
	}
	
	public void setPos(Vector2 pos)
	{
	    this.pos = pos;
	    rebuildModelMatrix();
	}
	
	public void setTopPad(float topPad)
	{
	    this.topPad = topPad;
	}
	
	public void setLeftPad(float leftPad)
	{
	    this.leftPad = leftPad;
	}
	
	public void setBottomPad(float bottomPad)
	{
	    this.bottomPad = bottomPad;
	}
	
	public void setRightPad(float rightPad)
	{
	    this.rightPad = rightPad;
	}
	
	public void setColor(Vector4 color)
	{
	    this.colorVec = color;
	}
	
	public void setTexture(Texture tex)
	{
	    this.tex = tex;
	}
	
	public void rebuildModelMatrix()
	{
	    model = Matrix4.translate(pos);
	}
}
