package com.lds.physics.forcegenerators;

import com.lds.physics.primatives.Shape;
import com.lds.math.Vector2;

public class Controller implements IndivForce
{
    public float vertForce;
    public float horizForce;
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    public Controller(float vertForce, float horizForce)
    {
        this.vertForce = vertForce;
        this.horizForce = horizForce;
        up = false;
        down = false;
        left = false;
        right = false;
    }

    public void UpdateForce(float frameTime, Shape s)
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
