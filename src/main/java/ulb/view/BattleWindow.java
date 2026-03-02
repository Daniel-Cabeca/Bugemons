package ulb.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

import ulb.model.bugemon.Bugemon;
import ulb.controller.BattleController;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.model.team.Team;
import ulb.model.Player;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.ListCell;

public class BattleWindow {
	@FXML
	private GridPane playerTeamGrid;
	@FXML
	private GridPane opponentTeamGrid;
	@FXML
	private ListView<Item> inventoryList;

	private Team playerTeam;
	private Team opponentTeam;
	private Inventory playerInventory;
	private BattleController battleController;
	private Player player;

	public void setBattleController(BattleController battleController) {
		this.battleController = battleController;
	}

	public void initializeBattle(Team playerTeam, Team opponentTeam, Inventory playerInventory) {
		this.playerTeam = playerTeam;
		this.opponentTeam = opponentTeam;
		this.playerInventory = playerInventory;
		displayTeams();
		setupInventoryList();
		displayInventory();
	}

	private void displayTeams() {
		displayTeamWithStats(playerTeam, playerTeamGrid);
		displayTeamWithStats(opponentTeam, opponentTeamGrid);
	}

	private void displayTeamWithStats(Team team, GridPane grid) {
		grid.getChildren().clear();
		int row = 0;
		for (Bugemon bugemon : team.getMembers()) {
			Label label = new Label(bugemon.getName() + " HP: " + bugemon.getFightStats().getHp() + 
			" ATK: " + bugemon.getFightStats().getAttack() + " DEF: " + 
			bugemon.getFightStats().getDefense() + " INIT: " + bugemon.getFightStats().getInitiative());
			grid.add(label, 0, row++);
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
								// show updated stats and inventory
								displayTeams();
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
								Image image = new Image(getClass().getResourceAsStream("/png/objets/" + item.getSprite()));
								imageView.setImage(image);
							} catch (Exception e) {
								System.err.println("Failed to load item image: " + e.getMessage());
							}
							label.setText(item.getName() + " x" + playerInventory.getItems().get(item));
							setGraphic(hbox);
						}
					}
				};
			}
		});
	}

	private void displayInventory() {
		inventoryList.getItems().setAll(playerInventory.getItems().keySet());
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
