package ulb.view.windows;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;

public class SocialPanel {

    @FXML
    private VBox invitePane;
    @FXML
    private VBox friendsPane;
    @FXML
    private VBox chatPane;
    @FXML
    private VBox requestsPane;
    @FXML
    private TextField inviteField;
    @FXML
    private Label inviteStatus;
    @FXML
    private ListView<String> friendsListView;
    @FXML
    private ListView<String> chatFriendsList;
    @FXML
    private ListView<String> chatMessagesList;
    @FXML
    private TextField chatMessageField;
    @FXML
    private ListView<String> requestsListView;

    private ViewListener viewListener;
    private String selectedChatFriend;
    private Timeline chatRefresh;

    public void setViewListener(ViewListener viewListener) { this.viewListener = viewListener; }

    /**
     * Initializes the fonts, chats of the social panel.
     */
    @FXML
    private void initialize() {
        chatRefresh = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            if (selectedChatFriend != null) {
                viewListener.onChatFriendSelected(selectedChatFriend);
            }
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
                viewListener.onChatFriendSelected(selected);
            }
        });
    }

    /**
     * Updates the friends list in both friends and chat views.
     * @param List<String> the list of friends that need to be shown.
     */
    public void setFriendsList(List<String> friends) {
        friendsListView.getItems().setAll(friends);
        chatFriendsList.getItems().setAll(friends);
    }

    /**
     * Updates the list of incoming friend requests.
     * @param List<String> the list of requests that need to be shown.
     */
    public void setRequests(List<String> requests) {
        requestsListView.getItems().setAll(requests);
    }

    /**
     * Updates the displayed chat messages.
     * @param List<String> messages the list of messages that need to be shown.
     */
    public void setMessages(List<String> messages) {
        chatMessagesList.getItems().setAll(messages);
    }

    public void setInviteStatus(String status) { inviteStatus.setText(status); }

    public void clearInviteField() { inviteField.clear(); }

    public void clearChatField() { chatMessageField.clear(); }

    /**
     * Displays the invite panel.
     */
    @FXML
    private void showInvite() {
        chatRefresh.stop();
        show(invitePane, friendsPane, chatPane, requestsPane);
    }

    /**
     * Displays the friends panel and refreshes the friends list.
     */
    @FXML
    private void showFriends() {
        chatRefresh.stop();
        show(friendsPane, invitePane, chatPane, requestsPane);
        viewListener.refreshFriends();
    }

    /**
     * Displays the chat panel and starts chat refresh.
     */
    @FXML
    private void showChat() {
        show(chatPane, invitePane, friendsPane, requestsPane);
        chatRefresh.play();
        viewListener.refreshFriends();
    }

    /**
     * Displays the friend requests panel.
     */
    @FXML
    private void showRequests() {
        chatRefresh.stop();
        show(requestsPane, invitePane, friendsPane, chatPane);
        viewListener.onRequestsOpened();
    }

    /**
     * Shows one panel and hides the others.
     * @param VBox visible, the panel that needs to be shown.
     * @param VBox hidden, the panel that needs to be hidden.
     */
    private void show(VBox visible, VBox... hidden) {
        visible.setVisible(true);
        for (VBox v : hidden) {
            v.setVisible(false);
        }
    }

    /**
     * Handles sending a friend invite.
     */
    @FXML
    private void handleInvite() {
        String target = inviteField.getText().trim();
        if (!target.isEmpty()) {
            viewListener.onInvite(target);
        }
    }

    /**
     * Handles accepting a selected friend request.
     */
    @FXML
    private void handleAccept() {
        String sender = requestsListView.getSelectionModel().getSelectedItem();
        if (sender != null) {
            viewListener.onAccept(sender);
        }
    }

    /**
     * Handles declining a selected friend request.
     */
    @FXML
    private void handleDecline() {
        String sender = requestsListView.getSelectionModel().getSelectedItem();
        if (sender != null) {
            viewListener.onDecline(sender);
        }
    }

    /**
     * Handles sending a chat message.
     */
    @FXML
    private void handleChatSend() {
        String content = chatMessageField.getText().trim();
        if (!content.isEmpty() && selectedChatFriend != null) {
            viewListener.onSendMessage(selectedChatFriend, content);
        }
    }

    /**
     * Handles closing the social panel.
     */
    @FXML
    private void handleClose() {
        viewListener.onClose();
    }

    public interface ViewListener {
        void onClose();
        void onDecline(String sender);
        void onAccept(String sender);
        void onInvite(String target);
        void onChatFriendSelected(String friend);
        void onRequestsOpened();
        void refreshFriends();
        void onSendMessage(String friend, String content);
    }
}
