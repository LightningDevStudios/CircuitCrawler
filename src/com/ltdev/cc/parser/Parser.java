/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev.cc.parser;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.ltdev.cc.Tile;
import com.ltdev.cc.ai.Node;
import com.ltdev.cc.ai.NodePath;
import com.ltdev.cc.entity.*;
import com.ltdev.cc.trigger.*;
import com.ltdev.trigger.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL11;

import org.xmlpull.v1.XmlPullParserException;
 
public class Parser
{
    public static final int START_DOCUMENT = 0;
    public static final int END_DOCUMENT = 1; 
    public static final int START_TAG = 2;
    public static final int END_TAG = 3;
    
	public ArrayList<EntityData> parsedList;
	public ArrayList<Entity> entList;
	public ArrayList<Trigger> triggerList;
	public ArrayList<Node> nodeList;
	public ArrayList<CauseData> causeDataList;
	public ArrayList<EffectData> effectDataList;
	public ArrayList<NodePath> nodePathList;
	
	public Tile[][] tileset;
	public Player player;
	
	public XmlResourceParser xrp;  
	public HashMap<String, String> dataHM;

	public Parser(Context context, int res)
	{
		xrp = context.getResources().getXml(res);
		
		parsedList = new ArrayList<EntityData>();
		entList = new ArrayList<Entity>();
		triggerList = new ArrayList<Trigger>();
		nodeList = new ArrayList<Node>();
		causeDataList = new ArrayList<CauseData>();
		effectDataList = new ArrayList<EffectData>();
		nodePathList = new ArrayList<NodePath>();
	}

/***********************
 General parsing methods
 **********************/
	
