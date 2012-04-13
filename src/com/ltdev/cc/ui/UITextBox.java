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

package com.ltdev.cc.ui;

import com.ltdev.math.Vector2;

public class UITextBox extends Control
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
			size = new Vector2(tex.getXSize(), tex.getYSize());
			halfSize = Vector2.scale(size, 0.5f);
			
			float x = halfSize.x();
			float y = halfSize.y();
			
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
