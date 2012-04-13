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
