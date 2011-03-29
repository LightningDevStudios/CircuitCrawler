package com.lds.puzzles.logic;

import java.util.ArrayList;

import com.lds.Enums.GateType;

public class Gate 
{
	protected GateType type;
	protected Gate[] input;
	protected Gate output;
	
	private boolean currentState;
	
	public Gate(GateType type, float posX,float posY)
	{
		this.type = type;
		
		switch(type)
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
		}
	}

	public boolean getState()
	{
		return currentState;
	}
	public void update()
	{
		boolean[] inputStates = new boolean[input.length];
		
		for(int i = 0; i < input.length; i++)
		{
			inputStates[i] = input[i].getState();
		}
		
		switch(type)
		{
			case AND:
				if(inputStates[0] == true && inputStates[1] == true)
					currentState = true;
				else
					currentState = false;
				
			case OR:
				if(inputStates[0] == true || inputStates[1] == true)
					currentState = true;
				else
					currentState = false;
				
			case XOR:
				if(inputStates[0] != inputStates[1])
					currentState = true;
				else
					currentState = false;
				
			case NAND:
				if(inputStates[0] == false || inputStates[1] == false)
					currentState = true;
				else
					currentState = false;
				
			case NOR:
				if(!(inputStates[0] == true || inputStates[1] == true))
					currentState = true;
				else
					currentState = false;
				
			case XNOR:
				if(inputStates[0] == inputStates[1])
					currentState = true;
				else
					currentState = false;
				
			case NOT:
				if(!inputStates[0])
					currentState = true;
				else
					currentState = false;
		}
		
		output.update();
	}
}