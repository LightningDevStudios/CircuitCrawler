package com.lds.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import android.content.res.XmlResourceParser;

import com.lds.game.R;
import com.lds.game.entity.Entity;
import com.lds.game.entity.PhysBlock;
 
public class Parser
{
	
	public ArrayList<EntityData> parsedList = new ArrayList<EntityData>();
	
	public XmlResourceParser xrp;  

	public Parser(Context context)
	{
		xrp = context.getResources().getXml(R.xml.tempdata);
	}
	
	public void parseLevel()
		throws XmlPullParserException, IOException
	{
	
		while (xrp.getEventType() != xrp.END_DOCUMENT)
		{
			if (xrp.getEventType() == xrp.START_TAG)
			{
				System.out.println(xrp.getName());
				if(xrp.getName().equals("Entity"))
					parseEntity();
			}
			xrp.next();
		}
	}
	
	public void parseEntity() throws XmlPullParserException, IOException
	{
		xrp.next();
		
		
		HashMap <String, String> dataHashMap = new HashMap<String, String>();		
		
		while (!((xrp.getEventType() == xrp.END_TAG && xrp.getName().equals("Entity")))) 
		{
			String tempDataType = null;

			tempDataType = xrp.getName();
			
			xrp.next();

			dataHashMap.put(tempDataType, xrp.getText());
			
				
			xrp.next(); 
			xrp.next();
		}
		
		
		PhysBlockData pbd = new PhysBlockData(dataHashMap);
		parsedList.add(pbd);
	}
	
	public ArrayList<Entity> convertDataToEnts()
	{
		ArrayList<Entity> entList = new ArrayList<Entity>();
		
		for(EntityData ent : parsedList)
		{
			if (ent instanceof PhysBlockData)
			{
				PhysBlock phy = new PhysBlock(ent.getSize(), ent.getXPos(), ent.getYPos());
				entList.add(phy);
			}
		}
		
		return entList;
	}
}