package com.lds.physics;

import com.lds.math.Vector2;
import com.lds.physics.primatives.Shape;

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
        restitution = 1;
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

        float totalInverseMass = 1 / (a.getMass() + b.getMass());
        Vector2 movePerIMass = Vector2.scale(contactNormal, penetration * totalInverseMass);

        if (!a.isStatic)
            a.setPos(Vector2.add(a.getPos(), Vector2.scale(movePerIMass, a.getMass())));
        if (!b.isStatic)
            b.setPos(Vector2.subtract(b.getPos(), Vector2.scale(movePerIMass, b.getMass())));
    }

    private void ResolveVelocity()
    {
        if (a.isStatic && b.isStatic)
            return;

        float sepV = GetSeperatingVelocity();

        if (sepV >= 0)
            return;

        float newSepV = -sepV * (restitution);
        float deltaV = newSepV - sepV;

        float totalInverseMass = 1 / (a.getMass() + b.getMass());

        float impulse = deltaV * totalInverseMass;
        Vector2 impulsePerIMass = Vector2.scale(contactNormal, impulse);

        Vector2 impulseA = impulsePerIMass;
        Vector2 impulseB = Vector2.negate(impulsePerIMass);

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
