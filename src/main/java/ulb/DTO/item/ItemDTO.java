package ulb.DTO.item;

import ulb.DTO.effect.EffectDTO;

public class ItemDTO {
    private String id;
	private String name;
	private String description;
	private String category;
	private EffectDTO effect;
	private String sprite;

	public ItemDTO(String id, String name, String description, String category, EffectDTO effect, String sprite){
		this.id = id;
		this.name = name;
		this.description = description;
		this.category = category;
		this.effect = effect;
		this.sprite = sprite;
	}

	public String getId() {return id;}
	public String getName() {return name;}
	public String getDescription() {return description;}
	public String getCategory() {return category;}
	public EffectDTO getEffect() {return effect;}
	public String getSprite() {return sprite;}

	public void setId(String id) {this.id = id;}
	public void setName(String name) {this.name = name;}
	public void setDescription(String description) {this.description = description;}
	public void setCategory(String category) {this.category = category;}
	public void setEffect(EffectDTO effect) {this.effect = effect;}
	public void setSprite(String sprite) {this.sprite = sprite;}
}