package com.lds.puzzles.circuit;
import com.lds.puzzle.IPuzzle;

public class CircuitPuzzle implements IPuzzle
{
	boolean puzzleWon = false;
	
	public boolean end() 
	{
		System.out.println("Finished, true");
		return puzzleWon;
	}

	public void init() 
	{
		System.out.println("initializing...");		
	}

	public void run() 
	{
		for (int i = 0; i < 10; i++)
		{
			System.out.println(i);
		}
	}
}