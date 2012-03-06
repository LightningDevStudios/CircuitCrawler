package com.ltdev;

import javax.microedition.khronos.opengles.GL11;

import com.ltdev.math.*;

/**
 * A class that contains all the information for an OpenGL light and automatically updates it.
 * @author Lightning Development Studios
 */
public class LightInfo
{
    private boolean enabled;
    private int index;
    
    private Vector4 ambient;
    private Vector4 diffuse;
    private Vector4 position;
    private float constantAttenuation;
    private float linearAttenuation;
    private float quadraticAttenuation;
    
    /**
     * Initializes a new instance of the LightInfo class.
     * @param index The light's index (must be between 0 and 8).
     * @param ambient The ambient color/intensity of the light.
     * @param diffuse The diffuse color/intensity of the light.
     * @param position The location of the light in world coordinates.
     * @param constantAttenuation The constant light falloff.
     * @param linearAttenuation The linear light falloff.
     * @param quadraticAttenuation The quadratic light falloff.
     */
    public LightInfo(int index, Vector4 ambient, Vector4 diffuse, Vector4 position, float constantAttenuation, float linearAttenuation, float quadraticAttenuation)
    {
        this.index = index;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.position = position;
        this.constantAttenuation = constantAttenuation;
        this.linearAttenuation = linearAttenuation;
        this.quadraticAttenuation = quadraticAttenuation;
        
        this.enabled = true;
    }
    
    /**
     * Initializes a new instance of the LightInfo class. Sets the OpenGL default parameter values.
     * @param index The light's index (must be between 0 and 8).
     */
    public LightInfo(int index)
    {
        this.index = index;
        this.ambient = Vector4.UNIT_W;
        this.position = Vector4.UNIT_Z;
        this.constantAttenuation = 1;
        
        if (index == 0)
            this.diffuse = Vector4.ONE;
        else
            this.diffuse = Vector4.UNIT_W;
    }
    
    /**
     * Enables the light.
     */
    public void enable()
    {
        this.enabled = true;
    }
    
    /**
     * Disables the light.
     */
    public void disable()
    {
        this.enabled = false;
    }
    
    /**
     * Gets the enabled state of the light.
     * @return A value indicating whether the light is enabled or not.
     */
    public boolean getEnableState()
    {
        return enabled;
    }
    
    /**
     * Gets the ambient color/intensity of the light.
     * @return The ambient vector.
     */
    public Vector4 getAmbient()
    {
        return ambient;
    }
    
    /**
     * Gets the diffuse color/intensity of the light.
     * @return The diffuse vector.
     */
    public Vector4 getDiffuse()
    {
        return diffuse;
    }
    
    /**
     * Gets the position of the light.
     * @return The position vector.
     */
    public Vector4 getPosition()
    {
        return position;
    }
    
    /**
     * Gets the constant light falloff.
     * @return The constant attenuation of the light.
     */
    public float getConstantAttenuation()
    {
        return constantAttenuation;
    }
    
    /**
     * Gets the linear light falloff.
     * @return The linear attenuation of the light.
     */
    public float getLinearAttenuation()
    {
        return linearAttenuation;
    }
    
    /**
     * Gets the quadratic light falloff.
     * @return The quadratic attenuation of the light.
     */
    public float getQuadraticAttenuation()
    {
        return quadraticAttenuation;
    }
    
    /**
     * Sets the light's ambient vector.
     * @param gl The OpenGL context.
     * @param ambient The new ambient vector.
     */
    public void setAmbient(GL11 gl, Vector4 ambient)
    {
        this.ambient = ambient;
        gl.glLightfv(GL11.GL_LIGHT0 + index, GL11.GL_AMBIENT, ambient.array(), 0);
    }
    
    /**
     * Sets the light's diffuse vector.
     * @param gl The OpenGL context.
     * @param diffuse The new diffuse vector.
     */
    public void setDiffuse(GL11 gl, Vector4 diffuse)
    {
        this.diffuse = diffuse;
        gl.glLightfv(GL11.GL_LIGHT0 + index, GL11.GL_DIFFUSE, diffuse.array(), 0);
    }
    
    /**
     * Sets the light's position vector.
     * @param gl The OpenGL context.
     * @param position The new position vector.
     */
    public void setPosition(GL11 gl, Vector4 position)
    {
        this.position = position;
        gl.glLightfv(GL11.GL_LIGHT0 + index, GL11.GL_POSITION, position.array(), 0);
    }
    
    /**
     * Sets the light's constant falloff.
     * @param gl The OpenGL context.
     * @param constantAttenuation The new constant attenuation.
     */
    public void setConstantAttenuation(GL11 gl, float constantAttenuation)
    {
        this.constantAttenuation = constantAttenuation;
        gl.glLightf(GL11.GL_LIGHT0 + index, GL11.GL_CONSTANT_ATTENUATION, constantAttenuation);
    }
    
    /**
     * Sets the light's linear falloff.
     * @param gl The OpenGL context.
     * @param linearAttenuation The new linear attenuation.
     */
    public void setLinearAttenuation(GL11 gl, float linearAttenuation)
    {
        this.linearAttenuation = linearAttenuation;
        gl.glLightf(GL11.GL_LIGHT0 + index, GL11.GL_LINEAR_ATTENUATION, linearAttenuation);
    }
    
    /**
     * Sets the light's quadratic falloff.
     * @param gl The OpenGL context.
     * @param quadraticAttenuation The new quadratic attenuation.
     */
    public void setQuadraticAttenuation(GL11 gl, float quadraticAttenuation)
    {
        this.quadraticAttenuation = quadraticAttenuation;
        gl.glLightf(GL11.GL_LIGHT0 + index, GL11.GL_QUADRATIC_ATTENUATION, quadraticAttenuation);
    }
}
