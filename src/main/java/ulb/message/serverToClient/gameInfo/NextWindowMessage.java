package ulb.message.serverToClient.gameInfo;

import java.io.Serializable;

public class NextWindowMessage implements Serializable {
	public enum WindowType{
		MAIN_MENU,
		NEXT_ROOM,
		LEVEL_UP,
		GAME,
		REWARD,
		FLOOR
	}

	private final WindowType nextWindow;

	public NextWindowMessage(WindowType nextWindow){
		this.nextWindow = nextWindow;
	}

	public WindowType getNextWindow(){return this.nextWindow;}
}
