package com.lds.math;

public class Vector4 
{
	/**
	 * Private Variables
	 */
	private float x;
	private float y;
	private float z;
	private float w;
	
	/**
	 * Constructor
	 */
	
	public Vector4(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	/***********************
	 * Getters and Setters *
	 ***********************/
	
	public float getX() { return x; } public void setX(float x) { this.x = x; }
	public float getY() { return y; } public void setY(float y) { this.y = y; }
	public float getZ() { return z; } public void setZ(float z) { this.z = z; }
	public float getW() { return w; } public void setW(float w) { this.w = w; }
}
