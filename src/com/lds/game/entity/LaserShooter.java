package com.lds.game.entity;

import javax.microedition.khronos.opengles.GL11;

import com.lds.EntityManager;
import com.lds.math.Vector2;

public class LaserShooter extends Entity
{
    private float shotsPerSecond;
    private float shotVelocity;
    private long time;
    private boolean isOn;
    private long onTime, offTime;
    private Laser laser;
    
    public LaserShooter(Vector2 position, float shotsPerSecond, float shotVelocity, float size, float angle, boolean isOn, long onTime, long offTime)
    {
        super(new com.lds.physics.primatives.Rectangle(new Vector2(size, size),position,angle,true));
        this.shotsPerSecond = shotsPerSecond;
        this.shotVelocity = shotVelocity;
        this.isOn = isOn;
        this.onTime = onTime;
        this.offTime = offTime;
    }
        
    public void update(GL11 gl)
    {
        super.update(gl);
        if(isOn && time>=onTime)
        {
            isOn = false;
            time = 0;
            EntityManager.removeEntity(laser);
        }
        else if(!isOn && time>=offTime)
        {
            isOn = true;
            time = 0;
            laser = new Laser(10, getAngle(), getPos());
        }
    }
}
