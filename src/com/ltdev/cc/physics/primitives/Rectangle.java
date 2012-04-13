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

package com.ltdev.cc.physics.primitives;

import com.ltdev.math.Vector2;

/**
 * A Shape that represents a rectangle.
 * @author Lightning Development Studios
 */
public class Rectangle extends Shape 
{
    /**
     * Initializes a new instance of the Rectangle class.
     * @param size The size of the rectangle
     * @param solid The solidity of the rectangle
     */
    public Rectangle(Vector2 size, boolean solid)
    {
        this(size, new Vector2(0, 0), solid);
    }
    
    /**
     * Initializes a new instance of the Rectangle class.
     * @param size The size of the rectangle
     * @param position The position of the rectangle
     * @param solid The solidity of the rectangle
     */
    public Rectangle(Vector2 size, Vector2 position, boolean solid)
    {
        this(size, position, 0, solid);
    }
    
    /**
     * Initializes a new instance of the Rectangle class.
     * @param size The size of the rectangle
     * @param position The position of the rectangle
     * @param angle The angle of the rectangle in radians
     * @param solid The solidity of the rectangle
     */
    public Rectangle(Vector2 size, Vector2 position, float angle, boolean solid)
    {
        super(position, angle, solid);
        
        float x = size.x() / 2;
        float y = size.y() / 2;

        float[] vertices = 
        {
            x,  y,
            x, -y,
            -x, y,
            -x, -y
        };

        this.vertices = vertices;
        transformVertices();
        updateMass();
    }
    
    @Override
    protected void updateMass()
    {
        //float width = vertices[0] * 2;
        //float height = vertices[1] * 2;
        //mass = density * width * height;
        mass = 1024;
    }
    
    @Override
    public Vector2[] getSATAxes(Shape s)
    {
       Vector2[] axes = new Vector2[2];
       
       axes[0] = Vector2.normalize(Vector2.subtract(worldVertices[0], worldVertices[1]));
       axes[1] = Vector2.perpendicularLeft(axes[0]);
       
       return axes;
    }
    
    @Override
    public float[] project(Vector2 axis)
    {
        float[] bounds = new float[2];
        
        bounds[0] = Vector2.dot(worldVertices[0], axis);
        bounds[1] = bounds[0];
        for (Vector2 vert : worldVertices)
        {
            float dot = Vector2.dot(vert, axis);
            if (dot > bounds[1])
                bounds[1] = dot;
            if (dot < bounds[0])
                bounds[0] = dot;
        }
        
        return bounds;
    }
}
