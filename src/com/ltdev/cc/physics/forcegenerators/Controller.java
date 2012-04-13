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

package com.ltdev.cc.physics.forcegenerators;

import com.ltdev.cc.physics.primitives.Shape;
import com.ltdev.math.Vector2;

public class Controller implements IndivForce
{
    private float vertForce;
    private float horizForce;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    public Controller(float vertForce, float horizForce)
    {
        this.vertForce = vertForce;
        this.horizForce = horizForce;
        up = false;
        down = false;
        left = false;
        right = false;
    }

    public void updateForce(float frameTime, Shape s)
    {
        if (up)
            s.addImpulse(Vector2.scale(new Vector2(0, vertForce), s.getMass() * frameTime));
        if (down)
            s.addImpulse(Vector2.scale(new Vector2(0, -vertForce), s.getMass() * frameTime));        
        if (left)
            s.addImpulse(Vector2.scale(new Vector2(-horizForce, 0), s.getMass() * frameTime));        
        if (right)
              s.addImpulse(Vector2.scale(new Vector2(horizForce, 0), s.getMass() * frameTime));
    }
}
