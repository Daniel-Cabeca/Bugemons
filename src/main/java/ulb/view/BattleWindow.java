package ulb.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

import ulb.model.battle.BattleState;
import ulb.model.bugemon.Bugemon;
import ulb.controller.BattleController;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.model.team.Team;
import ulb.model.Player;
import ulb.model.ability.Ability;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.ListCell;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class BattleWindow {
	@FXML
	private GridPane playerTeamGrid;
	@FXML
	private GridPane opponentTeamGrid;
	@FXML
	private ListView<Item> inventoryList;
	@FXML
	private ListView<Bugemon> bugemonsList;
	@FXML
	private ListView<Ability> abilitiesList;
	
	@FXML
	private ImageView OpponentBugemon;
	@FXML
	private ImageView PlayerBugemon;
	@FXML
	private ProgressBar PlayerBugemonHPBar;
	@FXML
	private Label PlayerBugemonLabel;
	@FXML
	private Label PlayerBugemonHPNumber;
	@FXML
	private ProgressBar OppentHPBar;
	@FXML
	private Label OpponentBugemonLabel;
	@FXML
	private Label OpponentHPNumber;
	@FXML
	private Button backButton;
	@FXML
	private Button autoButton;
	@FXML
	private GridPane buttonsGrid;
	@FXML
	private VBox inventoryView;
	@FXML
	private VBox bugemonsView;
	@FXML
	private VBox abilitiesView;
	@FXML
	private Button attackButton;
	@FXML
	private Button itemButton;
	@FXML
	private Button runButton;
	@FXML
	private Button switchButton;
	@FXML
	private Button backToMenuButton;

	private Team playerTeam;
	private Inventory playerInventory;
	private BattleController battleController;
	private Player player;

	public void setBattleController(BattleController battleController) {
		this.battleController = battleController;
	}

	public void setPlayer(Player player) { this.player = player; }

	/**
	 * Initializes the battle according to the teams, inventory and battle mode
	 *
	 * @param playerTeam the player's team
	 * @param opponentTeam the opponent's team
	 * @param playerInventory the player's inventory
	 * @param automatic  {@code true} if the battle mode is automatic, {@code false} if the mode is controlled
	 */
	public void initializeBattle(Team playerTeam, Team opponentTeam, Inventory playerInventory, boolean automatic) {
		this.playerTeam = playerTeam;
		this.playerInventory = playerInventory;

		// disables action buttons for the automatic mode
		if (automatic) {
			attackButton.setDisable(true);
			itemButton.setDisable(true);
			runButton.setDisable(true);
			switchButton.setDisable(true);
		} else {
			autoButton.setDisable(true);
		}
		
		initializeGraphicalBattle();
	}
	
	private void initializeGraphicalBattle() {
		// Get the active Bugemons (first non-KO Bugemon)
		Bugemon playerBugemon = battleController.getActiveBugemonSelf();
		Bugemon opponentBugemon = battleController.getActiveBugemonOpponent();
		
		// Set player Bugemon sprite and stats
		try {
			Image playerImage = new Image(playerBugemon.getSprite());
			PlayerBugemon.setImage(playerImage);
		} catch (Exception e) {
			System.err.println("Failed to load player bugemon sprite: " + e.getMessage());
		}
		
		PlayerBugemonLabel.setText(playerBugemon.getName() + " (" + playerBugemon.getType().name() + ")");
		PlayerBugemonHPNumber.setText("HP: " + playerBugemon.getFightStats().getHp() + "/" + playerBugemon.getBaseStats().getHp());
		PlayerBugemonHPBar.setProgress((double) playerBugemon.getFightStats().getHp() / playerBugemon.getBaseStats().getHp());
		
		// Set opponent Bugemon sprite and stats
		try {
			Image opponentImage = new Image(opponentBugemon.getSprite());
			OpponentBugemon.setImage(opponentImage);
		} catch (Exception e) {
			System.err.println("Failed to load opponent bugemon sprite: " + e.getMessage());
		}
		
		OpponentBugemonLabel.setText(opponentBugemon.getName() + " (" + opponentBugemon.getType().name() + ")");
		OpponentHPNumber.setText("HP: " + opponentBugemon.getFightStats().getHp() + "/" + opponentBugemon.getBaseStats().getHp());
		OppentHPBar.setProgress((double) opponentBugemon.getFightStats().getHp() / opponentBugemon.getBaseStats().getHp());
	}
	
	/**
	 * Handles the Item button click - shows inventory view
	 */
	public void handleItemMenu(ActionEvent event) {
		if (buttonsGrid != null && inventoryView != null) {
			buttonsGrid.setVisible(false);
			buttonsGrid.setManaged(false);
			inventoryView.setVisible(true);
			inventoryView.setManaged(true);
			displayInventory();
		}
	}

	/**
	 * Handles the Switch button click - shows team view
	 */
	public void handleBugemonsMenu(ActionEvent event) {
		if (buttonsGrid != null && bugemonsView != null) {
			buttonsGrid.setVisible(false);
			buttonsGrid.setManaged(false);
			bugemonsView.setVisible(true);
			bugemonsView.setManaged(true);
			displayTeam();
		}
	}

	/**
	 * Handles the Auto button click - equivalent to a turn in the automatic battle
	 *
	 * @param event the action triggered by clicking the return button
	 * @throws IOException if the main menu FXML file cannot be loaded when going back to main menu
	 */
	public void handleAuto(ActionEvent event) throws IOException {
		BattleState state = battleController.playAutoTurn();
		initializeGraphicalBattle();

		if (state == BattleState.WON) {
			battleController.switchToBattleEndWindow(true, event);
		} else if (state == BattleState.LOST) {
			battleController.switchToBattleEndWindow(false, event);
		}
	}

	/**
	 * Handles the Attack button click - shows abilities view
	 */
	public void handleAttack(ActionEvent event) {
		if (buttonsGrid != null && abilitiesView != null) {
			buttonsGrid.setVisible(false);
			buttonsGrid.setManaged(false);
			abilitiesView.setVisible(true);	
			abilitiesView.setManaged(true);
			displayAbilities();
		}
	}

	/**
	 * Handles the Back button click from inventory - returns to battle menu
	 */
	public void handleBackToMenu(ActionEvent event) {
		if (buttonsGrid != null && inventoryView != null) {
			inventoryView.setVisible(false);
			inventoryView.setManaged(false);
			bugemonsView.setVisible(false);
			bugemonsView.setManaged(false);
			abilitiesView.setVisible(false);
			abilitiesView.setManaged(false);
			buttonsGrid.setVisible(true);
			buttonsGrid.setManaged(true);
		}
	}

	private void setupInventoryList() {
		inventoryList.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
			@Override
			public ListCell<Item> call(ListView<Item> listView) {
				return new ListCell<Item>() {
					private final HBox hbox = new HBox(10);
					private final ImageView imageView = new ImageView();
					private final Label label = new Label();
					private final Button button = new Button("Use");

					{
						imageView.setFitHeight(30);
						imageView.setFitWidth(30);
						hbox.getChildren().addAll(imageView, label, button);
						button.setOnAction(event -> {
							Item item = getItem();
							if (item != null) {
								battleController.useItem(item);
								// Refresh display	
								initializeGraphicalBattle();
								displayInventory();
							}
						});
					}

					@Override
					protected void updateItem(Item item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setGraphic(null);
						} else {
							try {
								Image image = new Image(getClass().getResourceAsStream(item.getSprite()));
								imageView.setImage(image);
							} catch (Exception e) {
								System.err.println("Failed to load item image: " + e.getMessage());
							}
							label.setText(item.getName() + " x" + playerInventory.getItems().get(item));
							button.setDisable(!battleController.checkItem(item));
							setGraphic(hbox);
						}
					}
				};
			}
		});
	}

		private void setupBugemonsList() {
		bugemonsList.setCellFactory(new Callback<ListView<Bugemon>, ListCell<Bugemon>>() {
			@Override
			public ListCell<Bugemon> call(ListView<Bugemon> listView) {
				return new ListCell<Bugemon>() {
					private final HBox hbox = new HBox(10);
					private final ImageView imageView = new ImageView();
					private final Label label = new Label();
					private final Button button = new Button("Switch");

					{
						imageView.setFitHeight(30);
						imageView.setFitWidth(30);
						hbox.getChildren().addAll(imageView, label, button);
						button.setOnAction(event -> {
							Bugemon bugemon = getItem();
							if (bugemon != null) {
								battleController.setActiveBugemon(bugemon);
								// Refresh display	
								initializeGraphicalBattle();
								displayTeam();
							}
						});
					}

					@Override
					protected void updateItem(Bugemon bugemon, boolean empty) {
						super.updateItem(bugemon, empty);
						if (empty || bugemon == null || bugemon.equals(battleController.getActiveBugemonSelf())) {
							setText(null);
						} else {
							try {
								Image image = new Image(getClass().getResourceAsStream(bugemon.getSprite()));
								imageView.setImage(image);
							} catch (Exception e) {
								System.err.println("Failed to load bugemon image: " + e.getMessage());
							}
							label.setText(bugemon.getName());
							setGraphic(hbox);
						}
					}
				};
			}
		});
	}


	private void setupAbilitiesList() {
		abilitiesList.setCellFactory(new Callback<ListView<Ability>, ListCell<Ability>>() {
			@Override
			public ListCell<Ability> call(ListView<Ability> listView) {
				return new ListCell<Ability>() {
					private final HBox hbox = new HBox(10);
					private final Label label = new Label();
					private final Button button = new Button("Use");

					{
						hbox.getChildren().addAll(label, button);
						button.setOnAction(event -> {
							Ability ability = getItem();
							if (ability != null) {
								battleController.useAbility(ability);
								// Refresh display	
								initializeGraphicalBattle();
								displayTeam();
							}
						});
					}

					@Override
					protected void updateItem(Ability ability, boolean empty) {
						super.updateItem(ability, empty);
						if (empty || ability == null) {
							setText(null);
						} else {
							label.setText(ability.getName());
							setGraphic(hbox);
						}
					}
				};
			}
		});
	}


	private void displayAbilities() {
		List<Ability> abilities = new ArrayList<>();
		for (Ability ability : battleController.getActiveBugemonSelf().getAbilities()) {
			if (ability != null) {
				abilities.add(ability);
			}
		}
		abilitiesList.getItems().setAll(abilities);
		setupAbilitiesList();
	}

	private void displayInventory() {
		inventoryList.getItems().setAll(playerInventory.getItems().keySet());
		setupInventoryList();
	}

	private void displayTeam() {
		bugemonsList.getItems().setAll(playerTeam.getMembers());
		setupBugemonsList();
	}

	/**
	* Returns to the main menu
	* @param event the action triggered by clicking the return button
	* @throws IOException if the main menu FXML file cannot be loaded
	*/
	public void handleReturn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/MainMenu.fxml"));
		Parent root = loader.load();
		MainMenu controller = loader.getController();
		controller.setPlayer(player);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.getScene().setRoot(root);
	}
}
