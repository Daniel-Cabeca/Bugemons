package ulb.controller.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.controller.ClientController;
import ulb.exceptions.ViewLoadException;
import ulb.view.FxmlLoader;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class that represent a WindowController.
 *
 * @param <T> ViewWindow of the windowController
 */
public abstract class WindowController<T> {
	/**
	 * Object used for logging runtime information to the console or to a log file.
	 */
	protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	/**
	 * The associated FXML Stage.
	 */
	protected Stage stage;

	/**
	 * The associated view.
	 */
	protected T view;

	/**
	 * The FXML Loader responsible for loading .fxml files.
	 */
	protected FXMLLoader loader;

	/**
	 * The controller responsible for coordinating server communications and switching to other controllers.
	 */
	protected ClientController clientController;

	/**
	 * A super constructor for all window controllers.
	 *
	 * @param stage used to show fxml windows
	 * @param windowPath the fxml path of the viewWindow
	 * @param clientController the client controller
	 */
	protected WindowController(Stage stage, String windowPath, ClientController clientController) {
		this.stage = stage;
		this.clientController = clientController;

		try {
			this.loadView(windowPath);
		} catch (ViewLoadException e) {
			LOGGER.log(Level.WARNING,
					"\u001B[31m" + "Impossible d'afficher l'écran de " + this.getClass().getName() + "\u001B[0m");
		}
	}

	/**
	 * Read the fxml file and load the view window
	 * @throws ViewLoadException when an exception occures with the FxmlLoader 
	 */
	protected void loadView(String windowPath) throws ViewLoadException {
		loader = FxmlLoader.load(this, windowPath);
		view = loader.getController();
	}

	/**
	 * Changes the fxml window to the current fxml window.
	 */
	public void show() {
		Parent root = loader.getRoot();
		if (stage.getScene() == null) {
			stage.setScene(new Scene(root));
		} else {
			stage.getScene().setRoot(root);
		}
		this.stage.centerOnScreen(); //keeps UI neat
		this.stage.show();
	}

	/**
	 * WindowName enum is used by showWindow function to show the right window.
	 * Each name represent an FXML ViewWindow to show.
	 */
	public enum WindowName {
		REGISTER, MAIN_MENU, GAME_MODE, ATTACK_REPLACEMENT, BATTLE, BATTLE_END, CHOOSE_BUGEMON, CONFIRM_TEAM, FLOOR,
		FLOOR_REWARD, LEVEL_UP, LOAD_TEAM_PANEL, NEXT_ROOM, SOCIAL_PANEL, TEAM, WAIT
	}
}
