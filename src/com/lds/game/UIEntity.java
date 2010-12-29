package com.lds.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.lds.Enums.RenderMode;
import com.lds.Enums.UIPosition;

public abstract class UIEntity
{
	//constants
	private static final float[] UIPositionF = { 	0.0f, 1.0f,
													-1.0f, 0.0f,
													0.0f, -1.0f,
													1.0f, 0.0f,
													0.0f, 0.0f,
													-1.0f, 1.0f,
													1.0f, 1.0f,
													-1.0f, -1.0f,
													1.0f, -1.0f }; //clamped between -1 and 1
	
	//graphics data
	public float xSize, ySize, xPos, yPos, xRelative, yRelative, halfXSize, halfYSize;
	public float topPad, leftPad, bottomPad, rightPad;
	public float colorR, colorG, colorB, colorA;
	UIPosition position;
	RenderMode renderMode;
	
	public int texturePtr;
	public float[] vertices;
	public float[] texture;
	public byte[] indices;
	
	public FloatBuffer vertexBuffer;
	public FloatBuffer textureBuffer;
	public ByteBuffer indexBuffer;
	
	//constructors
	public UIEntity(float xSize, float ySize, UIPosition position)
	{
		this(xSize, ySize, UIPositionF[position.getValue() * 2], UIPositionF[(position.getValue() * 2) + 1], 0, 0, 0, 0);
		this.position = position;
	}
	
	public UIEntity(float xSize, float ySize, float xRelative, float yRelative) 
	{
		this(xSize, ySize, xRelative, yRelative, 0, 0, 0, 0);
	}
	
	public UIEntity(float xSize, float ySize, UIPosition position, float topPad, float leftPad, float bottomPad, float rightPad)
	{
		this (xSize, ySize, UIPositionF[position.getValue() * 2], UIPositionF[(position.getValue() * 2) + 1], topPad, leftPad, bottomPad, rightPad);
	}
	
	public UIEntity(float xSize, float ySize, float xRelative, float yRelative, float topPad, float leftPad, float bottomPad, float rightPad) 
	{
		this.xSize = xSize;
		this.ySize = ySize;
		this.xRelative = xRelative;
		this.yRelative = yRelative;
		this.topPad = topPad;
		this.leftPad = leftPad;
		this.bottomPad = bottomPad;
		this.rightPad = rightPad;
		
		this.halfXSize = xSize / 2;
		this.halfYSize = ySize / 2;
		
		float[] initVerts = { 	halfXSize, halfYSize,
								halfXSize, -halfYSize,
								-halfXSize, halfYSize,
								-halfXSize, -halfYSize };
		
		vertices = initVerts;
		
		byte[] initIndices = { 0, 1, 2, 3 };
		
		indices = initIndices;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
				
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}
	
	public void draw(GL10 gl)
	{
		if (renderMode == RenderMode.COLOR) 
		{
			gl.glColor4f(colorR, colorG, colorB, colorA);
		}
		if (renderMode == RenderMode.TEXTURE || renderMode == RenderMode.TILESET)
		{
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texturePtr);
		}
		
		gl.glFrontFace(GL10.GL_CW);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		if (renderMode == RenderMode.TEXTURE || renderMode == RenderMode.TILESET)
		{
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		if (renderMode == RenderMode.TEXTURE || renderMode == RenderMode.TILESET) 
		{
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		}
				
		if (renderMode == RenderMode.GRADIENT)
		{
			
		}
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
		if (renderMode == RenderMode.TEXTURE || renderMode == RenderMode.TILESET) 
		{
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		if (renderMode == RenderMode.COLOR || renderMode == RenderMode.GRADIENT)
		{
			gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}
	
	public void updatePosition(float screenW, float screenH, float camPosX, float camPosY)
	{
		xPos = (screenW / 2 * xRelative) + leftPad - rightPad;
		yPos = (screenH / 2 * yRelative) + bottomPad - topPad;
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
					this.topPad = halfYSize + topPad;
					break;
				case LEFT:
					this.leftPad = halfXSize + leftPad;
					break;
				case BOTTOM:
					this.bottomPad = halfYSize + bottomPad;
					break;
				case RIGHT:
					this.rightPad = halfXSize + rightPad;
					break;
				case CENTER:
					break;
				case TOPLEFT:
					this.leftPad = halfXSize + leftPad;
					this.topPad = halfYSize + topPad;
					break;
				case TOPRIGHT:
					this.rightPad = halfXSize + rightPad;
					this.topPad = halfYSize + topPad;
					break;
				case BOTTOMLEFT:
					this.leftPad = halfXSize + leftPad;
					this.bottomPad = halfYSize + bottomPad;
					break;
				case BOTTOMRIGHT:
					this.rightPad = halfXSize + rightPad;
					this.bottomPad = halfYSize + bottomPad;
					break;
				default:
			}
		}
	}
	
	public void setColor(float r, float b, float g, float a)
	{
		colorR = r;
		colorG = g;
		colorB = b;
		colorA = a;
	}
	
	public void setColor (int r, int b, int g, int a)
	{
		colorR = (float) r / 255.0f;
		colorG = (float) g / 255.0f;
		colorB = (float) b / 255.0f;
		colorA = (float) a / 255.0f;
	}
	
	public void setGradient(float[] colors)
	{
		
	}
}