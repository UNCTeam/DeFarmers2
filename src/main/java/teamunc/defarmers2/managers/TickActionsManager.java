package teamunc.defarmers2.managers;

import teamunc.defarmers2.serializables.GameStates;
import teamunc.defarmers2.tickloops.*;

import java.util.HashMap;

public class TickActionsManager extends Manager{

    // SINGLETON
    private static TickActionsManager instance;
    public static TickActionsManager getInstance() {
        return instance;
    }

    private HashMap<GameStates.GameState, AbstractTickLoop> tickLoops;
    public void registerTickLoop(GameStates.GameState state, AbstractTickLoop tickLoop) {
        tickLoops.put(state,tickLoop);
    }

    public static void enable() {
        instance = new TickActionsManager();
    }

    @Override
    public void onInit() {
        instance.tickLoops = new HashMap<>();
        registerTickLoop(GameStates.GameState.PHASE1, new TickPhase1());
        registerTickLoop(GameStates.GameState.PHASE2, new TickPhase2());
        registerTickLoop(GameStates.GameState.PHASE3, new TickPhase3());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public int getImportance() {
        return 4;
    }

    public void onTick() {
        GameManager gameManager = plugin.getGameManager();

        if (gameManager.getGameStates().getState() == GameStates.GameState.WAITING_FOR_PLAYERS) {
            this.plugin.getGameManager().stopGame();
            return;
        }

        for (GameStates.GameState state : tickLoops.keySet()) {
            if (gameManager.getGameStates().getState() == state) {
                 tickLoops.get(state).onTick();
            }
        }
    }

    public int getHourLeft() {
        int secPasse = plugin.getGameManager().getGameStates().getTimeLeftInThisPhase();

        return (secPasse - (secPasse % 3600)) / 3600;
    }

    public int getMinuteLeft() {
        int secPasse = plugin.getGameManager().getGameStates().getTimeLeftInThisPhase();

        return (secPasse % 3600 - (secPasse % 60) )/ 60;
    }

    public int getSecondLeft() {
        int secPasse = plugin.getGameManager().getGameStates().getTimeLeftInThisPhase();

        return secPasse % 60;
    }

}
