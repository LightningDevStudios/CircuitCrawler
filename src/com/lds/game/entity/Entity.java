package com.lds.game.entity;

import android.util.Log;

import com.lds.EntityManager;
import com.lds.Enums.RenderMode;
import com.lds.Stopwatch;
import com.lds.Texture;
import com.lds.TilesetHelper;
import com.lds.game.Game;
import com.lds.math.*;
import com.lds.math.Vector2;
import com.lds.physics.Shape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
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
	//TODO: remove most of this
	protected float colorR, colorG, colorB, colorA;
	protected float endColorR, endColorG, endColorB, endColorA;
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
	
	//debug data
	private int entID;
	private static int entCount = 0;
	
	public Entity(Shape shape)
	{
		//initialize debug data
		entID = entCount;
		entCount++;
		
		this.shape = shape;
		this.vertexBuffer = setBuffer(vertexBuffer, shape.getVertices());
		renderMode = EnumSet.noneOf(RenderMode.class);
	}
	
	public void draw(GL10 gl)
	{
		gl.glLoadMatrixf(shape.getModel().array(), 0);
		
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
		    gl.glColor4f(colorR, colorG, colorB, colorA);
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
			
	public void remove()
	{
		EntityManager.removeEntity(this);
	}
	
	public void update()
	{
	    //TODO: change this?
		if (shape.getScale().length() <= 0.0f)
			this.remove(); //remove the entity
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
	
	public boolean doesCollide(Entity ent)
	{
		return ent.willCollide();
	}

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
		
		float angleDiff = angle - angleBetween;
		
		if (angleDiff > 315.0f)
			angleDiff -= 360.0f;
		
		return angleDiff > -45 && angleDiff < 45;
	}
	
	protected boolean isCircleCollidingWithCircle(Entity ent) //if both entities are circles
	{
		boolean output;
		if (Vector2.subtract(this.posVec, ent.posVec).length() < halfSize + ent.halfSize)
			output = true;
		else
			output = false;
		
		if (output && this.doesCollide(ent) && ent.doesCollide(this))
		{
			this.circleBounceAgainstCircle(ent);
			ent.circleBounceAgainstCircle(this);
		}
		return output;
	}

	protected boolean isRectangleCollidingWithCircle(Entity ent) //if only ent is a circle
	{
		boolean output = true;
		this.updateAbsolutePointLocations();
		ent.updateAbsolutePointLocations();
		
		Vector2[] axes = new Vector2[3];
		//set rectangle-based axes
		axes[0] = Vector2.abs(Vector2.subtract(vertVecs[0], this.vertVecs[1]));
		axes[1] = Vector2.getNormal(axes[0]);
		//set circle axis
		axes[2] = Vector2.subtract(vertVecs[0], ent.posVec);
		for (int i = 1; i < vertVecs.length; i++)
		{
			Vector2 tempVec = Vector2.subtract(vertVecs[i], ent.posVec);
			if (axes[2].length() > tempVec.length())
			{
				axes[2].copy(tempVec);
			}
		}
		
		for (Vector2 axis : axes)
		{
			axis.normalize();
						
			//get mins and maxes for rectangle
			float min1 = axis.dot(this.vertVecs[0]);
			float max1 = min1;
			for (int i = 1; i < this.vertVecs.length; i++)
			{
				float dotProd1 = axis.dot(this.vertVecs[i]);
				if (dotProd1 > max1)
					max1 = dotProd1;
				if (dotProd1 < min1)
					min1 = dotProd1;
			}
			
			//get mins and maxes for circle 
			float min2 = axis.dot(Vector2.add(ent.posVec, Vector2.scale(axis, ent.halfSize)));
			float max2 = axis.dot(Vector2.subtract(ent.posVec, Vector2.scale(axis, ent.halfSize)));
			if (min2 > max2)
			{
				float temp = min2;
				min2 = max2;
				max2 = temp;
			}
			
			if ((max1 > max2 || max1 < min2) && (max2 > max1 || max2 < min1))
			{
				output = false;
			}
		}
		if (output && this.doesCollide(ent) && ent.doesCollide(this))
		{
			this.rectangleBounceAgainstCircle(ent);
			ent.circleBounceAgainstRectangle(this);
		}
		return output;
	}
	
	protected boolean isRectangleCollidingWithRectangle(Entity ent) //if both entities are circles
	{
		boolean output = true;
		this.updateAbsolutePointLocations();
		ent.updateAbsolutePointLocations();
		
		Vector2[] axes = new Vector2[4];
		axes[0] = Vector2.abs(Vector2.subtract(this.vertVecs[0], this.vertVecs[1]));
		axes[1] = Vector2.getNormal(axes[0]);
		axes[2] = Vector2.abs(Vector2.subtract(ent.vertVecs[0], ent.vertVecs[1]));
		axes[3] = Vector2.getNormal(axes[2]);
		
		for (Vector2 axis : axes)
		{
			axis.normalize();
						
			//get mins and maxes for first entity
			float min1 = axis.dot(this.vertVecs[0]);
			float max1 = min1;
			for (int i = 1; i < this.vertVecs.length; i++)
			{
				float dotProd1 = axis.dot(this.vertVecs[i]);
				if (dotProd1 > max1)
					max1 = dotProd1;
				if (dotProd1 < min1)
					min1 = dotProd1;
			}
			
			//get mins and maxes for second entity
			float min2 = axis.dot(ent.vertVecs[0]);
			float max2 = min2;
			for (int i = 1; i < ent.vertVecs.length; i++)
			{
				float dotProd2 = axis.dot(ent.vertVecs[i]);
				if (dotProd2 > max2)
					max2 = dotProd2;
				if (dotProd2 < min2)
					min2 = dotProd2;
			}
			
			if ((max1 > max2 || max1 < min2) && (max2 > max1 || max2 < min1))
			{	
				output = false;
			}
		}
		if (output && this.doesCollide(ent) && ent.doesCollide(this))
		{
			this.rectangleBounceAgainstRectangle(ent);
			ent.rectangleBounceAgainstRectangle(this);
		}
		return output;
	}
	
	/**
	 * Blank method, overridden by PhysEnt.
	 * @param ent An entity to collide with.
	 */
	public void circleBounceAgainstCircle(Entity ent) {}
	
	/**
	 * Blank method, overridden by PhysEnt.
	 * @param ent An entity to collide with.
	 */
	public void circleBounceAgainstRectangle(Entity ent) {}
	
	/**
	 * Blank method, overridden by PhysEnt.
	 * @param ent An entity to collide with.
	 */
	public void rectangleBounceAgainstCircle(Entity ent) {}
	
	/**
	 * Blank method, overridden by PhysEnt.
	 * @param ent An entity to collide with.
	 */
	public void rectangleBounceAgainstRectangle(Entity ent) {}
	
	/**
	 * Overriden for entity interaction.
	 * @param ent The entity to interact with.
	 */
	public void interact(Entity ent) {}
	
	/**
	 * Overriden for entity uninteraction.
	 * @param ent The entity that was interacted with.
	 */
	public void uninteract(Entity ent) {}
		
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
			colorR = r;
			colorG = g;
			colorB = b;
			colorA = a;
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
		endColorR = r;
		endColorG = g;
		endColorB = b;
		endColorA = a;
		isColorInterp = true;
	}
	 
	public void colorInterp()
	{
		if (isColorInterp)
		{
			final float colorInterp = colorInterpSpeed / 1000 * Stopwatch.getFrameTime();
			final double rNear = Math.abs(endColorR - colorR);
			final double gNear = Math.abs(endColorG - colorG);
			final double bNear = Math.abs(endColorB - colorB);
			final double aNear = Math.abs(endColorA - colorA);
			if (rNear < colorInterp && gNear < colorInterp && bNear < colorInterp && aNear < colorInterp)
			{
				colorR = endColorR;
				colorG = endColorG;
				colorB = endColorB;
				colorA = endColorA;
				isColorInterp = false;
			}
			else
			{
				if (endColorR > colorR)	colorR += colorInterp;
				else					colorR -= colorInterp;
				
				if (endColorG > colorG)	colorG += colorInterp;
				else					colorG -= colorInterp;
				
				if (endColorB > colorB)	colorB += colorInterp;
				else					colorB -= colorInterp;
				
				if (endColorA > colorA)	colorA += colorInterp;
				else					colorA -= colorInterp;
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
	
	public boolean containsPoint(Vector2 v)
	{
		boolean output = true;
		this.updateAbsolutePointLocations();
		Vector2 axis = Vector2.subtract(this.vertVecs[0], this.vertVecs[1]).abs();
		
		for (int i = 0; i < 2; i++)
		{
			//get mins and maxes for entity
			float min = axis.dot(this.vertVecs[0]);
			float max = min;
			for (int j = 1; j < this.vertVecs.length; j++)
			{
				float dotProd = axis.dot(this.vertVecs[j]);
				if (dotProd > max)
					max = dotProd;
				if (dotProd < min)
					min = dotProd;
			}
			
			//gets projection of vector
			float projection = axis.dot(v);
			
			if (projection < min || projection > max)
				output = false;
			
			axis.setNormal();
		}
		return output;
	}
	
	/**************************
	 * Accessors and Mutators *
	 **************************/
	
	public Shape getShape()
	{
	    return shape;
	}

    public float getColorR()
    {
        return colorR;
    }

    public float getColorG()
    {
        return colorG;
    }

    public float getColorB()
    {
        return colorB;
    }

    public float getColorA()
    {
        return colorA;
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


	
	public void resetAllBuffers()
	{
		if (vertices != null)
			vertexBuffer = setBuffer(vertexBuffer, vertices);
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
}
