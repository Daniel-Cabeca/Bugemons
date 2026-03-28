package ulb.communication.types;

import ulb.communication.Message;

import java.util.List;

public class SetupTeamMessage implements Message {

    List<String> selectedBugemons;

    public SetupTeamMessage(List<String> selectedBugemons) {
        this.selectedBugemons = selectedBugemons;
    }

    public List<String> getSelectedBugemons() { return selectedBugemons; }

}
