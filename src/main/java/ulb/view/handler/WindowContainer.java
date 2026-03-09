package ulb.view.handler;

import ulb.view.windows.CreateTeamWindow;
import ulb.view.windows.ModeWindow;

public final class WindowContainer {
	private ModeWindow modeWindow;
	private CreateTeamWindow createTeamWindow;

	public WindowContainer(){
		this.createTeamWindow = new CreateTeamWindow();
		this.modeWindow = new ModeWindow();
	}

	public Window getWindow(WindowName windowName){
		switch (windowName) {
			case ModeWindow:
				return modeWindow;
			case CreateTeamWindow:
				return createTeamWindow;
			default:
				return null;
		}
	}
}
