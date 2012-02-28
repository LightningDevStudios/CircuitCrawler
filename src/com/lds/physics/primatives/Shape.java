package com.lds.physics.primatives;

import com.lds.game.event.InteractListener;
import com.lds.math.Matrix4;
import com.lds.math.Vector2;
import com.lds.math.Vector4;
import com.lds.physics.forcegenerators.IndivForce;

import java.util.ArrayList;

/**
 * An abstract class that handles physics independently from other Entity code.
 * @author Lightning Development Studios
 */
public abstract class Shape
{
    /***********
     * Members *
     ***********/
    
    /**
     * Provides a callback for when this Shape collides with another one.
     */
    protected InteractListener onInteract;
    
    /**
     * A list of the current continual forces acting on the shape.
     */
    protected ArrayList<IndivForce> forces;
    
    /**
     * The shape's current impulse vector, sum of implulse forces acted on it.
     */
    protected Vector2 totalImpulse;
    
    /**
     * The shape's vertices in world coordinates.
     */
    protected Vector2[] worldVertices;
    
    /**
     * The current velocity of the shape.
     */
    protected Vector2 velocity;
    
    /**
     * The shape's mass. Calculated by multiplying area by density
     */
    protected float mass;
    
    /**
     * Determines whether the shape can move or not
     */
    public boolean isStatic;
    
    /**
     * The density of the shape. default is 1
     */
    protected float density;
    
    /**
     * The coefficient of static friction between the shape and the floor.
     */
    protected float staticFriction;
    
    /**
     * The coefficient of kinetic friction between the shape and the floor.
     */
    protected float kineticFriction;
    
    /**
     * The shape's vertices in local coordinates.
     */
    protected float[] vertices;
    
    /**
     * The shape's model matrix.
     */
    protected Matrix4 model;
    
    /**
     * The shape's position in world coords.
     */
    protected Vector2 position;
    
    /**
     * The shape's angle in radians (counterclockwise is positive).
     */
    protected float angle;
    
    /**
     * The shape's rotation matrix.
     */
    private Matrix4 rotMat;
    
    /**
     * The shape's translation matrix.
     */
    private Matrix4 transMat;
    
    /**
     * Whether or not the shape has physics.
     */
    private boolean solid;
    
    /****************
     * Constructors *
     ****************/
    
    /**
     * Initializes a new, solid instance of the Shape class.
     */
    public Shape()
    {
        this(new Vector2(0, 0), true);
    }
    
    /**
     * Initializes a new instance of the Shape class.
     * @param solid The solidity of the shape
     */
    public Shape(boolean solid)
    {
        this(new Vector2(0, 0), solid);
    }
    
    /**
     * Initializes a new instance of the Shape class.
     * @param position The position of the shape
     * @param solid The solidity of the shape
     */
    public Shape(Vector2 position, boolean solid)
    {
        this(position, 0, solid);
    }
    
    /**
     * Initializes a new instance of the Shape class.
     * \todo Fix angle, remove the hax'd in degree to radians check.
     * @param position The position of the shape
     * @param angle The angle of the shape in radians
     * @param solid The solidity of the shape
     */
    public Shape(Vector2 position, float angle, boolean solid)
    {
        density = 1;
        staticFriction = 2.3f;
        kineticFriction = 4f;
        velocity = new Vector2(0, 0);
        totalImpulse = new Vector2(0, 0);
        forces = new ArrayList<IndivForce>();
        this.solid = solid;
        this.position = position;
        this.angle = (float)Math.toRadians(angle);
        transMat = Matrix4.translate(this.position);
        rotMat = Matrix4.rotateZ(this.angle);
        rebuildModel();
    }
    
    /******************
     * Public Methods *
     ******************/
    
    public void integrate(float frameTime)
    {
        //no friction for static objects
        if (isStatic)
            return;

        //apply forces
        for (IndivForce f : forces)
            f.UpdateForce(frameTime, this);
                
        //apply friction
        //float impulseLength = totalImpulse.length();
        //if (impulseLength < staticFriction * mass)
           // totalImpulse = Vector2.ZERO;
        //else
            //totalImpulse = Vector2.subtract(totalImpulse, Vector2.scale(totalImpulse, -frameTime * kineticFriction * mass / impulseLength));
        
        //add impulse
        if (totalImpulse.length() > 0)
        {
            velocity = Vector2.add(velocity, Vector2.scale(totalImpulse, 1 / mass));
        }
        
        setPos(Vector2.add(position, Vector2.scale(velocity, frameTime)));

        totalImpulse = Vector2.ZERO;
    }
    
    /**
     * Rebuilds the model matrix.
     */
    public void rebuildModel()
    {
        model = Matrix4.multiply(rotMat, transMat);
    }
    
    /**
     * Transforms vertices to world coordinates.
     */
    public void transformVertices()
    {
        if (vertices == null || model == null)
            return;

        worldVertices = new Vector2[vertices.length / 2];
        for (int i = 0; i < worldVertices.length; i++)
        {
            worldVertices[i] = Vector4.transform(new Vector4(vertices[i * 2], vertices[i * 2 + 1], 0, 1), model).xy();
        }
    }
    
    /**
     * Adds a new impulse to the shape.
     * @param v The new impulse to add.
     */
    public void addImpulse(Vector2 v)
    {
        totalImpulse = Vector2.add(totalImpulse, v);
    }
    
    /**
     * Adds a new constant force to the shape.
     * @param f The new force to add.
     */
    public void addForce(IndivForce f)
    {
        forces.add(f);
    }
    
