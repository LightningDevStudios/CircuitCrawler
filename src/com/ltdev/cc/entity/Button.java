package com.ltdev.cc.entity;

import com.ltdev.cc.SoundPlayer;
import com.ltdev.cc.physics.primitives.Circle;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

/**
 * A Button is an object that reacts to other objects being placed on top of it.
 * @author Lightning Development Studios
 */
public class Button extends Entity
{
	private boolean active, prevActive;
	
	/**
	 * Initializes a new instance of the Button class.
	 * @param position The Button's position.
	 */
	public Button(Vector2 position)
	{
		super(new Circle(69, position, false));
		
		this.tilesetX = 0;
		this.tilesetY = 0;
	}
	
	@Override
	public void update(GL11 gl)
	{
	    super.update(gl);
	    
	    //update the texture when activated/deactivated.
	    if (prevActive != active)
	    {	        
	        prevActive = active;
	    }
	}
	
	/**
	 * Gets a value indicating whether the Button is activated or not.
	 * @return A value indicating whether the Button is activated or not.
	 */
	public boolean isActive()
	{
		return active;
	}
	
	/**
	 * Activate the Button.
	 */
	public void activate()
	{
	    prevActive = active;
		active = true;
		SoundPlayer.playSound(SoundPlayer.SOUND_TEST);
	}
	
	/**
	 * Deactivate the button.
	 */
	public void deactivate()
	{
	    prevActive = active;
		active = false;
		SoundPlayer.playSound(SoundPlayer.SOUND_TEST);
	}
	
	@Override
	public void interact(Entity ent)
	{
	    super.interact(ent);
	    
		if (ent instanceof Player || ent instanceof HoldObject)
			activate();
	}
	
	@Override
	public void uninteract(Entity ent)
	{
		if (ent instanceof Player || ent instanceof HoldObject)
			deactivate();	
	}
}
