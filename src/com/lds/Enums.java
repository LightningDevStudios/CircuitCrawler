package com.lds;

public class Enums 
{
	public static enum Direction  { UP, RIGHT, DOWN, LEFT }
	public static enum RenderMode { BLANK, COLOR, GRADIENT, TEXTURE, TILESET, TEXTUREALPHA, TILESETALPHA }
	public static enum TileStates { FLOOR, WALL, PIT, BRIDGE }
	public static enum UIPosition {	TOP(0), LEFT(1), BOTTOM(2), RIGHT(3), CENTER(4), TOPLEFT(5), TOPRIGHT(6), BOTTOMLEFT(7), BOTTOMRIGHT(8);
									private int value;
									
									private UIPosition(int value) { this.value = value; }
									public int getValue() { return value; }
								  }
	private Enums() 
	{

	}
}


