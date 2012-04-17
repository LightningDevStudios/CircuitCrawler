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

import com.ltdev.cc.entity.LaserShooter;
import com.ltdev.cc.entity.Entity;
import com.ltdev.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class LaserShooterData extends EntityData
{
    private float stupidity;
    private float beamWidth;
    private float shotsPerSecond;
    
    private LaserShooter laserShooterRef;
    
    public LaserShooterData(HashMap<String, String> laserShooterHM)
    {
        super(laserShooterHM);
        
        if (laserShooterHM.get("stupidity") != null)
            stupidity = Float.parseFloat(laserShooterHM.get("stupidity"));
        
        if (laserShooterHM.get("beamWidth") != null)
            beamWidth = Float.parseFloat(laserShooterHM.get("beamWidth"));
        
        if (laserShooterHM.get("shotsPerSecond") != null)
                shotsPerSecond = Float.parseFloat(laserShooterHM.get("shotsPerSecond"));
    }
    
    /**
     * \bug Cannons will crash the game. Restructure the parser to fix this.
     */
    public void createInst(ArrayList<Entity> entData)
    {
        laserShooterRef = new LaserShooter(size, new Vector2(xPos, yPos), angle, stupidity, beamWidth, shotsPerSecond, null, null);
       
        laserShooterRef.setTexture(tex);
        
        entData.add(laserShooterRef);
        ent = laserShooterRef;
    }
}
