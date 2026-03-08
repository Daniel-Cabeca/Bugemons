package ulb.view.handler;

import ulb.view.windows.CreateTeamWindow;

public final class WindowContainer {
	CreateTeamWindow createTeamWindow;

	public WindowContainer(){
		createTeamWindow = new CreateTeamWindow();
	}

	public Window getWindow(WindowName windowName){
		switch (windowName) {
			case CreateTeamWindow:
				return createTeamWindow;
			default:
				return null;
		}
	}
}
