package com.github.djamba.yreader.rss;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class Subscriptions {
	
	private List<Group> groups;
	private Map<String, Feed> feeds;
	
	public List<Group> getGroups() {
		return groups;
	}

	public Subscriptions(String sbSubscriptionsXml) {
		
		this.groups = new ArrayList<Group>();
		this.feeds = new HashMap<String, Feed>();
		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		
	        factory.setNamespaceAware(true);
	        XmlPullParser xpp = factory.newPullParser();
	
	        xpp.setInput(new StringReader(sbSubscriptionsXml));
	        
			int eventType = xpp.getEventType();
			
			Group tmpGroup = null;
			Feed tmpFeed = null;
			boolean isGroup = false, isFeed = false;
			boolean isTitle = false;
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				
				if (eventType == XmlPullParser.START_DOCUMENT)
				{
					Log.v("yReader", "Start document");
				}
				else if (eventType == XmlPullParser.START_TAG)
				{
					Log.v("yReader", "Start tag: '" + xpp.getName() + "'");
					
					if (xpp.getName() != null)
					{
						if (xpp.getName().equals("subscriptions")) {
							
							tmpGroup = new Group(0);
							tmpGroup.setName("All");
							tmpGroup.setUnreadCount(Integer.valueOf(xpp.getAttributeValue(null, "unread_count")));
							
							this.groups.add(tmpGroup);
						}
						else if (xpp.getName().equals("group")) {
							
							isGroup = true;
							
							tmpGroup = new Group(Integer.valueOf(xpp.getAttributeValue(null, "group_id")));
							tmpGroup.setUnreadCount(Integer.valueOf(xpp.getAttributeValue(null, "unread_count")));
							
							this.groups.add(tmpGroup);
						}
						else if (xpp.getName().equals("feed")) {
							isFeed = true;
						}
						else if (xpp.getName().equals("title")) {
							isTitle = true;
						}
					}
				}
				else if (eventType == XmlPullParser.END_TAG)
				{
					Log.v("yReader", "End tag: '" + xpp.getName() + "'");
					
					if (xpp.getName() != null)
					{
						if (xpp.getName().equals("group")) {
							tmpGroup = null;
							isGroup = false;
						}
						else if (xpp.getName().equals("feed")) {
							tmpFeed = null;
							isFeed = false;
						}
					}
				}
				else if (eventType == XmlPullParser.TEXT)
				{
					Log.v("yReader", "Text: '" + xpp.getText() + "'");
					
					if (isGroup && isTitle && !isFeed) {
						tmpGroup.setName(xpp.getText());
						isTitle = false;
					}
				}
				
				eventType = xpp.next();
			}
		}
		catch (XmlPullParserException e) {
			Log.e("Subscriptions::ctor", e.getMessage());
		}
		catch (IOException e) {
			Log.e("Subscriptions::ctor", e.getMessage());
		}
	}
	
	public class Group {
		
		private int id;
		private String name;
		private int unreadCount;
		
		public Group(int id) {
			this.setId(id);
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getUnreadCount() {
			return unreadCount;
		}

		public void setUnreadCount(int unreadCount) {
			this.unreadCount = unreadCount;
		}
		
		@Override
		public String toString() {
			return "#" + id + ": " + name + "  [" + unreadCount + "]";
		}
	}
	
	public class Feed {
		
		private String name;
		
		public Feed(String name) {
			this.name = name;
		}
	}

}
