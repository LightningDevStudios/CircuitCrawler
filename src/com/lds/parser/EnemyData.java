package com.lds.parser;

import java.util.HashMap;
import com.lds.Enums.AIType;
import com.lds.game.ai.NodePath;

public class EnemyData extends CharacterData
{
	protected AIType type;
	protected String nodePathID;
	protected NodePath nodePath;
	
	public EnemyData(HashMap<String, String> enemyHM)
	{
		super(enemyHM);
		if(enemyHM.get("AIType") != null)
		{
			if(enemyHM.get("AIType").equalsIgnoreCase("stalker"))
				type = AIType.STALKER;
			else if (enemyHM.get("AIType").equalsIgnoreCase("patrol"))
				type = AIType.PATROL;
			else if (enemyHM.get("AIType").equalsIgnoreCase("turret"))
				type = AIType.TURRET;
		}
		if (type == AIType.PATROL && enemyHM.get("PatrolPath") != null)
		{
			nodePathID = enemyHM.get("PatrolPath");
		}
	}
	
	public void setType(AIType newType)		{type = newType;}
	public void setPath(NodePath path)		{nodePath = path;}
	
	public AIType getType()		{return type;}
	public NodePath getPath()	{return nodePath;}
	public String getPathID()	{return nodePathID;}
}