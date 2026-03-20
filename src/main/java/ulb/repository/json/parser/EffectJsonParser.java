package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Map;

import ulb.model.Effect;
import ulb.model.Effect.EffectType;
import ulb.model.Effect.EffectTarget;
import ulb.model.Effect.EffectDuration;

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
				return this.parseOneSoin(node);
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
	 * TODO Implement effect lists. Right now the method just returns the first effect of the json data.
	 *
	 * @param node The json node
	 * @return A list of the parsed effects
	 * @throws LoadException If parsing failed
	 */
	public Effect parseList(JsonNode node) throws LoadException {
		for (JsonNode effectNode: node) {
			return this.parseOne(effectNode);
		}

		return null;
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
				return EffectTarget.LANCEUR;
			case "adversaire":
				return EffectTarget.ADVERSAIRE;
			case "equipe":
				return EffectTarget.EQUIPE;
			default:
				throw new LoadException("Unknown effect target: "+ targetStr);
		}
	}

	/**
	 * Parses an effect duration from a json node
	 *
	 * @param node The json node
	 * @return The corresponding duration enum value
	 * @throws LoadException If the duration is unrecognized
	 */
	public EffectDuration parseDuration(JsonNode node) throws LoadException {
		String durationStr = node.asText();

		switch(durationStr) {
			case "permanent":
				return EffectDuration.PERMANENT;
			case "tour", "1_tour":
				return EffectDuration.TOUR;
			default:
				throw new LoadException("Unrecognized effect duration: "+ durationStr);
		}
	}

	private Effect parseOneSoin(JsonNode node) throws LoadException {
		EffectType type = EffectType.SOIN;
		EffectTarget target = this.parseTarget(node.get("cible"));
		EffectDuration duration = EffectDuration.PERMANENT;

		Map<Effect.StatType, Integer> modifiers = Map.of(Effect.StatType.PV, node.get("valeur").asInt());

		return new Effect(type, target, modifiers, duration);
	}

	private Effect parseOneStatModifier(JsonNode node) throws LoadException {
		EffectType type = EffectType.STAT_MODIFIER;
		EffectTarget target = this.parseTarget(node.get("cible"));
		EffectDuration duration = this.parseDuration(node.get("duree"));

		Map<Effect.StatType, Integer> modifiers = Map.of(Effect.StatType.valueOf(node.get("stat").asText().toUpperCase()), node.get("modificateur").asInt());

		return new Effect(type, target, modifiers, duration);
	}

	private Effect parseOneStatModifierMultiple(JsonNode node) throws LoadException {
		EffectType type = EffectType.STAT_MODIFIER;
		EffectTarget target = this.parseTarget(node.get("cible"));
		EffectDuration duration = this.parseDuration(node.get("duree"));

		JsonNode modifiersNode = node.get("modificateurs");
		Map<Effect.StatType, Integer> modifiers = new HashMap<>();
		modifiersNode.fields().forEachRemaining(entry -> modifiers.put(Effect.StatType.valueOf(entry.getKey().toUpperCase()), entry.getValue().asInt()));

		return new Effect(type, target, modifiers, duration);
	}

	private Effect parseOneResetMalus(JsonNode node) throws LoadException {
		EffectType type = EffectType.RESET_MALUS;
		EffectTarget target = this.parseTarget(node.get("cible"));
		EffectDuration duration = EffectDuration.TOUR;

		Map<Effect.StatType, Integer> modifiers = Map.of();

		return new Effect(type, target, modifiers, duration);
	}

	private Effect parseOneSwitch(JsonNode node) throws LoadException {
		EffectType type = EffectType.SWITCH;
		EffectTarget target = this.parseTarget(node.get("cible"));
		EffectDuration duration = EffectDuration.TOUR;

		Map<Effect.StatType, Integer> modifiers = Map.of();

		return new Effect(type, target, modifiers, duration);
	}
}
