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
		if (move == Type.FLORA && target == Type.AQUA) {
			this.value = Value.HIGH;
		}
		else {
			this.value = Value.NORMAL;
		}
	}

	public Value getValue() { return this.value; }
}
