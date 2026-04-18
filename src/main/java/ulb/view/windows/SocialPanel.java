package ulb.view.windows;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
    private VBox friendRequestsPane;
	@FXML
	private VBox battleRequestsPane;
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
    private ListView<String> friendRequestsListView;
	@FXML
    private ListView<String> battleRequestsListView;

    private ViewListener viewListener;
    private String selectedChatFriend;
    private Timeline chatRefresh;

    public void setViewListener(ViewListener viewListener) { this.viewListener = viewListener; }

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

        friendsListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                Label nameLabel = new Label(item);
                nameLabel.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold;");

                Button challengeButton = new Button("Défier");
                challengeButton.setStyle("-fx-background-color: #f8f8d8; -fx-text-fill: #000000;"
                        + " -fx-border-color: #000000; -fx-border-width: 2;"
                        + " -fx-padding: 4 10; -fx-cursor: hand;");
                challengeButton.setOnAction(e -> viewListener.onChallengeFriend(item));

                HBox row = new HBox(12, nameLabel, challengeButton);
                row.setAlignment(Pos.CENTER_LEFT);

                setText(null);
                setGraphic(row);
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

    public void setFriendsList(List<String> friends) {
        friendsListView.getItems().setAll(friends);
        chatFriendsList.getItems().setAll(friends);
    }

    public void setFriendRequests(List<String> requests) {
        friendRequestsListView.getItems().setAll(requests);
    }

	public void setBattleRequests(List<String> battleRequests) {
        battleRequestsListView.getItems().setAll(battleRequests);
    }

    public void setMessages(List<String> messages) {
        chatMessagesList.getItems().setAll(messages);
    }

    public void setInviteStatus(String status) { inviteStatus.setText(status); }

    public void clearInviteField() { inviteField.clear(); }

    public void clearChatField() { chatMessageField.clear(); }

    @FXML
    private void showInvite() {
        chatRefresh.stop();
        show(invitePane, friendsPane, chatPane, friendRequestsPane, battleRequestsPane);
    }

    @FXML
    private void showFriends() {
        chatRefresh.stop();
        show(friendsPane, invitePane, chatPane, friendRequestsPane, battleRequestsPane);
        viewListener.refreshFriends();
    }

    @FXML
    private void showChat() {
        show(chatPane, invitePane, friendsPane, friendRequestsPane, battleRequestsPane);
        chatRefresh.play();
        viewListener.refreshFriends();
    }

    @FXML
    private void showFriendRequests() {
        chatRefresh.stop();
        show(friendRequestsPane, invitePane, friendsPane, chatPane, battleRequestsPane);
        viewListener.onFriendRequestsOpened();
    }

	@FXML
    private void showBattleRequests() {
        chatRefresh.stop();
        show(battleRequestsPane, friendRequestsPane, invitePane, friendsPane, chatPane);
        viewListener.onBattleRequestsOpened();
    }

    private void show(VBox visible, VBox... hidden) {
        visible.setVisible(true);
        for (VBox v : hidden) {
            v.setVisible(false);
        }
    }

    @FXML
    private void handleInvite() {
        String target = inviteField.getText().trim();
        if (!target.isEmpty()) {
            viewListener.onInvite(target);
        }
    }

    @FXML
    private void handleAcceptFriend() {
        String sender = friendRequestsListView.getSelectionModel().getSelectedItem();
        if (sender != null) {
            viewListener.onAcceptFriend(sender);
        }
    }

    @FXML
    private void handleDeclineFriend() {
        String sender = friendRequestsListView.getSelectionModel().getSelectedItem();
        if (sender != null) {
            viewListener.onDeclineFriend(sender);
        }
    }

	@FXML
    private void handleAcceptBattle() {
        String sender = battleRequestsListView.getSelectionModel().getSelectedItem();
        if (sender != null) {
            viewListener.onAcceptBattle(sender);
        }
    }

    @FXML
    private void handleDeclineBattle() {
        String sender = battleRequestsListView.getSelectionModel().getSelectedItem();
        if (sender != null) {
            viewListener.onDeclineBattle(sender);
        }
    }

    @FXML
    private void handleChatSend() {
        String content = chatMessageField.getText().trim();
        if (!content.isEmpty() && selectedChatFriend != null) {
            viewListener.onSendMessage(selectedChatFriend, content);
        }
    }

    @FXML
    private void handleClose() {
        viewListener.onClose();
    }

    public interface ViewListener {
        void onClose();
        void onDeclineFriend(String sender);
        void onDeclineBattle(String sender);
        void onAcceptFriend(String sender);
        void onAcceptBattle(String sender);
        void onInvite(String target);
        void onChatFriendSelected(String friend);
        void onFriendRequestsOpened();
        void onBattleRequestsOpened();
        void onChallengeFriend(String friend);
        void refreshFriends();
        void onSendMessage(String friend, String content);
    }
}
