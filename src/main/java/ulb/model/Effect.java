package ulb.model;

import java.util.Map;

public class Effect {
	private final String type;
	private final String target;
	// for "soin"
	private int value;
	// for "stat modifier"
	private String stat;
	private int modifier;
	private String duration;
	// for "stat modifier multiple"
	private Map<String, Integer> modifiers;


	public Effect(String type, String targetType, int value) {
		this.type = type;
		this.target = targetType;
		this.value = value;
	}

	public Effect(String type, String targetType, String stat, int modifier, String duration) {
		this.type = type;
		this.target = targetType;
		this.stat = stat;
		this.modifier = modifier;
		this.duration = duration;
	}

	public Effect(String type, String targetType) {
		this.type = type;
		this.target = targetType;
	}

	public Effect(String type, String targetType, Map<String, Integer> modifiers, String duration) {
		this.type = type;
		this.target = targetType;
		this.modifiers = modifiers;
		this.duration = duration;
	}

	// Getters
	public String getType() {
		return this.type;
	}

	public String getTarget() {
		return this.target;
	}

	public int getValue() {
		return this.value;
	}

	public String getStat() { return this.stat; }

	public int getModifier() { return this.modifier; }

	public String getDuration() { return this.duration; }

	public Map<String, Integer> getModifiers() { return this.modifiers; }

	public int apply(Bugemon target) {
		return 0;
	}
}
