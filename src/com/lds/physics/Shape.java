package com.lds.physics;

import com.lds.math.Matrix4;
import com.lds.math.Vector2;
import com.lds.math.Vector4;

import java.util.ArrayList;

public abstract class Shape
{
    /***********
     * Members *
     ***********/
    
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
     * @param position The position of the shape
     * @param angle The angle of the shape in radians
     * @param scale The scale of the shape
     * @param solid The solidity of the shape
     */
    public Shape(Vector2 position, float angle, Vector2 scale, boolean solid)
    {
        this.solid = solid;
        this.position = position;
        this.scale = scale;
        transMat = Matrix4.translate(position);
        rotMat = Matrix4.rotateZ(angle);
        scaleMat = Matrix4.scale(scale);
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
        
        //return verts.toArray(null);
        //return (Vector2[])verts.toArray();
    }
    
    /**************************
     * Accessors and Mutators *
     **************************/
    
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
    }
    
    public float getAngle()
    {
        return angle;
    }
    
    public void setAngle(float angle)
    {
        this.angle = angle;
        rotMat = Matrix4.rotateZ(angle);
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
