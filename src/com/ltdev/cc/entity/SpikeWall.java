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

import com.ltdev.Direction;
import com.ltdev.Stopwatch;
import com.ltdev.cc.models.SpikeWallData;
import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.graphics.TextureManager;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

public class SpikeWall extends Entity
{	
    private boolean extended;
    private Vector2 targetPos, initialPos, endPos;
    
    /**
     * Initializes a new instance of the SpikeWall class.
     * @param size The size of the SpikeWall.
     * @param position The position of the SpikeWall.
     * @param dir The direction the SpikeWall will move in.
     */
	public SpikeWall(float size, Vector2 position, Direction dir)
    {
        super(new Rectangle(new Vector2(size, size), position, 270, true));
        shape.setStatic(true);
        
        extended = false;
        initialPos = position;
        switch (dir)
        {
            case LEFT:
                endPos = new Vector2(position.x() - 72, position.y());
                shape.setAngle(270);
                break;
            case RIGHT:
                endPos = new Vector2(position.x() + 72, position.y());
                shape.setAngle(90);
                break;
            case DOWN:
                endPos = new Vector2(position.x(), position.y() - 72);
                shape.setAngle(0);
                break;
            case UP:
                endPos = new Vector2(position.x() + 72, position.y() + 72);
                shape.setAngle(180);
                break;
            default:
                endPos = position;
                shape.setAngle(0);
                break;
        }

        this.tex = TextureManager.getTexture("tilesetentities");
        this.tilesetX = 3;
        this.tilesetY = 1;
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
        
        //draw the entity.
        gl.glDrawArrays(GL11.GL_TRIANGLES, 0, SpikeWallData.VERTEX_COUNT);
        
        //TODO also temporary
        //gl.glDisable(GL11.GL_LIGHT0);
        //gl.glDisable(GL11.GL_LIGHTING);
        gl.glDisableClientState(GL11.GL_NORMAL_ARRAY);
    }
	
	@Override
	public void update(GL11 gl)
	{
	    super.update(gl);
	    
	    float frameTime = (float)Stopwatch.getFrameTime() / 1000f;

        if (extended)
            targetPos = Vector2.add(shape.getPos(), Vector2.scale(Vector2.subtract(initialPos, shape.getPos()), frameTime));
        else
            targetPos = Vector2.add(shape.getPos(), Vector2.scale(Vector2.subtract(endPos, shape.getPos()), frameTime * 10));
        shape.setPos(targetPos);
        
        if (targetPos.approxEquals(initialPos, 2))
            extended = false;
        if (targetPos.approxEquals(endPos, 1))
            extended = true;
	}
	
	@Override
    public void initialize(GL11 gl)
    {
        vbo = SpikeWallData.getBufferId(gl);        
        this.tex = TextureManager.getTexture("spikewall");
    }
	
	@Override
	public void interact(Entity ent)
	{
	    super.interact(ent);
	    
	    if (ent instanceof Player)
	        ((Player)ent).kill();
	}
}
