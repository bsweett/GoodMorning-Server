package com.goodmorning.models;

import java.util.ArrayList;
import java.util.List;

import com.goodmorning.enums.RSSType;

public class RSSFeed {
	private final String title;
	private final String link;
	private final String description;
	private final String language;
	private final String copyright;
	private final String pubDate;
	private RSSType type;

	private final List<RSSMessage> entries = new ArrayList<RSSMessage>();

	public RSSFeed(String title, String link, String description, String language, String copyright, String pubDate) {
		this.title = title;
		this.link = link;
		this.description = description;
		this.language = language;
		this.copyright = copyright;
		this.pubDate = pubDate;
		this.type = RSSType.UNKNOWN;
	}

	public List<RSSMessage> getMessages() {
		return entries;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public String getDescription() {
		return description;
	}

	public String getLanguage() {
		return language;
	}

	public String getCopyright() {
		return copyright;
	}

	public String getPubDate() {
		return pubDate;
	}

	@Override
	public String toString() {
		return "Feed [copyright=" + copyright + ", description=" + description
				+ ", language=" + language + ", link=" + link + ", pubDate="
				+ pubDate + ", title=" + title + "]";
	}

	public RSSType getType() {
		return type;
	}
}
