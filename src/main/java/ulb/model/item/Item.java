package ulb.model.item;

import ulb.model.Effect;
import ulb.model.Bugemon;

public class Item {
	private String id;
	private String name;        // Maps to "nom" in your JSON
	private String description;
	private String category;    // Maps to "categorie" in your JSON
	private Effect effect;      // Maps to "effet" in your JSON
	private String sprite;




	public Item(String id, String name, String description, String category, Effect effect, String sprite) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.category = category;
		this.effect = effect;
		this.sprite = sprite;
	}

	public String getId() { return this.id; }
	public String getName() { return this.name; }
	public String getDescription() { return this.description; }
	public String getCategory() { return this.category; }
	public Effect getEffect() { return this.effect; }
	public String getSprite() { return this.sprite; }


	public int use(Bugemon target) {
		return this.effect.apply(target);
	}
}
