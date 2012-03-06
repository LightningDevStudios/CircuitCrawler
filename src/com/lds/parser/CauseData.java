package com.lds.parser;

import com.lds.trigger.Cause;

public class CauseData
{
	private Cause cause;
	private String id;
	
	public CauseData(Cause cause, String id)
	{
		this.cause = cause;
		this.id = id;
	}
	
	public Cause getCause()
	{
		return cause;
	}
	
	public String getID()
	{
		return id;
	}
}
