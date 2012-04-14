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

package com.ltdev.cc.puzzles.circuit;

import android.graphics.Point;

import com.ltdev.Direction;
import com.ltdev.graphics.Texture;
import com.ltdev.graphics.TilesetHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Tile 
{
	public static float tileSize;
	
	private TileType type;
	private boolean selected;
	private boolean highlighted;
	private boolean powered;
	
	private float xPos, yPos;
	private int tileState;
	
	private Texture tex;
	
	private float[] vertices;
	private float[] texture;
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private ByteBuffer indexBuffer;
	
	/**
	 * Initializes a new instance of the Tile class.
	 * @param xPos The tile's X position.
	 * @param yPos The tile's Y position.
	 * @param tex The tile's texture.
	 * @param type The tile's type.
	 */
	public Tile(float xPos, float yPos, Texture tex, TileType type)
	{
		selected = false;
		highlighted = false;
		powered = false;
		
		this.type = type;
		this.xPos = xPos;
		this.yPos = yPos;
		this.tex = tex;
		this.tileState = 0;
		
		float halfSize = tileSize / 2;
		
		float[] initVerts =
		{
		    halfSize, halfSize,
			halfSize, -halfSize,
			-halfSize, halfSize,
			-halfSize, -halfSize
		};
		
		this.vertices = initVerts;
		this.vertexBuffer = setBuffer(vertexBuffer, vertices);
		
		updateTexture();
				
		byte[] indices = 
		{
		    0, 1, 2, 3
		};
		
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}
	
	/**
	 * Draws the tile.
	 * @param gl The OpenGL context.
	 */
	public void draw(GL10 gl)
	{
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex.getTexture());
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glDisable(GL10.GL_TEXTURE_2D);
		//gl.glDisable(GL10.GL_CULL_FACE);
	}

	public FloatBuffer setBuffer(FloatBuffer buffer, float[] array)
	{
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(array.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		buffer = byteBuf.asFloatBuffer();
		buffer.put(array);
		buffer.position(0);
		
		return buffer;
	}
		
	public static Direction flipDirection(Direction dir)
	{
		switch (dir)
		{
			case UP:
				return Direction.DOWN;
			case DOWN:
				return Direction.UP;
			case LEFT:
				return Direction.RIGHT;
			case RIGHT:
				return Direction.LEFT;
			default:
				return Direction.UP;
		}
	}
	
	public static int moveXPos(int x, Direction dir)
	{
		if (dir == Direction.LEFT)
			return x - 1;
		else if (dir == Direction.RIGHT)
			return x + 1;
		else
			return x;
	}
	
	public static int moveYPos(int y, Direction dir)
	{
		if (dir == Direction.UP)
			return y - 1;
		else if (dir == Direction.DOWN)
			return y + 1;
		else
			return y;
	}
	
	public boolean containsDirection(Direction dir)
	{
		return type.getDir1() == dir || type.getDir2() == dir;
	}
	
	public void updateTexture()
	{
		this.texture = TilesetHelper.getTextureVertices(tex, new Point(type.getValue(), tileState));
		
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
		this.textureBuffer = setBuffer(textureBuffer, texture);
	}
	
	public void select()
	{ 
		selected = true;
		tileState += 1;
		updateTexture();
	}
	
	public void deselect()		
	{
		selected = false;
		tileState -= 1;
		updateTexture();
	}
	
	public void highlight()	
	{
		highlighted = true;
		tileState += 2;
		updateTexture();
	}
	
	public void dehighlight()	
	{ 
		highlighted = false; 
		tileState -= 2;
		updateTexture();
	}
	
	public void power()	
	{ 
		powered = true;
		tileState = 3;
		updateTexture();
	}
	
	public void unpower()
	{ 
		powered = false; 
		tileState = 0;
		updateTexture();
	}
	
	public void setTileType(TileType type) 
	{ 
		this.type = type; 
		updateTexture();
	}
	
	public boolean isPowered()
	{
	    return powered;
	}
	
	public boolean isSelected()
	{
	    return selected;
	}
	
	public boolean isHightlighted()
	{
	    return highlighted;
	}
	
	public float getXPos()
	{
	    return xPos;
	}
	
	public float getYPos()
	{
	    return yPos;
	}
	
	public int getTileState()
	{
	    return tileState;
	}
	
	public TileType getType()
	{
	    return type;
	}
}
