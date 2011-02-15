package com.lds.parser;

import java.util.HashMap;

import com.lds.Vector2f;

public class AttackBoltData extends PhysEntData
{
	private Vector2f posVec, directionVec;
	
	public AttackBoltData(HashMap<String, String> attackBoltHM)
	{
		super(attackBoltHM);
		
		posVec = new Vector2f(Float.parseFloat(attackBoltHM.get("posVec").substring(0, attackBoltHM.get("posVec").indexOf(","))),Float.parseFloat(attackBoltHM.get("posVec").substring(attackBoltHM.get("posVec").indexOf(",") + 1)));
		directionVec = new Vector2f(Float.parseFloat(attackBoltHM.get("directionVec").substring(0, attackBoltHM.get("directionVec").indexOf(","))),Float.parseFloat(attackBoltHM.get("directionVec").substring(attackBoltHM.get("directionVec").indexOf(",") + 1)));
	}
		public Vector2f getPosVec()				{return posVec;}
		public Vector2f getDirectionVec()		{return directionVec;}
			
		public void setPosVec(Vector2f newPosVec)				{posVec = newPosVec;}
		public void setDirectionVec(Vector2f newDirectionVec)	{directionVec = newDirectionVec;}
}
