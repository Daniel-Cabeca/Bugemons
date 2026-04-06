package ulb.DTO.reward;

import java.io.Serializable;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.stats.StatsDTO;

public class RewardDTO implements Serializable {
    private BugemonDTO bugemon;
    private StatsDTO stats;

    public RewardDTO(BugemonDTO bugemon, StatsDTO stats){
        this.bugemon = bugemon;
        this.stats = stats;
    }

    public BugemonDTO getBugemon() {return bugemon;}
    public StatsDTO getStats() {return stats;}

    public void setBugemon(BugemonDTO bugemon) {this.bugemon = bugemon;}
    public void setStats(StatsDTO stats) {this.stats = stats;}
}
