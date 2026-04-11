package ulb.controller.windows;

import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.controller.BattleModeController;
import ulb.controller.ClientController;
import ulb.message.clientToServer.GetAllBugemonSpeciesMessage;
import ulb.message.clientToServer.SetUpTeamMessage;
import ulb.message.serverToClient.BugemonSpeciesMessage;
import ulb.view.WindowPath;
import ulb.view.windows.CreateTeamWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TeamController extends WindowController<CreateTeamWindow> implements CreateTeamWindow.ViewListener {
	private BattleModeController battleModeController;

	public TeamController(Stage stage, ClientController clientController) {
		super(stage,WindowPath.CREATE_TEAM ,clientController);
		this.view.setViewListener(this);
	}

	@Override
	public void show() {
		view.displayAvailableBugemons(this.getAllSpecies());
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
		this.clientController.setPlayerTeam(teamABugemons);
	}

	@Override
	public void onConfirmTeam(List<String> selectedBugemonIds) { // TO CHANGE
		setTeam(selectedBugemonIds);
		List<BugemonDTO> team = clientController.getPlayerTeam();

		if (!this.clientController.postData(new SetUpTeamMessage(team))){
			return;
		}
		this.battleModeController = new BattleModeController(this.stage, this.clientController, clientController.getPlayerTeam());
		try {
			battleModeController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onReturn() {
		clientController.showModeController();
	}

	@Override
	public List<BugemonSpeciesDTO> getAllSpecies() {
		Serializable message = this.clientController.getData(new GetAllBugemonSpeciesMessage());

		if (message instanceof BugemonSpeciesMessage speciesMessage){
			return speciesMessage.getSpecies();
		}
		return null;
	}

}
