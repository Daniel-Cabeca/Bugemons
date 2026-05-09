package ulb.message.serverToClient.gameInfo;

import java.io.Serializable;

public class GameFinishedMessage implements Serializable {
    private final boolean isGameFinished;

    public GameFinishedMessage(boolean isGameFinished){
        this.isGameFinished = isGameFinished;
    }

    public boolean isGameFinished(){return this.isGameFinished;}
}
