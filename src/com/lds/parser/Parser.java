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
	public ArrayList<CauseData> causeDataList = new ArrayList<CauseData>();
	public ArrayList<EffectData> effectDataList = new ArrayList<EffectData>();
	
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
Parse Triggers
************/
	
	public void parseTriggers() throws XmlPullParserException, IOException
	{
		xrp.next();
		while(!(xrp.getEventType() == END_TAG && xrp.getName().equals("Triggers")))
		{
			if(xrp.getName().equalsIgnoreCase("Cause"))
			{
				String causeId= xrp.getAttributeValue(0);
				String causeType = xrp.getAttributeValue(1);
				xrp.next();
				String[] causeParameters = xrp.getText().split(",");
				causeDataList.add(new CauseData(causeInitializer(causeType, causeParameters), causeId));
			}
			else if(xrp.getName().equalsIgnoreCase("Effect"))
			{
				String effectId= xrp.getAttributeValue(0);
				String effectType = xrp.getAttributeValue(1);
				xrp.next();
				String[] effectParameters = xrp.getText().split(",");
				effectDataList.add(new EffectData(effectInitializer(effectType, effectParameters), effectId));
			}
			else if(xrp.getName().equalsIgnoreCase("Trigger"))
			{			
				Cause cause = causeFromID(xrp.getAttributeName(0));
				Effect effect = effectFromID(xrp.getAttributeName(1)); 
				
				if (cause != null && effect != null)
				{
					triggerList.add(new Trigger(cause, effect));
				}
			}
			xrp.next();
			xrp.next();
		}
	}
	
	public Cause causeFromID (String causeID)
	{
		for(CauseData cd : causeDataList)
		{
			if(cd.getID().equalsIgnoreCase(causeID))
				return cd.getCause();
		}
		
		return null;
	}
	public Effect effectFromID (String effectID)
	{
		for(EffectData ed : effectDataList)
		{
			if(ed.getID().equalsIgnoreCase(effectID))
				return ed.getEffect();
		}
		
		return null;
	}
	
	public Cause causeInitializer(String type, String[] parameters)
	{
		Cause cause = null;
		//TODO NOT/AND/OR/XOR causes TriggerTimer effect
		if(type.equalsIgnoreCase("CauseNOT"))
		{
			cause = new CauseNOT(causeFromID(parameters[0]));
		}
		else if(type.equalsIgnoreCase("CauseAND"))
		{
			cause = new CauseAND(causeFromID(parameters[0]), causeFromID(parameters[1]));
		}
		else if(type.equalsIgnoreCase("CauseOR"))
		{
			cause = new CauseOR(causeFromID(parameters[0]), causeFromID(parameters[1]));
		}
		else if(type.equalsIgnoreCase("CauseXOR"))
		{
			cause = new CauseXOR(causeFromID(parameters[0]), causeFromID(parameters[1]));
		}
		else if(type.equalsIgnoreCase("CauseButton"))
		{
			cause = new CauseButton(this.<Button>stringToSubEntity(parameters[0]));
		}
		else if(type.equalsIgnoreCase("CauseDoneMoving"))
		{
			cause = new CauseDoneMoving(this.<PhysEnt>stringToSubEntity(parameters[0]));
		}
		else if(type.equalsIgnoreCase("CauseDoneRotating"))
		{
			cause = new CauseDoneRotating(this.<PhysEnt>stringToSubEntity(parameters[0]));
		}
		else if(type.equalsIgnoreCase("CauseDoneScaling"))
		{
			cause = new CauseDoneScaling(this.<PhysEnt>stringToSubEntity(parameters[0]));
		}
		else if(type.equalsIgnoreCase("CauseEnemyCount"))
		{
			cause = new CauseEnemyCount(Integer.parseInt(parameters[0]));
		}
		else if(type.equalsIgnoreCase("CauseLocation"))
		{
			cause = new CauseLocation(this.<Player>stringToSubEntity(parameters[0]), 
					Integer.parseInt(parameters[1]), 
					Integer.parseInt(parameters[2]), 
					Integer.parseInt(parameters[3]), 
					Integer.parseInt(parameters[4]));
		}
		else if(type.equalsIgnoreCase("CausePlayerHealth"))
		{
			cause = new CausePlayerHealth(Integer.parseInt(parameters[0]), this.<Player>stringToSubEntity(parameters[1]));
		}
		else if(type.equalsIgnoreCase("CauseTimePassed"))
		{
			if(parameters.length == 2)
				cause = new CauseTimePassed(Integer.parseInt(parameters[0]), Boolean.parseBoolean(parameters[1]));
			else
				cause = new CauseTimePassed(Integer.parseInt(parameters[0]));
		}
		
		return cause;
	}
	
	public Effect effectInitializer(String type, String[] parameters)
	{
		Effect effect = null;
		if(type.equalsIgnoreCase("EffectDoor"))
		{
			effect = new EffectDoor(this.<Door>stringToSubEntity(parameters[0]));
		}
		else if (type.equalsIgnoreCase("EffectEndGame"))
		{
			effect = new EffectEndGame(null);
		}
		else if (type.equalsIgnoreCase("EffectRaiseBridge"))
		{
			effect = new EffectRaiseBridge(tileset[Integer.parseInt(parameters[0])][Integer.parseInt(parameters[1])]);
		}
		else if(type.equalsIgnoreCase("EffectRemoveEntity"))
		{
			effect = new EffectRemoveEntity(this.<Entity>stringToSubEntity(parameters[0]));
		}
		/*else if (effectSTR.equalsIgnoreCase("EffectTriggerTimer"))
		{
			effect = new EffectTriggerTimer();
		}*/
		
		return effect;
	}

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

	