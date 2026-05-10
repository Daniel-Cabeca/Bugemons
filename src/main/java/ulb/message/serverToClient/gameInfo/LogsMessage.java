package ulb.message.serverToClient.gameInfo;

import java.io.Serializable;
import java.util.List;

public class LogsMessage implements Serializable {
    private final List<Integer> HpsAfterFirstAction;
    private final List<String> logs;

    public LogsMessage(List<Integer> HpsAfterFirstAction, List<String> logs){
		this.HpsAfterFirstAction = HpsAfterFirstAction;
        this.logs = logs;
    }

    public List<Integer> getHpsAfterFirstAction(){return this.HpsAfterFirstAction;}
    public List<String> getLogs(){return this.logs;}
}