	public void parseLevel(GL11 gl) throws XmlPullParserException, IOException
	{
		while (xrp.getEventType() != END_DOCUMENT)
		{
			if (xrp.getEventType() == START_TAG)
			{
				if (xrp.getName().equalsIgnoreCase("Entities"))
					parseEntities();
				else if (xrp.getName().equalsIgnoreCase("Tileset"))
					parseTileset(gl);
				else if (xrp.getName().equalsIgnoreCase("Triggers"))
					parseTriggers();
				else if (xrp.getName().equalsIgnoreCase("Nodes"))
					parseNodes();
				else if (xrp.getName().equalsIgnoreCase("NodeLinks"))
					parseNodeLinks();
				else if (xrp.getName().equalsIgnoreCase("TeleporterLinker"))
					parseTeleporterLinker();
					
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
			
			if (xrp.getEventType() == START_TAG)
			{
				dataHM = new HashMap<String, String>();
				if (xrp.getName().equalsIgnoreCase("Door"))
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
				else if (xrp.getName().equalsIgnoreCase("PuzzleBox"))
				{
					parseObj("PuzzleBox");
					PuzzleBoxData pbd = new PuzzleBoxData(dataHM);
					parsedList.add(pbd);
					pbd.createInst(entList);
				}
				else if (xrp.getName().equalsIgnoreCase("PhysBall"))
				{
					parseObj("PhysBall");
					PhysBallData pbd = new PhysBallData(dataHM);
					parsedList.add(pbd);
					pbd.createInst(entList);
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
				
				else if (xrp.getName().equalsIgnoreCase("Cannon"))
				{
					parseObj("Cannon");
					CannonData cd = new CannonData(dataHM);
					parsedList.add(cd);
					cd.createInst(entList);
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
			if (xrp.getEventType() == START_TAG && xrp.getName().equalsIgnoreCase("renderMode"))
			{
				parseObj("renderMode");
				xrp.next();
			}
			else if (xrp.getEventType() == START_TAG && xrp.getName().equalsIgnoreCase("tileset"))
			{
				dataHM.put("tileset", "0");
				parseObj("tileset");
				xrp.next();
			}
			else if (xrp.getEventType() == START_TAG && xrp.getName().equalsIgnoreCase("texture"))
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
	public void parseTileset(GL11 gl) throws XmlPullParserException, IOException
	{
		int x, y, curX, curY;
		
		HashMap<String, String> attributes = parseAttributes();
		
		x = Integer.parseInt(attributes.get("x"));
		y = Integer.parseInt(attributes.get("y"));
		
		TileData[][] tilesetData = new TileData[y][x];
		
		curX = 0;
		curY = 0;
		xrp.next();
		while (!(xrp.getEventType() == END_TAG && xrp.getName().equals("Tileset")))
		{
			if (curX == x)
			{
				curX = 0;
				curY++;
			}
			
			if (xrp.getName().equals("Tile"))
			{
				tilesetData[curY][curX] = new TileData(gl, xrp.getAttributeValue(0), curX, curY, x, y);
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
	
	/******************
	 * Parse Triggers *
	 ******************/
	
	/**
	 * Parse a level file and store the associated data in this class.
	 * \todo return a Level!
	 * @throws XmlPullParserException Thrown when the XML file is not properly formatted.
	 * @throws IOException Thrown when there is an issue accessing the file.
	 */
	public void parseTriggers() throws XmlPullParserException, IOException
	{
		xrp.next();
		Log.d("LDS_Game", xrp.getName());
		while (!(xrp.getEventType() == END_TAG && xrp.getName().equals("Triggers")))
		{
			if (xrp.getName().equalsIgnoreCase("Cause"))
			{
				String causeId = xrp.getAttributeValue(0);
				String causeType = xrp.getAttributeValue(1);
				xrp.next();
				String[] causeParameters = xrp.getText().split(",");
				causeDataList.add(new CauseData(causeInitializer(causeType, causeParameters), causeId));
			}
			else if (xrp.getName().equalsIgnoreCase("Effect"))
			{
				String effectId = xrp.getAttributeValue(0);
				String effectType = xrp.getAttributeValue(1);
				xrp.next();
				String[] effectParameters;
				if (xrp.getText() != null)
					effectParameters = xrp.getText().split(",");
				else
				{
					effectParameters = new String[1];
					effectParameters[0] = "";
				}
				effectDataList.add(new EffectData(effectInitializer(effectType, effectParameters), effectId));
			}
			else if (xrp.getName().equalsIgnoreCase("Trigger"))
			{			
				Cause cause = causeFromID(xrp.getAttributeValue(0));
				Effect effect = effectFromID(xrp.getAttributeValue(1)); 
				
				if (cause != null && effect != null)
				{
					triggerList.add(new Trigger(cause, effect));
				}
			}
			xrp.next();
			xrp.next();
		}
	}
	
	public Cause causeFromID(String causeID)
	{
		for (CauseData cd : causeDataList)
		{
			if (cd.getID().equalsIgnoreCase(causeID))
				return cd.getCause();
		}
		
		return null;
	}
	public Effect effectFromID(String effectID)
	{
		for (EffectData ed : effectDataList)
		{
			if (ed.getID().equalsIgnoreCase(effectID))
				return ed.getEffect();
		}
		
		return null;
	}
	
	public Cause causeInitializer(String type, String[] parameters)
	{
		Cause cause = null;
		if (type.equalsIgnoreCase("CauseNOT"))
		{
			cause = new CauseNOT(causeFromID(parameters[0]));
		}
		else if (type.equalsIgnoreCase("CauseAND"))
		{
			cause = new CauseAND(causeFromID(parameters[0]), causeFromID(parameters[1]));
		}
		else if (type.equalsIgnoreCase("CauseOR"))
		{
			cause = new CauseOR(causeFromID(parameters[0]), causeFromID(parameters[1]));
		}
		else if (type.equalsIgnoreCase("CauseXOR"))
		{
			cause = new CauseXOR(causeFromID(parameters[0]), causeFromID(parameters[1]));
		}
		else if (type.equalsIgnoreCase("CauseButton"))
		{
			cause = new CauseButton(this.<Button>stringToSubEntity(parameters[0]));
		}
		else if (type.equalsIgnoreCase("CauseEntityDestruction"))
		{
			cause = new CauseEntityDestruction(this.<Entity>stringToSubEntity(parameters[0]));
		}
		else if (type.equalsIgnoreCase("CauseLocation"))
		{
			cause = new CauseLocation(this.<Player>stringToSubEntity(parameters[0]), 
					Float.parseFloat(parameters[1]), 
					Float.parseFloat(parameters[2]), 
					Float.parseFloat(parameters[3]), 
					Float.parseFloat(parameters[4]));
		}
		
		else if (type.equalsIgnoreCase("CauseTimePassed"))
		{
			if (parameters.length == 2)
				cause = new CauseTimePassed(Integer.parseInt(parameters[0]), Boolean.parseBoolean(parameters[1]));
			else
				cause = new CauseTimePassed(Integer.parseInt(parameters[0]));
		}
		else if (type.equalsIgnoreCase("CauseOffScreen"))
		{
			cause = new CauseOffScreen(this.<Player>stringToSubEntity(parameters[0]), tileset);
		}
		
		return cause;
	}
	
	public Effect effectInitializer(String type, String[] parameters)
	{
		Effect effect = null;
		if (type.equalsIgnoreCase("EffectDoor"))
		{
			effect = new EffectDoor(this.<Door>stringToSubEntity(parameters[0]));
		}
		else if (type.equalsIgnoreCase("EffectEndGame"))
		{
			effect = new EffectEndGame(null, Boolean.parseBoolean(parameters[0]));
		}
		else if (type.equalsIgnoreCase("EffectRaiseBridge"))
		{
			effect = new EffectRaiseBridge(tileset[Integer.parseInt(parameters[1])][Integer.parseInt(parameters[0])]);  //\todo FIND OUT IF THIS WORKS! 
		}
		else if (type.equalsIgnoreCase("EffectRemoveEntity"))
		{
			effect = new EffectRemoveEntity(this.<Entity>stringToSubEntity(parameters[0]));
		}
		else if (type.equalsIgnoreCase("EffectTriggerTimer"))
		{
			effect = new EffectTriggerTimer((CauseTimePassed)causeFromID(parameters[0]), false);
		}
		else if (type.equalsIgnoreCase("EffectAND"))
		{
			effect = new EffectAND(effectFromID(parameters[0]), effectFromID(parameters[1]));
		}
		
		return effect;
	}

	public <T> T stringToSubEntity(String id)
	{
		for (EntityData entD : parsedList)
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
		
		for (String node : nodes)
		{
			String[] temp = node.split(",");
			nodeList.add(new Node(Float.parseFloat(temp[0]), Float.parseFloat(temp[1])));
		}
		
		xrp.next();
		xrp.next();
	}
	
	public void parseNodeLinks() throws XmlPullParserException, IOException
	{
		xrp.next();
		String[] nodeLinks = (xrp.getText()).split(";");
		
		for (String nodeLink : nodeLinks)
		{
			String[] temp = nodeLink.split(",");
			if (temp.length == 2)
				nodeList.get(Integer.parseInt(temp[0])).addNodeLink(nodeList.get(Integer.parseInt(temp[1])));
			else if (temp.length == 3)
				nodeList.get(Integer.parseInt(temp[0])).addNodeLink(nodeList.get(Integer.parseInt(temp[1])), Boolean.parseBoolean(temp[3]));
		}
		
		xrp.next();
		xrp.next();
	}
	
	public void parseNodePaths() throws XmlPullParserException, IOException
	{
		xrp.next();
		String[] nodePaths = (xrp.getText()).split(";");
		
		for (String nodePath : nodePaths)
		{
			NodePath np = new NodePath();
			String[] values = nodePath.split(",");
			np.setID(values[0]);
			for (int i = 1; i < values.length; i++)
			{
				np.add(nodeList.get(Integer.parseInt(values[i])));
			}
			nodePathList.add(np);
		}
		
		xrp.next();
		xrp.next();
	}
	
	/*********************
	 * TeleporterLinkers *
	 *********************/

	private void parseTeleporterLinker() throws XmlPullParserException, IOException //lol
	{		
		Teleporter tp1 = this.<Teleporter>stringToSubEntity(xrp.getAttributeValue(0));
		Teleporter tp2 = this.<Teleporter>stringToSubEntity(xrp.getAttributeValue(1));
		boolean oneWay = Boolean.parseBoolean(xrp.getAttributeValue(2));
		TeleporterLinker tpLink = new TeleporterLinker(tp1, tp2, oneWay);
		
		xrp.next();
	}
}
