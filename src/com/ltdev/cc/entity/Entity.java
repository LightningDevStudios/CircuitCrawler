package com.ltdev.cc.entity;

import android.graphics.Point;

import com.ltdev.Stopwatch;
import com.ltdev.Texture;
import com.ltdev.TilesetHelper;
import com.ltdev.cc.Tile;
import com.ltdev.cc.event.InteractListener;
import com.ltdev.cc.physics.primitives.Shape;
import com.ltdev.math.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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
	
	public Entity(Shape shape)
	{		
		this.shape = shape;
		this.shape.setInteractListener(this);
		this.colorVec = new Vector4(1, 1, 1, 1);
		this.endColorVec = new Vector4(1, 1, 1, 1);
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
	
	public void update(GL11 gl)
	{
	    
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
	
	private void updateTexCoords(GL11 gl)
	{
	    float[] t = TilesetHelper.getTextureVertices(tex, new Point(tilesetX, tilesetY));
	    
	    //store data in a float buffer, as required by Android.
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(t.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = byteBuf.asFloatBuffer();
        buffer.put(t);
        buffer.position(0);
	    
	    gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, vbo);
	    gl.glBufferSubData(GL11.GL_ARRAY_BUFFER, 32, t.length * 4, buffer);
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
	public void interact(Entity ent) {}
	
	/**
	 * Called when the entity stops colliding with another entity.
	 * @param ent The entity that was interacted with.
	 */
	public void uninteract(Entity ent) {}
	
	/**
	 * Called when the entity collides with a tile.
	 * @param tile The tile to interact with.
	 */
	public void tileInteract(Tile tile) {}
	
	/**
	 * Called when the entity stops colliding with a tile.
	 * @param tile The tile that was interacted with.
	 */
	public void tileUninteract(Tile tile) {}
		
	/**********************
	 * RenderMode methods *
	 **********************/
		
	//COLOR
	/**
	 * \todo use Vector4s instead (or make a Color4 class).
	 * @param r R component.
	 * @param g G component.
	 * @param b B component.
	 * @param a A component.
	 */
	public void enableColorMode(float r, float g, float b, float a)
	{
		updateColor(r, g, b, a);
	}
	
	public void enableColorMode(int r, int g, int b, int a)
	{
		enableColorMode((float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f, (float) a / 255.0f);
	}
	
	public void updateColor(float r, float g, float b, float a)
	{
		colorVec = new Vector4(r, g, b, a);
	}
	
	public void updateColor(int r, int g, int b, int a)
	{
		updateColor((float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f, (float) a / 255.0f);
	}
		
	public void initColorInterp(float r, float g, float b, float a)
	{
		endColorVec = new Vector4(r, g, b, a);
		isColorInterp = true;
	}
	 
	public void colorInterp()
	{
		if (isColorInterp)
		{
			final float colorInterp = colorInterpSpeed / 1000 * Stopwatch.getFrameTime();
			final Vector4 colorDiffVec = Vector4.abs(Vector4.subtract(endColorVec, colorVec));
			if (colorDiffVec.x() < colorInterp && colorDiffVec.y() < colorInterp && colorDiffVec.z() < colorInterp && colorDiffVec.w() < colorInterp)
			{
				colorVec = endColorVec;
				isColorInterp = false;
			}
			else
			{
				if (endColorVec.x() > colorVec.x())   colorVec = Vector4.add(colorVec, new Vector4(colorInterp, 0, 0, 0));
				else					                    colorVec = Vector4.subtract(colorVec, new Vector4(colorInterp, 0, 0, 0));
				
				if (endColorVec.y() > colorVec.y())	colorVec = Vector4.add(colorVec, new Vector4(0, colorInterp, 0, 0));
				else					                    colorVec = Vector4.subtract(colorVec, new Vector4(0, colorInterp, 0, 0));
				
				if (endColorVec.z() > colorVec.z())	colorVec = Vector4.add(colorVec, new Vector4(0, 0, colorInterp, 0));
				else					                    colorVec = Vector4.subtract(colorVec, new Vector4(0, 0, colorInterp, 0));
				
				if (endColorVec.w() > colorVec.w())	colorVec = Vector4.add(colorVec, new Vector4(0, 0, 0, colorInterp));
				else					                    colorVec = Vector4.subtract(colorVec, new Vector4(0, 0, 0, colorInterp));
			}
		}
	}

    public void unload(GL11 gl)
    {
        int[] buffer = new int[1];
        buffer[0] = vbo;
        
        gl.glDeleteBuffers(3, buffer, 0);
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
	public void setTile(GL11 gl, int tilesetX, int tilesetY)
	{
	    this.tilesetX = tilesetX;
	    this.tilesetY = tilesetY;
	    
	    this.updateTexCoords(gl);
	}
}
