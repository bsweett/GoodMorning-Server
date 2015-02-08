package com.goodmorning.enums;

public enum RSSType {
	
	NEWS("News"),
	WEATHER("Weather"),
	BUSINESS("Business"),
	SCIENCE("Science"),
	TECHNOLOGY("Technology"),
	SPORTS("Sports"),
	LIFESTYLE("Lifestyle"),
	ENTERTAINMENT("Entertainment"),
	OTHER("");
	
	private String text;

	RSSType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static RSSType fromString(String text) {
		if (text != null) {
			for (RSSType r : RSSType.values()) {
				if (text.equalsIgnoreCase(r.text)) {
					return r;
				}
			}
		}
		return null;
	}
}

