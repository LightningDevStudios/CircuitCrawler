package com.lds.game;

import java.util.ArrayList;

public class Inventory 
{
	private static ArrayList<String> inventory;
	
	private Inventory ()
	{
		inventory = new ArrayList<String>();
	}
	
	public static void add (String pickup)
	{
		inventory.add(pickup);
	}
	
	public static boolean remove (String pickup)
	{
		return inventory.remove(pickup);
	}
}
