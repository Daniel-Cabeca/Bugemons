package ulb.repository.inmemory;

import java.util.NoSuchElementException;

import ulb.repository.AbilityRepository;
import ulb.model.ability.Ability;

public class AbilityInMemoryRepository implements AbilityRepository {
	private final IdSet<Ability> abilities = new IdSet<>();

	/**
	 * Creates an empty ability repository.
	 */
	public AbilityInMemoryRepository() {
	}

	/**
	 * Creates a pre-filled ability repository.
	 *
	 * @param abilities The abilities to fill the repository with.
	 * @throws IllegalArgumentException If trying to add duplicate abilities.
	 */
	public AbilityInMemoryRepository(Iterable<Ability> abilities) throws IllegalArgumentException {
		this.add(abilities);
	}

	/**
	 * Adds abilities to the repository.
	 *
	 * @param abilities The abilities to add.
	 * @throws IllegalArgumentException If trying to add duplicate abilities.
	 */
	public void add(Iterable<Ability> abilities) throws IllegalArgumentException {
		for (Ability ability: abilities) {
			this.abilities.add(ability);
		}
	}

	@Override
	public Ability findById(String id) throws NoSuchElementException {
		return this.abilities.get(id);
	}
}
