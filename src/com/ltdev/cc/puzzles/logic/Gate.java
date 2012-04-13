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

package com.ltdev.cc.puzzles.logic;

public class Gate 
{
    /**
     * An enum of the different types of logic gates.
     * @author Lightning Development Studios
     */
    public enum GateType
    {
        NONE,
        AND,
        OR,
        XOR,
        NOT,
        NAND,
        NOR,
        XNOR
    }
    
	protected GateType type;
	protected Gate[] input;
	protected Gate output;
	
	private boolean currentState;
	
	public Gate(GateType type, float posX, float posY)
	{
		this.type = type;
		
		switch (type)
		{
			case AND:			
			case OR:
			case XOR:
			case NAND:
			case NOR:
			case XNOR:
				input = new Gate[2];
				break;		
			
			case NOT:
				input = new Gate[1];
				//false
				break;
        default:
            break;
		}
	}

	public boolean getState()
	{
		return currentState;
	}
	public void update()
	{
		boolean[] inputStates = new boolean[input.length];
		
		for (int i = 0; i < input.length; i++)
		{
			inputStates[i] = input[i].getState();
		}
		
		switch (type)
		{
			case AND:
				if (inputStates[0] && inputStates[1])
					currentState = true;
				else
					currentState = false;
				break;
				
			case OR:
				if (inputStates[0] || inputStates[1])
					currentState = true;
				else
					currentState = false;
				break;
				
			case XOR:
				if (inputStates[0] != inputStates[1])
					currentState = true;
				else
					currentState = false;
				break;
				
			case NAND:
				if (!inputStates[0] || !inputStates[1])
					currentState = true;
				else
					currentState = false;
				break;
				
			case NOR:
				if (!(inputStates[0] || inputStates[1]))
					currentState = true;
				else
					currentState = false;
				break;
				
			case XNOR:
				if (inputStates[0] == inputStates[1])
					currentState = true;
				else
					currentState = false;
				break;
				
			case NOT:
				if (!inputStates[0])
					currentState = true;
				else
					currentState = false;
				break;
			default:
		}
		
		output.update();
	}
}
