/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev.math;

/**
 * A 4-component vector.
 * @author Lightning Development Studios
 */
public final class Vector4 
{
    /**
     * A unit vector in the X direction.
     */
    public static final Vector4 UNIT_X = new Vector4(1, 0, 0, 0);
    
    /**
     * A unit vector in the Y direction.
     */
    public static final Vector4 UNIT_Y = new Vector4(0, 1, 0, 0);
    
    /**
     * A unit vector in the Z direction.
     */
    public static final Vector4 UNIT_Z = new Vector4(0, 0, 1, 0);
    
    /**
     * A unit vector in the W direction.
     */
    public static final Vector4 UNIT_W = new Vector4(0, 0, 0, 1);

    /**
     * A vector of all components equal to 1.
     */
    public static final Vector4 ONE = new Vector4(1, 1, 1, 1);

    /**
     * A vector of all components equal to 0.
     */
    public static final Vector4 ZERO = new Vector4(0, 0, 0, 0);

	/**
	 * The vector's X component.
	 */
	private final float x;
	
	/**
	 * The vector's Y component.
	 */
	private final float y;
	
	/**
	 * The vector's Z component.
	 */
	private final float z;
	
	/**
	 * The vector's W component.
	 */
	private final float w;

	/**
	 * Initializes a new instance of the Vector4 class with all components equal to 0.
	 */
	public Vector4()
	{
		x = 0;
		y = 0;
		z = 0;
		w = 0;
	}
	
	/**
	 * Initializes a new instance of the Vector4 class.
	 * @param x The X component of the vector.
	 * @param y The Y component of the vector.
	 * @param z The Z component of the vector.
	 * @param w The W component of the vector.
	 */
	public Vector4(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/**
     * Initializes a new instance of the Vector4 class.
     * @param xy A Vector2 containing the X and Y components of the vector.
     * @param z The Z component of the vector.
     * @param w The W component of the vector.
     */
    public Vector4(Vector2 xy, float z, float w)
    {
        this.x = xy.x();
        this.y = xy.y();
        this.z = z;
        this.w = w;
    }
	
	/**
	 * Initializes a new instance of the Vector4 class.
	 * @param xyz A Vector3 containing the X, Y, and Z components of the vector.
	 * @param w The W component of the vector.
	 */
	public Vector4(Vector3 xyz, float w)
	{
		this.x = xyz.x();
		this.y = xyz.y();
		this.z = xyz.z();
		this.w = w;
	}

	/**
     * Formats the vector for text output.
     * @return A formatted string.
     */
    @Override
    public String toString()
    {
        return "<" + x + ", " + y + ", " + z + ", " + w + ">";
    }

	/**
	 * Transforms a vector by a matrix.
	 * @param v The vector to be transformed.
	 * @param mat The matrix to transform the vector by.
	 * @return A transformed vector.
	 */
	public static Vector4 transform(Vector4 v, Matrix4 mat)
	{		
		float[] elements = mat.array();
		
		float m11 = elements[0],	m12 = elements[1],	m13 = elements[2],	m14 = elements[3],
			  m21 = elements[4],	m22 = elements[5],	m23 = elements[6],	m24 = elements[7],
			  m31 = elements[8],	m32 = elements[9],	m33 = elements[10],	m34 = elements[11],
			  m41 = elements[12],	m42 = elements[13],	m43 = elements[14],	m44 = elements[15];
		
		float vX = v.x;
		float vY = v.y;
		float vZ = v.z;
		float vW = v.w;
		
		return new Vector4(vX * m11 + vY * m21 + vZ * m31 + vW * m41,
		                   vX * m12 + vY * m22 + vZ * m32 + vW * m42,
		                   vX * m13 + vY * m23 + vZ * m33 + vW * m43,
		                   vX * m14 + vY * m24 + vZ * m34 + vW * m44);
	}
	
	/**
	 * Turns all components of the vector into their absolute value equivalent.
	 * @param v The vector to process.
	 * @return A vector with all positive components.
	 */
	public static Vector4 abs(Vector4 v)
	{
	    return new Vector4(Math.abs(v.x), Math.abs(v.y), Math.abs(v.z), Math.abs(v.w));
	}
	
	/**
	 * Scales a vector by a scalar.
	 * @param v The vector to scale.
	 * @param scale The scalar to scale by.
	 * @return A scaled vector.
	 */
	public static Vector4 scale(Vector4 v, float scale)
	{
	    return new Vector4(v.x * scale, v.y * scale, v.z * scale, v.w * scale);
	}
	
	/**
	 * Subtacts one vector from another.
	 * @param left The vector to subtract from.
     * @param right The vector to subtract by.
     * @return The resultant vector of the subtraction.
	 */
	public static Vector4 subtract(Vector4 left, Vector4 right)
	{
	    return new Vector4(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
	}
	
	/**
     * Adds two vectors together.
     * @param left The vector to be added to.
     * @param right The vector to be added by.
     * @return The resultant vector of the addition.
     */
	public static Vector4 add(Vector4 left, Vector4 right)
	{
	    return new Vector4(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
	}
	
	/**
	 * Gets the vector <x, y, z>.
	 * @return A Vector3 containing the x, y, and z components of this vector.
	 * @see Vector3
	 */
	public Vector3 xyz() 
	{ 
		return new Vector3(x, y, z); 
	}
	
	/**
	 * Gets the vector <x, y>.
	 * @return A Vector2 containing the x and y components of this vector.
	 * @see Vector2
	 */
	public Vector2 xy()
	{
		return new Vector2(x, y);
	}
	
	/**
	 * Gets the X component of the vector.
	 * @return The vector's X component.
	 */
	public float x()
	{
	    return x;
	}
	
	/**
	 * Gets the Y component of the vector.
	 * @return The vector's Y component.
	 */
	public float y()
	{
	    return y;
	}
	
	/**
	 * Gets the Z component of the vector.
	 * @return The vector's Z component.
	 */
	public float z()
	{
	    return z;
	}
	
	/**
	 * Gets the W component of the vector.
	 * @return The vector's W component.
	 */
	public float w()
	{
	    return w;
	}
	
	/**
	 * Converts the vector to a float array.
	 * @return A float array containing the vector components.
	 */
	public float[] array()
	{
		return new float[]
		{
		    x, y, z, w 
		};
	}
}
