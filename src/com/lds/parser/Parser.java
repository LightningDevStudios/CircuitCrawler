package com.lds.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import android.content.res.XmlResourceParser;

import com.lds.game.R;
 
public class Parser
{
	
	public XmlResourceParser xrp;  

	public Parser(Context context)
	{
		xrp = context.getResources().getXml(R.xml.tempdata);
	}
	
	public void parseLevel()
		throws XmlPullParserException, IOException
	{
		
		int eventType = xrp.getEventType();
	
		while (eventType != XmlPullParser.END_DOCUMENT)
		{
			if (eventType == XmlPullParser.START_TAG)
			{	
				if(xrp.getName() == "PhysBlock")
					parsePhysBlock();
				
				else if(xrp.getName() == "Player")
					parsePlayer();
			}
			eventType = xrp.next();
		}
	}
	
	public void parsePhysBlock() throws XmlPullParserException, IOException
	{
		xrp.next();
		xrp.next();
		
		
		String[] parserPhysBlockA = new String[14];
		
		
		for (int i = 1; i <= 14; i++)
		{
			if ((i + 1) % 3 == 0)
			{
				parserPhysBlockA[i - 1] = xrp.getText();
			}
			
			xrp.next();
			xrp.next();
			xrp.next();
		}
		
		new PhysBlockData(parserPhysBlockA);
	}
	
	public void parsePlayer()
	{
		//send to a parser and do other shit here		
	}
}