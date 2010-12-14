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
	public Point colTL, colTR, colBR, colBL;
	public double diagonal, rad;
	
	//used to initialize graphics data
	//TODO possibly get this working under the constructor?
	public void initialize (float _size, float _xPos, float _yPos, float _angle, float _xScl, float _yScl)
	{
		//initializes graphics variables
		size = _size;
		xPos = _xPos;
		yPos = _yPos;
		angle = _angle;
		xScl = _xScl;
		yScl = _yScl;
		
		float halfSize = size / 2;
		
		//initializes collision variables
		rad = Math.toRadians((double)(angle + 90.0f));
		diagonal = size * Math.sqrt(2);
		
		colTL = new Point(-halfSize, -halfSize);
		colBL = new Point(-halfSize, halfSize);
		colTR = new Point(halfSize, -halfSize);
		colBR = new Point(halfSize, halfSize);
		
		//updateAbsolutePointLocations();	
		
		//make it so x/yPos are in center of box - Robert
		float[] initVerts = {	-halfSize, -halfSize, //top left
								-halfSize, halfSize, //bottom left
								halfSize, -halfSize, //top right
								halfSize, halfSize }; //bottom right

		vertices = initVerts;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
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
	public void move (float x, float y)
	{
		this.xPos = x;
		this.yPos = y;
	}
	
	public void rotate (float degrees)
	{
		this.angle = degrees;
	}
	
	public void scale (float x, float y)
	{
		this.xScl = x;
		this.yScl = y;
	}
	
	//used to get the absolute, not relative, positions of the entity's 4 points in the XY Plane
	public void updateAbsolutePointLocations ()
	{
		colTL.setX((float)(Math.cos(rad + (Math.PI / 4)) * diagonal / 2) + xPos);
		colTL.setY((float)(Math.sin(rad + (Math.PI / 4)) * diagonal / 2) + yPos);
		colBL.setX((float)(Math.cos(rad + (3 * Math.PI / 4)) * diagonal / 2) + xPos);
		colBL.setY((float)(Math.sin(rad + (3 * Math.PI / 4)) * diagonal / 2) + yPos);
		colTR.setX((float)(Math.cos(rad - (Math.PI / 4)) * diagonal / 2) + xPos);
		colTR.setY((float)(Math.sin(rad - (Math.PI / 4)) * diagonal / 2) + yPos);
		colBR.setX((float)(Math.cos(rad - (3 * Math.PI / 4)) * diagonal / 2) + xPos);
		colBR.setY((float)(Math.sin(rad - (3 * Math.PI / 4)) * diagonal / 2) + yPos);
	}
	
	/********
	 * shit *
	 ********
	 
	public Point2D.Double  colBR, bottomRight, center;
	public double angle, height, width, diagonal, initialAngle;
	public boolean isRendered;
	
	public void initialize ()
	{
		height = topLeft.y - bottomRight.y;
		width = bottomRight.x - topLeft.x;
		diagonal = Math.sqrt((Math.pow (width, 2)) + Math.pow(height, 2));
		center.x = (width / 2) +  topLeft.x;
		center.y = (height / 2) +  topLeft.y;
		angle = 180 - Math.toDegrees(Math.atan(topLeft.y - center.y) / (center.x - topLeft.x)); //the angle is the angle between the center and the topLeft point
		initialAngle = angle;	
	}
	
	public boolean isColliding (Entity ent)
	{
		if ((topLeft.x >= ent.topLeft.x && topLeft.x <= ent.bottomRight.x || bottomRight.x >= ent.topLeft.x && bottomRight.x <= ent.bottomRight.x) 
				&& (topLeft.y <= ent.topLeft.y && topLeft.y >= ent.bottomRight.y || bottomRight.y <= ent.topLeft.y && bottomRight.y >= ent.bottomRight.y))
		{
			return true;
		}
		else
			return false;
	}
	
	public void move (dir moveDir, double distance)
	{
		if (moveDir == dir.up)
		{
			topLeft.y += distance;
			bottomRight.y += distance;
		}
		else if (moveDir == dir.down)
		{
			topLeft.y -= distance;
			bottomRight.y -= distance;
		}
		else if (moveDir == dir.right)
		{
			topLeft.x += distance;
			bottomRight.x += distance;
		}
		else
		{
			topLeft.x -= distance;
			bottomRight.x -= distance;
		}
	}
	
	public void move (double distance)
	{
		double tempAngle = angle - initialAngle + 90; //this will be the operable direction. if the Entity has just been created, this will point straight up
		double xShift = distance * Math.cos(Math.toRadians(tempAngle));
		double yShift = distance * Math.sin(Math.toRadians(tempAngle));
		double finalX = topLeft.x + xShift; //used in following while loop
		while (topLeft.x != finalX) //slowly increments movement, so it appears smooth
		{
			topLeft.x++; //for now, just add one each time. later, variable speed will be added
			bottomRight.x++;
			topLeft.y += (yShift / xShift);
			bottomRight.y += (yShift / xShift);
		}
	}
	
	public void rotate (double degrees) //positive degrees will rotate counterclockwise, negative degrees clockwise
	{
		angle += degrees;
		double rad = Math.toRadians(angle);
		topLeft.x = (Math.cos(rad) * diagonal / 2) + xPos;
		topLeft.y = (Math.sin(rad) * diagonal / 2) + yPos;
		bottomRight.x = (Math.cos(rad + Math.PI) * diagonal / 2) + xPos;
		bottomRight.x = (Math.cos(rad + Math.PI) * diagonal / 2) + yPos;
	}
	 **********
	 *end shit*
	 **********/
}
