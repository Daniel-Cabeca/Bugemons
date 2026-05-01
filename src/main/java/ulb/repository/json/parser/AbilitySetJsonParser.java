package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.NoSuchElementException;

import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;

import ulb.repository.AbilityRepository;
import ulb.exceptions.LoadException;

/**
 * Object that parses ability sets from json nodes.
 */
public class AbilitySetJsonParser {
	private AbilityRepository abilityRepository;

	public AbilitySetJsonParser(AbilityRepository abilityRepository) {
		this.abilityRepository = abilityRepository;
	}

	/**
	 * Parses one ability set from a json node.
	 *
	 * @param node The json node
	 * @return The parsed ability set
	 * @throws LoadException If parsing failed
	 */
	public AbilitySet parseOne(JsonNode node) throws LoadException {
		if (node.size() != AbilitySet.SIZE) {
			throw new LoadException("Ability sets must have exactly "+ AbilitySet.SIZE +" abilities.");
		}

		Ability[] abilities = new Ability[AbilitySet.SIZE];
		int i = 0;

		for (JsonNode abilityNode: node) {
			String abilityId = abilityNode.asText();

			try {
				Ability ability = this.abilityRepository.findById(abilityId);
				abilities[i++] = ability;
			} catch (NoSuchElementException e) {
				throw new LoadException("Ability does not exist: "+ abilityId);
			}
		}

		// Couldn't find a more adaptable way of expanding the array.
		return new AbilitySet(
			abilities[0],
			abilities[1],
			abilities[2]
		);
	}
}
