package com.ltdev.cc.entity;



import com.ltdev.EntityManager;
import com.ltdev.Stopwatch;
import com.ltdev.cc.Game;
import com.ltdev.cc.physics.RaycastData;
import com.ltdev.cc.physics.World;
import com.ltdev.cc.physics.primitives.*;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;


public class LaserShooter extends Entity
{
    private float shotsPerSecond, time, stupidity, beamWidth;
    private Laser laser;
    private Player player;
    private World physWorld;
    
    public LaserShooter(Vector2 position, float size, float angle, float stupidity, float beamWidth, float shotsPerSecond, Player player, World physWorld)
    {
        super(new Rectangle(new Vector2(size, size), position, angle, true));
        
        this.shotsPerSecond = shotsPerSecond;
        this.stupidity = stupidity;
        this.beamWidth = beamWidth;
        this.physWorld = physWorld;
        
        time = Stopwatch.getFrameTime();
    }
       
    public void facePlayer()
    {
        Vector2 distance = Vector2.subtract(shape.getPos(), player.getPos());
        float angle = (float)((Math.atan2(Vector2.perpDot(Vector2.UNIT_X, distance), Vector2.dot(Vector2.UNIT_X, distance))) + (Math.random() * 2 - 1) * stupidity);
        shape.setAngle(angle);
    }
    
    public void update(GL11 gl)
    {
        super.update(gl);
        
        time += Stopwatch.getFrameTime();
        facePlayer();
        
        if (time / 1000 > shotsPerSecond)
        {
            time = 0;
            if (laser != null)
                EntityManager.removeEntity(laser);
            
            RaycastData data = physWorld.rayCast(getPos(), shape.getAngle());
            if(data != null)
            {
                Vector2 laserPos = Vector2.add(getPos(), new Vector2(data.distance / 2 * (float)Math.cos(getAngle()), data.distance / 2 * (float)Math.sin(getAngle())));
                laser = new Laser(5, data.distance, getAngle(), laserPos);
                laser.setTexture(Game.tilesetentities);
                EntityManager.addEntity(laser);
            }
        }
    }
}
