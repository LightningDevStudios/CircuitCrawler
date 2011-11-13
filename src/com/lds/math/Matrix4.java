package com.lds.math;

/**
 * A 4x4 matrix class.
 * @author Lightning Development Studios
 * \todo Gauss-Jordan elimination for an inverse method?
 */
public final class Matrix4 
{
    /*************
     * Constants *
     *************/
    
    /**
     * The identity matrix, where each row is it's respective unit vector.
     */
    public static final Matrix4 IDENTITY = new Matrix4(1, 0, 0, 0,
                                                       0, 1, 0, 0,
                                                       0, 0, 1, 0,
                                                       0, 0, 0, 1);
    
    /***********
     * Members *
     ***********/
    
    /**
     * The first row of the matrix.
     */
    private final Vector4 col0;
    
    /**
     * The second row of the matrix.
     */
    private final Vector4 col1;
    
    /**
     * The third row of the matrix.
     */
    private final Vector4 col2;
    
    /**
     * The fourth row of the matrix.
     */
    private final Vector4 col3;
    
	/****************
	 * Constructors *
	 ****************/
	
	/**
	 * Creates a new instance of the Matrix4 class. All elements initialized to 0.
	 */
	public Matrix4()
	{
		this(0, 0, 0, 0, 
		     0, 0, 0, 0,
		     0, 0, 0, 0,
		     0, 0, 0, 0);
	}
	
	/**
	 * Creates a new instance of the Matrix4 class.
	 * @param col0 The first column of the matrix.
	 * @param col1 The second column of the matrix.
	 * @param col2 The third column of the matrix.
	 * @param col3 The fourth column of the matrix.
	 */
	public Matrix4(Vector4 col0, Vector4 col1, Vector4 col2, Vector4 col3)
	{
		this.col0 = col0;
		this.col1 = col1;
		this.col2 = col2;
		this.col3 = col3;
	}
		
	/**
	 * Creates a new instance of the Matrix4 class.
	 * @param m11 Column 1, Row 1 of the matrix.
	 * @param m12 Column 1, Row 2 of the matrix.
	 * @param m13 Column 1, Row 3 of the matrix.
	 * @param m14 Column 1, Row 4 of the matrix.
	 * @param m21 Column 2, Row 1 of the matrix.
	 * @param m22 Column 2, Row 2 of the matrix.
	 * @param m23 Column 2, Row 3 of the matrix.
	 * @param m24 Column 2, Row 4 of the matrix.
	 * @param m31 Column 3, Row 1 of the matrix.
	 * @param m32 Column 3, Row 2 of the matrix.
	 * @param m33 Column 3, Row 3 of the matrix.
	 * @param m34 Column 3, Row 4 of the matrix.
	 * @param m41 Column 4, Row 1 of the matrix.
	 * @param m42 Column 4, Row 2 of the matrix.
	 * @param m43 Column 4, Row 3 of the matrix.
	 * @param m44 Column 4, Row 4 of the matrix.
	 */
	public Matrix4(float m11, float m12, float m13, float m14, 
				   float m21, float m22, float m23, float m24, 
				   float m31, float m32, float m33, float m34, 
				   float m41, float m42, float m43, float m44)
	{
		col0 = new Vector4(m11, m12, m13, m14);
		col1 = new Vector4(m21, m22, m23, m24);
		col2 = new Vector4(m31, m32, m33, m34);
		col3 = new Vector4(m41, m42, m43, m44);
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
	    
	    float m11 = array[0], m12 = array[1], m13 = array[2], m14 = array[3],
	          m21 = array[4], m22 = array[5], m23 = array[6], m24 = array[7],
	          m31 = array[8], m32 = array[9], m33 = array[10], m34 = array[11],
	          m41 = array[12], m42 = array[13], m43 = array[14], m44 = array[15];
		
		return m11 * m22 * m33 * m44 - m11 * m22 * m34 * m43 + m11 * m23 * m34 * m42 - m11 * m23 * m32 * m44
			 + m11 * m24 * m32 * m43 - m11 * m24 * m33 * m42 - m12 * m23 * m34 * m41 + m12 * m23 * m31 * m44
			 - m12 * m24 * m31 * m43 + m12 * m24 * m33 * m41 - m12 * m21 * m33 * m44 + m12 * m21 * m34 * m43
			 + m13 * m24 * m31 * m42 - m13 * m24 * m32 * m41 + m13 * m21 * m32 * m44 - m13 * m21 * m34 * m42
			 + m13 * m22 * m34 * m41 - m13 * m22 * m31 * m44 - m14 * m21 * m32 * m43 + m14 * m21 * m33 * m42
			 - m14 * m22 * m33 * m41 + m14 * m22 * m31 * m43 - m14 * m23 * m31 * m42 + m14 * m23 * m32 * m41;
	}
	
