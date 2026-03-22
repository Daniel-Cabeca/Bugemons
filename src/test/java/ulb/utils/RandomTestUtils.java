package ulb.utils;

import java.util.Random;

/**
 * Collection of utilities for writing tests for features using RNG.
 */
public class RandomTestUtils {
	public interface RandomCondition {
		boolean apply(Random random);
	}

	/**
	 * Returns a seed for a Random object that satisfies a given condition.
	 *
	 * @param condition The condition to satify
	 * @return A matching seed
	 */
	public static int findSeed(RandomCondition condition) {
		int seed = 0;
		while (!condition.apply(new Random(seed))) {
			seed += 1;
		}
		return seed;
	}

	/**
	 * Returns a Random object that satisfies a given condition.
	 *
	 * @param condition The condition to satify
	 * @return A matching seed
	 */
	public static Random findRandom(RandomCondition condition) {
		int seed = findSeed(condition);
		return new Random(seed);
	}
}
