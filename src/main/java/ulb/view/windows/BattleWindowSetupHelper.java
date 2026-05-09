package ulb.view.windows;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import ulb.view.TypeColor;
import ulb.view.windows.BattleWindow.AbilityEntry;
import ulb.view.windows.BattleWindow.BugemonEntry;
import ulb.view.windows.BattleWindow.InventoryEntry;
import ulb.view.windows.BattleWindow.ViewListener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Configures the cell factories for the three ListView components used in the battle window.
 * Each factory produces custom implementations that render an entry's sprite (where applicable),
 * a descriptive label, and an action button connected to the provided {@link ViewListener}.
 */
public class BattleWindowSetupHelper {
    private static final Logger LOGGER = Logger.getLogger(BattleWindowSetupHelper.class.getName());

    /**
     * Installs a cell factory on the inventory {@link ListView} that renders each
     * {@link InventoryEntry} with its sprite, a name-and-quantity label, and a
     * "Utiliser" button. The button is disabled when the entry is not usable, and
     * a tooltip showing the item description appears when hovering.
     *
     * @param inventoryList the list view to configure
     * @param viewListener the listener to notify when the button is clicked;
     */
    public void setupInventoryList(ListView<InventoryEntry> inventoryList, ViewListener viewListener) {
        inventoryList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<InventoryEntry> call(ListView<InventoryEntry> listView) {
                return createInventoryListCell(viewListener);
            }
        });
    }

    /**
     * Installs a cell factory on the Bugemon {@link ListView} that renders each
     * {@link BugemonEntry} with its sprite, a name label (suffixed with " (KO)" when
     * the Bugemon is KO), and an "Échanger" button. Cells whose entry is marked
     * as active are rendered empty so the currently battling Bugemon is not shown as
     * a swap option. The button is disabled when the entry is not selectable.
     *
     * @param bugemonsList the list view to configure
     * @param viewListener the listener to notify when the button is clicked
     */
    public void setupBugemonsList(ListView<BugemonEntry> bugemonsList, ViewListener viewListener) {
        bugemonsList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<BugemonEntry> call(ListView<BugemonEntry> listView) {
                return createBugemonListCell(viewListener);
            }
        });
    }

    /**
     * Installs a cell factory on the abilities {@link ListView} that renders each
     * {@link AbilityEntry} with a name label and a "Utiliser" button, styled with the
     * ability's type color. A tooltip showing the description and, when present, the
     * effectiveness label appears when hovering.
     *
     * @param abilitiesList the list view to configure
     * @param viewListener the listener to notify when the button is clicked
     */
    public void setupAbilitiesList(ListView<AbilityEntry> abilitiesList, ViewListener viewListener) {
        abilitiesList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<AbilityEntry> call(ListView<AbilityEntry> listView) {
                return createAbilityListCell(viewListener);
            }
        });
    }

    /**
     * Creates a reusable {@link ListCell} for {@link InventoryEntry} objects.
     * Each cell contains a sprite {@link ImageView}, a quantity label, and a
     * "Utiliser" button. The button's action calls the ViewListener with the entry's
     * item ID. Sprite load failures are logged but do not interrupt cell rendering.
     *
     * @param viewListener the listener to notify on button click
     * @return a new configured {@link ListCell} instance
     */
    private ListCell<InventoryEntry> createInventoryListCell(ViewListener viewListener) {
        return new ListCell<>() {
            private final HBox hbox = new HBox(10);
            private final ImageView imageView = new ImageView();
            private final Label label = new Label();
            private final Button button = new Button("Utiliser");

            {
                imageView.setFitHeight(30);
                imageView.setFitWidth(30);
                hbox.getChildren().addAll(imageView, label, button);
                button.setOnAction(event -> {
                    InventoryEntry entry = getItem();
                    if (entry != null && viewListener != null) {
                        viewListener.onUseItem(entry.itemId(), event);
                    }
                });
            }

            @Override
            protected void updateItem(InventoryEntry entry, boolean empty) {
                super.updateItem(entry, empty);
                if (empty || entry == null) {
                    setText(null);
                    setGraphic(null);
                    setTooltip(null);
                } else {
                    try {
                        Image image = new Image(getClass().getResourceAsStream(entry.itemSpritePath()));
                        imageView.setImage(image);
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Failed to load item image: " + entry.itemSpritePath(), e);
                        imageView.setImage(null);
                    }
                    label.setText(entry.itemName() + " x" + entry.quantity());
                    button.setDisable(!entry.usable());
                    Tooltip tooltip = new Tooltip(entry.itemDescription());
                    tooltip.setShowDelay(javafx.util.Duration.millis(100));
                    setTooltip(tooltip);
                    setGraphic(hbox);
                }
            }
        };
    }

    /**
     * Creates a reusable {@link ListCell} for {@link BugemonEntry} objects.
     * Each cell contains a sprite {@link ImageView}, a name label, and an
     * "Échanger" button.
     * The button is disabled when {@code selectable} is {@code false}, and its action
     * calls the ViewListener with the entry's Bugemon ID.
     *
     * @param viewListener the listener to notify on button click
     * @return a new configured {@link ListCell} instance
     */
    private ListCell<BugemonEntry> createBugemonListCell(ViewListener viewListener) {
        return new ListCell<>() {
            private final HBox hbox = new HBox(10);
            private final ImageView imageView = new ImageView();
            private final Label label = new Label();
            private final Button button = new Button("Échanger");

            {
                imageView.setFitHeight(30);
                imageView.setFitWidth(30);
                hbox.getChildren().addAll(imageView, label, button);
                button.setOnAction(event -> {
                    BugemonEntry entry = getItem();
                    if (entry != null && viewListener != null) {
                        viewListener.onSwapBugemon(entry.bugemonId(), event);
                    }
                });
            }

            @Override
            protected void updateItem(BugemonEntry entry, boolean empty) {
                super.updateItem(entry, empty);
                if (empty || entry == null || entry.active()) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(getClass().getResourceAsStream(entry.bugemonSpritePath()));
                        imageView.setImage(image);
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Failed to load Bugemon image: " + entry.bugemonSpritePath(), e);
                        imageView.setImage(null);
                    }
                    label.setText(entry.ko() ? entry.bugemonName() + " (KO)" : entry.bugemonName());
                    button.setDisable(!entry.selectable());
                    setGraphic(hbox);
                }
            }
        };
    }

    /**
     * Creates a reusable {@link ListCell} for {@link AbilityEntry} objects.
     * Each cell contains a name label and a "Utiliser" button, arranged in an
     * {@link HBox} whose background is set to the ability's type color
     * A tooltip is always shown with the ability description and effectiveness
     * The button's action calls the ViewListener with the entry's ability ID.
     *
     * @param viewListener the listener to notify on button click
     * @return a new configured {@link ListCell} instance
     */
    private ListCell<AbilityEntry> createAbilityListCell(ViewListener viewListener) {
        return new ListCell<>() {
            private final HBox hbox = new HBox(10);
            private final Label label = new Label();
            private final Button button = new Button("Utiliser");

            {
                hbox.getChildren().addAll(label, button);
                button.setOnAction(event -> {
                    AbilityEntry entry = getItem();
                    if (entry != null && viewListener != null) {
                        viewListener.onUseAbility(entry.abilityId(), event);
                    }
                });
            }

            @Override
            protected void updateItem(AbilityEntry entry, boolean empty) {
                super.updateItem(entry, empty);
                if (empty || entry == null) {
                    setText(null);
                    setGraphic(null);
                    setTooltip(null);
                } else {
                    label.setText(entry.abilityName());

                    hbox.setStyle(
                            "-fx-background-color: " + TypeColor.getTypeColor(entry.abilityType()) + ";" +
                                    "-fx-padding: 6;" +
                                    "-fx-background-radius: 6;"
                    );
                    if (entry.effectiveness() != null) {
                        Tooltip tooltip = new Tooltip(entry.abilityDescription() + "\n" + entry.effectiveness());
                        tooltip.setShowDelay(javafx.util.Duration.millis(100));
                        setTooltip(tooltip);
                    } else {
                        Tooltip tooltip = new Tooltip(entry.abilityDescription());
                        tooltip.setShowDelay(javafx.util.Duration.millis(100));
                        setTooltip(tooltip);
                    }

                    setGraphic(hbox);
                }
            }
        };
    }
}
