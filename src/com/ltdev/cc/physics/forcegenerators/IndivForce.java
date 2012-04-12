package com.ltdev.cc.physics.forcegenerators;

import com.ltdev.cc.physics.primitives.Shape;

/**
 * Defines a force generator.
 * @author Lightning Development Studios
 */
public interface IndivForce
{
    /**
     * Apply a force to a shape, given a dt.
     * @param frameTime dt.
     * @param s The shape.
     */
    void updateForce(float frameTime, Shape s);
}
