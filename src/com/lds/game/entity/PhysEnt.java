package com.lds.game.entity;

import java.util.ArrayList;

import com.lds.game.Game;
import com.lds.game.SoundPlayer;
import com.lds.Stopwatch;
import com.lds.Vector2f;

public abstract class PhysEnt extends Entity //physics objects are movable, such as doors, blocks, etc.
{
	//interpolation data
	public float interpAngle, endAngle;
	public int moveTimeMs, rotTimeMs, sclTimeMs;
	protected float moveSpeed, rotSpeed, sclSpeed;
	public boolean isMoving, isRotating, isScaling, isRotatingCCW;
	protected Vector2f moveVec, moveInterpVec, endPosVec;
	protected Vector2f sclVec, sclInterpVec, endScaleVec;
	protected int moveInterpCount, sclInterpCount;
	protected ArrayList<Vector2f> bounceList;
	
	
	public PhysEnt(float size, float xPos, float yPos, boolean circular, boolean willCollide, float moveSpeed, float rotSpeed, float sclSpeed)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, circular, willCollide, moveSpeed, rotSpeed, sclSpeed);
	}
	
	public PhysEnt(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide, float moveSpeed, float rotSpeed, float sclSpeed)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, willCollide);
		
		//initialize interpolation variables
		endAngle = angle;
		this.moveSpeed = moveSpeed;
		this.rotSpeed = rotSpeed;
		this.sclSpeed = sclSpeed;
		moveVec = new Vector2f();
		sclVec = new Vector2f();
		moveInterpVec = new Vector2f();
		moveInterpCount = 0;
		sclInterpCount = 0;
		bounceList = new ArrayList<Vector2f>();
	}
	
	@Override
	public void update()
	{
		super.update();
		moveInterpolate();
		rotateInterpolate();
		scaleInterpolate();
	}
	
	//happens upon touching a tile at all
	public void tileInteract(Tile tile)
	{
		
	}
	
	//happens upon ceasing to touch or be on a tile
	public void tileUninteract(Tile tile)
	{
		
	}
	
	//runs with the nearestTile only
	public void onTileInteract(Tile tile)
	{
		if (tile != null)
		{
			if (tile.isPit())
			{
				this.scaleTo(0, 0);
				this.moveTo(tile.getXPos(), tile.getYPos());
				SoundPlayer.getInstance().playSound(SoundPlayer.PIT_FALL);
			}
		}
	}
	
	public void renderNextFrame()
	{
		
	}
	
	/***************************************
	 * Interpolated Transformation Methods *
	 ***************************************/
	
	//sets position to to new x and y and interpolates
	public void moveTo (float x, float y)
	{
		if (!(posVec.getX() == x && posVec.getY() == y))
		{
			moveInterpCount = 0;
			moveVec.set(x - getXPos(), y - getYPos());
			isMoving = true;
			moveTimeMs = Stopwatch.elapsedTimeMs();
			endPosVec = new Vector2f(x, y);
		}
	}
	
	public void moveTo (Vector2f moveToVec)
	{
		this.moveTo(moveToVec.getX(), moveToVec.getY());
	}
	
	//sets angle of an entity to a new value
	public void rotateTo (float degrees)
	{
		//clamp between 0 and 360
		if (degrees == 360.0f)
			degrees = 0.0f;
		else if (degrees > 360.0f)
			degrees -= 360.0f * (int)(degrees/360);
		else if (degrees < 0.0f)
			degrees = degrees + 360;
		
		endAngle = degrees;
		float dist = endAngle - angle;
		float absDist = Math.abs(dist);
		
		if (absDist < 10.0f || absDist > 350.0f)
		{
			this.setAngle(degrees);
			isRotating = false;
		}
		else
		{
			//figures out whether the angle crossed 0/360 degrees
			boolean crossing;
			
			if (angle >= 0 && angle < 180)
			{
				if (endAngle > angle + 180)
					crossing = true;
				else
					crossing = false;
			}
			else
			{
				if (endAngle < angle - 180)
					crossing = true;
				else
					crossing = false;
			}
			
			//figures out whether it needs to rotate CCW or CW
			if (crossing)
			{
				if (absDist > 350.0f)
				{
					this.setAngle(degrees);
					isRotating = false;
				}
				else if (dist > 0)
					isRotatingCCW = false;
				else
					isRotatingCCW = true;
			}
			else
			{
				if (dist > 0)
					isRotatingCCW = true;
				else
					isRotatingCCW = false;
			}
			
			isRotating = true;
			rotTimeMs = Stopwatch.elapsedTimeMs();
		}
	}
	
	//sets scaling of an entity to a new value
	//note, we will probably never scale to 0.0f (that will be infinitely small) or negative numbers (you can get the same results with positives)
	public void scaleTo (float x, float y)
	{
		sclInterpCount = 0;
		sclVec.set(x - getXScl(), y - getYScl());
		isScaling = true;
		sclTimeMs = Stopwatch.elapsedTimeMs();
		endScaleVec = new Vector2f(x, y);
	}
	
	//much like moveTo, but instead of going to a specific point, move() moves relative to the current position
	public void move (float x, float y)
	{
		if (!(x == 0 && y == 0))
		{
			moveInterpCount = 0;
			moveVec.set(x, y);
			isMoving = true;
			moveTimeMs = Stopwatch.elapsedTimeMs();
			endPosVec = Vector2f.add(moveVec, posVec);
			Game.worldOutdated = true;
		}
	}
	
	public void moveBy (Vector2f moveByVec)
	{
		this.move(moveByVec.getX(), moveByVec.getY());
	}
	
	//much like rotateTo, but rotate() adds or subtracts the number of degrees from the current number
	//i.e. ent1 is rotated 30 degrees, if you do ent1.rotate(30.0f) it will be at 60 degrees
	public void rotate (float degrees)
	{
		rotateTo(angle + degrees);
	}
	
	//scales relative to current scaling
	//i.e. if ent1 is scaled (2.0f, 2.0f), if you do ent1.scale(3.0f, 3.0f) the final scaling will be (6.0f, 6.0f)
	public void scale (float x, float y)
	{
		sclInterpCount = 0;
		sclVec.set((x - 1) * getXScl(), (y - 1) * getYScl());
		isScaling = true;
		sclTimeMs = Stopwatch.elapsedTimeMs();
		endScaleVec = Vector2f.add(sclVec, scaleVec);
		Game.worldOutdated = true;
	}
	
	/*********************
	 * Collision Methods *
	 *********************/
	
	@Override
	public void circleBounce (Entity ent)
	{
		Vector2f bounceSide = Vector2f.sub(this.posVec, ent.posVec);
		bounceSide.setNormal();
		bounceSide.normalize();
		bounceSide.scale(2 * moveInterpVec.dot(bounceSide));
		this.addBounceVec(Vector2f.sub(bounceSide, moveInterpVec));
	}

	@Override
	public void rectangleBounce (Entity ent)
	{
		//checks if this entity is moving; if it is not, do nothing
		if (this.moveInterpVec.getX() == 0.0f && this.moveInterpVec.getY() == 0.0f)
			return;
		
		Vector2f[] vertDistVecs = new Vector2f[4];

		for (int i = 0; i < 4; i++)
			vertDistVecs[i] = Vector2f.sub(ent.vertVecs[i], Vector2f.sub(posVec, moveInterpVec));
	
		//goes through the vectors and sorts them from low to high (thanks Mr. Carlson)
		int maxPos;
		Vector2f temp = new Vector2f();
	    for (int k = vertDistVecs.length; k >= 2; k--)
	    {
	    	maxPos = 0; 
	        for (int i = 1; i < k; i++) 
	        {
	             if (vertDistVecs[i].mag() > vertDistVecs[maxPos].mag()) 
	                  maxPos = i; 
	        }
	        temp.set(vertDistVecs[maxPos]); 
	        vertDistVecs[maxPos].set(vertDistVecs[k-1]); 
	        vertDistVecs[k-1].set(temp);
	    }
	    
	    Vector2f bounceNormal = Vector2f.sub(vertDistVecs[0], vertDistVecs[1]).setNormal().normalize();
	    Vector2f mpDist = Vector2f.getMidpoint(vertDistVecs[0].add(this.posVec), vertDistVecs[1].add(this.posVec)).sub(ent.posVec);
		if (mpDist.mag() > mpDist.add(bounceNormal).mag())
			bounceNormal.neg();
	    
		//calculate the bounceVec direction
		Vector2f bounceVec = Vector2f.getNormal(bounceNormal).scale(Vector2f.getNormal(bounceNormal).dot(this.moveInterpVec)).sub(Vector2f.scale(bounceNormal, bounceNormal.dot(this.moveInterpVec))).normalize();
		
		//get min of projection of this entity
		float thisMin = bounceNormal.dot(this.vertVecs[0]);
		float thisMax = thisMin;
		for (int i = 1; i < this.vertVecs.length; i++)
		{
			float dotProd1 = bounceNormal.dot(this.vertVecs[i]);
			if (dotProd1 < thisMin)
				thisMin = dotProd1;
			if (dotProd1 > thisMax)
				thisMax = dotProd1;
		}
		
		//get max of projection of ent
		float entMax = bounceNormal.dot(ent.vertVecs[0]);
		float entMin = entMax;
		for (int i = 1; i < ent.vertVecs.length; i++)
		{
			float dotProd2 = bounceNormal.dot(ent.vertVecs[i]);
			if (dotProd2 < entMin)
				entMin = dotProd2;
			if (dotProd2 > entMax)
				entMax = dotProd2;
		}
		
		//scale bounceVec by magnitude and add to bounceList
		this.addBounceVec(bounceVec.scale((entMax - thisMin) / (float)Math.cos(bounceVec.angle(bounceNormal))));
	}
	
	/**********************************
	 * Instant Transformation Methods *
	 **********************************/
	
	//mutators for position
	public void setPos (Vector2f v)
	{
		moveInterpVec = Vector2f.sub(v, posVec);
		posVec.set(v);
		Game.worldOutdated = true;
	}
	
	public void setPos (float x, float y)
	{
		setPos(new Vector2f(x, y));
	}
	
	//mutator for angle
	public void setAngle (float degrees)
	{	
		//clamp angle between 0 and 360
		if (degrees == 360.0f)
			degrees = 0.0f;
		else if (degrees > 360.0f)
			degrees -= 360 * (int)(degrees / 360);
		else if (degrees < 0.0f)
			degrees += 360;
		
		angle = degrees;
		endAngle = degrees;
		Game.worldOutdated = true;
	}
	
	//mutator for scale
	public void setScale (float x, float y)
	{
		sclVec.set(x, y);
		Game.worldOutdated = true;
	}
	
	/*************************
	 * Interpolation Methods *
	 *************************/
	
	public void moveInterpolate ()
	{	
		//if the object needs to be interpolated
		if (isMoving)
		{
			if(moveSpeed <= 0)
			{
				moveSpeed = 0;
				isMoving = false;
				moveInterpCount = 0;
			}
			else
			{
				moveInterpCount++;
				moveInterpVec = Vector2f.scale(Vector2f.normalize(moveVec), moveSpeed / 1000 * (Stopwatch.elapsedTimeMs() - moveTimeMs));
				Game.worldOutdated = true;
				
				if (moveVec.mag() - moveInterpVec.mag() * moveInterpCount <= moveInterpVec.mag() / 2)
				{
					moveInterpVec.set(0, 0);
					posVec = endPosVec;
					isMoving = false;
					moveInterpCount = 0;
				}
				else
				{
					posVec.add(moveInterpVec);
					Game.worldOutdated = true;
				}
			}
				
			moveTimeMs = Stopwatch.elapsedTimeMs();
		}
	}
	
	public void rotateInterpolate ()
	{
		if (isRotating)
		{
			float increment = (float)(Stopwatch.elapsedTimeMs() - rotTimeMs);
			
			float dist = Math.abs(endAngle - angle);
			
			if (isRotatingCCW)
			{	
				if ((dist > 180 && angle < endAngle) || (dist < 180 && angle > endAngle))
				{
					this.setAngle(endAngle);
					isRotating = false;
				}
				else
					this.setAngle(angle + rotSpeed / 1000 * increment);
			}
			else
			{
				if ((dist > 180 && angle > endAngle) || (dist < 180 && angle < endAngle))
				{
					this.setAngle(endAngle);
					isRotating = false;
				}
				else
					this.setAngle(angle - rotSpeed / 1000 * increment);
			}	
			rotTimeMs = Stopwatch.elapsedTimeMs();
			
			Game.worldOutdated = true;
		}
	}
	
	public void scaleInterpolate ()
	{
		if (isScaling)
		{
			if(sclSpeed <= 0)
			{
				sclSpeed = 0;
				isScaling = false;
				sclInterpCount = 0;
			}
			else 
			{
				sclInterpCount++;
				sclInterpVec = Vector2f.scale(Vector2f.normalize(sclVec), sclSpeed / 1000 * (Stopwatch.elapsedTimeMs() - sclTimeMs));
				
				if (sclVec.mag() - sclInterpVec.mag() * sclInterpCount <= sclInterpVec.mag())
				{
					sclInterpVec.set(0, 0);
					scaleVec = endScaleVec;
					isScaling = false;
					sclInterpCount = 0;
				}
				else
				{
					scaleVec.add(sclInterpVec);
				}
				
				Game.worldOutdated = true;
			}
							
			sclTimeMs = Stopwatch.elapsedTimeMs();
		}
	}
	
	public void stop()
	{
		isMoving = false;
		isRotating = false;
		isScaling = false;
	}
	
	public Vector2f getBounceVec()
	{
	 	if (bounceList.isEmpty())
		{
			return new Vector2f();
		}
		else
		{
			Vector2f bounceVec = new Vector2f(bounceList.get(0));
			for (int i = 1; i < bounceList.size(); i++)
			{
				bounceVec.add(bounceList.get(i));
			}
			bounceList.clear();
			return bounceVec;
		}
	}
	
	public void addBounceVec (Vector2f v)
	{
		bounceList.add(v);
	}
	
	public Vector2f getBounceVec(int index)
	{
		return bounceList.get(index);
	}
	
	public int getBounceListSize()
	{
		return bounceList.size();
	}
	
	public Vector2f getMoveInterpVec ()
	{
		return moveInterpVec;
	}
	
	public void setMoveInterpVec (Vector2f v)
	{
		moveInterpVec = v;
	}
	
	public void setIsMoving(boolean isMoving)
	{
		this.isMoving = isMoving;
	}
	
	public void setIsRotating(boolean isRotating)
	{
		this.isRotating = isRotating;
	}
	
	public void setIsScaling(boolean isScaling)
	{
		this.isScaling = isScaling;
	}
	
	public float getMoveSpeed()
	{
		return moveSpeed;
	}
}
