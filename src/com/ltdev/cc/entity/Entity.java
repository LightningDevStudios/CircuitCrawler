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

import android.graphics.Point;

import com.ltdev.Texture;
import com.ltdev.TilesetHelper;
import com.ltdev.cc.event.InteractListener;
import com.ltdev.cc.physics.primitives.Shape;
import com.ltdev.math.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL11;

public abstract class Entity implements InteractListener
{
	public static final float DEFAULT_SIZE = 32.0f;
	
	//behavior data
	protected boolean isColorInterp;
	
	protected Shape shape;
	
	//graphics data
	protected Vector4 colorVec, endColorVec;
	protected float colorInterpSpeed;
	protected Texture tex;
	protected int tilesetX, tilesetY;
		
	protected int vbo;
	
	private ArrayList<Entity> previousFrameColliders;
	private ArrayList<Entity> currentFrameColliders;
	
	public Entity(Shape shape)
	{		
		this.shape = shape;
		this.shape.setInteractListener(this);
		this.colorVec = new Vector4(1, 1, 1, 1);
		this.endColorVec = new Vector4(1, 1, 1, 1);
		
		this.previousFrameColliders = new ArrayList<Entity>();
		this.currentFrameColliders = new ArrayList<Entity>();
	}
	
	public void draw(GL11 gl)
	{
	    //convert local space to world space.
		gl.glMultMatrixf(shape.getModel().array(), 0);
		
		//bind the texture and set the color.
		gl.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTexture());
	    gl.glColor4f(colorVec.x(), colorVec.y(), colorVec.z(), colorVec.w());

	    //bind the VBO and set up the vertex and tex coord pointers.
		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo);
		gl.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
		gl.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 32);
		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);

		//draw the entity.
		gl.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
	}
	
	/**
	 * Updates the entity.
	 * @param gl The OpenGL context.
	 */
	@SuppressWarnings("unchecked")
    public void update(GL11 gl)
	{
	    if (previousFrameColliders.size() > 0 || currentFrameColliders.size() > 0)
	    {
    	    for (Entity ent : previousFrameColliders)
    	    {
    	        if (!currentFrameColliders.contains(ent))
    	            this.uninteract(ent);
    	    }
    	    
    	    previousFrameColliders = (ArrayList<Entity>)currentFrameColliders.clone();
    	    currentFrameColliders.clear();
	    }
	}

	/**
	 * \todo move this to the constructor, rework parser to do that.
	 * @param gl The OpenGL context.
	 */
	public void initialize(GL11 gl)
	{	    
	    //8 floats for 4 points, 2 sets of data.
	    float[] verts = new float[8 * 2];
	    
	    //gather the required data.
	    float[] v = shape.getVertices();
	    float[] t = TilesetHelper.getTextureVertices(tex, new Point(tilesetX, tilesetY));
	    
	    //copy vertices over
	    for (int i = 0; i < v.length; i++)
	        verts[i] = v[i];
	    
	    //copy texture coordinates over.
	    for (int i = 0; i < t.length; i++)
	        verts[i + 8] = t[i];
	    
	    //store data in a float buffer, as required by Android.
	    ByteBuffer byteBuf = ByteBuffer.allocateDirect(verts.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = byteBuf.asFloatBuffer();
        buffer.put(verts);
        buffer.position(0);
	    
        //generate a VBO.
	    int[] tempPtr = new int[1];
        gl.glGenBuffers(1, tempPtr, 0);
        vbo = tempPtr[0];
        
        //send data to GPU.
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo);
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, verts.length * 4, buffer, GL11.GL_STATIC_DRAW);
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
	}
	
	/*********************
	 * Collision Methods *
	 *********************/

	//TODO: I don't even...
	public boolean isFacing(Entity ent)
	{
		float angleBetween = (float)Math.toDegrees(Math.atan2(ent.getPos().y() - this.getPos().y() , ent.getPos().x() - this.getPos().x()));
		//clamp angle between 0 and 360
		if (angleBetween == 360.0f)
			angleBetween = 0.0f;
		else if (angleBetween > 360.0f)
			angleBetween -= 360 * (int)(angleBetween / 360);
		else if (angleBetween < 0.0f)
			angleBetween += 360;
		
		float angleDiff = shape.getAngle() - angleBetween;
		
		if (angleDiff > 315.0f)
			angleDiff -= 360.0f;
		
		return angleDiff > -45 && angleDiff < 45;
	}
	
	/**
	 * Called when the entity collides with another entity.
	 * @param ent The entity to interact with.
	 */
	public void interact(Entity ent)
	{
	    currentFrameColliders.add(ent);
	}
	
	/**
	 * Called when the entity stops colliding with another entity.
	 * @param ent The entity that was interacted with.
	 */
	public void uninteract(Entity ent)
	{
	    
	}

    public void unload(GL11 gl)
    {
        int[] buffer = new int[1];
        buffer[0] = vbo;
        
        gl.glDeleteBuffers(1, buffer, 0);
    }
	
	/**************************
	 * Accessors and Mutators *
	 **************************/
	
    /**
     * Gets the current entity.
     * Used for collision callbacks.
     * @return The current entity as an Entity.
     */
    public Entity getEntity()
    {
        return this;
    }
    
    /**
     * Gets the underlying Shape used for physics.
     * @return The Entity's Shape.
     */
	public Shape getShape()
    {   
	    return shape;
	}
	
	/**
	 * Gets the Entity's position in world coordinates.
	 * @return A Vector2 representing the Entity's location.
	 */
	public Vector2 getPos()
	{
	    return shape.getPos();
	}
	
	/**
	 * Gets the Entity's angle, in radians.
	 * @return The Entity's angle.
	 * \todo make sure it's actually in radians?
	 */
	public float getAngle()
    {
        return shape.getAngle();
    }

    /**
     * Gets the color of the Entity.
     * @return The Entity's color.
     */
    public Vector4 getColor()
    {
        return colorVec;
    }

    /**
     * Gets the texture used to display this Entity.
     * @return The Entity's texture.
     */
    public Texture getTexture()
    {
        return tex;
    }
	
	/**
	 * Sets the Entity's position in world coordinates.
	 * @param position The Entity's new position.
	 */
	public void setPos(Vector2 position)
	{
	    shape.setPos(position);
	}
	
	/**
	 * Sets the angle of the Entity.
	 * @param angle The Entity's new angle.
	 */
	public void setAngle(float angle)
	{
	    shape.setAngle(angle);
	}
	
	/**
	 * Sets the speed at which changes in the Entity's color will smoothly transition.
	 * @param s The speed to interpolate at.
	 */
	public void setColorInterpSpeed(float s)
	{
	    this.colorInterpSpeed = s;
	}
	
	/**
	 * Sets the Entity's texture.
	 * @param tex The Entity's new Texture.
	 */
	public void setTexture(Texture tex)
	{
	    this.tex = tex;
	}
	
	/**
	 * Sets the Entity's tile.
	 * @param gl The OpenGL context.
	 * @param tilesetX The X coordinate of the tile.
	 * @param tilesetY The Y coordinate of the tile.
	 */
	/*public void setTile(GL11 gl, int tilesetX, int tilesetY)
	{
	    this.tilesetX = tilesetX;
	    this.tilesetY = tilesetY;
	    
	    this.updateTexCoords(gl);
	}*/
}
