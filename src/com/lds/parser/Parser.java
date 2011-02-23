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
	public ArrayList<Entity> entList = new ArrayList<Entity>();
	
	public XmlResourceParser xrp;  
	public HashMap <String, String> dataHashMap;

	public Parser(Context context, int res)
	{
		xrp = context.getResources().getXml(res);
	}
	
	public void parseLevel()
		throws XmlPullParserException, IOException
	{
	
		while (xrp.getEventType() != xrp.END_DOCUMENT)
		{
			if (xrp.getEventType() == xrp.START_TAG)
			{
				System.out.println(xrp.getName());
				if(xrp.getName().equalsIgnoreCase("Entity"))
					parseEntity();
				else if(xrp.getName().equalsIgnoreCase("Tileset"))
					parseTileset();
			}
			xrp.next();
		}
	}
	
	public void parseEntity() throws XmlPullParserException, IOException
	{
		dataHashMap = new HashMap<String, String>();
		xrp.next();
		while (!((xrp.getEventType() == xrp.END_TAG && xrp.getName().equals("Entity"))))
		{
			xrp.next();
		
			if(xrp.getEventType() == xrp.START_TAG && xrp.getName().equalsIgnoreCase("Door"))
			{
				parseObj("Door");
				DoorData dd = new DoorData(dataHashMap);
				dd.createInst(entList);
			}
			else if (xrp.getEventType() == xrp.START_TAG && xrp.getName().equalsIgnoreCase("Button"))
			{
				parseObj("Button");
				ButtonData bd = new ButtonData(dataHashMap);
				bd.createInst(entList);
			}
			else if (xrp.getEventType() == xrp.START_TAG && xrp.getName().equalsIgnoreCase("PhysBlock"))
			{
				parseObj("PhysBlock");
				PhysBlockData pbd = new PhysBlockData(dataHashMap);
				pbd.createInst(entList);
			}
			else if (xrp.getEventType() == xrp.START_TAG && xrp.getName().equalsIgnoreCase("Bloc"))
			{
				parseObj("Blob");
				BlobData bd = new BlobData(dataHashMap);
				bd.createInst(entList);
			}
			else if (xrp.getEventType() == xrp.START_TAG && xrp.getName().equalsIgnoreCase("PuzzleBox"))
			{
				parseObj("PuzzleBox");
				PuzzleBoxData pbd = new PuzzleBoxData(dataHashMap);
				pbd.createInst(entList);
			}
			else if (xrp.getEventType() == xrp.START_TAG && xrp.getName().equalsIgnoreCase("PhysCircle"))
			{
				parseObj("PhysCircle");
				PhysCircleData pcd= new PhysCircleData(dataHashMap);
				pcd.createInst(entList);
			}
			else if (xrp.getEventType() == xrp.START_TAG && xrp.getName().equalsIgnoreCase("Player"))
			{
				parseObj("Player");
				PlayerData pd = new PlayerData(dataHashMap);
				pd.createInst(entList);
			}
			else if (xrp.getEventType() == xrp.START_TAG && xrp.getName().equalsIgnoreCase("PickupHealth"))
			{
				parseObj("PickupHealth");
				PickupHealthData phd = new PickupHealthData(dataHashMap);
				phd.createInst(entList);
			}
	
		}
		
	}
	
	public void parseObj(String tn) throws XmlPullParserException, IOException
	{		
		
		xrp.next();
		
		while (!((xrp.getEventType() == xrp.END_TAG && xrp.getName().equalsIgnoreCase(tn)))) 
		{
			//dataHashMap = parseTag(dataHashMap);
			parseTag(dataHashMap);	
			
			xrp.next(); 
			xrp.next();
		}
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
				xrp.next();
				tileHashMap = parseTag(tileHashMap);
				
				tileset[curY][curX] = new TileData(tileHashMap, curX, curY);
			}
			
			else if(xrp.getEventType() == xrp.END_TAG)
			{
				if(xrp.getName().equals("Tile"))
					curX++;
				
				else if(xrp.getName().equals("TileRow"))
					curY++;
			}
		}
	}
	
	public HashMap<String, String> parseAttributes()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < xrp.getAttributeCount(); i++)
		{
			map.put(xrp.getAttributeName(i), xrp.getAttributeValue(i));
		}
		
		return map;
	}
	
	public HashMap<String, String> parseTag(HashMap<String, String> map) throws XmlPullParserException, IOException
	{
		String tagName = xrp.getName();
		xrp.next();
		map.put(tagName, xrp.getText());
		
		return map;
	}
	
}