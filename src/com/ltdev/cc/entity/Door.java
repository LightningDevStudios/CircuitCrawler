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

package com.ltdev.cc.entity;

import com.ltdev.Direction;
import com.ltdev.Stopwatch;
import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.graphics.TextureManager;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

/**
 * A Door can be opened and closed via triggers as part of puzzles.
 * @author Lightning Development Studios
 */
public class Door extends Entity
{
    private boolean open;
    private Vector2 targetPos;
    private Vector2 openedPosition, closedPosition;
    
    /**
     * Initializes a new instance of the Door class.
     * @param position The door's position.
     */
	public Door(Vector2 position, Direction dir)
	{
	    this(72, position, dir);
	}
	
	/**
	 * Initializes a new instance of the Door class.
	 * @param size The door's size.
	 * @param position The door's position.
	 */
	public Door(float size, Vector2 position, Direction dir)
    {
        super(new Rectangle(new Vector2(size - 10, size), position, 0, true));
        shape.setStatic(true);
        colorInterpSpeed = 1.0f;
        
        open = false;
        targetPos = Vector2.ZERO;
        openedPosition = position;
        
        switch (dir)
        {
            case LEFT:
                closedPosition = new Vector2(openedPosition.x() - size, openedPosition.y());
            case RIGHT:
                closedPosition = new Vector2(openedPosition.x() + size, openedPosition.y());
            case DOWN:
                closedPosition = new Vector2(openedPosition.x(), openedPosition.y() - size);
            case UP:
                closedPosition = new Vector2(openedPosition.x(), openedPosition.y() + size);
        }
        
        tex = TextureManager.getTexture("tilesetentities");
        this.tilesetX = 2;
        this.tilesetY = 1;
    }

	@Override
	public void update(GL11 gl)
	{
	    super.update(gl);
	    
	    float frameTime = (float)Stopwatch.getFrameTime() / 1000f;
	    if (open)
	        targetPos = Vector2.add(shape.getPos(), Vector2.scale(Vector2.subtract(closedPosition, shape.getPos()), frameTime));
	    else
            targetPos = Vector2.add(shape.getPos(), Vector2.scale(Vector2.subtract(openedPosition, shape.getPos()), frameTime));
	    shape.setPos(targetPos);
	}
	
	/**
	 * Opens the door.
	 */
	public void open()
	{
	    if (!open)
	    {
	        open = true;
	    }
	}
	
	/**
	 * Closes the door.
	 */
	public void close()
	{	
	    if (open)
        {
	        open = false;
        }
	}
}
