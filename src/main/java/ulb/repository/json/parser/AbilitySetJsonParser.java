package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.repository.AbilityRepository;

/**
 * Object that parses ability sets from JSON nodes.
 */
public class AbilitySetJsonParser {
	/** The repository holding all existing abilities. */
	private final AbilityRepository abilityRepository;

	public AbilitySetJsonParser(AbilityRepository abilityRepository) {
		this.abilityRepository = abilityRepository;
	}

	/**
	 * Parses one ability set from a JSON node.
	 *
	 * @param node The JSON node
	 * @return The parsed ability set
	 * @throws LoadException If parsing failed
	 */
	public AbilitySet parseOne(JsonNode node) throws LoadException {
		if (node.size() != AbilitySet.SIZE) {
			throw new LoadException("Ability sets must have exactly " + AbilitySet.SIZE + " abilities.");
		}

		Ability[] abilities = new Ability[AbilitySet.SIZE];
		int i = 0;

		for (JsonNode abilityNode : node) {
			String abilityId = abilityNode.asText();
			try {
				Ability ability = this.abilityRepository.findById(abilityId);
				abilities[i++] = ability;

			} catch (EntityNotFoundException e) {
				throw new LoadException("Ability does not exist: " + abilityId);
			}
		}

		return new AbilitySet(abilities[0], abilities[1], abilities[2]);
	}
}
