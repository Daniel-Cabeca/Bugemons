package ulb.DTO.bugemon;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.stats.StatsDTO;
import ulb.model.type.Type;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Transferable Bugemon, used on the vue side.
 */
public record BugemonDTO(BugemonSpeciesDTO species, StatsDTO baseStats, StatsDTO fightStats, int xp, int level,
                         int remainingRewards) implements Serializable {
	/**
	 * Creates a BugemonDTO from a species, using its base stats as both base and fight stats.
	 *
	 * @param species The bugemon species
	 */
	public BugemonDTO(BugemonSpeciesDTO species) {
		this(species, species.baseStats(), species.baseStats(), 0, 1, 0);
	}
	public String getId() { return this.species().id(); }
	public String getName() { return this.species().name(); }
	public String getSpritePath() { return this.species().getSpritePath(); }
	public Type getType() { return this.species().type(); }
	public int getHp() { return this.fightStats().hp(); }
	public int getAttack() { return this.fightStats().attack(); }
	public int getDefense() { return this.fightStats().defense(); }
	public int getInitiative() { return this.fightStats().initiative(); }

	/**
	 * Finds an active ability by its id among this Bugemon's abilities.
	 *
	 * @param abilityId The id of the ability to find
	 * @return An optional containing the matching ability, or empty if not found
	 */
	public Optional<AbilityDTO> findActiveAbilityById(String abilityId) {
		for (AbilityDTO ability : this.getAbilities()) {
			if (ability != null && abilityId.equals(ability.id())) {
				return Optional.of(ability);
			}
		}
		return Optional.empty();
	}
	public List<AbilityDTO> getAbilities() { return this.species().abilities(); }
}