package com.lds.parser;

import java.io.IOException;
import java.util.HashMap;

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
	
		while (xrp.getEventType() != xrp.END_DOCUMENT)
		{
			if (xrp.getEventType() == xrp.START_TAG)
			{
				System.out.println(xrp.getName());
				if(xrp.getName().equals("Entity"))
					parseEntity();
				
				else if(xrp.getName().equals("Player"))
					parsePlayer();
			}
			xrp.next();
		}
	}
	
	public void parseEntity() throws XmlPullParserException, IOException
	{
		xrp.next();
		
		
		//String[] entString = new String[14];
		HashMap <String, String> entHashMap = new HashMap<String, String>();		
		
		while (!((xrp.getEventType() == xrp.END_TAG && xrp.getName().equals("Entity")))) 
		{
			//entString[i - 1] = xrp.getText();
			String tempDataType = null;
			
			tempDataType = xrp.getName();
			
			xrp.next();
			
			entHashMap.put(tempDataType, xrp.getText());
			
			System.out.println(xrp.getText());
				
			xrp.next(); 
			xrp.next();
		}
		
		
		new EntityData(entHashMap);
	}
	
	public void parsePlayer()
	{
		//send to a parser and do other shit here		
	}
}