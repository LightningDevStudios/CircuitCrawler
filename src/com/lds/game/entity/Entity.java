package com.lds.game.entity;

import javax.microedition.khronos.opengles.GL10;

import com.lds.EntityCleaner;
import com.lds.Texture;
import com.lds.TilesetHelper;
import com.lds.Enums.RenderMode;
import com.lds.Vector2f;

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
	protected boolean rendered;
	protected boolean willCollideWithPlayer;
	protected boolean circular;
	
	//graphics data
	protected float angle, size, halfSize;
	protected float colorR, colorG, colorB, colorA;
	protected RenderMode renderMode;
	protected Texture tex;
	protected Vector2f posVec, scaleVec;
	
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
	protected Vector2f[] vertVecs;
	protected double diagonal, rad;
	
	public ArrayList<Entity> colList = new ArrayList<Entity>();
	public ArrayList<Entity> colIgnoreList = new ArrayList<Entity>();
	
	
	public Entity (float size, float xPos, float yPos, boolean circular)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, circular);
	}
	
	public  Entity (float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular)
	{
		//initialize debug data
		entID = entCount;
		entCount++;
		
		//initialize behavior variables
		this.isSolid = isSolid;
		this.circular = circular;
		
		//initializes graphics variables
		this.size = size;
		halfSize = size / 2;
		this.angle = angle;
		posVec = new Vector2f(xPos, yPos);
		scaleVec = new Vector2f(xScl, yScl);
		
		//initializes collision variables
		rad = Math.toRadians((double)(angle));
		diagonal = Math.sqrt(Math.pow(halfSize * xScl, 2) + Math.pow(halfSize * yScl, 2)); //distance from center to corner
		 
		vertVecs = new Vector2f[4];
		for (int i = 0; i < vertVecs.length; i++)
		{
			vertVecs[i] = new Vector2f();
		}
		
		
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
		
		renderMode = RenderMode.BLANK;
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
		rad = Math.toRadians(angle);
		diagonal = Math.sqrt(Math.pow(halfSize * getXScl(), 2) + Math.pow(halfSize * getYScl(), 2));	}
	
	//used to get the absolute, not relative, positions of the entity's 4 points in the XY Plane
	public void updateAbsolutePointLocations ()
	{	
		Vector2f unscaledVec = Vector2f.scale(new Vector2f((float)Math.cos(Math.toRadians(angle)), (float)Math.sin(Math.toRadians(angle))), halfSize);
		Vector2f xVec = Vector2f.scale(unscaledVec, getXScl());
		Vector2f yVec = Vector2f.scale(Vector2f.getNormal(unscaledVec), getYScl());
		 
		vertVecs[0].set(Vector2f.add(posVec, Vector2f.add(xVec, yVec))); //top  right
		vertVecs[1].set(Vector2f.add(posVec, Vector2f.sub(yVec, xVec))); //top left
		vertVecs[2].set(Vector2f.add(posVec, Vector2f.add(Vector2f.neg(xVec), Vector2f.neg(yVec)))); //bottom left
		vertVecs[3].set(Vector2f.add(posVec, Vector2f.sub(xVec, yVec))); //bottom right
	}
	
	public boolean closeEnough (Entity ent)
	{
		initializeCollisionVariables();
		if (Vector2f.sub(this.posVec, ent.posVec).mag() < (float)((diagonal) + ent.diagonal))
			return true;
		else
			return false;
	}

	public boolean isFacing(Entity ent)
	{
		float angleBetween = (float)Math.toDegrees(Math.atan2((ent.getYPos() - this.getYPos()) , (ent.getXPos() - this.getXPos())));
		float angleDiff = (angle + 90.0f) - angleBetween;
		
		if (angleDiff > 315.0f)
			angleDiff -= 360.0f;
		
		if (angleDiff > -45 && angleDiff < 45)
			return true;
		
		else
			return false;
	}
	
	public boolean isColliding (Entity ent) //if both entities are polygons
	{	
		//checks to see if either object is not solid
		if (this.isSolid == false || ent.isSolid == false)
			return false;
		
		if (colIgnoreList.contains(ent))
			return false;
		
		//makes sure the entities are close enough so that collision testing is actually necessary
		if (!closeEnough(ent))
			return false;
		
		if (this.isCircular() && ent.isCircular())
			return this.isCircleCollidingWithCircle(ent);
		
		else if (ent.isCircular())
			return this.isRectangleCollidingWithCircle(ent);
		
		else if (this.isCircular())
			return ent.isRectangleCollidingWithCircle(this);
		
		else
			return this.isRectangleCollidingWithRectangle(ent);
	}
	
	protected boolean isCircleCollidingWithCircle (Entity ent) //if both entities are circles
	{
		if (Vector2f.sub(this.posVec, ent.posVec).mag() < halfSize + ent.halfSize)
			return true;
		return false;
	}
	
	protected boolean isRectangleCollidingWithCircle (Entity ent) //if only ent is a circle
	{
		this.updateAbsolutePointLocations();
		
		Vector2f[] axes = new Vector2f[3];
		//set rectangle-based axes
		axes[0] = Vector2f.abs(Vector2f.sub(vertVecs[0], this.vertVecs[1]));
		axes[1] = Vector2f.getNormal(axes[0]);
		//set circle axis
		axes[2] = Vector2f.sub(vertVecs[0], ent.posVec);
		for (int i = 1; i < vertVecs.length; i++)
		{
			Vector2f tempVec = Vector2f.sub(vertVecs[i], ent.posVec);
			if (axes[2].mag() > tempVec.mag())
			{
				axes[2].set(tempVec);
			}
		}
		
		for (Vector2f axis : axes)
		{
			axis.normalize();
						
			//get mins and maxes for rectangle
			float min1 = axis.dot(this.vertVecs[0]);
			float max1 = min1;
			for (int i = 1; i < this.vertVecs.length; i++)
			{
				float dotProd1 = axis.dot(this.vertVecs[i]);
				if (dotProd1 > max1)
					max1 = dotProd1;
				if (dotProd1 < min1)
					min1 = dotProd1;
			}
			
			//get mins and maxes for circle 
			float min2 = axis.dot(Vector2f.add(ent.posVec, Vector2f.scale(axis, ent.halfSize)));
			float max2 = axis.dot(Vector2f.sub(ent.posVec, Vector2f.scale(axis, ent.halfSize)));
			if (min2 > max2)
			{
				float temp = min2;
				min2 = max2;
				max2 = temp;
			}
			
			if ((max1 > max2 || max1 < min2) && (max2 > max1 || max2 < min1))
			{
				return false;
			}
		}
		System.out.println("Collision!");
		return true;
	}
	
	protected boolean isRectangleCollidingWithRectangle (Entity ent) //if both entities are circles
	{
		this.updateAbsolutePointLocations();
		ent.updateAbsolutePointLocations();
		
		Vector2f[] axes = new Vector2f[4];
		axes[0] = Vector2f.abs(Vector2f.sub(this.vertVecs[0], this.vertVecs[1]));
		axes[1] = Vector2f.getNormal(axes[0]);
		axes[2] = Vector2f.abs(Vector2f.sub(ent.vertVecs[0], ent.vertVecs[1]));
		axes[3] = Vector2f.getNormal(axes[2]);
		
		for (Vector2f axis : axes)
		{
			axis.normalize();
						
			//get mins and maxes for first entity
			float min1 = axis.dot(this.vertVecs[0]);
			float max1 = min1;
			for (int i = 1; i < this.vertVecs.length; i++)
			{
				float dotProd1 = axis.dot(this.vertVecs[i]);
				if (dotProd1 > max1)
					max1 = dotProd1;
				if (dotProd1 < min1)
					min1 = dotProd1;
			}
			
			//get mins and maxes for second entity
			float min2 = axis.dot(ent.vertVecs[0]);
			float max2 = min2;
			for (int i = 1; i < ent.vertVecs.length; i++)
			{
				float dotProd2 = axis.dot(ent.vertVecs[i]);
				if (dotProd2 > max2)
					max2 = dotProd2;
				if (dotProd2 < min2)
					min2 = dotProd2;
			}
			
			if ((max1 > max2 || max1 < min2) && (max2 > max1 || max2 < min1))
			{	
				return false;
			}
		}
		
		return true;
	}
	
	//blank method, overridden by PhysEnt
	public void circleBounce (Entity ent)
	{
		
	}
	
	//blank method, overridden by PhysEnt
	public void rectangleBounce (Entity ent)
	{
		
	}
			
	//this is a blank method, to be overriden by subclasses
	//it determines how each object interacts with other objects and performs the action
	public void interact (Entity ent)
	{
		
	}
	
	//this is a blank method ot be overriden similar to interact
	//it performs the action to occur when an object stops colliding with another
	public void uninteract (Entity ent)
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
	public Vector2f getPos()			{ return posVec; }
	public Vector2f getScl()			{ return scaleVec; }
	public float getXPos()				{ return posVec.getX(); }
	public float getYPos()				{ return posVec.getY(); }
	public float getAngle()				{ return angle; }
	public float getXScl()				{ return scaleVec.getX(); }
	public float getYScl()				{ return scaleVec.getY(); }
	public float getColorR()			{ return colorR; }
	public float getColorG()			{ return colorG; }
	public float getColorB()			{ return colorB; }
	public float getColorA()			{ return colorA; }
	public RenderMode getRenderMode()	{ return renderMode; }
	public float[] getVertices()		{ return vertices; }
	public float[] getColorCoords()		{ return color; }
	public float[] getTextureCoords()	{ return texture; }
	public Texture getTexture()			{ return tex; }
	public Vector2f[] getVertVecs()		{ return vertVecs; }
	public double getDiagonal()			{ return diagonal; }
	public double getRad()				{ return rad; }
	public int getEntID()				{ return entID; }
	public static int getEntCount()		{ return entCount; }
	public boolean willCollideWithPlayer() { return willCollideWithPlayer; }
	public boolean isCircular()			{ return circular; }
	public boolean isRendered()			{ return rendered; }
	
	public void setSize(float size)		{ this.size = size; }
	public void setAngle(float angle)	{ this.angle = angle; }
	public void setXPos(float xPos)		{ posVec.setX(xPos); }
	public void setYPos(float yPos)		{ posVec.setY(yPos); }
	public void setXScl(float xScl)		{ scaleVec.setX(xScl); }
	public void setYScl(float yScl)		{ scaleVec.setY(yScl); }
	public void setRendered(boolean state)	{ rendered = state; }
	public void setSolidity(boolean solid)	{ isSolid = solid; }
	public void setWillCollideWithPlayer(boolean willCollideWithPlayer) { this.willCollideWithPlayer = willCollideWithPlayer; }
	public void setVertexVecs(Vector2f[] vertVecs)
	{
		if (vertVecs.length == 4)
			this.vertVecs = vertVecs;
	}
}
