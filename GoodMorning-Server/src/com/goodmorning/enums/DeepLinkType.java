package com.goodmorning.enums;

public enum DeepLinkType {

	FACEBOOK("FaceBook"),
	YOUTUBE("YouTube"),
	MAPS("Maps"),
	SMS("Messenger"),
	MUSIC("Music"),
	BOOKS("Books"),
	NONE("None");

	private String text;

	DeepLinkType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static DeepLinkType fromString(String text) {
		if (text != null) {
			for (DeepLinkType b : DeepLinkType.values()) {
				if (text.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
		}
		return null;
	}
}
