package com.lds.UI;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import com.lds.game.Game;
import com.lds.Enums.RenderMode;
import com.lds.Enums.UIPosition;
import com.lds.Texture;
import com.lds.TilesetHelper;

public abstract class UIEntity
{
	//constants
	protected static final float[] UIPositionF = { 	0.0f, 1.0f,
													-1.0f, 0.0f,
													0.0f, -1.0f,
													1.0f, 0.0f,
													0.0f, 0.0f,
													-1.0f, 1.0f,
													1.0f, 1.0f,
													-1.0f, -1.0f,
													1.0f, -1.0f }; //clamped between -1 and 1, turns UIPosition enum to relative coords
	
	//graphics data
	protected float xSize, ySize, xPos, yPos, xRelative, yRelative, halfXSize, halfYSize;
	private float topPad, leftPad, bottomPad, rightPad;
	private float colorR, colorG, colorB, colorA;
	protected UIPosition position;
	protected RenderMode renderMode;
	private Texture tex;
	
	private float[] vertices;
	private float[] texture;
	private float[] color;
	private byte[] indices;
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private FloatBuffer colorBuffer;
	private ByteBuffer indexBuffer;
	
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
		this.position = position;
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
		
		setVertices(initVerts);
		
		byte[] initIndices = { 0, 1, 2, 3 };
		
		indices = initIndices;
				
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
			gl.glBindTexture(GL10.GL_TEXTURE_2D, tex.getTexture());
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
	
	//Runs every frame
	public void update()
	{
		//default update does nothing, no updating needed.
	}
	
	public void touch(int x, int y)
	{
		
	}
	
	/*******************
	 * Padding Methods *
	 *******************/
	
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
			updatePosition();
		}
		else
		{
			System.out.println("Warning: Current UIEntity is not using positioning with UIPosition. No padding changes made!");
		}
	}
	
	public void updatePosition()
	{
		xPos = (Game.screenW / 2 * xRelative) + leftPad - rightPad;
		yPos = (Game.screenH / 2 * yRelative) + bottomPad - topPad;
	}
	
	/**********************
	 * RenderMode methods *
	 **********************/
	
	//BLANK
	public void setBlankMode()
	{
		renderMode = RenderMode.BLANK;
	}
	
	//COLOR
	public void setColorMode(float r, float g, float b, float a)
	{
		renderMode = RenderMode.COLOR;
		updateColor(r, g, b, a);
	}
	
	public void setColorMode(int r, int b, int g, int a)
	{
		renderMode = RenderMode.COLOR;
		updateColor(r, g, b, a);
	}
	
	public void updateColor(float r, float g, float b, float a)
	{
		if (renderMode == RenderMode.COLOR)
		{
			colorR = r;
			colorG = g;
			colorB = b;
			colorA = a;
		}
	}
	
	public void updateColor(int r, int g, int b, int a)
	{
		if (renderMode == RenderMode.COLOR)
		{
			colorR = (float) r / 255.0f;
			colorG = (float) g / 255.0f;
			colorB = (float) b / 255.0f;
			colorA = (float) a / 255.0f;
		}
	}
	
	//GRADIENT
	public void setGradientMode(float[] color)
	{
		renderMode = RenderMode.GRADIENT;
		updateGradient(color);
	}
	
	public void updateGradient(float[] color)
	{
		if (renderMode == RenderMode.GRADIENT)
		{
			this.color = color;
			
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(color.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			colorBuffer = byteBuf.asFloatBuffer();
			colorBuffer.put(color);
			colorBuffer.position(0);
		}
	}
	
	//TEXTURE
	public void setTextureMode(Texture tex)
	{
		renderMode = RenderMode.TEXTURE;
		updateTexture(tex);
	}
	
	public void setTextureMode(Texture tex, float[] texture)
	{
		renderMode = RenderMode.TEXTURE;
		updateTexture(tex, texture);
	}
		
	public void updateTexture(Texture tex)
	{
		if (renderMode == RenderMode.TEXTURE)
		{
			this.tex = tex;
			float[] initTexture = { 1.0f, 0.0f,
									1.0f, 1.0f,
									0.0f, 0.0f,
									0.0f, 1.0f};
			texture = initTexture;
			
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			textureBuffer = byteBuf.asFloatBuffer();
			textureBuffer.put(texture);
			textureBuffer.position(0);
		}
	}
	
	public void updateTexture(Texture tex, float[] texture)
	{
		if (renderMode == RenderMode.TEXTURE)
		{
			this.tex = tex;
			this.texture = texture;
			
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			textureBuffer = byteBuf.asFloatBuffer();
			textureBuffer.put(texture);
			textureBuffer.position(0);
		}
	}
	
	//TILESET
	public void setTilesetMode(Texture tex, int x, int y)
	{
		renderMode = RenderMode.TILESET;
		updateTileset(tex, x, y);
	}
	
	public void setTilesetMode (Texture tex, int tileID)
	{
		renderMode = RenderMode.TILESET;
		updateTileset(tex, tileID);
	}
		
	public void updateTileset(Texture tex, int x, int y)
	{
		if (renderMode == RenderMode.TILESET)
		{
			this.tex = tex;
			texture = TilesetHelper.getTextureVertices(tex, x, y);
			
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			textureBuffer = byteBuf.asFloatBuffer();
			textureBuffer.put(texture);
			textureBuffer.position(0);
		}
	}
	
	public void updateTileset(Texture tex, int tileID)
	{
		if (renderMode == RenderMode.TILESET)
		{
			this.tex = tex;
			texture = TilesetHelper.getTextureVertices(tex, tileID);
			
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
			byteBuf.order(ByteOrder.nativeOrder());
			textureBuffer = byteBuf.asFloatBuffer();
			textureBuffer.put(texture);
			textureBuffer.position(0);
		}
	}
	
	public void updateTileset(int x, int y)
	{
		if (tex != null && renderMode == RenderMode.TILESET)
			updateTileset(tex, x, y);
	}
	
	public void updateTileset(int tileID)
	{
		if (tex != null && renderMode == RenderMode.TILESET)
			updateTileset(tex, tileID);
	}
	
	/**************************
	 * Accessors and Mutators *
	 **************************/
	
	public float getXSize() 			{ return xSize; }
	public float getYSize() 			{ return ySize; }
	public float getXPos()				{ return xPos; }
	public float getYPos()				{ return yPos; }
	public float getColorR() 			{ return colorR; }
	public float getColorG()			{ return colorG; }
	public float getColorB()			{ return colorB; }
	public float getColorA()			{ return colorA; }
	public Texture getTexture()			{ return tex; }
	public UIPosition getPosEnum()		{ return position; }
	public RenderMode getRenderMode()	{ return renderMode; }
	public float[] getVertices()		{ return vertices; }
	public float[] getGradientCoords()	{ return color; }
	public float[] getTextureCoords()	{ return texture; }
	
	public void setXSize(float xSize)			{ this.xSize = xSize; }
	public void setYSize(float ySize)			{ this.ySize = ySize; }
	public void setXPos(float xPos)				{ this.xPos = xPos; }
	public void setYPos(float yPos)				{ this.yPos = yPos; }
	public void setTopPad(float topPad)			{ this.topPad = topPad; }
	public void setLeftPad(float leftPad)		{ this.leftPad = leftPad; }
	public void setBottomPad(float bottomPad)	{ this.bottomPad = bottomPad; }
	public void setRightPad(float rightPad)		{ this.rightPad = rightPad; }
	
	public void setVertices(float[] vertices)
	{
		this.vertices = vertices;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}
}
