package com.lds.game;

import javax.microedition.khronos.opengles.GL10;

import com.lds.Point;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//Highest level abstract class in game

public abstract class Entity 
{
	//graphics data
	public float angle, size, xPos, yPos, xScl, yScl;
	public float[] vertices;
	public FloatBuffer vertexBuffer;
	
	//debug data
	public int entID;
	public boolean isRendered;
	public static int entCount;
	
	//collision data
	public Point[] colPoints;
	public float[] colSlopes;
	public double diagonal, rad;
	
	//used to initialize graphics data
	//TODO possibly get this working under the constructor?
	public void initialize (float _size, float _xPos, float _yPos, float _angle, float _xScl, float _yScl)
	{
		//initializes graphics variables
		size = _size;
		xPos = _xPos;
		yPos = _yPos;
		angle = -_angle;
		xScl = _xScl;
		yScl = _yScl;
		
		float halfSize = size / 2;
		
		//initializes collision variables
		rad = Math.toRadians((double)(angle + 90.0f));
		diagonal = size * Math.sqrt(2);
		colSlopes = new float[4];
		
		colPoints = new Point[4]; //0: top left, 1: bottom left, 2:top right, 3: borrom right
		colPoints[0] = new Point(halfSize, halfSize);
		colPoints[1] = new Point(halfSize, -halfSize);
		colPoints[2] = new Point(-halfSize, halfSize);
		colPoints[3] = new Point(-halfSize, -halfSize);
		
		updateAbsolutePointLocations();	
		
		//make it so x/yPos are in center of box - Robert
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
		
		Game.entList.add(this);
	}
	
	public void initialize (float _size, float _xPos, float _yPos)
	{
		initialize(_size, _xPos, _yPos, 0.0f, 1.0f, 1.0f);
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
		this.xPos = x;
		this.yPos = y;
	}
	
	public void rotateTo (float degrees)
	{
		this.angle = -degrees;
	}
	
	public void scaleTo (float x, float y)
	{
		this.xScl = x;
		this.yScl = y;
	}
	
	public void move (float x, float y)
	{
		this.xPos += x;
		this.yPos += y;
	}
	
	public void rotate (float degrees)
	{
		this.angle -= degrees;
	}
	
	public void scale (float x, float y)
	{
		this.xScl += x;
		this.yScl += y;
	}
	
	//used to get the absolute, not relative, positions of the entity's 4 points in the XY Plane
	public void updateAbsolutePointLocations ()
	{
		//TODO: calculate in scaling
		colPoints[0].setX((float)(Math.cos(this.rad + (Math.PI / 4)) * diagonal / 2) + xPos);
		colPoints[0].setY((float)(Math.sin(this.rad + (Math.PI / 4)) * diagonal / 2) + yPos);
		colPoints[1].setX((float)(Math.cos(this.rad + (3 * Math.PI / 4)) * diagonal / 2) + xPos);
		colPoints[1].setY((float)(Math.sin(this.rad + (3 * Math.PI / 4)) * diagonal / 2) + yPos);
		colPoints[2].setX((float)(Math.cos(this.rad - (Math.PI / 4)) * diagonal / 2) + xPos);
		colPoints[2].setY((float)(Math.sin(this.rad - (Math.PI / 4)) * diagonal / 2) + yPos);
		colPoints[3].setX((float)(Math.cos(this.rad - (3 * Math.PI / 4)) * diagonal / 2) + xPos);
		colPoints[3].setY((float)(Math.sin(this.rad - (3 * Math.PI / 4)) * diagonal / 2) + yPos);
		System.out.println(colPoints[0] + "\n" + colPoints[1] + "\n" + colPoints[2] + "\n" + colPoints[3]);
	}
	
	//This tests for collision between two entities (no shit) - Devin
	public boolean isColliding (Entity ent)
	{
		//used to determine the number of collisions with respect to the axes. if it is 4 after the check, method returns true
		int colCount = 0;
		//as if used as ent1.isColliding(ent2)
		float ent1High = 0.0f;
		float ent2High = 0.0f;
		float ent1Low = 999999.0f;
		float ent2Low = 999999.0f;
		
		//calculates 4 slopes to use with the SAT
		colSlopes[0] = (float)(Math.tan(this.rad));
		colSlopes[1] = -1 / colSlopes[0];
		colSlopes[2] = (float)(Math.tan(ent.rad));
		colSlopes[3] = -1 / colSlopes[2];
		
		//checks for collision on each of the 4 slopes
		for (float slope : colSlopes)
		{
			ent1High = 0.0f;
			ent2High = 0.0f;
			ent1Low = 999999.0f;
			ent2Low = 999999.0f;
			System.out.println("Slope: " + slope);
			//iterates through each point on ent1 to get high and low c values
			for (Point point : this.colPoints)
			{
				//finds c (as in y = mx + c) for the line going through each point
				if (slope < 1.0E-16f)
				{
					slope = 0;
				}
				point.setColC((point.getY() - slope * point.getX()));
				System.out.println("C1: " + point.getColC());
				if (ent1Low > point.getColC())
				{
					ent1Low = point.getColC();
				}
				if (ent1High < point.getColC())
				{
					ent1High = point.getColC();
				}
			}
			System.out.println("ent1Low: " + ent1Low);
			System.out.println("ent1High: " + ent1High);
			
			//same as above for ent2
			for (Point point : ent.colPoints)
			{
				if (slope < 1.0E-16f)
				{
					slope = 0;
				}
				point.setColC(point.getY() - slope * point.getX());
				System.out.println("C2: " + point.getColC());
				if (ent2Low > point.getColC())
				{
					ent2Low = point.getColC();
				}
				if (ent2High < point.getColC())
				{
					ent2High = point.getColC();
				}
			}
			System.out.println("ent2Low: " + ent2Low);
			System.out.println("ent2High: " + ent2High);
			
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
