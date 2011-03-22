package com.lds.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.lds.game.ai.Node;
import com.lds.game.entity.*;
import com.lds.trigger.*;
 
public class Parser //this is a parser
{
	public ArrayList<EntityData> parsedList = new ArrayList<EntityData>();
	public ArrayList<Entity> entList = new ArrayList<Entity>();
	public ArrayList<Trigger> triggerList = new ArrayList<Trigger>();
	public ArrayList<Node> nodeList = new ArrayList<Node>();
	public Tile[][] tileset;
	public Player player;
	
	public XmlResourceParser xrp;  
	public HashMap <String, String> dataHM;
	
	public static final int START_DOCUMENT = 0;
	public static final int END_DOCUMENT = 1;
	public static final int START_TAG = 2;
	public static final int END_TAG = 3;

	public Parser(Context context, int res)
	{
		xrp = context.getResources().getXml(res);
	}

/***********************
 General parsing methods
 **********************/
	
	public void parseLevel() throws XmlPullParserException, IOException
	{
		while (xrp.getEventType() != END_DOCUMENT)
		{
			if (xrp.getEventType() == START_TAG)
			{
				System.out.println(xrp.getName());
				if(xrp.getName().equalsIgnoreCase("Entities"))
					parseEntities();
				else if(xrp.getName().equalsIgnoreCase("Tileset"))
					parseTileset();
				else if(xrp.getName().equalsIgnoreCase("Triggers"))
					parseTriggers();
				else if(xrp.getName().equalsIgnoreCase("Nodes"))
					parseNodes();
				else if(xrp.getName().equalsIgnoreCase("NodeLinks"))
					parseNodeLinks();
					
			}
			
			xrp.next();
		}
	}
	
