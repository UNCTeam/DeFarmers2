package teamunc.defarmers2.serializables;

import teamunc.defarmers2.utils.worldEdit.InGameItemsList;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class GameStates implements Serializable {
	public enum GameState {
		WAITING_FOR_PLAYERS,
		PHASE1,
		PHASE2,
		PHASE3, END_GAME;

		public GameState next() {
			switch (this) {
				case WAITING_FOR_PLAYERS:
					return PHASE1;
				case PHASE1:
					return PHASE2;
				case PHASE2:
					return PHASE3;
				case PHASE3:
					return END_GAME;
				case END_GAME:
				default:
					return WAITING_FOR_PLAYERS;
			}
		}

		public boolean isInGamePhase() {
			return this == PHASE1 || this == PHASE2 || this == PHASE3;
		}

	}
	private GameState state;
	private InGameItemsList itemsList;
	private HashMap<UUID,UUID> mobsTargeting;
	private int timeLeftInThisPhase;
	public GameStates() {
		state = GameState.WAITING_FOR_PLAYERS;
		itemsList = null;
		timeLeftInThisPhase = -1;
		mobsTargeting = new HashMap<>();
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public void reset() {
		state = GameState.WAITING_FOR_PLAYERS;
		itemsList = null;
		timeLeftInThisPhase = -1;
	}

	public int getTimeLeftInThisPhase() {
		return timeLeftInThisPhase;
	}

	public void setTimeLeftInThisPhase(int timeLeftInThisPhase) {
		this.timeLeftInThisPhase = timeLeftInThisPhase;
	}

	public void setItemsList(InGameItemsList itemsList) {
		this.itemsList = itemsList;
	}

	public InGameItemsList getItemsList() {
		return itemsList;
	}

	public HashMap<UUID,UUID> getMobsTargeting() {
		return mobsTargeting;
	}

	public void removeMobTargeting(UUID mobUuid) {
		mobsTargeting.remove(mobUuid);
	}

	public UUID getMobTargeting(UUID mob) {
		return mobsTargeting.get(mob);
	}

	public boolean hasMobTargeting(UUID mob) {
		return mobsTargeting.containsKey(mob);
	}

	public void setMobTargeting(UUID mob, UUID target) {
		mobsTargeting.put(mob, target);
	}
}
