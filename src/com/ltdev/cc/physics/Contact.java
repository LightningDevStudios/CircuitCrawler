package com.ltdev.cc.physics;

import com.ltdev.cc.physics.primitives.Shape;
import com.ltdev.math.Vector2;

public class Contact
{
    public Shape a;
    public Shape b;
    
    public float penetration;
    public float restitution;

    public Vector2 contactNormal;
    
    public Contact(Shape a, Shape b)
    {
        this.a = a;
        this.b = b;
        restitution = 0.5f;
    }

    public Contact(Shape a, Shape b, float penetration, Vector2 contactNormal)
    {
        this(a, b);
        
        this.penetration = penetration;
        this.contactNormal = contactNormal;
    }

    public void Resolve(float duration)
    {
        ResolveInterpenetration();
        ResolveVelocity();
    }

    private void ResolveInterpenetration()
    {
        if (penetration <= 0 || a.isStatic && b.isStatic)
            return;
        
        if (!a.isSolid() || !b.isSolid())
            return;
        
        Vector2 moveVec = Vector2.scale(contactNormal, penetration);

        if (!a.isStatic)
            a.setPos(Vector2.add(a.getPos(), moveVec));
        if (!b.isStatic)
            b.setPos(Vector2.subtract(b.getPos(), moveVec));
    }

    private void ResolveVelocity()
    {
        if (a.isStatic && b.isStatic)
            return;
        
        if (!a.isSolid() || !b.isSolid())
            return;
        
        Vector2 impulseA = Vector2.ZERO;
        Vector2 impulseB = Vector2.ZERO;
        
        float sepV = GetSeperatingVelocity();
        
        if (sepV >= 0)
            return;
        
        float newSepV = -sepV * restitution;
        float deltaV = newSepV - sepV;
        
        if (a.isStatic)
            impulseB = Vector2.scale(contactNormal, -deltaV * b.getMass());
        else if (b.isStatic)
            impulseA = Vector2.scale(contactNormal, deltaV * a.getMass());
        else
        {
            Vector2 impulsePerIMass = Vector2.scale(contactNormal, deltaV / (a.getMass() + b.getMass()));
    
            impulseA = Vector2.scale(impulsePerIMass, b.getMass() * b.getMass());
            impulseB = Vector2.scale(impulsePerIMass, -a.getMass() * a.getMass());
        }

        a.addImpulse(impulseA);
        b.addImpulse(impulseB);
    }

    private float GetSeperatingVelocity()
    {
        Vector2 relativeVelocity = Vector2.subtract(a.getVelocity(), b.getVelocity());
        if (contactNormal == null)
            return 0;
        return Vector2.dot(relativeVelocity, contactNormal);
    }
}
