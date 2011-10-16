package com.lds.game.entity;

import android.util.Log;

import com.lds.EntityManager;
import com.lds.Enums.RenderMode;
import com.lds.Stopwatch;
import com.lds.Texture;
import com.lds.TilesetHelper;
import com.lds.math.*;
import com.lds.math.Vector2;
import com.lds.physics.Shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.EnumSet;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;


public abstract class Entity 
{
	public static final float DEFAULT_SIZE = 32.0f;
	
	//behavior data
	protected boolean isColorInterp, isGradientInterp;
	
	protected Shape shape;
	
	//graphics data
	protected Vector4 colorVec, endColorVec;
	protected float colorInterpSpeed;
	protected Texture tex;
	protected EnumSet<RenderMode> renderMode;
	
	protected float[] texture;
	protected float[] color, endColor;
	public static final byte[] indices = {0, 1, 2, 3};
	
	protected FloatBuffer vertexBuffer;
	protected FloatBuffer textureBuffer;
	protected FloatBuffer colorBuffer;
	public static ByteBuffer indexBuffer;
	
	protected int VBOVertPtr, VBOGradientPtr, VBOTexturePtr;
	protected boolean needToUpdateTexVBO, needToUpdateGradientVBO, needToUpdateVertexVBO;
	public static int VBOIndexPtr;
	public static boolean useVBOs;
	
	public Entity(Shape shape)
	{		
		this.shape = shape;
		this.vertexBuffer = setBuffer(vertexBuffer, shape.getVertices());
		this.colorVec = new Vector4(0, 0, 0, 1);
		this.endColorVec = new Vector4(0, 0, 0, 1);
		renderMode = EnumSet.noneOf(RenderMode.class);
	}
	
	public void draw(GL10 gl)
	{
		gl.glMultMatrixf(shape.getModel().array(), 0);
		
		final boolean containsColor = renderMode.contains(RenderMode.COLOR);
		final boolean containsGradient = renderMode.contains(RenderMode.GRADIENT);
		final boolean containsTexture = renderMode.contains(RenderMode.TEXTURE);
		final boolean containsTileset = renderMode.contains(RenderMode.TILESET);
		
		//Enable texturing and bind the current texture pointer (texturePtr) to GL_TEXTURE_2D
		if (containsTexture || containsTileset)
		{
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, tex.getTexture());
		}
		
		//Backface culling
		gl.glFrontFace(GL10.GL_CW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		
		//Enable settings for this polygon
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		if (containsTexture || containsTileset) 
		{
		    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}
		
		if (containsGradient) 
		{
		    gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		}
		
		//Sets color
		if (containsColor) 
		{
		    gl.glColor4f(colorVec.getX(), colorVec.getY(), colorVec.getZ(), colorVec.getW());
		}
		
		//Bind vertices, texture coordinates, and/or color coordinates to the OpenGL system
		if (!useVBOs)
		{
			gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
			if (containsTexture || containsTileset) 
			{
			    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
			}
			
			if (containsGradient) 
			{
			    gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
			}
			
			//Draw the vertices
			gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL10.GL_UNSIGNED_BYTE, indexBuffer);	
		}
		else
		{
			GL11 gl11 = (GL11)gl;
			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBOVertPtr);
			gl11.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
			
