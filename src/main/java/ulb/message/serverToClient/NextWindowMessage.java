package ulb.message.serverToClient;

import java.io.Serializable;

public class NextWindowMessage implements Serializable {
	public enum WindowType{
		MAIN_MENU,
		NEXT_ROOM,
		LEVEL_UP,
		GAME,
		REWARD
	}

	private WindowType nextWindow;

	public NextWindowMessage(WindowType nextWindow){
		this.nextWindow = nextWindow;
	}

	public WindowType getNextWindow(){return this.nextWindow;}
}
