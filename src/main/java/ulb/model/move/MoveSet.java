package ulb.model.move;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * Represents the set of moves usable by a Bugemon.
 */
public class MoveSet implements Iterable<Move> {
	private final Map<String, Move> moves = new HashMap<>();

	@Override
	public Iterator<Move> iterator() {
		return this.moves.values().iterator();
	}

	/**
	 * Gives the number of moves in the moveset.
	 *
	 * @return The number of moves
	 */
	public int size() {
		return this.moves.size();
	}

	/**
	 * Checks whether a move is present in the moveset.
	 *
	 * @param id The id of the move to check.
	 * @return True if the move is present, false otherwise
	 */
	public boolean contains(String id) {
		return this.moves.containsKey(id);
	}

	/**
	 * Checks whether a move is present in the moveset.
	 *
	 * @param move The move to check.
	 * @return True if the move is present, false otherwise
	 */
	public boolean contains(Move move) {
		return this.contains(move.getId());
	}

	/**
	 * Adds a new move to the moveset, if not already present.
	 *
	 * @param move The new move
	 */
	public void add(Move move) {
		this.moves.put(move.getId(), move);
	}

	/**
	 * Removes a move from the moveset, if present.
	 *
	 * @param id The id of the move to remove.
	 */
	public void remove(String id) {
		this.moves.remove(id);
	}

	/**
	 * Removes a move from the moveset, if present.
	 *
	 * @param move The move to remove.
	 */
	public void remove(Move move) {
		this.moves.remove(move.getId());
	}

	/**
	 * Swaps a known move for a new one.
	 *
	 * @param newMove The new move
	 * @param oldMove The old move
	 * @throws IllegalArgumentException If oldMove is not known.
	 */
	public void swap(Move newMove, Move oldMove) throws IllegalArgumentException {
		if (! this.contains(oldMove)) {
			throw new IllegalArgumentException("Cannot swap out a move that is not learned.");
		}

		this.remove(oldMove);
		this.add(newMove);
	}
}
