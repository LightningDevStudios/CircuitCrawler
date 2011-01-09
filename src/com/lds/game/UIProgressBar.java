package com.lds.game;

import com.lds.Enums.UIPosition;
import com.lds.Enums.Direction;

public abstract class UIProgressBar extends UIEntity
{
	private int value, maximum, minimum, originalValue;
	private float[] originalColor;
	private float originalXSize, originalYSize;
	private Direction dir;
	private float originalTopPad, originalLeftPad, originalBottomPad, originalRightPad;
		
	public UIProgressBar(float xSize, float ySize, UIPosition position, Direction dir, int value, int minimum, int maximum)
	{
		this(xSize, ySize, UIPositionF[position.getValue() * 2], UIPositionF[(position.getValue() * 2) + 1], dir, 0, 0, 0, 0, value, minimum, maximum);
		this.position = position;
	}
	
	public UIProgressBar(float xSize, float ySize, float xRelative, float yRelative, Direction dir, int value, int minimum, int maximum)
	{
		this(xSize, ySize, xRelative, yRelative, dir, 0, 0, 0, 0, value, minimum, maximum);
	}
	
	public UIProgressBar(float xSize, float ySize, UIPosition position, Direction dir, float topPad, float leftPad, float bottomPad, float rightPad, int value, int minimum, int maximum) 
	{
		this(xSize, ySize, UIPositionF[position.getValue() * 2], UIPositionF[(position.getValue() * 2) + 1], dir, topPad, leftPad, bottomPad, rightPad, value, minimum, maximum);
		this.position = position;
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
	
	//If there's any change to value, 
	@Override
	public void update()
	{
		if (this.value != this.originalValue)
		{
			updateGradient();
			updateVertices();
			autoPadding(originalTopPad, originalLeftPad, originalBottomPad, originalRightPad);
			updatePosition();
			originalValue = value;
		}
	}
	
	private void updateGradient()
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
		
		updateGradient(initColor);
	}
	
	private void updateVertices()
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
		
		setVertices(initVerts);
	}
	
	
	
	public int getValue()				{ return value;	}
	public int getMaximum() 			{ return maximum; }
	public int getMinimum() 			{ return minimum; }
	
	public void setMaximum(int maximum) { this.maximum = maximum; }
	public void setMinimum(int minimum) { this.minimum = minimum; }
	
	public void setValue(int value)
	{
		if (value >= minimum && value <= maximum)
			this.value = value;
	}
	
	@Override
	public void setGradientMode(float[] color)
	{
		this.originalColor = color;
		updateGradient();
		super.setGradientMode(color);
	}
	
	@Override
	public void setXSize(float xSize)
	{
		this.originalXSize = xSize;
		updateVertices();
	}
	
	@Override
	public void setYSize(float ySize)
	{
		this.originalYSize = ySize;
		updateVertices();
	}
	
	@Override
	public void setTopPad(float topPad)
	{
		this.originalTopPad = topPad;
		super.setTopPad(topPad);
	}
	
	@Override
	public void setLeftPad(float leftPad)
	{
		this.originalLeftPad = leftPad;
		super.setLeftPad(leftPad);
	}
	
	@Override
	public void setBottomPad(float bottomPad)
	{
		this.originalBottomPad = bottomPad;
		super.setBottomPad(bottomPad);
	}
	
	@Override
	public void setRightPad(float rightPad)
	{
		this.originalRightPad = rightPad;
		super.setRightPad(rightPad);
	}
}
