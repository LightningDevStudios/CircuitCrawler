package com.lds.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.lds.Enums.Direction;
import com.lds.Enums.UIPosition;

public abstract class UIProgressBar extends UIEntity
{
	protected int value, maximum, minimum;
	protected float[] originalColor;
	protected float originalXSize, originalYSize;
	protected Direction dir;
	public float originalTopPad, originalLeftPad, originalBottomPad, originalRightPad;
	public boolean tempBool;
		
	public UIProgressBar(float xSize, float ySize, UIPosition position, Direction dir, int value, int minimum, int maximum)
	{
		super(xSize, ySize, position);
		this.dir = dir;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.originalXSize = xSize;
		this.originalYSize = ySize;
		
		this.originalTopPad = 0.0f;
		this.originalLeftPad = 0.0f;
		this.originalBottomPad = 0.0f;
		this.originalRightPad = 0.0f;
	}
	
	public UIProgressBar(float xSize, float ySize, float xRelative, float yRelative, Direction dir, int value, int minimum, int maximum)
	{
		super (xSize, ySize, xRelative, yRelative);
		this.dir = dir;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.originalXSize = xSize;
		this.originalYSize = ySize;
		
		this.originalTopPad = 0.0f;
		this.originalLeftPad = 0.0f;
		this.originalBottomPad = 0.0f;
		this.originalRightPad = 0.0f;
	}
	
	public UIProgressBar(float xSize, float ySize, UIPosition position, Direction dir, float topPad, float leftPad, float bottomPad, float rightPad, int value, int minimum, int maximum) 
	{
		super(xSize, ySize, position, topPad, leftPad, bottomPad, rightPad);
		this.dir = dir;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.originalXSize = xSize;
		this.originalYSize = ySize;
		
		this.originalTopPad = topPad;
		this.originalLeftPad = leftPad;
		this.originalBottomPad = bottomPad;
		this.originalRightPad = rightPad;
	}
	
	public UIProgressBar(float xSize, float ySize, float xRelative, float yRelative, Direction dir, float topPad, float leftPad, float bottomPad, float rightPad, int value, int minimum, int maximum)
	{
		super(xSize, ySize, xRelative, yRelative, topPad, leftPad, bottomPad, rightPad);
		this.dir = dir;
		this.value = value;
		this.minimum = minimum;
		this.maximum = maximum;
		this.originalXSize = xSize;
		this.originalYSize = ySize;
		
		this.originalTopPad = topPad;
		this.originalLeftPad = leftPad;
		this.originalBottomPad = bottomPad;
		this.originalRightPad = rightPad;
	}
	
	@Override
	public void setGradient(float[] color)
	{
		this.originalColor = color;
		updateGradient();
	}
	
	public void updateGradient()
	{
		//split the color array to colors for each vertex
		float[] topRight = {originalColor[0], originalColor[1], originalColor[2], originalColor[3]};
		float[] bottomRight = {originalColor[4], originalColor[5], originalColor[6], originalColor[7]};
		float[] topLeft = {originalColor[8], originalColor[9], originalColor[10], originalColor[11]};
		float[] bottomLeft = {originalColor[12], originalColor[13], originalColor[14], originalColor[15]};
		
		//values for the weighted average change based on direction, we need a switch case for this.
		switch(dir)
		{
			case UP:
				for(int i = 0; i < 4; i++)
				{
					bottomRight[i] = (((float)(maximum - value) * topRight[i]) + ((float)(value - minimum) * bottomRight[i])) / (float)(maximum - minimum); //weighted average of colors by current percentage
					bottomLeft[i] = (((float)(maximum - value) * topLeft[i]) + ((float)(value - minimum) * bottomLeft[i])) / (float)(maximum - minimum);
				}
				break;
			case RIGHT:
				for(int i = 0; i < 4; i++)
				{
					topRight[i] = (((float)(maximum - value) * topLeft[i]) + ((float)(value - minimum) * topRight[i])) / (float)(maximum - minimum);
					bottomRight[i] = (((float)(maximum - value) * bottomLeft[i]) + ((float)(value - minimum) * bottomRight[i])) / (float)(maximum - minimum);
				}
				break;
			case DOWN:
				for(int i = 0; i < 4; i++)
				{
					bottomRight[i] = (((float)(maximum - value) * topRight[i]) + ((float)(value - minimum) * bottomRight[i])) / (float)(maximum - minimum);
					bottomLeft[i] = (((float)(maximum - value) * topLeft[i]) + ((float)(value - minimum) * bottomLeft[i])) / (float)(maximum - minimum);
				}
				break;
			case LEFT:
				for(int i = 0; i < 4; i++)
				{
					topLeft[i] = (((float)(maximum - value) * topRight[i]) + ((float)(value - minimum) * topLeft[i])) / (float)(maximum - minimum);
					bottomLeft[i] = (((float)(maximum - value) * bottomRight[i]) + ((float)(value - minimum) * bottomLeft[i])) / (float)(maximum - minimum);
				}
				break;
			default:
		}
		
		//recreate the color array and send it back
		float[] initColor = { 	topRight[0], topRight[1], topRight[2], topRight[3],
								bottomRight[0], bottomRight[1], bottomRight[2], bottomRight[3],
								topLeft[0], topLeft[1], topLeft[2], topLeft[3],
								bottomLeft[0], bottomLeft[1], bottomLeft[2], bottomLeft[3] };
		
		this.color = initColor;
		
		//re-allocate the color buffer
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(color.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		colorBuffer = byteBuf.asFloatBuffer();
		colorBuffer.put(color);
		colorBuffer.position(0);
	}
	
	public void updateVertices()
	{
		//different values must be used for different directions
		switch(dir)
		{
		case UP:
		case DOWN:
			xSize = originalXSize;
			ySize = originalYSize * ((float)(value - minimum) / (float)(maximum - minimum));
			break;
		case LEFT:
		case RIGHT:
			xSize = originalXSize * ((float)(value - minimum) / (float)(maximum - minimum));
			ySize = originalYSize;
			break;
		default:	
		}
		
				
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
	
	public void setValue(int value)
	{
		if (value >= minimum && value <= maximum)
			this.value = value;
	}
	
	public void setMaximum(int maximum)
	{
		this.maximum = maximum;
	}
	
	public void setMinimum(int minimum)
	{
		this.minimum = minimum;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public int getMaximum()
	{
		return maximum;
	}
	
	public int getMinimum()
	{
		return minimum;
	}
}
