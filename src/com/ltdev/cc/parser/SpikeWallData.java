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

package com.ltdev.cc.parser;

import com.ltdev.Direction;
import com.ltdev.cc.entity.Entity;
import com.ltdev.cc.entity.SpikeWall;
import com.ltdev.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class SpikeWallData extends EntityData
{
    private Direction dir;
    
    private SpikeWall spikeWallRef;
    
    public SpikeWallData(HashMap<String, String> spikeWallHM)
    {
        super(spikeWallHM);
        
        String dirName = spikeWallHM.get("dir");
        if (dirName != null)
        {
            if (dirName.equals("LEFT"))
                dir = Direction.LEFT;
            else if (dirName.equals("RIGHT"))
                dir = Direction.RIGHT;
            else if (dirName.equals("UP"))
                dir = Direction.UP;
            else if (dirName.equals("DOWN"))
                dir = Direction.DOWN;
        }
        else
            dir = Direction.LEFT;
    }
    
    public void createInst(ArrayList<Entity> entData)
    {
        spikeWallRef = new SpikeWall(size, new Vector2(xPos, yPos), dir);
        spikeWallRef.setAngle(angle);
     
        entData.add(spikeWallRef);
        ent = spikeWallRef;
    }
}
