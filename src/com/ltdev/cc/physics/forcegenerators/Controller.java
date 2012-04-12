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
