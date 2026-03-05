package ulb.view;

import java.util.HashMap;
import java.util.Map;

public final class UIContainer {
	public Map<UIName,UI> uiMap = new HashMap<UIName, UI>();

	public UI getUI(UIName uiName){
		return this.uiMap.get(uiName);
	}
}
