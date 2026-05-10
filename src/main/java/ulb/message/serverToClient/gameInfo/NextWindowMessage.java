package ulb.message.serverToClient.gameInfo;

import java.io.Serializable;

public class NextWindowMessage implements Serializable {
	private final WindowType nextWindow;

	public NextWindowMessage(WindowType nextWindow){
		this.nextWindow = nextWindow;
	}

	public WindowType getNextWindow(){return this.nextWindow;}
}
