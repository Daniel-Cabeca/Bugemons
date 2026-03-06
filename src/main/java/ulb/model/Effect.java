package ulb.model;

import java.util.Map;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

public class Effect {
	private final String type;
	private final String target;
	// for "soin"
	private int value;
	// for "stat modifier"
	private String stat;
	private int modifier;
	private String duration;
	// for "stat modifier multiple"
	private Map<String, Integer> modifiers;


	public Effect(String type, String targetType, int value) {
		this.type = type;
		this.target = targetType;
		this.value = value;
	}

	public Effect(String type, String targetType, String stat, int modifier, String duration) {
		this.type = type;
		this.target = targetType;
		this.stat = stat;
		this.modifier = modifier;
		this.duration = duration;
	}

	public Effect(String type, String targetType) {
		this.type = type;
		this.target = targetType;
	}

	public Effect(String type, String targetType, Map<String, Integer> modifiers, String duration) {
		this.type = type;
		this.target = targetType;
		this.modifiers = modifiers;
		this.duration = duration;
	}

	// Getters
	public String getType() {
		return this.type;
	}

	public String getTarget() {
		return this.target;
	}

	public int getValue() {
		return this.value;
	}

	public String getStat() { return this.stat; }

	public int getModifier() { return this.modifier; }

	public String getDuration() { return this.duration; }

	public Map<String, Integer> getModifiers() { return this.modifiers; }

	public int apply(Bugemon target) {
		switch (this.type) {
			case "soin":
				target.changeFightStats(new Stats(this.value, 0, 0, 0));
				break;
			case "stat_modifier":
				Stats statsChange = new Stats();
				switch (this.stat) {
					case "pv":
						statsChange.plus(new Stats(this.modifier, 0, 0, 0));
						break;
					case "attaque":
						statsChange.plus(new Stats(0, this.modifier, 0, 0));
						break;
					case "defense":
						statsChange.plus(new Stats(0, 0, this.modifier, 0));
						break;
					case "initiative":
						statsChange.plus(new Stats(0, 0, 0, this.modifier));
						break;
				}
				if (this.duration.equals("permanent")) {
					target.changeFightStats(statsChange);
				} else {
					// TODO: battle rounds and revert after one round
					target.changeFightStats(statsChange);
				}
				break;
			case "stat_modifier_multiple":
				Stats multipleStatsChange = new Stats();
				for (Map.Entry<String, Integer> entry : this.modifiers.entrySet()) {
					switch (entry.getKey()) {
						case "pv":
							multipleStatsChange.change(new Stats(entry.getValue(), 0, 0, 0));
							break;
						case "attaque":
							multipleStatsChange.change(new Stats(0, entry.getValue(), 0, 0));
							break;
						case "defense":
							multipleStatsChange.change(new Stats(0, 0, entry.getValue(), 0));
							break;
						case "initiative":
							multipleStatsChange.change(new Stats(0, 0, 0, entry.getValue()));
							break;
					}
				}
				if (this.duration.equals("permanent")) {
					target.changeFightStats(multipleStatsChange);
				} else {
					// TODO: battle rounds and revert after one round
					target.changeFightStats(multipleStatsChange);
				}
				break;
			case "reset_malus":
				target.removeStatsDebuffs();
				break;
			case "switch":
				break;
			default:
				return 0;
		}
		return 1;
	}
}
