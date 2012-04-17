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

import com.ltdev.EntityManager;
import com.ltdev.Stopwatch;
import com.ltdev.cc.physics.primitives.*;
import com.ltdev.graphics.TextureManager;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

/**
 * A Laser is an object that reflects off mirrors and pushes everything else aside.
 * @author Lightning Development Studios
 */
public class Laser extends Entity
{
    private int countdown;
    
    /**
     * Initializes a new instance of the Laser class.
     * @param beamWidth The laser beam's width.
     * @param beamLength The laser beam's length.
     * @param angle The laser beam's angle.
     * @param position The geometric center of the laser beam.
     */
    public Laser(float beamWidth, float beamLength, float angle, Vector2 position)
    {
        super(new Rectangle(new Vector2(beamLength, beamWidth), position, (float)Math.toDegrees(angle), false));
        this.tex = TextureManager.getTexture("tilesetentities");
        this.tilesetX = 1;
        this.tilesetY = 3;
        countdown = 100;
    }
    
    @Override
    public void interact(Entity ent)
    {
        super.interact(ent);
        
        /*if (ent instanceof Player)
        {
            //Player.kill();
        }*/
    }
    
    @Override
    public void update(GL11 gl)
    {
        countdown -= Stopwatch.getFrameTime();
        
        if (countdown < 0)
            EntityManager.removeEntity(this);
    }
}
