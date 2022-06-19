package teamunc.defarmers2.serializables;

import teamunc.defarmers2.utils.worldEdit.ApiWorldEdit;
import teamunc.defarmers2.utils.worldEdit.InGameItemsList;

import java.io.Serializable;

public class GameStates implements Serializable {
	public enum GameState {
		WAITING_FOR_PLAYERS,
		PHASE1,
		PHASE2,
		PHASE3;

		public GameState next() {
			switch (this) {
				case WAITING_FOR_PLAYERS:
					return PHASE1;
				case PHASE1:
					return PHASE2;
				case PHASE2:
					return PHASE3;
				case PHASE3:
					default:
					return WAITING_FOR_PLAYERS;
			}
		}

	}
	private GameState state;
	private InGameItemsList itemsList;
	private int TimeInSecondPastInThisPhase;
	public GameStates() {
		state = GameState.WAITING_FOR_PLAYERS;
		TimeInSecondPastInThisPhase = -1;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public void reset() {
		state = GameState.WAITING_FOR_PLAYERS;
		TimeInSecondPastInThisPhase = -1;
	}

	public int getTimeInSecondPastInThisPhase() {
		return TimeInSecondPastInThisPhase;
	}

	public void setTimeInSecondPastInThisPhase(int timeInSecondPastInThisPhase) {
		TimeInSecondPastInThisPhase = timeInSecondPastInThisPhase;
	}

	public void setItemsList(InGameItemsList itemsList) {
		this.itemsList = itemsList;
	}

	public InGameItemsList getItemsList() {
		return itemsList;
	}
}
