package ulb.controller.windows;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.controller.ClientController;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.ServerStatusException;
import ulb.exceptions.UnknownServerResponse;
import ulb.message.request.Request;
import ulb.message.request.playerInfo.GetPlayerRequest;
import ulb.message.request.social.*;
import ulb.message.response.playerInfo.PlayerResponse;
import ulb.message.response.social.*;
import ulb.model.chat.ChatMessage;
import ulb.view.WindowPath;
import ulb.view.windows.SocialPanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for the social panel handling interactions with other players.
 */
public class SocialPanelController extends WindowController<SocialPanel> implements SocialPanel.ViewListener {
	/**
	 * FXML Stage for the panel.
	 */
	private Stage popupStage;

	/**
	 * Information on the current player.
	 */
	private PlayerDTO player;

	/**
	 * Creates the social panel controller.
	 *
	 * @param stage The application stage
	 * @param clientController The client controller
	 */
	public SocialPanelController(Stage stage, ClientController clientController) {
		super(stage, WindowPath.SOCIAL_PANEL, clientController);
		this.view.setViewListener(this);
	}

	/**
	 * Displays the Social panel screen.
	 */
	public void show() {

		try {
			view.setFriendsList(this.getFriendList());
			view.setLeaderboardList(this.getLeaderboardList());
		} catch (Exception e) {
			return;
		}

		if (popupStage == null) {
			popupStage = new Stage();
			popupStage.initStyle(StageStyle.UNDECORATED);
			popupStage.initOwner(this.stage);
			Scene scene = new Scene(loader.getRoot());
			scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
			popupStage.setScene(scene);
			popupStage.setX(this.stage.getX());
			popupStage.setY(this.stage.getY());
		}

		popupStage.show();
	}

	/**
	 * Retrieves the list of the current player's friends.
	 *
	 * @return List of the usernames of the friends
	 */
	private List<String> getFriendList() throws EntityNotFoundException,  ServerStatusException, UnknownServerResponse {
		if (this.clientController.getData(new GetFriendsListRequest(getPlayer().getUsername())) instanceof FriendsListResponse msg)
			return msg.getFriends();
		LOGGER.warning("Impossible de récupérer la liste d'amis");
		throw new UnknownServerResponse("getFriendsListRequest");
	}

	/**
	 * Returns a list of the multiplayer score of each registered player.
	 *
	 * @return A map associating each player's username to his score
	 */
	private Map<String, Integer> getLeaderboardList() throws ServerStatusException, UnknownServerResponse {
		if (this.clientController.getData(new GetLeaderboardRequest()) instanceof LeaderboardResponse msg)
			return msg.getLeaderboard();
		LOGGER.warning("Impossible de récupérer le leaderboard");
		throw new UnknownServerResponse("getLeaderboard");
	}

	private PlayerDTO getPlayer() throws EntityNotFoundException {
		if (this.player == null) {
			Optional<PlayerDTO> optionalPlayer = this.clientController.getPlayer();
			if (optionalPlayer.isEmpty()){
				throw new EntityNotFoundException("player", "");
			}
			this.player = optionalPlayer.get();
		}
		return this.player;
	}	
	
	private List<String> getRequests(Request request) throws ServerStatusException, UnknownServerResponse {
		Serializable response = this.clientController.getData(request);
		if (response instanceof FriendRequestsResponse msg) return msg.getRequests();
		else if (response instanceof BattleRequestsResponse msg) return msg.getRequests();
		else if (response instanceof FriendsListResponse msg) return msg.getFriends();
		throw new UnknownServerResponse("");
	}

	/**
	 * Fetches the list of the current player's friends and updates it in the view.
	 */
	private void refreshFriendRequests() {
		try {
			List<String> friendRequests = getRequests(new GetFriendRequestsRequest(getPlayer().getUsername()));
			view.setFriendRequests(friendRequests);
		} catch (Exception e) {
			LOGGER.warning("Impossible de rafraichir la liste des demandes d'amis");
		}
	}

	/**
	 * Fetches the list of pending battle requests for the current player and updates it in the view.
	 */
	private void refreshBattleRequests() {
		try {
			List<String> battleRequests = getRequests(new GetBattleRequestsRequest(getPlayer().getUsername()));
			view.setBattleRequests(battleRequests);
		} catch (Exception e) {
			LOGGER.warning("Impossible de rafraichir la liste des demandes de combat");
		}
	}

	/**
	 * Load messages of a chat with a given friend.
	 */
	private void loadMessages(String friend) {
		new Thread(() -> {
			try {
				loadMessagesInCurrentThread(friend);
			} catch (Exception e) {
				LOGGER.warning("Impossible de charger les message");
			}
		}).start();
	}

	private void loadMessagesInCurrentThread(String friend) throws EntityNotFoundException, ServerStatusException, UnknownServerResponse {
		List<ChatMessage> chatMessages = null;
		if (this.clientController.getData(new GetChatMessagesRequest(getPlayer().getUsername(), friend)) instanceof ChatMessagesResponse msg) {
			chatMessages = msg.getMessages();
		} else {
			throw new UnknownServerResponse("getChatMessage");
		}

		List<String> lines = new ArrayList<>();
		for (ChatMessage chatMessage : chatMessages) {
			lines.add(chatMessage.format(this.getPlayer().getUsername()));
		}
		Platform.runLater(() -> view.setMessages(lines));
	}

