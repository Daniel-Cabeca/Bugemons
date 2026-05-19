package ulb.DTO.reward;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.stats.StatsDTO;

import java.io.Serializable;

/**
 * Transferable Reward, used on the vue side.
 */
public record RewardDTO(BugemonDTO bugemon, StatsDTO stats) implements Serializable {
}
