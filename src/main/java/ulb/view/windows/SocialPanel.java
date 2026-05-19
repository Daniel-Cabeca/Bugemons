package ulb.view.windows;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	private VBox leaderboardPane;
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
	@FXML
	private ListView<Map.Entry<String, Integer>> leaderboardView;

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
				challengeButton.setStyle("-fx-background-color: #f8f8d8; -fx-text-fill: #000000;" + " -fx-border-color" +
						": #000000; -fx-border-width: 2;" + " -fx-padding: 4 10; -fx-cursor: hand;");
				challengeButton.setOnAction(e -> viewListener.onChallengeFriend(item));

				HBox row = new HBox(12, nameLabel, challengeButton);
				row.setAlignment(Pos.CENTER_LEFT);

				setText(null);
				setGraphic(row);
			}
		});

		leaderboardView.setCellFactory(lv -> new ListCell<Map.Entry<String, Integer>>() {
			@Override
			protected void updateItem(Map.Entry<String, Integer> item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
					return;
				}

				Label nameLabel = new Label(item.getKey());
				nameLabel.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold;");

				Label scoreLabel = new Label(item.getValue().toString());
				scoreLabel.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold;");

				HBox row = new HBox(12, nameLabel, scoreLabel);
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

	/**
	 * Updates the friends list in both friends and chat views.
	 *
	 * @param List<String> the list of friends that need to be shown.
	 */
	public void setFriendsList(List<String> friends) {
		String selectedFriend = selectedChatFriend;

		friendsListView.getItems().setAll(friends);
		chatFriendsList.getItems().setAll(friends);

		if (selectedFriend != null && friends.contains(selectedFriend)) {
			chatFriendsList.getSelectionModel().select(selectedFriend);
		}
	}

	/**
	 * Updates the leaderboard list.
	 *
	 * @param List<String> the list of accounts that need to be shown.
	 */
	public void setLeaderboardList(Map<String, Integer> leaderboard) {
		List<Map.Entry<String, Integer>> items = new ArrayList<>(leaderboard.entrySet());
		leaderboardView.getItems().setAll(items);
	}

	public void setBattleRequests(List<String> battleRequests) { battleRequestsListView.getItems().setAll(battleRequests); }

	/**
	 * Updates the list of incoming friend requests.
	 *
	 * @param List<String> the list of requests that need to be shown.
	 */
	public void setFriendRequests(List<String> requests) {
		friendRequestsListView.getItems().setAll(requests);
	}

	/**
	 * Updates the displayed chat messages.
	 *
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
		show(invitePane, friendsPane, chatPane, friendRequestsPane, battleRequestsPane, leaderboardPane);
	}

	/**
	 * Shows one panel and hides the others.
	 *
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
	 * Displays the friends panel and refreshes the friends list.
	 */
	@FXML
	private void showFriends() {
		chatRefresh.stop();
		show(friendsPane, invitePane, chatPane, friendRequestsPane, battleRequestsPane, leaderboardPane);
		viewListener.refreshFriends();
	}

	/**
	 * Displays the leaderboard panel and refreshes the leaderboard list.
	 */
	@FXML
	private void showLeaderboard() {
		chatRefresh.stop();
		show(leaderboardPane, invitePane, friendsPane, chatPane, friendRequestsPane, battleRequestsPane);
		viewListener.refreshLeaderboard();
	}

	/**
	 * Displays the chat panel and starts chat refresh.
	 */
	@FXML
	private void showChat() {
		show(chatPane, invitePane, friendsPane, friendRequestsPane, battleRequestsPane, leaderboardPane);
		chatRefresh.play();
		viewListener.refreshFriends();
	}

	/**
	 * Displays the friend requests panel.
	 */
	@FXML
	private void showFriendRequests() {
		chatRefresh.stop();
		show(friendRequestsPane, invitePane, friendsPane, chatPane, battleRequestsPane, leaderboardPane);
		viewListener.onFriendRequestsOpened();
	}

	@FXML
	private void showBattleRequests() {
		chatRefresh.stop();
		show(battleRequestsPane, friendRequestsPane, invitePane, friendsPane, chatPane, leaderboardPane);
		viewListener.onBattleRequestsOpened();
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
	private void handleAcceptFriend() {
		String sender = friendRequestsListView.getSelectionModel().getSelectedItem();
		if (sender != null) {
			viewListener.onAcceptFriend(sender);
		}
	}

	/**
	 * Handles declining a selected friend request.
	 */
	@FXML
	private void handleDeclineFriend() {
		String sender = friendRequestsListView.getSelectionModel().getSelectedItem();
		if (sender != null) {
			viewListener.onDeclineFriend(sender);
		}
	}

	/**
	 * Handles accepting a battle invite
	 */
	@FXML
	private void handleAcceptBattle() {
		String sender = battleRequestsListView.getSelectionModel().getSelectedItem();
		if (sender != null) {
			viewListener.onAcceptBattle(sender);
		}
	}

	/**
	 * Handles declining a battle invite
	 */
	@FXML
	private void handleDeclineBattle() {
		String sender = battleRequestsListView.getSelectionModel().getSelectedItem();
		if (sender != null) {
			viewListener.onDeclineBattle(sender);
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
		void refreshLeaderboard();
		void onSendMessage(String friend, String content);
	}
}
