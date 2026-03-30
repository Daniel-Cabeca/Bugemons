package ulb.view.windows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import ulb.communication.Message;
import ulb.communication.types.AutoTurnRequestMessage;
import ulb.communication.types.AutoTurnResponseMessage;
import ulb.communication.types.BattleEndCheckMessage;
import ulb.communication.types.SwapRequestMessage;
import ulb.communication.types.UseAbilityRequestMessage;
import ulb.communication.types.UseItemRequestMessage;
import ulb.communication.types.*;
import ulb.controller.towerManager.TowerManager;
import ulb.model.battle.BattleState;
import ulb.model.bugemon.Bugemon;
import ulb.controller.BattleController;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.model.team.Team;
import ulb.model.ability.Ability;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import ulb.model.type.Type;
import ulb.view.WindowPath;

public class BattleWindow extends Window {

	@FXML
	private ListView<Item> inventoryList;
	@FXML
	private ListView<Bugemon> bugemonsList;
	@FXML
	private ListView<Ability> abilitiesList;

	@FXML
	private Label floorLabel;
	@FXML
	private Label roomLabel;
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
	private Label battleLog;

	private Team playerTeam;
	private Inventory playerInventory;
	private BattleController battleController;
	private TowerManager towerManager;

	public void setTowerManager(TowerManager towerManager) {this.towerManager = towerManager;}

	@Override
	public void onLoad() {
		Message m = viewManager.handleMessage(new GetInfoMessage(InfoType.SETUP_GAME));
		if (m instanceof SetupGameModeMessage setup) {
			this.battleController = setup.getBattleController();
			initializeBattle(setup.getTeamA(), setup.getInventory(), setup.getGameMode());
		}
	}

	/**
	 * Initializes the battle according to the teams, inventory and battle mode
	 *
	 * @param playerTeam the player's team
	 * @param playerInventory the player's inventory
	 */
	public void initializeBattle(Team playerTeam, Inventory playerInventory, GameMode gameMode) {
		this.playerTeam = playerTeam;
		this.playerInventory = playerInventory;

        // disables action buttons for the automatic mode
		if (gameMode == GameMode.AUTO) {
			attackButton.setDisable(true);
			itemButton.setDisable(true);
			runButton.setDisable(true);
			switchButton.setDisable(true);
		} else {
			autoButton.setDisable(true);
		}

		initializeGraphicalBattle();
		if (gameMode == GameMode.TOWER) {
			floorLabel.setText("Etage: NO" + towerManager.getFloorNumber());
			roomLabel.setText("Salle: " + towerManager.getCurrentRoomIndex());
		}
	}


	private void initializeGraphicalBattle() {
		// Get the active Bugemons (first non-KO Bugemon)
		Bugemon playerBugemon = battleController.getActiveBugemonSelf();
		Bugemon opponentBugemon = battleController.getActiveBugemonOpponent();

		// Set player Bugemon sprite and stats
		try {
			Image playerImage = new Image(getClass().getResourceAsStream(playerBugemon.getSprite()));
			PlayerBugemon.setImage(playerImage);
		} catch (Exception e) {
			System.err.println("Failed to load player bugemon sprite: " + e.getMessage());
		}

		String playerColor = getColor(playerBugemon.getType());

		PlayerBugemonLabel.setText(playerBugemon.getName() + "  Lv." + playerBugemon.getLevel());
		PlayerBugemonLabel.setStyle("-fx-text-fill: " + playerColor + ";");
		double playerRatio = (double) playerBugemon.getFightStats().getHp() / playerBugemon.getBaseStats().getHp();
		PlayerBugemonHPBar.setProgress(playerRatio);
		updateHPBarColor(PlayerBugemonHPBar, playerRatio);
		PlayerBugemonHPNumber.setText(playerBugemon.getHp() + " / " + playerBugemon.getBaseStats().hp);

		// Set opponent Bugemon sprite and stats
		try {
			Image opponentImage = new Image(getClass().getResourceAsStream(opponentBugemon.getSprite()));
			OpponentBugemon.setImage(opponentImage);
		} catch (Exception e) {
			System.err.println("Failed to load opponent bugemon sprite: " + e.getMessage());
		}

		String opponentColor = getColor(opponentBugemon.getType());

		OpponentBugemonLabel.setText(opponentBugemon.getName() + "  Lv." + opponentBugemon.getLevel());
		OpponentBugemonLabel.setStyle("-fx-text-fill: " + opponentColor + ";");
		double opponentRatio = (double) opponentBugemon.getFightStats().getHp() / opponentBugemon.getBaseStats().getHp();
		OpponentHPBar.setProgress(opponentRatio);
		updateHPBarColor(OpponentHPBar, opponentRatio);
		OpponentHPNumber.setText(opponentBugemon.getHp() + " / " + opponentBugemon.getBaseStats().hp);
	}

