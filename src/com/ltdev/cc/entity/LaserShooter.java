package com.ltdev.cc.entity;

import com.ltdev.EntityManager;
import com.ltdev.Stopwatch;
import com.ltdev.cc.Game;
import com.ltdev.cc.physics.RaycastData;
import com.ltdev.cc.physics.World;
import com.ltdev.cc.physics.primitives.*;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

/**
 * A LaserShooter is an object that fires Lasers.
 * @author Lightning Development Studios.
 *
 */
public class LaserShooter extends Entity
{
    private float shotsPerSecond, time, stupidity, beamWidth;
    private Laser laser;
    private Player player;
    private World physWorld;
    
    /**
     * Initializes a new instance of the LaserShooter class.
     * \todo Fill out the rest of the parameters.
     * @param position The location of the LaserShooter.
     * @param size The size of the LaserShooter.
     * @param angle The angle of the LaserShooter.
     * @param stupidity
     * @param beamWidth
     * @param shotsPerSecond
     * @param player The player.
     * @param physWorld The physics world.
     */
    public LaserShooter(Vector2 position, float size, float angle, float stupidity, float beamWidth, float shotsPerSecond, Player player, World physWorld)
    {
        super(new Rectangle(new Vector2(size, size), position, angle, true));
        
        shape.setStatic(true);
        this.player = player;
        
        this.shotsPerSecond = shotsPerSecond;
        this.stupidity = stupidity;
        this.beamWidth = beamWidth;
        this.physWorld = physWorld;
        
        this.tilesetX = 3;
        this.tilesetY = 0;
        
        time = Stopwatch.getFrameTime();
    }

    /**
     * Aligns the LaserShooter to face the player.
     */
    public void facePlayer()
    {
        Vector2 distance = Vector2.subtract(shape.getPos(), player.getPos());
        float targetAng = distance.angleDeg() + 180;
        shape.setAngle(targetAng);
    }
    
    @Override
    public void update(GL11 gl)
    {
        super.update(gl);
        
        time += Stopwatch.getFrameTime();
        facePlayer();
        
        if (time / 1000 > shotsPerSecond)
        {
            if (laser != null)
                EntityManager.removeEntity(laser);
            
            float rand = (float)((Math.random() * 2 - 1) * stupidity);
            RaycastData data = physWorld.raycast(getPos(), (float) (shape.getAngle() + Math.toRadians(rand)));
            if (data != null)
            {
                time = 0;
                Vector2 laserPos = Vector2.add(getPos(), new Vector2(data.getDistance() / 2 * (float)Math.cos((float) (shape.getAngle() + Math.toRadians(rand))), data.getDistance() / 2 * (float)Math.sin((float) (shape.getAngle() + Math.toRadians(rand)))));
                laser = new Laser(5, data.getDistance(), (float) (shape.getAngle() + Math.toRadians(rand)), laserPos);
                laser.setTexture(Game.tilesetentities);
                EntityManager.addEntity(laser);
            }
        }
    }
}
