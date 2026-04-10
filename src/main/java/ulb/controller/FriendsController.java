package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.repository.AccountRepository;
import ulb.repository.LoadException;
import ulb.service.AccountService;
import ulb.service.ServiceLoader;
import ulb.view.WindowPath;
import ulb.view.windows.FriendsWindow;


public class FriendsController implements FriendsWindow.ViewListener{


	private FriendsWindow view;
	private Listener listener;
	private Stage stage;

	public FriendsController(Stage stage, Listener listener) {
		this.stage = stage;
		this.listener = listener;
	}

	public void setListener(Listener listener) {
		this.listener = listener;

	}

	@Override
	public void populateFriends(VBox friendsList) {
		this.listener.populateFriends(friendsList);
	}

	@Override
	public void returnToMultiplayerWindow() {
		this.listener.returnToMultiplayerWindow();
	}

	@Override
	public void onInviteFriend(String usernameField){
		try {
			int id = ServiceLoader.getAccountService().getUserId(usernameField);
			if (id != 0) {
				listener.addFriend(id);
			} else {

			}
		} catch (LoadException e) {

		}

	}

	public void show() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.FRIENDS));
		loader.load();
		view = loader.getController();
		view.setListener(this);

		Parent root = loader.getRoot();
		if (stage.getScene() == null) {
			stage.setScene(new Scene(root));
		} else {
			stage.getScene().setRoot(root);
		}
		this.stage.show();
	}

	public interface Listener {
		void returnToMultiplayerWindow();
		void addFriend(int id);
		void populateFriends(VBox friendsList);
	}
}
