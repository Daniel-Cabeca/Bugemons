package ulb.repository.inmemory;

import java.util.NoSuchElementException;

import ulb.repository.BugemonSpeciesRepository;
import ulb.model.bugemon.BugemonSpecies;import ulb.repository.json.IdSet;

public class BugemonSpeciesInMemoryRepository implements BugemonSpeciesRepository {
	private final IdSet<BugemonSpecies> species = new IdSet<>();

	/**
	 * Creates an empty species repository.
	 */
	public BugemonSpeciesInMemoryRepository() {
	}

	/**
	 * Creates a pre-filled species repository.
	 *
	 * @param species The species to fill the repository with.
	 * @throws IllegalArgumentException If trying to add duplicate species.
	 */
	public BugemonSpeciesInMemoryRepository(Iterable<BugemonSpecies> species) throws IllegalArgumentException {
		this.add(species);
	}

	/**
	 * Adds species to the repository.
	 *
	 * @param species The species to add.
	 * @throws IllegalArgumentException If trying to add duplicate species.
	 */
	public void add(Iterable<BugemonSpecies> species) throws IllegalArgumentException {
		for (BugemonSpecies speciesToAdd: species) {
			this.species.add(speciesToAdd);
		}
	}

	@Override
	public BugemonSpecies findById(String id) throws NoSuchElementException {
		return this.species.get(id);
	}

	@Override
	public Iterable<BugemonSpecies> findAll() {
		return this.species;
	}
}
