package com.lds.game;

import javax.microedition.khronos.opengles.GL10;

import com.lds.Point;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//Highest level abstract class in game

public abstract class Entity 
{
	//behavior data
	public boolean isStatic, isSolid;
	
	//graphics data
	public float angle, size, xPos, yPos, xScl, yScl, halfSize;
	public float[] vertices;
	public FloatBuffer vertexBuffer;
		
	//interpolation data
	public float interpSlope, interpSclRatio, endX, endY, endXScl, endYScl, endAngle;
	public boolean posInterpMove, posInterpScl, posInterpRotate;
	
	//debug data
	public int entID;
	public boolean isRendered;
	public static int entCount;
	
	//collision data
	public Point[] colPoints;
	public float[] colSlopes;
	public double diagonal, rad, diagAngle;
	
	//used to initialize graphics data
	//TODO possibly get this working under the constructor?
	public void initialize (float _size, float _xPos, float _yPos, float _angle, float _xScl, float _yScl, boolean _isStatic, boolean _isSolid)
	{
		//initialize behavior variables
		isStatic = _isStatic;
		isSolid = _isSolid;
		
		//initializes graphics variables
		size = _size;
		xPos = _xPos;
		yPos = _yPos;
		angle = _angle;
		xScl = _xScl;
		yScl = _yScl;
		halfSize = size / 2;
		
		//initialize interpolation variables
		endX = xPos;
		endY = yPos;
		endXScl = xScl;
		endYScl = yScl;
		endAngle = angle;
		
		//initializes collision variables
		rad = Math.toRadians((double)(angle + 90.0f));
		diagonal = Math.sqrt(Math.pow(halfSize * xScl, 2) + Math.pow(halfSize * yScl, 2)); //distance from center to corner
		diagAngle = Math.asin((size * xScl / 2) / diagonal); //angle between vertical line and diagonal to top left corner
		colSlopes = new float[4];
		
		colPoints = new Point[4]; //0: top left, 1: bottom left, 2:top right, 3: borrom right
		colPoints[0] = new Point();
		colPoints[1] = new Point();
		colPoints[2] = new Point();
		colPoints[3] = new Point();
		
		//makes it so x/yPos are in center of box - Robert
		float[] initVerts = {	halfSize, halfSize, //top left
								halfSize, -halfSize, //bottom left
								-halfSize, halfSize, //top right
								-halfSize, -halfSize }; //bottom right

		vertices = initVerts;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}
	
	public void initialize (float _size, float _xPos, float _yPos)
	{
		initialize(_size, _xPos, _yPos, 0.0f, 1.0f, 1.0f, false, true);
	}
	
	public void draw(GL10 gl)
	{
		gl.glFrontFace(GL10.GL_CW);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 2);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	//TODO interpolate to position, per frame (ie. a loop inside these methods won't work)
	public void moveTo (float x, float y)
	{
		if (!isStatic)
		{
			interpSlope = (y - yPos) / (x - xPos);
			if (x - xPos > 0)
				posInterpMove = true;
			else
				posInterpMove = false;
			endX = x;
			endY = y;
		}
		else
		{
			System.out.println("Error. Cannot move static object.");
		}
	}
	
	public void rotateTo (float degrees)
	{
		endAngle = degrees;
		if (degrees - angle > 0)
		{
			posInterpRotate = true;
		}
		else
		{
			posInterpRotate = false;
		}
	}
	
	public void scaleTo (float x, float y)
	{
		if (!isStatic)
		{
			interpSclRatio = (y - yScl) / (x - xScl);
			if (x - xScl > 0)
				posInterpScl = true;
			else
				posInterpScl = false;
			endXScl = x;
			endYScl = y;
		}
		else
		{
			System.out.println("Error. Cannot scale static object.");
		}
	}
	
	public void move (float x, float y)
	{
		if (!isStatic)
		{
			interpSlope = y / x;
			if (x > 0)
				posInterpMove = true;
			else
				posInterpMove = false;
			endX = xPos + x;
			endY = yPos + y;
		}
		else
		{
			System.out.println("Error. Cannot move static object.");
		}
	}
	
	public void rotate (float degrees)
	{
		endAngle = angle + degrees;
		if (degrees > 0)
		{
			posInterpRotate = true;
		}
		else
		{
			posInterpRotate = false;
		}
	}
	
	public void scale (float x, float y)
	{
		if (!isStatic)
		{
			interpSclRatio = y / x;
			if (x > 0)
				posInterpScl = true;
			else
				posInterpScl = false;
			endXScl = xScl + x;
			endYScl = yScl + y;
		}
		else
		{
			System.out.println("Error. Cannot scale static object.");
		}
	}
	
	//used to get the absolute, not relative, positions of the entity's 4 points in the XY Plane
	public void updateAbsolutePointLocations ()
	{
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
		
		//makes sure the entities are close enough so that collision testing is actually neccessary
		if (Math.sqrt(Math.pow(xPos - ent.xPos, 2) + Math.pow(yPos - ent.yPos, 2)) > ((diagonal) + ent.diagonal))
		{
			return false;
		}
		updateAbsolutePointLocations();
		ent.updateAbsolutePointLocations();
		//used to determine the number of collisions with respect to the axes. if it is 4 after the check, method returns true
		int colCount = 0;
		float ent1High, ent1Low, ent2High, ent2Low;
		
		//calculates 4 slopes to use with the SAT
		if (this.colPoints[0].getX() - this.colPoints[1].getX() == 0.0f)
		{
			colSlopes[0] = 1.0E16f;
		}
		else if (this.colPoints[0].getY() - this.colPoints[1].getY() == 0.0f)
		{
			colSlopes[1] = 1.0E16f;
		}
		else
		{
			colSlopes[0] = ((this.colPoints[0].getY() - this.colPoints[1].getY()) / (this.colPoints[0].getX() - this.colPoints[1].getX()));
			colSlopes[1] = -1 / colSlopes[0];
		}
		
		if (ent.colPoints[0].getX() - ent.colPoints[1].getX() == 0.0f)
		{
			colSlopes[2] = 1.0E16f;
		}
		else if (ent.colPoints[0].getY() - ent.colPoints[1].getY() == 0.0f)
		{
			colSlopes[3] = 1.0E16f;
		}
		else
		{
			colSlopes[2] = ((ent.colPoints[0].getY() - ent.colPoints[1].getY()) / (ent.colPoints[0].getX() - ent.colPoints[1].getX()));
			colSlopes[3] = -1 / colSlopes[2];
		}

		//checks for collision on each of the 4 slopes
		for (float slope : colSlopes)
		{
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
			return true;
		}
		else
		{
			return false;
		}
	}
}