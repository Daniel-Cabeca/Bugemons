package ulb.view.windows;

import java.io.IOException;

import ulb.communication.Message;
import ulb.communication.types.SwitchWindowMessage;
import ulb.controller.GameController;
import ulb.view.ViewManager;

import javax.swing.text.View;

public abstract class Window {

	protected ViewManager viewManager;

	public void setViewManager(ViewManager viewManager) { this.viewManager = viewManager; }

	public void onLoad() {}

}
