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
import ulb.view.windows.BattleWindow.AbilityEntry;
import ulb.view.windows.BattleWindow.BugemonEntry;
import ulb.view.windows.BattleWindow.InventoryEntry;
import ulb.view.windows.BattleWindow.ViewListener;

public class BattleWindowSetupHelper {

    public void setupInventoryList(ListView<InventoryEntry> inventoryList, ViewListener viewListener) {
        inventoryList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<InventoryEntry> call(ListView<InventoryEntry> listView) {
                return createInventoryListCell(viewListener);
            }
        });
    }

    public void setupBugemonsList(ListView<BugemonEntry> bugemonsList, ViewListener viewListener) {
        bugemonsList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<BugemonEntry> call(ListView<BugemonEntry> listView) {
                return createBugemonListCell(viewListener);
            }
        });
    }

    public void setupAbilitiesList(ListView<AbilityEntry> abilitiesList, ViewListener viewListener) {
        abilitiesList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<AbilityEntry> call(ListView<AbilityEntry> listView) {
                return createAbilityListCell(viewListener);
            }
        });
    }

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
                        System.err.println("Failed to load item image: " + e.getMessage());
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
                        System.err.println("Failed to load bugemon image: " + e.getMessage());
                    }
                    label.setText(entry.ko() ? entry.bugemonName() + " (KO)" : entry.bugemonName());
                    button.setDisable(!entry.selectable());
                    setGraphic(hbox);
                }
            }
        };
    }

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
                            "-fx-background-color: " + entry.color() + ";" +
                                    "-fx-padding: 6;" +
                                    "-fx-background-radius: 6;"
                    );
                    if (entry.effectiveness() != null) {
                        Tooltip tooltip = new Tooltip(entry.effectiveness() + "\n" + entry.abilityDescription());
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
