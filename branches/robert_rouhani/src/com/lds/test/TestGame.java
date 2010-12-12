package com.lds.test;

//import com.lds.test.CircuitPuzzle;
//import com.lds.puzzles.circuit.*;
//import java.lang.reflect.*;

public class TestGame {

	public static void main(String[] args)
	{
		String className = "com.lds.test.CircuitPuzzle";
		IPuzzle currentPuzzle = null;
		try 
		{
			currentPuzzle = (IPuzzle)( Class.forName(className, true, Thread.currentThread().getContextClassLoader()).newInstance() );
		} 
		catch (InstantiationException e) { e.printStackTrace(); }
		catch (IllegalAccessException e) { e.printStackTrace(); }
		catch (ClassNotFoundException e) { e.printStackTrace(); }
		
		currentPuzzle.init();
		currentPuzzle.run();
		System.out.println(currentPuzzle.end());
	}

}
