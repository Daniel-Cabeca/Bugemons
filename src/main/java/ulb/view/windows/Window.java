package ulb.view.windows;

import ulb.communication.types.SwitchWindowMessage;
import ulb.view.ViewManager;

public abstract class Window {

	protected ViewManager viewManager;

	public void setViewManager(ViewManager viewManager) { this.viewManager = viewManager; }

	public void onLoad() {}

	protected void sendSwitchWindowMessage(String fxmlPath) {
		viewManager.handleMessage(new SwitchWindowMessage(fxmlPath));
	}

}
