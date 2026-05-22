package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.team.TeamDTO;

import java.util.List;

public class LoadTeamPanel {

	@FXML
	public Accordion savedTeamsAccordion;

	private ViewListener viewListener;
	private TeamDTO selectedTeam;

	public void setViewListener(ViewListener viewListener) { this.viewListener = viewListener; }

	/**
	 * Displays the player's saved teams in the accordion.
	 *
	 * @param savedTeams the player's saved teams
	 */
	public void populateSavedTeams(List<TeamDTO> savedTeams) {
		savedTeamsAccordion.getPanes().clear();
		for (TeamDTO team : savedTeams) {
			TitledPane pane = createTeamPane(team);
			savedTeamsAccordion.getPanes().add(pane);
			pane.setOnMouseClicked(e -> selectedTeam = team);
		}
	}

	/**
	 * Creates a TitledPane for a team with the name as title and member bugemon names and sprites as content.
	 *
	 * @param team the team to display
	 * @return the constructed TitledPane
	 */
	private TitledPane createTeamPane(TeamDTO team) {
		HBox teamBugemonsBox = new HBox(12);
		for (BugemonDTO bugemon : team.members()) {
			VBox cell = new VBox(4);
			Image image = new Image(bugemon.getSpritePath());
			ImageView sprite = new ImageView(image);
			sprite.setFitWidth(64);
			sprite.setFitHeight(64);
			sprite.setPreserveRatio(true);

			Label name = new Label(bugemon.getName());

			cell.getChildren().addAll(sprite, name);
			teamBugemonsBox.getChildren().add(cell);
		}
		return new TitledPane(team.teamName(), teamBugemonsBox);
	}


	/**
	 * Handles clicking the load button.
	 */
	@FXML
	public void handleLoadTeam() {
		if (selectedTeam != null) {
			viewListener.onLoadTeam(selectedTeam);
		}
	}

	/**
	 * Handles clicking the return button.
	 */
	@FXML
	public void handleReturn() {
		viewListener.onReturn();
	}

	public interface ViewListener {
		/**
		 * Notifies the listener to load the given team.
		 * @param selectedTeam the team
		 */
		void onLoadTeam(TeamDTO selectedTeam);
		
		/**
		 * Handles return action.
		 */
		void onReturn();
	}

}
