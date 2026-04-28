package ulb.DTO.item;

import java.io.Serializable;

import ulb.DTO.effect.EffectDTO;

/**
 * Transferable Item, used on the vue side.
 */
public record ItemDTO (String id,
					   String name,
					   String description,
					   String category,
					   EffectDTO effect,
					   String sprite) implements Serializable{ }

