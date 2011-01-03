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
	
	public float[] vertices;
	public float[] texture;
	public float[] color;
	public byte[] indices;
	public int texturePtr;
	
	public FloatBuffer vertexBuffer;
	public FloatBuffer textureBuffer;
	public FloatBuffer colorBuffer;
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
		//Enable texturing and bind the current texture pointer (texturePtr) to GL_TEXTURE_2D
		if (renderMode == RenderMode.TEXTURE || renderMode == RenderMode.TILESET)
		{
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texturePtr);
		}
		
		//Sets the front face of a polygon based on rotation (Clockwise - GL_CW, Counter-clockwise - GL_CCW)
		gl.glFrontFace(GL10.GL_CW);
		
		//Backface culling
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		
		//Enable settings for this polygon
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		if (renderMode == RenderMode.TEXTURE || renderMode == RenderMode.TILESET) {gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);}
		if (renderMode == RenderMode.GRADIENT) {gl.glEnableClientState(GL10.GL_COLOR_ARRAY);}
		
		//Bind vertices, texture coordinates, and/or color coordinates to the OpenGL system
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		if (renderMode == RenderMode.TEXTURE || renderMode == RenderMode.TILESET) {gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);}
		if (renderMode == RenderMode.GRADIENT) {gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);}
		
		//Sets color
		if (renderMode == RenderMode.COLOR) {gl.glColor4f(colorR, colorG, colorB, colorA);}
		
		//Draw the vertices
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);		
		
		//Disable things for next polygon
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		if(renderMode == RenderMode.GRADIENT) {gl.glDisableClientState(GL10.GL_COLOR_ARRAY);}
		gl.glDisable(GL10.GL_CULL_FACE);
		
		//Disable texturing for next polygon
		if (renderMode == RenderMode.TEXTURE || renderMode == RenderMode.TILESET) 
		{
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		
		//Reset color for next polygon.
		if (renderMode == RenderMode.COLOR || renderMode == RenderMode.GRADIENT) {gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);}
	}
	
	public void updatePosition(float screenW, float screenH)
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
				case TOPLEFT:
				case TOPRIGHT:
					this.topPad = halfYSize + topPad;
					break;
			}
			
			switch (position)
			{
				case LEFT:
				case TOPLEFT:
				case BOTTOMLEFT:
					this.leftPad = halfXSize + leftPad;
					break;
			}
			
			switch (position)
			{
				case BOTTOM:
				case BOTTOMLEFT:
				case BOTTOMRIGHT:
					this.bottomPad = halfYSize + bottomPad;
					break;
			}
			
			switch (position)
			{
				case RIGHT:
				case TOPRIGHT:
				case BOTTOMRIGHT:
					this.rightPad = halfXSize + rightPad;
					break;
			}
		}
		else
		{
			System.out.println("Warning: Current UIEntity is not using positioning with UIPosition. No padding changes made!");
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
	
	public void setGradient(float[] color)
	{
		this.color = color;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(color.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		colorBuffer = byteBuf.asFloatBuffer();
		colorBuffer.put(color);
		colorBuffer.position(0);
	}
	
	public void setPadding (float topPad, float leftPad, float bottomPad, float rightPad)
	{
		this.topPad = topPad;
		this.leftPad = leftPad;
		this.bottomPad = bottomPad;
		this.rightPad = rightPad;
	}
}
