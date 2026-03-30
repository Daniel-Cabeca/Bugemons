package ulb.view.windows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.layout.GridPane;

import ulb.controller.GameController;
import ulb.controller.towerManager.TowerManager;
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
	private Button backToMenuButton;
	@FXML
	private Label battleLog;

	private Team playerTeam;
	private Inventory playerInventory;
	private BattleController battleController;
	private TowerManager towerManager;
	private boolean tower;
	private GameController gameController;
	private Player player;
	private boolean automaticMode;
	private boolean waitingForOpponentAction = false;
	private MediaPlayer mediaPlayer;


	public void setBattleController(BattleController battleController) { this.battleController = battleController; }

	public void setGameController(GameController gameController){ this.gameController = gameController;}

	public void setPlayer(Player player) { this.player = player; }

	public void setTowerManager(TowerManager towerManager) {this.towerManager = towerManager;}

	/**
	 * Initializes the battle according to the teams, inventory and battle mode
	 *
	 * @param playerTeam the player's team
	 * @param opponentTeam the opponent's team
	 * @param playerInventory the player's inventory
	 * @param automatic  {@code true} if the battle mode is automatic, {@code false} if the mode is controlled
	 */
	public void initializeBattle(Team playerTeam, Team opponentTeam, Inventory playerInventory, boolean automatic,
								 boolean tower) {
		this.playerTeam = playerTeam;
		this.playerInventory = playerInventory;
		this.tower = tower;
		this.automaticMode = automatic;

		// disables action buttons for the automatic mode
		if (automatic) {
			attackButton.setDisable(true);
			itemButton.setDisable(true);
			runButton.setDisable(true);
			switchButton.setDisable(true);
		}

		initializeGraphicalBattle();
		playBattleMusic();
		if (tower) {
			floorLabel.setText("Etage: NO" + towerManager.getFloorNumber());
			roomLabel.setText("Salle: " + towerManager.getCurrentRoomIndex());
		}
	}


	private void playBattleMusic() {
		try {
			java.net.URL resource = getClass().getResource("/audio/2-29. Battle! (Elite Four).mp3");
			if (resource != null) {
				mediaPlayer = new MediaPlayer(new Media(resource.toExternalForm()));
				mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
				mediaPlayer.play();
			}
		} catch (Exception e) {
			System.err.println("Failed to load battle music: " + e.getMessage());
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

		PlayerBugemonLabel.setText(playerBugemon.getName());
		PlayerLevelLabel.setText("Lv." + playerBugemon.getLevel());
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

		OpponentBugemonLabel.setText(opponentBugemon.getName());
		OpponentLevelLabel.setText("Lv." + opponentBugemon.getLevel());
		double opponentRatio = (double) opponentBugemon.getFightStats().getHp() / opponentBugemon.getBaseStats().getHp();
		OpponentHPBar.setProgress(opponentRatio);
		updateHPBarColor(OpponentHPBar, opponentRatio);
		OpponentHPNumber.setText(opponentBugemon.getHp() + " / " + opponentBugemon.getBaseStats().hp);
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

	/**
	 * Indicates whether the player is currently in a forced switch state.
	 *
	 * Typical case:
	 * - the player's active Bugémon has just been knocked out
	 * - the game therefore forces the player to choose another one
	 * - in this situation, the player must not be allowed to return
	 *   to the main battle menu
	 *
	 * @return true if the current battle state is SWAPPING, false otherwise
	 */
	private boolean isForcedSwitch() {
		return battleController != null && battleController.getState() == BattleState.SWAPPING;
	}

	/**
	 * Updates the state of the "Back" buttons in the relevant views.
	 *
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
	*
	* Here, this method is used to disable the "Back" button
	* in the Bugémons view when switching is mandatory.
	*
	* @param view the view containing the buttons to update
	* @param disabled true to disable the buttons, false to enable them again
	*/
	private void setBackButtonDisabled(VBox view, boolean disabled) {
		// Safety check: if the view does not exist, do nothing
		if (view == null) {
			return;
		}

		// Iterate through all children of the view
		for (Node child : view.getChildren()) {
			// If the child is a button, update its disabled state
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
								BattleState stateAfter = battleController.getState();
								displayMessagesSequentially(() -> {
									checkBattleState(stateAfter, event);
									if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
										handleBackToMenu(event);
									} else {
										displayInventory();
									}
								});
							// refreshAfterAction(event);
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
								BattleState stateAfter = battleController.getState();
								displayMessagesSequentially(() -> {
									checkBattleState(stateAfter, event);
									if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
										handleBackToMenu(event);
									} else {
										displayTeam();
									}
								});
							// refreshAfterAction(event);
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
								BattleState stateAfter = battleController.getState();
								displayMessagesSequentially(() -> {
									checkBattleState(stateAfter, event);
									if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
										handleBackToMenu(event);
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
			handleBackToMenu(event);
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
		attackButton.setDisable(disabled || automaticMode);
		itemButton.setDisable(disabled || automaticMode);
		runButton.setDisable(disabled || automaticMode);
		switchButton.setDisable(disabled || automaticMode);



		// Also disable subviews to prevent any interaction while waiting
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
			stopBattleMusic();
			if (!this.tower) {
				gameController.switchToBattleEndWindow(true, event);
			} else {
				gameController.switchToNextRoomWindow(event);
			}
		} else if (state == BattleState.LOST) {
			stopBattleMusic();
			gameController.switchToBattleEndWindow(false, event);
		}
	}

	private void stopBattleMusic() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.dispose();
			mediaPlayer = null;
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
		stopBattleMusic();
		switchWindow(event, MODE_WINDOW_PATH);
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
			battleLog.setText(wrapText("Quelle sera votre prochaine action ?", 35));
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
	 * Displays messages from a list one by one, advancing automatically with a 1-second delay.
	 */
	private void displayPhase(List<String> messages, int index, Runnable onComplete) {
		if (index >= messages.size()) {
			onComplete.run();
			return;
		}
		battleLog.setText(wrapText(messages.get(index), 35));
		PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(1));
		pause.setOnFinished(e -> displayPhase(messages, index + 1, onComplete));
		pause.play();
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
