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

public class GravityPoint extends GlobalForce
{
    private Vector2 point;
    private float g;

    public GravityPoint(Vector2 point, float g)
    {
        this.point = point;
        this.g = g;
    }

    @Override
    public void updateForce(float frameTime)
    {
        for (Shape s : shapes)
        {
            Vector2 v = Vector2.subtract(point, s.getPos());
            float vmag = v.length() + 10;
            float mag = frameTime * g * s.getMass() / vmag / vmag;
            Vector2 i = Vector2.scaleTo(v, mag);
            s.addImpulse(i);
        }
    }
}
