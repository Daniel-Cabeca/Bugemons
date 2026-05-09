package ulb.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.model.chat.ChatMessage;
import ulb.view.FxmlLoader;
import ulb.exceptions.ViewLoadException;
import ulb.view.WindowPath;
import ulb.view.windows.SocialPanel;

import java.util.ArrayList;
import java.util.List;

public class SocialPanelController implements SocialPanel.ViewListener {
	private Stage WindowStage;
	private SocialPanel view;
	private final ClientController clientController;

	public SocialPanelController(ClientController clientController) {
		this.clientController = clientController;
	}

	public Stage getParentStage() { return this.clientController.getStage(); }

	/**
	* Displays the Social panel screen.
	*/
	public void show() throws ViewLoadException {
		FXMLLoader loader = FxmlLoader.load(this, WindowPath.SOCIAL_PANEL);
		view = loader.getController();
		view.setViewListener(this);

		view.setFriendsList(this.clientController.getFriendsList());
		view.setLeaderboardList(this.clientController.getLeaderboardList());

		WindowStage = new Stage();
		WindowStage.initStyle(StageStyle.UNDECORATED);
		WindowStage.initOwner(this.getParentStage());

		Scene scene = new Scene(loader.getRoot());
		scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
		WindowStage.setScene(scene);
		WindowStage.setX(this.getParentStage().getX());
		WindowStage.setY(this.getParentStage().getY());
		WindowStage.show();
	}

	/**
	* Closes the social panel screen.
	*/
	public void close() {
		this.WindowStage.close();
	}

	/**
	* Close the social panel screen.
	*/
	@Override
	public void onClose() {
		this.close();
	}

	/**
	* Decline a friend request.
	*/
	@Override
	public void onDeclineFriend(String sender) {
		if (this.clientController.declineFriendRequest(sender)) {
			refreshFriendRequests();
		}
	}

	@Override
	public void onDeclineBattle(String sender) {
		if (this.clientController.declineBattleRequest(sender)) {
			refreshBattleRequests();
		}
	}

	/**
	* Accept a friend request.
	*/
	@Override
	public void onAcceptFriend(String sender) {
		if (this.clientController.acceptFriendRequest(sender)) {
			refreshFriendRequests();
            refreshFriends();
		}
	}

	@Override
	public void onAcceptBattle(String sender) {
		if (this.clientController.acceptBattleRequest(sender)) {
			PlayerDTO opponent = this.clientController.getPlayer(sender);

			this.clientController.closeSocialPanel();
			this.clientController.switchToTeamSelectionForMulti(opponent);
		}
	}

	/**
	* Displays the appropriat status and sends friend request.
	*/
	@Override
	public void onInvite(String target) {
		if (target.equals(this.clientController.getPlayerName())) {
			view.setInviteStatus("On ne peut pas être son propre ami...");
			return;
		}
		if (this.clientController.getFriendsList().contains(target)) {
			view.setInviteStatus("Vous êtes déjà amis !");
			return;
		}
		boolean ok = this.clientController.sendFriendRequest(target);
		view.setInviteStatus(ok ? "Demande envoyée !" : "Utilisateur introuvable.");
		if (ok) {
			view.clearInviteField();
		}
	}

	/**
	* Select given friend to chat.
	*/
	@Override
	public void onChatFriendSelected(String friend) {
		loadMessages(friend);
	}

	/**
	* Open request tab and refreshes.
	*/
	@Override
	public void onFriendRequestsOpened() {
		refreshFriendRequests();
	}

	@Override
	public void onBattleRequestsOpened() {
		refreshBattleRequests();
	}

	@Override
	public void onChallengeFriend(String friendName) {
		if (friendName == null || friendName.isBlank()) {
			return;
		}

		PlayerDTO friend = this.clientController.getPlayer(friendName);
		boolean ok = this.clientController.sendBattleRequest(friend.getUsername());
		view.setInviteStatus(ok ? "Défi envoyé !" : "Impossible d'envoyer le défi.");

		if (ok) {
			this.clientController.openWaitWindow(e -> {
				this.waitForBattleRequestResponse(friend);
			});
		}
	}

	/**
	 * Function playing in loop while waiting for the opponent to respond to a battle request.
	 *
	 * @param opponent The player the battle request has been sent to
	 */
	private void waitForBattleRequestResponse(PlayerDTO opponent) {
		PlayerDTO self = this.clientController.getPlayer();
		MultiBattleStatusDTO status = this.clientController.getMultiBattleStatus(self.getUserId(), opponent.getUserId());

		switch(status.getStatus()) {
			case PICKING_TEAMS:
				this.clientController.stopWaitWindow();
				this.clientController.switchToTeamSelectionForMulti(opponent);
				break;

			case WAITING_ACCEPT:
				break;

			default:
				this.clientController.stopWaitWindow();
				this.clientController.switchToModeWindow();
		}
	}

	/**
     * Send given contents in form of a message to a given friend.
     */
    @Override
    public void onSendMessage(String friend, String content) {
        view.clearChatField();
        new Thread(() -> {
            this.clientController.sendChatMessage(friend, content);
            loadMessagesInCurrentThread(friend);
        }).start();
    }

	@Override
	public void refreshFriends() {
		view.setFriendsList(this.clientController.getFriendsList());
	}

	@Override
	public void refreshLeaderboard() {
		view.setLeaderboardList(this.clientController.getLeaderboardList());
	}

	private void refreshFriendRequests() {
		view.setFriendRequests(this.clientController.getFriendRequests());
	}

	private void refreshBattleRequests() {
		view.setBattleRequests(this.clientController.getBattleRequests());
	}

	/**
     * Load messages of a chat with a given friend.
     */
    private void loadMessages(String friend) {
        new Thread(() -> loadMessagesInCurrentThread(friend)).start();
    }

    private void loadMessagesInCurrentThread(String friend) {
        String me = this.clientController.getPlayerName();
        List<ChatMessage> msgs = this.clientController.getChatMessages(friend);
        List<String> lines = new ArrayList<>();
        for (ChatMessage msg : msgs) {
            lines.add(msg.format(me));
        }
        Platform.runLater(() -> view.setMessages(lines));
    }

}
