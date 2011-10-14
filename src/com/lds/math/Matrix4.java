package com.lds.math;

//TODO Gauss-Jordan elimination for an inverse method?

/**
 * A 4x4 matrix class.
 * @author Lightning Development Studios
 */
public final class Matrix4 
{
    /***********
     * Members *
     ***********/
    
    /**
     * The first row of the matrix.
     */
    private final Vector4 row0;
    
    /**
     * The second row of the matrix.
     */
    private final Vector4 row1;
    
    /**
     * The third row of the matrix.
     */
    private final Vector4 row2;
    
    /**
     * The fourth row of the matrix.
     */
    private final Vector4 row3;
    
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
	 * @param column0 The first column of the matrix.
	 * @param column1 The second column of the matrix.
	 * @param column2 The third column of the matrix.
	 * @param column3 The fourth column of the matrix.
	 */
	public Matrix4(Vector4 row0, Vector4 row1, Vector4 row2, Vector4 row3)
	{
		this.row0 = row0;
		this.row1 = row1;
		this.row2 = row2;
		this.row3 = row3;
	}
	
	/**
	 * Creates a new instance of the Matrix class.
	 * @param m11 Row 1, Column 1 of the matrix.
	 * @param m12 Row 1, Column 2 of the matrix.
	 * @param m13 Row 1, Column 3 of the matrix.
	 * @param m14 Row 1, Column 4 of the matrix.
	 * @param m21 Row 2, Column 1 of the matrix.
	 * @param m22 Row 2, Column 2 of the matrix.
	 * @param m23 Row 2, Column 3 of the matrix.
	 * @param m24 Row 2, Column 4 of the matrix.
	 * @param m31 Row 3, Column 1 of the matrix.
	 * @param m32 Row 3, Column 2 of the matrix.
	 * @param m33 Row 3, Column 3 of the matrix.
	 * @param m34 Row 3, Column 4 of the matrix.
	 * @param m41 Row 4, Column 1 of the matrix.
	 * @param m42 Row 4, Column 2 of the matrix.
	 * @param m43 Row 4, Column 3 of the matrix.
	 * @param m44 Row 4, Column 4 of the matrix.
	 */
	public Matrix4(float m11, float m12, float m13, float m14, 
				   float m21, float m22, float m23, float m24, 
				   float m31, float m32, float m33, float m34, 
				   float m41, float m42, float m43, float m44)
	{
		row0 = new Vector4(m11, m12, m13, m14);
		row1 = new Vector4(m21, m22, m23, m24);
		row2 = new Vector4(m31, m32, m33, m34);
		row3 = new Vector4(m41, m42, m43, m44);
	}
	
	/**
	 * Creates a new instance of the Matrix4 class.
	 * @param data An array of 16 floats in column-major order.
	 * @deprecated Don't use arrays.
	 */
	public Matrix4(float[] data)
	{
		if (data.length == 16)
		{
		    row0 = new Vector4(data[0], data[1], data[2], data[3]);
		    row1 = new Vector4(data[4], data[5], data[6], data[7]);
		    row2 = new Vector4(data[8], data[9], data[10], data[11]);
		    row3 = new Vector4(data[12], data[13], data[14], data[15]);
		}
		else
			throw new IllegalArgumentException("elements is not of proper size (16).");
	}
	
