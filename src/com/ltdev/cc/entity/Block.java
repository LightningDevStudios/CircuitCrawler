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

import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.graphics.TextureManager;
import com.ltdev.math.Vector2;

/**
 * A Block is a basic square that can be held and thrown.
 * @author Lightning Development Studios
 */
public class Block extends HoldObject
{
    /**
     * Initializes a new instance of the Block class.
     * @param size The size of the Block.
     * @param position The position of the Block.
     */
    public Block(float size, Vector2 position)
    {
        super(new Rectangle(new Vector2(size, size), position, true));
        this.tex = TextureManager.getTexture("tilesetentities");
        this.tilesetX = 3;
        this.tilesetY = 0;
    }
}
