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
			/*String tempDataType = null;

			tempDataType = xrp.getName();
			
			xrp.next();

			dataHashMap.put(tempDataType, xrp.getText());*/
			dataHashMap = parseTag(dataHashMap);
				
			xrp.next(); 
			xrp.next();
		}
		
		
		PhysBlockData pbd = new PhysBlockData(dataHashMap);
		parsedList.add(pbd);
	}
	
	public void parseTileset() throws XmlPullParserException, IOException
	{
		HashMap<String, String> tileHashMap = new HashMap<String, String>();
		int x, y, curX, curY;
		
		HashMap<String, String> attributes = parseAttributes();
		
		x = Integer.parseInt(attributes.get("x"));
		y = Integer.parseInt(attributes.get("y"));
		
		TileData[][] tileset = new TileData[y][x];
		
		curX = 0;
		curY = 0;
		
		while(!(xrp.getEventType() == xrp.END_TAG && xrp.getName().equals("Tileset")))
		{
			if(curX >= x)
				curX = 0;
			
			xrp.next();
			if (xrp.getEventType() == xrp.START_TAG && xrp.getName().equals("Tile"))
			{
				/*xrp.next();
				String tagName = xrp.getName();
				xrp.next();
				tileHashMap.put(tagName, xrp.getText());*/
				xrp.next();
				tileHashMap = parseTag(tileHashMap);
				
				tileset[curY][curX] = new TileData(tileHashMap, curX, curY, x, y);
			}
			
			else if(xrp.getEventType() == xrp.END_TAG)
			{
				if(xrp.getName().equals("Tile"))
					curX++;
				
				else if(xrp.getName().equals("TileRow"))
					curY++;
			}
		}
			/*curY++;
			
			xrp.next();
			(while(!(xrp.getEventType() == xrp.END_TAG && xrp.getName().equals("TileRow")))
			{
				curX++;
				xrp.next();
				while(!(xrp.getEventType() == xrp.END_TAG && xrp.getName().equals("Tile")))
				{
					xrp.next();
					String tempTag = xrp.getName();
					xrp.next();
					tileHashMap.put(tempTag, xrp.getText());
					
					tdma[curY][curX] = new TileData(tileHashMap, curX, curY, x, y);
					
					xrp.next();
					xrp.next();
				}
			}
			curX = -1;
		}
		curY = -1;*/
	}
	
	public HashMap<String, String> parseAttributes()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < xrp.getAttributeCount(); i++)
		{
			map.put(xrp.getAttributeName(i), xrp.getAttributeValue(i));
		}
		
		return map;
		/*String tag = xrp.getName();
		tag = tag.substring(tag.indexOf(" "));
		while(tag.indexOf(" ") < 0)
		{
			attributes.put(tag.substring(1, tag.indexOf("=") - 1), tag.substring((tag.indexOf("=") + 2), tag.indexOf("\"")));
			tag = tag.trim();
			tag = tag.substring(tag.indexOf(" "));
		}*/
	}
	
	public HashMap<String, String> parseTag(HashMap<String, String> map) throws XmlPullParserException, IOException
	{
		String tagName = xrp.getName();
		xrp.next();
		map.put(tagName, xrp.getText());
		
		return map;
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