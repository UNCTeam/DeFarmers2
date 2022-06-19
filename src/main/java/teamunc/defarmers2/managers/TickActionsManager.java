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
        int secMax = plugin.getGameManager().getGameOptions().getTimeForPhase(plugin.getGameManager().getGameStates().getState());
        int secPasse = plugin.getGameManager().getGameStates().getTimeInSecondPastInThisPhase();

        int totalSec = secMax - secPasse;

        int heureRestante = (totalSec - (totalSec % 3600)) / 3600;

        return heureRestante;
    }

    public int getMinuteLeft() {
        int secMax = plugin.getGameManager().getGameOptions().getTimeForPhase(plugin.getGameManager().getGameStates().getState());
        int secPasse = plugin.getGameManager().getGameStates().getTimeInSecondPastInThisPhase();

        int totalSec = secMax - secPasse;

        int minuteRestante = (totalSec % 3600 - (totalSec % 60) )/ 60;

        return minuteRestante;
    }

    public int getSecondLeft() {
        int secMax = plugin.getGameManager().getGameOptions().getTimeForPhase(plugin.getGameManager().getGameStates().getState());
        int secPasse = plugin.getGameManager().getGameStates().getTimeInSecondPastInThisPhase();

        int totalSecondes = secMax - secPasse;

        int secondRestante = totalSecondes % 60;

        return secondRestante;
    }

}
