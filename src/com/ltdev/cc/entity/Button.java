/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev.cc.entity;

import com.ltdev.cc.SoundPlayer;
import com.ltdev.cc.models.ButtonDownData;
import com.ltdev.cc.models.ButtonUpData;
import com.ltdev.cc.physics.primitives.Circle;
import com.ltdev.graphics.TextureManager;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

/**
 * A Button is an object that reacts to other objects being placed on top of it.
 * @author Lightning Development Studios
 */
public class Button extends Entity
{
	private boolean active, previousActive;
	private int indCount;
	
	private int vertVbo, indVbo;
	
	/**
	 * Initializes a new instance of the Button class.
	 * @param position The Button's position.
	 */
	public Button(Vector2 position)
	{
		super(new Circle(69, position, false));
		
		this.tex = TextureManager.getTexture("tilesetentities");
		this.tilesetX = 0;
		this.tilesetY = 0;
	}
	
	@Override
	public void draw(GL11 gl)
	{
	    //convert local space to world space.
        gl.glMultMatrixf(shape.getModel().array(), 0);
        
        //TODO temorary while we don't have the rest of the art
        gl.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        //gl.glEnable(GL11.GL_LIGHTING);
        //gl.glEnable(GL11.GL_LIGHT0);
        
        //bind the texture and set the color.
        gl.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTexture());
        gl.glColor4f(colorVec.x(), colorVec.y(), colorVec.z(), colorVec.w());

        //bind the VBO and set up the vertex and tex coord pointers.
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vertVbo);
        gl.glVertexPointer(3, GL11.GL_FLOAT, 32, 0);
        gl.glTexCoordPointer(2, GL11.GL_FLOAT, 32, 12);
        gl.glNormalPointer(GL11.GL_FLOAT, 32, 20);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        
        //draw the entity.
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, indVbo);
        gl.glDrawElements(GL11.GL_TRIANGLES, indCount, GL11.GL_UNSIGNED_SHORT, 0);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
        //gl.glDrawArrays(GL11.GL_TRIANGLES, 0, BlockData.VERTEX_COUNT);
        
        //TODO also temporary
        //gl.glDisable(GL11.GL_LIGHT0);
        //gl.glDisable(GL11.GL_LIGHTING);
        gl.glDisableClientState(GL11.GL_NORMAL_ARRAY);
	}
	
	@Override
	public void initialize(GL11 gl)
	{
	    vertVbo = ButtonUpData.getVertexBufferId(gl);   
        indVbo = ButtonUpData.getIndexBufferId(gl);
        this.tex = TextureManager.getTexture("button");
        indCount = ButtonUpData.INDEX_COUNT;
	}
	
	@Override
	public void update(GL11 gl)
	{
	    super.update(gl);
	    
	    //update the texture when activated/deactivated.
	    if (previousActive != active)
	    {
	        if (active)
	        {
	            vertVbo = ButtonDownData.getVertexBufferId(gl);   
	            indVbo = ButtonDownData.getIndexBufferId(gl);
	            indCount = ButtonDownData.INDEX_COUNT;
	        }
	        else
	        {
	            vertVbo = ButtonUpData.getVertexBufferId(gl);   
	            indVbo = ButtonUpData.getIndexBufferId(gl);
	            indCount = ButtonUpData.INDEX_COUNT;
	        }
	            
	        
	        previousActive = active;
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
		active = true;
		SoundPlayer.playSound(SoundPlayer.SOUND_TEST);
	}
	
	/**
	 * Deactivate the button.
	 */
	public void deactivate()
	{
		active = false;
		SoundPlayer.playSound(SoundPlayer.SOUND_TEST);
	}
	
	@Override
	public void interact(Entity ent)
	{
	    super.interact(ent);
	    
		if (ent instanceof Player || ent instanceof HoldObject)
		    if (!active)
		        activate();
	}
	
	@Override
	public void uninteract(Entity ent)
	{
		if (ent instanceof Player || ent instanceof HoldObject)
		    if (active)
		        deactivate();	
	}
}
