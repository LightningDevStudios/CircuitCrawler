package com.lds.UI;

import com.lds.Enums.Direction;
import com.lds.Enums.RenderMode;
import com.lds.Enums.UIPosition;
import com.lds.math.Vector2;

public abstract class UIProgressBar extends UIEntity
{
	private int value, maximum, minimum, originalValue;
	private float[] originalColor;
	private float originalXSize, originalYSize;
	private Direction dir;
	private float originalTopPad, originalLeftPad, originalBottomPad, originalRightPad;
		
	public UIProgressBar(float xSize, float ySize, UIPosition position, Direction dir, int value, int minimum, int maximum)
	{
		this(xSize, ySize, UI_POSITION_F[position.getValue() * 2], UI_POSITION_F[(position.getValue() * 2) + 1], dir, 0, 0, 0, 0, value, minimum, maximum);
		this.position = position;
	}
	
	public UIProgressBar(float xSize, float ySize, float xRelative, float yRelative, Direction dir, int value, int minimum, int maximum)
	{
		this(xSize, ySize, xRelative, yRelative, dir, 0, 0, 0, 0, value, minimum, maximum);
	}
	
	public UIProgressBar(float xSize, float ySize, UIPosition position, Direction dir, float topPad, float leftPad, float bottomPad, float rightPad, int value, int minimum, int maximum) 
	{
		this(xSize, ySize, UI_POSITION_F[position.getValue() * 2], UI_POSITION_F[(position.getValue() * 2) + 1], dir, topPad, leftPad, bottomPad, rightPad, value, minimum, maximum);
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
	
	@Override
	public void update()
	{
		if (this.value != this.originalValue)
		{
			if (renderMode.contains(RenderMode.GRADIENT))
					updateGradientProgress();
			
			updateVertices();
			autoPadding(originalTopPad, originalLeftPad, originalBottomPad, originalRightPad);
			updatePosition();
			originalValue = value;
		}
	}
	
	private void updateGradientProgress()
	{		
		float[] curGradient = getGradientCoords();
				
		switch(dir)
		{
			case UP:
				for (int i = 0; i < 4; i++)
				{
					curGradient[i + 4] = (((float)(maximum - value) * originalColor[i]) + ((float)(value - minimum) * originalColor[i + 4])) / (float)(maximum - minimum); //weighted average of colors by current percentage
					curGradient[i + 12] = (((float)(maximum - value) * originalColor[i + 8]) + ((float)(value - minimum) * originalColor[i + 12])) / (float)(maximum - minimum);
				}
				break;
			case RIGHT:
				for (int i = 0; i < 4; i++)
				{
					curGradient[i] = (((float)(maximum - value) * originalColor[i + 8]) + ((float)(value - minimum) * originalColor[i])) / (float)(maximum - minimum);
					curGradient[i + 4] = (((float)(maximum - value) * originalColor[i + 12]) + ((float)(value - minimum) * originalColor[i + 4])) / (float)(maximum - minimum);
				}
				break;
			case DOWN:
				for (int i = 0; i < 4; i++)
				{
					curGradient[i + 4] = (((float)(maximum - value) * originalColor[i]) + ((float)(value - minimum) * originalColor[i + 4])) / (float)(maximum - minimum);
					curGradient[i + 12] = (((float)(maximum - value) * originalColor[i + 8]) + ((float)(value - minimum) * originalColor[i + 12])) / (float)(maximum - minimum);
				}
				break;
			case LEFT:
				for (int i = 0; i < 4; i++)
				{
					curGradient[i + 8] = (((float)(maximum - value) * originalColor[i]) + ((float)(value - minimum) * originalColor[i + 8])) / (float)(maximum - minimum);
					curGradient[i + 12] = (((float)(maximum - value) * originalColor[i + 4]) + ((float)(value - minimum) * originalColor[i + 12])) / (float)(maximum - minimum);
				}
				break;
			default:
		}
				
		updateGradient(curGradient);
	}
	
	private void updateVertices()
	{
		//different values must be used for different directions
		switch(dir)
		{
		case UP:
		case DOWN:
		    size = new Vector2(originalXSize, originalYSize * ((float)value - minimum) / (float)(maximum - minimum));
			break;
		case LEFT:
		case RIGHT:
			size = new Vector2(originalXSize * ((float)(value - minimum) / (float)(maximum - minimum)), originalYSize);
			break;
		default:	
		}
		
		halfSize = Vector2.scale(size, 0.5f);
		
		float x = halfSize.getX();
		float y = halfSize.getY();
		
		float[] initVerts = 
		{
		    x, y,
		    x, -y,
			-x, y,
			-x, -y
		};
		
		this.vertices = initVerts;
		this.vertexBuffer = setBuffer(vertexBuffer, initVerts);
		needToUpdateVertexVBO = true;
		
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
	
	public void setMaximum(int maximum)
	{
	    this.maximum = maximum;
	}
	
	public void setMinimum(int minimum)
	{
	    this.minimum = minimum;
	}
	
	public void setValue(int value)
	{
		if (value >= minimum && value <= maximum)
			this.value = value;
	}
	
	@Override
	public void enableGradientMode(float[] color)
	{
		this.originalColor = color;
		super.enableGradientMode(color);
		updateGradientProgress();
	}
	
	@Override 
	public void setSize(Vector2 size)
	{
	    this.originalXSize = size.getX();
	    this.originalYSize = size.getY();
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
