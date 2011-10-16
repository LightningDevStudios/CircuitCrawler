package com.lds.UI;

import com.lds.Enums.UIPosition;
import com.lds.math.Vector2;

public class UITextBox extends UIEntity
{
	private String text;
	private float originalTopPad, originalLeftPad, originalBottomPad, originalRightPad;
	
	public UITextBox(float xSize, float ySize, UIPosition position)
	{
		super(xSize, ySize, position);
	}
	
	public UITextBox(float xSize, float ySize, float xRelative, float yRelative)
	{
		super(xSize, ySize, xRelative, yRelative);
	}
	
	public UITextBox(float xSize, float ySize, UIPosition position, float topPad, float leftPad, float bottomPad, float rightPad) 
	{
		super(xSize, ySize, position, topPad, leftPad, bottomPad, rightPad);
	}
	
	public UITextBox(float xSize, float ySize, float xRelative, float yRelative, float topPad, float leftPad, float bottomPad, float rightPad)
	{
		super(xSize, ySize, xRelative, yRelative, topPad, leftPad, bottomPad, rightPad);
	}
		
	@Override
	public void autoPadding(float topPad, float leftPad, float bottomPad, float rightPad)
	{
		this.originalTopPad = topPad;
		this.originalLeftPad = leftPad;
		this.originalBottomPad = bottomPad;
		this.originalRightPad = rightPad;
		
		super.autoPadding(topPad, leftPad, bottomPad, rightPad);
	}
	
	public void setText(String text)
	{
		if ((this.text == null || !this.text.equalsIgnoreCase(text)) && text != null && !text.equals(""))
		{
			this.text = text;
			tex.reloadTexture(text);
			size = new Vector2(tex.getXSize(), tex.getYSize());
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
			setBuffer(vertexBuffer, vertices);
			needToUpdateVertexVBO = true;
			
			autoPadding(originalTopPad, originalLeftPad, originalBottomPad, originalRightPad);
			this.updatePosition();
		}
	}
	
	public String getText()
	{
		return text;
	}
}
