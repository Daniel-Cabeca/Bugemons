package ulb.view.windows;

// import java.lang.classfile.Label;
import java.util.ArrayList;
import java.util.List;

// import javax.swing.text.html.ListView;

import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.layout.GridPane;

import ulb.communication.Message;
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
	private Label PlayerLevelLabel;
	@FXML
	private Label PlayerBugemonHPNumber;
	@FXML
	private ProgressBar OpponentHPBar;
	@FXML
	private Label OpponentBugemonLabel;
	@FXML
	private Label OpponentLevelLabel;
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
	private VBox messageBox;
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
	private GameMode gameMode;
	private MediaPlayer mediaPlayer;
	private boolean waitingForOpponentAction = false;
	
	@Override
	public void onLoad() {
		this.towerManager = gameController.getTowerManager();
		Message m = sendMessage(new GetInfoMessage(InfoType.SETUP_GAME));
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
		this.gameMode = gameMode;

        // disables action buttons for the automatic mode
		if (gameMode == GameMode.AUTO) {
			attackButton.setDisable(true);
			itemButton.setDisable(true);
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

		PlayerBugemonLabel.setText(playerBugemon.getName());
		PlayerLevelLabel.setText("Lv." + playerBugemon.getLevel());
		PlayerBugemonLabel.setStyle("-fx-text-fill: " + playerColor + ";");
		double playerRatio = (double) playerBugemon.getHp() / playerBugemon.getBaseStats().getHp();
		PlayerBugemonHPBar.setProgress(playerRatio);
		updateHPBarColor(PlayerBugemonHPBar, playerRatio);
		PlayerBugemonHPNumber.setText(playerBugemon.getHp() + " / " + playerBugemon.getBaseStats().getHp());

		// Set opponent Bugemon sprite and stats
		try {
			Image opponentImage = new Image(getClass().getResourceAsStream(opponentBugemon.getSprite()));
			OpponentBugemon.setImage(opponentImage);
		} catch (Exception e) {
			System.err.println("Failed to load opponent bugemon sprite: " + e.getMessage());
		}

		String opponentColor = getColor(opponentBugemon.getType());

		OpponentBugemonLabel.setText(opponentBugemon.getName());
		OpponentLevelLabel.setText("Lv." + opponentBugemon.getLevel());
		OpponentBugemonLabel.setStyle("-fx-text-fill: " + opponentColor + ";");
		double opponentRatio = (double) opponentBugemon.getHp() / opponentBugemon.getBaseStats().getHp();
		OpponentHPBar.setProgress(opponentRatio);
		updateHPBarColor(OpponentHPBar, opponentRatio);
		OpponentHPNumber.setText(opponentBugemon.getHp() + " / " + opponentBugemon.getBaseStats().getHp());
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

		Message response = sendMessage(new AutoTurnRequestMessage());
		BattleState state = null;
		if (response instanceof AutoTurnResponseMessage autoTurnResponse) {
			state = autoTurnResponse.getBattleState();
		}

		displayMessagesSequentially(() -> {
			// refreshAfterAction gère la suite du combat
			refreshAfterAction(event);

			autoButton.setVisible(true);
			autoButton.setManaged(true);
		});
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
	 * Applies a manual battle action by sending a typed message directly to GameController
	 * and returns the resulting BattleState.
	 *
	 * @param request the message sent to GameController
	 * @return state after the action, or null if no controller state is available
	 */
	private BattleState battleStateAfterManualMessage(Message request) {
		Message response = sendMessage(request);
		if (response instanceof AutoTurnResponseMessage r) {
			return r.getBattleState();
		}
		return battleController != null ? battleController.getState() : null;
	}

	/**
	 * Sets up the inventory list by displaying all available items in the inventory.
	 * Sends a message to GameController when an item is clicked.
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
	 * Sends a swap request message to GameController when a Bugemon is clicked
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
	 * Sends a Use Ability Request message to GameController when an ability is selected
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
	 * Handles everything that must happen after a player action
	 * (attack, item use, switch, etc.), i.e. refreshes everything.
	 *
	 * Steps:
	 * 1. retrieve the current battle state
	 * 2. return to the main menu unless the player is in a forced switch
	 * 3. check whether the battle state requires a specific transition
	 * 4. display the next battle message
	 * 5. if waiting for the opponent's action, start the waiting logic
	 *
	 * @param event the JavaFX event that triggered the action
	 */
	private void refreshAfterAction(ActionEvent event) {
		BattleState state = battleController.getState();

		// Return to the main menu only if the player is not forced
    	// to select a new Bugémon
		if (state != BattleState.SWAPPING) {
			handleBackToMenu();
		}
		// Check whether the current state requires a specific action
    	// (end of game, forced switch, etc.)
		checkBattleState(state, event);

		// Display the next message in the battle message queue
		displayNextMessage();

		// If it is now the opponent's turn, wait for their action
		if (state == BattleState.WAITING) {
			waitingForOpponentAction(event);
		}
	}

	/**
	 * Waits until the opponent has finished their action (the opponent may play in a separate thread).
	 * During that time:
	 * - player interactions are disabled
	 * - the code waits until the state is no longer WAITING
	 * - the interface is then refreshed on the JavaFX thread
	 *
	 * @param event the original JavaFX event
	 */
	private void waitingForOpponentAction(ActionEvent event) {
		// Prevent starting multiple waiting threads at the same time
		if (waitingForOpponentAction) {
			return;
		}

		waitingForOpponentAction = true;

		// Disable battle controls while the opponent is playing
		setBattleInputsDisabled(true);
		
		Thread waitingThread = new Thread(() -> {
			try {
				// Active waiting loop:
				// as long as the opponent has not finished their turn
				// and the game is not over, wait in short intervals
				while (battleController.getState() == BattleState.WAITING && !battleController.isGameFinished()) {
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				// Restore the interrupted status if the thread is interrupted	
				Thread.currentThread().interrupt();
				}
			// Any JavaFX UI update must be executed on the UI thread	
			Platform.runLater(() -> {
				waitingForOpponentAction = false;

				// Re-enable player interactions
				setBattleInputsDisabled(false);

				// Display any new messages generated by the opponent's action
				displayNextMessage();

				// Re-check the battle state after the opponent has acted
				checkBattleState(battleController.getState(), event);
			});
		});
		
		// Mark the thread as daemon so it does not prevent the application from closing
		waitingThread.setDaemon(true);
		waitingThread.start();
	}

	/**
	 * Enables or disables the main battle controls.
	 *
	 * When the opponent is playing or the interface is waiting for an update,
	 * the player must not be able to interact with the battle buttons.
	 *
	 * The logic also takes automatic mode into account:
	 * - in automatic mode, some buttons must remain disabled
	 * - the auto button is enabled only when automatic mode is active
	 *
	 * @param disabled true to block interactions, false to allow them again
	 */
	private void setBattleInputsDisabled(boolean disabled) {
		// Main battle buttons
		attackButton.setDisable(disabled || gameMode == GameMode.AUTO);
		itemButton.setDisable(disabled || gameMode == GameMode.AUTO);
		runButton.setDisable(disabled);
		switchButton.setDisable(disabled || gameMode == GameMode.AUTO);



		// Also disable subviews to prevent any interaction while waiting
		inventoryView.setDisable(disabled);
		bugemonsView.setDisable(disabled);
		abilitiesView.setDisable(disabled);
	}

	 /* Displays the available abilities when the "Attaque" button is clicked
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
	 * Sends a message to GameController to check if the battle is over
	 *
	 * @param state the current battle state
	 */
	private void checkBattleState(BattleState state, ActionEvent event){
		if (state == BattleState.WON || state == BattleState.LOST){
			stopBattleMusic();
		}
		sendMessage(new BattleEndCheckMessage(state, event));
		switchBugemon(state);
	}

	private void stopBattleMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.dispose();
			mediaPlayer = null;
		}
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
		*/
	// In tower mode, fleeing goes back to the next room screen — otherwise returns to main menu
	public void handleReturn(ActionEvent event) {
		stopBattleMusic();
		if (gameMode == GameMode.TOWER) {
			sendMessage(new TowerFleeMessage());
		} else {
			switchWindow(WindowPath.MODE);
		}
	}


	/**
	 * Displays the messages describing the actions of the current turn and updates the view.
	 * Used by auto mode which doesn't need sequential delays.
	 */
	public void displayNextMessage() {
		List<String> logs = battleController.getLogMsg();
		if (logs != null && !logs.isEmpty()) {
			String allMessages = logs.stream().filter(m -> m != null).map(m -> wrapText(m, 35)).collect(java.util.stream.Collectors.joining("\n"));
			battleLog.setText(allMessages);
			messageBox.setVisible(true);
			messageBox.setManaged(true);
			battleController.clearLogMsg();
		} else {
			battleLog.setText("");
			messageBox.setVisible(false);
			messageBox.setManaged(false);
		}
		initializeGraphicalBattle();
	}

	private void hideAllMenus() {
		buttonsGrid.setVisible(false);
		buttonsGrid.setManaged(false);
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
		List<String> rawLogs = new java.util.ArrayList<>(battleController.getLogMsg());
		battleController.clearLogMsg();

		// null is used as a separator between the two actions of a round
		int sep = rawLogs.indexOf(null);
		List<String> phase1;
		List<String> phase2;
		if (sep < 0) {
			phase1 = rawLogs.stream().filter(m -> m != null).collect(java.util.stream.Collectors.toList());
			phase2 = new java.util.ArrayList<>();
		} else {
			phase1 = rawLogs.subList(0, sep).stream().filter(m -> m != null).collect(java.util.stream.Collectors.toList());
			phase2 = rawLogs.subList(sep + 1, rawLogs.size()).stream().filter(m -> m != null).collect(java.util.stream.Collectors.toList());
		}

		if (phase1.isEmpty() && phase2.isEmpty()) {
			initializeGraphicalBattle();
			onComplete.run();
			return;
		}

		messageBox.setVisible(true);
		messageBox.setManaged(true);

		// Update HP BEFORE showing messages so graphics match what the text describes
		if (sep >= 0) {
			// Phase 1 describes the player's action: show HP after player's action
			updateHPDisplay(battleController.getHpAfterFirstActionSelf(), battleController.getHpAfterFirstActionOpponent());
		} else {
			// No separator: show final state immediately
			initializeGraphicalBattle();
		}

		Runnable closeAndComplete = () -> {
			battleLog.setText("");
			messageBox.setVisible(false);
			messageBox.setManaged(false);
			onComplete.run();
		};

		Runnable afterPhase1 = () -> {
			if (phase2.isEmpty()) {
				closeAndComplete.run();
			} else {
				// Phase 2 describes the opponent's action: show final HP before phase 2
				initializeGraphicalBattle();
				displayPhase(phase2, 0, closeAndComplete);
			}
		};

		if (phase1.isEmpty()) {
			afterPhase1.run();
		} else {
			displayPhase(phase1, 0, afterPhase1);
		}
	}

	// Breaks text into lines so no line exceeds maxChars, splitting only at spaces
	private String wrapText(String text, int maxChars) {
		StringBuilder result = new StringBuilder();
		for (String line : text.split("\n")) {
			if (result.length() > 0) result.append("\n");
			String[] words = line.split(" ");
			int lineLen = 0;
			for (String word : words) {
				if (lineLen == 0) {
					result.append(word);
					lineLen = word.length();
				} else if (lineLen + 1 + word.length() <= maxChars) {
					result.append(" ").append(word);
					lineLen += 1 + word.length();
				} else {
					result.append("\n").append(word);
					lineLen = word.length();
				}
			}
		}
		return result.toString();
	}

	/**
	 * Displays messages from a list one by one, advancing automatically with a 2-second delay.
	 */
	private void displayPhase(List<String> messages, int index, Runnable onComplete) {
		if (index >= messages.size()) {
			onComplete.run();
			return;
		}
		battleLog.setText(wrapText(messages.get(index), 35));

		// refreshes the sprites only when a Bugemon switch happens
		if (containsSwitchMessage(messages)) {
			initializeGraphicalBattle();
		}
		// always updates the HP
		updateHPDisplay(battleController.getActiveBugemonSelf().getHp(), battleController.getActiveBugemonOpponent().getHp());

		// 2s delay between one turn's action messages
		PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(2));
		pause.setOnFinished(e -> displayPhase(messages, index + 1, onComplete));
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
		PlayerBugemonHPNumber.setText(selfHp + " / " + self.getBaseStats().getHp());
		updateHPBarColor(PlayerBugemonHPBar, selfRatio);
		OpponentHPBar.setProgress(oppRatio);
		OpponentHPNumber.setText(opponentHp + " / " + opponent.getBaseStats().getHp());
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
