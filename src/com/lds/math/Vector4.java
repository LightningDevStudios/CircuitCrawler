package com.lds.math;

/**
 * A 4-component vector.
 * @author Lightning Development Studios
 *
 */
public class Vector4 
{
	/***********
	 * Members *
	 ***********/
	
	/**
	 * The vector's X component.
	 */
	private float x;
	
	/**
	 * The vector's Y component.
	 */
	private float y;
	
	/**
	 * The vector's Z component.
	 */
	private float z;
	
	/**
	 * The vector's W component.
	 */
	private float w;
	
	/****************
	 * Constructors *
	 ****************/
	
	/**
	 * Creates a new instance of the Vector4 class with all components equal to 0.
	 */
	public Vector4()
	{
		x = 0;
		y = 0;
		z = 0;
		w = 0;
	}
	
	/**
	 * Creates a new instance of the Vector4 class.
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
	 * Creates a new instance of the Vector4 class.
	 * @param xyz A Vector3 containing the X, Y, and Z components of the vector.
	 * @param w The W component of the vector.
	 */
	public Vector4(Vector3 xyz, float w)
	{
		this.x = xyz.getX();
		this.y = xyz.getY();
		this.z = xyz.getZ();
		this.w = w;
	}
	
	/**
	 * Creates a new instance of the Vector4 class.
	 * @param xy A Vector2 containing the X and Y components of the vector.
	 * @param z The Z component of the vector.
	 * @param w The W component of the vector.
	 */
	public Vector4(Vector2 xy, float z, float w)
	{
		this.x = xy.getX();
		this.y = xy.getY();
		this.z = z;
		this.w = w;
	}
	
	/********************
	 * Instance Methods *
	 ********************/
	
	/**
	 * Creates a shallow copy of a vector.
	 * @param v The vector to be copied.
	 * @return A shallow copy of the vector.
	 */
	public Vector4 copy(Vector4 v)
	{
		return new Vector4(v.getX(), v.getY(), v.getZ(), v.getW());
	}
	
	/******************
	 * Static Methods *
	 ******************/
	
	/**
	 * Transforms a vector by a matrix.
	 * @param v The vector to be transformed.
	 * @param mat The matrix to transform the vector by.
	 * @return A transformed vector.
	 */
	public static Vector4 transform(Vector4 v, Matrix4 mat)
	{
		Vector4 result = new Vector4();
		
		float[] elements = mat.array();
		
		float m11 = elements[0],	m12 = elements[1],	m13 = elements[2],	m14 = elements[3],
			  m21 = elements[4],	m22 = elements[5],	m23 = elements[6],	m24 = elements[7],
			  m31 = elements[8],	m32 = elements[9],	m33 = elements[10],	m34 = elements[11],
			  m41 = elements[12],	m42 = elements[13],	m43 = elements[14],	m44 = elements[15];
		
		float vX = v.getX();
		float vY = v.getY();
		float vZ = v.getZ();
		float vW = v.getW();
		
		result.setX(vX * m11 + vY * m21 + vZ * m31 + vW * m41);
		result.setY(vX * m12 + vY * m22 + vZ * m32 + vW * m42);
		result.setZ(vX * m13 + vY * m23 + vZ * m33 + vW * m43);
		result.setW(vX * m14 + vY * m24 + vZ * m34 + vW * m44);
		
		return result;
	}
	
	/***********
	 * Swizzle *
	 ***********/
	
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
	
	/**************************
	 * Accessors and Mutators *
	 **************************/
	
	/**
	 * Gets the X component of the vector.
	 * @return The vector's X component.
	 */
	public float getX()
	{
	    return x;
	}
	
	/**
	 * Gets the Y component of the vector.
	 * @return The vector's Y component.
	 */
	public float getY()
	{
	    return y;
	}
	
	/**
	 * Gets the Z component of the vector.
	 * @return The vector's Z component.
	 */
	public float getZ()
	{
	    return z;
	}
	
	/**
	 * Gets the W component of the vector.
	 * @return The vector's W component.
	 */
	public float getW()
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
	
	/**
	 * Manually sets the X component of the vector.
	 * @param x A new X component.
	 */
	public void setX(float x)
	{
	    this.x = x;
	}
	
	/**
	 * Manually sets the Y component of the vector.
	 * @param y A new Y component.
	 */
	public void setY(float y)
	{
	    this.y = y;
	}
	
	/**
	 * Manually sets the Z component of the vector.
	 * @param z A new Z component.
	 */
	public void setZ(float z)
	{
	    this.z = z;
	}
	
	/**
	 * Manually sets the W component of the vector.
	 * @param w A new W component.
	 */
	public void setW(float w)
	{
	    this.w = w;
	}
	
	/*************
     * Constants *
     *************/
    
    /**
     * A unit vector in the X direction.
     * @return A vector.
     */
    public static Vector4 unitX()
    {
        return new Vector4(1, 0, 0, 0);
    }
    
    /**
     * A unit vector in the Y direction.
     * @return A vector.
     */
    public static Vector4 unitY()
    {
        return new Vector4(0, 1, 0, 0);
    }
    
    /**
     * A unit vector in the Z direction.
     * @return A vector.
     */
    public static Vector4 unitZ()
    {
        return new Vector4(0, 0, 1, 0);
    }
    
    /**
     * A unit vector in the W direction.
     * @return A vector.
     */
    public static Vector4 unitW()
    {
        return new Vector4(0, 0, 0, 1);
    }
    
    /**
     * A vector of all components equal to 1.
     * @return A vector.
     */
    public static Vector4 one()
    {
        return new Vector4(1, 1, 1, 1);
    }
    
    /**
     * A vector of all components equal to 0.
     * @return A vector.
     */
    public static Vector4 zero()
    {
        return new Vector4(0, 0, 0, 0);
    }
}
