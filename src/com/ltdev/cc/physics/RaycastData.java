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

package com.ltdev.cc.physics;

import com.ltdev.cc.physics.primitives.Shape;

/**
 * Contains all the data associated with the result of a raycast.
 * @author Lightning Development Studios.
 */
public class RaycastData 
{
    private float distance;
    private Shape shape;
    
    /**
     * Initializes a new instance of the RaycastData class.
     * @param distance The distance the ray traveled.
     * @param s The shape the ray hit.
     */
    public RaycastData(float distance, Shape s)
    {
        this.distance = distance;
        shape = s;
    }
    
    /**
     * Gets the distance of the ray from the start to where it hit an object.
     * @return The distance of the ray.
     */
    public float getDistance() 
    {
        return distance;
    }
    
    /**
     * Gets the shape that the ray collided with.
     * @return The shape that the ray collided with.
     */
    public Shape getShape()
    {
        return shape;
    }
}
