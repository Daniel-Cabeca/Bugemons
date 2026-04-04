package ulb.DTO.bugemon;

import ulb.DTO.stats.StatsDTO;

public class BugemonDTO {
    private BugemonSpeciesDTO species;
	private StatsDTO baseStats;
	private StatsDTO fightStats;
	private int xp;
	private int level;
	private int remainingRewards;

	public BugemonDTO(BugemonSpeciesDTO species, StatsDTO baseStats, StatsDTO fightStats, int xp, int level, int remainingRewards){
		this.species = species;
		this.baseStats = baseStats;
		this.fightStats = fightStats;
		this.xp = xp;
		this.level = level;
		this.remainingRewards = remainingRewards;
	}

	public BugemonSpeciesDTO getSpecies() {return species;}
	public StatsDTO getBaseStats() {return baseStats;}
	public StatsDTO getFightStats() {return fightStats;}
	public int getXp() {return xp;}
	public int getLevel() {return level;}
	public int getRemainingRewards() {return remainingRewards;}

	public void setSpecies(BugemonSpeciesDTO species) {this.species = species;}
	public void setBaseStats(StatsDTO baseStats) {this.baseStats = baseStats;}
	public void setFightStats(StatsDTO fightStats) {this.fightStats = fightStats;}
	public void setXp(int xp) {this.xp = xp;}
	public void setLevel(int level) {this.level = level;}
	public void setRemainingRewards(int remainingRewards) {this.remainingRewards = remainingRewards;}
}
