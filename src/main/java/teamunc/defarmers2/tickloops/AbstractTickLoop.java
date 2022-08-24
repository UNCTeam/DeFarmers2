package teamunc.defarmers2.tickloops;

import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.GameAnnouncer;
import teamunc.defarmers2.managers.GameManager;
import teamunc.defarmers2.serializables.GameOptions;
import teamunc.defarmers2.serializables.GameStates;

public abstract class AbstractTickLoop {

    protected Defarmers2 plugin;
    protected GameManager gameManager;
    protected GameStates gameStates;
    protected GameOptions gameOptions;
    protected GameStates.GameState phase;

    public AbstractTickLoop(GameStates.GameState phase) {
        this.plugin = Defarmers2.getInstance();
        this.gameManager = this.plugin.getGameManager();
        this.gameStates = this.gameManager.getGameStates();
        this.gameOptions = this.gameManager.getGameOptions();
        this.phase = phase;
    }

    public void onTick() {

        if (gameStates.getTimeLeftInThisPhase() <= 0) {
            this.onEnd();
        }

        // timer increase
        gameStates.setTimeLeftInThisPhase(gameStates.getTimeLeftInThisPhase() - 1);

        // actions
        actionsEachSecond();

        // ingamescoreboard actualization
        gameManager.ActualiseInGameScoreboard();
    }

    public abstract void actionsEachSecond();
    public abstract void actionsOnEnd();

    public void onEnd() {
        // new phase
        GameStates.GameState nextPhase = gameStates.getState().next();

        // announce
        GameAnnouncer.announceNextPhase(nextPhase.toString());

        // teleport
        this.gameManager.teleportPlayers(nextPhase);

        this.switchingPhase(nextPhase);
    }

    public void switchingPhase(GameStates.GameState state) {
        gameStates.setState(state);
        gameStates.setTimeLeftInThisPhase(gameOptions.getTimeForPhase(state));
        this.actionsOnEnd();
    }
}
