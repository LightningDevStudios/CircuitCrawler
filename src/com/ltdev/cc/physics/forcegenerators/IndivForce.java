package com.ltdev.cc.physics.forcegenerators;

import com.ltdev.cc.physics.primitives.Shape;

public interface IndivForce
{
    void UpdateForce(float frameTime, Shape s);
}