    public void removeForce(IndivForce f)
    {
        forces.remove(f);
    }
    
    /********************
     * Abstract Methods *
     ********************/
    
    /**
     * Abstract method used to estimate mass.
     */
    protected abstract void updateMass();
    
    /**************************
     * Accessors and Mutators *
     **************************/
    
    /**
     * Resets the impulse vector to <0, 0>.
     */
    public void clearImpulse()
    {
        totalImpulse = new Vector2(0, 0);
    }
    
    /**
     * Clears the ArrayList of forces acting on the Shape.
     */
    public void clearForces()
    {
        forces.clear();
    }
    
    /**
     * Push the Shape by a specified Vector2.
     * \todo not sure how this is different from impulse...
     * @param f The Vector2 to push by.
     */
    public void push(Vector2 f)
    {
        velocity = Vector2.add(velocity, Vector2.scale(f, 1.0f / mass));
    }
    
    /**
     * Gets the Shape's current velocity vector.
     * @return The Shape's current velocity.
     */
    public Vector2 getVelocity()
    {
        return velocity;
    }
    
    /**
     * Gets a Vector2 representing all the impulses to be applied to the Shape on the next update.
     * @return The resultant Vector2 of adding all the impulses acting on the Shape.
     */
    public Vector2 getImpulse()
    {
        return totalImpulse;
    }
    
    /**
     * Gets an ArrayList<IndivForce> of all the forces acting on the Shape.
     * @return All the forces acting on the shape.
     */
    public ArrayList<IndivForce> getForces()
    {
        return forces;
    }
    
    /**
     * Gets the density of the Shape.
     * @return The Shape's density.
     */
    public float getDensity()
    {
        return density;
    }
    
    /**
     * Gets the mass of the Shape.
     * \todo what units?
     * @return The mass of the Shape.
     */
    public float getMass()
    {
        return mass;
    }
    
    /**
     * Gets the coefficient of static friction for this Shape.
     * @return The coefficient of friction.
     */
    public float getStaticFriction()
    {
        return staticFriction;
    }
    
    /**
     * Gets the coefficient of kinetic friction for this Shape.
     * @return The coefficient of friction.
     */
    public float getKineticFriction()
    {
        return kineticFriction;
    }
    
    /**
     * Gets the collision callback for this Shape.
     * @return The event listener for this Shape.
     */
    public InteractListener getInteractListener()
    {
        return onInteract;
    }
    
    /**
     * Gets the Shape's vertices as a float array.
     * @return A float[] containing all the Shape's vertices.
     */
    public float[] getVertices()
    {
        return vertices;
    }
    
    /**
     * Gets the Shape's vertices as a Vector2 array that has been transformed to world coordinates.
     * @return A Vector2[] of world coordinate vertices.
     */
    public Vector2[] getWorldVertices()
    {
        return worldVertices;
    }
    
    /**
     * Gets the model matrix containing scaling and rotation data for this Shape.
     * @return The Shape's model matrix.
     */
    public Matrix4 getModel()
    {
        return model;
    }
    
    /**
     * Gets the Shape's current position in world coordinates.
     * @return The Shape's position.
     */
    public Vector2 getPos()
    {
        return position;
    }
    
    /**
     * Gets the angle of the current Shape in radians.
     * @return The Shape's angle in radians.
     */
    public float getAngle()
    {
        return angle;
    }
    
    /**
     * Gets a value indincating whether the Shape is solid or not.
     * @return A boolean determining solidity.
     */
    public boolean isSolid()
    {
        return solid;
    }
    
    /**
     * Overwrites the velocity vector.
     * @param v The new velocity vector.
     */
    public void setVelocity(Vector2 v)
    {
        velocity = v;
    }
    
    /**
     * Sets the Shape's density.
     * @param density The density of the Shape.
     */
    public void setDensity(float density)
    {
        this.density = density;
        updateMass();
    }
    
    /**
     * Sets the Shape's coefficient of static friction.
     * @param friction The new coefficient of static friction.
     */
    public void setStaticFriction(float friction)
    {
       staticFriction = friction;
    }
    
    /**
     * Sets the Shape's coefficient of kinetic friction.
     * @param friction The new coefficient of kinetic friction.
     */
    public void setKineticFriction(float friction)
    {
        kineticFriction = friction;
    }
    
    /**
     * Sets the methods to be called when this Shape collides with another one.
     * @param i The new event listener.
     */
    public void setInteractListener(InteractListener i)
    {
        onInteract = i;
    }
    
    /**
     * Sets the Shape's vertices.
     * @param vertices The Shape's new vectices.
     */
    public void setVertices(float[] vertices)
    {
        this.vertices = vertices;
        transformVertices();
    }
    
    /**
     * Sets the position of the Shape.
     * @param position The Shape's new position.
     */
    public void setPos(Vector2 position)
    {
        this.position = position;
        transMat = Matrix4.translate(position);
        rebuildModel();
        transformVertices();
    }
    
    /**
     * Sets the shape's angle.
     * \todo remove the hax'd in degrees to radians conversion
     * @param angle The Shape's new angle.
     */
    public void setAngle(float angle)
    {
        if (angle == Float.NaN)
            return;
        
        this.angle = angle;
        
        rotMat = Matrix4.rotateZ(this.angle);
        rebuildModel();
        transformVertices();
    }
    
    /**
     * Sets a value indicating whether the Shape is solid or not.
     * @param solid The solidity of this Entity.
     */
    public void setSolid(boolean solid)
    {
        this.solid = solid;
    }
}
