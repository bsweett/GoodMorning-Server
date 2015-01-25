package com.goodmorning.enums;

public enum AlertType {
	
	NOTIFICATION("notification"),
	SOUND("sound"),
	VIBERATE("viberate"),
	ALL("all"),
	NONE("none");
	
	private String text;

	AlertType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static AlertType fromString(String text) {
		if (text != null) {
			for (AlertType b : AlertType.values()) {
				if (text.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
		}
		return null;
	}
}
