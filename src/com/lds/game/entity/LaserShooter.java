package com.lds.game.entity;

import com.lds.physics.World;

import com.lds.Stopwatch;

import javax.microedition.khronos.opengles.GL11;

import com.lds.EntityManager;
import com.lds.math.Vector2;

public class LaserShooter extends Entity
{
    private float shotsPerSecond, time, stupidity, beamWidth;
    private Laser laser;
    private Player player;
    private World physWorld;
    
    public LaserShooter(Vector2 position, float size, float angle, float stupidity, float beamWidth, float shotsPerSecond, Player player, World physWorld)
    {
        super(new com.lds.physics.primatives.Rectangle(new Vector2(size, size),position,angle,true));
        
        this.shotsPerSecond = shotsPerSecond;
        this.stupidity = stupidity;
        this.beamWidth = beamWidth;
        this.physWorld = physWorld;
        
        time = Stopwatch.getFrameTime();
    }
       
    public void facePlayer()
    {
        Vector2 distance = Vector2.subtract(shape.getPos(), player.getPos());
        float angle = (float) (((float)Math.atan2(Vector2.perpDot(Vector2.UNIT_X,distance), Vector2.dot(Vector2.UNIT_X,distance))) + (Math.random() * 2 - 1) * stupidity);
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
            if(laser != null)
                EntityManager.removeEntity(laser);
            

            //Vector2 end = null;
           // physWorld.rayCast(getPos(), end);
            
            //laser = new Laser(beamWidth, shape.getAngle(), shape.getPos());
            //EntityManager.addEntity(laser);
        }
    }
}
