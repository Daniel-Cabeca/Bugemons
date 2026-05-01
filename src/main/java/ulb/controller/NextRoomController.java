package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.FxmlLoader;
import ulb.view.WindowPath;
import ulb.view.windows.NextRoomWindow;

/**
 * Controller for the transition screen between tower rooms.
 */
public class NextRoomController implements NextRoomWindow.ViewListener {

    private final Listener listener;
    private NextRoomWindow view;
    private Stage stage;

    /**
     * Creates the next room controller.
     *
     * @param stage The application stage
     * @param listener The listener notified of navigation actions
     */
    public NextRoomController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    /**
     * Displays the next room screen.
     */
    public void show() {
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.NEXT_ROOM);
        view = loader.getController();
        view.setViewListener(this);

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

    /**
     * Handles continue action.
     */
    @Override
    public void onContinue() {
        listener.onContinue();
    }

    /**
     * Handles return action.
     */
    @Override
    public void onReturn() {
        listener.onReturn();
    }

    /**
     * Listener for next room navigation events.
     */
    public interface Listener {
        /** Called when the user continues to the next room. */
        void onContinue();
        /** Called when the user returns to the previous flow. */
        void onReturn();
    }
}