	private static String getColor(Type type) {
		String color;
		switch (type) {
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
		return color;
	}

	/**
	 * Handles the Item button click - shows inventory view
	 */
	public void handleItemMenu() {
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
	public void handleBugemonsMenu() {
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
	 */
	public void handleAuto(ActionEvent event) {
		autoButton.setVisible(false);
		autoButton.setManaged(false);

		Message response = viewManager.handleMessage(new AutoTurnRequestMessage());
		BattleState state = null;
		if (response instanceof AutoTurnResponseMessage autoTurnResponse) {
			state = autoTurnResponse.getBattleState();
		}

		displayNextMessage();

		autoButton.setVisible(true);
		autoButton.setManaged(true);

		if (state != null) {
			viewManager.handleMessage(new BattleEndCheckMessage(state, event));
		}
	}

	/**
	 * Handles the Attack button click - shows abilities view
	 */
	public void handleAttack() {
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
	public void handleBackToMenu() {
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

	/**
	 * Indicates whether the player is currently in a forced switch state. This happens when the Bugemon is KO
	 * and the player has to choose a new one.
	 *
	 * @return true if the current battle state is SWAPPING, false otherwise
	 */
	private boolean isForcedSwitch() {
		return battleController != null && battleController.getState() == BattleState.SWAPPING;
	}

	/**
	 * Updates the state of the "Back" buttons in the relevant views.
	 * If the player is in a forced switch, the "Back" button must be
	 * disabled so they cannot leave the switch menu without choosing
	 * a valid replacement Bugémon.
	 */
	private void updateBackButtonsState() {
		boolean disableBack = isForcedSwitch();
		setBackButtonDisabled(bugemonsView, disableBack);
	}

	/**
	* Enables or disables all buttons contained in the given view.
	* This method is used to disable the "Back" button in the Bugémons view during forced switch.
	*
	* @param view the view containing the buttons to update
	* @param disabled true to disable the buttons, false to enable them again
	*/
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

	/**
	 * Applies a manual battle action by sending a message to GameController through ViewManager
	 * and returns the resulting BattleState.
	 *
	 * @param request the message sent to ViewManager
	 * @return state after the action, or null if no controller state is available
	 */
	private BattleState battleStateAfterManualMessage(Message request) {
		Message response = viewManager.handleMessage(request);
		if (response instanceof AutoTurnResponseMessage r) {
			return r.getBattleState();
		}
		return battleController != null ? battleController.getState() : null;
	}

	/**
	 * Sets up the inventory list by displaying all available items in the inventory.
	 * Sends a message to ViewManager when an item is clicked.
	 */
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
								BattleState stateAfter = battleStateAfterManualMessage(new UseItemRequestMessage(item));
								displayMessagesSequentially(() -> {
									checkBattleState(stateAfter, event);
									if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
										handleBackToMenu();
									} else {
										displayInventory();
									}
								});
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

	/**
	 * Sets up the team's bugemons list by indicating which ones are KO
	 * Sends a Swap request message to ViewManager when a Bugemon is clicked
	 */
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
								BattleState stateAfter = battleStateAfterManualMessage(new SwapRequestMessage(bugemon));
								displayMessagesSequentially(() -> {
									checkBattleState(stateAfter, event);
									if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
										handleBackToMenu();
									} else {
										displayTeam();
									}
								});
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


	/**
	 * Sets up the abilities list
	 * Sends a Use Ability Request message to the ViewManager when an ability is selected
	 */
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
								BattleState stateAfter = battleStateAfterManualMessage(new UseAbilityRequestMessage(ability));
								displayMessagesSequentially(() -> {
									checkBattleState(stateAfter, event);
									if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
										handleBackToMenu();
									}
								});
							// refreshAfterAction(event);
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

							String color = getColor(ability.getType());

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

	/**
	 * Displays the available abilities when the "Attaque" button is clicked
	 */
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

	/**
	 * Displays the inventory when the "Objet" button is clicked
	 */
	private void displayInventory() {
		inventoryList.getItems().setAll(playerInventory.getItems().keySet());
		setupInventoryList();
	}

	/**
	 * Displays the team's Bugemons when the "Echanger" button is clicked
	 */
	private void displayTeam() {
		bugemonsList.getItems().setAll(playerTeam.getMembers());
		setupBugemonsList();
	}

	/**
	 * Sends a message to ViewManager to check if the battle is over
	 *
	 * @param state the current battle state
	 */
	private void checkBattleState(BattleState state, ActionEvent event){
		viewManager.handleMessage(new BattleEndCheckMessage(state, event));
		switchBugemon(state);
	}


	/**
	 * Opens the Bugemon menu when the battle state is SWAPPING
	 *
	 * @param state the current battle state
	 */
	private void switchBugemon(BattleState state){
		if (state == BattleState.SWAPPING){
			handleBackToMenu();
			handleBugemonsMenu();
		}
	}

	/**
	* Returns to the main menu
	* @param event the action triggered by clicking the return button
	* @throws IOException if the main menu FXML file cannot be loaded
	*/
	// In tower mode, fleeing goes back to the next room screen — otherwise returns to main menu
	public void handleReturn(ActionEvent event) throws IOException {
		if (gameMode == GameMode.TOWER) {
			viewManager.handleMessage(new TowerFleeMessage());
		} else {
			viewManager.handleMessage(new SwitchWindowMessage(WindowPath.MODE));
		}
	}


	/**
	 * Displays the messages describing the actions of the current turn and updates the view.
	 * Used by auto mode which doesn't need sequential delays.
	 */
	public void displayNextMessage() {
		List<String> logs = battleController.getLogMsg();
		if (logs != null && !logs.isEmpty()) {
			String allMessages = String.join("\n", logs.stream().filter(
					m -> m != null).collect(java.util.stream.Collectors.toList()));
			battleLog.setText(allMessages);
			battleController.clearLogMsg();
		} else {
			battleLog.setText("");
		}
		initializeGraphicalBattle();
	}

	private void hideAllMenus() {
		buttonsGrid.setVisible(false);
		buttonsGrid.setManaged(false);
		autoButton.setVisible(false);
		autoButton.setManaged(false);
		inventoryView.setVisible(false);
		inventoryView.setManaged(false);
		bugemonsView.setVisible(false);
		bugemonsView.setManaged(false);
		abilitiesView.setVisible(false);
		abilitiesView.setManaged(false);
	}

	/**
	 * Displays the log messages step by step
	 *
	 * @param onComplete true if the turn is complete and both action messages have been displayed, false otherwise 
	 */
	private void displayMessagesSequentially(Runnable onComplete) {
		hideAllMenus();
		List<String> logs = new java.util.ArrayList<>(battleController.getLogMsg());
		battleController.clearLogMsg();

		// skip if there are no messages to display
		if (logs.isEmpty()) {
			initializeGraphicalBattle();
			onComplete.run();
			return;
		}

		// null is used as a separator between the two actions of a round
		int sep = logs.indexOf(null);
		if (sep < 0) {
			battleLog.setText(String.join("\n", logs));
			initializeGraphicalBattle();
			onComplete.run();
			return;
		}

		battleLog.setText(String.join("\n", logs.subList(0, sep)));

		// refreshes the sprites only when a Bugemon switch happens
		if (containsSwitchMessage(logs.subList(0, sep))) {
			initializeGraphicalBattle();
		}
		// always updates the HP
		updateHPDisplay(battleController.getHpAfterFirstActionSelf(), battleController.getHpAfterFirstActionOpponent());

		// 1s delay between one turn's action messages
		PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(1));
		pause.setOnFinished(e -> {
			List<String> second = new java.util.ArrayList<>();
			for (String msg : logs.subList(sep + 1, logs.size())) {
				if (msg != null) second.add(msg);
			}
			if (!second.isEmpty()) {
				battleLog.setText(String.join("\n", second));
				initializeGraphicalBattle();
			}
			onComplete.run();
		});
		pause.play();
	}


	/**
	 * Checks whether the provided messages include a Bugémon switch announcement.
	 *
	 * @param messages the messages displayed for the current phase
	 * @return {@code true} if a switch announcement is present, {@code false} otherwise
	 */
	private boolean containsSwitchMessage(List<String> messages) {
		for (String message : messages) {
			if (message != null && message.contains("envoyé")) {
				return true;
			}
		}
		return false;
	}

	private void updateHPDisplay(int selfHp, int opponentHp) {
		Bugemon self = battleController.getActiveBugemonSelf();
		Bugemon opponent = battleController.getActiveBugemonOpponent();
		double selfRatio = (double) selfHp / self.getBaseStats().getHp();
		double oppRatio = (double) opponentHp / opponent.getBaseStats().getHp();
		PlayerBugemonHPBar.setProgress(selfRatio);
		PlayerBugemonHPNumber.setText(selfHp + " / " + self.getBaseStats().hp);
		updateHPBarColor(PlayerBugemonHPBar, selfRatio);
		OpponentHPBar.setProgress(oppRatio);
		OpponentHPNumber.setText(opponentHp + " / " + opponent.getBaseStats().hp);
		updateHPBarColor(OpponentHPBar, oppRatio);
	}

	private void updateHPBarColor(ProgressBar bar, double ratio) {
		bar.getStyleClass().removeAll("hp-bar-green", "hp-bar-yellow", "hp-bar-red");
		if (ratio > 0.5) {
			bar.getStyleClass().add("hp-bar-green");
		} else if (ratio > 0.25) {
			bar.getStyleClass().add("hp-bar-yellow");
		} else {
			bar.getStyleClass().add("hp-bar-red");
		}
	}

}
