package com.goodmorning.enums;

public enum TaskType {

	CHORE("chore"),
	TRAVEL("travel"),
	ENTERTAINMENT("entertainment"),
	ALARM("alarm"),
	UNKNOWN("");

	private String text;

	TaskType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public static TaskType fromString(String text) {
		if (text != null) {
			for (TaskType b : TaskType.values()) {
				if (text.equalsIgnoreCase(b.text)) {
					return b;
				}
			}
		}
		return null;
	}
}
