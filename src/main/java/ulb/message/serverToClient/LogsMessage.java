package ulb.message.serverToClient;

import java.io.Serializable;
import java.util.List;

public class LogsMessage implements Serializable {
    private List<Integer> HpsAfterFirstAction;
    private List<String> logs;

    public LogsMessage(List<Integer> HpsAfterFirstAction, List<String> logs){
        this.logs = logs;
    }

    public List<Integer> getHpsAfterFirstAction(){return this.HpsAfterFirstAction;}
    public List<String> getLogs(){return this.logs;}
}
