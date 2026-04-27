package ulb.DTO.reward;

import java.io.Serializable;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.stats.StatsDTO;

/**
 * Transferable Reward, used on the vue side.
 */
public record RewardDTO (BugemonDTO bugemon, StatsDTO stats) implements Serializable {}
