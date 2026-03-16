package ulb.communication.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ulb.communication.Message;
import ulb.controller.action.Action;


public class ActionMessage implements Message {
    private Action action;

    public ActionMessage(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return this.action;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {

    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        
    }
}
