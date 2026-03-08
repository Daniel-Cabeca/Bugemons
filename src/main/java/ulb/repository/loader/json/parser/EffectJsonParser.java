package ulb.repository.loader.json;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Map;

import ulb.model.Effect;
import ulb.model.Effect.EffectType;
import ulb.repository.loader.LoadFailureException;

/**
 * Parses effects from json nodes.
 */
class EffectJsonParser {
	/**
	 * Parses one effect from a json node.
	 *
	 * @param node The json node
	 * @throws LoadFailureException If a parsing error occured
	 */
	public Effect parseOne(JsonNode node) throws LoadFailureException {
		EffectType effectType = this.parseEffectType(node.get("type"));
		String target = node.get("cible").asText();

		Effect effect;

		switch (effectType) {
			case SOIN:
				effect = new Effect(effectType, Effect.EffectTarget.valueOf(target.toUpperCase()),
					Map.of(Effect.StatType.PV, node.get("valeur").asInt()), Effect.EffectDuration.PERMANENT);
				break;
			case STAT_MODIFIER:
				String duration = node.get("duree").asText().toUpperCase().replace("1_TOUR", "TOUR");
				effect = new Effect(effectType, Effect.EffectTarget.valueOf(target.toUpperCase()),
					Map.of(Effect.StatType.valueOf(node.get("stat").asText().toUpperCase()),
					node.get("modificateur").asInt()),
					Effect.EffectDuration.valueOf(duration));
				break;
			default:
				throw new LoadFailureException("Unhandled effect type");
		}

		return effect;
	}

	/**
	 * Parses a list of effects from a json node.
	 * TODO Implement effect lists. Right now the method just returns the first effect of the json data.
	 *
	 *
	 */
	public Effect parseList(JsonNode node) throws LoadFailureException {
		for (JsonNode effectNode: node) {
			return this.parseOne(effectNode);
		}

		return null;
	}

	/**
	 * Parses an effect type from a json node.
	 *
	 * @param node The json node
	 * @throws LoadFailureException If the type isn't recognized
	 */
	public EffectType parseEffectType(JsonNode node) throws LoadFailureException {
		String str = node.asText();

		switch(str) {
			case "soin":
				return EffectType.SOIN;
			case "stat_modifier":
				return EffectType.STAT_MODIFIER;
			default:
				throw new LoadFailureException("Unknown effect type: " + str);
		}
	}
}
