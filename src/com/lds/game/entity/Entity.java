package com.lds.game.entity;

import android.util.Log;

import com.lds.Stopwatch;
import com.lds.Texture;
import com.lds.TilesetHelper;
import com.lds.game.event.InteractListener;
import com.lds.math.*;
import com.lds.physics.Shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public abstract class Entity implements InteractListener
{
	public static final float DEFAULT_SIZE = 32.0f;
	public static final byte[] indices = {0, 1, 2, 3};
	
	public static ByteBuffer indexBuffer;
	public static int VBOIndexPtr;
	
	//behavior data
	protected boolean isColorInterp;
	
	protected Shape shape;
	
	//graphics data
	protected Vector4 colorVec, endColorVec;
	protected float colorInterpSpeed;
	protected Texture tex;
	
	protected float[] texture;
	
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer textureBuffer;
	protected FloatBuffer colorBuffer;
	
	protected int VBOVertPtr, VBOGradientPtr, VBOTexturePtr;
	protected boolean needToUpdateTexVBO, needToUpdateVertexVBO;
	
	public Entity(Shape shape)
	{		
		this.shape = shape;
		this.shape.setInteractListener(this);
		this.vertexBuffer = setBuffer(vertexBuffer, shape.getVertices());
		this.colorVec = new Vector4(1, 1, 1, 1);
		this.endColorVec = new Vector4(1, 1, 1, 1);
	}
	
	public void draw(GL11 gl)
	{
		gl.glMultMatrixf(shape.getModel().array(), 0);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex.getTexture());
		
		//Enable settings for this polygon
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

	    gl.glColor4f(colorVec.getX(), colorVec.getY(), colorVec.getZ(), colorVec.getW());

		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBOVertPtr);
		gl.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);

		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBOTexturePtr);
		gl.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
		gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);

		gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, VBOIndexPtr);
		gl.glDrawElements(GL11.GL_TRIANGLE_STRIP, 4, GL11.GL_UNSIGNED_BYTE, 0);
		gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
				
		//Disable things for next polygon
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	public void update()
	{
	    //TODO: change this?
		//if (shape.getScale().length() <= 0.0f)
			//EntityManager.removeEntity(this);
		colorInterp();
	}

	protected FloatBuffer setBuffer(FloatBuffer buffer, float[] values)
	{
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(values.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		buffer = byteBuf.asFloatBuffer();
		buffer.put(values);
		buffer.position(0);
		
		return buffer;
	}
	
	/*********************
	 * Collision Methods *
	 *********************/

	//TODO: I don't even...
	public boolean isFacing(Entity ent)
	{
		float angleBetween = (float)Math.toDegrees(Math.atan2(ent.getYPos() - this.getYPos() , ent.getXPos() - this.getXPos()));
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
	
	//TEXTURE
	public void enableTextureMode(Texture tex)
	{
		updateTexture(tex);
	}
	
	public void enableTextureMode(Texture tex, float[] texture)
	{
		updateTexture(tex, texture);
	}
		
	public void updateTexture(Texture tex)
	{
		final float[] initTexture = 
	    {
	        1.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 0.0f,
			0.0f, 1.0f
		};
		
		updateTexture(tex, initTexture);
	}
	
	public void updateTexture(Texture tex, float[] texture)
	{
		this.tex = tex;
		this.texture = texture;
		this.textureBuffer = setBuffer(textureBuffer, texture);
		needToUpdateTexVBO = true;
	}
	
	public void disableTextureMode()
	{
	}
	
	//TILESET
	public void enableTilesetMode(Texture tex, int x, int y)
	{
		updateTileset(tex, x, y);
	}
	
	public void enableTilesetMode(Texture tex, int tileID)
	{
		updateTileset(tex, tileID);
	}
		
	public void updateTileset(Texture tex, int x, int y)
	{
		updateTileset(tex, TilesetHelper.getTilesetID(x, y, tex));	
	}
	
	public void updateTileset(Texture tex, int tileID)
	{
		this.tex = tex;
		texture = TilesetHelper.getTextureVertices(tex, tileID);
		this.textureBuffer = setBuffer(textureBuffer, texture);
		needToUpdateTexVBO = true;
	}
	
	public void updateTileset(int x, int y)
	{
		if (tex != null)
			updateTileset(tex, x, y);
	}
	
	public void updateTileset(int tileID)
	{
		if (tex != null)
			updateTileset(tex, tileID);
	}
	
	public void disableTilesetMode()
	{
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
			if (colorDiffVec.getX() < colorInterp && colorDiffVec.getY() < colorInterp && colorDiffVec.getZ() < colorInterp && colorDiffVec.getW() < colorInterp)
			{
				colorVec = endColorVec;
				isColorInterp = false;
			}
			else
			{
				if (endColorVec.getX() > colorVec.getX())   colorVec = Vector4.add(colorVec, new Vector4(colorInterp, 0, 0, 0));
				else					                    colorVec = Vector4.subtract(colorVec, new Vector4(colorInterp, 0, 0, 0));
				
				if (endColorVec.getY() > colorVec.getY())	colorVec = Vector4.add(colorVec, new Vector4(0, colorInterp, 0, 0));
				else					                    colorVec = Vector4.subtract(colorVec, new Vector4(0, colorInterp, 0, 0));
				
				if (endColorVec.getZ() > colorVec.getZ())	colorVec = Vector4.add(colorVec, new Vector4(0, 0, colorInterp, 0));
				else					                    colorVec = Vector4.subtract(colorVec, new Vector4(0, 0, colorInterp, 0));
				
				if (endColorVec.getW() > colorVec.getW())	colorVec = Vector4.add(colorVec, new Vector4(0, 0, 0, colorInterp));
				else					                    colorVec = Vector4.subtract(colorVec, new Vector4(0, 0, 0, colorInterp));
			}
		}
	}
	
	/*************
	 * VBO Stuph *
	 *************/
	
	public void resetAllBuffers()
    {
        if (shape.getVertices() != null)
            vertexBuffer = setBuffer(vertexBuffer, shape.getVertices());
        if (texture != null)
            textureBuffer = setBuffer(textureBuffer, texture);
    }
    
    public static void resetIndexBuffer()
    {
        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }
    
    public void genHardwareBuffers(GL11 gl)
    {
        int[] tempPtr = new int[1];
        
        //VERTEX
        gl.glGenBuffers(1, tempPtr, 0);
        VBOVertPtr = tempPtr[0];
        
        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBOVertPtr);
        final int vertSize = vertexBuffer.capacity() * 4;
        gl.glBufferData(GL11.GL_ARRAY_BUFFER, vertSize, vertexBuffer, GL11.GL_STATIC_DRAW); //\TODO choose static/draw settings..?
        
        gl.glGenBuffers(1, tempPtr, 0);
        VBOTexturePtr = tempPtr[0];
        needToUpdateTexVBO = true;
        updateTextureVBO(gl);

        gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
    }
    
    public void updateVertexVBO(GL11 gl)
    {
        if (needToUpdateVertexVBO)
        {
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBOVertPtr);
            final int vertexSize = vertexBuffer.capacity() * 4;
            gl.glBufferData(GL11.GL_ARRAY_BUFFER, vertexSize, vertexBuffer, GL11.GL_STATIC_DRAW);
        }
        needToUpdateVertexVBO = false;
    }
    
    public void updateTextureVBO(GL11 gl)
    {
        if (needToUpdateTexVBO)
        {
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBOTexturePtr);
            final int textureSize = textureBuffer.capacity() * 4;
            gl.glBufferData(GL11.GL_ARRAY_BUFFER, textureSize, textureBuffer, GL11.GL_STATIC_DRAW);
        }
        needToUpdateTexVBO = false;
    }
    
    public static void genIndexBuffer(GL11 gl)
    {
        int[] tempPtr = new int[1];
        
        gl.glGenBuffers(1, tempPtr, 0);
        VBOIndexPtr = tempPtr[0];
        
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, VBOIndexPtr);
        final int indexSize = indexBuffer.capacity();
        gl.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indexSize, indexBuffer, GL11.GL_STATIC_DRAW);
        gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        final int error = gl.glGetError();
        if (error != GL11.GL_NO_ERROR)
        {
            Log.e("LDS_Game", "Index buffer generates GL_ERROR: " + error);
        }
    }
    
    public void freeHardwareBuffers(GL11 gl)
    {
        GL11 gl11 = (GL11)gl;
        int[] buffer = new int[3];
        buffer[0] = VBOVertPtr;
        buffer[1] = VBOTexturePtr;
        buffer[2] = VBOGradientPtr;
        
        gl11.glDeleteBuffers(3, buffer, 0);
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
	 * Gets the Entity's X position in world coordiantes.
	 * @return The Entity's X coordinate.
	 * @deprecated Use "getPos().getX()" instead.
	 */
	public float getXPos()
	{
	    return shape.getPos().getX();
	}
	
	/**
     * Gets the Entity's Y position in world coordiantes.
     * @return The Entity's Y coordinate.
     * @deprecated Use "getPos().getY()" instead.
     */
	public float getYPos()
	{
	    return shape.getPos().getY();
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
	 * Gets the Entity's scale.
	 * @return The Entity's scale.
	 * \todo should only be defined by vertices, consider removing scale altogether.
	 */
	public Vector2 getScale()
    {
        return shape.getScale();
    }
	
	/**
	 * Get's the Entity's scale in the X direction.
	 * @return The Entity's X scale.
	 * @deprecated Use "getScale().getX()" instead.
	 */
	public float getXScale()
    {
        return getScale().getX();
    }
    
	/**
     * Get's the Entity's scale in the Y direction.
     * @return The Entity's Y scale.
     * @deprecated Use "getScale().getY()" instead.
     */
    public float getYScale()
    {
        return getScale().getY();
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
     * Gets the texture coordinates of the Entity.
     * @return The Entity's texture coordinates.
     * @deprecated Replaced by interlaced VBO system.
     */
    public float[] getTextureCoords()
    {
        return texture;
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
     * Gets the vertex VBO pointer.
     * @return A pointer to the Entity's vertex array on the GPU.
     * @deprecated Replaced by interlaced VBO system.
     */
    public int getVertexVBO()
    {
        return VBOVertPtr;
    }

    /**
     * Gets the texture coordinate VBO pointer.
     * @return A pointer to the Entity's texture coordinate array on the GPU.
     * @deprecated Replaced by interlaced VBO system.
     */
    public int getTextureVBO()
    {
        return VBOTexturePtr;
    }

    /**
     * Gets the index VBO pointer.
     * @return A pointer to the Entity's indices on the GPU.
     * @deprecated Replaced by interlaced VBO system.
     */
    public int getGradientVBO()
    {
        return VBOGradientPtr;
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
	 * Sets the scale of the Entity.
	 * @param scale The Entity's new scale.
	 */
	public void setScale(Vector2 scale)
	{
	    shape.setScale(scale);
	}
	
	/**
	 * Sets the speed at which changes in the Entity's color will smoothly transition.
	 * @param s The speed to interpolate at.
	 */
	public void setColorInterpSpeed(float s)
	{
	    this.colorInterpSpeed = s;
	}
}
