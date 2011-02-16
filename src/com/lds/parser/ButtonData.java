package com.lds.parser;

import java.util.ArrayList;
import java.util.HashMap;
import com.lds.game.entity.Button;
import com.lds.game.entity.Entity;

public class ButtonData extends StaticEntData
{
	private Button buttonRef;
	public ButtonData(HashMap<String, String> buttonHM)
	{
		super(buttonHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		buttonRef = new Button(xPos, yPos);
		if (rgba != null)
			buttonRef.enableColorMode(rgba[0],rgba[1],rgba[2],rgba[3]);
		if (tex != null && xy != null)
			buttonRef.enableTilesetMode(tex, xy[1], xy[2]);
		entData.add(buttonRef);
	}
}
