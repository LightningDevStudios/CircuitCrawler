package com.lds.trigger;

public class NOTCause extends Cause
{
	private Cause cause;
	
	public NOTCause (Cause cause)
	{
		this.cause = cause;
	}
	
	@Override
	public boolean isTriggered ()
	{
		return !cause.isTriggered();
	}
}
