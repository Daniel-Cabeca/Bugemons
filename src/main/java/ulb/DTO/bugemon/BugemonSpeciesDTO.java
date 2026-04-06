package ulb.DTO.bugemon;

import java.util.List;
import java.util.ArrayList;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.stats.StatsDTO;
import ulb.model.type.Type;

public class BugemonSpeciesDTO {
    private String id;
	private String name;
	private Type type;
	private StatsDTO baseStats;
	private List<AbilityDTO> abilities;
	private String sprite;
	private boolean starter;

	public String getId() {return id;}
	public String getName() {return name;}
	public Type getType() {return type;}
	public StatsDTO getBaseStats() {return baseStats;}
	public List<AbilityDTO> getAbilities() {return abilities;}
	public AbilityDTO getAbility(int index){return this.abilities.get(index);}
	public String getSprite() {return sprite;}
	public boolean isStarter() {return starter;}

	public void setId(String id) {this.id = id;}
	public void setName(String name) {this.name = name;}
	public void setType(Type type) {this.type = type;}
	public void setBaseStats(StatsDTO baseStats) {this.baseStats = baseStats;}
	public void setAbilities(List<AbilityDTO> abilities) {this.abilities = abilities;}
	public void setSprite(String sprite) {this.sprite = sprite;}
	public void setStarter(boolean starter) {this.starter = starter;}

	public void addAbility(AbilityDTO ability){
		if (this.abilities == null){
			this.abilities = new ArrayList<AbilityDTO>();
		}
		this.abilities.add(ability);}
}
