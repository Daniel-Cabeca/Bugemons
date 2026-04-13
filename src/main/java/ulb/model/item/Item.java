package ulb.model.item;

import ulb.model.HasId;
import ulb.model.effect.Effect;
import ulb.model.battle.Battle;
import ulb.model.effect.EffectTarget;

/**
 * Consumable item with an associated battle effect.
 */
public class Item implements HasId {
	private String id;
	private String name;
	private String description;
	private String category;
	private Effect effect;
	private String sprite;

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

	/** Returns the item display name. */
	public String getName() { return this.name; }
	/** Returns the item description. */
	public String getDescription() { return this.description; }
	/** Returns the item category. */
	public String getCategory() { return this.category; }
	/** Returns the item effect. */
	public Effect getEffect() { return this.effect; }
	/** Returns the item sprite path. */
	public String getSprite() { return this.sprite; }

	/**
	 * Applies this item effect in battle.
	 *
	 * @param battle Current battle
	 * @param team Acting participant label
	 */
	public void use(Battle battle, Battle.ParticipantLabel team){
		this.effect.apply(battle, team);
	}

	/**
	 * Returns the target scope of this item's effect.
	 *
	 * @return Effect target scope
	 */
	public EffectTarget getTarget(){
		return this.effect.getTarget();
	}
}
