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
 
public class Parser //this is a perser
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
	
	public void parseLevel()
		throws XmlPullParserException, IOException
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
			}
			xrp.next();
		}
	}
	
	public void parseEntities() throws XmlPullParserException, IOException
	{
		xrp.next();
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
					dd.createInst(entList);
					//dataHM = null;
				}
				else if (xrp.getName().equalsIgnoreCase("Button"))
				{
					parseObj("Button");
					ButtonData bd = new ButtonData(dataHM);
					bd.createInst(entList);
					//dataHM = null;
				}
				else if (xrp.getName().equalsIgnoreCase("PhysBlock"))
				{
					parseObj("PhysBlock");
					PhysBlockData pbd = new PhysBlockData(dataHM);
					pbd.createInst(entList);
					//dataHM = null;
				}
				else if (xrp.getName().equalsIgnoreCase("Blob"))
				{
					parseObj("Blob");
					BlobData bd = new BlobData(dataHM);
					bd.createInst(entList);
					//dataHM = null;
				}
				else if (xrp.getName().equalsIgnoreCase("PuzzleBox"))
				{
					parseObj("PuzzleBox");
					PuzzleBoxData pbd = new PuzzleBoxData(dataHM);
					pbd.createInst(entList);
					//dataHM = null;
				}
				else if (xrp.getName().equalsIgnoreCase("PhysCircle"))
				{
					parseObj("PhysCircle");
					PhysCircleData pcd= new PhysCircleData(dataHM);
					pcd.createInst(entList);
					//dataHM = null;
				}
				else if (xrp.getName().equalsIgnoreCase("Player"))
				{
					parseObj("Player");
					PlayerData pd = new PlayerData(dataHM);
					pd.createInst(entList);
					//dataHM = null;
				}
				else if (xrp.getName().equalsIgnoreCase("PickupHealth"))
				{
					parseObj("PickupHealth");
					PickupHealthData phd = new PickupHealthData(dataHM);
					phd.createInst(entList);
					//dataHM = null;
				}
				else
				{
					xrp.next();
				}
			}
		}
	}
	
	public void parseObj(String tn) throws XmlPullParserException, IOException
	{		
		
		
		xrp.next();
		
		while (!(xrp.getEventType() == xrp.END_TAG && xrp.getName().equalsIgnoreCase(tn))) 
		{
			if(xrp.getEventType() == xrp.START_TAG && xrp.getName().equalsIgnoreCase("renderMode"))
			{
				parseRM();
				xrp.next();
			}
			else
				parseTag(dataHM);	
			
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
		
	public void parseRM() throws XmlPullParserException, IOException
	{
		while (!(xrp.getEventType() == xrp.END_TAG && xrp.getName().equalsIgnoreCase("renderMode")))
		{
			System.out.println(xrp.getName() + "," + xrp.getEventType());
			xrp.next();
			System.out.println(xrp.getName() + "," + xrp.getEventType());
			if(xrp.getEventType() == xrp.END_TAG && xrp.getName().equalsIgnoreCase("renderMode"))
				break;
			
			if(xrp.getName().equalsIgnoreCase("texture") || xrp.getName().equalsIgnoreCase("tileset"))
			{
				xrp.next();
				parseTag(dataHM);
				xrp.next();
				xrp.next();
				parseTag(dataHM);
				xrp.next();
				xrp.next();
			}
						
			else
			{
				parseTag(dataHM);
				xrp.next();
				xrp.next();
				/*parseTag(dataHM);
				xrp.next();
				xrp.next();*/			
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
	
	/*public static String[] csvSeperator(String tv)
	{
		String ttv = tv;
		int i = 1;
		//COUNTER!!!!!
		while(ttv.contains(","))
		{
			i++;
			ttv = ttv.replaceFirst(",", "");
		}
		
		String[] sepString = new String[i];
		for(int j = 0; j < i; j++)
		{
			if(tv.indexOf(",") == 0)
			{
				tv = tv.replaceFirst(",","");
				if(tv.contains(","))
				{
					sepString[j] = tv.substring(0, tv.indexOf(","));
					tv = tv.substring(tv.indexOf(","));
				}
				else
				{
					sepString[j] = tv;
				}
			}
			else
			{
				sepString[j] = tv.substring(0, tv.indexOf(","));
				tv = tv.substring(tv.indexOf(","));
			}
			System.out.println();
		}
		for (String temp : sepString)
		{
			if(tv.indexOf(",") > 0)
				temp = tv.substring(0, tv.indexOf(","));
			else if(tv.indexOf(",") == 0)
			{
				temp = tv.substring(1);
				if(tv.indexOf(",") > 0)
					temp = tv.substring(0, tv.indexOf(","));
				else
					temp = tv;
			}
			temp = temp.replace(",", " ");
			temp = temp.trim();
		}
		
		return sepString;
	}*/
}