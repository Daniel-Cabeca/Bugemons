package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.controller.ClientController;
import ulb.model.chat.ChatMessage;
import ulb.service.ServiceLoader;

import java.util.List;

public class SocialPanel {

    @FXML private VBox invitePane;
    @FXML private VBox friendsPane;
    @FXML private VBox chatPane;
    @FXML private VBox requestsPane;
    @FXML private TextField inviteField;
    @FXML private Label inviteStatus;
    @FXML private ListView<String> friendsListView;
    @FXML private ListView<String> chatFriendsList;
    @FXML private ListView<String> chatMessagesList;
    @FXML private TextField chatMessageField;
    @FXML private ListView<String> requestsListView;

    private Stage stage;
    private ClientController clientController;
    private String selectedChatFriend;

    public void setStage(Stage stage) { this.stage = stage; }

    @FXML
    private void initialize() {
        chatFriendsList.setOnMouseClicked(e -> {
            selectedChatFriend = chatFriendsList.getSelectionModel().getSelectedItem();
            if (selectedChatFriend != null) loadMessages();
        });
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
        List<String> friends = clientController.getFriendsList();
        friendsListView.getItems().setAll(friends);
        chatFriendsList.getItems().setAll(friends);
    }

    private void loadMessages() {
        String me = clientController.getPlayer().getName();
        chatMessagesList.getItems().clear();
        for (ChatMessage msg : ServiceLoader.getChatService().getMessages(me, selectedChatFriend)) {
            String prefix = msg.getSenderUsername().equals(me) ? "Vous" : msg.getSenderUsername();
            chatMessagesList.getItems().add(prefix + ": " + msg.getContent());
        }
    }

    @FXML private void showInvite()   { show(invitePane,   friendsPane, chatPane, requestsPane); }
    @FXML private void showFriends()  { show(friendsPane,  invitePane,  chatPane, requestsPane); }
    @FXML private void showChat()     { show(chatPane,     invitePane,  friendsPane, requestsPane); }
    @FXML
    private void showRequests() {
        show(requestsPane, invitePane, friendsPane, chatPane);
        requestsListView.getItems().setAll(clientController.getFriendRequests());
    }

    private void show(VBox visible, VBox... hidden) {
        visible.setVisible(true);
        for (VBox v : hidden) v.setVisible(false);
    }

    @FXML
    private void handleInvite() {
        String target = inviteField.getText().trim();
        if (target.isEmpty()) return;
        boolean ok = clientController.sendFriendRequest(target);
        inviteStatus.setText(ok ? "Demande envoyée !" : "Utilisateur introuvable.");
        if (ok) inviteField.clear();
    }

    @FXML
    private void handleAccept() {
        String sender = requestsListView.getSelectionModel().getSelectedItem();
        if (sender == null) return;
        clientController.acceptFriendRequest(sender);
        showRequests();
    }

    @FXML
    private void handleDecline() {
        String sender = requestsListView.getSelectionModel().getSelectedItem();
        if (sender == null) return;
        clientController.declineFriendRequest(sender);
        showRequests();
    }

    @FXML
    private void handleChatSend() {
        String content = chatMessageField.getText().trim();
        if (content.isEmpty() || selectedChatFriend == null) return;
        ServiceLoader.getChatService().sendMessage(clientController.getPlayer().getName(), selectedChatFriend, content);
        chatMessageField.clear();
        loadMessages();
    }

    @FXML private void handleClose() { stage.close(); }
}
