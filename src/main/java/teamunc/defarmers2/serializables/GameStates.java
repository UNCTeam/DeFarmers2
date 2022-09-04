package teamunc.defarmers2.serializables;

import teamunc.defarmers2.utils.worldEdit.InGameItemsList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
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
	private ArrayList<UUID> mobsUntouchable;
	private int timeLeftInThisPhase;
	public GameStates() {
		state = GameState.WAITING_FOR_PLAYERS;
		itemsList = null;
		timeLeftInThisPhase = -1;
		mobsTargeting = new HashMap<>();
		mobsUntouchable = new ArrayList<>();
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
		mobsTargeting = new HashMap<>();
		mobsUntouchable = new ArrayList<>();
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
		if (target == null) mobsTargeting.remove(mob);
		else mobsTargeting.put(mob, target);
	}

	public ArrayList<UUID> getAllAgressor(UUID mob) {
		ArrayList<UUID> agressors = new ArrayList<>();
		for (UUID uuid : mobsTargeting.keySet()) {
			if (mobsTargeting.get(uuid).equals(mob)) agressors.add(uuid);
		}
		return agressors;
	}

	public void removeAllAgressor(UUID mob) {
		ArrayList<UUID> uuids = new ArrayList<>(mobsTargeting.keySet());
		for (UUID uuid : uuids) {
			if (mobsTargeting.get(uuid).equals(mob)) mobsTargeting.remove(uuid);
		}
	}

	public ArrayList<UUID> getMobsUntouchable() {
		return mobsUntouchable;
	}

	public void addMobUntouchable(UUID mob) {
		mobsUntouchable.add(mob);
	}

	public void removeMobUntouchable(UUID mob) {
		mobsUntouchable.remove(mob);
	}
}
