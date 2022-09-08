package teamunc.defarmers2.serializables;

import java.io.Serializable;

public interface SavedGame extends Serializable {
    GameStates gameStates = null;
    TeamsStates teamsStates = null;

}
