package com.lds.game;

import javax.microedition.khronos.opengles.GL10;

import com.lds.EntityCleaner;
import com.lds.Point;
import com.lds.Texture;
import com.lds.TilesetHelper;
import com.lds.Enums.RenderMode;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import java.util.ArrayList;

//Highest level abstract class in game

public abstract class Entity 
{
	public static final float DEFAULT_SIZE = 69.0f;
	
	//behavior data
	protected boolean isSolid;
	protected boolean isRendered;
	protected boolean willCollideWithPlayer;
	
	//graphics data
	protected float angle, size, xPos, yPos, xScl, yScl, halfSize;
	protected float colorR, colorG, colorB, colorA;
	protected RenderMode renderMode;
	protected Texture tex;
	
	protected float[] vertices;
	protected float[] texture;
	protected float[] color;
	private byte[] indices;
	
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer textureBuffer;
	protected FloatBuffer colorBuffer;
	private ByteBuffer indexBuffer;
	
	//debug data
	private int entID;
	private static int entCount = 0;
	
	//collision data
	protected Point[] colPoints;
	protected float[] colSlopes;
	protected double diagonal, rad, diagAngle;
	
	public ArrayList<Entity> colList = new ArrayList<Entity>();
	public ArrayList<Entity> colIgnoreList = new ArrayList<Entity>();
	
	
	public Entity (float size, float xPos, float yPos, RenderMode renderMode)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, renderMode);
	}
	
	public  Entity (float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, RenderMode renderMode)
	{
		//initialize debug data
		entID = entCount;
		entCount++;
		
		//initialize behavior variables
		this.isSolid = isSolid;
		
		//initializes graphics variables
		this.size = size;
		this.xPos = xPos;
		this.yPos = yPos;
		this.angle = angle;
		this.xScl = xScl;
		this.yScl = yScl;
		this.renderMode = renderMode;
		
		halfSize = size / 2;
		
		//initializes collision variables
		rad = Math.toRadians((double)(angle + 90.0f));
		diagonal = Math.sqrt(Math.pow(halfSize * xScl, 2) + Math.pow(halfSize * yScl, 2)); //distance from center to corner
		diagAngle = Math.asin((halfSize * xScl) / diagonal); //angle between vertical line and diagonal to top left corner
		colSlopes = new float[4];
		
		colPoints = new Point[4]; //0: top left, 1: bottom left, 2:top right, 3: bottom right
		colPoints[0] = new Point();
		colPoints[1] = new Point();
		colPoints[2] = new Point();
		colPoints[3] = new Point();
		
		//makes it so x/yPos are in center of box - Robert
		float[] initVerts = {	halfSize, halfSize, 	//top left
								halfSize, -halfSize, 	//bottom left
								-halfSize, halfSize, 	//top right
								-halfSize, -halfSize }; //bottom right

		vertices = initVerts;
				
		byte[] initIndices = {	0, 1, 2, 3 };
		
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
		
	public void remove()
	{
		EntityCleaner.queueEntityForRemoval(this);
	}
	
	public void update()
	{
		
	}
		
	public void collide(Entity ent)
	{
		
	}
	
	/*********************
	 * Collision Methods *
	 *********************/
	
	//reinitialize colllision variables
	public void initializeCollisionVariables ()
	{
		rad = Math.toRadians((double)(angle + 90.0f));
		diagonal = Math.sqrt(Math.pow(halfSize * xScl, 2) + Math.pow(halfSize * yScl, 2));
		diagAngle = Math.atan2((halfSize * xScl) , (halfSize * yScl));
	}
	
	//used to get the absolute, not relative, positions of the entity's 4 points in the XY Plane
	public void updateAbsolutePointLocations ()
	{
		initializeCollisionVariables();
		
		double cp0 = this.rad + diagAngle;
		double cp1 = this.rad + Math.PI - diagAngle;
		double cp2 = this.rad - diagAngle;
		double cp3 = this.rad - Math.PI + diagAngle;
		
		colPoints[0].setX((float)(Math.cos(cp0) * diagonal) + xPos); //top left
		colPoints[0].setY((float)(Math.sin(cp0) * diagonal) + yPos); //top left
		
		colPoints[1].setX((float)(Math.cos(cp1) * diagonal) + xPos); //bottom left
		colPoints[1].setY((float)(Math.sin(cp1) * diagonal) + yPos); //bottom left
		
		colPoints[2].setX((float)(Math.cos(cp2) * diagonal) + xPos); //top right
		colPoints[2].setY((float)(Math.sin(cp2) * diagonal) + yPos); //top right
		
		colPoints[3].setX((float)(Math.cos(cp3) * diagonal) + xPos); //bottom right
		colPoints[3].setY((float)(Math.sin(cp3) * diagonal) + yPos); //bottom right
	}
	
	public void updateAbsolutePointLocations(Point[] pts)
	{
		initializeCollisionVariables();
		this.colPoints = pts;
	}
	
	public boolean closeEnough (Entity ent)
	{
		if (Math.sqrt(Math.pow(xPos - ent.xPos, 2) + Math.pow(yPos - ent.yPos, 2)) < (float)((diagonal) + ent.diagonal))
			return true;
		
		else
			return false;
	}
	
	public boolean isFacing(Entity ent)
	{
		float angleBetween = (float)Math.toDegrees(Math.atan2((ent.getYPos() - yPos) , (ent.getXPos() - xPos)));
		float angleDiff = (angle + 90.0f) - angleBetween;
		
		if (angleDiff > 315.0f)
			angleDiff -= 360.0f;
		
		if (angleDiff > -45 && angleDiff < 45)
			return true;
		
		else
			return false;
	}
	
	public boolean isColliding (Entity ent)
	{	
		//checks to see if either object is not solid
		if (this.isSolid == false || ent.isSolid == false)
			return false;
		
		if (colIgnoreList.contains(ent))
			return false;
		
		//update values
		initializeCollisionVariables();
		ent.initializeCollisionVariables();
		
		//makes sure the entities are close enough so that collision testing is actually necessary
		if (!closeEnough(ent))
		{
			return false;
		}
		
		if (collisionCheck(this, ent) || collisionCheck(ent, this))
		{
			return true;
		}
		return false;
	}
	
	public boolean collisionCheck (Entity ent1, Entity ent2)
	{
		Point[] ent1TempPts = ent1.getColPoints();
		Point[] ent2TempPts = ent2.getColPoints();
		
		float ent1StartAngle = ent1.angle;
		float ent2StartAngle = ent2.getAngle();
		float ent2StartX = ent2.getXPos();
		float ent2StartY = ent2.getYPos();
		float entDistance = (float)Math.sqrt(Math.pow(ent1.xPos - ent2.getXPos(), 2) + Math.pow(ent1.yPos - ent2.getYPos(), 2)); //the distance between the entities
		float entEndAngle = (float)Math.atan((ent1.yPos - ent2.getYPos()) / (ent1.xPos - ent2.getXPos())) - (float)Math.toRadians(ent1.angle); //the angle between the entities
		
		ent1.angle = 0.0f;
		ent2.setAngle(ent2.getAngle() - ent1StartAngle);
		ent2.setXPos((float)Math.cos(entEndAngle) * entDistance + ent1.xPos);
		ent2.setYPos((float)Math.sin(entEndAngle) * entDistance + ent1.yPos);
		
		ent1.updateAbsolutePointLocations();
		ent2.updateAbsolutePointLocations();
		
		for (Point point : ent2.colPoints)
		{
			if (point.getX() <= ent1.colPoints[3].getX() && point.getX() >= ent1.colPoints[0].getX() && point.getY() <= ent1.colPoints[0].getY() && point.getY() >= ent1.colPoints[3].getY())
			{
				ent1.angle = ent1StartAngle;
				ent2.setAngle(ent2StartAngle);
				ent2.setXPos(ent2StartX);
				ent2.setYPos(ent2StartY);
								
				ent1.updateAbsolutePointLocations(ent1TempPts);
				ent2.updateAbsolutePointLocations(ent2TempPts);
				
				return true;
			}
		}
		ent1.angle = ent1StartAngle;
		ent2.setAngle(ent2StartAngle);
		ent2.setXPos(ent2StartX);
		ent2.setYPos(ent2StartY);
		
		
		ent1.updateAbsolutePointLocations(ent1TempPts);
		ent2.updateAbsolutePointLocations(ent2TempPts);
		
		return false;
	}
			
	//this is a blank method, to be overriden by subclasses
	//it determines how each object interacts with other objects and performs the action
	public void interact (Entity ent)
	{
		
	}
	
	//this is a blank method ot be overriden similat to interact
	//it performs the action to occur when an object stops colliding with another
	public void uninteract (Entity ent)
	{
		
	}
	
	//this is a blank method to be overriden by PickupObj
	//TODO: get rid of this shell, run pickupScale directly from GameRenderer
	public void pickupScale ()
	{
		
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
		if (tex != null)
			updateTileset(tex, x, y);
	}
	
	public void updateTileset(int tileID)
	{
		if (tex != null)
			updateTileset(tex, tileID);
	}
	
	/**************************
	 * Accessors and Mutators *
	 **************************/
	
	public float getSize()				{ return size; }
	public float getXPos()				{ return xPos; }
	public float getYPos()				{ return yPos; }
	public float getAngle()				{ return angle; }
	public float getXScl()				{ return xScl; }
	public float getYScl()				{ return yScl; }
	public float getColorR()			{ return colorR; }
	public float getColorG()			{ return colorG; }
	public float getColorB()			{ return colorB; }
	public float getColorA()			{ return colorA; }
	public RenderMode getRenderMode()	{ return renderMode; }
	public float[] getVertices()		{ return vertices; }
	public float[] getColorCoords()		{ return color; }
	public float[] getTextureCoords()	{ return texture; }
	public Texture getTexture()			{ return tex; }
	public Point[] getColPoints()		{ return colPoints; }
	public double getDiagonal()			{ return diagonal; }
	public double getRad()				{ return rad; }
	public double getDiagAngle()		{ return diagAngle; }
	public int getEntID()				{ return entID; }
	public static int getEntCount()		{ return entCount; }
	public boolean willCollideWithPlayer() { return willCollideWithPlayer; }
	
	public void setSize(float size)		{ this.size = size; }
	public void setXPos(float xPos)		{ this.xPos = xPos; }
	public void setYPos(float yPos)		{ this.yPos = yPos; }
	public void setAngle(float angle)	{ this.angle = angle; }
	public void setXScl(float xScl)		{ this.xScl = xScl; }
	public void setYScl(float yScl)		{ this.yScl	= yScl; }
	public void setWillCollideWithPlayer(boolean willCollideWithPlayer) { this.willCollideWithPlayer = willCollideWithPlayer; }
	
	public void setColPoints(Point[] colPoints)
	{
		if (colPoints.length == 4)
			this.colPoints = colPoints;
	}
}
