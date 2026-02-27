package ulb.model.type;

public abstract class Effectiveness {
	public enum Category {
		HIGH,
		NORMAL,
		LOW,
	}

	/**
	 * Gives the effectiveness category corresponding to the type of a move and the type of the target Bugemon.
	 *
	 * @param move The type of the move
	 * @param target The type of the target Bugemon
	 * @return The effectiveness category
	 */
	public static Category getCategory(Type move, Type target) {
		switch (move) {
			case FLORA:
				if (target == Type.AQUA) {
					return Category.HIGH;
				} else if (target == Type.LITHO) {
					return Category.LOW;
				}
				break;
			case AQUA:
				if (target  == Type.PYRO) {
					return Category.HIGH;
				}
				else if (target == Type.FLORA) {
					return Category.LOW;
				}
				break;
			case PYRO:
				if (target  == Type.LITHO) {
					return Category.HIGH;
				}
				else if (target == Type.AQUA) {
					return Category.LOW;
				}
				break;
			case LITHO:
				if (target  == Type.FLORA) {
					return Category.HIGH;
				}
				else if (target == Type.PYRO) {
					return Category.LOW;
				}
				break;
			default:
				break;
		}

		return Category.NORMAL;
	}

	/**
	 * Gives the multiplicative factor for damage calculations corresponding to the given effectiveness category.
	 *
	 * @param category The effectiveness category
	 * @return The multiplicative factor
	 */
	public static float getFactor(Category category) {
		switch(category) {
			case HIGH:
				return 1.5f;

			case LOW:
				return 0.5f;

			default:
				return 1.0f;
		}
	}

	/**
	 * Gives the multiplicative corresponding to the type of a move and the type of the target Bugemon.
	 *
	 * @param move The type of the move
	 * @param target The type of the target Bugemon
	 * @return The multiplicative factor
	 */
	public static float getFactor(Type move, Type target) {
		Category category = getCategory(move, target);
		return getFactor(category);
	}
}