			if (containsTexture || containsTileset)
			{
				gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBOTexturePtr);
				gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
			}
			if (containsGradient)
			{
				gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBOGradientPtr);
				gl11.glColorPointer(4, GL11.GL_FLOAT, 0, 0);
			}
			gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, VBOIndexPtr);
			gl11.glDrawElements(GL11.GL_TRIANGLE_STRIP, 4, GL11.GL_UNSIGNED_BYTE, 0);
			
			gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
			gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
			
			final int error = gl11.glGetError();
			if (error != GL11.GL_NO_ERROR)
			{
				Log.e("LDS_Game", "Entity rendering generates GL_ERROR: " + error);
			}
		}
				
		//Disable things for next polygon
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		if (containsGradient)
		{
		    gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}
		gl.glDisable(GL10.GL_CULL_FACE);
		
		//Disable texturing for next polygon
		if (containsTexture || containsTileset)
		{
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		
		//Reset color for next polygon.
		if (containsColor) 
		{
		    gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}
	
	public void update()
	{
	    //TODO: change this?
		if (shape.getScale().length() <= 0.0f)
			EntityManager.removeEntity(this);
		colorInterp();
		gradientInterp();
	}
		
	public void collide(Entity ent)
	{
		
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
	 * Called when the entity collides with another entity
	 * @param ent The entity to interact with.
	 */
	public void interact(Entity ent) {}
	
	/**
	 * Called when the entity stops colliding with another entity
	 * @param ent The entity that was interacted with.
	 */
	public void uninteract(Entity ent) {}
	
	/**
	 * Called when the entity collides with a tile
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
	
	//BLANK
	public void clearRenderModes()
	{
		renderMode.clear();
	}
	
	//COLOR
	public void enableColorMode(float r, float g, float b, float a)
	{
		if (!renderMode.contains(RenderMode.COLOR))
			renderMode.add(RenderMode.COLOR);
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
	
	public void disableColorMode()
	{
		if (renderMode.contains(RenderMode.COLOR))
			renderMode.remove(RenderMode.COLOR);
	}
	
	//GRADIENT
	public void enableGradientMode(float[] color)
	{
		if (!renderMode.contains(RenderMode.GRADIENT))
			renderMode.add(RenderMode.GRADIENT);
		updateGradient(color);
	}
	
	public void updateGradient(float[] color)
	{
			this.color = color;
			this.colorBuffer = setBuffer(colorBuffer, color);
			needToUpdateGradientVBO = true;
	}
	
	public void disableGradientMode()
	{
		if (renderMode.contains(RenderMode.GRADIENT))
			renderMode.remove(RenderMode.GRADIENT);
	}
	
	//TEXTURE
	public void enableTextureMode(Texture tex)
	{
		if (!renderMode.contains(RenderMode.TEXTURE))
			renderMode.add(RenderMode.TEXTURE);
		updateTexture(tex);
	}
	
	public void enableTextureMode(Texture tex, float[] texture)
	{
		if (!renderMode.contains(RenderMode.TEXTURE))
			renderMode.add(RenderMode.TEXTURE);
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
		if (renderMode.contains(RenderMode.TEXTURE))
			renderMode.remove(RenderMode.TEXTURE);
	}
	
	//TILESET
	public void enableTilesetMode(Texture tex, int x, int y)
	{
		if (!renderMode.contains(RenderMode.TILESET))
			renderMode.add(RenderMode.TILESET);
		updateTileset(tex, x, y);
	}
	
	public void enableTilesetMode(Texture tex, int tileID)
	{
		if (!renderMode.contains(RenderMode.TILESET))
			renderMode.add(RenderMode.TILESET);
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
		if (renderMode.contains(RenderMode.TILESET))
			renderMode.remove(RenderMode.TILESET);
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
				color = endColor;
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
	
	public  void initGradientInterp(float[] c)
	{
		endColor = c;
		isGradientInterp = true;
	}
	
	public void gradientInterp()
	{
		if (isGradientInterp)
		{
			final float gradientInterp = colorInterpSpeed / 1000 * Stopwatch.getFrameTime();
			int nearCount = 0;
			for (int i = 0; i < color.length; i++)
			{
				if (Math.abs(endColor[i] - color[i]) < gradientInterp)
				{
					nearCount++;
					color[i] = endColor[i];
				}
				else if (endColor[i] > color[i])
					color[i] += gradientInterp;
				else
					color[i] -= gradientInterp;
			}
			
			if (nearCount == color.length)
				isGradientInterp = false;
			
			colorBuffer = setBuffer(colorBuffer, color);
			needToUpdateGradientVBO = true;
		}
	}
	
	/*************
	 * VBO Stuph *
	 *************/
	
	public void resetAllBuffers()
    {
        if (shape.getVertices() != null)
            vertexBuffer = setBuffer(vertexBuffer, shape.getVertices());
        if (color != null)
            colorBuffer = setBuffer(colorBuffer, color);
        if (texture != null)
            textureBuffer = setBuffer(textureBuffer, texture);
    }
    
    public static void resetIndexBuffer()
    {
        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }
    
    public void genHardwareBuffers(GL10 gl)
    {
        if (gl instanceof GL11)
        {
            
            final GL11 gl11 = (GL11)gl;
            
            int[] tempPtr = new int[1];
            
            //VERTEX
            gl11.glGenBuffers(1, tempPtr, 0);
            VBOVertPtr = tempPtr[0];
            
            gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBOVertPtr);
            final int vertSize = vertexBuffer.capacity() * 4;
            gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vertSize, vertexBuffer, GL11.GL_STATIC_DRAW); //\TODO choose static/draw settings..?
            
            if (renderMode.contains(RenderMode.GRADIENT))
            {
                gl11.glGenBuffers(1, tempPtr, 0);
                VBOGradientPtr = tempPtr[0];
                needToUpdateGradientVBO = true;
                updateGradientVBO(gl);
            }
            if (renderMode.contains(RenderMode.TEXTURE) || renderMode.contains(RenderMode.TILESET))
            {
                gl11.glGenBuffers(1, tempPtr, 0);
                VBOTexturePtr = tempPtr[0];
                needToUpdateTexVBO = true;
                updateTextureVBO(gl);
            }
            
            gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
            
            final int error = gl11.glGetError();
            if (error != GL11.GL_NO_ERROR)
            {
                Log.e("LDS_Game", "Buffers generate GL_ERROR: " + error);
            }
        }
    }
    
    public void updateVertexVBO(GL10 gl)
    {
        if (useVBOs && needToUpdateVertexVBO)
        {
            GL11 gl11 = (GL11)gl;
            gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBOVertPtr);
            final int vertexSize = vertexBuffer.capacity() * 4;
            gl11.glBufferData(GL11.GL_ARRAY_BUFFER, vertexSize, vertexBuffer, GL11.GL_STATIC_DRAW);
        }
        needToUpdateVertexVBO = false;
    }
    
    public void updateTextureVBO(GL10 gl)
    {
        if (useVBOs && needToUpdateTexVBO)
        {
            GL11 gl11 = (GL11)gl;
            gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBOTexturePtr);
            final int textureSize = textureBuffer.capacity() * 4;
            gl11.glBufferData(GL11.GL_ARRAY_BUFFER, textureSize, textureBuffer, GL11.GL_STATIC_DRAW);
        }
        needToUpdateTexVBO = false;
    }
    
    public void updateGradientVBO(GL10 gl)
    {
        if (useVBOs && needToUpdateGradientVBO)
        {
            GL11 gl11 = (GL11)gl;
            gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, VBOGradientPtr);
            final int gradientSize = colorBuffer.capacity() * 4;
            gl11.glBufferData(GL11.GL_ARRAY_BUFFER, gradientSize, colorBuffer, GL11.GL_STATIC_DRAW);
        }
        needToUpdateGradientVBO = false;
    }
    
    public static void genIndexBuffer(GL10 gl)
    {
        if (gl instanceof GL11)
        {
            useVBOs = true;
            
            GL11 gl11 = (GL11)gl;
            int[] tempPtr = new int[1];
            
            gl11.glGenBuffers(1, tempPtr, 0);
            VBOIndexPtr = tempPtr[0];
            
            gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, VBOIndexPtr);
            final int indexSize = indexBuffer.capacity();
            gl11.glBufferData(GL11.GL_ELEMENT_ARRAY_BUFFER, indexSize, indexBuffer, GL11.GL_STATIC_DRAW);
            gl11.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
            
            final int error = gl11.glGetError();
            if (error != GL11.GL_NO_ERROR)
            {
                Log.e("LDS_Game", "Index buffer generates GL_ERROR: " + error);
            }
        }
    }
    
    public void freeHardwareBuffers(GL10 gl)
    {
        if (useVBOs)
        {
            GL11 gl11 = (GL11)gl;
            int[] buffer = new int[3];
            buffer[0] = VBOVertPtr;
            buffer[1] = VBOTexturePtr;
            buffer[2] = VBOGradientPtr;
            
            gl11.glDeleteBuffers(3, buffer, 0);
            
        }
    }
	
	/**************************
	 * Accessors and Mutators *
	 **************************/
	
	public Shape getShape()
    {   
	    return shape;
	}
	
	public Vector2 getPos()
	{
	    return shape.getPos();
	}
	
	public float getXPos()
	{
	    return getPos().getX();
	}
	
	public float getYPos()
	{
	    return getPos().getY();
	}
	
	public void setPos(Vector2 position)
	{
	    shape.setPos(position);
	}
	
	public float getAngle()
	{
	    return shape.getAngle();
	}
	
	public void setAngle(float angle)
	{
	    shape.setAngle(angle);
	}
	
	public Vector2 getScale()
	{
	    return shape.getScale();
	}
	
	public void setScale(Vector2 scale)
	{
	    shape.setScale(scale);
	}
	
	public float getXScale()
	{
	    return getScale().getX();
	}
	
	public float getYScale()
	{
	    return getScale().getY();
	}

    public Vector4 getColor()
    {
        return colorVec;
    }

    public float[] getColorCoords()
    {
        return color;
    }

    public float[] getTextureCoords()
    {
        return texture;
    }

    public Texture getTexture()
    {
        return tex;
    }

    public EnumSet<RenderMode> getRenderMode()
    {
        return renderMode;
    }

    public int getVertexVBO()
    {
        return VBOVertPtr;
    }

    public int getTextureVBO()
    {
        return VBOTexturePtr;
    }

    public int getGradientVBO()
    {
        return VBOGradientPtr;
    }
	
	public void setColorInterpSpeed(float s)
	{
	    this.colorInterpSpeed = s;
	}
}
