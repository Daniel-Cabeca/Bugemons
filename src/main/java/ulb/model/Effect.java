package ulb.model;

import ulb.model.type.Type;

public class Effect {
	private final Type type;
	private final Type targetType;
	private final int value;



	public Effect(Type type, Type targetType, int value) {
		this.type = type;
		this.targetType = targetType;
		this.value = value;
	}
	// Getters
	public Type getType() { return this.type; }
	public Type getTargetType() { return this.targetType; }
	public int getValue() { return this.value; }

	public int apply(Bugemon target) {return 0;}
}
