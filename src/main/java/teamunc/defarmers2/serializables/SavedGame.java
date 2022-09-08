package teamunc.defarmers2.serializables;

import java.io.Serializable;

public class SavedGame implements Serializable {
    private final GameStates gameStates;
    private final TeamsStates teamsStates;

    public SavedGame(GameStates gameStates, TeamsStates teamsStates) {
        this.gameStates = gameStates;
        this.teamsStates = teamsStates;
    }
}
