package ulb.DTO.player;

import java.io.Serializable;

/**
 * Transferable Player, used on the vue side.
 */
public class PlayerRegisterDTO implements Serializable{
	private String username;
	private String password;

	public PlayerRegisterDTO(String userName, String password) {
		this.username = userName;
		this.password = password;
	}

	public String getUsername() {return this.username;}
	public String getPassword() {return password;}
}
