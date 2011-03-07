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
import com.lds.game.entity.Tile;
import com.lds.trigger.*;
 
public class Parser //this is a parser
{
	public ArrayList<EntityData> parsedList = new ArrayList<EntityData>();
	public ArrayList<Entity> entList = new ArrayList<Entity>();
	public Tile[][] tileset;
	
	public XmlResourceParser xrp;  
	public HashMap <String, String> dataHM;

	public Parser(Context context, int res)
	{
		xrp = context.getResources().getXml(res);
	}
	
/***********************
 General parsing methods
 **********************/
	
	public void parseLevel() throws XmlPullParserException, IOException
	{
		while (xrp.getEventType() != xrp.END_DOCUMENT)
		{
			if (xrp.getEventType() == xrp.START_TAG)
			{
				System.out.println(xrp.getName());
				if(xrp.getName().equalsIgnoreCase("Entities"))
					parseEntities();
				else if(xrp.getName().equalsIgnoreCase("Tileset"))
					parseTileset();
				else if(xrp.getName().equalsIgnoreCase("Triggers"));
					parseTriggers();
			}
			
			xrp.next();
		}
	}
	
	public void parseEntities() throws XmlPullParserException, IOException
	{
		//xrp.next();
		while (!((xrp.getEventType() == xrp.END_TAG && xrp.getName().equals("Entities"))))
		{
			xrp.next();
			
			if(xrp.getEventType() == xrp.START_TAG)
			{
				dataHM = new HashMap<String, String>();
				if(xrp.getName().equalsIgnoreCase("Door"))
				{
					parseObj("Door");
					DoorData dd = new DoorData(dataHM);
					parsedList.add(dd);
					dd.createInst(entList);
				}
				else if (xrp.getName().equalsIgnoreCase("Button"))
				{
					parseObj("Button");
					ButtonData bd = new ButtonData(dataHM);
					parsedList.add(bd);
					bd.createInst(entList);
				}
				else if (xrp.getName().equalsIgnoreCase("PhysBlock"))
				{
					parseObj("PhysBlock");
					PhysBlockData pbd = new PhysBlockData(dataHM);
					parsedList.add(pbd);
					pbd.createInst(entList);
				}
				else if (xrp.getName().equalsIgnoreCase("Blob"))
				{
					parseObj("Blob");
					BlobData bd = new BlobData(dataHM);
					parsedList.add(bd);
					bd.createInst(entList);
				}
				else if (xrp.getName().equalsIgnoreCase("PuzzleBox"))
				{
					parseObj("PuzzleBox");
					PuzzleBoxData pbd = new PuzzleBoxData(dataHM);
					parsedList.add(pbd);
					pbd.createInst(entList);
				}
				else if (xrp.getName().equalsIgnoreCase("PhysCircle"))
				{
					parseObj("PhysCircle");
					PhysCircleData pcd= new PhysCircleData(dataHM);
					parsedList.add(pcd);
					pcd.createInst(entList);
				}
				else if (xrp.getName().equalsIgnoreCase("Player"))
				{
					parseObj("Player");
					PlayerData pd = new PlayerData(dataHM);
					parsedList.add(pd);
					pd.createInst(entList);
				}
				else if (xrp.getName().equalsIgnoreCase("PickupHealth"))
				{
					parseObj("PickupHealth");
					PickupHealthData phD = new PickupHealthData(dataHM);
					parsedList.add(phD);
					phD.createInst(entList);
				}
			}
		}
	}
	
	public HashMap<String, String> parseTag(HashMap<String, String> map) throws XmlPullParserException, IOException
	{
		String tagName = xrp.getName();
		xrp.next();
		map.put(tagName, xrp.getText());
		
		return map;
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
	
/****************
 Parse An Object
*****************/
	public void parseObj(String tn) throws XmlPullParserException, IOException
	{		
		
		xrp.next();
		
		while (!(xrp.getEventType() == xrp.END_TAG && xrp.getName().equalsIgnoreCase(tn))) 
		{
			if(xrp.getEventType() == xrp.START_TAG && xrp.getName().equalsIgnoreCase("renderMode"))
			{
				parseObj("renderMode");
				xrp.next();
			}
			else if(xrp.getEventType() == xrp.START_TAG && xrp.getName().equalsIgnoreCase("tileset"))
			{
				dataHM.put("tileset", "0");
				parseObj("tileset");
				xrp.next();
			}
			else if(xrp.getEventType() == xrp.START_TAG && xrp.getName().equalsIgnoreCase("texture"))
			{
				dataHM.put("texture", "0");
				parseObj("texture");
				xrp.next();
			}
			else
			{
				parseTag(dataHM);			
				xrp.next(); 
				xrp.next();
			}
		}
	}
/***************
Parse A Tileset
***************/
	public void parseTileset() throws XmlPullParserException, IOException
	{
		HashMap<String, String> tileHashMap = new HashMap<String, String>();
		int x, y, curX, curY;
		
		HashMap<String, String> attributes = parseAttributes();
		
		x = Integer.parseInt(attributes.get("x"));
		y = Integer.parseInt(attributes.get("y"));
		
		TileData[][] tilesetData = new TileData[y][x];
		
		curX = 0;
		curY = 0;
		xrp.next();
		while(!(xrp.getEventType() == xrp.END_TAG && xrp.getName().equals("Tileset")))
		{
			if(curX == x)
			{
				curX = 0;
				curY++;
			}
			
			if (xrp.getName().equals("Tile"))
			{
				tilesetData[curY][curX] = new TileData(xrp.getAttributeValue(0), curX, curY, x, y);
				curX++;
				
				xrp.next();
				xrp.next();
			}
		}
		
		tileset = new Tile[y][x];
		
		for (int i = 0; i < y; i++)
		{
			for (int j = 0; j < x; j++)
			{
				tileset[i][j] = tilesetData[i][j].getTile();
			}
		}
	}
	
/************
Parse Triggas
************/
	
	public void parseTriggers() throws XmlPullParserException, IOException
	{
		while(!(xrp.getEventType() == xrp.END_TAG && xrp.getName().equals("Triggers")))
		{
			xrp.next();
			
			while(!(xrp.getEventType() == xrp.END_TAG && xrp.getName().equals("Trigger")))
			{
				xrp.next();
				
				String causeSTR = xrp.getAttributeValue(0); //picks up a cause
				xrp.next();
				
				String ids = xrp.getName();
				String[] causeID = ids.split(","); //picks and splits cause IDs
				
				xrp.next();
				xrp.next();
				
				String effectSTR = xrp.getAttributeValue(0); //picks up an effect
				xrp.next();
				
				ids = xrp.getName();
				String[] effectID = ids.split(","); //picks and splits effect IDs
				
				/*************************
				sets the causes and effects
				**************************/
				Cause cause;
				
				if(causeSTR.equalsIgnoreCase("CauseButton"))
				{
					//FILL STUFF IN
				}
				
				xrp.next();
				xrp.next();
			}
		}
	}
}
