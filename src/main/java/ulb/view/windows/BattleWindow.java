package ulb.view.windows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import ulb.model.battle.BattleState;
import ulb.model.bugemon.Bugemon;
import ulb.controller.BattleController;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.model.team.Team;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.controller.action.UseItem;
import ulb.controller.strategy.StrategyRandom;
import ulb.controller.action.UseAbility;
import ulb.controller.action.Swap;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import ulb.view.handler.Window;

public class BattleWindow extends Window {

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
	private ProgressBar OpponentHPBar;
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
	@FXML
	private Label battleLog;
	@FXML
	private Button nextMessageButton;




	private Team playerTeam;
	private Inventory playerInventory;
	private BattleController battleController;
	private Player player;
	private boolean automaticMode;
	private boolean waitingForOpponentAction = false;


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
		this.automaticMode = automatic;

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

		String playerColor;
		switch (playerBugemon.getType()) {
			case PYRO:
				playerColor = "#ED2424";
				break;
			case FLORA:
				playerColor = "#50A346";
				break;
			case AQUA:
				playerColor = "#51B0F0";
				break;
			case LITHO:
				playerColor = "#807979";
				break;
			default:
				playerColor = "#ced4da";
		}

		PlayerBugemonLabel.setText(playerBugemon.getName() + " (" + playerBugemon.getType().name() + ")"
				+ " Level: " +  playerBugemon.getLevel());
		PlayerBugemonLabel.setStyle("-fx-text-fill: " + playerColor + ";");
		PlayerBugemonHPBar.setProgress((double) playerBugemon.getFightStats().getHp() / playerBugemon.getBaseStats().getHp());
		PlayerBugemonHPNumber.setText("PV: " + playerBugemon.getHp() + "/" + playerBugemon.getBaseStats().hp);

		// Set opponent Bugemon sprite and stats
		try {
			Image opponentImage = new Image(opponentBugemon.getSprite());
			OpponentBugemon.setImage(opponentImage);
		} catch (Exception e) {
			System.err.println("Failed to load opponent bugemon sprite: " + e.getMessage());
		}

		String opponentColor;
		switch (opponentBugemon.getType()) {
			case PYRO:
				opponentColor = "#ED2424";
				break;
			case FLORA:
				opponentColor = "#50A346";
				break;
			case AQUA:
				opponentColor = "#51B0F0";
				break;
			case LITHO:
				opponentColor = "#807979";
				break;
			default:
				opponentColor = "#ced4da";
		}

