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
import com.ltdev.cc.models.ButtonUpData;
import com.ltdev.cc.physics.primitives.Circle;
import com.ltdev.graphics.TextureManager;
import com.ltdev.math.Vector2;

import java.nio.*;

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
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo);
        gl.glVertexPointer(3, GL11.GL_FLOAT, 32, 0);
        gl.glTexCoordPointer(2, GL11.GL_FLOAT, 32, 12);
        gl.glNormalPointer(GL11.GL_FLOAT, 32, 20);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        
        //TODO also temporary
        //gl.glDisable(GL11.GL_LIGHT0);
        //gl.glDisable(GL11.GL_LIGHTING);
        gl.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        
        //draw the entity.
        gl.glDrawArrays(GL11.GL_TRIANGLES, 0, ButtonUpData.VERTEX_COUNT);
	}
	
	@Override
	public void initialize(GL11 gl)
	{
	    //TODO make this hold only one instance of the model instead of one per model.
	    ByteBuffer byteBuf = ByteBuffer.allocateDirect(ButtonUpData.VERTEX_BYTE_COUNT);
        byteBuf.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = byteBuf.asFloatBuffer();
        buffer.put(ButtonUpData.VERTICES);
        buffer.position(0);
        
        //generate a VBO.
        int[] tempPtr = new int[1];
        gl.glGenBuffers(1, tempPtr, 0);
        vbo = tempPtr[0];
        
        //send data to GPU.
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, ButtonUpData.VERTEX_BYTE_COUNT, buffer, GL11.GL_STATIC_DRAW);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
        
        this.tex = TextureManager.getTexture("buttonup");
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
