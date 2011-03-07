package com.lds.UI;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.EnumSet;

import javax.microedition.khronos.opengles.GL10;

import com.lds.game.Game;
import com.lds.Enums.RenderMode;
import com.lds.Enums.UIPosition;
import com.lds.Texture;
import com.lds.TilesetHelper;

//TODO allow relative sizing to scale for multiple monitors
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
	protected EnumSet<RenderMode> renderMode;
	protected Texture tex;
	
	protected float[] vertices;
	protected float[] texture;
	protected float[] color;
	private byte[] indices;
	
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer textureBuffer;
	protected FloatBuffer colorBuffer;
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
		
		this.vertices = initVerts;
		this.vertexBuffer = setBuffer(vertexBuffer, vertices);
		
		byte[] initIndices = { 0, 1, 2, 3 };
		indices = initIndices;
				
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		renderMode = EnumSet.allOf(RenderMode.class);
		renderMode.clear();
	}
	
	public void draw(GL10 gl)
	{
		gl.glTranslatef(xPos, yPos, 0.0f);
		
		//Enable texturing and bind the current texture pointer (texturePtr) to GL_TEXTURE_2D
		if (renderMode.contains(RenderMode.TEXTURE) || renderMode.contains(RenderMode.TILESET))
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
		if (renderMode.contains(RenderMode.TEXTURE) || renderMode.contains(RenderMode.TILESET)) {gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);}
		if (renderMode.contains(RenderMode.GRADIENT)) {gl.glEnableClientState(GL10.GL_COLOR_ARRAY);}
		
		//Bind vertices, texture coordinates, and/or color coordinates to the OpenGL system
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		if (renderMode.contains(RenderMode.TEXTURE) || renderMode.contains(RenderMode.TILESET)) {gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);}
		if (renderMode.contains(RenderMode.GRADIENT)) {gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);}
		
		//Sets color
		if (renderMode.contains(RenderMode.COLOR)) {gl.glColor4f(colorR, colorG, colorB, colorA);}
		
		//Draw the vertices
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);		
		
		//Disable things for next polygon
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		if(renderMode.contains(RenderMode.GRADIENT)) {gl.glDisableClientState(GL10.GL_COLOR_ARRAY);}
		gl.glDisable(GL10.GL_CULL_FACE);
		
		//Disable texturing for next polygon
		if (renderMode.contains(RenderMode.TEXTURE) || renderMode.contains(RenderMode.TILESET))
		{
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		
		//Reset color for next polygon.
		if (renderMode.contains(RenderMode.COLOR) /*|| renderMode.contains(RenderMode.GRADIENT)*/) {gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);}
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
	public void clearRenderModes()
	{
		renderMode.clear();
	}
	
	//COLOR
	public void enableColorMode(float r, float g, float b, float a)
	{
		if (!renderMode.contains(RenderMode.COLOR))
			renderMode.add(RenderMode.COLOR);
		updateColor(r, g, b, a);
	}
	
	public void enableColorMode(int r, int b, int g, int a)
	{
		enableColorMode((float) r / 255.0f, (float) b / 255.0f, (float) g / 255.0f, (float) a / 255.0f);
	}
	
	public void updateColor(float r, float g, float b, float a)
	{
			colorR = r;
			colorG = g;
			colorB = b;
			colorA = a;
	}
	
	public void updateColor(int r, int g, int b, int a)
	{
		updateColor((float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f, (float) a / 255.0f);
	}
	
	public void disableColorMode()
	{
		if (renderMode.contains(RenderMode.COLOR))
			renderMode.remove(RenderMode.COLOR);
	}
	
	//GRADIENT
	public void enableGradientMode(float[] color)
	{
		if (!renderMode.contains(RenderMode.GRADIENT))
			renderMode.add(RenderMode.GRADIENT);
		updateGradient(color);
	}
	
	public void updateGradient(float[] color)
	{
			this.color = color;
			this.colorBuffer = setBuffer(colorBuffer, color);
	}
	
	public void disableGradientMode()
	{
		if (renderMode.contains(RenderMode.GRADIENT))
			renderMode.remove(RenderMode.GRADIENT);
	}
	
	//TEXTURE
	public void enableTextureMode(Texture tex)
	{
		if (!renderMode.contains(RenderMode.TEXTURE))
			renderMode.add(RenderMode.TEXTURE);
		updateTexture(tex);
	}
	
	public void enableTextureMode(Texture tex, float[] texture)
	{
		if (!renderMode.contains(RenderMode.TEXTURE))
			renderMode.add(RenderMode.TEXTURE);
		updateTexture(tex, texture);
	}
		
	public void updateTexture(Texture tex)
	{
			float[] initTexture = { 1.0f, 0.0f,
									1.0f, 1.0f,
									0.0f, 0.0f,
									0.0f, 1.0f};
			updateTexture(tex, initTexture);
	}
	
	public void updateTexture(Texture tex, float[] texture)
	{
			this.tex = tex;
			this.texture = texture;
			this.textureBuffer = setBuffer(textureBuffer, texture);
	}
	
	public void disableTextureMode()
	{
		if(renderMode.contains(RenderMode.TEXTURE))
			renderMode.remove(RenderMode.TEXTURE);
	}
	
	//TILESET
	public void enableTilesetMode(Texture tex, int x, int y)
	{
		if (!renderMode.contains(RenderMode.TILESET))
			renderMode.add(RenderMode.TILESET);
		updateTileset(tex, x, y);
	}
	
	public void enableTilesetMode(Texture tex, int tileID)
	{
		if (!renderMode.contains(RenderMode.TILESET))
			renderMode.add(RenderMode.TILESET);
		updateTileset(tex, tileID);
	}
		
	public void updateTileset(Texture tex, int x, int y)
	{
		updateTileset(tex, TilesetHelper.getTilesetID(x, y, tex));	
	}
	
	public void updateTileset(Texture tex, int tileID)
	{
		this.tex = tex;
		texture = TilesetHelper.getTextureVertices(tex, tileID);
		this.textureBuffer = setBuffer(textureBuffer, texture);
	}
	
	public void updateTileset(int x, int y)
	{
		if (tex != null)
			updateTileset(tex, x, y);
	}
	
	public void updateTileset(int tileID)
	{
		if (tex != null)
			updateTileset(tex, tileID);
	}
	
	public void disableTilesetMode()
	{
		if(renderMode.contains(RenderMode.TILESET))
			renderMode.remove(RenderMode.TILESET);
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
	public float[] getVertices()		{ return vertices; }
	public float[] getGradientCoords()	{ return color; }
	public float[] getTextureCoords()	{ return texture; }
	public EnumSet<RenderMode> getRenderMode()	{ return renderMode; }
	
	public void setXSize(float xSize)			{ this.xSize = xSize; }
	public void setYSize(float ySize)			{ this.ySize = ySize; }
	public void setXPos(float xPos)				{ this.xPos = xPos; }
	public void setYPos(float yPos)				{ this.yPos = yPos; }
	public void setTopPad(float topPad)			{ this.topPad = topPad; }
	public void setLeftPad(float leftPad)		{ this.leftPad = leftPad; }
	public void setBottomPad(float bottomPad)	{ this.bottomPad = bottomPad; }
	public void setRightPad(float rightPad)		{ this.rightPad = rightPad; }
}