	/**
	 * Gets the determinant of this matrix.
	 * @return The determinant of the matrix.
	 */
	public float determinant()
	{
		final float m11 = row0.getX(), m12 = row0.getY(), m13 = row0.getZ(), m14 = row0.getW(),
		            m21 = row1.getX(), m22 = row1.getY(), m23 = row1.getZ(), m24 = row1.getW(),
		            m31 = row2.getX(), m32 = row2.getY(), m33 = row2.getZ(), m34 = row2.getW(),
		            m41 = row3.getX(), m42 = row3.getY(), m43 = row3.getZ(), m44 = row3.getW();
		
		return m11 * m22 * m33 * m44 - m11 * m22 * m34 * m43 + m11 * m23 * m34 * m42 - m11 * m23 * m32 * m44
			 + m11 * m24 * m32 * m43 - m11 * m24 * m33 * m42 - m12 * m23 * m34 * m41 + m12 * m23 * m31 * m44
			 - m12 * m24 * m31 * m43 + m12 * m24 * m33 * m41 - m12 * m21 * m33 * m44 + m12 * m21 * m34 * m43
			 + m13 * m24 * m31 * m42 - m13 * m24 * m32 * m41 + m13 * m21 * m32 * m44 - m13 * m21 * m34 * m42
			 + m13 * m22 * m34 * m41 - m13 * m22 * m31 * m44 - m14 * m21 * m32 * m43 + m14 * m21 * m33 * m42
			 - m14 * m22 * m33 * m41 + m14 * m22 * m31 * m43 - m14 * m23 * m31 * m42 + m14 * m23 * m32 * m41;
	}
	
	/******************
	 * Static Methods *
	 ******************/
	
	/**
	 * Multiplies two matrices together.
	 * @param left The left operand matrix.
	 * @param right The right operand matrix.
	 * @return A new matrix that is the result of the multiplication.
	 * \todo don't use array
	 */
	public static Matrix4 multiply(Matrix4 left, Matrix4 right)
	{
		float[] result = new float[16];
		
		//create local copies of matrix values for quicker access times
		float[] lArray = left.array();
		float[] rArray = right.array();
		
		//copy to M(row)(column) format for readability.
		//TODO multiply direct array values, if it's faster, time should be negligible. 
		float lM11 = lArray[0], lM12 = lArray[1], lM13 = lArray[2], lM14 = lArray[3],
			  lM21 = lArray[4], lM22 = lArray[5], lM23 = lArray[6], lM24 = lArray[7],
			  lM31 = lArray[8], lM32 = lArray[9], lM33 = lArray[10], lM34 = lArray[11],
			  lM41 = lArray[12], lM42 = lArray[13], lM43 = lArray[14], lM44 = lArray[15];
		
		float rM11 = rArray[0], rM12 = rArray[1], rM13 = rArray[2], rM14 = rArray[3],
			  rM21 = rArray[4], rM22 = rArray[5], rM23 = rArray[6], rM24 = rArray[7],
			  rM31 = rArray[8], rM32 = rArray[9], rM33 = rArray[10], rM34 = rArray[11],
			  rM41 = rArray[12], rM42 = rArray[13], rM43 = rArray[14], rM44 = rArray[15];
		
		result[0]  = (lM11 * rM11) + (lM12 * rM21) + (lM13 * rM31) + (lM14 * rM41);
		result[1]  = (lM11 * rM12) + (lM12 * rM22) + (lM13 * rM32) + (lM14 * rM42);
		result[2]  = (lM11 * rM13) + (lM12 * rM23) + (lM13 * rM33) + (lM14 * rM43);
		result[3]  = (lM11 * rM14) + (lM12 * rM24) + (lM13 * rM34) + (lM14 * rM44);
		result[4]  = (lM21 * rM11) + (lM22 * rM21) + (lM23 * rM31) + (lM24 * rM41);
		result[5]  = (lM21 * rM12) + (lM22 * rM22) + (lM23 * rM32) + (lM24 * rM42);
		result[6]  = (lM21 * rM13) + (lM22 * rM23) + (lM23 * rM33) + (lM24 * rM43);
		result[7]  = (lM21 * rM14) + (lM22 * rM24) + (lM23 * rM34) + (lM24 * rM44);
		result[8]  = (lM31 * rM11) + (lM32 * rM21) + (lM33 * rM31) + (lM34 * rM41);
		result[9]  = (lM31 * rM12) + (lM32 * rM22) + (lM33 * rM32) + (lM34 * rM42);
		result[10] = (lM31 * rM13) + (lM32 * rM23) + (lM33 * rM33) + (lM34 * rM43);
		result[11] = (lM31 * rM14) + (lM32 * rM24) + (lM33 * rM34) + (lM34 * rM44);
		result[12] = (lM41 * rM11) + (lM42 * rM21) + (lM43 * rM31) + (lM44 * rM41);
		result[13] = (lM41 * rM12) + (lM42 * rM22) + (lM43 * rM32) + (lM44 * rM42);
		result[14] = (lM41 * rM13) + (lM42 * rM23) + (lM43 * rM33) + (lM44 * rM43);
		result[15] = (lM41 * rM14) + (lM42 * rM24) + (lM43 * rM34) + (lM44 * rM44);
		
		return new Matrix4(result);
	}
	
