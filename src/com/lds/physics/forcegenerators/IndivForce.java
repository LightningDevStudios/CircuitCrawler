package com.lds.physics.forcegenerators;

import com.lds.physics.primitives.Shape;

public interface IndivForce
{
    void UpdateForce(float frameTime, Shape s);
}
