package com.lds.physics;

import com.lds.game.event.InteractListener;
import com.lds.math.Matrix4;
import com.lds.math.Vector2;
import com.lds.math.Vector4;

import java.util.ArrayList;

public abstract class Shape
{
    /***********
     * Members *
     ***********/
    protected InteractListener onInteract;
    /**
     * The current velocity of the shape
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
     * The friction constant (mu) between the shape and the floor
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
     * Adds a new impulse to the shape
     * @param v The new impulse to add
     */
    public void addImpulse(Vector2 v)
    {
        impulseVec = Vector2.add(impulseVec, v);
    }
    
    /**
     * Adds a new constant force to the shape
     * @param f The new force to add
     */
    public void addForce(Vector2 f)
    {
        forces.add(f);
    }
    
    /********************
     * Abstract Methods *
     ********************/
    
    protected abstract void updateMass();
    
    /**************************
     * Accessors and Mutators *
     **************************/
    
    public Vector2 getVelocity()
    {
        return velocity;
    }
    
    public void setVelocity(Vector2 v)
    {
        velocity = v;
    }
    
    public void push(Vector2 f)
    {
        velocity = Vector2.add(velocity, Vector2.scale(f, 1.0f / mass));
    }
    
    public Vector2 getImpulse()
    {
        return impulseVec;
    }
    
    public void clearImpulse()
    {
        impulseVec = new Vector2(0, 0);
    }
    
    public ArrayList<Vector2> getForces()
    {
        return forces;
    }
    
    public float getDensity()
    {
        return density;
    }
    
    public void setDensity(float density)
    {
        this.density = density;
        updateMass();
    }
    
    public float getMass()
    {
        return mass;
    }
    
    public float getFriction()
    {
        return friction;
    }
    
    public void setFriction(float friction)
    {
        this.friction = friction;
    }
    
    public void clearForces()
    {
        forces.clear();
    }
    
    public InteractListener getInteractListener()
    {
        return onInteract;
    }
    
    public void setInteractListener(InteractListener i)
    {
        onInteract = i;
    }
    
    public float[] getVertices()
    {
        return vertices;
    }
    
    public Vector2[] getWorldVertices()
    {
        return worldVertices;
    }
    
    public void setVertices(float[] vertices)
    {
        this.vertices = vertices;
        transformVertices();
    }
    
    public Matrix4 getModel()
    {
        return model;
    }
    
    public Vector2 getPos()
    {
        return position;
    }
    
    public void setPos(Vector2 position)
    {
        this.position = position;
        transMat = Matrix4.translate(position);
        rebuildModel();
        transformVertices();
    }
    
    public Vector2 getScale()
    {
        return scale;
    }
    
    public void setScale(Vector2 scale)
    {
        this.scale = scale;
        scaleMat = Matrix4.scale(scale);
        rebuildModel();
        transformVertices();
        updateMass();
    }
    
    public float getAngle()
    {
        return angle;
    }
    
    /**
     * Sets the shape's angle.
     * \todo remove the hax'd in degrees to radians conversion
     * @param angle
     */
    public void setAngle(float angle)
    {
        this.angle = (float)Math.toRadians(angle);
        rotMat = Matrix4.rotateZ(this.angle);
        rebuildModel();
        transformVertices();
    }
    
    public boolean isSolid()
    {
        return solid;
    }
    
    public void setSolid(boolean solid)
    {
        this.solid = solid;
    }
}
