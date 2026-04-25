package ulb.DTO.ability;

import java.io.Serializable;
import java.util.List;

import ulb.DTO.effect.EffectDTO;
import ulb.model.type.Type;

/**
 * Transferable Ability, used on the vue side.
 */
public class AbilityDTO implements Serializable{
    private String id;
	private String name;
	private Type type;
	private String description;
	private int power;
	private List<EffectDTO> effects;

	public AbilityDTO(String id, String name, Type type, String description, int power, List<EffectDTO> effects){
		this.id = id;
		this.name = name;
		this.type = type;
		this.description = description;
		this.power = power;
		this.effects = effects;
	}

	public String getId(){return this.id;}
	public String getName(){return this.name;}
	public Type getType(){return this.type;}
	public String getDescription(){return this.description;}
	public String getAccurateDescription() {return this.description + "\n Puissance:  " + this.power;}
	public int getPower(){return this.power;}
	public List<EffectDTO> getEffects(){return this.effects;}
	public EffectDTO getEffect(int index){return this.effects.get(index);}

	public void setId(String id){this.id = id;}
	public void setName(String name){this.name = name;}
	public void setType(Type type){this.type = type;}
	public void setDescription(String description){this.description = description;}
	public void setPower(int power){this.power = power;}
	public void setEffects(List<EffectDTO> effects){this.effects = effects;}
	public void addEffect(EffectDTO effect){this.effects.add(effect);}
}
