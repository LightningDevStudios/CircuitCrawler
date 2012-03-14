package com.ltdev.math;

/**
 * A 2x2 matrix class.
 * @author Lightning Development Studios
 */
public final class Matrix2 
{
    /*************
     * Constants *
     *************/
    
    /**
     * The identity matrix, where each row is it's respective unit vector.
     */
    public static final Matrix2 IDENTITY = new Matrix2(1, 0,
                                                       0, 1);
    
    /***********
     * Members *
     ***********/
    
    /**
     * The first row of the matrix.
     */
    private final Vector2 col0;
    
    /**
     * The second row of the matrix.
     */
    private final Vector2 col1;
    
    /****************
     * Constructors *
     ****************/
    
    /**
     * Initializes a new instance of the Matrix2 class. All elements initialized to 0.
     */
    public Matrix2()
    {
        this(0, 0,
             0, 0);
    }
    
    /**
     * Initializes a new instance of the Matrix2 class.
     * @param row0 The first column of the matrix.
     * @param row1 The second column of the matrix.
     */
    public Matrix2(Vector2 row0, Vector2 row1)
    {
        this.col0 = row0;
        this.col1 = row1;
    }
    
    /**
     * Initializes a new instance of the Matrix2 class.
     * @param m11 Column 1, Row 1 of the matrix.
     * @param m12 Column 1, Row 2 of the matrix.
     * @param m21 Column 2, Row 1 of the matrix.
     * @param m22 Column 2, Row 2 of the matrix.
     */
    public Matrix2(float m11, float m12,
                   float m21, float m22)
    {
        col0 = new Vector2(m11, m12);
        col1 = new Vector2(m21, m22);
    }
    
    /********************
     * Instance methods *
     ********************/
    
    /**
     * Gets the determinant of this matrix.
     * @return The determinant of the matrix.
     */
    public float determinant()
    {
        float[] array = array();
        final float m11 = array[0], m12 = array[1],
                    m21 = array[2], m22 = array[3];
        
        return m11 * m22 - m12 * m21;
    }
    
    /**
     * Formats the vector for text output.
     * @return A formatted string.
     */
    @Override
    public String toString()
    {
        return "| " + col0.x() + ", \t" + col1.x() + " \t|\n"
             + "| " + col0.y() + ", \t" + col1.y() + " \t|\n";
    }
    
    /******************
     * Static Methods *
     ******************/
    
    /**
     * Multiplies two matrices together.
     * @param left The left operand matrix.
     * @param right The right operand matrix.
     * @return A new matrix that is the result of the multiplication.
     */
    public static Matrix2 multiply(Matrix2 left, Matrix2 right)
    {
        float[] lArray = left.array();
        float[] rArray = right.array();
        
        float lM11 = lArray[0], lM12 = lArray[1],
              lM21 = lArray[2], lM22 = lArray[3];
        
        float rM11 = rArray[0], rM12 = rArray[1],
              rM21 = rArray[2], rM22 = rArray[3];
        
        float m11 = (lM11 * rM11) + (lM12 * rM21);
        float m12 = (lM11 * rM12) + (lM12 * rM22);
        float m21 = (lM21 * rM11) + (lM22 * rM21);
        float m22 = (lM21 * rM12) + (lM22 * rM22);
        
        return new Matrix2(m11, m12,
                           m21, m22);
    }
    
    /**
     * Turns a column-major matrix into a row-major matrix and vice versa. 
     * @param mat The matrix to transpose.
     * @return A transposed matrix.
     */
    public static Matrix2 transpose(Matrix2 mat)
    {
        return new Matrix2(mat.col0(), mat.col1());
    }
    
    /**
     * Creates a uniform scale matrix.
     * @param x Scaling in the X direction.
     * @param y Scaling in the Y direction.
     * @return A scale matrix.
     */
    public static Matrix2 scale(float x, float y)
    {
        return new Matrix2(x, 0,
                           0, y);
    }
    
    /**
     * Creates a uniform scale matrix.
     * @param scale Scaling in the X and Y directions.
     * @return A scale matrix.
     */
    public static Matrix2 scale(Vector2 scale)
    {
        return Matrix2.scale(scale.x(), scale.y());
    }
    
    /**
     * Creates a 2D rotation matrix.
     * @param radians The angle to turn to. Measured in radians.
     * @return A rotation matrix.
     */
    public static Matrix2 rotate(float radians)
    {       
        float sin = (float)Math.sin(radians);
        float cos = (float)Math.cos(radians);
        
        return new Matrix2(cos, sin,
                          -sin, cos);
    }
    
    /**************************
     * Accessors and Mutators *
     **************************/
    
    /**
     * Gets the matrix element at row 1, column 1.
     * @return The X component of the first column.
     */
    public float m11() 
    {
        return col0.x();
    }
    
    /**
     * Gets the matrix element at row 1, column 2.
     * @return The Y component of the first column.
     */
    public float m12() 
    {
        return col0.y();
    }
    
    /**
     * Gets the matrix element at row 2, column 1.
     * @return The X component of the second column.
     */
    public float m21() 
    {
        return col1.x();
    }
    
    /**
     * Gets the matrix element at row 2, column 2.
     * @return The X component of the second column.
     */
    public float m22() 
    {
        return col1.y();
    }
    
    /**
     * Gets the first row of the matrix.
     * @return A Vector2 containing the X components of all the columns.
     */
    public Vector2 row0() 
    {
        return col0;
    }
    
    /**
     * Gets the second row of the matrix.
     * @return A Vector2 containing the X components of all the columns.
     */
    public Vector2 row1() 
    {
        return col1;
    }
    
    /**
     * Gets the first column of the matrix.
     * @return A Vector2 containing all the components of the first column.
     */
    public Vector2 col0() 
    {
        return new Vector2(col0.x(), col1.x());
    }
    
    /**
     * Gets the second column of the matrix.
     * @return A Vector2 containing all the components of the second column.
     */
    public Vector2 col1() 
    {
        return new Vector2(col0.y(), col1.y());
    }
    
    /**
     * Gets an array containing all the elements of the matrix.
     * @return A float[4] containing all the elements of the matrix in column-major order.
     */
    public float[] array()
    {
        float[] array = new float[4];
        
        float[] r0 = col0.array(), r1 = col1.array();
        
        for (int i = 0; i < 2; i++)
        {
            array[i] = r0[i];
            array[i + 2] = r1[i];
        }
        
        return array;
    }
}
