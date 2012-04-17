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
import com.ltdev.math.Vector2;

/**
 * Contains two shapes that are colliding and information about the collision.
 * @author Lightning Development Studios
 */
public class Contact
{
    private Shape a;
    private Shape b;
    
    private float penetration;
    private float restitution;

    private Vector2 contactNormal;
    
    /**
     * Initializes a new instance of the Contact class.
     * @param a The first shape.
     * @param b The second shape.
     */
    public Contact(Shape a, Shape b)
    {
        this.a = a;
        this.b = b;
        restitution = 0.5f;
    }

    /**
     * Initializes a new instance of the Contact class.
     * @param a The first shape.
     * @param b The second shape.
     * @param penetration The distance of penetration.
     * @param contactNormal The contact normal.
     */
    public Contact(Shape a, Shape b, float penetration, Vector2 contactNormal)
    {
        this(a, b);
        
        this.penetration = penetration;
        this.contactNormal = contactNormal;
    }

    /**
     * Resolves the collision and iteracts the two shapes with one another.
     * @param duration The duration.
     */
    public void resolve(float duration)
    {
        resolveInterpenetration();
        resolveVelocity();
        
        if (a.getInteractListener() != null && b.getInteractListener() != null)
        {
            a.getInteractListener().interact(b.getInteractListener().getEntity());
            b.getInteractListener().interact(a.getInteractListener().getEntity());
        }
    }

    /**
     * Resolves the collision by moving the objects so they no longer collide.
     */
    private void resolveInterpenetration()
    {
        if (penetration <= 0 || a.isStatic() && b.isStatic())
            return;
        
        if (!a.isSolid() || !b.isSolid())
            return;
        
        Vector2 moveVec = Vector2.scale(contactNormal, penetration);

        if (!a.isStatic())
            a.setPos(Vector2.add(a.getPos(), moveVec));
        if (!b.isStatic())
            b.setPos(Vector2.subtract(b.getPos(), moveVec));
    }

    /**
     * Resolves the new velocities of the objects post-collision.
     */
    private void resolveVelocity()
    {
        if (a.isStatic() && b.isStatic())
            return;
        
        if (!a.isSolid() || !b.isSolid())
            return;
        
        Vector2 impulseA = Vector2.ZERO;
        Vector2 impulseB = Vector2.ZERO;
        
        float sepV = getSeperatingVelocity();
        
        if (sepV >= 0)
            return;
        
        float newSepV = -sepV * restitution;
        float deltaV = newSepV - sepV;
        
        if (a.isStatic())
            impulseB = Vector2.scale(contactNormal, -deltaV * b.getMass());
        else if (b.isStatic())
            impulseA = Vector2.scale(contactNormal, deltaV * a.getMass());
        else
        {   
            impulseA = Vector2.scale(contactNormal, deltaV / 2 * a.getMass());
            impulseB = Vector2.scale(contactNormal, -deltaV / 2 * b.getMass());
        }

        a.addImpulse(impulseA);
        b.addImpulse(impulseB);
    }

    /**
     * Gets the contact's separating velocity.
     * @return The separating velocity.
     */
    private float getSeperatingVelocity()
    {
        Vector2 relativeVelocity = Vector2.subtract(a.getVelocity(), b.getVelocity());
        if (contactNormal == null)
            return 0;
        return Vector2.dot(relativeVelocity, contactNormal);
    }
    
    /**
     * Gets the first shape in the contact.
     * @return Shape A.
     */
    public Shape getA()
    {
        return a;
    }
    
    /**
     * Gets the second shape in the contact.
     * @return Shape B.
     */
    public Shape getB()
    {
        return b;
    }
    
    /**
     * Gets the penetration distance between the two shapes.
     * @return The penetration distance between the two shapes.
     */
    public float getPenetration()
    {
        return penetration;
    }
    
    /**
     * Gets the collision's restitution.
     * @return The collision's restitution.
     */
    public float getRestitution()
    {
        return restitution;
    }
    
    /**
     * Gets the contact normal.
     * @return The contact normal.
     */
    public Vector2 getContactNormal()
    {
        return contactNormal;
    }
    
    /**
     * Sets a new penetration distance between the shapes.
     * @param penetration The new penetration distance.
     */
    public void setPenetration(float penetration)
    {
        this.penetration = penetration;
    }
    
    /**
     * Sets a new contact normal vector.
     * @param contactNormal The new contact normal vector.
     */
    public void setContactNormal(Vector2 contactNormal)
    {
        this.contactNormal = contactNormal;
    }
}
