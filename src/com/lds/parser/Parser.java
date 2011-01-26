// DOESN'T WORK COMPLETELY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

package com.lds.parser;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.lds.game.R;



public class Parser
{
	
public XmlPullParserFactory factory;
public XmlPullParser xpp;
public XmlResourceParser xrp;   //I have no idea what the difference between the resource and pull parsers is, just how to use them.

	public Parser(Context context)
	{
		try {
			factory = XmlPullParserFactory.newInstance();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		factory.setNamespaceAware(true);
		
		xrp = context.getResources().getXml(R.xml.tempdata);
		try {
			xpp = factory.newPullParser();
		} catch (XmlPullParserException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseLevel()
		throws XmlPullParserException, IOException
	{
		
		//XmlResourceParser tempXML = context.getResources().getXml(R.xml.tempdata);
		int eventType = xrp.getEventType();
	
		while (eventType != XmlPullParser.END_DOCUMENT)
		{
			if (eventType == XmlPullParser.START_TAG)
			{	
				if(xpp.getName() == "PhysBlock")
					parsePhysBlock();
				
				else if(xpp.getName() == "Player")
					parsePlayer();
			}
			eventType = xrp.next();
		}
	}
	
	public void parsePhysBlock()
	{
		for (int i = 0; i <= 42; i++)
		{
			try {
				xrp.next();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if ((i + 1) % 3 == 0)
			{
				System.out.println(xrp.getText());
			}
				
		}
	}
	
	public void parsePlayer()
	{
		
	}
}