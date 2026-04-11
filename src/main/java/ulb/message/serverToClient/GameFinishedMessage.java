package ulb.message.serverToClient;

import java.io.Serializable;

public class GameFinishedMessage implements Serializable {
    private boolean isGameFinished;

    public GameFinishedMessage(boolean isGameFinished){
        this.isGameFinished = isGameFinished;
    }

    public boolean isGameFinished(){return this.isGameFinished;}
}
