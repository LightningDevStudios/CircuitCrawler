package com.lds.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.lds.Enums.UIPosition;

public class UIProgressBar extends UIEntity
{
	public int value;
	public int maximum;
	public float[] originalColor;
	public float originalXSize, originalYSize;
		
	public UIProgressBar(float xSize, float ySize, UIPosition position, int value, int maximum)
	{
		super(xSize, ySize, position);
		this.value = value;
		this.maximum = maximum;
		this.originalXSize = xSize;
		this.originalYSize = ySize;
	}
	
	public UIProgressBar(float xSize, float ySize, float xRelative, float yRelative, int value, int maximum)
	{
		super (xSize, ySize, xRelative, yRelative);
		this.value = value;
		this.maximum = maximum;
		this.originalXSize = xSize;
		this.originalYSize = ySize;
	}
	
	public UIProgressBar(float xSize, float ySize, UIPosition position, float topPad, float leftPad, float bottomPad, float rightPad, int value, int maximum) 
	{
		super(xSize, ySize, position, topPad, leftPad, bottomPad, rightPad);
		this.value = value;
		this.maximum = maximum;
		this.originalXSize = xSize;
		this.originalYSize = ySize;
	}
	
	public UIProgressBar(float xSize, float ySize, float xRelative, float yRelative, float topPad, float leftPad, float bottomPad, float rightPad, int value, int maximum)
	{
		super(xSize, ySize, xRelative, yRelative, topPad, leftPad, bottomPad, rightPad);
		this.value = value;
		this.maximum = maximum;
		this.originalXSize = xSize;
		this.originalYSize = ySize;
	}
	
	@Override
	public void setGradient(float[] color)
	{
		this.originalColor = color;
		updateGradient();
	}
	
	//TODO somewhat hacky, should not depend on a required 16 floats in the color array, and should be able to average any direction
	public void updateGradient()
	{
		float[] topRight = {originalColor[0], originalColor[1], originalColor[2], originalColor[3]};
		float[] bottomRight = {originalColor[4], originalColor[5], originalColor[6], originalColor[7]};
		float[] topLeft = {originalColor[8], originalColor[9], originalColor[10], originalColor[11]};
		float[] bottomLeft = {originalColor[12], originalColor[13], originalColor[14], originalColor[15]};
		
		for(int i = 0; i < 4; i++)
		{
			topRight[i] = (((float)(maximum - value) * topLeft[i]) + ((float)value * topRight[i])) / (float)maximum; //weighted averages by current percentage
			bottomRight[i] = (((float)(maximum - value) * bottomLeft[i]) + ((float)value * bottomRight[i])) / (float)maximum;
		}
		
		float[] initColor = { 	topRight[0], topRight[1], topRight[2], topRight[3],
								bottomRight[0], bottomRight[1], bottomRight[2], bottomRight[3],
								topLeft[0], topLeft[1], topLeft[2], topLeft[3],
								bottomLeft[0], bottomLeft[1], bottomLeft[2], bottomLeft[3] };
		
		this.color = initColor;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(color.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		colorBuffer = byteBuf.asFloatBuffer();
		colorBuffer.put(color);
		colorBuffer.position(0);
	}
	
	public void updateVertices()
	{
		xSize = originalXSize * ((float)value / maximum);
		ySize = originalYSize;
				
		halfXSize = xSize / 2;
		halfYSize = ySize / 2;
		
		float[] initVerts = { 	halfXSize, halfYSize,
								halfXSize, -halfYSize,
								-halfXSize, halfYSize,
								-halfXSize, -halfYSize };
		
		vertices = initVerts;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}
}
