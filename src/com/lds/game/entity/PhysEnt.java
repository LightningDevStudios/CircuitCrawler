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
	protected float moveSpeed, rotSpeed, sclSpeed;
	public boolean isMoving, isRotating, isScaling, isRotatingCCW, falling, gettingPushed;
	protected Vector2f moveVec, moveInterpVec, endPosVec, movedVec;
	protected Vector2f sclVec, sclInterpVec, endScaleVec;
	protected int moveInterpCount, sclInterpCount;
	protected ArrayList<Vector2f> bounceList;
	protected float friction;
	
	
	public PhysEnt(float size, float xPos, float yPos, boolean circular, boolean willCollide, float moveSpeed, float rotSpeed, float sclSpeed, float friction)
	{
		this(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, circular, willCollide, moveSpeed, rotSpeed, sclSpeed, friction);
	}
	
	public PhysEnt(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide, float moveSpeed, float rotSpeed, float sclSpeed, float friction)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, willCollide);
		
		//initialize interpolation variables
		endAngle = angle;
		this.moveSpeed = moveSpeed;
		this.rotSpeed = rotSpeed;
		this.sclSpeed = sclSpeed;
		this.friction = friction;
		moveVec = new Vector2f();
		sclVec = new Vector2f();
		moveInterpVec = new Vector2f();
		sclInterpVec = new Vector2f();
		moveInterpCount = 0;
		sclInterpCount = 0;
		bounceList = new ArrayList<Vector2f>();
		falling = false;
		gettingPushed = false;
		movedVec = new Vector2f();
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
	public void tileInteract (Tile tile)
	{
		
	}
	
	//happens upon ceasing to touch or be on a tile
	public void tileUninteract(Tile tile)
	{
		
	}
	
	//runs with the nearestTile only
	public void onTileInteract(final Tile tile)
	{
		if (tile != null)
		{
			if (tile.isPit())
			{
				this.stop();
				this.scaleTo(0, 0);
				this.moveTo(tile.getXPos(), tile.getYPos());
				if (!falling)
					SoundPlayer.getInstance().playSound(SoundPlayer.PIT_FALL);
				falling = true;
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
	public void moveTo (final float x, final float y)
	{
		moveInterpVec.set(0, 0);
		isMoving = false;
		movedVec.set(0, 0);
		if (!(posVec.getX() == x && posVec.getY() == y))
		{
			moveInterpCount = 0;
			moveVec.set(x - getXPos(), y - getYPos());
			isMoving = true;
			endPosVec = new Vector2f(x, y);
		}
		movedVec.set(0, 0);
	}
	
	public void moveTo (final Vector2f moveToVec)
	{
		this.moveTo(moveToVec.getX(), moveToVec.getY());
	}
	
	public void push (final float x, final float y)
	{
		gettingPushed = true;
		moveInterpVec.set(x, y);
		isMoving = true;
		Game.worldOutdated = true;
	}
	
	public void push (final Vector2f initVec)
	{
		push(initVec.getX(), initVec.getY());
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
		final float dist = endAngle - angle;
		final float absDist = Math.abs(dist);
		
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
					setAngle(degrees);
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
		}
	}
	
	//sets scaling of an entity to a new value
	//note, we will probably never scale to 0.0f (that will be infinitely small) or negative numbers (you can get the same results with positives)
	public void scaleTo (final float x, final float y)
	{
		sclInterpCount = 0;
		sclVec.set(x - getXScl(), y - getYScl());
		isScaling = true;
		endScaleVec = new Vector2f(x, y);
	}
	
	//much like moveTo, but instead of going to a specific point, move() moves relative to the current position
	public void move (final float x, final float y)
	{
		moveInterpVec.set(0, 0);
		isMoving = false;
		movedVec.set(0, 0);
		if (!(x == 0 && y == 0))
		{
			moveInterpCount = 0;
			moveVec.set(x, y);
			isMoving = true;
			endPosVec = Vector2f.add(moveVec, posVec);
			Game.worldOutdated = true;
		}

	}
	
	public void moveBy (final Vector2f moveByVec)
	{
		move(moveByVec.getX(), moveByVec.getY());
	}
	
	//much like rotateTo, but rotate() adds or subtracts the number of degrees from the current number
	//i.e. ent1 is rotated 30 degrees, if you do ent1.rotate(30.0f) it will be at 60 degrees
	public void rotate (final float degrees)
	{
		rotateTo(angle + degrees);
	}
	
	//scales relative to current scaling
	//i.e. if ent1 is scaled (2.0f, 2.0f), if you do ent1.scale(3.0f, 3.0f) the final scaling will be (6.0f, 6.0f)
	public void scale (final float x, final float y)
	{
		sclInterpCount = 0;
		sclVec.set((x - 1) * getXScl(), (y - 1) * getYScl());
		isScaling = true;
		endScaleVec = Vector2f.add(sclVec, scaleVec);
		Game.worldOutdated = true;
	}
	
	/*********************
	 * Collision Methods *
	 *********************/
	
	@Override
	public void circleBounceAgainstCircle (Entity ent)
	{
		//checks if this entity is moving; if it is not, do nothing
		if (this.moveInterpVec.getX() == 0.0f && this.moveInterpVec.getY() == 0.0f)
			return;
		
		Vector2f bounceNormal = Vector2f.sub(this.posVec, ent.posVec).normalize();
		Vector2f bounceSide = Vector2f.getNormal(bounceNormal);
		
		Vector2f thisProjPoint = Vector2f.sub(posVec, bounceNormal.scale(halfSize));
		float thisMin = bounceNormal.normalize().dot(thisProjPoint);
		Vector2f entProjPoint = Vector2f.add(ent.posVec, bounceNormal.scale(ent.halfSize));
		float entMax = bounceNormal.normalize().dot(entProjPoint);
		addBounceVec(bounceNormal.scale(entMax - thisMin));
		
		if (gettingPushed)
			this.moveInterpVec.set(bounceSide.scale(bounceSide.dot(this.moveInterpVec) * 2).sub(moveInterpVec));
	}
	
	@Override
	public void circleBounceAgainstRectangle (Entity ent)
	{
		ent.updateAbsolutePointLocations();
		 
		//checks if this entity is moving; if it is not, do nothing
		if (this.moveInterpVec.getX() == 0.0f && this.moveInterpVec.getY() == 0.0f)
			return;
		
		Vector2f[] vertDistVecs = new Vector2f[4];

		for (int i = 0; i < 4; i++)
			vertDistVecs[i] = Vector2f.sub(posVec, moveInterpVec).sub(ent.vertVecs[i]);
	
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
	    
	    Vector2f bounceSide = Vector2f.sub(vertDistVecs[0], vertDistVecs[1]).normalize();
	    Vector2f bounceNormal = Vector2f.getNormal(bounceSide);
	    Vector2f mpDist = Vector2f.getMidpoint(vertDistVecs[0].add(this.posVec), vertDistVecs[1].add(this.posVec)).sub(ent.posVec);
		if (mpDist.mag() > mpDist.add(bounceNormal).mag())
			bounceNormal.neg();
		
		//get max of projection of ent
		float entMax = bounceNormal.dot(ent.vertVecs[0]);
		for (int i = 1; i < ent.vertVecs.length; i++)
		{
			final float dotProd2 = bounceNormal.dot(ent.vertVecs[i]);
			 if (dotProd2 > entMax)
				entMax = dotProd2;
		}
		
		Vector2f projPoint = Vector2f.sub(posVec, bounceNormal.scale(halfSize));
		float thisMin = bounceNormal.normalize().dot(projPoint);
		addBounceVec(bounceNormal.scale(entMax - thisMin));
		
		if (gettingPushed)
			this.moveInterpVec.set(bounceSide.scale(bounceSide.dot(this.moveInterpVec) * 2).sub(moveInterpVec));
	}
	
	@Override
	public void rectangleBounceAgainstCircle (Entity ent)
	{
		 this.updateAbsolutePointLocations();
		 
		//checks if this entity is moving; if it is not, do nothing
		if (this.moveInterpVec.getX() == 0.0f && this.moveInterpVec.getY() == 0.0f)
			return;
		
		Vector2f[] vertDistVecs = new Vector2f[4];

		for (int i = 0; i < 4; i++)
			vertDistVecs[i] = Vector2f.sub(this.vertVecs[i], this.moveInterpVec).sub(ent.posVec);
	
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
	    
	    Vector2f bounceSide = Vector2f.sub(vertDistVecs[0], vertDistVecs[1]).normalize();
	    Vector2f bounceNormal = Vector2f.getNormal(bounceSide);
	    Vector2f mpDist = Vector2f.getMidpoint(vertDistVecs[0].add(this.posVec), vertDistVecs[1].add(this.posVec)).sub(ent.posVec);
		if (mpDist.mag() > mpDist.add(bounceNormal).mag())
			bounceNormal.neg();
		
		//get max of projection of ent
		float thisMin = bounceNormal.dot(this.vertVecs[0]);
		for (int i = 1; i < this.vertVecs.length; i++)
		{
			final float dotProd1 = bounceNormal.dot(this.vertVecs[i]);
			if (dotProd1 < thisMin)
				thisMin = dotProd1;
		}
		
		Vector2f projPoint = Vector2f.add(ent.posVec, bounceNormal.scale(ent.halfSize));
		float entMax = bounceNormal.normalize().dot(projPoint);
		addBounceVec(bounceNormal.scale(entMax - thisMin));
		
		if (gettingPushed)
			this.moveInterpVec.set(bounceSide.scale(bounceSide.dot(this.moveInterpVec) * 2).sub(moveInterpVec));
	}

	@Override
	public void rectangleBounceAgainstRectangle (Entity ent) 
	{
		this.updateAbsolutePointLocations();
		ent.updateAbsolutePointLocations();
		
		//checks if this entity is moving; if it is not, do nothing
		if (this.moveInterpVec.getX() == 0.0f && this.moveInterpVec.getY() == 0.0f)
			return;
		
		Vector2f[] vertDistVecs = new Vector2f[4];

		for (int i = 0; i < 4; i++)
			vertDistVecs[i] = Vector2f.sub(posVec, moveInterpVec).sub(ent.vertVecs[i]);
	
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
	    
	    Vector2f bounceSide = Vector2f.sub(vertDistVecs[0], vertDistVecs[1]).normalize();
	    Vector2f bounceNormal = Vector2f.getNormal(bounceSide);
	    Vector2f mpDist = Vector2f.getMidpoint(vertDistVecs[0].add(this.posVec), vertDistVecs[1].add(this.posVec)).sub(ent.posVec);
		if (mpDist.mag() > mpDist.add(bounceNormal).mag())
			bounceNormal.neg();
		
		//get min of projection of this entity
		float thisMin = bounceNormal.dot(this.vertVecs[0]);
		for (int i = 1; i < this.vertVecs.length; i++)
		{
			final float dotProd1 = bounceNormal.dot(this.vertVecs[i]);
			if (dotProd1 < thisMin)
				thisMin = dotProd1;
		}
		
		//get max of projection of ent
		float entMax = bounceNormal.dot(ent.vertVecs[0]);
		for (int i = 1; i < ent.vertVecs.length; i++)
		{
			final float dotProd2 = bounceNormal.dot(ent.vertVecs[i]);
			if (dotProd2 > entMax)
				entMax = dotProd2;
		}
		
		//scale the bounceNormal the the proper magnitude to get the entity out of collision
		addBounceVec(bounceNormal.scale(entMax - thisMin));
		
		if (gettingPushed)
			this.moveInterpVec.set(bounceSide.scale(bounceSide.dot(this.moveInterpVec) * 2).sub(moveInterpVec));
	}
	
	/**********************************
	 * Instant Transformation Methods *
	 **********************************/
	
	public void setPosNoInterp(final float x, final float y)
	{
		posVec.set(x, y);
		Game.worldOutdated = true;
	}
	
	public void setPosNoInterp(final Vector2f v)
	{
		posVec = v;
		Game.worldOutdated = true;
	}
	
	//mutators for position
	public void setPos (final Vector2f v)
	{
		moveInterpVec = Vector2f.sub(v, posVec);
		posVec.set(v);
		Game.worldOutdated = true;
	}
	
	public void addPos (final Vector2f v)
	{
		if (!gettingPushed && (v.getX() != 0.0f || v.getY() != 0.0f))
			moveInterpVec.set(v);
		posVec.add(v);
		Game.worldOutdated = true;
	}
	
	public void setPos (final float x, final float y)
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
	public void setScale (final float x, final float y)
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
				movedVec.set(0, 0);
			}
			else
			{
				if (gettingPushed)
				{
					moveInterpVec.scaleTo(moveInterpVec.mag() - friction);
				}
				else
				{
					moveInterpCount++;
					moveInterpVec = Vector2f.normalize(moveVec).scale(moveSpeed / 1000 * Stopwatch.getFrameTime());
				}
				
				if (!gettingPushed && movedVec.mag() > moveVec.mag())
				{
					moveInterpVec.set(0, 0);
					posVec = endPosVec;
					isMoving = false;
					moveInterpCount = 0;
					movedVec.set(0, 0);
				}
				else
				{
					if (moveInterpVec.mag() > 0.1)
					{
						posVec.add(moveInterpVec);
						movedVec.add(moveInterpVec);
					}
					else
						stop();
				}
				
				Game.worldOutdated = true;
			}
		}
	}
	
	public void rotateInterpolate ()
	{
		if (isRotating)
		{			
			final float dist = Math.abs(endAngle - angle);
			
			if (isRotatingCCW)
			{	
				if ((dist > 180 && angle < endAngle) || (dist < 180 && angle > endAngle))
				{
					this.setAngle(endAngle);
					isRotating = false;
				}
				else
					this.setAngle(angle + rotSpeed / 1000 * Stopwatch.getFrameTime());
			}
			else
			{
				if ((dist > 180 && angle > endAngle) || (dist < 180 && angle < endAngle))
				{
					this.setAngle(endAngle);
					isRotating = false;
				}
				else
					this.setAngle(angle - rotSpeed / 1000 * Stopwatch.getFrameTime());
			}
			
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
				sclInterpVec = Vector2f.normalize(sclVec).scale(sclSpeed / 1000 * Stopwatch.getFrameTime());
				
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
		}
	}
	
	public void stop()
	{
		sclInterpVec.set(0.0f, 0.0f);
		moveInterpVec.set(0.0f, 0.0f);
		isMoving = false;
		isRotating = false;
		isScaling = false;
		gettingPushed = false;
		movedVec.set(0, 0);
	}
	
	public void setMoveSpeed(float num)
	{
		moveSpeed = num;
	}
	
	public Vector2f getBounceVec()
	{
	 	if (bounceList.isEmpty())
		{
			return Vector2f.empty;
		}
		else
		{
			Vector2f bounceVec = new Vector2f(bounceList.get(0));
			for (int i = 1; i < bounceList.size(); i++)
			{
				bounceVec.add(bounceList.get(i));
			}
			bounceList.clear();
			movedVec.add(bounceVec);
			return bounceVec;
		}
	}
	
	public void addBounceVec (final Vector2f v)
	{
		bounceList.add(v);
	}
	
	public Vector2f getBounceVec(final int index)
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
	
	public void setMoveInterpVec (final Vector2f v)
	{
		moveInterpVec = v;
	}
	
	public void setIsMoving(final boolean isMoving)
	{
		this.isMoving = isMoving;
	}
	
	public void setIsRotating(final boolean isRotating)
	{
		this.isRotating = isRotating;
	}
	
	public void setIsScaling(final boolean isScaling)
	{
		this.isScaling = isScaling;
	}
	
	public float getMoveSpeed()
	{
		return moveSpeed;
	}
}