package ulb.DTO.item;

import ulb.DTO.effect.EffectDTO;

import java.io.Serializable;

/**
 * Transferable Item, used on the vue side.
 */
public record ItemDTO(String id, String name, String description, String category, EffectDTO effect,
                      String sprite) implements Serializable {
}

