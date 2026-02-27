package ulb.model;

public class Effect {
	private final String type;
	private final String targetType;
	private int value;
	private String stat;
	private int modifier;
	private String duration;


	public Effect(String type, String targetType, int value) {
		this.type = type;
		this.targetType = targetType;
		this.value = value;
	}

	public Effect(String type, String targetType, String stat, int modifier, String duration) {
		this.type = type;
		this.targetType = targetType;
		this.stat = stat;
		this.modifier = modifier;
		this.duration = duration;
	}

	public Effect(String type, String targetType) {
		this.type = type;
		this.targetType = targetType;
	}

	// Getters
	public String getType() {
		return this.type;
	}

	public String getTargetType() {
		return this.targetType;
	}

	public int getValue() {
		return this.value;
	}

	public String getStat() { return this.stat; }

	public int getModifier() { return this.modifier; }

	public String getDuration() { return this.duration; }

	public int apply(Bugemon target) {
		return 0;
	}
}
