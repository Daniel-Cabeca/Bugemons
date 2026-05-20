package ulb.model.item;

import ulb.model.HasId;
import ulb.model.battle.Battle;
import ulb.model.effect.Effect;

/**
 * Consumable item with an associated battle effect.
 */
public class Item implements HasId {
	private final String id;
	private final String name;
	private final String description;
	private final String category;
	private final Effect effect;
	private final String sprite;

	/**
	 * Creates an item definition.
	 *
	 * @param id Item id
	 * @param name Item name
	 * @param description Item description
	 * @param category Item category
	 * @param effect Item effect
	 * @param sprite Item sprite path
	 */
	public Item(String id, String name, String description, String category, Effect effect, String sprite) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.category = category;
		this.effect = effect;
		this.sprite = sprite;
	}

	/** {@inheritDoc} */
	@Override
	public String getId() { return this.id; }


	public String getName() { return this.name; }

	public String getDescription() { return this.description; }

	public String getCategory() { return this.category; }

	public Effect getEffect() { return this.effect; }

	public String getSprite() { return this.sprite; }

	/**
	 * Applies this item effect in battle.
	 *
	 * @param battle Current battle
	 * @param team Acting participant label
	 */
	public void use(Battle battle, Battle.ParticipantLabel team) {
		this.effect.apply(battle, team);
	}

}
