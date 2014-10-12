package com.goodmorning.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import com.goodmorning.models.RSSFeed;
import com.goodmorning.models.RSSMessage;
import com.goodmorning.util.Messages;
import com.goodmorning.util.ServerLogger;

public class RSSFeedParser {
	private final String TITLE = "title";
	private final String DESCRIPTION = "description";
	//private final String CHANNEL = "channel";
	private final String LANGUAGE = "language";
	private final String COPYRIGHT = "copyright";
	private final String LINK = "link";
	private final String AUTHOR = "author";
	private final String ITEM = "item";
	private final String PUB_DATE = "pubDate";
	private final String GUID = "guid";

	private URL url = null;

	public RSSFeedParser(String feedUrl) {
		try {
			this.url = new URL(feedUrl);
		} catch (MalformedURLException e) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_RSSFeedParser, Messages.RSSFEEDPARSER_FAILED, e);
		}
	}

	public RSSFeed readFeed() {
		RSSFeed feed = null;
		try {
			boolean isFeedHeader = true;
			// Set header values initial to the empty string
			String description = "";
			String title = "";
			String link = "";
			String language = "";
			String copyright = "";
			String author = "";
			String pubdate = "";
			String guid = "";

			// First create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			InputStream in = read();
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			// read the XML document
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					String localPart = event.asStartElement().getName().getLocalPart();
					switch (localPart) {
					case ITEM:
						if (isFeedHeader) {
							isFeedHeader = false;
							feed = new RSSFeed(title, link, description, language, copyright, pubdate);
						}
						event = eventReader.nextEvent();
						break;
					case TITLE:
						title = getCharacterData(event, eventReader);
						break;
					case DESCRIPTION:
						description = getCharacterData(event, eventReader);
						break;
					case LINK:
						link = getCharacterData(event, eventReader);
						break;
					case GUID:
						guid = getCharacterData(event, eventReader);
						break;
					case LANGUAGE:
						language = getCharacterData(event, eventReader);
						break;
					case AUTHOR:
						author = getCharacterData(event, eventReader);
						break;
					case PUB_DATE:
						pubdate = getCharacterData(event, eventReader);
						break;
					case COPYRIGHT:
						copyright = getCharacterData(event, eventReader);
						break;
					}
				} else if (event.isEndElement()) {
					if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
						RSSMessage message = new RSSMessage();
						message.setAuthor(author);
						message.setDescription(description);
						message.setGuid(guid);
						message.setLink(link);
						message.setTitle(title);
						feed.getMessages().add(message);
						event = eventReader.nextEvent();
						continue;
					}
				}
			}
		} catch (XMLStreamException e) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_READFEED, Messages.RSSFEEDPARSER_FAILED, e);
		}
		return feed;
	}

	private String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
		String result = "";
		event = eventReader.nextEvent();
		if (event instanceof Characters) {
			result = event.asCharacters().getData();
		}
		return result;
	}

	private InputStream read() {
		try {
			return url.openStream();
		} catch (IOException e) {
			ServerLogger.getDefault().severe(this, Messages.METHOD_READ, Messages.RSSFEEDPARSER_FAILED, e);
			return null;
		}
	}
}
