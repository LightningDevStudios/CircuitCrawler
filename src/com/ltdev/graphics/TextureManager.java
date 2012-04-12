package com.ltdev.graphics;

import java.util.HashMap;

/**
 * Stores all the textures and gives any code access to the textures..
 * @author Lightning Development Studios
 *
 */
public final class TextureManager
{
    private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
    
    /**
     * Prevents initialization of static class.
     */
    private TextureManager()
    {
        
    }
    
    /**
     * Adds a texture to the manager for later lookup.
     * @param id A unique string identifier for the texture.
     * @param tex The texture to store.
     */
    public static void addTexture(String id, Texture tex)
    {
        textures.put(id, tex);
    }
    
    /**
     * Retrieve a texture from the manager using the unique string ID.
     * @param id The string ID.
     * @return The texture.
     */
    public static Texture getTexture(String id)
    {
        return textures.get(id);
    }
}