	public void parseEntities() throws XmlPullParserException, IOException
	{
		//xrp.next();
		while (!((xrp.getEventType() == END_TAG && xrp.getName().equals("Entities"))))
		{
			xrp.next();
			
			if(xrp.getEventType() == START_TAG)
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
					PhysBallData pcd= new PhysBallData(dataHM);
					parsedList.add(pcd);
					pcd.createInst(entList);
				}
				else if (xrp.getName().equalsIgnoreCase("Player"))
				{
					parseObj("Player");
					PlayerData pd = new PlayerData(dataHM);
					parsedList.add(pd);
					ArrayList<Entity> tempPlayerList = new ArrayList<Entity>();
					pd.createInst(tempPlayerList);
					player = (Player)tempPlayerList.get(0);
				}
				else if (xrp.getName().equalsIgnoreCase("PickupHealth"))
				{
					parseObj("PickupHealth");
					PickupHealthData phD = new PickupHealthData(dataHM);
					parsedList.add(phD);
					phD.createInst(entList);
				}
				else if (xrp.getName().equalsIgnoreCase("Cannon"))
				{
					parseObj("Cannon");
					CannonData cd = new CannonData(dataHM);
					parsedList.add(cd);
					cd.createInst(entList);
				}
				
				else if (xrp.getName().equalsIgnoreCase("SpikeBall"))
				{
					parseObj("SpikeBall");
					SpikeBallData sbd = new SpikeBallData(dataHM);
					parsedList.add(sbd);
					sbd.createInst(entList);
				}
				
				else if (xrp.getName().equalsIgnoreCase("Spike"))
				{
					parseObj("Spike");
					SpikeData sd = new SpikeData(dataHM);
					parsedList.add(sd);
					sd.createInst(entList);
				}
				
				else if (xrp.getName().equalsIgnoreCase("WallButton"))
				{
					parseObj("WallButton");
					WallButtonData wbd = new WallButtonData(dataHM);
					parsedList.add(wbd);
					wbd.createInst(entList);
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
		
		while (!(xrp.getEventType() == END_TAG && xrp.getName().equalsIgnoreCase(tn))) 
		{
			if(xrp.getEventType() == START_TAG && xrp.getName().equalsIgnoreCase("renderMode"))
			{
				parseObj("renderMode");
				xrp.next();
			}
			else if(xrp.getEventType() == START_TAG && xrp.getName().equalsIgnoreCase("tileset"))
			{
				dataHM.put("tileset", "0");
				parseObj("tileset");
				xrp.next();
			}
			else if(xrp.getEventType() == START_TAG && xrp.getName().equalsIgnoreCase("texture"))
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
		int x, y, curX, curY;
		
		HashMap<String, String> attributes = parseAttributes();
		
		x = Integer.parseInt(attributes.get("x"));
		y = Integer.parseInt(attributes.get("y"));
		
		TileData[][] tilesetData = new TileData[y][x];
		
		curX = 0;
		curY = 0;
		xrp.next();
		while(!(xrp.getEventType() == END_TAG && xrp.getName().equals("Tileset")))
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
		while(!(xrp.getEventType() == END_TAG && xrp.getName().equals("Triggers")))
		{
			while(!(xrp.getEventType() == END_TAG && xrp.getName().equals("Trigger")))
			{
				xrp.next();
				xrp.next();
				String causeSTR = xrp.getAttributeValue(0); //picks up a cause
				
				xrp.next();
				
				String ids = xrp.getText();
				String[] causeID = ids.split(","); //picks and splits cause IDs
				
				xrp.next();
				xrp.next();
				
				String effectSTR = xrp.getAttributeValue(0); //picks up an effect
				xrp.next();
				
				ids = xrp.getText();
				String[] effectID = ids.split(","); //picks and splits effect IDs
				
				/*************************
				sets the causes and effects
				**************************/
				Cause cause = null;
				Effect effect = null;
				
				//TODO NOT/AND/OR/XOR causes TriggerTimer effect
				if(causeSTR.equalsIgnoreCase("CauseButton"))
				{
					cause = new CauseButton(this.<Button>stringToSubEntity(causeID[0]));
				}
				else if(causeSTR.equalsIgnoreCase("CauseDoneMoving"))
				{
					cause = new CauseDoneMoving(this.<PhysEnt>stringToSubEntity(causeID[0]));
				}
				else if(causeSTR.equalsIgnoreCase("CauseDoneRotating"))
				{
					cause = new CauseDoneRotating(this.<PhysEnt>stringToSubEntity(causeID[0]));
				}
				else if(causeSTR.equalsIgnoreCase("CauseDoneScaling"))
				{
					cause = new CauseDoneScaling(this.<PhysEnt>stringToSubEntity(causeID[0]));
				}
				else if(causeSTR.equalsIgnoreCase("CauseEnemyCount"))
				{
					cause = new CauseEnemyCount(Integer.parseInt(causeID[0]));
				}
				else if(causeSTR.equalsIgnoreCase("CauseLocation"))
				{
					cause = new CauseLocation(this.<Player>stringToSubEntity(causeID[0]), 
							Integer.parseInt(causeID[1]), 
							Integer.parseInt(causeID[2]), 
							Integer.parseInt(causeID[3]), 
							Integer.parseInt(causeID[4]));
				}
				else if(causeSTR.equalsIgnoreCase("CausePlayerHealth"))
				{
					cause = new CausePlayerHealth(Integer.parseInt(causeID[0]), this.<Player>stringToSubEntity(causeID[1]));
				}
				else if(causeSTR.equalsIgnoreCase("CauseTimePassed"))
				{
					if(causeID.length == 2)
						cause = new CauseTimePassed(Integer.parseInt(causeID[0]), Boolean.parseBoolean(causeID[1]));
					else
						cause = new CauseTimePassed(Integer.parseInt(causeID[0]));
				}
				
				if(effectSTR.equalsIgnoreCase("EffectDoor"))
				{
					effect = new EffectDoor(this.<Door>stringToSubEntity(effectID[0]));
				}
				else if (effectSTR.equalsIgnoreCase("EffectEndGame"))
				{
					effect = new EffectEndGame(null);
				}
				else if (effectSTR.equalsIgnoreCase("EffectRaiseBridge"))
				{
					effect = new EffectRaiseBridge(tileset[Integer.parseInt(effectID[0])][Integer.parseInt(effectID[1])]);
				}
				else if (effectSTR.equalsIgnoreCase("EffectRemoveEntity"))
				{
					effect = new EffectRemoveEntity(this.<Entity>stringToSubEntity(effectID[0]));
				}
				/*else if (effectSTR.equalsIgnoreCase("EffectTriggerTimer"))
				{
					effect = new EffectTriggerTimer();
				}*/
				
				if (cause != null && effect != null)
				{
					triggerList.add(new Trigger(cause, effect));
				}
				xrp.next();
				xrp.next();
				
				Log.d("LDS_Game", "Current tag:" + xrp.getEventType() + ", " + xrp.getName() + ", " + xrp.getText());
			}
			
			xrp.next();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T stringToSubEntity(String id)
	{
		for(EntityData entD : parsedList)
		{
			if (entD.getID().equalsIgnoreCase(id))
			{
				return (T)entD.getEnt();
			}
		}
		return null;
	}
	
	/*********************
	Parse Nodes/Node Lists
	**********************/
	public void parseNodes() throws XmlPullParserException, IOException
	{
		xrp.next();
		String[] nodes = (xrp.getText()).split(";");
		
		for(String node : nodes)
		{
			String[] temp = node.split(",");
			Node tempNode = new Node(Float.parseFloat(temp[0]), Float.parseFloat(temp[1]));
			nodeList.add(tempNode);
		}
		
		xrp.next();
		xrp.next();
		
		//to make nodes in game, goto game constructor and nodeList = parser.nodeList
	}
	
	public void parseNodeLinks() throws XmlPullParserException, IOException
	{
		xrp.next();
		String[] nodeLinks = (xrp.getText()).split(";");
		
		for(String nodeLink : nodeLinks)
		{
			String[] temp = nodeLink.split(",");
			if(temp.length == 2)
				(nodeList.get(Integer.parseInt(temp[0]))).addNodeLink(nodeList.get(Integer.parseInt(temp[1])));
			else if(temp.length == 3)
				(nodeList.get(Integer.parseInt(temp[0]))).addNodeLink(nodeList.get(Integer.parseInt(temp[1])), Boolean.parseBoolean(temp[3]));
		}
		
		xrp.next();
		xrp.next();
	}
}

	