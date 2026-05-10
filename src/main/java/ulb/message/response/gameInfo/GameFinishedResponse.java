package ulb.message.response.gameInfo;

import ulb.message.response.Response;

public class GameFinishedResponse extends Response {
    private final boolean isGameFinished;

    public GameFinishedResponse(boolean isGameFinished){
        this.isGameFinished = isGameFinished;
    }

    public boolean isGameFinished(){return this.isGameFinished;}
}
