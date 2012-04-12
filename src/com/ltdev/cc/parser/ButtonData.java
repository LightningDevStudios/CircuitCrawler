package com.ltdev.cc.parser;

import com.ltdev.cc.entity.Button;
import com.ltdev.cc.entity.Entity;
import com.ltdev.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class ButtonData extends StaticEntData
{
	private Button buttonRef;
	
	public ButtonData(HashMap<String, String> buttonHM)
	{
		super(buttonHM);
	}
	
	public void createInst(ArrayList<Entity> entData)
	{
		buttonRef = new Button(new Vector2(xPos, yPos));
		buttonRef.setAngle(angle);

		buttonRef.setTexture(tex);
		
		entData.add(buttonRef);
		ent = buttonRef;
	}
}
