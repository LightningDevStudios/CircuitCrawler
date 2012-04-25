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

import com.ltdev.cc.models.BallData;
import com.ltdev.cc.physics.primitives.Circle;
import com.ltdev.graphics.TextureManager;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

/**
 * An object that represents the player.
 * @author Lightning Development Studios
 */
public class Player extends Entity
{
    private int vertVbo, indVbo;
	private boolean controlled;
	private boolean isDead;
	
	/**
	 * Initializes a new instance of the Player class.
	 * @param position The location of the Player.
	 * @param angle The angle of the Player.
	 */
	public Player(Vector2 position, float angle)
	{
		super(new Circle(DEFAULT_SIZE, position, angle, true));
		controlled = true;		
		tex = TextureManager.getTexture("tilesetentities");
		tilesetX = 0;
		tilesetY = 0;
	}
	
	
	@Override
	public void interact(Entity ent)
	{
	    super.interact(ent);
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
        gl.glDrawElements(GL11.GL_TRIANGLES, BallData.INDEX_COUNT, GL11.GL_UNSIGNED_SHORT, 0);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        //TODO also temporary
        //gl.glDisable(GL11.GL_LIGHT0);
        //gl.glDisable(GL11.GL_LIGHTING);
        gl.glDisableClientState(GL11.GL_NORMAL_ARRAY);
    }
    
    @Override
    public void initialize(GL11 gl)
    {
        vertVbo = BallData.getVertexBufferId(gl);   
        indVbo = BallData.getIndexBufferId(gl);
        this.tex = TextureManager.getTexture("player");
    }
	
	/**
	 * \todo make player flash once it is hit.
	 * @param gl The OpenGL context.
	 */
	@Override
    public void update(GL11 gl)
    {
        super.update(gl); 
    }
	
	public void kill()
	{
	    System.out.println("LOLZ PLAYERZ ARE DETH");
	    System.out.println("NOOS YOU DIES. TIEM TO REASTRAT");
	    isDead = true;
	}
	
	@Override
	public void fall()
	{
	    super.fall();
	    controlled = false;
	}
	
	public void disableUserControl()
	{
		controlled = false;
	}
	
	public boolean isdead()
	{
	    return isDead;
	}
	
	public boolean userHasControl()
	{
		return controlled;
	}

    public void addImpulse(Vector2 f)
    {
        shape.addImpulse(f);
    }
}
