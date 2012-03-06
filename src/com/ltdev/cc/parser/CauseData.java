package com.ltdev.cc.parser;

import com.ltdev.trigger.Cause;

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
