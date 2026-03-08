package ulb.view.handler;

import java.util.HashMap;
import java.util.Map;

public final class WindowContainer {
	public Map<WindowName,Window> uiMap = new HashMap<WindowName, Window>();

	public Window getUI(WindowName uiName){
		return this.uiMap.get(uiName);
	}
}
