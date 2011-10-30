package com.lds.physics;

import java.util.ArrayList;

import com.lds.math.Vector2;

public final class PhysicsManager 
{
    private PhysicsManager()
    {
    }
    
    public static void updatePairs(ArrayList<CollisionPair> pairs)
    {
        for (CollisionPair pair : pairs)
            runCollisionPhysics(pair);
    }
	
	public static void updateShapes(ArrayList<Shape> shapes)
	{
	    for (Shape s : shapes)
        {
            s.push(s.getImpulse());
            s.clearImpulse();
            
            for (Vector2 f : s.getForces())
                s.push(f);
                    
            s.setPos(Vector2.add(s.getPos(), s.getVelocity()));
            
            s.push(Vector2.scale(s.getVelocity(), -s.getMass() * s.getFriction()));
        }
	}
	
	public static void runCollisionPhysics(CollisionPair cp)
	{
	    //TODO: Better Collision Physics
	    cp.getEnt1().addImpulse(Vector2.scale(cp.getEnt2().getVelocity(), cp.getEnt2().getMass()));
	    cp.getEnt2().addImpulse(Vector2.scale(cp.getEnt1().getVelocity(), cp.getEnt1().getMass()));
	}	
	
	/*
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