	/**
     * Formats the vector for text output.
     * @return A formatted string.
     */
    @Override
    public String toString()
    {
        return "| " + col0.x() + ", \t" + col1.x() + ", \t" + col2.x() + ", \t" + col3.x() + " \t|\n"
             + "| " + col0.y() + ", \t" + col1.y() + ", \t" + col2.y() + ", \t" + col3.y() + " \t|\n"
             + "| " + col0.z() + ", \t" + col1.z() + ", \t" + col2.z() + ", \t" + col3.z() + " \t|\n"
             + "| " + col0.w() + ", \t" + col1.w() + ", \t" + col2.w() + ", \t" + col3.w() + " \t|";
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
	public static Matrix4 multiply(Matrix4 left, Matrix4 right)
	{		
		//create local copies of matrix values for quicker access times
		float[] lArray = left.array();
		float[] rArray = right.array();
		
		//copy to M(col)(row) format for readability.
		float lM11 = lArray[0], lM12 = lArray[1], lM13 = lArray[2], lM14 = lArray[3],
			  lM21 = lArray[4], lM22 = lArray[5], lM23 = lArray[6], lM24 = lArray[7],
			  lM31 = lArray[8], lM32 = lArray[9], lM33 = lArray[10], lM34 = lArray[11],
			  lM41 = lArray[12], lM42 = lArray[13], lM43 = lArray[14], lM44 = lArray[15];
		
		float rM11 = rArray[0], rM12 = rArray[1], rM13 = rArray[2], rM14 = rArray[3],
			  rM21 = rArray[4], rM22 = rArray[5], rM23 = rArray[6], rM24 = rArray[7],
			  rM31 = rArray[8], rM32 = rArray[9], rM33 = rArray[10], rM34 = rArray[11],
			  rM41 = rArray[12], rM42 = rArray[13], rM43 = rArray[14], rM44 = rArray[15];
		
		float m11 = (lM11 * rM11) + (lM12 * rM21) + (lM13 * rM31) + (lM14 * rM41);
		float m12 = (lM11 * rM12) + (lM12 * rM22) + (lM13 * rM32) + (lM14 * rM42);
		float m13 = (lM11 * rM13) + (lM12 * rM23) + (lM13 * rM33) + (lM14 * rM43);
		float m14 = (lM11 * rM14) + (lM12 * rM24) + (lM13 * rM34) + (lM14 * rM44);
		float m21 = (lM21 * rM11) + (lM22 * rM21) + (lM23 * rM31) + (lM24 * rM41);
		float m22 = (lM21 * rM12) + (lM22 * rM22) + (lM23 * rM32) + (lM24 * rM42);
		float m23 = (lM21 * rM13) + (lM22 * rM23) + (lM23 * rM33) + (lM24 * rM43);
		float m24 = (lM21 * rM14) + (lM22 * rM24) + (lM23 * rM34) + (lM24 * rM44);
		float m31 = (lM31 * rM11) + (lM32 * rM21) + (lM33 * rM31) + (lM34 * rM41);
		float m32 = (lM31 * rM12) + (lM32 * rM22) + (lM33 * rM32) + (lM34 * rM42);
		float m33 = (lM31 * rM13) + (lM32 * rM23) + (lM33 * rM33) + (lM34 * rM43);
		float m34 = (lM31 * rM14) + (lM32 * rM24) + (lM33 * rM34) + (lM34 * rM44);
		float m41 = (lM41 * rM11) + (lM42 * rM21) + (lM43 * rM31) + (lM44 * rM41);
		float m42 = (lM41 * rM12) + (lM42 * rM22) + (lM43 * rM32) + (lM44 * rM42);
		float m43 = (lM41 * rM13) + (lM42 * rM23) + (lM43 * rM33) + (lM44 * rM43);
		float m44 = (lM41 * rM14) + (lM42 * rM24) + (lM43 * rM34) + (lM44 * rM44);
		
		return new Matrix4(m11, m12, m13, m14,
		                   m21, m22, m23, m24,
		                   m31, m32, m33, m34,
		                   m41, m42, m43, m44);
	}
	
	/**
	 * Turns a column-major matrix into a row-major matrix and vice versa. 
	 * @param mat The matrix to transpose.
	 * @return A transposed matrix.
	 */
	public static Matrix4 transpose(Matrix4 mat)
	{
	    return new Matrix4(mat.row0(), mat.row1(), mat.row2(), mat.row3());
	}
	
	/**
     * Creates a uniform scale matrix.
     * @param x Scaling in the X direction.
     * @param y Scaling in the Y direction.
     * @param z Scaling in the Z direction.
     * @return A scale matrix.
     */
    public static Matrix4 scale(float x, float y, float z)
    {
        return new Matrix4(x, 0, 0, 0,
                           0, y, 0, 0,
                           0, 0, z, 0,
                           0, 0, 0, 1);
    }
	
	/**
	 * Creates a uniform scale matrix.
	 * @param scale The amount to scale the matrix by.
	 * @return A scale matrix.
	 */
	public static Matrix4 scale(float scale)
	{
		return Matrix4.scale(scale, scale, scale);
	}
	
	/**
	 * Creates a uniform scale matrix.
	 * @param scale Scaling in the X, Y, and Z directions.
	 * @return A scale matrix.
	 */
	public static Matrix4 scale(Vector3 scale)
	{
		return Matrix4.scale(scale.x(), scale.y(), scale.z());
	}
	
	/**
	 * Creates a uniform scale matrix.
	 * @param scale Scaling in the X and Y directions.
	 * @return A scale matrix.
	 */
	public static Matrix4 scale(Vector2 scale)
	{
	    return Matrix4.scale(scale.x(), scale.y(), 1);
	}
	
	/**
     * Creates a translation matrix.
     * @param position A point in 3d space represented as a vector.
     * @return A translation matrix.
     */
    public static Matrix4 translate(Vector3 position)
    {
        return new Matrix4(Vector4.UNIT_X, Vector4.UNIT_Y, Vector4.UNIT_Z, new Vector4(position, 1));
    }
	
	/**
	 * Creates a translation matrix.
	 * @param position A position in 3d space represented as a vector.
	 * @return A translation matrix.
	 */
	public static Matrix4 translate(Vector2 position)
	{
		return Matrix4.translate(new Vector3(position, 0));
	}
		
	/**
	 * Creates a rotation matrix on the X axis.
	 * @param radians The angle to turn to. Measured in radians.
	 * @return A rotation matrix.
	 */
	public static Matrix4 rotateX(float radians)
	{		
		float sin = (float)Math.sin(radians);
		float cos = (float)Math.cos(radians);
		
		return new Matrix4(1, 0,    0,   0,
		                   0, cos,  sin, 0,
		                   0, -sin, cos, 0,
		                   0, 0,    0,   1);
	}
	
	/**
	 * Creates a rotation matrix on the Y axis.
	 * @param radians The angle to turn to. Measured in radians.
	 * @return A rotation matrix.
	 */
	public static Matrix4 rotateY(float radians)
	{
		float sin = (float)Math.sin(radians);
		float cos = (float)Math.cos(radians);
		
		return new Matrix4(cos, 0,  -sin, 0,
		                   0,   1,  0,    0,
		                   sin, 0,  cos,  0,
		                   0,   0,  0,    1);
	}
	
	/**
	 * Creates a rotation matrix on the Z axis.
	 * @param radians The angle to turn to. Measured in radians.
	 * @return A rotation matrix.
	 */
	public static Matrix4 rotateZ(float radians)
	{		
		float sin = (float)Math.sin(radians);
		float cos = (float)Math.cos(radians);
		
		return new Matrix4(cos, sin, 0, 0,
		                  -sin, cos, 0, 0,
		                   0,   0,   1, 0,
		                   0,   0,   0, 1);
	}
	
	/**
	 * Creates an orthographic projection matrix.
	 * @param left The left bound of the projection.
	 * @param right The right bound of the projection.
	 * @param top The upper bound of the projection.
	 * @param bottom The lower bound of the projection.
	 * @param near The location of the near clipping plane.
	 * @param far The location of the far clipping plane.
	 * @return An orthographic projection matrix.
	 * \todo don't use an array.
	 */
	public static Matrix4 ortho(float left, float right, float top, float bottom, float near, float far)
	{
		float rl = 1.0f / (right - left);
		float tb = 1.0f / (top - bottom);
		float fn = 1.0f / (far - near);
		
		return new Matrix4(2 * rl,                0,                      0,                  0,
		                   0,                     2 * tb,                 0,                  0,
		                   0,                     0,                      -2 * fn,            0,
		                   -(right + left) * rl,  -(top + bottom) * tb,   -(far + near) * fn, 1);
	}
	
	/**************************
	 * Accessors and Mutators *
	 **************************/
	
	/**
	 * Gets the matrix element at column 1, row 1.
	 * @return The X component of the first column.
	 */
	public float m11() 
	{
	    return col0.x();
	}
	
	/**
	 * Gets the matrix element at column 1, row 2.
	 * @return The Y component of the first column.
	 */
	public float m12() 
	{
	    return col0.y();
	}
	
	/**
	 * Gets the matrix element at column 1, row 3.
	 * @return The X component of the first column.
	 */
	public float m13() 
	{
	    return col0.z();
	}
	
	/**
	 * Gets the matrix element at column 1, row 4.
	 * @return The X component of the first column.
	 */
	public float m14() 
	{
	    return col0.w();
	}
	
	/**
	 * Gets the matrix element at column 2, row 1.
	 * @return The X component of the second column.
	 */
	public float m21() 
	{
	    return col1.x();
	}
	
	/**
	 * Gets the matrix element at column 2, row 2.
	 * @return The X component of the second column.
	 */
	public float m22() 
	{
	    return col1.y();
	}
	
	/**
	 * Gets the matrix element at column 2, row 3.
	 * @return The X component of the second column.
	 */
	public float m23()
	{
	    return col1.z();
	}
	
	/**
	 * Gets the matrix element at column 2, row 4.
	 * @return The X component of the second column.
	 */
	public float m24()
	{
	    return col1.w();
	}
	
	/**
	 * Gets the matrix element at column 3, row 1.
	 * @return The X component of the third column.
	 */
	public float m31() 
	{
	    return col2.x();
	}
	
	/**
	 * Gets the matrix element at column 3, row 2.
	 * @return The X component of the third column.
	 */
	public float m32() 
	{
	    return col2.y();
	}
	
	/**
	 * Gets the matrix element at column 3, row 3.
	 * @return The X component of the third column.
	 */
	public float m33() 
	{
	    return col2.z();
	}
	
	/**
	 * Gets the matrix element at column 3, row 4.
	 * @return The X component of the third column.
	 */
	public float m34() 
	{
	    return col2.w();
	}
	
	/**
	 * Gets the matrix element at column 4, row 1.
	 * @return The X component of the fourth column.
	 */
	public float m41() 
	{
	    return col3.x();
	}
	
	/**
	 * Gets the matrix element at column 4, row 2.
	 * @return The X component of the fourth column.
	 */
	public float m42() 
	{
	    return col3.y();
	}
	
	/**
	 * Gets the matrix element at column 4, row 3.
	 * @return The X component of the fourth column.
	 */
	public float m43() 
	{
	    return col3.z();
	}
	
	/**
	 * Gets the matrix element at column 4, row 4.
	 * @return The X component of the fourth column.
	 */
	public float m44() 
	{
	    return col3.w();
	}
	
	/**
	 * Gets the first column of the matrix.
	 * @return A Vector4 containing the first column.
	 */
	public Vector4 col0() 
	{
	    return col0;
	}
	
	/**
	 * Gets the second column of the matrix.
	 * @return A Vector4 containing the second column.
	 */
	public Vector4 col1() 
	{
	    return col1;
	}
	
	/**
	 * Gets the third column of the matrix.
	 * @return A Vector4 containing the third column.
	 */
	public Vector4 col2()
	{
	    return col2;
	}
	
	/**
	 * Gets the fourth column of the matrix.
	 * @return A Vector4 containing the fourth column.
	 */
	public Vector4 col3() 
	{
	    return col3;
	}
	
	/**
	 * Gets the first row of the matrix.
	 * @return A Vector4 containing the X components of all the columns.
	 */
	public Vector4 row0() 
	{
	    return new Vector4(col0.x(), col1.x(), col2.x(), col3.x());
	}
	
	/**
	 * Gets the second row of the matrix.
	 * @return A Vector4 containing the Y components of all the columns.
	 */
	public Vector4 row1() 
	{
	    return new Vector4(col0.y(), col1.y(), col2.y(), col3.y());
	}
	
	/**
	 * Gets the third row of the matrix.
	 * @return A Vector4 containing the Z components of all the columns.
	 */
	public Vector4 row2()
	{
	    return new Vector4(col0.z(), col1.z(), col2.z(), col3.z());
	}
	
	/**
	 * Gets the fourth column of the matrix.
	 * @return A Vector4 containing the W components of all the columns.
	 */
	public Vector4 row3()
	{
	    return new Vector4(col0.w(), col1.w(), col2.w(), col3.w());
	}
	
	/**
	 * Gets an array containing all the elements of the matrix.
	 * @return A float[16] containing all the elements of the matrix in column-major order.
	 */
	public float[] array()
	{	    
	    float[] array = new float[16];
	    float[] c0 = col0.array(), c1 = col1.array(), c2 = col2.array(), c3 = col3.array();
	    
	    //iterate through the components (x, y, z, w)
	    //add that component from every vector to the properly offset array location.
	    for (int i = 0; i < 4; i++)
	    {
	        array[i] = c0[i];
	        array[i + 4] = c1[i];
	        array[i + 8] = c2[i];
	        array[i + 12] = c3[i];
	    }
	    
	    return array;
	}
}
