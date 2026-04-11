package ulb.controller.windows;

import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.controller.ClientController;
import ulb.view.WindowPath;
import ulb.view.windows.CreateTeamWindow;

import java.util.ArrayList;
import java.util.List;

public class TeamController extends WindowController<CreateTeamWindow> implements CreateTeamWindow.ViewListener {

	private final ClientController clientController;

	public TeamController(Stage stage, ClientController clientController) {
		this.stage = stage;
		this.clientController = clientController;
		this.windowPath = WindowPath.CREATE_TEAM;

		try {
			this.init();
		} catch (Exception e) {
			System.err.println("Couldn't load the FXML file : " + e);
		}
		this.view.setViewListener(this);
	}

	@Override
	public void show() {
		view.displayAvailableBugemons(clientController.getAllSpecies());
		super.show();
		view.populateAvailableBugemons();
	}

	public void setTeam(List<String> selectedBugemonsId) {
		List<BugemonDTO> teamABugemons = new ArrayList<>();
		List<BugemonSpeciesDTO> allSpecies = this.getAllSpecies();
		for (String bugemonId : selectedBugemonsId) {
			for (BugemonSpeciesDTO species : allSpecies) {
				if (bugemonId.equals(species.getId())) {
					teamABugemons.add(new BugemonDTO(species));
				}
			}
		}
		this.clientController.getPlayer().setTeam(teamABugemons);
	}

	@Override
	public void onConfirmTeam(List<String> selectedBugemonIds) {
		setTeam(selectedBugemonIds);
		clientController.onTeamConfirmed();
	}

	@Override
	public void onReturn() {
		clientController.onReturn();
	}

	@Override
	public List<BugemonSpeciesDTO> getAllSpecies() {
		return clientController.getAllSpecies();
	}

}
