package ulb.view.windows;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import ulb.controller.ClientController;
import ulb.model.chat.ChatMessage;

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
    private Timeline chatRefresh;

    public void setStage(Stage stage) { this.stage = stage; }

    @FXML
    private void initialize() {
        chatRefresh = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            if (selectedChatFriend != null) loadMessages();
        }));
        chatRefresh.setCycleCount(Timeline.INDEFINITE);

        chatMessagesList.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(item);
                if (item.startsWith("Vous")) {
                    setStyle("-fx-text-fill: #3050d8;");
                } else {
                    setStyle("-fx-text-fill: #c02020;");
                }
            }
        });

        chatFriendsList.setOnMouseClicked(e -> {
            String selected = chatFriendsList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedChatFriend = selected;
                loadMessages();
            }
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
        String friend = selectedChatFriend;
        new Thread(() -> {
            List<ChatMessage> msgs = clientController.getChatMessages(friend);
            Platform.runLater(() -> {
                chatMessagesList.getItems().clear();
                for (ChatMessage msg : msgs) {
                    String prefix = msg.getSenderUsername().equals(me) ? "Vous" : msg.getSenderUsername();
                    chatMessagesList.getItems().add(prefix + ": " + msg.getContent());
                }
            });
        }).start();
    }

    @FXML
    private void showInvite() {
        chatRefresh.stop();
        show(invitePane, friendsPane, chatPane, requestsPane);
    }

    @FXML
    private void showFriends() {
        chatRefresh.stop();
        show(friendsPane, invitePane, chatPane, requestsPane);
    }

    @FXML
    private void showChat() {
        show(chatPane, invitePane, friendsPane, requestsPane);
        chatRefresh.play();
    }

    @FXML
    private void showRequests() {
        chatRefresh.stop();
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
        chatMessageField.clear();
        String friend = selectedChatFriend;
        new Thread(() -> {
            clientController.sendChatMessage(friend, content);
            loadMessages();
        }).start();
    }

    @FXML
    private void handleClose() { stage.close(); }
}
