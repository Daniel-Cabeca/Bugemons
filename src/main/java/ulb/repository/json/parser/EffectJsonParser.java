package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Map;

import ulb.model.effect.Effect;
import ulb.model.effect.EffectList;
import ulb.model.effect.EffectResetMalus;
import ulb.model.effect.EffectStatModifier;
import ulb.model.effect.EffectHeal;
import ulb.model.effect.EffectTarget;
import ulb.model.effect.EffectStatDuration;
import ulb.model.effect.EffectStatType;
import ulb.model.effect.EffectSwitch;
import ulb.repository.LoadException;

/**
 * Parses effects from json nodes.
 */
public class EffectJsonParser {
	/**
	 * Parses one effect from a json node.
	 *
	 * @param node The json node
	 * @return The parsed effect
	 * @throws LoadException If a parsing error occured
	 */
	public Effect parseOne(JsonNode node) throws LoadException {
		String typeStr = node.get("type").asText();

		switch(typeStr) {
			case "soin":
				return this.parseOneHeal(node);
			case "stat_modifier":
				return this.parseOneStatModifier(node);
			case "stat_modifier_multiple":
				return this.parseOneStatModifierMultiple(node);
			case "reset_malus":
				return this.parseOneResetMalus(node);
			case "switch":
				return this.parseOneSwitch(node);
			default:
				throw new LoadException("Unknown effect type: "+ typeStr);
		}
	}

	/**
	 * Parses a list of effects from a json node.
	 *
	 * @param node The json node
	 * @return A list of the parsed effects
	 * @throws LoadException If parsing failed
	 */
	public EffectList parseList(JsonNode node) throws LoadException {
		EffectList effects = new EffectList();

		for (JsonNode effectNode: node) {
			Effect effect = this.parseOne(effectNode);
			effects.add(effect);
		}

		return effects;
	}

	/**
	 * Parses an effect target from a json node.
	 *
	 * @param node The json node
	 * @return The corresponding target enum value
	 * @throws LoadException If the target is unrecognized
	 */
	public EffectTarget parseTarget(JsonNode node) throws LoadException {
		String targetStr = node.asText();

		switch(targetStr) {
			case "lanceur":
				return EffectTarget.OWN_BUGEMON;
			case "adversaire":
				return EffectTarget.OPPOSITE_BUGEMON;
			case "equipe":
				return EffectTarget.OWN_TEAM;
			default:
				throw new LoadException("Unknown effect target: "+ targetStr);
		}
	}

	/**
	 * Parses an effect duration from a json node.
	 *
	 * @param node The json node
	 * @return The corresponding duration enum value
	 * @throws LoadException If the duration is unrecognized
	 */
	public EffectStatDuration parseDuration(JsonNode node) throws LoadException {
		String durationStr = node.asText();

		switch(durationStr) {
			case "permanent":
				return EffectStatDuration.PERMANENT;
			case "tour", "1_tour":
				return EffectStatDuration.ROUND;
			default:
				throw new LoadException("Unrecognized effect duration: "+ durationStr);
		}
	}

	/**
	 * Parses a stat type from a json node.
	 *
	 * @param node The json node
	 * @return The corresponding stat type enum value
	 * @throws LoadException If the stat type is unrecognized
	*/
	public EffectStatType parseEffectStatType(String statStr) throws LoadException {
		switch(statStr) {
			case "pv":
				return EffectStatType.HP;

			case "attaque":
				return EffectStatType.ATTACK;

			case "defense":
				return EffectStatType.DEFENSE;

			case "initiative":
				return EffectStatType.INITIATIVE;

			default:
				throw new LoadException("Unrecognized effect target"+ statStr);
		}
	}

	/**
	 * Parses a stat type from a json node.
	 *
	 * @param node The json node
	 * @return The corresponding stat type enum value
	 * @throws LoadException If the stat type is unrecognized
	*/
	public EffectStatType parseEffectStatType(JsonNode node) throws LoadException {
		return this.parseEffectStatType(node.asText());
	}

	/**
	 * Parses one heal effect from a json node.
	 *
	 * @param node The json node
	 * @return The parsed effect
	 * @throws LoadException If parsing failed
	 */
	private Effect parseOneHeal(JsonNode node) throws LoadException {
		EffectTarget target = this.parseTarget(node.get("cible"));

		int value = node.get("valeur").asInt();

		return new EffectHeal(target, value);
	}

	/**
	 * Parses one stat modifier effect from a json node.
	 *
	 * @param node The json node
	 * @return The parsed effect
	 * @throws LoadException If parsing failed
	 */
	private Effect parseOneStatModifier(JsonNode node) throws LoadException {
		EffectTarget target = this.parseTarget(node.get("cible"));
		EffectStatDuration duration = this.parseDuration(node.get("duree"));

		EffectStatType EffectStatType = this.parseEffectStatType(node.get("stat"));
		int modifierValue = node.get("modificateur").asInt();
		Map<EffectStatType, Integer> modifiers = Map.of(EffectStatType, modifierValue);

		return new EffectStatModifier(target, duration, modifiers);
	}

	/**
	 * Parses one stat modifier effect that affects multiple stats from a json node.
	 *
	 * @param node The json node
	 * @return The parsed effect
	 * @throws LoadException If parsing failed
	 */
	private Effect parseOneStatModifierMultiple(JsonNode node) throws LoadException {
		EffectTarget target = this.parseTarget(node.get("cible"));
		EffectStatDuration duration = this.parseDuration(node.get("duree"));

		JsonNode modifiersNode = node.get("modificateurs");
		Map<EffectStatType, Integer> modifiers = new HashMap<>();
		modifiersNode.fields().forEachRemaining(entry -> modifiers.put(
				this.parseEffectStatType(entry.getKey()),
				entry.getValue().asInt()
			));

		return new EffectStatModifier(target, duration, modifiers);
	}

	/**
	 * Parses one reset malus effect from a json node.
	 *
	 * @param node The json node
	 * @return The parsed effect
	 * @throws LoadException If parsing failed
	 */
	private Effect parseOneResetMalus(JsonNode node) throws LoadException {
		EffectTarget target = this.parseTarget(node.get("cible"));

		return new EffectResetMalus(target);
	}

	/**
	 * Parses one switch effect from a json node.
	 *
	 * @param node The json node
	 * @return The parsed effect
	 * @throws LoadException If parsing failed
	 */
	private Effect parseOneSwitch(JsonNode node) throws LoadException {
		EffectTarget target = this.parseTarget(node.get("cible"));

		return new EffectSwitch(target);
	}
}
