package ulb.service.strategy;

import ulb.model.action.Action;
import ulb.model.battle.Battle;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class simulating user input for battles against the computer.
 */
public class AI extends Thread {
	/** Object used for logging runtime information to the console or to a log file. */
	private static final Logger LOGGER = Logger.getLogger(AI.class.getName());
	/** Duration in milliseconds to wait between two actions. */
	private final long SLEEP_TIME = 1000;
	/** Current battle instance. */
	private final Battle battle;
	/** The logic for picking battle actions. */
	private final Strategy strategy;
	/** The team of the AI. */
	private final Battle.ParticipantLabel teamLabel = Battle.ParticipantLabel.TEAM_B;

	public AI(Battle battle, Strategy strategy) {
		this.battle = battle;
		this.strategy = strategy;
	}

	@Override
	public void run() {
		this.play();
	}

	/**
	 * Plays the game loop in the automatic battle mode
	 */
	public void play() {
		while (!this.battle.isGameFinished()) {
			this.playAutoTurn();
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				LOGGER.log(Level.WARNING, "AI interrupted while waiting for the next automatic turn.");
				return;
			}
		}
	}

	/**
	 * Plays a turn in the automatic battle mode
	 */
	public void playAutoTurn() {
		Action action = strategy.pickAction(battle, teamLabel);

		if (action != null) {
			battle.chooseAction(action, teamLabel);
			battle.readyToPlay(teamLabel);
		}
	}
}