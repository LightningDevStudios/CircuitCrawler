package com.lds.physics;

import java.util.ArrayList;

public class PhysicsManager 
{
	/***********
	 * Members *
	 ***********/

    /**
     * 
     */
	private CollisionDetector collisionDetector;
	
	/****************
	 * Constructors *
	 ****************/
	
	/**
	 * Initializes a new instance of the PhysicsManager class.
	 * @param collisionDetector A collision detector object.
	 */
	public PhysicsManager(CollisionDetector collisionDetector)
	{
		this.collisionDetector = collisionDetector;
	}

	/**
	 * Get shapes from a Collision Detector.
	 * <ol>
	 *     <li>Run QuadTreeDetection();</li>
	 *     <li>Use the QuadTreeList from QuadTreeDetection() and pass the shapes to RadiusCheck.</li>
	 *     <li>If the RadiusCheck returns true pass them to the SeperatingAxisTheorem.</li>
	 *     <li>Solve the Collisions from the CollisionPair.</li>
	 * </ol>
	 * 
	 * Note the QuadTreeDetection() returns 2 list of lists:
	 * <ol>
	 *     <li>List1 called normalShape contains all shapes that could be colliding with each other so if
	 *     there is 1 list in normalShape and it contains 3 shapes; shape 1 compares to shape 2 and 3,
	 *     and shape 2 compares to 3</li>
	 *     <li>List 2 called onLineShape contains all shapes that do not fall in a specific quadrant so
	 *     if onLineShape contains 1 list which has 6 shapes in it; shape 1 is compared to
	 *     shape 2, 3, 4, 5, and 6.</li>
	 * </ol>
	 * @return 
	 */
	public ArrayList<CollisionPair> SolveCollision()
	{
		ArrayList<CollisionPair> pairList= new ArrayList<CollisionPair>();
		
		ArrayList<ArrayList<Shape>> quadList = collisionDetector.QuadTreeDetection();
		
		for(ArrayList<Shape> shapes : quadList)
		{
			for(Shape shape : shapes)
			{
				for(Shape s : shapes)
				{
					if(shape != s && CollisionDetector.RadiusCheck(shape, s))
					{
						CollisionPair pair = CollisionDetector.CheckCollision(shape, s);
						if(pair != null)
						{
							pairList.add(pair);
						}
					}
				}
			}
		}
		return pairList;
	}
	
