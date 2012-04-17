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
import com.ltdev.EntityManager;
import com.ltdev.Stopwatch;
import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.math.Vector2;
import com.ltdev.math.Vector4;

import javax.microedition.khronos.opengles.GL11;

/**
 * A Door can be opened and closed via triggers as part of puzzles.
 * @author Lightning Development Studios
 */
public class BreakableDoor extends Entity
{
    private boolean open;
    private Vector2 targetPos;
    private Vector2 openedPosition, closedPosition;
    private int hitCount, hitsLeft;
    /**
     * Initializes a new instance of the Door class.
     * @param position The door's position.
     */
    public BreakableDoor(Vector2 position, int maxHits)
    {
        this(72, position, maxHits);
    }
    
    /**
     * Initializes a new instance of the Door class.
     * @param size The door's size.
     * @param position The door's position.
     */
    public BreakableDoor(float size, Vector2 position, int maxHits)
    {
        super(new Rectangle(new Vector2(size - 10, size), position, 0, true));
        shape.setStatic(true);
        colorInterpSpeed = 1.0f;
        hitCount = hitsLeft = maxHits;
        
        this.tilesetX = 2;
        this.tilesetY = 1;
    }

    @Override
    public void update(GL11 gl)
    {
        super.update(gl);
    }
    
    /**
     * Opens the door.
     */
    public void damageDoor()
    {
        hitsLeft--;
            
        if(hitsLeft < 1)
        {
            EntityManager.removeEntity(this);
        }
        else
        {
            float percent = 1 / (float)hitCount;
            Vector4 vec = new Vector4(percent, percent, percent, 1);
            Vector4.subtract(this.colorVec, vec);
        }
    }
}
