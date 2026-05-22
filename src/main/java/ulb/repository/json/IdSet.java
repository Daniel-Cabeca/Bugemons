package ulb.repository.json;

import ulb.exceptions.EntityNotFoundException;
import ulb.model.HasId;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Container for objects with a String id field for fast lookup.
 * Is not ordered. Entries are unique.
 */
public class IdSet<T extends HasId> implements Iterable<T> {
	/** Map associating each entry's id to its instance. */
	private final Map<String, T> entries = new HashMap<>();

	/**
	 * Gets an entry matching the requested id.
	 *
	 * @param id The entry's id
	 * @return The matching entry
	 * @throws EntityNotFoundException If no match was found for the id
	 */
	public T get(String id) throws EntityNotFoundException {
		T entry = this.entries.get(id);

		if (entry == null) {
			throw new EntityNotFoundException("Entry", id);
		}

		return entry;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return this.entries.values().iterator();
	}

	/**
	 * Adds an entry to the set.
	 *
	 * @param entry The entry to add
	 * @throws IllegalArgumentException If an entry with the same id is already present
	 */
	public void add(T entry) throws IllegalArgumentException {
		T oldEntry = this.entries.putIfAbsent(entry.getId(), entry);

		if (oldEntry != null) {
			throw new IllegalArgumentException("Entry already exists: " + entry.getId());
		}
	}
}
