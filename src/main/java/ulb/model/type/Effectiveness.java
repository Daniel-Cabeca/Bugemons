package ulb.model.type;

public class Effectiveness {
	public enum Value {
		HIGH, // 1.5
		NORMAL, // 1.0
		LOW, // 0.5
	}

	private Value value;

	public Effectiveness(Value value) {
		this.value = value;
	}

	public Effectiveness(Type move, Type target) {
		switch (move) {
			case FLORA:
				if (target == Type.AQUA) {
					this.value = Value.HIGH;
				} else if (target == Type.LITHO) {
					this.value = Value.LOW;
				} else {
					this.value = Value.NORMAL;
				}
				break;
			case AQUA:
				if (target  == Type.PYRO) {
					this.value = Value.HIGH;
				}
				else if (target == Type.FLORA) {
					this.value = Value.LOW;
				}
				else {
					this.value = Value.NORMAL;
				}
				break;
			case PYRO:
				if (target  == Type.LITHO) {
					this.value = Value.HIGH;
				}
				else if (target == Type.AQUA) {
					this.value = Value.LOW;
				}
				else {
					this.value = Value.NORMAL;
				}
				break;
			case LITHO:
				if (target  == Type.FLORA) {
					this.value = Value.HIGH;
				}
				else if (target == Type.PYRO) {
					this.value = Value.LOW;
				}
				else {
					this.value = Value.NORMAL;
				}
				break;
			default:
				break;
		}
	}

	public float getFactor() {
		switch(this.getValue()) {
			case HIGH:
				return 1.5f;

			case LOW:
				return 0.5f;

			default:
				return 1.0f;
		}
	}

	public Value getValue() { return this.value; }
}
