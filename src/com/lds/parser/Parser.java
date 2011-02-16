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
 
public class Parser //this is a perser
{
	public HashMap<String, String> attributes;
	
	public ArrayList<EntityData> parsedList = new ArrayList<EntityData>();
	
	public XmlResourceParser xrp;  
	
	int x, y, curx, cury;

	public Parser(Context context)
	{
		xrp = context.getResources().getXml(R.xml.tempdata);
		attributes = new HashMap<String, String>();
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
				else if(xrp.getName().equals("Tileset"))
					parseTileset();
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
	
	public void parseTileset() throws XmlPullParserException, IOException
	{
		HashMap<String, String> tileHashMap = new HashMap<String, String>();
		x = Integer.parseInt(attributes.get("x"));
		y = Integer.parseInt(attributes.get("y"));
		
		TileData[][] tdma = new TileData[y][x];
		
		curx = -1;
		cury = -1;
		while(!(xrp.getEventType() == xrp.END_TAG && xrp.getName().equals("Tileset")))
		{
			parseAttributes();
			cury++;
			
			xrp.next();
			while(!(xrp.getEventType() == xrp.END_TAG && xrp.getName().equals("TileRow")))
			{
				curx++;
				xrp.next();
				while(!(xrp.getEventType() == xrp.END_TAG && xrp.getName().equals("Tile")))
				{
					xrp.next();
					String tempTag = xrp.getName();
					xrp.next();
					tileHashMap.put(tempTag, xrp.getText());
					
					tdma[cury][curx] = new TileData(tileHashMap);
					
					xrp.next();
					xrp.next();
				}
			}
			curx = -1;
		}
		cury = -1;
	}
	
	public void parseAttributes()
	{
		String tag = xrp.getName();
		tag = tag.substring(tag.indexOf(" "));
		while(tag.indexOf(" ") < 0)
		{
			attributes.put(tag.substring(1, tag.indexOf("=") - 1), tag.substring((tag.indexOf("=") + 1), tag.indexOf(" ")));
			tag = tag.trim();
			tag = tag.substring(tag.indexOf(" "));
		}
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