		OpponentBugemonLabel.setText(opponentBugemon.getName() + " (" + opponentBugemon.getType().name() + ")"
				+ " Level: " + opponentBugemon.getLevel());
		OpponentBugemonLabel.setStyle("-fx-text-fill: " + opponentColor + ";");
		OpponentHPBar.setProgress((double) opponentBugemon.getFightStats().getHp() / opponentBugemon.getBaseStats().getHp());
		OpponentHPNumber.setText("PV: " + opponentBugemon.getHp() + "/" +  opponentBugemon.getBaseStats().hp);
	}

	public void initializebattleMessage(){

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
			updateBackButtonsState();
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
			updateBackButtonsState();
		}
	}

	/**
	 * Handles the Auto button click - equivalent to a turn in the automatic battle
	 *
	 * @param event the action triggered by clicking the return button
	 * @throws IOException if the main menu FXML file cannot be loaded when going back to main menu
	 */
	public void handleAuto(ActionEvent event) throws IOException {
		StrategyRandom strategyRandom = new StrategyRandom(battleController);
		BattleState state = strategyRandom.playAutoTurn();

		displayNextMessage();

		this.checkBattleEnd(state, event);
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
			updateBackButtonsState();
		}
	}

	/**
	 * Handles the Back button click from inventory - returns to battle menu
	 */
	public void handleBackToMenu(ActionEvent event) {
		if (isForcedSwitch()) {
			updateBackButtonsState();
		}
		
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

		updateBackButtonsState();
	}

	private boolean isForcedSwitch() {
		return battleController != null && battleController.getState() == BattleState.SWAPPING;
	}

	private void updateBackButtonsState() {
		boolean disableBack = isForcedSwitch();
		setBackButtonDisabled(bugemonsView, disableBack);
	}

	private void setBackButtonDisabled(VBox view, boolean disabled) {
		if (view == null) {
			return;
		}

		for (Node child : view.getChildren()) {
			if (child instanceof Button button) {
				button.setDisable(disabled);
			}
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
					private final Button button = new Button("Utiliser");

					{
						imageView.setFitHeight(30);
						imageView.setFitWidth(30);
						hbox.getChildren().addAll(imageView, label, button);
						button.setOnAction(event -> {
							Item item = getItem();
							if (item != null) {
								UseItem useItem = new UseItem(item);
								battleController.useAction(useItem);
								refreshAfterAction(event);
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
					private final Button button = new Button("Échanger");

					{
						imageView.setFitHeight(30);
						imageView.setFitWidth(30);
						hbox.getChildren().addAll(imageView, label, button);
						button.setOnAction(event -> {
							Bugemon bugemon = getItem();
							if (bugemon != null) {
								Swap swap = new Swap(bugemon);
								battleController.useAction(swap);
								refreshAfterAction(event);
							}
						});
					}

					@Override
					protected void updateItem(Bugemon bugemon, boolean empty) {
						super.updateItem(bugemon, empty);
						if (empty || bugemon == null || bugemon.equals(battleController.getActiveBugemonSelf())) {
							setText(null);
							setGraphic(null);
						} else {
							try {
								Image image = new Image(getClass().getResourceAsStream(bugemon.getSprite()));
								imageView.setImage(image);
							} catch (Exception e) {
								System.err.println("Failed to load bugemon image: " + e.getMessage());
							}
							label.setText(bugemon.isKO() ? bugemon.getName() + " (KO)" : bugemon.getName());
							button.setDisable(bugemon.isKO());
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
					private final Button button = new Button("Utiliser");

					{
						hbox.getChildren().addAll(label, button);
						button.setOnAction(event -> {
							Ability ability = getItem();
							if (ability != null) {
								UseAbility useAbility = new UseAbility(ability);
								battleController.useAction(useAbility);
								refreshAfterAction(event);

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

							String color;
							switch (ability.getType()) {
								case PYRO:
									color = "#ED2424";
									break;
								case FLORA:
									color = "#50A346";
									break;
								case AQUA:
									color = "#51B0F0";
									break;
								case LITHO:
									color = "#807979";
									break;
								default:
									color = "#ced4da";
							}
							hbox.setStyle(
									"-fx-background-color: " + color + ";" +
											"-fx-padding: 6;" +
											"-fx-background-radius: 6;"
							);

							String effectiveness = battleController.getEffectiveness(ability);
							Tooltip tooltip = new Tooltip(effectiveness);
							// displays the message only if effectiveness is not normal
							if (effectiveness != null) {
								tooltip.setShowDelay(javafx.util.Duration.millis(100));
								setTooltip(tooltip);
							}

							setGraphic(hbox);

						}
					}
				};
			}
		});
	}

	private void refreshAfterAction(ActionEvent event) {
		BattleState state = battleController.getState();

		if (state != BattleState.SWAPPING) {
			handleBackToMenu(event);
		}

		checkBattleState(state, event);
		displayNextMessage();

		if (state == BattleState.WAITING) {
			waitingForOpponentAction(event);
		}
	}

	private void waitingForOpponentAction(ActionEvent event) {
		if (waitingForOpponentAction) {
			return;
		}

		waitingForOpponentAction = true;
		setBattleInputsDisabled(true);
		
		Thread waitingThread = new Thread(() -> {
			try {
				while (battleController.getState() == BattleState.WAITING && !battleController.isGameFinished()) {
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			Platform.runLater(() -> {
				waitingForOpponentAction = false;
				setBattleInputsDisabled(false);
				displayNextMessage();
				checkBattleState(battleController.getState(), event);
			});
		});
	
		waitingThread.setDaemon(true);
		waitingThread.start();
	}

	private void setBattleInputsDisabled(boolean disabled) {
		attackButton.setDisable(disabled || automaticMode);
		itemButton.setDisable(disabled || automaticMode);
		runButton.setDisable(disabled || automaticMode);
		switchButton.setDisable(disabled || automaticMode);
		autoButton.setDisable(disabled || !automaticMode);

		inventoryView.setDisable(disabled);
		bugemonsView.setDisable(disabled);
		abilitiesView.setDisable(disabled);
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

	private void checkBattleState(BattleState state, ActionEvent event){
		checkBattleEnd(state, event);
		switchBugemon(state, event);
	}

	public void checkBattleEnd(BattleState state, ActionEvent event){
		if (state == BattleState.WON) {
			battleController.switchToBattleEndWindow(true, event);
		} else if (state == BattleState.LOST) {
			battleController.switchToBattleEndWindow(false, event);
		}
	}

	private void switchBugemon(BattleState state, ActionEvent event){
		if (state == BattleState.SWAPPING){
			handleBackToMenu(event);
			handleBugemonsMenu(event);
		}
	}

	/**
	* Returns to the main menu
	* @param event the action triggered by clicking the return button
	* @throws IOException if the main menu FuXML file cannot be loaded
	*/
	public void handleReturn(ActionEvent event) throws IOException {
		switchWindow(event, MODE_WINDOW_PATH);
	}


	/**
	 * Displays the messages describing the actions of the current turn and updates the view
	 */
	public void displayNextMessage() {

		List<String> logs = battleController.getLogMsg();
		if (logs != null && !logs.isEmpty()) {
			String allMessages = String.join("\n", logs);
			battleLog.setText(allMessages);
			battleController.clearLogMsg();
		} else {
			battleLog.setText("");
		}
		initializeGraphicalBattle();
	}


}
