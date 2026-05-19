package ulb.message.response.gameInfo;

import ulb.message.response.Response;

public class NextWindowResponse extends Response {
	private final WindowType nextWindow;

	public NextWindowResponse(WindowType nextWindow) {
		this.nextWindow = nextWindow;
	}

	public WindowType getNextWindow() { return this.nextWindow; }
}
