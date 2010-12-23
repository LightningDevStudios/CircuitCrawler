package com.lds.test;

public class CircuitPuzzle implements IPuzzle
{
	public CircuitPuzzle()
	{
		System.out.print("Loaded!");
	}
	boolean puzzleWon = false;
	
	public boolean end() 
	{
		System.out.println("Finished, " + puzzleWon);
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