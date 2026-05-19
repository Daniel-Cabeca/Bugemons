package ulb.DTO.player;

import java.io.Serializable;

/**
 * Transferable Player, used on the vue side.
 */
public record PlayerRegisterDTO(String username, String password) implements Serializable {
}