	/* \todo Implement this physics stuff
	@Override
    public void circleBounceAgainstCircle(Shape shape)
    {
        //checks if this shape is moving; if it is not, do nothing
        if (this.moveInterpVec.getX() == 0.0f && this.moveInterpVec.getY() == 0.0f)
            return;
        
        Vector2 bounceNormal = Vector2.subtract(this.posVec, shape.posVec).normalize();
        Vector2 bounceSide = Vector2.getNormal(bounceNormal);
        
        Vector2 thisProjPoint = Vector2.subtract(posVec, bounceNormal.scale(halfSize));
        float thisMin = bounceNormal.normalize().dot(thisProjPoint);
        Vector2 shapeProjPoint = Vector2.add(shape.posVec, bounceNormal.scale(shape.halfSize));
        float shapeMax = bounceNormal.normalize().dot(shapeProjPoint);
        addBounceVec(bounceNormal.scale(shapeMax - thisMin));
        
        if (gettingPushed)
            this.moveInterpVec.copy(bounceSide.scale(bounceSide.dot(this.moveInterpVec) * 2).subtract(moveInterpVec));
    }
    
    @Override
    public void circleBounceAgainstRectangle(Shape shape)
    {
        shape.updateAbsolutePointLocations();
         
        //checks if this shape is moving; if it is not, do nothing
        if (this.moveInterpVec.getX() == 0.0f && this.moveInterpVec.getY() == 0.0f)
            return;
        
        Vector2[] vertDistVecs = new Vector2[4];

        for (int i = 0; i < 4; i++)
            vertDistVecs[i] = Vector2.subtract(posVec, moveInterpVec).subtract(shape.vertVecs[i]);
    
        //goes through the vectors and sorts them from low to high (thanks Mr. Carlson)
        int maxPos;
        Vector2 temp = new Vector2();
        for (int k = vertDistVecs.length; k >= 2; k--)
        {
            maxPos = 0; 
            for (int i = 1; i < k; i++)
            {
                 if (vertDistVecs[i].length() > vertDistVecs[maxPos].length()) 
                      maxPos = i; 
            }
            temp.copy(vertDistVecs[maxPos]); 
            vertDistVecs[maxPos].copy(vertDistVecs[k - 1]); 
            vertDistVecs[k - 1].copy(temp);
        }
        
        Vector2 bounceSide = Vector2.subtract(vertDistVecs[0], vertDistVecs[1]).normalize();
        Vector2 bounceNormal = Vector2.getNormal(bounceSide);
        Vector2 mpDist = Vector2.getMidpoint(vertDistVecs[0].add(this.posVec), vertDistVecs[1].add(this.posVec)).subtract(shape.posVec);
        if (mpDist.length() > mpDist.add(bounceNormal).length())
            bounceNormal.negate();
        
        //get max of projection of shape
        float shapeMax = bounceNormal.dot(shape.vertVecs[0]);
        for (int i = 1; i < shape.vertVecs.length; i++)
        {
            final float dotProd2 = bounceNormal.dot(shape.vertVecs[i]);
             if (dotProd2 > shapeMax)
                shapeMax = dotProd2;
        }
        
        Vector2 projPoint = Vector2.subtract(posVec, bounceNormal.scale(halfSize));
        float thisMin = bounceNormal.normalize().dot(projPoint);
        addBounceVec(bounceNormal.scale(shapeMax - thisMin));
        
        if (gettingPushed)
            this.moveInterpVec.copy(bounceSide.scale(bounceSide.dot(this.moveInterpVec) * 2).subtract(moveInterpVec));
    }
    
    @Override
    public void rectangleBounceAgainstCircle(Shape shape)
    {
         this.updateAbsolutePointLocations();
         
        //checks if this shape is moving; if it is not, do nothing
        if (this.moveInterpVec.getX() == 0.0f && this.moveInterpVec.getY() == 0.0f)
            return;
        
        Vector2[] vertDistVecs = new Vector2[4];

        for (int i = 0; i < 4; i++)
            vertDistVecs[i] = Vector2.subtract(this.vertVecs[i], this.moveInterpVec).subtract(shape.posVec);
    
        //goes through the vectors and sorts them from low to high (thanks Mr. Carlson)
        int maxPos;
        Vector2 temp = new Vector2();
        for (int k = vertDistVecs.length; k >= 2; k--)
        {
            maxPos = 0; 
            for (int i = 1; i < k; i++)
            {
                 if (vertDistVecs[i].length() > vertDistVecs[maxPos].length()) 
                      maxPos = i; 
            }
            temp.copy(vertDistVecs[maxPos]); 
            vertDistVecs[maxPos].copy(vertDistVecs[k - 1]); 
            vertDistVecs[k - 1].copy(temp);
        }
        
        Vector2 bounceSide = Vector2.subtract(vertDistVecs[0], vertDistVecs[1]).normalize();
        Vector2 bounceNormal = Vector2.getNormal(bounceSide);
        Vector2 mpDist = Vector2.getMidpoint(vertDistVecs[0].add(this.posVec), vertDistVecs[1].add(this.posVec)).subtract(shape.posVec);
        if (mpDist.length() > mpDist.add(bounceNormal).length())
            bounceNormal.negate();
        
        //get max of projection of shape
        float thisMin = bounceNormal.dot(this.vertVecs[0]);
        for (int i = 1; i < this.vertVecs.length; i++)
        {
            final float dotProd1 = bounceNormal.dot(this.vertVecs[i]);
            if (dotProd1 < thisMin)
                thisMin = dotProd1;
        }
        
        Vector2 projPoint = Vector2.add(shape.posVec, bounceNormal.scale(shape.halfSize));
        float shapeMax = bounceNormal.normalize().dot(projPoint);
        addBounceVec(bounceNormal.scale(shapeMax - thisMin));
        
        if (gettingPushed)
            this.moveInterpVec.copy(bounceSide.scale(bounceSide.dot(this.moveInterpVec) * 2).subtract(moveInterpVec));
    }

    @Override
    public void rectangleBounceAgainstRectangle(Shape shape) 
    {
        this.updateAbsolutePointLocations();
        shape.updateAbsolutePointLocations();
        
        //checks if this shape is moving; if it is not, do nothing
        if (this.moveInterpVec.getX() == 0.0f && this.moveInterpVec.getY() == 0.0f)
            return;
        
        Vector2[] vertDistVecs = new Vector2[4];

        for (int i = 0; i < 4; i++)
            vertDistVecs[i] = Vector2.subtract(posVec, moveInterpVec).subtract(shape.vertVecs[i]);
    
        //goes through the vectors and sorts them from low to high (thanks Mr. Carlson)
        int maxPos;
        Vector2 temp = new Vector2();
        for (int k = vertDistVecs.length; k >= 2; k--)
        {
            maxPos = 0; 
            for (int i = 1; i < k; i++) 
            {
                 if (vertDistVecs[i].length() > vertDistVecs[maxPos].length()) 
                      maxPos = i; 
            }
            temp.copy(vertDistVecs[maxPos]); 
            vertDistVecs[maxPos].copy(vertDistVecs[k - 1]); 
            vertDistVecs[k - 1].copy(temp);
        }
        
        Vector2 bounceSide = Vector2.subtract(vertDistVecs[0], vertDistVecs[1]).normalize();
        Vector2 bounceNormal = Vector2.getNormal(bounceSide);
        Vector2 mpDist = Vector2.getMidpoint(vertDistVecs[0].add(this.posVec), vertDistVecs[1].add(this.posVec)).subtract(shape.posVec);
        if (mpDist.length() > mpDist.add(bounceNormal).length())
            bounceNormal.negate();
        
        //get min of projection of this shape
        float thisMin = bounceNormal.dot(this.vertVecs[0]);
        for (int i = 1; i < this.vertVecs.length; i++)
        {
            final float dotProd1 = bounceNormal.dot(this.vertVecs[i]);
            if (dotProd1 < thisMin)
                thisMin = dotProd1;
        }
        
        //get max of projection of shape
        float shapeMax = bounceNormal.dot(shape.vertVecs[0]);
        for (int i = 1; i < shape.vertVecs.length; i++)
        {
            final float dotProd2 = bounceNormal.dot(shape.vertVecs[i]);
            if (dotProd2 > shapeMax)
                shapeMax = dotProd2;
        }
        
        //scale the bounceNormal the the proper magnitude to get the shape out of collision
        addBounceVec(bounceNormal.scale(shapeMax - thisMin));
        
        if (gettingPushed)
            this.moveInterpVec.copy(bounceSide.scale(bounceSide.dot(this.moveInterpVec) * 2).subtract(moveInterpVec));
    }
	 */
}
