package com.lds.physics;

import com.lds.game.event.InteractListener;
import com.lds.math.Matrix4;
import com.lds.math.Vector2;
import com.lds.math.Vector4;

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
     * The current velocity of the shape.
     */
    protected Vector2 velocity;
    
    /**
     * The shape's current impulse vector, sum of implulse forces acted on it.
     */
    protected Vector2 impulseVec;
    
    /**
     * A list of the current continual forces acting on the shape.
     */
    protected ArrayList<Vector2> forces;
    
    /**
     * The density of the shape. default is 1
     */
    protected float density;
    
    /**
     * The friction constant (mu) between the shape and the floor.
     */
    protected float friction;
    
    /**
     * The shape's mass. Calculated by multiplying area by density
     */
    protected float mass;
    
    /**
     * The shape's vertices in local coordinates.
     */
    protected float[] vertices;
    
    /**
     * The shape's vertices in world coordinates.
     */
    protected Vector2[] worldVertices;
    
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
     * The shape's x and y scale.
     */
    protected Vector2 scale; 
    
    /**
     * The shape's scale matrix.
     */
    private Matrix4 scaleMat;
    
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
     * @param position The position of the shape
     * @param angle The angle of the shape in radians
     * @param solid The solidity of the shape
     */
    public Shape(Vector2 position, float angle, boolean solid)
    {
        this(position, angle, new Vector2(1, 1), solid);
    }
    
    /**
     * Initializes a new instance of the Shape class.
     * \todo Fix angle, remove the hax'd in degree to radians check.
     * @param position The position of the shape
     * @param angle The angle of the shape in radians
     * @param scale The scale of the shape
     * @param solid The solidity of the shape
     */
    public Shape(Vector2 position, float angle, Vector2 scale, boolean solid)
    {
        density = 0.001f;
        friction = 0.1f;
        velocity = new Vector2(0, 0);
        impulseVec = new Vector2(0, 0);
        forces = new ArrayList<Vector2>();
        this.solid = solid;
        this.position = position;
        this.scale = scale;
        this.angle = (float)Math.toRadians(angle);
        transMat = Matrix4.translate(this.position);
        rotMat = Matrix4.rotateZ(this.angle);
        scaleMat = Matrix4.scale(this.scale);
        rebuildModel();
    }
    
    /******************
     * Public Methods *
     ******************/
    
    /**
     * Rebuilds the model matrix.
     */
    public void rebuildModel()
    {
        model = Matrix4.multiply(Matrix4.multiply(scaleMat, rotMat), transMat);
    }
    
    /**
     * Transforms vertices to world coordinates.
     */
    public void transformVertices()
    {
        Vector2[] vectorVertices = getVectorVertices();
        for (int i = 0; i < vectorVertices.length; i++)
            vectorVertices[i] = Vector4.transform(new Vector4(vectorVertices[i], 0, 1), model).xy();
        worldVertices = vectorVertices;
    }
    
    /**
     * Converts vertices into a Vector2[].
     * @return an array of Vector2s, each one representing a vertex
     */
    public Vector2[] getVectorVertices()
    {
        Vector2[] verts = new Vector2[vertices.length / 2];
        for (int i = 0; i < verts.length; i++)
            verts[i] = new Vector2(vertices[i * 2], vertices[i * 2 + 1]);

        return verts;
    }
    
    /**
     * Adds a new impulse to the shape.
     * @param v The new impulse to add.
     */
    public void addImpulse(Vector2 v)
    {
        impulseVec = Vector2.add(impulseVec, v);
    }
    
    /**
     * Adds a new constant force to the shape.
     * @param f The new force to add.
     */
    public void addForce(Vector2 f)
    {
        forces.add(f);
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
        impulseVec = new Vector2(0, 0);
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
        return impulseVec;
    }
    
    /**
     * Gets an ArrayList<Vector2> of all the forces acting on the Shape.
     * @return All the forces acting on the shape.
     */
    public ArrayList<Vector2> getForces()
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
     * Gets the coefficient of friction for this Shape.
     * \todo static or kinetic friction>
     * @return The coefficient of friction.
     */
    public float getFriction()
    {
        return friction;
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
     * Gets the scale of the current Shape.
     * @return The Shape's scale.
     */
    public Vector2 getScale()
    {
        return scale;
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
     * Sets the Shape's coefficient of friction.
     * @param friction The new coefficient of friction.
     */
    public void setFriction(float friction)
    {
        this.friction = friction;
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
     * Sets the scale of the Shape.
     * @param scale The Shape's new scale.
     */
    public void setScale(Vector2 scale)
    {
        this.scale = scale;
        scaleMat = Matrix4.scale(scale);
        rebuildModel();
        transformVertices();
        updateMass();
    }
    
    /**
     * Sets the shape's angle.
     * \todo remove the hax'd in degrees to radians conversion
     * @param angle The Shape's new angle.
     */
    public void setAngle(float angle)
    {
        this.angle = (float)Math.toRadians(angle);
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
