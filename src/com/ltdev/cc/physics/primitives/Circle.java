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
 * A Shape that represents a circle.
 * @author Lightning Development Studios
 */
public class Circle extends Shape
{
    private float radius;

    /**
     * Initializes a new instance of the Circle class.
     */
    public Circle()
    {
        this(true);
    }
    
    /**
     * Initializes a new instance of the Circle class.
     * @param solid The solidity of the circle
     */
    public Circle(boolean solid)
    {
        this(1, solid);
    }
    
    /**
     * Initializes a new instance of the Circle class.
     * @param size The size of the circle
     * @param solid The solidity of the circle
     */
    public Circle(float size, boolean solid)
    {
        this(size, new Vector2(0, 0), solid);
    }
    
    /**
     * Initializes a new instance of the Circle class.
     * @param size The size of the circle
     * @param position The position of the circle
     * @param solid The solidity of the circle
     */
    public Circle(float size, Vector2 position, boolean solid)
    {
        this(size, position, 0, solid);
    }
    
    /**
     * Initializes a new instance of the Shape class.
     * @param size The size of the circle
     * @param position The position of the circle
     * @param angle The angle of the circle in radians
     * @param solid The solidity of the circle
     */
    public Circle(float size, Vector2 position, float angle, boolean solid)
    {
        super(position, angle, solid);
        
        float halfSize = size / 2;
        radius = halfSize;
        float[] vertices = 
            {
                halfSize, halfSize,     //top left
                halfSize, -halfSize,    //bottom left
                -halfSize, halfSize,    //top right
                -halfSize, -halfSize    //bottom right
            };
        this.vertices = vertices;
        
        transformVertices();
        updateMass();
    }
    
    @Override
    protected void updateMass()
    {
        //mass = density * getRadius() * getRadius() * (float)Math.PI;
        mass = 1024;
    }
    
    @Override
    public Vector2[] getSATAxes(Shape s)
    {
        Vector2[] axes = new Vector2[1];
        Vector2[] sVerts = s.getWorldVertices();
        
        axes[0] = Vector2.subtract(sVerts[0], position);
        for (int i = 1; i < sVerts.length; i++)
        {
            Vector2 tempVec = Vector2.subtract(sVerts[i], position);
            if (axes[0].length() > tempVec.length())
            {
                axes[0] = tempVec;
            }
        }
        
        axes[0] = Vector2.normalize(axes[0]);
        return axes;
    }
    
    @Override
    public float[] project(Vector2 axis)
    {
        float[] bounds = new float[2];
        float posDot = Vector2.dot(position, axis);
        
        bounds[0] = posDot - radius;
        bounds[1] = posDot + radius;
        
        return bounds;
    }

    /**
     * Gets the circle's radius.
     * @return The circle's radius.
     */
    public float getRadius()
    {
        return vertices[0];
    }
}
