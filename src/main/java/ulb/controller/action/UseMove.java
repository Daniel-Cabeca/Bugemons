package ulb.controller.action;

import ulb.model.move.Move;

public class UseMove implements Action {
	private Move move;
	public UseMove(Move move){
		this.move = move;
	}
	public Move getMove(){return move;}

}
