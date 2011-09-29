package com.lds.math;

//TODO Gauss-Jordan elimination for an inverse method. Only if we need that method.

/**
 * A 4x4 matrix class.
 * @author Lightning Development Studios
 * 
 */
public class Matrix4 
{
	/*************
	 * Constants *
	 *************/
	
	/**
	 * The identity matrix, where each row is it's respective unit vector.
	 */
	public static final Matrix4 Identity = new Matrix4( 1, 0, 0, 0,
														0, 1, 0, 0,
														0, 0, 1, 0,
														0, 0, 0, 1);
	
	/***********
	 * Members *
	 ***********/
	
	/**
	 * Column-major order.
	 */
	private float[] elements;
	
	/****************
	 * Constructors *
	 ****************/
	
	/**
	 * Creates a new instance of the Matrix4 class. All elements initialized to 0.
	 */
	public Matrix4()
	{
		elements = new float[16];
	}
	
	/**
	 * Creates a new instance of the Matrix4 class.
	 * @param column0 The first column of the matrix.
	 * @param column1 The second column of the matrix.
	 * @param column2 The third column of the matrix.
	 * @param column3 The fourth column of the matrix.
	 */
	public Matrix4(Vector4 column0, Vector4 column1, Vector4 column2, Vector4 column3)
	{
		this();
		
		float[] col0 = column0.getArray();
		float[] col1 = column1.getArray();
		float[] col2 = column2.getArray();
		float[] col3 = column3.getArray();
		
		elements[0]  = col0[0];
		elements[1]  = col0[1];
		elements[2]  = col0[2];
		elements[3]  = col0[3];
		elements[4]  = col1[0];
		elements[5]  = col1[1];
		elements[6]  = col1[2];
		elements[7]  = col1[3];
		elements[8]  = col2[0];
		elements[9]  = col2[1];
		elements[10] = col2[2];
		elements[11] = col2[3];
		elements[12] = col3[0];
		elements[13] = col3[1];
		elements[14] = col3[2];
		elements[15] = col3[3];
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
	public Matrix4(	float m11, float m12, float m13, float m14, 
					float m21, float m22, float m23, float m24, 
					float m31, float m32, float m33, float m34, 
					float m41, float m42, float m43, float m44)
	{
		this();
		
		elements[0]  = m11;
		elements[1]  = m12;
		elements[2]  = m13;
		elements[3]  = m14;
		elements[4]  = m21;
		elements[5]  = m22;
		elements[6]  = m23;
		elements[7]  = m24;	
		elements[8]  = m31;
		elements[9]  = m32;
		elements[10] = m33;
		elements[11] = m34;
		elements[12] = m41;
		elements[13] = m42;
		elements[14] = m43;
		elements[15] = m44;
	}
	
	/**
	 * Creates a new instance of the Matrix4 class.
	 * @param elements An array of 16 floats in column-major order.
	 */
	public Matrix4(float[] elements)
	{
		if (elements.length == 16)
			this.elements = elements;
		
		else
			throw new IllegalArgumentException("elements is not of proper size (16).");
	}
	
	/********************
	 * Instance Methods *
	 ********************/
	
	/**
	 * Converts the matrix to row-major order.
	 */
	public void transpose()
	{
		this.elements = Matrix4.transpose(this).array();
	}
	
	/**
	 * Multiplies another matrix to this one.
	 * @param right The right operand.
	 */
	public void multiply(Matrix4 right)
	{
		this.elements = Matrix4.multiply(this, right).array();
	}
	
	/**
	 * Gets the determinant of this matrix.
	 * @return The determinant of the matrix.
	 */
	public float determinant()
	{
		float m11 = elements[0], m12 = elements[1], m13 = elements[2], m14 = elements[3],
			  m21 = elements[4], m22 = elements[5], m23 = elements[6], m24 = elements[7],
			  m31 = elements[8], m32 = elements[9], m33 = elements[10], m34 = elements[11],
			  m41 = elements[12], m42 = elements[13], m43 = elements[14], m44 = elements[15];
		
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
	 * Turns a column-major matrix into a row-major matrix and vica versa. 
	 * @param mat The matrix to transpose.
	 * @return A transposed matrix.
	 */
	public static Matrix4 transpose(Matrix4 mat)
	{
		float[] result = new float[16];
		
		float[] values = mat.array();
		
		result[0]  = values[0];
		result[1]  = values[4];
		result[2]  = values[8];
		result[3]  = values[12];
		result[4]  = values[1];
		result[5]  = values[5];
		result[6]  = values[9];
		result[7]  = values[13];
		result[8]  = values[2];
		result[9]  = values[6];
		result[10] = values[10];
		result[11] = values[14];
		result[12] = values[3];
		result[13] = values[7];
		result[14] = values[11];
		result[15] = values[15];
		
		return new Matrix4(result);
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
		Matrix4 scaleMat = Matrix4.Identity;
		
		scaleMat.setM11(x);
		scaleMat.setM22(y);
		scaleMat.setM33(z);
		
		return scaleMat;
	}
	
	/**
	 * Creates a translation matrix.
	 * @param position A position in 3d space represented as a vector.
	 * @return A translation matrix.
	 */
	public static Matrix4 translate(Vector2 position)
	{
		return Matrix4.translate(position.getX(), position.getY(), 0);
	}
	
	/**
	 * Creates a translation matrix.
	 * @param position A point in 3d space represented as a vector.
	 * @return A translation matrix.
	 */
	public static Matrix4 translate(Vector3 position)
	{
		return Matrix4.translate(position.getX(), position.getY(), position.getZ());
	}
	
	/**
	 * Creates a translation matrix.
	 * @param x The X component of a point in 2d space.
	 * @param y The Y component of a point in 2d space.
	 * @return A translation matrix.
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
	 */
	public static Matrix4 translate(float x, float y, float z)
	{
		Matrix4 transMat = Matrix4.Identity;
		transMat.setM14(x);
		transMat.setM24(y);
		transMat.setM34(z);
		
		return transMat;
	}
	
	/**
	 * Creates a rotation matrix on the X axis.
	 * @param radians The angle to turn to. Measured in radians.
	 * @return A rotation matrix.
	 */
	public static Matrix4 rotateX(float radians)
	{
		Matrix4 rotMat = Matrix4.Identity;
		
		float sin = (float)Math.sin(radians);
		float cos = (float)Math.cos(radians);
		
		rotMat.setM22(cos);
		rotMat.setM23(sin);
		rotMat.setM32(-sin);
		rotMat.setM33(cos);
		
		return rotMat;
	}
	
	/**
	 * Creates a rotation matrix on the Y axis.
	 * @param radians The angle to turn to. Measured in radians.
	 * @return A rotation matrix.
	 */
	public static Matrix4 rotateY(float radians)
	{
		Matrix4 rotMat = Matrix4.Identity;
		
		float sin = (float)Math.sin(radians);
		float cos = (float)Math.cos(radians);
		
		rotMat.setM11(cos);
		rotMat.setM13(-sin);
		rotMat.setM31(sin);
		rotMat.setM33(cos);
		
		return rotMat;
	}
	
	/**
	 * Creates a rotation matrix on the Z axis.
	 * @param radians The angle to turn to. Measured in radians.
	 * @return A rotation matrix.
	 */
	public static Matrix4 rotateZ(float radians)
	{
		Matrix4 rotMat = Matrix4.Identity;
		
		float sin = (float)Math.sin(radians);
		float cos = (float)Math.cos(radians);
		
		rotMat.setM11(cos);
		rotMat.setM12(sin);
		rotMat.setM21(-sin);
		rotMat.setM22(cos);
		
		return rotMat;
	}
	
	/**************************
	 * Accessors and Mutators *
	 **************************/
	
	/**
	 * Gets the matrix element at row 1, column 1.
	 * @return The X component of the first column.
	 */
	public float m11() { return elements[0]; }
	
	/**
	 * Gets the matrix element at row 1, column 2.
	 * @return The Y component of the first column.
	 */
	public float m12() { return elements[1]; }
	
	/**
	 * Gets the matrix element at row 1, column 3.
	 * @return The X component of the first column.
	 */
	public float m13() { return elements[2]; }
	
	/**
	 * Gets the matrix element at row 1, column 4.
	 * @return The X component of the first column.
	 */
	public float m14() { return elements[3]; }
	
	/**
	 * Gets the matrix element at row 2, column 1.
	 * @return The X component of the second column.
	 */
	public float m21() { return elements[4]; }
	
	/**
	 * Gets the matrix element at row 2, column 2.
	 * @return The X component of the second column.
	 */
	public float m22() { return elements[5]; }
	
	/**
	 * Gets the matrix element at row 2, column 3.
	 * @return The X component of the second column.
	 */
	public float m23() { return elements[6]; }
	
	/**
	 * Gets the matrix element at row 2, column 4.
	 * @return The X component of the second column.
	 */
	public float m24() { return elements[7]; }
	
	/**
	 * Gets the matrix element at row 3, column 1.
	 * @return The X component of the third column.
	 */
	public float m31() { return elements[8]; }
	
	/**
	 * Gets the matrix element at row 3, column 2.
	 * @return The X component of the third column.
	 */
	public float m32() { return elements[9]; }
	
	/**
	 * Gets the matrix element at row 3, column 3.
	 * @return The X component of the third column.
	 */
	public float m33() { return elements[10]; }
	
	/**
	 * Gets the matrix element at row 3, column 4.
	 * @return The X component of the third column.
	 */
	public float m34() { return elements[11]; }
	
	/**
	 * Gets the matrix element at row 4, column 1.
	 * @return The X component of the fourth column.
	 */
	public float m41() { return elements[12]; }
	
	/**
	 * Gets the matrix element at row 4, column 2.
	 * @return The X component of the fourth column.
	 */
	public float m42() { return elements[13]; }
	
	/**
	 * Gets the matrix element at row 4, column 3.
	 * @return The X component of the fourth column.
	 */
	public float m43() { return elements[14]; }
	
	/**
	 * Gets the matrix element at row 4, column 4.
	 * @return The X component of the fourth column.
	 */
	public float m44() { return elements[15]; }
	
	/**
	 * Gets the first row of the matrix.
	 * @return A Vector4 containing the X components of all the columns.
	 */
	public Vector4 row0() { return new Vector4(elements[0], elements[4], elements[8], elements[12]); }
	
	/**
	 * Gets the second row of the matrix.
	 * @return A Vector4 containing the X components of all the columns.
	 */
	public Vector4 row1() { return new Vector4(elements[1], elements[5], elements[9], elements[13]); }
	
	/**
	 * Gets the third row of the matrix.
	 * @return A Vector4 containing the X components of all the columns.
	 */
	public Vector4 row2() { return new Vector4(elements[2], elements[6], elements[10], elements[14]); }
	
	/**
	 * Gets the fourth row of the matrix.
	 * @return A Vector4 containing the X components of all the columns.
	 */
	public Vector4 row3() { return new Vector4(elements[3], elements[7], elements[11], elements[15]); }
	
	/**
	 * Gets the first column of the matrix.
	 * @return A Vector4 containing all the components of the first column.
	 */
	public Vector4 column0() { return new Vector4(elements[0], elements[1], elements[2], elements[3]); }
	
	/**
	 * Gets the second column of the matrix.
	 * @return A Vector4 containing all the components of the second column.
	 */
	public Vector4 column1() { return new Vector4(elements[4], elements[5], elements[6], elements[7]); }
	
	/**
	 * Gets the third column of the matrix.
	 * @return A Vector4 containing all the components of the third column.
	 */
	public Vector4 column2() { return new Vector4(elements[8], elements[9], elements[10], elements[11]); }
	
	/**
	 * Gets the fourth column of the matrix.
	 * @return A Vector4 containing all the components of the fourth column.
	 */
	public Vector4 column3() { return new Vector4(elements[12], elements[13], elements[14], elements[15]); }
	
	/**
	 * Gets an array containing all the elements of the matrix.
	 * @return A float[16] containing all the elements of the matrix in column-major order.
	 */
	public float[] array() { return elements; }
	
	/**
	 * Sets the matrix element at row 1, column 1.
	 * @param value The new element value.
	 */
	public void setM11(float value) { elements[0] = value; }
	
	/**
	 * Sets the matrix element at row 1, column 2.
	 * @param value The new element value.
	 */
	public void setM12(float value) { elements[1] = value; }
	
	/**
	 * Sets the matrix element at row 1, column 3.
	 * @param value The new element value.
	 */
	public void setM13(float value) { elements[2] = value; }
	
	/**
	 * Sets the matrix element at row 1, column 4.
	 * @param value The new element value.
	 */
	public void setM14(float value) { elements[3] = value; }
	
	/**
	 * Sets the matrix element at row 2, column 1.
	 * @param value The new element value.
	 */
	public void setM21(float value) { elements[4] = value; }
	
	/**
	 * Sets the matrix element at row 2, column 2.
	 * @param value The new element value.
	 */
	public void setM22(float value) { elements[5] = value; }
	
	/**
	 * Sets the matrix element at row 2, column 3.
	 * @param value The new element value.
	 */
	public void setM23(float value) { elements[6] = value; }
	
	/**
	 * Sets the matrix element at row 2, column 4.
	 * @param value The new element value.
	 */
	public void setM24(float value) { elements[7] = value; }
	
	/**
	 * Sets the matrix element at row 3, column 1.
	 * @param value The new element value.
	 */
	public void setM31(float value) { elements[8] = value; }
	
	/**
	 * Sets the matrix element at row 3, column 2.
	 * @param value The new element value.
	 */
	public void setM32(float value) { elements[9] = value; }
	
	/**
	 * Sets the matrix element at row 3, column 3.
	 * @param value The new element value.
	 */
	public void setM33(float value) { elements[10] = value; }
	
	/**
	 * Sets the matrix element at row 3, column 4.
	 * @param value The new element value.
	 */
	public void setM34(float value) { elements[11] = value; }
	
	/**
	 * Sets the matrix element at row 4, column 1.
	 * @param value The new element value.
	 */
	public void setM41(float value) { elements[12] = value; }
	
	/**
	 * Sets the matrix element at row 4, column 2.
	 * @param value The new element value.
	 */
	public void setM42(float value) { elements[13] = value; }
	
	/**
	 * Sets the matrix element at row 4, column 3.
	 * @param value The new element value.
	 */
	public void setM43(float value) { elements[14] = value; }
	
	/**
	 * Sets the matrix element at row 4, column 4.
	 * @param value The new element value.
	 */
	public void setM44(float value) { elements[15] = value; }
	
	/**
	 * Sets the X components of all the columns.
	 * @param value The Vector4 containing the elements.
	 */
	public void setRow0(Vector4 value) { elements[0] = value.getX(); elements[4] = value.getY(); elements[8] = value.getZ(); elements[12] = value.getW(); }
	
	/**
	 * Sets the Y components of all the columns.
	 * @param value The Vector4 containing the elements.
	 */
	public void setRow1(Vector4 value) { elements[1] = value.getX(); elements[5] = value.getY(); elements[9] = value.getZ(); elements[13] = value.getW(); }
	
	/**
	 * Sets the Z components of all the columns.
	 * @param value The Vector4 containing the elements.
	 */
	public void setRow2(Vector4 value) { elements[2] = value.getX(); elements[6] = value.getY(); elements[10] = value.getZ(); elements[14] = value.getW(); }
	
	/**
	 * Sets the W components of all the columns.
	 * @param value The Vector4 containing the elements.
	 */
	public void setRow3(Vector4 value) { elements[3] = value.getX(); elements[7] = value.getY(); elements[11] = value.getZ(); elements[15] = value.getW(); }
	
	/**
	 * Sets the elements contained in the first column of the matrix.
	 * @param value The new column as a Vector4.
	 */
	public void setColumn0(Vector4 value) { elements[0] = value.getX(); elements[1] = value.getY(); elements[2] = value.getZ(); elements[3] = value.getW(); }
	
	/**
	 * Sets the elements contained in the second column of the matrix.
	 * @param value The new column as a Vector4.
	 */
	public void setColumn1(Vector4 value) { elements[4] = value.getX(); elements[5] = value.getY(); elements[6] = value.getZ(); elements[7] = value.getW(); }
	
	/**
	 * Sets the elements contained in the third column of the matrix.
	 * @param value The new column as a Vector4.
	 */
	public void setColumn2(Vector4 value) { elements[8] = value.getX(); elements[9] = value.getY(); elements[10] = value.getZ(); elements[11] = value.getW(); }
	
	/**
	 * Sets the elements contained in the fourth column of the matrix.
	 * @param value The new column as a Vector4.
	 */
	public void setColumn3(Vector4 value) { elements[12] = value.getX(); elements[13] = value.getY(); elements[14] = value.getZ(); elements[15] = value.getW(); }
}