	/**
	 * Fetches information on a given player.
	 *
	 * @param username The player's username
	 * @return Information on the player
	 */
	private PlayerDTO getPlayerByName(String username) throws ServerStatusException, UnknownServerResponse {
		if (this.clientController.getData(new GetPlayerRequest(username)) instanceof PlayerResponse msg) {
			return msg.getPlayer();
		}
		throw new UnknownServerResponse("getPlayer");
	}

	/**
	 * Switches the view to the team selection for a multiplayer battle.
	 *
	 * @param opponent The opponent player
	 */
	private void switchToTeamSelectionForMulti(PlayerDTO opponent) {
		this.clientController.setOpponentMulti(opponent);
		this.clientController.showWindow(WindowName.TEAM);
	}

	/**
	 * Function playing in loop while waiting for the opponent to respond to a battle request.
	 *
	 * @param opponent The player the battle request has been sent to
	 */
	private void waitForBattleRequestResponse(PlayerDTO opponent) {
		MultiBattleStatusDTO status;
		try {
			status = this.clientController.getMultiBattleStatus(getPlayer().getUserId(), opponent.getUserId());
		} catch (Exception e) {
			this.clientController.stopWaitWindow();
			this.clientController.showWindow(WindowName.MAIN_MENU);
			return;
		}

		switch (status.status()) {
			case PICKING_TEAMS:
				this.clientController.stopWaitWindow();
				this.switchToTeamSelectionForMulti(opponent);
				break;

			case WAITING_ACCEPT:
				break;

			default:
				this.clientController.stopWaitWindow();
				this.clientController.showWindow(WindowName.MAIN_MENU);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClose() {
		this.popupStage.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDeclineFriend(String sender) {
		try {
			if (this.clientController.postData(new DeclineFriendRequestRequest(getPlayer().getUsername(), sender))) {
				refreshFriendRequests();
			}
		} catch (Exception e) {
			LOGGER.warning("Impossible de décliner la demande d'amis de " + sender);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onDeclineBattle(String sender) {
		try {
			if (this.clientController.postData(new DeclineBattleRequestRequest(getPlayer().getUsername(), sender))) {
				refreshFriendRequests();
			}
		} catch (Exception e) {
			LOGGER.warning("Impossible de décliner la demande de combat de " + sender);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAcceptFriend(String sender) {
		try {
			if (this.clientController.postData(new AcceptFriendRequestRequest(getPlayer().getUsername(), sender))) {
				refreshFriendRequests();
				refreshFriends();
			}
		} catch (Exception e) {
			LOGGER.warning("Impossible d'accepter la demande d'amis de " + sender);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAcceptBattle(String sender) {
		try {
			if (this.clientController.postData(new AcceptBattleRequestRequest(getPlayer().getUsername(), sender))) {
				PlayerDTO opponent = this.getPlayerByName(sender);
	
				this.popupStage.close();
				this.switchToTeamSelectionForMulti(opponent);
			}
		} catch (Exception e) {
			LOGGER.warning("Impossible d'accepter la demande de combat de " + sender);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onInvite(String target) {
		try {
			if (target.equals(this.getPlayer().getUsername())) {
				view.setInviteStatus("On ne peut pas être son propre ami...");
				return;
			}
			if (this.getFriendList().contains(target)) {
				view.setInviteStatus("Vous êtes déjà amis !");
				return;
			}
			boolean ok = this.clientController.postData(new SendFriendRequestRequest(getPlayer().getUsername(), target));
			view.setInviteStatus(ok ? "Demande envoyée !" : "Utilisateur introuvable.");
			if (ok) {
				view.clearInviteField();
			}
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onChatFriendSelected(String friend) {
		loadMessages(friend);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onFriendRequestsOpened() {
		refreshFriendRequests();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBattleRequestsOpened() {
		refreshBattleRequests();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onChallengeFriend(String friendName) {
		if (friendName == null || friendName.isBlank()) {
			return;
		}
		PlayerDTO friend;
		boolean ok;
		try {
			friend = this.getPlayerByName(friendName);
			ok = this.clientController.postData(new SendBattleRequestRequest(getPlayer().getUsername(),
					friendName));
		} catch (Exception e) {
			return;
		}
		view.setInviteStatus(ok ? "Défi envoyé !" : "Impossible d'envoyer le défi.");

		if (ok) {
			this.onClose();
			this.clientController.setNewTimeLine(e -> {
				this.waitForBattleRequestResponse(friend);
			});
			this.clientController.showWindow(WindowName.WAIT);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSendMessage(String friend, String content) {
		view.clearChatField();
		new Thread(() -> {
			try {
				this.clientController.postData(new SendChatMessageRequest(getPlayer().getUsername(), friend, content));
				loadMessagesInCurrentThread(friend);
			} catch (Exception e) {
				LOGGER.warning("Impossible de charger les messages.");
			}
		}).start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refreshFriends() {
		try {
			view.setFriendsList(this.getFriendList());
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refreshLeaderboard() {
		try {
			view.setLeaderboardList(this.getLeaderboardList());
		} catch (Exception e) {
			return;
		}
	}
}
