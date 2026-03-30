package ulb.communication.types;

import ulb.communication.Message;

import java.util.List;

/**
 * Message used by the view to send the chosen bugemons to the controller
 */
public class SetupTeamMessage implements Message {

    private List<String> selectedBugemons;

    public SetupTeamMessage(List<String> selectedBugemons) {
        this.selectedBugemons = selectedBugemons;
    }

    public List<String> getSelectedBugemons() { return selectedBugemons; }

    @Override
    public MessageType getMessageType() {
        return MessageType.SETUP_TEAM;
    }
}
