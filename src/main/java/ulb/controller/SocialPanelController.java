package ulb.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ulb.model.chat.ChatMessage;
import ulb.view.WindowPath;
import ulb.view.windows.SocialPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SocialPanelController implements SocialPanel.ViewListener {

    private final Stage stage;
    private Stage popupStage;
    private SocialPanel view;
    private final Listener listener;

    public SocialPanelController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.SOCIAL_PANEL));
        loader.load();
        view = loader.getController();
        view.setViewListener(this);

        view.setFriendsList(listener.getFriendsList());

        popupStage = new Stage();
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.initOwner(stage);

        Scene scene = new Scene(loader.getRoot());
        scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
        popupStage.setScene(scene);
        popupStage.setX(stage.getX());
        popupStage.setY(stage.getY());
        popupStage.show();
    }


    @Override
    public void onClose() {
        popupStage.close();
    }

    @Override
    public void onDecline(String sender) {
        if (listener.declineFriendRequest(sender)) {
            refreshRequests();
        }
    }

    @Override
    public void onAccept(String sender) {
        if (listener.acceptFriendRequest(sender)) {
            refreshRequests();
        }
    }

    @Override
    public void onInvite(String target) {
        if (target.equals(listener.getPlayerName())) {
            view.setInviteStatus("On ne peut pas être son propre ami...");
            return;
        }
        if (listener.getFriendsList().contains(target)) {
            view.setInviteStatus("Vous êtes déjà amis !");
            return;
        }
        boolean ok = listener.sendFriendRequest(target);
        view.setInviteStatus(ok ? "Demande envoyée !" : "Utilisateur introuvable.");
        if (ok) {
            view.clearInviteField();
        }
    }

    @Override
    public void onChatFriendSelected(String friend) {
        loadMessages(friend);
    }

    @Override
    public void onRequestsOpened() {
        refreshRequests();
    }

    @Override
    public void onSendMessage(String friend, String content) {
        view.clearChatField();
        new Thread(() -> {
            listener.sendChatMessage(friend, content);
            loadMessages(friend);
        }).start();
    }

    @Override
    public void refreshFriends() {
        view.setFriendsList(listener.getFriendsList());
    }

    private void refreshRequests() {
        view.setRequests(listener.getFriendRequests());
    }

    private void loadMessages(String friend) {
        String me = listener.getPlayerName();
        new Thread(() -> {
            List<ChatMessage> msgs = listener.getChatMessages(friend);
            List<String> lines = new ArrayList<>();
            for (ChatMessage msg : msgs) {
                lines.add(msg.format(me));
            }
            Platform.runLater(() -> view.setMessages(lines));
        }).start();
    }

    public interface Listener {
        boolean acceptFriendRequest(String sender);
        boolean declineFriendRequest(String sender);
        boolean sendFriendRequest(String target);
        void sendChatMessage(String friend, String content);
        List<String> getFriendRequests();
        String getPlayerName();
        List<ChatMessage> getChatMessages(String friend);
        List<String> getFriendsList();
    }

}
