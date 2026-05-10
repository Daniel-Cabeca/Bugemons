package ulb.controller.windows;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.message.request.Request;
import ulb.message.request.playerInfo.GetPlayerRequest;
import ulb.message.request.social.*;
import ulb.message.response.playerInfo.PlayerResponse;
import ulb.message.response.social.*;
import ulb.model.chat.ChatMessage;
import ulb.view.WindowPath;
import ulb.view.windows.SocialPanel;

public class SocialPanelController extends WindowController<SocialPanel> implements SocialPanel.ViewListener {
	private Stage popupStage;
	private PlayerDTO player;

	/**
     * Creates the social panel controller.
     *
     * @param stage The application stage
     * @param clientListener listener to communicate with the clientController
     */
    public SocialPanelController(Stage stage, ClientListener clientListener) {
        super(stage, WindowPath.SOCIAL_PANEL, clientListener);
        this.view.setViewListener(this);
    }

	private PlayerDTO getPlayer(){
		if (player == null){
			this.player = this.clientListener.onGetPlayer();
		}
		return player;
	}

	/**
	* Displays the Social panel screen.
	*/
	public void show() {

		view.setFriendsList(this.getFriendList());
		view.setLeaderboardList(this.getLeaderboardList());

		if (popupStage == null){
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

	private List<String> getRequests(Request request){
		Serializable response = this.clientListener.onGetData(request);
		if (response instanceof FriendRequestsResponse msg)
			return msg.getRequests();
		else if (response instanceof BattleRequestsResponse msg)
			return msg.getRequests();
		else if (response instanceof FriendsListResponse msg)
			return msg.getFriends();
		return List.of();
	}

	private List<String> getFriendList(){
		if (this.clientListener.onGetData(new GetFriendsListRequest(getPlayer().getUsername())) instanceof FriendsListResponse msg)
			return msg.getFriends();
		return List.of();
	}

	private Map<String, Integer> getLeaderboardList(){
		if (this.clientListener.onGetData(new GetLeaderboardRequest()) instanceof LeaderboardResponse msg)
			return msg.getLeaderboard();
		return Collections.<String, Integer>emptyMap();
	
	}

	private void refreshFriendRequests() {
		List<String> friendRequests = getRequests(new GetFriendRequestsRequest(getPlayer().getUsername()));
		view.setFriendRequests(friendRequests);
	}

	private void refreshBattleRequests(){
		List<String> battleRequests = getRequests(new GetBattleRequestsRequest(getPlayer().getUsername()));
		view.setBattleRequests(battleRequests);
	}

	/**
     * Load messages of a chat with a given friend.
     */
    private void loadMessages(String friend) {
        new Thread(() -> loadMessagesInCurrentThread(friend)).start();
    }

    private void loadMessagesInCurrentThread(String friend) {
		List<ChatMessage> chatMessages = null;
		if (this.clientListener.onGetData(new GetChatMessagesRequest(getPlayer().getUsername(), friend)) instanceof ChatMessagesResponse msg)
			chatMessages = msg.getMessages();

        List<String> lines = new ArrayList<>();
        for (ChatMessage chatMessage : chatMessages) {
            lines.add(chatMessage.format(this.getPlayer().getUsername()));
        }
        Platform.runLater(() -> view.setMessages(lines));
    }

	private PlayerDTO getPlayerByName(String username){
		if (this.clientListener.onGetData(new GetPlayerRequest(username)) instanceof PlayerResponse msg) {
			return msg.getPlayer();
		}
		return null;
	}

	/**
	 * Returns the current multiplayer battle status between two players.
	 *
	 * @param userId1 The first player's id
	 * @param userId2 The second player's id
	 * @return The multiplayer battle status DTO
	 */
	public MultiBattleStatusDTO getMultiBattleStatus(int userId1, int userId2) {
		if (this.clientListener.onGetData(new GetMultiBattleStatusRequest(userId1, userId2)) instanceof MultiBattleStatusResponse msg)
			return msg.getStatus();

		LOGGER.warning("Failed to obtain multiplayer battle status.");
		return new MultiBattleStatusDTO();
	}

	private void switchToTeamSelectionForMulti(PlayerDTO opponent){
		this.clientListener.onSetOpponentMulti(opponent);
		this.clientListener.onShowWindow(WindowName.TEAM);
	}

	/**
	 * Function playing in loop while waiting for the opponent to respond to a battle request.
	 *
	 * @param opponent The player the battle request has been sent to
	 */
	private void waitForBattleRequestResponse(PlayerDTO opponent) {
		MultiBattleStatusDTO status = this.getMultiBattleStatus(getPlayer().getUserId(), opponent.getUserId());

		switch(status.getStatus()) {
			case PICKING_TEAMS:
				this.clientListener.onStopWaitWindow();
				this.switchToTeamSelectionForMulti(opponent);
				break;

			case WAITING_ACCEPT:
				break;

			default:
				this.clientListener.onStopWaitWindow();
				this.clientListener.onShowWindow(WindowName.MODE);
		}
	}

	/**
	* Close the social panel screen.
	*/
	@Override
	public void onClose() {
		this.popupStage.close();
	}

	/**
	* Decline a friend request.
	*/
	@Override
	public void onDeclineFriend(String sender) {
		if (this.clientListener.onPostData(new DeclineFriendRequestRequest(getPlayer().getUsername(), sender))){
			refreshFriendRequests();
		}
	}

	@Override
	public void onDeclineBattle(String sender) {
		if (this.clientListener.onPostData(new DeclineBattleRequestRequest(getPlayer().getUsername(), sender))){
			refreshFriendRequests();
		}
	}

	/**
	* Accept a friend request.
	*/
	@Override
	public void onAcceptFriend(String sender) {
		if (this.clientListener.onPostData(new AcceptFriendRequestRequest(getPlayer().getUsername(), sender))) {
			refreshFriendRequests();
            refreshFriends();
		}
	}

	@Override
	public void onAcceptBattle(String sender) {
		if (this.clientListener.onPostData(new AcceptBattleRequestRequest(getPlayer().getUsername(), sender))) {
			PlayerDTO opponent = this.getPlayerByName(sender);

			this.popupStage.close();
			this.switchToTeamSelectionForMulti(opponent);
		}
	}

	/**
	* Displays the appropriat status and sends friend request.
	*/
	@Override
	public void onInvite(String target) {
		if (target.equals(this.getPlayer().getUsername())) {
			view.setInviteStatus("On ne peut pas être son propre ami...");
			return;
		}
		if (this.getFriendList().contains(target)) {
			view.setInviteStatus("Vous êtes déjà amis !");
			return;
		}
		boolean ok = this.clientListener.onPostData(new SendFriendRequestRequest(getPlayer().getUsername(), target));
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

		PlayerDTO friend = this.getPlayerByName(friendName);
		boolean ok = this.clientListener.onPostData(new SendBattleRequestRequest(getPlayer().getUsername(), friendName));
		view.setInviteStatus(ok ? "Défi envoyé !" : "Impossible d'envoyer le défi.");

		if (ok) {
			this.onClose();
			this.clientListener.onSetNewTimeLine(e -> {
				this.waitForBattleRequestResponse(friend);
			});
			this.clientListener.onShowWindow(WindowName.WAIT);
		}
	}

	/**
     * Send given contents in form of a message to a given friend.
     */
    @Override
    public void onSendMessage(String friend, String content) {
        view.clearChatField();
        new Thread(() -> {
            this.clientListener.onPostData(new SendChatMessageRequest(getPlayer().getUsername(), friend, content));
            loadMessagesInCurrentThread(friend);
        }).start();
    }

	@Override
	public void refreshFriends() {
		view.setFriendsList(this.getFriendList());
	}

	@Override
	public void refreshLeaderboard() {
		view.setLeaderboardList(this.getLeaderboardList());
	}
}
