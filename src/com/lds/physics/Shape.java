package com.lds.physics;

import java.util.ArrayList;
import java.util.IllegalFormatException;

import com.lds.math.Matrix4;
import com.lds.math.Vector2;
import com.lds.math.Vector4;

public abstract class Shape
{
    /***********
     * Members *
     ***********/
    
    /**
     * The shape's vertices in local coordinates
     */
    protected float[] vertices;
    
    /**
     * The shape's vertices in world coordinates
     */
    protected Vector2[] worldVertices;
    
    /**
     * The shape's scale matrix
     */
    private Matrix4 scaleMat;
    
    /**
     * The shape's rotation matrix
     */
    private Matrix4 rotMat;
    
    /**
     * The shape's translation matrix
     */
    private Matrix4 transMat;
    
    /**
     * The shape's model matrix
     */
    protected Matrix4 model;
    
    /**
     * The shape's position in world coords
     */
    protected Vector2 position;
    
    /**
     * The shape's angle in radians (counterclockwise is positive)
     */
    protected float angle;
    
    /**
     * The shape's x and y scale
     */
    protected Vector2 scale;  
    
    /****************
     * Constructors *
     ****************/
    
    /**
     * Initializes a new instance of the Shape class
     */
    public Shape()
    {
        this(new Vector2(0, 0));
    }
    
    /**
     * Initializes a new instance of the Shape class
     * @param position The position of the shape
     */
    public Shape(Vector2 position)
    {
        this(position, 0);
    }
    
    /**
     * Initializes a new instance of the Shape class
     * @param position The position of the shape
     * @param angle The angle of the shape in radians
     */
    public Shape(Vector2 position, float angle)
    {
        this(position, angle, new Vector2(1, 1));
    }
    
    /**
     * Initializes a new instance of the Shape class
     * @param position The position of the shape
     * @param angle The angle of the shape in radians
     * @param scale The scale of the shape
     */
    public Shape(Vector2 position, float angle, Vector2 scale)
    {
        transMat = Matrix4.translate(position);
        rotMat = Matrix4.rotateZ(angle);
        scaleMat = Matrix4.scale(scale);
        rebuildModel();
        transformVertices();
    }
    
    /******************
     * Public Methods *
     ******************/
    
    /**
     * Rebuilds the model matrix
     */
    public void rebuildModel()
    {
        model = Matrix4.multiply(Matrix4.multiply(scaleMat, rotMat), transMat);
    }
    
    /**
     * Transforms vertices to world coordinates
     */
    public void transformVertices()
    {
        Vector2[] vectorVertices = getVectorVertices();
        for (Vector2 v : vectorVertices)
            v = Vector4.transform(new Vector4(v, 0, 1), model).xy();
        worldVertices = vectorVertices;
    }
    
    /**
     * Converts vertices into a Vector2[]
     * @return an array of Vector2s, each one representing a vertex
     */
    public Vector2[] getVectorVertices()
    {
        ArrayList<Vector2> verts = new ArrayList<Vector2>();
        for (int i = 0; i < vertices.length - 1; i += 2)
            verts.add(new Vector2(vertices[i], vertices[i+1]));

        return (Vector2[])verts.toArray();
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
    
    public Matrix4 getModel()
    {
        return model;
    }
    
    public Vector2 getPos()
    {
        return position;
    }
    
    public void setPosition(Vector2 position)
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
}
