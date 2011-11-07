package com.lds.math;

/**
 * A 3x2 matrix class.
 * This is a 2x2 matrix that also stores translation, used primarily for Vector2 transformation.
 * @author Lightning Development Studios
 */
public final class Matrix3x2
{
    /*************
     * Constants *
     *************/
    
    /**
     * The identity matrix, where each row is it's respective unit vector.
     */
    public static final Matrix3x2 IDENTITY = new Matrix3x2(1, 0, 0,
                                                           0, 1, 0);
    
    /***********
     * Members *
     ***********/
    
    /**
     * The first row of the matrix.
     */
    private final Vector3 col0;
    
    /**
     * The second row of the matrix.
     */
    private final Vector3 col1;
    
    /****************
     * Constructors *
     ****************/
    
    /**
     * Creates a new instance of the Matrix3x2 class. All elements initialized to 0.
     */
    public Matrix3x2()
    {
        this(0, 0, 0,
             0, 0, 0);
    }
    
    /**
     * Creates a new instance of the Matrix3x2 class.
     * @param col0 The first column of the matrix.
     * @param col1 The second column of the matrix.
     */
    public Matrix3x2(Vector3 col0, Vector3 col1)
    {
        this.col0 = col0;
        this.col1 = col1;
    }
    
    /**
     * Creates a new instance of the Matrix3x2 class.
     * @param m11 Column 1, Row 1 of the matrix.
     * @param m12 Column 1, Row 2 of the matrix.
     * @param m13 Column 1, Row 3 of the matrix.
     * @param m21 Column 2, Row 1 of the matrix.
     * @param m22 Column 2, Row 2 of the matrix.
     * @param m23 Column 2, Row 3 of the matrix.
     */
    public Matrix3x2(float m11, float m12, float m13,
                     float m21, float m22, float m23)
    {
        col0 = new Vector3(m11, m12, m13);
        col1 = new Vector3(m21, m22, m23);
    }
    
    /********************
     * Instance methods *
     ********************/
    
    /******************
     * Static Methods *
     ******************/
    
    public static Matrix3x2 translate(Vector2 position)
    {
        return new Matrix3x2(1, 0, position.x(),
                             0, 1, position.y());
    }
    
    public static Matrix3x2 rotate(float radians)
    {
        float sin = (float)Math.sin(radians);
        float cos = (float)Math.cos(radians);
        
        return new Matrix3x2(cos, sin, 0,
                            -sin, cos, 0);
    }
    
    public static Matrix3x2 scale(float scale)
    {
        return new Matrix3x2(scale, 0, 0,
                             0, scale, 0);
    }
    
    /**************************
     * Accessors and Mutators *
     **************************/
    
    /**
     * Gets an array containing all the elements of the matrix.
     * @return A float[16] containing all the elements of the matrix in column-major order.
     */
    public float[] array()
    {       
        float[] array = new float[6];
        float[] c0 = col0.array(), c1 = col1.array();
        
        for(int i = 0; i < 3; i++)
        {
            array[i] = c0[i];
            array[i + 3] = c1[i];
        }
        
        return array;
    }
}
