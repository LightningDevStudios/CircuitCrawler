package com.lds.game.entity;

public class PickupHealth extends Pickup
{
	int healthValue;
	
	public PickupHealth (int healthValue, float xPos, float yPos)
	{
		super(xPos, yPos);
		this.healthValue = healthValue;
	}
	
	public int getHealthValue()
	{
		return healthValue;
	}
	
	public void setHealthValue(int healthValue)
	{
		this.healthValue = healthValue;
	}
}
