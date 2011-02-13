package com.lds.game.entity;

public class PickupEnergy extends PickupObj
{
	private int energyValue;
	
	public PickupEnergy (int energyValue, float xPos, float yPos)
	{
		super(xPos, yPos);
		this.energyValue = energyValue;
	}
	
	public int getEnergyValue()
	{
		return energyValue;
	}
	
	public void setEvergyValue(int energyValue)
	{
		this.energyValue = energyValue;
	}
}