	/**
	 * Turns a column-major matrix into a row-major matrix and vice versa. 
	 * @param mat The matrix to transpose.
	 * @return A transposed matrix.
	 */
	public static Matrix4 transpose(Matrix4 mat)
	{
	    return new Matrix4(mat.column0(), mat.column1(), mat.column2(), mat.column3());
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
	 * Creates a non-uniform scale matrix.
	 * @param scale The vector to scale by.
	 * @return A scale matrix.
	 */
	public static Matrix4 scale(Vector3 scale)
	{
		return Matrix4.scale(scale.getX(), scale.getY(), scale.getZ());
	}
	
	/**
	 * Creates a non-uniform scale matrix.
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
	
	public static Matrix4 scale(Vector2 scale)
	{
	    return Matrix4.scale(scale.getX(), scale.getY(), 1);
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
	 * Creates a translation matrix.
	 * @param position A point in 3d space represented as a vector.
	 * @return A translation matrix.
	 */
	public static Matrix4 translate(Vector3 position)
	{
	    return new Matrix4(Vector4.unitX, Vector4.unitY, Vector4.unitZ, new Vector4(position, 1));
	}
	
	/**
	 * Creates a translation matrix.
	 * @param x The X component of a point in 2d space.
	 * @param y The Y component of a point in 2d space.
	 * @return A translation matrix.
	 * @deprecated Use Vector2 or Vector3 instead.
	 */
	public static Matrix4 translate(float x, float y)
	{
		return Matrix4.translate(x, y, 0);
	}
	
	/**
	 * Creates a translation matrix.
	 * @param x The X component of a point in 3d space.
	 * @param y The Y component of a point in 3d space.
	 * @param z The Z component of a point in 3d space.
	 * @return A translation matrix.
	 * @deprecated Use Vector2 or Vector3 instead.
	 */
	public static Matrix4 translate(float x, float y, float z)
	{
	    return Matrix4.translate(new Vector3(x, y, z));
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
		float[] result = new float[16];
		
		float rl = 1.0f / (right - left);
		float tb = 1.0f / (top - bottom);
		float fn = 1.0f / (far - near);
		
		result[0] = 2 * rl;
		result[5] = 2 * tb;
		result[10] = -2 * fn;
		
		result[3] = rl * -(right + left);
		result[7] = tb * -(top + bottom);
		result[11] = fn * -(far + near);
		
		return new Matrix4(result);
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
	    return row0.getX();
	}
	
	/**
	 * Gets the matrix element at row 1, column 2.
	 * @return The Y component of the first column.
	 */
	public float m12() 
	{
	    return row0.getY();
	}
	
	/**
	 * Gets the matrix element at row 1, column 3.
	 * @return The X component of the first column.
	 */
	public float m13() 
	{
	    return row0.getZ();
	}
	
	/**
	 * Gets the matrix element at row 1, column 4.
	 * @return The X component of the first column.
	 */
	public float m14() 
	{
	    return row0.getW();
	}
	
	/**
	 * Gets the matrix element at row 2, column 1.
	 * @return The X component of the second column.
	 */
	public float m21() 
	{
	    return row1.getX();
	}
	
	/**
	 * Gets the matrix element at row 2, column 2.
	 * @return The X component of the second column.
	 */
	public float m22() 
	{
	    return row1.getY();
	}
	
	/**
	 * Gets the matrix element at row 2, column 3.
	 * @return The X component of the second column.
	 */
	public float m23()
	{
	    return row1.getZ();
	}
	
	/**
	 * Gets the matrix element at row 2, column 4.
	 * @return The X component of the second column.
	 */
	public float m24()
	{
	    return row1.getW();
	}
	
	/**
	 * Gets the matrix element at row 3, column 1.
	 * @return The X component of the third column.
	 */
	public float m31() 
	{
	    return row2.getX();
	}
	
	/**
	 * Gets the matrix element at row 3, column 2.
	 * @return The X component of the third column.
	 */
	public float m32() 
	{
	    return row2.getY();
	}
	
	/**
	 * Gets the matrix element at row 3, column 3.
	 * @return The X component of the third column.
	 */
	public float m33() 
	{
	    return row2.getZ();
	}
	
	/**
	 * Gets the matrix element at row 3, column 4.
	 * @return The X component of the third column.
	 */
	public float m34() 
	{
	    return row2.getW();
	}
	
	/**
	 * Gets the matrix element at row 4, column 1.
	 * @return The X component of the fourth column.
	 */
	public float m41() 
	{
	    return row3.getX();
	}
	
	/**
	 * Gets the matrix element at row 4, column 2.
	 * @return The X component of the fourth column.
	 */
	public float m42() 
	{
	    return row3.getY();
	}
	
	/**
	 * Gets the matrix element at row 4, column 3.
	 * @return The X component of the fourth column.
	 */
	public float m43() 
	{
	    return row3.getZ();
	}
	
	/**
	 * Gets the matrix element at row 4, column 4.
	 * @return The X component of the fourth column.
	 */
	public float m44() 
	{
	    return row3.getW();
	}
	
	/**
	 * Gets the first row of the matrix.
	 * @return A Vector4 containing the X components of all the columns.
	 */
	public Vector4 row0() 
	{
	    return row0;
	}
	
	/**
	 * Gets the second row of the matrix.
	 * @return A Vector4 containing the X components of all the columns.
	 */
	public Vector4 row1() 
	{
	    return row1;
	}
	
	/**
	 * Gets the third row of the matrix.
	 * @return A Vector4 containing the X components of all the columns.
	 */
	public Vector4 row2()
	{
	    return row2;
	}
	
	/**
	 * Gets the fourth row of the matrix.
	 * @return A Vector4 containing the X components of all the columns.
	 */
	public Vector4 row3() 
	{
	    return row3;
	}
	
	/**
	 * Gets the first column of the matrix.
	 * @return A Vector4 containing all the components of the first column.
	 */
	public Vector4 column0() 
	{
	    return new Vector4(row0.getX(), row1.getX(), row2.getX(), row3.getX());
	}
	
	/**
	 * Gets the second column of the matrix.
	 * @return A Vector4 containing all the components of the second column.
	 */
	public Vector4 column1() 
	{
	    return new Vector4(row0.getY(), row1.getY(), row2.getY(), row3.getY());
	}
	
	/**
	 * Gets the third column of the matrix.
	 * @return A Vector4 containing all the components of the third column.
	 */
	public Vector4 column2()
	{
	    return new Vector4(row0.getZ(), row1.getZ(), row2.getZ(), row3.getZ());
	}
	
	/**
	 * Gets the fourth column of the matrix.
	 * @return A Vector4 containing all the components of the fourth column.
	 */
	public Vector4 column3()
	{
	    return new Vector4(row0.getW(), row1.getW(), row2.getW(), row3.getW());
	}
	
	/**
	 * Gets an array containing all the elements of the matrix.
	 * @return A float[16] containing all the elements of the matrix in column-major order.
	 */
	public float[] array()
	{
	    return new float[]
        {
	        row0.getX(), row0.getY(), row0.getZ(), row0.getW(),
	        row1.getX(), row1.getY(), row1.getZ(), row1.getW(),
	        row2.getX(), row2.getY(), row2.getZ(), row2.getW(),
	        row3.getX(), row3.getY(), row3.getZ(), row3.getW()
        };
	}
	
	/*************
     * Constants *
     *************/
    
    /**
     * The identity matrix, where each row is it's respective unit vector.
     */
    public static final Matrix4 identity = new Matrix4(1, 0, 0, 0,
                                                       0, 1, 0, 0,
                                                       0, 0, 1, 0,
                                                       0, 0, 0, 1);
    
}
