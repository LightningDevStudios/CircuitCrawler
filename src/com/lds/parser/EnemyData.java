package com.lds.parser;

import java.util.HashMap;
import com.lds.Enums.AIType;

public class EnemyData extends CharacterData
{
	protected AIType type;
	//protected Node nodepath;
	
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
	}
	
	public void setType(AIType newType)		{type = newType;}
	
	public AIType getType()		{return type;}
}