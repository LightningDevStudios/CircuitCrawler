package com.lds.game;

import javax.microedition.khronos.opengles.GL10;

import com.lds.EntityCleaner;
import com.lds.Point;
import com.lds.TilesetHelper;
import com.lds.Enums.RenderMode;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import java.util.ArrayList;

//Highest level abstract class in game

public abstract class Entity 
{
	public static final float DEFAULT_SIZE = 30.0f;
	
	//behavior data
	public boolean isSolid;
	
	//graphics data
	public int texturePtr;
	public float angle, size, xPos, yPos, xScl, yScl, speed, halfSize;
	public float colorR, colorG, colorB, colorA;
	public RenderMode renderMode;
	
	public float[] vertices;
	public float[] texture;
	public float[] color;
	public byte[] indices;
	
	public FloatBuffer vertexBuffer;
	public FloatBuffer textureBuffer;
	public FloatBuffer colorBuffer;
	public ByteBuffer indexBuffer;
	
	//debug data
	public int entID;
	public boolean isRendered;
	public static int entCount;
	
	//collision data
	public Point[] colPoints;
	public float[] colSlopes;
	public double diagonal, rad, diagAngle;
	
	public ArrayList<Entity> colList = new ArrayList<Entity>();
	
	public  Entity (float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, RenderMode renderMode)
	{
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
	
	public Entity (float _size, float _xPos, float _yPos, RenderMode renderMode)
	{
		this(_size, _xPos, _yPos, 0.0f, 1.0f, 1.0f, true, renderMode);
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
		
	public void remove()
	{
		EntityCleaner.queueEntityForRemoval(this);
	}
	
	public void setColor(float r, float b, float g, float a)
	{
		colorR = r;
		colorB = b;
		colorG = g;
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
	
	
	
	/*********************
	 * Collision Methods *
	 *********************/
	
	//used to get the absolute, not relative, positions of the entity's 4 points in the XY Plane
	public void updateAbsolutePointLocations ()
	{
		//keeps the angle not exactly 0, so slope is never undefined. Still works within unnoticeable margin of error
		if (angle % 90 == 0.0f)
		{
			angle += 0.1f;
		}
		//reinitialize colllision variables
		rad = Math.toRadians((double)(angle + 90.0f));
		diagonal = Math.sqrt(Math.pow(halfSize * xScl, 2) + Math.pow(halfSize * yScl, 2));
		diagAngle = Math.atan((halfSize * xScl) / (halfSize * yScl));
		
		colPoints[0].setX((float)(Math.cos(this.rad + diagAngle) * diagonal) + xPos);
		colPoints[0].setY((float)(Math.sin(this.rad + diagAngle) * diagonal) + yPos);
		colPoints[1].setX((float)(Math.cos(this.rad + Math.PI - diagAngle) * diagonal) + xPos);
		colPoints[1].setY((float)(Math.sin(this.rad + Math.PI - diagAngle) * diagonal) + yPos);
		colPoints[2].setX((float)(Math.cos(this.rad - diagAngle) * diagonal) + xPos);
		colPoints[2].setY((float)(Math.sin(this.rad - diagAngle) * diagonal) + yPos);
		colPoints[3].setX((float)(Math.cos(this.rad - Math.PI + diagAngle) * diagonal) + xPos);
		colPoints[3].setY((float)(Math.sin(this.rad - Math.PI + diagAngle) * diagonal) + yPos);
	}
	
	//This tests for collision between two entities (no shit) - Devin
	public boolean isColliding (Entity ent)
	{	
		//checks to see if either object is not solid
		if (this.isSolid == false || ent.isSolid == false)
		{
			return false;
		}
		
		//hurr durr
		updateAbsolutePointLocations();
		ent.updateAbsolutePointLocations();
		
		//makes sure the entities are close enough so that collision testing is actually neccessary
		if (Math.sqrt(Math.pow(xPos - ent.xPos, 2) + Math.pow(yPos - ent.yPos, 2)) > ((diagonal) + ent.diagonal))
		{
			return false;
		}
		
		//used to determine the number of collisions with respect to the axes. if it is 4 after the check, method returns true
		int colCount = 0;
		float ent1High, ent1Low, ent2High, ent2Low;
		
		//calculates 4 slopes to use with the SAT
		colSlopes[0] = ((this.colPoints[0].getY() - this.colPoints[1].getY()) / (this.colPoints[0].getX() - this.colPoints[1].getX()));
		colSlopes[1] = -1 / colSlopes[0];
		colSlopes[2] = ((ent.colPoints[0].getY() - ent.colPoints[1].getY()) / (ent.colPoints[0].getX() - ent.colPoints[1].getX()));
		colSlopes[3] = -1 / colSlopes[2];
		
		//checks for collision on each of the 4 slopes
		for (float slope : colSlopes)
		{
			//TODO get a more reliable system. Maybe a world is offset by a value of 1,000,000. Constants like this are NEVER a good idea.
			ent1High = -999999.0f;
			ent2High = -999999.0f;
			ent1Low = 999999.0f;
			ent2Low = 999999.0f;
			//iterates through each point on ent1 to get high and low c values
			for (Point point : this.colPoints)
			{
				//finds c (as in y = mx + c) for the line going through each point
				point.setColC((point.getY() - slope * point.getX()));
				if (ent1Low > point.getColC())
				{
					ent1Low = point.getColC();
				}
				if (ent1High < point.getColC())
				{
					ent1High = point.getColC();
				}
			}
			
			//same as above for ent2
			for (Point point : ent.colPoints)
			{
				point.setColC(point.getY() - slope * point.getX());
				if (ent2Low > point.getColC())
				{
					ent2Low = point.getColC();
				}
				if (ent2High < point.getColC())
				{
					ent2High = point.getColC();
				}
			}
			
			//checks for collision with respect to the current axis. adds one to colCount if the collision is true, and returns false if not
			if ((ent1High >= ent2Low && ent1High <= ent2High) || (ent2High >= ent1Low && ent2High <= ent1High))
			{
				colCount++;
			}
			else
			{
				return false;
			}
		}
		
		//if the objects are colliding with respect to all 4 axes, return true
		if (colCount == 4)
		{
			colList.add(ent);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void setTexture (int ptr)
	{
		texturePtr = ptr;
	}
	
	public void setTilesetCoords (int x, int y)
	{
		texture = TilesetHelper.getTextureVertices(x, y, 0, 7);
		allocTextureBuffer();
	}
	
	public void setTilesetCoords (int tileID)
	{
		texture = TilesetHelper.getTextureVertices(tileID);
		allocTextureBuffer();
	}
	
	private void allocTextureBuffer()
	{
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
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
}
