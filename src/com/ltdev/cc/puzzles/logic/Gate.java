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
