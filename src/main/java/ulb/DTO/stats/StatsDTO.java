package ulb.DTO.stats;

import java.io.Serializable;

/**
 * Transferable Stats, used on the vue side.
 */
public record StatsDTO (int hp, int attack, int defense, int initiative) implements Serializable {}
